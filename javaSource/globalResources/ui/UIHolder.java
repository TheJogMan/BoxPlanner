package globalResources.ui;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import globalResources.graphics.Canvas;

/**
 * Contains UI components
 */
public abstract class UIHolder
{
	protected ArrayList<UIComponent> uiComponents;
	protected ArrayList<UIComponent> secondLayerUI;
	protected Canvas canvas;
	protected FrameInput inputSource;
	
	protected UIHolder(Canvas canvas, FrameInput inputSource)
	{
		this.canvas = canvas;
		this.inputSource = inputSource;
		uiComponents = new ArrayList<UIComponent>();
		secondLayerUI = new ArrayList<UIComponent>();
	}
	
	/**
	 * Gets the FrameInput used by this UIHolder
	 * @return this UIHolder's FrameInput
	 */
	public FrameInput getInput()
	{
		return inputSource;
	}
	
	/**
	 * Gets the canvas for this UIHolder
	 * @return this UIHolder's canvas
	 */
	public Canvas getCanvas()
	{
		return canvas;
	}
	
	protected void updateUI()
	{
		List<UIComponent> removedComponents = new ArrayList<UIComponent>();
		int count = uiComponents.size();
		for (int index = 0; index < count; index++)
		{
			UIComponent component = uiComponents.get(index);
			
			component.run();
			
			if (component.isRemoved())
			{
				removedComponents.add(component);
			}
		}
		for (Iterator<UIComponent> iterator = removedComponents.iterator(); iterator.hasNext();)
		{
			UIComponent component = iterator.next();
			component.dispose();
			uiComponents.remove(component);
		}
		
		removedComponents = new ArrayList<UIComponent>();
		count = secondLayerUI.size();
		for (int index = 0; index < count; index++)
		{
			UIComponent component = secondLayerUI.get(index);
			
			component.run();
			
			if (component.isRemoved())
			{
				removedComponents.add(component);
			}
		}
		for (Iterator<UIComponent> iterator = removedComponents.iterator(); iterator.hasNext();)
		{
			UIComponent component = iterator.next();
			component.dispose();
			secondLayerUI.remove(component);
		}
	}
	
	protected void renderUI()
	{
		int count = uiComponents.size();
		for (int index = 0; index < count; index++)
		{
			uiComponents.get(index).draw();
		}
	}
	
	protected void renderSecondLayerUI()
	{
		int count = secondLayerUI.size();
		for (int index = 0; index < count; index++)
		{
			secondLayerUI.get(index).draw();
		}
	}
	
	protected void clearUI()
	{
		List<UIComponent> removedComponents = new ArrayList<UIComponent>();
		int count = uiComponents.size();
		for (int index = 0; index < count; index++)
		{
			removedComponents.add(uiComponents.get(index));
		}
		for (Iterator<UIComponent> iterator = removedComponents.iterator(); iterator.hasNext();)
		{
			iterator.next().dispose();
		}
		
		removedComponents = new ArrayList<UIComponent>();
		count = secondLayerUI.size();
		for (int index = 0; index < count; index++)
		{
			removedComponents.add(secondLayerUI.get(index));
		}
		for (Iterator<UIComponent> iterator = removedComponents.iterator(); iterator.hasNext();)
		{
			iterator.next().dispose();
		}
	}
}