package globalResources.newUI.components;

import java.awt.Color;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Iterator;

import globalResources.graphics.Canvas;
import globalResources.newUI.NUIComponent;
import globalResources.newUI.NUIHolder;
import globalResources.newUI.event.NUIEvent;
import globalResources.utilities.VectorInt;

public class NUITextField extends NUIComponent
{
	VectorInt scroll;
	String text;
	
	int cursorPos;
	int selectionPos;
	int selectionLength;
	
	ArrayList<Runnable> textChangeListeners;
	
	public NUITextField(NUIHolder parent, VectorInt position, VectorInt dimensions)
	{
		super(parent, position, dimensions);
		textChangeListeners = new ArrayList<Runnable>();
		reset();
	}
	
	void reset()
	{
		scroll = new VectorInt(0, 0);
		text = "";
		cursorPos = -1;
		selectionPos = 0;
		selectionLength = 0;
	}
	
	public String getText()
	{
		return text;
	}
	
	public void setText(String text)
	{
		this.text = text;
		if (cursorPos > text.length()) cursorPos = text.length();
	}
	
	public void addTextChangeListener(Runnable listener)
	{
		textChangeListeners.add(listener);
	}
	
	private void changeText(String newText)
	{
		this.text = newText;
		for (Iterator<Runnable> iterator = textChangeListeners.iterator(); iterator.hasNext();) iterator.next().run();
	}
	
	private String[] splitAroundCursor()
	{
		String[] segments = {"", ""};
		if (text.length() > 0)
		{
			if (cursorPos == -1) segments[1] = text;
			else if (cursorPos == text.length()) segments[0] = text;
			else
			{
				segments[0] = text.substring(0, cursorPos + 1);
				segments[1] = text.substring(cursorPos + 1);
			}
		}
		return segments;
	}
	
	@Override
	public void keyPress(int key)
	{
		if (key == KeyEvent.VK_LEFT)
		{
			if (cursorPos > -1) cursorPos--;
		}
		else if (key == KeyEvent.VK_RIGHT)
		{
			if (cursorPos < text.length()) cursorPos++;
		}
	}
	
	@Override
	public void keyRelease(int key)
	{
		
	}
	
	@Override
	public void buttonPress(int button)
	{
		
	}
	
	@Override
	public void buttonRelease(int button)
	{
		
	}
	
	@Override
	public void curseorMove(VectorInt start, VectorInt stop)
	{
		
	}
	
	@Override
	public void cursorDrag(VectorInt start, VectorInt stop)
	{
		
	}
	
	@Override
	public void scroll(int amount)
	{
		
	}
	
	@Override
	public void textInput(int key, char chr)
	{
		String[] segments = splitAroundCursor();
		cursorPos++;
		changeText(segments[0] + chr + segments[1]);
	}
	
	@Override
	public void textEdit(int key, char chr)
	{
		int keyCode = KeyEvent.getExtendedKeyCodeForChar(chr);
		if (keyCode == KeyEvent.VK_BACK_SPACE && cursorPos > -1)
		{
			String[] segments = splitAroundCursor();
			cursorPos--;
			if (segments[0].length() > 0) segments[0] = segments[0].substring(0, segments[0].length() - 1);
			changeText(segments[0] + segments[1]);
		}
		else if (keyCode == KeyEvent.VK_DELETE && cursorPos < text.length())
		{
			String[] segments = splitAroundCursor();
			if (segments[1].length() == 1) segments[1] = "";
			else if (segments[1].length() > 1) segments[1] = segments[1].substring(1);
			changeText(segments[0] + segments[1]);
		}
	}
	
	@Override
	public void handle(NUIEvent event)
	{
		
	}
	
	@Override
	protected void componentDraw(Canvas canvas)
	{
		canvas.drawRect(0, 0, dimensions.getX(), dimensions.getY(), Color.WHITE);
		int x = scroll.getX() + 3;
		int y = scroll.getY() + 3;
		if (cursorPos == -1 && selected()) canvas.drawLine(x, y, x, y + canvas.getFontHeightR(), Color.BLACK);
		for (int index = 0; index < text.length(); index++)
		{
			char ch = text.charAt(index);
			if (ch == '\n')
			{
				y += canvas.getFontHeightR();
				x = scroll.getX();
			}
			else
			{
				canvas.drawTextR("" + ch, x, y, Color.BLACK);
				x += canvas.getStringLengthR("" + ch);
				if (index == cursorPos && selected())
				{
					canvas.drawLine(x, y, x, y + canvas.getFontHeightR(), Color.BLACK);
				}
			}
		}
		if (cursorPos == text.length() && selected()) canvas.drawLine(x, y, x, y + canvas.getFontHeightR(), Color.BLACK);
	}
	
	@Override
	protected void componentUpdate()
	{
		
	}
}