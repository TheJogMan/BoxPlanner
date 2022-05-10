package globalResources.newUI;

import java.util.ArrayList;
import java.util.Iterator;

import globalResources.dataTypes.LinkedList;
import globalResources.graphics.Canvas;
import globalResources.newUI.event.ComponentAdded;
import globalResources.newUI.event.FocusEvent;
import globalResources.newUI.event.InputEvent;
import globalResources.newUI.event.InputEvent.ButtonPress;
import globalResources.newUI.event.InputEvent.ComponentInput;
import globalResources.newUI.event.InputEvent.CursorInput;
import globalResources.newUI.event.InputEvent.CursorMove;
import globalResources.newUI.event.NUIEvent;
import globalResources.newUI.event.NUIEventHandler;
import globalResources.newUI.event.SelectEvent;
import globalResources.ui.FrameInput;
import globalResources.utilities.VectorInt;

public abstract class NUIHolder
{
	private LinkedList<NUIComponent> components;
	protected VectorInt position;
	protected VectorInt dimensions;
	protected NUIComponent selectedComponent;
	protected ArrayList<NUIEventHandler> eventHandlers;
	
	public NUIHolder(VectorInt position, VectorInt dimensions)
	{
		this.position = position.clone();
		this.dimensions = dimensions.clone();
		components = new LinkedList<NUIComponent>();
		eventHandlers = new ArrayList<NUIEventHandler>();
	}
	
	public abstract FrameInput getRootInputSource();
	
	protected final void updateUI()
	{
		for (Iterator<NUIComponent> iterator = components.iterator(); iterator.hasNext();)
		{
			iterator.next().update();
		}
	}
	
	public void removeAllComponents()
	{
		for (Iterator<NUIComponent> iterator = components.iterator(); iterator.hasNext();)
		{
			iterator.next().remove();
		}
	}
	
	public boolean hovered()
	{
		VectorInt position = getRelativeCursorPosition();
		return position.getX() >= 0 && position.getY() >= 0 && position.getX() < dimensions.getX() && position.getY() < dimensions.getY();
	}
	
	public VectorInt getRelativeCursorPosition()
	{
		return getRootInputSource().getMousePosition();
	}
	
	protected final void drawUI(Canvas canvas)
	{
		for (Iterator<NUIComponent> iterator = components.iterator(); iterator.hasNext();)
		{
			iterator.next().draw(canvas);
		}
	}
	
	public final void setPosition(VectorInt position)
	{
		this.position = position.clone();
	}
	
	public void setDimensions(VectorInt dimensions)
	{
		this.dimensions = dimensions.clone();
	}
	
	public final VectorInt getPosition()
	{
		return position.clone();
	}
	
	public final VectorInt getDimensions()
	{
		return dimensions.clone();
	}
	
	public final NUIEventHandler addEventHandler(NUIEventHandler handler)
	{
		eventHandlers.add(handler);
		return handler;
	}
	
	public final void removeEventHandler(NUIEventHandler handler)
	{
		eventHandlers.remove(handler);
	}
	
	public final void processEvent(NUIEvent event)
	{
		if (event instanceof InputEvent)
		{
			if (this instanceof NUIComponent) ((NUIComponent)this).processInput(((InputEvent)event).getInput());
			for (Iterator<NUIComponent> iterator = components.reverseIterator(); iterator.hasNext();)
			{
				NUIComponent component = iterator.next();
				ComponentInput input = ((InputEvent)event).getInput().contextualize(component);
				if (input instanceof CursorInput)
				{
					if (input instanceof CursorMove)
					{
						VectorInt startPosition = ((CursorMove)input).getStart();
						VectorInt stopPosition = ((CursorMove)input).getPosition();
						boolean start = startPosition.getX() >= 0 && startPosition.getY() >= 0 && startPosition.getX() < component.dimensions.getX() && startPosition.getY() < component.dimensions.getY();
						boolean stop = stopPosition.getX() >= 0 && stopPosition.getY() >= 0 && stopPosition.getX() < component.dimensions.getX() && stopPosition.getY() < component.dimensions.getY();
						if (start || stop)
						{
							component.processEvent(new InputEvent(input));
							break;
						}
					}
					else
					{
						VectorInt position = ((CursorInput)input).getPosition();
						if (position.getX() >= 0 && position.getY() >= 0 && position.getX() < component.dimensions.getX() && position.getY() < component.dimensions.getY())
						{
							if (input instanceof ButtonPress) processEvent(new SelectEvent(component));
							component.processEvent(new InputEvent(input));
							break;
						}
					}
				}
				else if (component.equals(selectedComponent))
				{
					component.processEvent(new InputEvent(input));
				}
			}
		}
		else
		{
			for (Iterator<NUIEventHandler> iterator = eventHandlers.iterator(); iterator.hasNext() && event.continueProcessing();)
			{
				iterator.next().handle(event);
			}
			event.performAction();
			if (event instanceof SelectEvent && !((SelectEvent)event).cancelled())
			{
				selectedComponent = ((SelectEvent)event).getSelectedComponent();
				processEvent(new FocusEvent(selectedComponent));
			}
			else if (event instanceof ComponentAdded && !((ComponentAdded)event).cancelled()) ((ComponentAdded)event).setElement(components.add(((ComponentAdded)event).getComponent()));
		}
	}
}