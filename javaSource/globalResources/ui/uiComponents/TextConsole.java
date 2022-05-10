package globalResources.ui.uiComponents;

import java.util.ArrayList;
import java.util.Iterator;

import globalResources.graphics.Canvas;
import globalResources.graphics.Drawable;
import globalResources.richText.RichCharacter;
import globalResources.richText.RichString;
import globalResources.richText.RichStringBuilder;
import globalResources.ui.UIComponent;
import globalResources.ui.UIHolder;
import globalResources.utilities.ShiftingLinkedList;
import globalResources.utilities.VectorInt;

public class TextConsole extends UIComponent
{
	ShiftingLinkedList<? extends ConsoleEntry> messageHistory;
	
	int scroll = 0;
	
	protected TextConsole(UIHolder parent, VectorInt position, VectorInt dimensions)
	{
		super(parent, position, dimensions);
		messageHistory = new ShiftingLinkedList<RichStringEntry>();
	}
	
	public ShiftingLinkedList<? extends ConsoleEntry> getMessageHistory()
	{
		return messageHistory;
	}
	
	public void setMessageHistory(ShiftingLinkedList<? extends ConsoleEntry> messageHistory)
	{
		this.messageHistory = messageHistory;
	}
	
	@Override
	protected void update()
	{
		
	}
	
	@Override
	protected void render(Drawable drawable)
	{
		Canvas canvas = drawable.getCanvas();
		int y = scroll;
		for (Iterator<? extends ConsoleEntry> iterator = messageHistory.reverseIterator(); iterator.hasNext();)
		{
			ConsoleEntry entry = iterator.next();
			ArrayList<RichString> lines = entry.getRichStringLines();
			y -= canvas.getFontHeight() * lines.size();
			int dy = y;
			for (Iterator<RichString> lineIterator = lines.iterator(); lineIterator.hasNext();)
			{
				canvas.drawText(lineIterator.next(), 0, dy);
				dy += canvas.getFontHeight();
			}
		}
	}
	
	public static interface ConsoleEntry
	{
		public RichString getRichString();
		public ArrayList<RichString> getRichStringLines();
		
		public default String getRawString()
		{
			return getRichString().toString();
		}
		
		public default ArrayList<String> getRawStringLines()
		{
			ArrayList<String> rawLines = new ArrayList<String>();
			for (Iterator<RichString> iterator = getRichStringLines().iterator(); iterator.hasNext();) rawLines.add(iterator.next().toString());
			return rawLines;
		}
	}
	
	public static class RichStringEntry implements ConsoleEntry
	{
		RichString string;
		
		public RichStringEntry(RichString string)
		{
			this.string = string;
		}
		
		@Override
		public RichString getRichString()
		{
			return string;
		}
		
		public void setRichString(RichString string)
		{
			this.string = string;
		}
		
		@Override
		public ArrayList<RichString> getRichStringLines()
		{
			ArrayList<RichString> lines = new ArrayList<RichString>();
			RichStringBuilder builder = new RichStringBuilder();
			for (int index = 0; index < string.length(); index++)
			{
				RichCharacter ch = string.charAt(index);
				if (ch.getPrimitiveCharacter() == '\n')
				{
					lines.add(builder.build());
					builder.clear();
				}
				else builder.append(ch);
			}
			lines.add(builder.build());
			return lines;
		}
	}
}