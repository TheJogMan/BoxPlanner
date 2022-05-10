package globalResources.newUI.components;

import java.awt.Color;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Iterator;

import globalResources.graphics.Canvas;
import globalResources.newUI.NUIComponent;
import globalResources.newUI.NUIHolder;
import globalResources.newUI.event.NUIEvent;
import globalResources.utilities.VectorInt;

public class NUISwitch extends NUIComponent
{
	boolean state;
	
	ArrayList<Runnable> changeListeners;
	
	public NUISwitch(NUIHolder parent, VectorInt position, VectorInt dimensions)
	{
		super(parent, position, dimensions);
		changeListeners = new ArrayList<Runnable>();
		state = true;
	}
	
	public void addChangeListener(Runnable runnable)
	{
		changeListeners.add(runnable);
	}
	
	public void setState(boolean state)
	{
		this.state = state;
	}
	
	public boolean getState()
	{
		return state;
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
		if (button == MouseEvent.BUTTON1)
		{
			state = !state;
			for (Iterator<Runnable> iterator = changeListeners.iterator(); iterator.hasNext();) iterator.next().run();
		}
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
		
	}
	
	@Override
	public void textEdit(int key, char chr)
	{
		
	}
	
	@Override
	public void handle(NUIEvent event)
	{
		
	}
	
	@Override
	protected void componentDraw(Canvas canvas)
	{
		canvas.drawRect(0, 0, dimensions.getX() / 2, dimensions.getY(), Color.RED);
		canvas.drawRect(dimensions.getX() / 2, 0, dimensions.getX() / 2, dimensions.getY(), Color.GREEN);
		int dx = 0;
		if (!state)
		{
			dx += dimensions.getX() / 2;
		}
		canvas.drawRect(dx, 0, dimensions.getX() / 2, dimensions.getY(), Color.BLUE);
	}
	
	@Override
	protected void componentUpdate()
	{
		
	}
}