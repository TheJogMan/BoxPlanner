package globalResources.newUI.components;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import globalResources.graphics.Canvas;
import globalResources.newUI.NUIComponent;
import globalResources.newUI.NUIHolder;
import globalResources.newUI.event.NUIEvent;
import globalResources.utilities.VectorInt;

public class NUIButton extends NUIComponent
{
	static Color normalColor = new Color(255, 255, 255);
	static Color hoveredColor = new Color(120, 120, 120);
	static Color clickedColor = new Color(60, 60, 60);
	static Color borderColor = new Color(0, 0, 0);
	static Color textColor = new Color(0, 0, 0);
	
	String text;
	boolean clicked;
	
	List<Runnable> clickEvents;
	List<Runnable> releaseEvents;
	
	public NUIButton(NUIHolder parent, VectorInt position, VectorInt dimensions, String text)
	{
		super(parent, position, dimensions);
		this.text = text;
		clickEvents = new ArrayList<Runnable>();
		releaseEvents = new ArrayList<Runnable>();
		clicked = false;
	}
	
	@Override
	public void keyPress(int key)
	{
		
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
	protected void componentDraw(Canvas canvas)
	{
		Canvas drawable = canvas;
		drawable.drawRect(0, 0, dimensions.getX(), dimensions.getY(), borderColor);
		Color backgroundColor = normalColor;
		if (clicked())
		{
			backgroundColor = clickedColor;
		}
		else if (hovered())
		{
			backgroundColor = hoveredColor;
		}
		drawable.drawRect(1, 1, dimensions.getX() - 2, dimensions.getY() - 2, backgroundColor);
		drawable.drawTextR(text, 2, 2, textColor);
	}
	
	@Override
	protected void componentUpdate()
	{
		if (clicked && !clicked())
		{
			clicked = false;
			for (Iterator<Runnable> iterator = releaseEvents.iterator(); iterator.hasNext();) iterator.next().run();
		}
		else if (!clicked && clicked())
		{
			clicked = true;
			for (Iterator<Runnable> iterator = clickEvents.iterator(); iterator.hasNext();) iterator.next().run();
		}
	}
	
	public void registerClickEvent(Runnable event)
	{
		clickEvents.add(event);
	}
	
	public void registerReleaseEvent(Runnable event)
	{
		releaseEvents.add(event);
	}
	
	@Override
	public void handle(NUIEvent event)
	{
		
	}

	@Override
	public void textInput(int key, char chr)
	{
		
	}

	@Override
	public void textEdit(int key, char chr)
	{
		
	}
}