package mechanic.engine;

import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;

import globalResources.ui.FrameInput;
import globalResources.ui.FrameInput.FrameInputListenerInterface;
import globalResources.utilities.Vector;
import globalResources.window.Window;

public class GameInput
{
	private static FrameInput input;
	
	static void init(Window window)
	{
		if (Engine.isInitializing())
		{
			input = window.getInput();
		}
	}
	
	/**
	 * Registers a new input listener
	 * @param listener the listener to be registered
	 */
	public static void registerWindowInputListener(FrameInputListenerInterface listener)
	{
		input.registerInputListener(listener);
	}
	
	/**
	 * Removes an input listener
	 * @param listener the listener to be removed
	 */
	public static void removeWindowInputListener(FrameInputListenerInterface listener)
	{
		input.removeInputListener(listener);
	}
	
	/**
	 * Get the x position of the mouse cursor
	 * @return x position of the mouse cursor
	 */
	public static int getMouseX()
	{
		return input.getMousePosition().getX();
	}
	
	/**
	 * Get the y position of the mouse cursor
	 * @return y position of the mouse cursor
	 */
	public static int getMouseY()
	{
		return input.getMousePosition().getY();
	}
	
	/**
	 * Gets a vector containing the position of the mouse within the game world
	 * @return mouse position
	 */
	public static Vector getMouseWorldPosition()
	{
		return new Vector(getMouseX() - Engine.cameraPosition.getX() - GameWindow.getWidth() / 2, getMouseY() - Engine.cameraPosition.getY() - GameWindow.getHeight() / 2);
	}
	
	/**
	 * Checks if a mouse button is currently pressed
	 * @param buttonCode MouseEvent code for the button
	 * @return whether the button is being pressed
	 * @see java.awt.event.MouseEvent
	 */
	public static boolean isButton(int buttonCode)
	{
		return input.isButton(buttonCode);
	}
	
	/**
	 * Checks if a key on the keyboard is currently pressed
	 * @param keyCode KeyEvent code for the key
	 * @return whether the key is pressed
	 * @see java.awt.event.KeyEvent
	 */
	public static boolean isKey(int keyCode)
	{
		return input.isKey(keyCode);
	}
	
	/**
	 * Checks if a control is currently active
	 * @param control the control to check
	 * @return if the control is active
	 */
	public static boolean isControl(Control control)
	{
		for (int index = 0; index < control.buttons.length; index++) if (isButton(control.buttons[index])) return true;
		for (int index = 0; index < control.keys.length; index++) if (isKey(control.keys[index])) return true;
		return false;
	}
	
	/**
	 * Checks if an input code corresponds to a given control
	 * @param code input code
	 * @param control the control to check
	 * @return if the code corresponds to the control
	 */
	public static boolean forControl(int code, Control control)
	{
		for (int index = 0; index < control.buttons.length; index++) if (control.buttons[index] == code) return true;
		for (int index = 0; index < control.keys.length; index++) if (control.keys[index] == code) return true;
		return false;
	}
	
	/**
	 * A control is an input that can be triggered by multiple components of multiple devices
	 * <p>
	 * For example, the UP control is triggered by both the W key as well as the Up Arrow key on the keyboard.
	 * </p>
	 */
	public static enum Control
	{
		UP		(new int[] {KeyEvent.VK_W, KeyEvent.VK_UP}, new int[] {}),
		DOWN	(new int[] {KeyEvent.VK_S, KeyEvent.VK_DOWN}, new int[] {}),
		LEFT	(new int[] {KeyEvent.VK_A, KeyEvent.VK_LEFT}, new int[] {}),
		RIGHT	(new int[] {KeyEvent.VK_D, KeyEvent.VK_RIGHT}, new int[] {}),
		LCLICK	(new int[] {}, new int[] {MouseEvent.BUTTON1}),
		RCLICK	(new int[] {}, new int[] {MouseEvent.BUTTON3}),
		MCLICK	(new int[] {}, new int[] {MouseEvent.BUTTON2});
		
		int[] keys;
		int[] buttons;
		
		Control(int[] keys, int[] buttons)
		{
			this.keys = keys;
			this.buttons = buttons;
		}
	}
}