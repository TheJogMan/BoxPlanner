package globalResources.ui;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import javax.swing.JFrame;

import globalResources.dataTypes.LinkedList;
import globalResources.dataTypes.LinkedList.LinkedElement;
import globalResources.utilities.VectorInt;

/**
 * Organizes input to a JFrame
 */
public class FrameInput
{
	private JFrame frame;
	KeyEventListener keyEventListener;
	MouseEventListener mouseEventListener;
	MouseMotionEventListener mouseMotionEventListener;
	MouseWheelEventListener mouseWheelEventListener;
	private boolean disposed;
	
	private HashMap<Integer, Boolean> keyStates;
	private HashMap<Integer, Boolean> buttonStates;
	
	private final int mouseOffX = -7;
	private final int mouseOffY = -30;
	
	private VectorInt mousePosition;
	
	private ArrayList<FrameInputListenerInterface> listeners;
	
	private LinkedList<InputEvent> eventQueue;
	//useEventQueue currently would always be set to true, however it is implemented incase the feature is ever desired
	private boolean useEventQueue;
	
	/**
	 * Creates a new FrameInput for the given JFrame
	 * @param frame the JFrame to organize input for
	 */
	public FrameInput(JFrame frame)
	{
		this.frame = frame;
		disposed = false;
		
		mousePosition = new VectorInt(0, 0);
		
		keyStates = new HashMap<Integer, Boolean>();
		buttonStates = new HashMap<Integer, Boolean>();
		
		listeners = new ArrayList<FrameInputListenerInterface>();
		
		eventQueue = new LinkedList<InputEvent>();
		useEventQueue = true;
		
		keyEventListener = new KeyEventListener();
		frame.addKeyListener(keyEventListener);
		mouseEventListener = new MouseEventListener();
		frame.addMouseListener(mouseEventListener);
		mouseMotionEventListener = new MouseMotionEventListener();
		frame.addMouseMotionListener(mouseMotionEventListener);
		mouseWheelEventListener = new MouseWheelEventListener();
		frame.addMouseWheelListener(mouseWheelEventListener);
	}
	
	/**
	 * Registers a new listener to this FrameInput
	 * @param listener the listener to be registered
	 */
	public void registerInputListener(FrameInputListenerInterface listener)
	{
		queueEvent(new ListenerEvent(listener, true));
	}
	
	/**
	 * Removes a listener from this FrameInput
	 * @param listener the listener to be removed
	 */
	public void removeInputListener(FrameInputListenerInterface listener)
	{
		queueEvent(new ListenerEvent(listener, false));
	}
	
	/**
	 * Get the current position of the mouse cursor within this frame
	 * @return position of the mouse cursor
	 */
	public VectorInt getMousePosition()
	{
		return mousePosition.clone();
	}
	
	/**
	 * Checks if a mouse button is currently pressed
	 * @param buttonCode MouseEvent code for the button
	 * @return the state of the mouse button
	 */
	public boolean isButton(int buttonCode)
	{
		Integer key = Integer.valueOf(buttonCode);
		if (buttonStates.containsKey(key)) return buttonStates.get(key);
		else return false;
	}
	
	/**
	 * Checks if a key is currently pressed
	 * @param keyCode KeyEvent code for the button
	 * @return the state of the key
	 */
	public boolean isKey(int keyCode)
	{
		Integer key = Integer.valueOf(keyCode);
		if (keyStates.containsKey(key)) return keyStates.get(key);
		else return false;
	}
	
	/**
	 * Processes the event queue
	 */
	public void processEventQueue()
	{
		LinkedElement<InputEvent> lastEvent = eventQueue.getLastElement();
		LinkedElement<InputEvent> currentEvent = eventQueue.getFirstElement();
		if (currentEvent != null)
		{
			do
			{
				handleEvent(currentEvent.getValue());
				LinkedElement<InputEvent> nextEvent = currentEvent.getNextElement();
				currentEvent.remove();
				currentEvent = nextEvent;
			}
			while(currentEvent != null && !currentEvent.equals(lastEvent));
		}
	}
	
	/**
	 * Disposes this FrameInput
	 */
	public void dispose()
	{
		if (!disposed)
		{
			frame.removeKeyListener(keyEventListener);
			frame.removeMouseListener(mouseEventListener);
			frame.removeMouseMotionListener(mouseMotionEventListener);
			frame.removeMouseWheelListener(mouseWheelEventListener);
			disposed = true;
		}
	}
	
	/**
	 * Listens for input events from a FrameInput
	 */
	public static interface FrameInputListenerInterface
	{
		void buttonPressEvent(MouseEvent event);
		void buttonReleaseEvent(MouseEvent event);
		void keyPressEvent(KeyEvent event);
		void keyReleaseEvent(KeyEvent event);
		void textEditEvent(KeyEvent event);
		void textInputEvent(KeyEvent event);
		void mouseScrollEvent(MouseWheelEvent event);
		void mouseMoveEvent(VectorInt delta, VectorInt newPosition);
		void mouseDragEvent(VectorInt delta, VectorInt newPosition);
	}
	
	/**
	 * Listens for input events from a FrameInput
	 */
	public static abstract class FrameInputListener implements FrameInputListenerInterface
	{
		protected abstract void buttonPress(MouseEvent event);
		protected abstract void buttonRelease(MouseEvent event);
		protected abstract void keyPress(KeyEvent event);
		protected abstract void keyRelease(KeyEvent event);
		protected abstract void textEdit(KeyEvent event);
		protected abstract void textInput(KeyEvent event);
		protected abstract void mouseScroll(MouseWheelEvent event);
		protected abstract void mouseMove(VectorInt delta, VectorInt newPosition);
		protected abstract void mouseDrag(VectorInt delta, VectorInt newPosition);
		
		@Override public void buttonPressEvent(MouseEvent event) {buttonPress(event);};
		@Override public void buttonReleaseEvent(MouseEvent event) {buttonRelease(event);};
		@Override public void keyPressEvent(KeyEvent event) {keyPress(event);};
		@Override public void keyReleaseEvent(KeyEvent event) {keyRelease(event);};
		@Override public void textEditEvent(KeyEvent event) {textEdit(event);};
		@Override public void textInputEvent(KeyEvent event) {textInput(event);};
		@Override public void mouseScrollEvent(MouseWheelEvent event) {mouseScroll(event);};
		@Override public void mouseMoveEvent(VectorInt delta, VectorInt newPosition) {mouseMove(delta, newPosition);};
		@Override public void mouseDragEvent(VectorInt delta, VectorInt newPosition) {mouseDrag(delta, newPosition);};
	}
	
	public static class SimpleFrameInputListener extends FrameInputListener
	{

		@Override
		protected void buttonPress(MouseEvent event)
		{
			// TODO Auto-generated method stub
			
		}

		@Override
		protected void buttonRelease(MouseEvent event)
		{
			// TODO Auto-generated method stub
			
		}

		@Override
		protected void keyPress(KeyEvent event)
		{
			// TODO Auto-generated method stub
			
		}

		@Override
		protected void keyRelease(KeyEvent event)
		{
			// TODO Auto-generated method stub
			
		}

		@Override
		protected void textEdit(KeyEvent event)
		{
			// TODO Auto-generated method stub
			
		}

		@Override
		protected void textInput(KeyEvent event)
		{
			// TODO Auto-generated method stub
			
		}

		@Override
		protected void mouseScroll(MouseWheelEvent event)
		{
			// TODO Auto-generated method stub
			
		}

		@Override
		protected void mouseMove(VectorInt delta, VectorInt newPosition)
		{
			// TODO Auto-generated method stub
			
		}

		@Override
		protected void mouseDrag(VectorInt delta, VectorInt newPosition)
		{
			// TODO Auto-generated method stub
			
		}
		
	}
	
	//////////////////////////////////////////////////////////////Event Handling
	
	private void queueEvent(InputEvent event)
	{
		if (event != null)
		{
			if (useEventQueue)
			{
				eventQueue.add(event);
			}
			else
			{
				handleEvent(event);
			}
		}
	}
	
	private void handleEvent(InputEvent event)
	{
		//System.out.println("Event: " + event.describe());
		if (event instanceof ListenerEvent)
		{
			handleListenerEvent((ListenerEvent)event);
		}
		else if (event instanceof KeyInputEvent)
		{
			handleKeyInputEvent((KeyInputEvent)event);
		}
		else if (event instanceof MouseInputEvent)
		{
			handleMouseInputEvent((MouseInputEvent)event);
		}
		else if (event instanceof MouseWheelInputEvent)
		{
			handleMouseWheelInputEvent((MouseWheelInputEvent)event);
		}
	}
	
	private void handleListenerEvent(ListenerEvent event)
	{
		if (event.addOrRemove) listeners.add(event.listener);
		else listeners.remove(event.listener);
	}
	
	private void handleKeyInputEvent(KeyInputEvent keyEvent)
	{
		KeyEvent event = keyEvent.event;
		
		if (keyEvent instanceof KeyPressEvent)
		{
			keyStates.put(Integer.valueOf(event.getKeyCode()), true);
			for (Iterator<FrameInputListenerInterface> iterator = listeners.iterator(); iterator.hasNext();) iterator.next().keyPressEvent(event);
		}
		else if (keyEvent instanceof KeyReleaseEvent)
		{
			keyStates.put(Integer.valueOf(event.getKeyCode()), false);
			for (Iterator<FrameInputListenerInterface> iterator = listeners.iterator(); iterator.hasNext();) iterator.next().keyReleaseEvent(event);
		}
		else if (keyEvent instanceof KeyTypedEvent)
		{
			if (Character.isISOControl(Character.codePointAt(new char[] {event.getKeyChar()}, 0)))
			{
				for (Iterator<FrameInputListenerInterface> iterator = listeners.iterator(); iterator.hasNext();) iterator.next().textEditEvent(event);
			}
			else
			{
				for (Iterator<FrameInputListenerInterface> iterator = listeners.iterator(); iterator.hasNext();) iterator.next().textInputEvent(event);
			}
		}
	}
	
	private void handleMouseInputEvent(MouseInputEvent mouseEvent)
	{
		MouseEvent event = mouseEvent.event;
		
		if (mouseEvent instanceof MousePressEvent)
		{
			buttonStates.put(Integer.valueOf(event.getButton()), true);
			for (Iterator<FrameInputListenerInterface> iterator = listeners.iterator(); iterator.hasNext();) iterator.next().buttonPressEvent(event);
		}
		else if (mouseEvent instanceof MouseReleaseEvent)
		{
			buttonStates.put(Integer.valueOf(event.getButton()), false);
			for (Iterator<FrameInputListenerInterface> iterator = listeners.iterator(); iterator.hasNext();) iterator.next().buttonReleaseEvent(event);
		}
		else
		{
			VectorInt newPosition = new VectorInt(event.getX() + mouseOffX, event.getY() + mouseOffY);
			VectorInt delta = new VectorInt(newPosition.getX() - mousePosition.getX(), newPosition.getY() - mousePosition.getY());
			if (mouseEvent instanceof MouseDragEvent)
			{
				for (Iterator<FrameInputListenerInterface> iterator = listeners.iterator(); iterator.hasNext();) iterator.next().mouseDragEvent(delta, newPosition);
			}
			else if (mouseEvent instanceof MouseMoveEvent)
			{
				for (Iterator<FrameInputListenerInterface> iterator = listeners.iterator(); iterator.hasNext();) iterator.next().mouseMoveEvent(delta, newPosition);
			}
			mousePosition.set(newPosition);
		}
	}
	
	private void handleMouseWheelInputEvent(MouseWheelInputEvent mouseWheelEvent)
	{
		MouseWheelEvent event = mouseWheelEvent.event;
		
		if (mouseWheelEvent instanceof WheelMoveEvent)
		{
			for (Iterator<FrameInputListenerInterface> iterator = listeners.iterator(); iterator.hasNext();) iterator.next().mouseScrollEvent(event);
		}
	}
	
	//////////////////////////////////////////////////////////////Event Definition
	
	private abstract class InputEvent
	{
		abstract String describe();
	}
	
	private class ListenerEvent extends InputEvent
	{
		FrameInputListenerInterface listener;
		boolean addOrRemove;
		
		ListenerEvent(FrameInputListenerInterface listener, boolean addOrRemove)
		{
			this.listener = listener;
			this.addOrRemove = addOrRemove;
		}
		
		@Override
		String describe()
		{
			if (addOrRemove) return "adding FrameInputListener " + listener.getClass().getName();
			else return "removing FrameInputListener " + listener.getClass().getName();
		}
	}
	
	private abstract class KeyInputEvent extends InputEvent
	{
		KeyEvent event;
		
		KeyInputEvent(KeyEvent event)
		{
			this.event = event;
		}
	}
	
	private abstract class MouseWheelInputEvent extends InputEvent
	{
		MouseWheelEvent event;
		
		MouseWheelInputEvent(MouseWheelEvent event)
		{
			this.event = event;
		}
	}
	
	private abstract class MouseInputEvent extends InputEvent
	{
		MouseEvent event;
		
		MouseInputEvent(MouseEvent event)
		{
			this.event = event;
		}
	}
	
	private class KeyPressEvent extends KeyInputEvent
	{
		KeyPressEvent(KeyEvent event)
		{
			super(event);
		}
		
		@Override
		String describe()
		{
			return "key press: " + KeyEvent.getKeyText(event.getKeyCode());
		}
	}
	
	private class KeyReleaseEvent extends KeyInputEvent
	{
		KeyReleaseEvent(KeyEvent event)
		{
			super(event);
		}
		
		@Override
		String describe()
		{
			return "key release: " + KeyEvent.getKeyText(event.getKeyCode());
		}
	}
	
	private class KeyTypedEvent extends KeyInputEvent
	{
		KeyTypedEvent(KeyEvent event)
		{
			super(event);
		}
		
		@Override
		String describe()
		{
			return "key type: " + KeyEvent.getKeyText(event.getKeyCode());
		}
	}
	
	private class MousePressEvent extends MouseInputEvent
	{
		MousePressEvent(MouseEvent event)
		{
			super(event);
		}
		
		@Override
		String describe()
		{
			return "mouse press: " + event.getButton();
		}
	}
	
	private class MouseReleaseEvent extends MouseInputEvent
	{
		MouseReleaseEvent(MouseEvent event)
		{
			super(event);
		}
		
		@Override
		String describe()
		{
			return "mouse release: " + event.getButton();
		}
	}
	
	private class MouseDragEvent extends MouseInputEvent
	{
		MouseDragEvent(MouseEvent event)
		{
			super(event);
		}
		
		@Override
		String describe()
		{
			return "mouse drag";
		}
	}
	
	private class MouseMoveEvent extends MouseInputEvent
	{
		MouseMoveEvent(MouseEvent event)
		{
			super(event);
		}
		
		@Override
		String describe()
		{
			return "mouse move";
		}
	}
	
	private class WheelMoveEvent extends MouseWheelInputEvent
	{
		WheelMoveEvent(MouseWheelEvent event)
		{
			super(event);
		}
		
		@Override
		String describe()
		{
			return "wheel drag";
		}
	}
	
	//////////////////////////////////////////////////////////////Event Scheduling
	
	private class KeyEventListener implements KeyListener
	{
		@Override
		public void keyPressed(KeyEvent event)
		{
			queueEvent(new KeyPressEvent(event));
		}
		
		@Override
		public void keyReleased(KeyEvent event)
		{
			queueEvent(new KeyReleaseEvent(event));
		}
		
		@Override
		public void keyTyped(KeyEvent event)
		{
			queueEvent(new KeyTypedEvent(event));
		}
	}
	
	private class MouseEventListener implements MouseListener
	{
		@Override
		public void mouseClicked(MouseEvent event)
		{
			
		}
		
		@Override
		public void mouseEntered(MouseEvent event)
		{
			
		}
		
		@Override
		public void mouseExited(MouseEvent event)
		{
			
		}
		
		@Override
		public void mousePressed(MouseEvent event)
		{
			queueEvent(new MousePressEvent(event));
		}
		
		@Override
		public void mouseReleased(MouseEvent event)
		{
			queueEvent(new MouseReleaseEvent(event));
		}
	}
	
	private class MouseMotionEventListener implements MouseMotionListener
	{
		@Override
		public void mouseDragged(MouseEvent event)
		{
			queueEvent(new MouseDragEvent(event));
		}
		
		@Override
		public void mouseMoved(MouseEvent event)
		{
			queueEvent(new MouseMoveEvent(event));
		}
	}
	
	private class MouseWheelEventListener implements MouseWheelListener
	{
		@Override
		public void mouseWheelMoved(MouseWheelEvent event)
		{
			queueEvent(new WheelMoveEvent(event));
		}
	}
}