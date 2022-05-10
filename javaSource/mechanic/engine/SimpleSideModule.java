package mechanic.engine;

import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;

import globalResources.utilities.VectorInt;

public abstract class SimpleSideModule extends SideModule
{
	public SimpleSideModule()
	{
		
	}
	
	public SimpleSideModule(boolean persistent)
	{
		super(persistent);
	}
	
	@Override
	public void postRender()
	{
		
	}
	
	@Override
	public void buttonPressEvent(MouseEvent event)
	{
		
	}
	
	@Override
	public void buttonReleaseEvent(MouseEvent event)
	{
		
	}
	
	@Override
	public void keyPressEvent(KeyEvent event)
	{
		
	}
	
	@Override
	public void keyReleaseEvent(KeyEvent event)
	{
		
	}
	
	@Override
	public void textEditEvent(KeyEvent event)
	{
		
	}
	
	@Override
	public void textInputEvent(KeyEvent event)
	{
		
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