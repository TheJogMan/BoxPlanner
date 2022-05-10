package globalResources.window;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;

/**
 * Manages all the windows belonging to this application
 */
public class WindowManager
{
	private static ArrayList<Window> windows;
	private static boolean initialized = false;
	private static ArrayList<WindowManagerEvent> eventQueue;
	private static ArrayList<WindowManagerEventListener> listeners;
	//useEventQueues currently would always be set to false, however it is implemented incase the feature is ever desired
	private static boolean useEventQueues = true;
	
	private static ArrayList<Window> windowAdditionQueue;
	
	/**
	 * Initializes the WindowManager
	 * <p>
	 * Windows cannot be created until the WindowManager has been initialized.
	 * Initializing the WindowManager multiples times has no additional effect.
	 * </p>
	 */
	public static void init()
	{
		if (!initialized)
		{
			windows = new ArrayList<Window>();
			windowAdditionQueue = new ArrayList<Window>();
			eventQueue = new ArrayList<WindowManagerEvent>();
			listeners = new ArrayList<WindowManagerEventListener>();
			initialized = true;
		}
	}
	
	/**
	 * Gets the amount of windows that currently exist
	 * @return window count
	 */
	public static int getWindowCount()
	{
		return windows.size();
	}
	
	/**
	 * Checks if a window exists
	 * @param window the window to check
	 * @return if the window exists
	 */
	public static boolean checkForWindow(Window window)
	{
		return windows.contains(window);
	}
	
	/**
	 * Checks if a window exists
	 * @param windowID id of the window to check
	 * @return if the window exists
	 */
	public static boolean checkForWindow(UUID windowID)
	{
		for (Iterator<Window> iterator = windows.iterator(); iterator.hasNext();)
		{
			Window window = iterator.next();
			if (window.getWindowID().compareTo(windowID) == 0)
			{
				return true;
			}
		}
		return false;
	}
	
	/**
	 * Gets a window with the given ID
	 * @param windowID id of the window to get
	 * @return the window, or null if it doesn't exist
	 */
	public static Window getWindow(UUID windowID)
	{
		if (initialized && windowID != null)
		{
			for (Iterator<Window> iterator = windows.iterator(); iterator.hasNext();)
			{
				Window window = iterator.next();
				if (window.getWindowID().compareTo(windowID) == 0)
				{
					return window;
				}
			}
		}
		return null;
	}
	
	/**
	 * Creates a new window
	 * @param width width of the window
	 * @param height height of the window
	 * @param title title of the window
	 * @return the created window
	 */
	public static Window createWindow(int width, int height, String title)
	{
		if (initialized)
		{
			Window window = new Window(width, height, title);
			addWindow(window);
			return window;
		}
		return null;
	}
	
	/**
	 * Closes all windows
	 */
	public static void closeAll()
	{
		for (Iterator<Window> iterator = windows.iterator(); iterator.hasNext();)
		{
			iterator.next().close();
		}
	}
	
	/**
	 * Updates the WindowManager and all the windows
	 */
	public static void update()
	{
		if (initialized)
		{
			processModuleQueue();
			int count = eventQueue.size();
			ArrayList<WindowManagerEvent> processedEvents = new ArrayList<WindowManagerEvent>();
			for (int index = 0; index < count; index++)
			{
				WindowManagerEvent event = eventQueue.get(index);
				event.process();
				processedEvents.add(event);
			}
			for (Iterator<WindowManagerEvent> iterator = processedEvents.iterator(); iterator.hasNext();) eventQueue.remove(iterator.next());
			List<Window> closedWindows = new ArrayList<Window>();
			for (Iterator<Window> iterator = windows.iterator(); iterator.hasNext();)
			{
				Window window = iterator.next();
				if (!window.remove)
				{
					window.update();
				}
				if (window.remove)
				{
					closedWindows.add(window);
				}
			}
			for (Iterator<Window> iterator = closedWindows.iterator(); iterator.hasNext();)
			{
				Window window = iterator.next();
				window.destroy();
				windows.remove(window);
			}
		}
	}
	
	/**
	 * Renders all the windows
	 */
	public static void render()
	{
		if (initialized)
		{
			for (Iterator<Window> iterator = windows.iterator(); iterator.hasNext();)
			{
				renderWindow(iterator.next(), true);
			}
		}
	}
	
	/**
	 * Get the amount of time until any window will need to render a new frame
	 * @return time in nanoseconds
	 */
	public static long getTimeTillNextProcess()
	{
		if (initialized && windows.size() > 0)
		{
			long time = Long.MAX_VALUE;
			for (Iterator<Window> iterator = windows.iterator(); iterator.hasNext();)
			{
				Window window = iterator.next();
				long windowTime = window.getTimeTillNextFrame();
				if (windowTime < time)
				{
					time = windowTime;
				}
			}
			return time;
		}
		return 0;
	}
	
	/**
	 * Gets if window event queues will be used
	 * @return if window event queues will be used
	 * <p>
	 * When true, window events will be held until the next update cycle, at which point they will be processed all at once
	 * otherwise, they will be processed immediately
	 * </p>
	 */
	public static boolean useEventQueues()
	{
		return useEventQueues;
	}
	
	/**
	 * Registers an event listener to the WindowManager
	 * @param listener the listener to register
	 */
	public static void registerListener(WindowManagerEventListener listener)
	{
		listeners.add(listener);
	}
	
	/**
	 * Removes an event listener from the WindowManager
	 * @param listener the listener to remove
	 */
	public static void removeListener(WindowManagerEventListener listener)
	{
		listeners.remove(listener);
	}
	
	static void renderWindow(Window window, boolean checkFrame)
	{
		if (!window.remove && (!checkFrame || window.nextFrameDue()))
		{
			window.render();
		}
	}
	
	static void cycleWindow(Window window, boolean checkFrame)
	{
		window.update();
		renderWindow(window, checkFrame);
	}
	
	static void queueEvent(WindowManagerEvent event)
	{
		if (useEventQueues)
		{
			
		}
		else
		{
			event.process();
		}
	}
	
	/**
	 * Listens to events from the WindowManager
	 */
	public static abstract class WindowManagerEventListener
	{
		/**
		 * Called when a new window is created
		 * @param window the new window
		 */
		public abstract void windowCreated(Window window);
	}
	
	static abstract class WindowManagerEvent
	{
		abstract void process();
	}
	
	static class WindowCreationEvent extends WindowManagerEvent
	{
		Window window;
		
		WindowCreationEvent(Window window)
		{
			this.window = window;
		}
		
		@Override
		void process()
		{
			for (Iterator<WindowManagerEventListener> iterator = listeners.iterator(); iterator.hasNext();) iterator.next().windowCreated(window);
		}
	}
	
	private static void processModuleQueue()
	{
		int size = windowAdditionQueue.size();
		ArrayList<Window> processedModules = new ArrayList<Window>();
		for (int index = 0; index < size; index++)
		{
			Window module = windowAdditionQueue.get(index);
			windows.add(module);
			processedModules.add(module);
		}
		for (Iterator<Window> iterator = processedModules.iterator(); iterator.hasNext();) windowAdditionQueue.remove(iterator.next());
	}
	
	static void addWindow(Window module)
	{
		windowAdditionQueue.add(module);
	}
}