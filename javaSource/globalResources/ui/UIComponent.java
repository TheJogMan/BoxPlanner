package globalResources.ui;

import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import globalResources.graphics.Canvas;
import globalResources.graphics.Drawable;
import globalResources.ui.FrameInput.FrameInputListener;
import globalResources.ui.FrameInput.FrameInputListenerInterface;
import globalResources.utilities.VectorInt;

/**
 * Processes user input
 */
public abstract class UIComponent extends UIHolder
{
	public enum Event
	{
		click, unClick, hover, unHover
	}
	
	protected VectorInt position;
	protected VectorInt dimensions;
	protected boolean shown;
	protected boolean remove;
	protected boolean hovered;
	protected boolean clicked;
	protected boolean doClear;
	protected UIHolder parent;
	protected Drawable drawable;
	
	protected abstract void update();
	protected abstract void render(Drawable drawable);
	
	protected ArrayList<Event> queuedEvents;
	protected ArrayList<ComponentListener> listeners;
	protected ArrayList<FrameInputListenerInterface> addedListeners;
	protected FrameInputListener listener;
	
	protected UIComponent(UIHolder parent, VectorInt position, VectorInt dimensions)
	{
		super(null, parent.inputSource);
		this.parent = parent;
		this.listener = new InputListener();
		this.listeners = new ArrayList<ComponentListener>();
		this.queuedEvents = new ArrayList<Event>();
		this.position = position.clone();
		this.dimensions = dimensions.clone();
		addedListeners = new ArrayList<FrameInputListenerInterface>();
		shown = false;
		remove = false;
		hovered = false;
		clicked = false;
		doClear = true;
		drawable = new Drawable(dimensions);
		super.canvas = drawable.getCanvas();
		parent.uiComponents.add(this);
		setShown(true);
	}
	
	protected void postUIRender(Drawable drawable)
	{
		
	}
	
	public void setDimensions(VectorInt dimensions)
	{
		this.dimensions = dimensions.clone();
		drawable = new Drawable(dimensions);
		Canvas canvas = drawable.getCanvas();
		canvas.setClearColor(super.canvas.getClearColor());
		canvas.setColor(super.canvas.getColor());
		canvas.setFont(super.canvas.getFont());
		canvas.setFontR(super.canvas.getFontR());
		canvas.setFontScale(super.canvas.getFontScale());
		super.canvas = drawable.getCanvas();
	}
	
	/**
	 * Gets the position of the mouse within this components parent.
	 * @return position
	 */
	public VectorInt getParentMousePos()
	{
		if (parent instanceof UIComponent) return ((UIComponent)parent).getInternalMousePos();
		else return parent.inputSource.getMousePosition();
	}
	
	/**
	 * Gets the position of the mouse within this component.
	 * @return position
	 */
	public VectorInt getInternalMousePos()
	{
		VectorInt mousePos = getParentMousePos();
		mousePos.subtract(position);
		return mousePos;
	}
	
	/**
	 * @see #getParentMousePos()
	 * @return position
	 */
	public VectorInt getAdjustedMousePos()
	{
		return getParentMousePos();
	}
	
	/**
	 * Registers a listener to this component
	 * @param listener the listener to be registered
	 */
	public void registerListener(ComponentListener listener)
	{
		listeners.add(listener);
	}
	
	/**
	 * Add an input listener to this component
	 * @param listener
	 */
	public void addFrameListener(FrameInputListenerInterface listener)
	{
		if (shown) parent.inputSource.registerInputListener(listener);
		addedListeners.add(listener);
	}
	
	/**
	 * Called when this component is discarded
	 */
	public void discard()
	{
		
	}
	
	/**
	 * Gets this components parent
	 * @return the parent of this component
	 */
	public UIHolder getParent()
	{
		return parent;
	}
	
	/**
	 * Returns whether this component is currently shown
	 * @return is this component currently shown
	 */
	public boolean shown()
	{
		return shown;
	}
	
	/**
	 * Returns whether this component is currently hovered
	 * @return is this component currently hovered
	 */
	public boolean hovered()
	{
		return hovered;
	}
	
	/**
	 * Returns whether this component is currently clicked
	 * @return is this component currently clicked
	 */
	public boolean clicked()
	{
		return clicked;
	}
	
	/**
	 * Marks this component for removal
	 */
	public void remove()
	{
		remove = true;
	}
	
	/**
	 * Checks if this component is marked for removal
	 * @return is this component marked for removal
	 */
	public boolean isRemoved()
	{
		return remove;
	}
	
	/**
	 * Gets the position of this component within its parent
	 * @return position of this component
	 */
	public VectorInt getPosition()
	{
		return position.clone();
	}
	
	/**
	 * Gets the dimensions of this component
	 * @return dimensions of this component
	 */
	public VectorInt getDimensions()
	{
		return dimensions.clone();
	}
	
	/**
	 * Sets the position of this component within its parent
	 * @param newPosition the new position
	 */
	public void setPosition(VectorInt newPosition)
	{
		position.set(newPosition);
	}
	
	/**
	 * Sets whether this component should be shown
	 * @param shown if this component should be shown
	 */
	public void setShown(boolean shown)
	{
		if (shown != this.shown)
		{
			this.shown = shown;
			if (shown) addListeners();
			else removeListeners();
		}
	}
	
	void removeListeners()
	{
		parent.inputSource.removeInputListener(listener);
		for (Iterator<FrameInputListenerInterface> iterator = addedListeners.iterator(); iterator.hasNext();) parent.inputSource.removeInputListener(iterator.next());
		for (Iterator<UIComponent> iterator = uiComponents.iterator(); iterator.hasNext();) iterator.next().removeListeners();
	}
	
	void addListeners()
	{
		parent.inputSource.registerInputListener(listener);
		for (Iterator<FrameInputListenerInterface> iterator = addedListeners.iterator(); iterator.hasNext();) parent.inputSource.registerInputListener(iterator.next());
		for (Iterator<UIComponent> iterator = uiComponents.iterator(); iterator.hasNext();) iterator.next().addListeners();
	}
	
	/**
	 * Listens for events from a UI component
	 */
	public static abstract class ComponentListener
	{
		/**
		 * Called when the component is clicked
		 */
		public abstract void clicked();
		/**
		 * Called when the component is no longer being clicked
		 */
		public abstract void unClicked();
		/**
		 * called when the mouse cursor hovers over the component
		 */
		public abstract void hovered();
		/**
		 * called when the mouse cursor is no longer hovering over the component
		 */
		public abstract void unHovered();
	}
	
	void triggerEvent(Event event)
	{
		if (event.compareTo(Event.click) == 0) triggerClickEvent();
		else if (event.compareTo(Event.hover) == 0) triggerHoverEvent();
		else if (event.compareTo(Event.unClick) == 0) triggerUnClickEvent();
		else if (event.compareTo(Event.unHover) == 0) triggerUnHoverEvent();
	}
	
	void triggerClickEvent()
	{
		clicked = true;
		for (Iterator<ComponentListener> iterator = listeners.iterator(); iterator.hasNext();) iterator.next().clicked();
	}
	
	void triggerUnClickEvent()
	{
		clicked = false;
		for (Iterator<ComponentListener> iterator = listeners.iterator(); iterator.hasNext();) iterator.next().unClicked();
	}
	
	void triggerHoverEvent()
	{
		hovered = true;
		for (Iterator<ComponentListener> iterator = listeners.iterator(); iterator.hasNext();) iterator.next().hovered();
	}
	
	void triggerUnHoverEvent()
	{
		hovered = false;
		for (Iterator<ComponentListener> iterator = listeners.iterator(); iterator.hasNext();) iterator.next().unHovered();
		if (clicked)
		{
			triggerUnClickEvent();
		}
	}
	
	protected void dispose()
	{
		super.clearUI();
		discard();
		parent.inputSource.removeInputListener(listener);
		parent.uiComponents.remove(this);
		for (Iterator<FrameInputListenerInterface> iterator = addedListeners.iterator(); iterator.hasNext();) parent.inputSource.registerInputListener(iterator.next());
	}
	
	void draw()
	{
		if (shown)
		{
			if (doClear) drawable.getCanvas().clear();
			
			render(drawable);
			renderUI();
			postUIRender(drawable);
			renderSecondLayerUI();
			
			parent.canvas.draw(drawable, position);
		}
	}
	
	void processEvents()
	{
		List<Event> processedEvents = new ArrayList<Event>();
		int count = queuedEvents.size();
		for (int index = 0; index < count; index++)
		{
			triggerEvent(queuedEvents.get(index));
			processedEvents.add(queuedEvents.get(index));
		}
		for (Iterator<Event> iterator = processedEvents.iterator(); iterator.hasNext();) queuedEvents.remove(iterator.next());
	}
	
	void run()
	{
		if (!remove)
		{
			updateUI();
			processEvents();
			update();
		}
	}
	
	protected class InputListener extends FrameInputListener
	{
		@Override
		public void buttonPress(MouseEvent event)
		{
			if (hovered)
			{
				if (event.getButton() == MouseEvent.BUTTON1)
				{
					queuedEvents.add(Event.click);
				}
			}
		}
		
		@Override
		public void buttonRelease(MouseEvent event)
		{
			if (hovered)
			{
				if (event.getButton() == MouseEvent.BUTTON1)
				{
					queuedEvents.add(Event.unClick);
				}
			}
		}
		
		@Override
		public void keyPress(KeyEvent event)
		{
			
		}
		
		@Override
		public void keyRelease(KeyEvent event)
		{
			
		}
		
		@Override
		public void textEdit(KeyEvent event)
		{
			
		}
		
		@Override
		public void textInput(KeyEvent event)
		{
			
		}
		
		@Override
		public void mouseScroll(MouseWheelEvent event)
		{
			
		}
		
		@Override
		public void mouseMove(VectorInt delta, VectorInt newPosition)
		{
			boolean wasHovered = hovered;
			boolean hovered = false;
			VectorInt pos = getInternalMousePos();
			if (pos.getX() >= 0 && pos.getX() < dimensions.getX()
			&& pos.getY() >= 0 && pos.getY() < dimensions.getY()) hovered = true;
			
			if (wasHovered != hovered)
			{
				if (hovered) queuedEvents.add(Event.hover);
				else queuedEvents.add(Event.unHover);
			}
		}
		
		@Override
		public void mouseDrag(VectorInt delta, VectorInt newPosition)
		{	
			boolean wasHovered = hovered;
			boolean hovered = false;
			VectorInt pos = getInternalMousePos();
			if (pos.getX() >= 0 && pos.getX() < dimensions.getX()
			&& pos.getY() >= 0 && pos.getY() < dimensions.getY()) hovered = true;
			
			if (wasHovered != hovered)
			{
				if (hovered) queuedEvents.add(Event.hover);
				else queuedEvents.add(Event.unHover);
			}
		}
	}
}