package globalResources.newUI.components;

import java.awt.Color;

import globalResources.graphics.Canvas;
import globalResources.newUI.NUIComponent;
import globalResources.newUI.NUIHolder;
import globalResources.newUI.event.NUIEvent;
import globalResources.utilities.VectorInt;

public class NUIContainer extends NUIComponent
{
	public NUIContainer(NUIHolder parent, VectorInt position, VectorInt dimensions)
	{
		super(parent, position, dimensions);
	}
	
	public void setBackgroundColor(Color color)
	{
		getDrawable().getCanvas().setClearColor(color);
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
		
	}
	
	@Override
	protected void componentUpdate()
	{
		
	}
}