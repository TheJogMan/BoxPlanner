package globalResources.ui.uiComponents;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Iterator;

import globalResources.graphics.Canvas;
import globalResources.graphics.Drawable;
import globalResources.ui.UIComponent;
import globalResources.ui.UIHolder;
import globalResources.utilities.VectorInt;

/**
 * Switch component
 * <p>
 * Used to allow the user to control a boolean state
 * </p>
 */
public class Switch extends UIComponent
{
	boolean state;
	
	ArrayList<Runnable> changeListeners;
	
	/**
	 * Creates a new Switch
	 * @param parent the UIHolder to add the Switch to
	 * @param position the position of the Switch
	 * @param dimensions the dimensions of the Switch
	 */
	public Switch(UIHolder parent, VectorInt position, VectorInt dimensions)
	{
		super(parent, position, dimensions);
		changeListeners = new ArrayList<Runnable>();
		state = true;
		super.registerListener(new InputListener());
	}
	
	public void addChangeListener(Runnable runnable)
	{
		changeListeners.add(runnable);
	}
	
	@Override
	protected void update()
	{
		
	}
	
	@Override
	protected void render(Drawable canvas)
	{
		Canvas drawable = canvas.getCanvas();
		drawable.drawRect(0, 0, dimensions.getX() / 2, dimensions.getY(), Color.RED);
		drawable.drawRect(dimensions.getX() / 2, 0, dimensions.getX() / 2, dimensions.getY(), Color.GREEN);
		int dx = 0;
		if (!state)
		{
			dx += dimensions.getX() / 2;
		}
		drawable.drawRect(dx, 0, dimensions.getX() / 2, dimensions.getY(), Color.BLUE);
	}
	
	/**
	 * Set the current state of the switch
	 * @param state new state
	 */
	public void setState(boolean state)
	{
		this.state = state;
	}
	
	/**
	 * Get the current state of the switch
	 * @return current state
	 */
	public boolean getState()
	{
		return state;
	}
	
	class InputListener extends ComponentListener
	{
		@Override
		public void clicked()
		{
			state = !state;
			for (Iterator<Runnable> iterator = changeListeners.iterator(); iterator.hasNext();) iterator.next().run();
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
}