package globalResources.ui.uiComponents;

import java.awt.Color;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.util.ArrayList;
import java.util.Iterator;

import globalResources.graphics.Canvas;
import globalResources.graphics.Drawable;
import globalResources.ui.FrameInput.FrameInputListenerInterface;
import globalResources.utilities.VectorInt;
import globalResources.ui.UIComponent;
import globalResources.ui.UIHolder;

public class Slider extends UIComponent
{
	Color barColor;
	double value;
	double min;
	double max;
	double steps;
	
	ArrayList<Runnable> changeListeners;
	
	public Slider(UIHolder parent, VectorInt position, VectorInt dimensions, double min, double max, double steps)
	{
		super(parent, position, dimensions);
		changeListeners = new ArrayList<Runnable>();
		barColor = Color.WHITE;
		super.registerListener(new InputListener());
		super.addFrameListener(new FrameInputListener());
		this.max = max;
		this.min = min;
		this.steps = steps;
		value = min;
		drawable.getCanvas().setClearColor(Color.DARK_GRAY);
	}
	
	@Override
	protected void update()
	{
		if (parent.getInput().isButton(MouseEvent.BUTTON1) && hovered)
		{
			double mx = getAdjustedMousePos().getX() - position.getX();
			mx /= dimensions.getX();
			mx *= (max - min);
			if (mx >= min && mx < max) change(mx);
		}
	}
	
	@Override
	protected void render(Drawable drawable)
	{
		Canvas canvas = drawable.getCanvas();
		double width = value - min;
		width /= (max - min);
		width *= (double)canvas.getWidth();
		canvas.drawRect(0, 0, (int)width, canvas.getHeight(), barColor);
	}
	
	public void addChangeListener(Runnable runnable)
	{
		changeListeners.add(runnable);
	}
	
	void change(double value)
	{
		if (value < min) value = min;
		else if (value > max) value = max;
		if (value != this.value)
		{
			this.value = value;
			for (Iterator<Runnable> iterator = changeListeners.iterator(); iterator.hasNext();) iterator.next().run();
		}
	}
	
	public double getValue()
	{
		return value;
	}
	
	public void setValue(double value)
	{
		this.value = value;
		if (value < min) value = min;
		else if (value > max) value = max;
	}
	
	public void setBarColor(Color color)
	{
		barColor = color;
	}
	
	class InputListener extends ComponentListener
	{
		@Override
		public void clicked()
		{
			
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
	
	class FrameInputListener implements FrameInputListenerInterface
	{
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
			if (hovered)
			{
				double step = (max - min) / steps;
				change(value + event.getWheelRotation() * step);
			}
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
}