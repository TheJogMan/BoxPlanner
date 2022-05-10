package globalResources.newUI;

import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Iterator;

import globalResources.dataTypes.LinkedList.LinkedElement;
import globalResources.graphics.Canvas;
import globalResources.graphics.Drawable;
import globalResources.newUI.event.ComponentAdded;
import globalResources.newUI.event.InputEvent.ComponentInput;
import globalResources.newUI.event.InputEvent.NUIInputListener;
import globalResources.newUI.event.NUIEventHandler;
import globalResources.ui.FrameInput;
import globalResources.utilities.VectorInt;

public abstract class NUIComponent extends NUIHolder implements NUIInputListener, NUIEventHandler
{
	NUIHolder parent;
	LinkedElement<NUIComponent> linkedElement;
	ArrayList<NUIInputListener> listeners;
	protected Drawable drawable;
	
	protected abstract void componentDraw(Canvas canvas);
	protected abstract void componentUpdate();
	
	public NUIComponent(NUIHolder parent, VectorInt position, VectorInt dimensions)
	{
		super(position, dimensions);
		this.drawable = new Drawable(dimensions);
		listeners = new ArrayList<NUIInputListener>();
		ComponentAdded event = new ComponentAdded(this, parent);
		parent.processEvent(event);
		if (!event.cancelled())
		{
			this.parent = parent;
			linkedElement = event.getElement();
		}
		addInputListener(this);
		addEventHandler(this);
	}
	
	public final NUIInputListener addInputListener(NUIInputListener listener)
	{
		listeners.add(listener);
		return listener;
	}
	
	public final void processInput(ComponentInput input)
	{
		for (Iterator<NUIInputListener> iterator = listeners.iterator(); iterator.hasNext();)
		{
			input.processOnListener(iterator.next());
		}
	}
	
	public final void remove()
	{
		linkedElement.remove();
		parent = null;
	}
	
	public Drawable getDrawable()
	{
		return drawable;
	}
	
	@Override
	public final void setDimensions(VectorInt dimensions)
	{
		super.setDimensions(dimensions);
		drawable = new Drawable(dimensions);
	}
	
	public final FrameInput getRootInputSource()
	{
		return parent.getRootInputSource();
	}
	
	public final boolean clicked()
	{
		return getRootInputSource().isButton(MouseEvent.BUTTON1) && hovered();
	}
	
	public final boolean selected()
	{
		return parent.selectedComponent != null && parent.selectedComponent.equals(this);
	}
	
	public final boolean hovered()
	{
		return parent.hovered() && super.hovered();
	}
	
	@Override
	public final VectorInt getRelativeCursorPosition()
	{
		VectorInt position = parent.getRelativeCursorPosition();
		position.subtract(this.position);
		return position;
	}
	
	final void update()
	{
		updateUI();
		componentUpdate();
	}
	
	final void draw(Canvas destinationCanvas)
	{
		Canvas canvas = drawable.getCanvas();
		canvas.clear();
		componentDraw(canvas);
		drawUI(canvas);
		overDraw(canvas);
		destinationCanvas.draw(drawable, position);
	}
	
	public final void addTo(NUIHolder parent)
	{
		
	}
	
	public final NUIHolder getHolder()
	{
		return parent;
	}
	
	protected void overDraw(Canvas canvas)
	{
		
	}
}