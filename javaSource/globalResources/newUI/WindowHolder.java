package globalResources.newUI;

import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;

import globalResources.newUI.event.InputEvent;
import globalResources.newUI.event.InputEvent.ButtonPress;
import globalResources.newUI.event.InputEvent.ButtonRelease;
import globalResources.newUI.event.InputEvent.CursorDrag;
import globalResources.newUI.event.InputEvent.CursorMove;
import globalResources.newUI.event.InputEvent.KeyPress;
import globalResources.newUI.event.InputEvent.KeyRelease;
import globalResources.newUI.event.InputEvent.Scroll;
import globalResources.newUI.event.InputEvent.TextEdit;
import globalResources.newUI.event.InputEvent.TextInput;
import globalResources.ui.FrameInput;
import globalResources.ui.FrameInput.FrameInputListenerInterface;
import globalResources.utilities.VectorInt;
import globalResources.window.Window;

public class WindowHolder extends NUIHolder implements FrameInputListenerInterface
{
	Window window;
	
	public WindowHolder(Window window)
	{
		super(new VectorInt(0, 0), new VectorInt(window.getWidth(), window.getHeight()));
		this.window = window;
		window.getInput().registerInputListener(this);
	}
	
	public void update()
	{
		updateUI();
	}
	
	public void draw()
	{
		drawUI(window.getCanvas());
	}
	
	public Window getWindow()
	{
		return window;
	}
	
	@Override
	public void buttonPressEvent(MouseEvent event)
	{
		processEvent(new InputEvent(new ButtonPress(event.getButton(), window.getInput().getMousePosition())));
	}
	
	@Override
	public void buttonReleaseEvent(MouseEvent event)
	{
		processEvent(new InputEvent(new ButtonRelease(event.getButton(), window.getInput().getMousePosition())));
	}
	
	@Override
	public void keyPressEvent(KeyEvent event)
	{
		processEvent(new InputEvent(new KeyPress(event.getKeyCode())));
	}
	
	@Override
	public void keyReleaseEvent(KeyEvent event)
	{
		processEvent(new InputEvent(new KeyRelease(event.getKeyCode())));
	}
	
	@Override
	public void textEditEvent(KeyEvent event)
	{
		processEvent(new InputEvent(new TextEdit(event.getKeyCode(), event.getKeyChar())));
	}
	
	@Override
	public void textInputEvent(KeyEvent event)
	{
		processEvent(new InputEvent(new TextInput(event.getKeyCode(), event.getKeyChar())));
	}
	
	@Override
	public void mouseScrollEvent(MouseWheelEvent event)
	{
		processEvent(new InputEvent(new Scroll(event.getWheelRotation(), window.getInput().getMousePosition())));
	}
	
	@Override
	public void mouseMoveEvent(VectorInt delta, VectorInt newPosition)
	{
		VectorInt start = newPosition.clone();
		start.subtract(delta);
		processEvent(new InputEvent(new CursorMove(start, newPosition)));
	}
	
	@Override
	public void mouseDragEvent(VectorInt delta, VectorInt newPosition)
	{
		VectorInt start = newPosition.clone();
		start.subtract(delta);
		processEvent(new InputEvent(new CursorDrag(start, newPosition)));
	}

	@Override
	public FrameInput getRootInputSource()
	{
		return window.getInput();
	}
}