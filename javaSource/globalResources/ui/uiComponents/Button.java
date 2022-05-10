package globalResources.ui.uiComponents;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import globalResources.graphics.Canvas;
import globalResources.graphics.Drawable;
import globalResources.ui.UIComponent;
import globalResources.ui.UIHolder;
import globalResources.utilities.VectorInt;

/**
 * Button component
 * <p>
 * Allows the user to initiate an action
 * </p>
 */
public class Button extends UIComponent
{
	static Color normalColor = new Color(255, 255, 255);
	static Color hoveredColor = new Color(120, 120, 120);
	static Color clickedColor = new Color(60, 60, 60);
	static Color borderColor = new Color(0, 0, 0);
	static Color textColor = new Color(0, 0, 0);
	
	String text;
	
	List<Runnable> clickEvents;
	List<Runnable> releaseEvents;
	
	/**
	 * Creates a new button
	 * @param parent the UIHolder to add the button to
	 * @param position the position of the button
	 * @param dimensions the size of the button
	 * @param text the button's label
	 */
	public Button(UIHolder parent, VectorInt position, VectorInt dimensions, String text)
	{
		super(parent, position, dimensions);
		super.registerListener(new InputListener());
		this.text = text;
		clickEvents = new ArrayList<Runnable>();
		releaseEvents = new ArrayList<Runnable>();
	}
	
	/**
	 * Register a Runnable to be called when the button is clicked
	 * @param event the Runnable to be called
	 */
	public void registerClickEvent(Runnable event)
	{
		clickEvents.add(event);
	}
	
	/**
	 * Register a Runnable to be called when the button is released
	 * @param event the Runnable to be called
	 */
	public void registerReleaseEvent(Runnable event)
	{
		releaseEvents.add(event);
	}
	
	@Override
	protected void update()
	{
		
	}
	
	@Override
	protected void render(Drawable canvas)
	{
		Canvas drawable = canvas.getCanvas();
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
	
	class InputListener extends ComponentListener
	{
		@Override
		public void clicked()
		{
			for (Iterator<Runnable> iterator = clickEvents.iterator(); iterator.hasNext();) iterator.next().run();
		}
		
		@Override
		public void unClicked()
		{
			for (Iterator<Runnable> iterator = releaseEvents.iterator(); iterator.hasNext();) iterator.next().run();
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
