package globalResources.ui.uiComponents;

import java.awt.Color;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;

import globalResources.graphics.Canvas;
import globalResources.graphics.Drawable;
import globalResources.ui.UIComponent;
import globalResources.ui.UIHolder;
import globalResources.utilities.VectorInt;

public class Container extends UIComponent
{
	HashMap<UIComponent, String> labels;
	int gapSize;
	int boarderThickness;
	boolean doBoarder = true;
	
	public Container(UIHolder parent, VectorInt position, VectorInt dimensions, int gapSize)
	{
		super(parent, position, dimensions);
		this.gapSize = gapSize;
		boarderThickness = 3;
		canvas.setClearColor(Color.GRAY);
		labels = new HashMap<UIComponent, String>();
	}
	
	@Override
	protected void update()
	{
		
	}
	
	@Override
	protected void render(Drawable drawable)
	{
		Canvas canvas = drawable.getCanvas();
		for (Iterator<Entry<UIComponent, String>> iterator = labels.entrySet().iterator(); iterator.hasNext();)
		{
			Entry<UIComponent, String> entry = iterator.next();
			VectorInt position = getPosition(entry.getKey());
			canvas.drawTextR(entry.getValue(), position.getX(), position.getY(), Color.BLACK);
		}
		if (doBoarder) drawable.getCanvas().drawRect(0, 0, dimensions.getX(), dimensions.getY(), false, Color.BLACK);
		{
			canvas.drawRect(0, 0, canvas.getWidth(), boarderThickness, Color.BLACK);
			canvas.drawRect(canvas.getWidth() - boarderThickness, boarderThickness, boarderThickness, canvas.getHeight() - boarderThickness, Color.BLACK);
			canvas.drawRect(0, boarderThickness, boarderThickness, canvas.getHeight() - boarderThickness, Color.BLACK);
			canvas.drawRect(boarderThickness, canvas.getHeight() - boarderThickness, canvas.getWidth() - boarderThickness * 2, boarderThickness, Color.BLACK);
		}
	}
	
	@SuppressWarnings("unchecked")
	public ArrayList<UIComponent> getComponents()
	{
		return (ArrayList<UIComponent>)uiComponents.clone();
	}
	
	public void setLabel(UIComponent component, String label)
	{
		if (uiComponents.contains(component))
		{
			if (!labels.containsKey(component))
			{
				VectorInt position = component.getPosition();
				position.setY(position.getY() + canvas.getFontHeightR() + gapSize);
				component.setPosition(position);
			}
			labels.put(component, label);
		}
	}
	
	public void removeLabel(UIComponent component)
	{
		if (labels.containsKey(component))
		{
			VectorInt position = component.getPosition();
			position.setY(position.getY() - canvas.getFontHeightR() - gapSize);
			component.setPosition(position);
			labels.remove(component);
		}
	}
	
	public VectorInt getPosition(UIComponent component)
	{
		if (uiComponents.contains(component))
		{
			VectorInt pos = component.getPosition();
			if (labels.containsKey(component)) pos.setY(pos.getY() - canvas.getFontHeightR() - gapSize);
			return pos;
		}
		else return null;
	}
	
	public VectorInt getDimensions(UIComponent component)
	{
		if (uiComponents.contains(component))
		{
			VectorInt dim = component.getDimensions();
			if (labels.containsKey(component))
			{
				dim.setY(dim.getY() + canvas.getFontHeightR() + gapSize);
				int labelWidth = canvas.getStringLengthR(labels.get(component));
				if (labelWidth > dim.getX()) dim.setX(labelWidth);
			}
			return dim;
		}
		else return null;
	}
	
	public boolean canFit(UIComponent component, VectorInt position)
	{
		if (uiComponents.contains(component))
		{
			int boarder = doBoarder ? boarderThickness : 0;
			if (position.getX() >= gapSize + boarder && position.getY() >= gapSize + boarder && position.getX() + getDimensions(component).getX() + gapSize + boarder < dimensions.getX())
			{
				for (Iterator<UIComponent> iterator = uiComponents.iterator(); iterator.hasNext();)
				{
					UIComponent otherComponent = iterator.next();
					if (!otherComponent.equals(component))
					{
						if (position.getX() < getPosition(otherComponent).getX() + getDimensions(otherComponent).getX() + gapSize) return false;
						if (position.getY() < getPosition(otherComponent).getY() + getDimensions(otherComponent).getX() + gapSize) return false;
						if (position.getX() + getDimensions(component).getX() + gapSize >= getPosition(otherComponent).getX()) return false;
						if (position.getY() + getDimensions(component).getY() + gapSize >= getPosition(otherComponent).getY()) return false;
					}
				}
				return true;
			}
		}
		return false;
	}
	
	public void clear()
	{
		for (Iterator<UIComponent> iterator = uiComponents.iterator(); iterator.hasNext();) iterator.next().remove();
	}
	
	/*
	private int getHighestPoint(int limit)
	{
		int highest = Integer.MAX_VALUE;
		for (Iterator<UIComponent> iterator = uiComponents.iterator(); iterator.hasNext();)
		{
			UIComponent component = iterator.next();
			VectorInt position = getPosition(component);
			VectorInt dimensions = getDimensions(component);
			int height = position.getY() + dimensions.getY() + gapSize;
			if (height > limit && height < highest) highest = height;
		}
		return highest == Integer.MAX_VALUE ? limit : highest;
	}
	
	private int getNearestPoint(int limit)
	{
		int nearest = Integer.MAX_VALUE;
		for (Iterator<UIComponent> iterator = uiComponents.iterator(); iterator.hasNext();)
		{
			UIComponent component = iterator.next();
			VectorInt position = getPosition(component);
			VectorInt dimensions = getDimensions(component);
			int distance = position.getX() + dimensions.getX() + gapSize;
			if (distance > limit && distance < nearest) nearest = distance;
		}
		return nearest == Integer.MAX_VALUE ? limit : nearest;
	}
	
	public void setPosition(UIComponent component, VectorInt position)
	{
		if (uiComponents.contains(component))
		{
			if (labels.containsKey(component)) position.setY(position.getY() + canvas.getFontHeightR() + gapSize);
			component.setPosition(position);
		}
	}
	
	public void autoOrientate()
	{
		VectorInt temporaryPosition = new VectorInt(Integer.MIN_VALUE, Integer.MIN_VALUE);
		for (Iterator<UIComponent> iterator = uiComponents.iterator(); iterator.hasNext();) iterator.next().setPosition(temporaryPosition);
		int yLimit = gapSize + (doBoarder ? boarderThickness : 0);
		int xLimit = gapSize + (doBoarder ? boarderThickness : 0);
		int componentNumber = 0;
		boolean continueFirstRow = true;
		int x = xLimit;
		int y = yLimit;
		while (continueFirstRow && componentNumber < uiComponents.size())
		{
			UIComponent component = uiComponents.get(componentNumber);
			VectorInt position = new VectorInt(x, y);
			if (canFit(component, position))
			{
				setPosition(component, position);
				x += getDimensions(component).getX() + gapSize;
				componentNumber++;
			}
			else continueFirstRow = false;
		}
		x = xLimit;
		for (int index = componentNumber; index < uiComponents.size(); index++)
		{
			
		}
	}
	*/
}