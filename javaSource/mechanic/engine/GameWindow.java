package mechanic.engine;

import globalResources.graphics.Canvas;
import globalResources.ui.UIHolder;
import globalResources.window.Window;
import globalResources.window.Window.Renderer;
import globalResources.window.Window.WindowEventListener;
import globalResources.window.WindowManager;

public class GameWindow
{
	private static Window window;
	
	static void init(int width, int height, String title)
	{
		if (Engine.isInitializing())
		{
			WindowManager.init();
			window = WindowManager.createWindow(width, height, title);
			window.addRenderer(new GameWindowRenderer());
			GameInput.init(window);
		}
	}
	
	/**
	 * Gets the UIHolder for the GameWindow
	 * @return the UIHolder for the GameWindow
	 */
	public static UIHolder getUIHolder()
	{
		return (UIHolder)window;
	}
	
	/**
	 * Registers a new WindowEventListener
	 * @param listener the listener to be registered
	 */
	public static void addListener(WindowEventListener listener)
	{
		window.addListener(listener);
	}
	
	public static Window getWindow()
	{
		return window;
	}
	
	/**
	 * Removes a listener
	 * @param listener the listener to be removed
	 */
	public static void removeListener(WindowEventListener listener)
	{
		window.removeListener(listener);
	}
	
	/**
	 * Gets the current title of the window
	 * @return the window title
	 */
	public static String getTitle()
	{
		return window.getTitle();
	}
	
	/**
	 * Sets the current title of the window
	 * @param title the new title
	 * @return the previous title
	 */
	public static String setTitle(String title)
	{
		return window.setTitle(title);
	}
	
	/**
	 * Sets the mouse cursor to be hidden while on this window
	 */
	public static void hideMouse()
	{
		window.hideMouse();
	}
	
	/**
	 * Sets the mouse cursor to be shown while on this window
	 */
	public static void showMouse()
	{
		window.showMouse();
	}
	
	/**
	 * Gets the x coordinate of the window on the monitor
	 * @return x coordinate of the window
	 */
	public static int getXPosition()
	{
		return window.getXPosition();
	}
	
	/**
	 * Gets the y coordinate of the window on the monitor
	 * @return y coordinate of the window
	 */
	public static int getYPosition()
	{
		return window.getYPosition();
	}
	
	/**
	 * Sets the location of this window on the monitor
	 * @param x x coordinate
	 * @param y y coordinate
	 */
	public static void setPosition(int x, int y)
	{
		window.setPosition(x, y);
	}
	
	/**
	 * Sets the pixel scale on the x axis of the window
	 * @param xScale pixel scale on the x axis
	 */
	public static void setXScale(double xScale)
	{
		window.setXScale(xScale);
	}
	
	/**
	 * Sets the pixel scale on the x axis of the window
	 * @return pixel scale on the x axis
	 * @see #setXScale(double)
	 */
	public static double getXScale()
	{
		return window.getXScale();
	}
	
	/**
	 * Sets the pixel scale on the y axis of the window
	 * @param yScale pixel scale on the y axis
	 */
	public static void setYScale(double yScale)
	{
		window.setYScale(yScale);
	}
	
	/**
	 * Sets the pixel scale on the y axis of the window
	 * @return pixel scale on the y axis
	 * @see #setYScale(double)
	 */
	public static double getYScale()
	{
		return window.getYScale();
	}
	
	/**
	 * Gets the width of the window
	 * @return window width
	 */
	public static int getWidth()
	{
		return window.getWidth();
	}
	
	/**
	 * Gets the height of the window
	 * @return window height
	 */
	public static int getHeight()
	{
		return window.getHeight();
	}
	
	/**
	 * Sets the dimensions of the window
	 * @param width window width
	 * @param height window height
	 */
	public static void resize(int width, int height)
	{
		window.resize(width, height);
	}
	
	/**
	 * Sets whether the user can resize the window
	 * @param resizable whether the user can resize the window
	 */
	public static void setResizable(boolean resizable)
	{
		window.setResizable(resizable);
	}
	
	/**
	 * Returns whether the user can resize the window
	 * @return whether the user can resize the window
	 */
	public static boolean resizable()
	{
		return window.resizable();
	}
	
	/**
	 * Sets the current tool tip for the window
	 * @param tip the tool tip
	 * <p>
	 * The tool tip is drawn near the mouse cursor
	 * </p>
	 */
	public static void toolTip(String tip)
	{
		window.toolTip(tip);
	}
	
	static void close()
	{
		window.close();
		Engine.stopEngine();
	}
	
	/**
	 * Gets a canvas for drawing to the window
	 * @return the canvas for the window
	 */
	public static Canvas getCanvas()
	{
		return window.getCanvas();
	}
	
	/**
	 * Gets the estimated amount of frames being drawn every second
	 * @return estimated FPS
	 */
	public static int getFramesPerSecond()
	{
		return window.getFramesPerSecond();
	}
	
	/**
	 * Sets the desired FPS of the window
	 * @param framesPerSecond the desired FPS of the window
	 */
	public static void setFramesPerSecond(double framesPerSecond)
	{
		window.setFramesPerSecond(framesPerSecond);
	}
	
	/**
	 * Gets the desired FPS of the window
	 * @return the desired FPS of the window
	 */
	public static double getFramesPerSecondSetting()
	{
		return window.getFramesPerSecondSetting();
	}
	
	/**
	 * Gets how many nanoseconds will need to pass before the next frame will be drawn
	 * @return nanoseconds until the next frame
	 */
	public static long getTimeTillNextFrame()
	{
		return window.getTimeTillNextFrame();
	}
	
	/**
	 * Sets whether the window will be capped at a desired FPS
	 * @param framesCapped whether the FPS of the window will be capped
	 * @see #setFramesPerSecond(double)
	 */
	public static void capFrames(boolean framesCapped)
	{
		window.capFrames(framesCapped);
	}
	
	/**
	 * Gets whether the window will be capped at a desired FPS
	 * @return whether the FPS of the window will be capped
	 * @see #capFrames(boolean)
	 */
	public static boolean framesCapped()
	{
		return window.framesCapped();
	}
	
	/**
	 * Gets whether a frame will be drawn at the next opportunity
	 * @return whether a frame will be drawn at the next opportunity
	 */
	public static boolean nextFrameDue()
	{
		return window.nextFrameDue();
	}
	
	/**
	 * Triggers a notification
	 * @param message the message to be displayed
	 */
	public static void notify(String message)
	{
		window.notify(message);
	}
	
	/**
	 * Checks if this window is marked to be closed
	 * @return if this window is marked to be closed
	 */
	public static boolean closed()
	{
		return window.closed();
	}
	
	static void update()
	{
		WindowManager.update();
	}
	
	static void destroy()
	{
		WindowManager.closeAll();
		WindowManager.update();
	}
	
	static void render()
	{
		WindowManager.render();
	}
	
	static class GameWindowRenderer extends Renderer
	{
		@Override
		public void render(Window window)
		{
			Engine.draw();
		}
	}
}