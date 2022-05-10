package globalResources.ui.uiComponents;

import java.awt.Color;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.util.ArrayList;
import java.util.Iterator;

import globalResources.graphics.Canvas;
import globalResources.graphics.Drawable;
import globalResources.ui.FrameInput.FrameInputListenerInterface;
import globalResources.utilities.VectorInt;
import globalResources.ui.UIComponent;
import globalResources.ui.UIHolder;

public class TextField extends UIComponent
{
	VectorInt scroll;
	String text;
	
	int cursorPos;
	int selectionPos;
	int selectionLength;
	boolean selected;
	boolean permSelected;
	
	ArrayList<Runnable> textChangeListeners;
	
	public TextField(UIHolder parent, VectorInt position, VectorInt dimensions)
	{
		super(parent, position, dimensions);
		super.registerListener(new ComponentInputListener());
		super.addFrameListener(new FrameInputListener());
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
		selected = false;
		permSelected = false;
	}
	
	public void setPermSelected(boolean selected)
	{
		this.permSelected = selected;
	}
	
	public boolean permSelected()
	{
		return permSelected;
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
	
	@Override
	protected void update()
	{
		
	}
	
	private void changeText(String newText)
	{
		this.text = newText;
		for (Iterator<Runnable> iterator = textChangeListeners.iterator(); iterator.hasNext();) iterator.next().run();
	}
	
	@Override
	protected void render(Drawable drawable)
	{
		Canvas canvas = drawable.getCanvas();
		canvas.drawRect(0, 0, dimensions.getX(), dimensions.getY(), Color.WHITE);
		int x = scroll.getX();
		int y = scroll.getY();
		if (cursorPos == -1) canvas.drawLine(x, y, x, y + canvas.getFontHeightR(), Color.BLACK);
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
				if (index == cursorPos)
				{
					canvas.drawLine(x, y, x, y + canvas.getFontHeightR(), Color.BLACK);
				}
			}
		}
		if (cursorPos == text.length()) canvas.drawLine(x, y, x, y + canvas.getFontHeightR(), Color.BLACK);
	}
	
	class ComponentInputListener extends ComponentListener
	{
		@Override
		public void clicked()
		{
			
		}
		
		@Override
		public void unClicked()
		{
			
		}
		
		@Override
		public void hovered()
		{
			
		}
		
		@Override
		public void unHovered()
		{
			
		}
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
	
	class FrameInputListener implements FrameInputListenerInterface
	{
		@Override
		public void buttonPressEvent(MouseEvent event)
		{
			selected = hovered;
		}
		
		@Override
		public void buttonReleaseEvent(MouseEvent event)
		{
			
		}
		
		@Override
		public void keyPressEvent(KeyEvent event)
		{
			if (selected || permSelected)
			{
				if (event.getKeyCode() == KeyEvent.VK_LEFT)
				{
					if (cursorPos > -1) cursorPos--;
				}
				else if (event.getKeyCode() == KeyEvent.VK_RIGHT)
				{
					if (cursorPos < text.length()) cursorPos++;
				}
			}
		}
		
		@Override
		public void keyReleaseEvent(KeyEvent event)
		{
			
		}
		
		@Override
		public void textEditEvent(KeyEvent event)
		{
			if (selected || permSelected)
			{
				int keyCode = KeyEvent.getExtendedKeyCodeForChar(event.getKeyChar());
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
		}
		
		@Override
		public void textInputEvent(KeyEvent event)
		{
			if (selected || permSelected)
			{
				String[] segments = splitAroundCursor();
				cursorPos++;
				changeText(segments[0] + event.getKeyChar() + segments[1]);
			}
		}
		
		@Override
		public void mouseScrollEvent(MouseWheelEvent event)
		{
			
		}
		
		@Override
		public void mouseMoveEvent(VectorInt delta, VectorInt newPosition)
		{
			
		}
		
		@Override
		public void mouseDragEvent(VectorInt delta, VectorInt newPosition)
		{
			
		}
	}
}