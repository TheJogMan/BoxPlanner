package globalResources.window;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.awt.image.BufferedImage;
import java.awt.image.VolatileImage;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.UUID;

import javax.swing.JFrame;

import globalResources.graphics.Canvas;
import globalResources.newUI.NUIHolder;
import globalResources.newUI.WindowHolder;
import globalResources.ui.FrameInput;
import globalResources.ui.UIHolder;

/**
 * Window
 */
public class Window extends UIHolder
{
	JFrame frame;
	CustomBuffer buffer;
	Canvas canvas;
	FrameInput input;
	UUID windowID;
	double framesPerSecond;
	double frameTime;
	long timeOfLastFrame;
	boolean remove;
	boolean framesCapped;
	boolean forceFrame;
	boolean resizeable;
	boolean mouseHidden;
	long timeSinceLastFrame;
	ArrayList<WindowEventObject> eventQueue;
	ArrayList<WindowEventListener> listeners;
	ArrayList<RendererInterface> renderers;
	SelfListener selfListener;
	WindowHolder nuiHolder;
	
	String toolTip;
	ArrayList<Notification> notifications;
	
	Cursor hiddenCursor;
	Cursor defaultCursor;
	
	double xScale = 1.0;
	double yScale = 1.0;
	
	int xOriginOffset = 8;
	int yOriginOffset = 31;
	//both increase by 5 when window is resizable; X: 3, Y: 26 when not
	
	int xBoundOffset = 8;
	int yBoundOffset = 8;
	//both increase by 5 when window is resizable; X: 3, Y: 3 when not
	
	Window(int width, int height, String title)
	{
		super(null, null);
		
		eventQueue = new ArrayList<WindowEventObject>();
		listeners = new ArrayList<WindowEventListener>();
		renderers = new ArrayList<RendererInterface>();
		notifications = new ArrayList<Notification>();
		selfListener = new SelfListener();
		
		frame = new JFrame(title);
		frame.setPreferredSize(new Dimension(width + xOriginOffset + xBoundOffset, height + yOriginOffset + yBoundOffset));
		frame.pack();
		frame.addWindowListener(new RootWindowEventListener());
		frame.addComponentListener(new ComponentEventListener());
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
		frame.setResizable(true);
		frame.toFront();
		
		defaultCursor = frame.getContentPane().getCursor();
		hiddenCursor = Toolkit.getDefaultToolkit().createCustomCursor(new BufferedImage(16, 16, BufferedImage.TYPE_INT_ARGB), new Point(0, 0), "HiddenCursor");
		
		input = new FrameInput(frame);
		
		createDrawable(true);
		
		windowID = UUID.randomUUID();
		remove = false;
		setFramesPerSecond(60.0);
		timeOfLastFrame = 0;
		timeSinceLastFrame = 1;
		framesCapped = true;
		forceFrame = false;
		resizeable = true;
		mouseHidden = false;
		processEvents();
		super.inputSource = input;
		nuiHolder = new WindowHolder(this);
	}
	
	public NUIHolder getNUIHolder()
	{
		return nuiHolder;
	}
	
	/**
	 * Registers a new WindowEventListener
	 * @param listener the listener to be registered
	 */
	public void addListener(WindowEventListener listener)
	{
		listeners.add(listener);
	}
	
	/**
	 * Removes a listener
	 * @param listener the listener to be removed
	 */
	public void removeListener(WindowEventListener listener)
	{
		listeners.remove(listener);
	}
	
	/**
	 * Gets the current title of the window
	 * @return the window title
	 */
	public String getTitle()
	{
		return frame.getTitle();
	}
	
	/**
	 * Sets the current title of the window
	 * @param title the new title
	 * @return the previous title
	 */
	public String setTitle(String title)
	{
		String previousTitle = frame.getTitle();
		frame.setTitle(title);
		return previousTitle;
	}
	
	/**
	 * Sets the mouse cursor to be hidden while on this window
	 */
	public void hideMouse()
	{
		if (!mouseHidden)
		{
			frame.getContentPane().setCursor(hiddenCursor);
			mouseHidden = true;
		}
	}
	
	/**
	 * Sets the mouse cursor to be shown while on this window
	 */
	public void showMouse()
	{
		if (mouseHidden)
		{
			frame.getContentPane().setCursor(defaultCursor);
			mouseHidden = false;
		}
	}
	
	/**
	 * Gets the x coordinate of the window on the monitor
	 * @return x coordinate of the window
	 */
	public int getXPosition()
	{
		return frame.getX();
	}
	
	/**
	 * Gets the y coordinate of the window on the monitor
	 * @return y coordinate of the window
	 */
	public int getYPosition()
	{
		return frame.getY();
	}
	
	/**
	 * Sets the location of this window on the monitor
	 * @param x x coordinate
	 * @param y y coordinate
	 */
	public void setPosition(int x, int y)
	{
		frame.setLocation(x, y);
	}
	
	/**
	 * Sets the pixel scale on the x axis of the window
	 * @param xScale pixel scale on the x axis
	 */
	public void setXScale(double xScale)
	{
		this.xScale = xScale;
	}
	
	/**
	 * Sets the pixel scale on the x axis of the window
	 * @return pixel scale on the x axis
	 * @see #setXScale(double)
	 */
	public double getXScale()
	{
		return xScale;
	}
	
	/**
	 * Sets the pixel scale on the y axis of the window
	 * @param yScale pixel scale on the y axis
	 */
	public void setYScale(double yScale)
	{
		this.yScale = yScale;
	}
	
	/**
	 * Sets the pixel scale on the y axis of the window
	 * @return pixel scale on the y axis
	 * @see #setYScale(double)
	 */
	public double getYScale()
	{
		return yScale;
	}
	
	/**
	 * Gets the width of the window
	 * @return window width
	 */
	public int getWidth()
	{
		return frame.getWidth() - (xOriginOffset + xBoundOffset);
	}
	
	/**
	 * Gets the height of the window
	 * @return window height
	 */
	public int getHeight()
	{
		return frame.getHeight() - (yOriginOffset + yBoundOffset);
	}
	
	/**
	 * Sets the dimensions of the window
	 * @param width window width
	 * @param height window height
	 */
	public void resize(int width, int height)
	{
		frame.setPreferredSize(new Dimension(width + xOriginOffset + xBoundOffset, height + yOriginOffset + yBoundOffset));
		createDrawable(true);
		queueEvent(new WindowEventObject(WindowEvents.RESIZE, false, true));
	}
	
	/**
	 * Sets whether the user can resize the window
	 * @param resizable whether the user can resize the window
	 */
	public void setResizable(boolean resizable)
	{
		frame.setResizable(resizable);
		if (resizable != this.resizeable)
		{
			if (resizable)
			{
				xOriginOffset += 5;
				yOriginOffset += 5;
				xBoundOffset += 5;
				yBoundOffset += 5;
			}
			else
			{
				xOriginOffset -= 5;
				yOriginOffset -= 5;
				xBoundOffset -= 5;
				yBoundOffset -= 5;
			}
			frame.setPreferredSize(new Dimension(getWidth() + xOriginOffset + xBoundOffset, getHeight() + yOriginOffset + yBoundOffset));
			createDrawable(true);
		}
		this.resizeable = resizable;
	}
	
	/**
	 * Returns whether the user can resize the window
	 * @return whether the user can resize the window
	 */
	public boolean resizable()
	{
		return resizeable;
	}
	
	/**
	 * Adds a renderer to this window
	 * @param renderer the renderer to be added
	 */
	public void addRenderer(RendererInterface renderer)
	{
		renderers.add(renderer);
	}
	
	/**
	 * Removes a renderer from this window
	 * @param renderer the renderer to be removed
	 */
	public void removeRendere(RendererInterface renderer)
	{
		renderers.remove(renderer);
	}
	
	/**
	 * Sets the current tool tip for the window
	 * @param tip the tool tip
	 * <p>
	 * The tool tip is drawn near the mouse cursor
	 * </p>
	 */
	public void toolTip(String tip)
	{
		toolTip = tip;
	}
	
	/**
	 * Closes the window
	 */
	public void close()
	{
		remove = true;
	}
	
	/**
	 * Checks if this window is marked to be closed
	 * @return if this window is marked to be closed
	 */
	public boolean closed()
	{
		return remove;
	}
	
	/**
	 * Gets a canvas for drawing to the window
	 * @return the canvas for the window
	 */
	public Canvas getCanvas()
	{
		return canvas;
	}
	
	/**
	 * Gets the estimated amount of frames being drawn every second
	 * @return estimated FPS
	 */
	public int getFramesPerSecond()
	{
		return (int)(1000000000 / (timeSinceLastFrame + 1));
	}
	
	/**
	 * Sets the desired FPS of the window
	 * @param framesPerSecond the desired FPS of the window
	 */
	public void setFramesPerSecond(double framesPerSecond)
	{
		this.framesPerSecond = framesPerSecond;
		frameTime = 1.0 / framesPerSecond;
	}
	
	/**
	 * Gets the desired FPS of the window
	 * @return the desired FPS of the window
	 */
	public double getFramesPerSecondSetting()
	{
		return framesPerSecond;
	}
	
	/**
	 * Gets how many nanoseconds will need to pass before the next frame will be drawn
	 * @return nanoseconds until the next frame
	 */
	public long getTimeTillNextFrame()
	{
		long time = timeOfLastFrame + (long)(frameTime * 1000000000.0) - System.nanoTime();
		if (time < 0 || forceFrame || !framesCapped)
		{
			time = 0;
		}
		return time;
	}
	
	/**
	 * Sets whether the window will be capped at a desired FPS
	 * @param framesCapped whether the FPS of the window will be capped
	 * @see #setFramesPerSecond(double)
	 */
	public void capFrames(boolean framesCapped)
	{
		this.framesCapped = framesCapped;
	}
	
	/**
	 * Gets whether the window will be capped at a desired FPS
	 * @return whether the FPS of the window will be capped
	 * @see #capFrames(boolean)
	 */
	public boolean framesCapped()
	{
		return framesCapped;
	}
	
	/**
	 * Gets whether a frame will be drawn at the next opportunity
	 * @return whether a frame will be drawn at the next opportunity
	 */
	public boolean nextFrameDue()
	{
		if (framesCapped)
		{
			return (getTimeTillNextFrame() == 0);
		}
		return true;
	}
	
	/**
	 * Triggers a notification
	 * @param message the message to be displayed
	 */
	public void notify(String message)
	{
		notifications.add(new Notification(message));
	}
	
	/**
	 * Gets the ID of this window
	 * @return windowID
	 */
	public UUID getWindowID()
	{
		return windowID;
	}
	
	void createDrawable(boolean remakeBuffer)
	{
		if (remakeBuffer)
		{
			if (buffer == null)
			{
				buffer = new CustomBuffer();
			}
			else
			{
				buffer.remakeBuffer();
			}
		}
		Graphics2D graphics = buffer.getDrawGraphics();
		graphics.translate(xOriginOffset, yOriginOffset);
		Rectangle clip = new Rectangle(0, 0, frame.getWidth() - (xOriginOffset + xBoundOffset), frame.getHeight() - (yOriginOffset + yBoundOffset));
		if (canvas != null)
		{
			canvas.setGraphics(graphics, clip);
		}
		else
		{
			canvas = new Canvas(graphics, clip);
		}
		while (buffer.eitherContentsLost());
		super.canvas = canvas;
	}
	
	void queueEvent(WindowEventObject event)
	{
		if (WindowManager.useEventQueues())
		{
			eventQueue.add(event);
		}
		else
		{
			triggerEvent(event);
		}
	}
	
	void processEvents()
	{
		int count = eventQueue.size();
		ArrayList<WindowEventObject> processedEvents = new ArrayList<WindowEventObject>();
		for (int index = 0; index < count; index++)
		{
			WindowEventObject event = eventQueue.get(index);
			triggerEvent(event);
			processedEvents.add(event);
		}
		for (Iterator<WindowEventObject> iterator = processedEvents.iterator(); iterator.hasNext();) eventQueue.remove(iterator.next());
	}
	
	void update()
	{
		processEvents();
		input.processEventQueue();
		nuiHolder.update();
		updateUI();
		ArrayList<Notification> removedNotifications = new ArrayList<Notification>();
		int count = notifications.size();
		for (int index = 0; index < count; index++)
		{
			Notification notification = notifications.get(index);
			if (notification.isDead()) removedNotifications.add(notification);
		}
		for (Iterator<Notification> iterator = removedNotifications.iterator(); iterator.hasNext();) notifications.remove(iterator.next());
		buffer.validate();
	}
	
	void destroy()
	{
		input.dispose();
		if (frame != null) frame.dispose();
	}
	
	void callRenderers()
	{
		for (Iterator<RendererInterface> iterator = renderers.iterator(); iterator.hasNext();) iterator.next().render(this);
		renderUI();
		nuiHolder.draw();
	}
	
	void render()
	{
		forceFrame = false;
		if (!remove)
		{
			timeSinceLastFrame = System.nanoTime() - timeOfLastFrame;
			timeOfLastFrame = System.nanoTime();
			toolTip = "";
			do
			{
				callRenderers();
				buffer.validateCurrent();
			}
			while (buffer.contentsRestored());
			if (toolTip.length() > 0)
			{
				int x = input.getMousePosition().getX();
				int y = input.getMousePosition().getY();
				y+=16;
				
				int gap = 2;
				
				canvas.drawRect(x, y, canvas.getStringWidth(toolTip) + gap * 4, canvas.getFontHeight() + gap * 4, Color.BLACK);
				canvas.drawRect(x + gap, y + gap, canvas.getStringWidth(toolTip) + gap * 2, canvas.getFontHeight() + gap * 2);
				canvas.drawText(toolTip, x + gap * 2, y + gap * 2 , Color.BLACK);
			}
			int y = canvas.getHeight() - Notification.gap;
			int count = notifications.size();
			for (int index = 0; index < count; index++) y -= (notifications.get(index).draw(y) + Notification.gap);
			buffer.show();
			createDrawable(false);
		}
	}
	
	void triggerEvent(WindowEventObject event)
	{
		if (event != null && event.event != null)
		{
			if (event.event.compareTo(WindowEvents.CLOSE) == 0)
			{
				if (event.triggerSelfListener) selfListener.windowClose();
				if (event.triggerOtherListeners)
				{
					for (Iterator<WindowEventListener> iterator = listeners.iterator(); iterator.hasNext();)
					{
						iterator.next().windowClose();
					}
				}
			}
			else if (event.event.compareTo(WindowEvents.MOVE) == 0)
			{
				if (event.triggerSelfListener) selfListener.windowMove();
				if (event.triggerOtherListeners)
				{
					for (Iterator<WindowEventListener> iterator = listeners.iterator(); iterator.hasNext();)
					{
						iterator.next().windowMove();
					}
				}
			}
			else if (event.event.compareTo(WindowEvents.RESIZE) == 0)
			{
				if (event.triggerSelfListener) selfListener.windowResize();
				if (event.triggerOtherListeners)
				{
					for (Iterator<WindowEventListener> iterator = listeners.iterator(); iterator.hasNext();)
					{
						iterator.next().windowResize();
					}
				}
			}
		}
	}
	
	class Notification
	{
		static final int lifeSpan = 5000;//milliseconds
		static final int gap = 2;
		
		String message;
		long creationTime;
		
		Notification(String message)
		{
			this.message = message;
			creationTime = System.currentTimeMillis();
		}
		
		int draw(int y)
		{
			Canvas canvas = getCanvas();
			
			int height = canvas.getFontHeight() + gap * 4;
			y -= height;
			
			canvas.drawRect(canvas.getWidth() - (gap * 5 + canvas.getStringWidth(message)), y, gap * 4 + canvas.getStringWidth(message), gap * 4 + canvas.getFontHeight(), Color.BLACK);
			canvas.drawRect(canvas.getWidth() - (gap * 4 + canvas.getStringWidth(message)), y + gap, gap * 2 + canvas.getStringWidth(message), gap * 2 + canvas.getFontHeight());
			canvas.drawText(message, canvas.getWidth() - (gap * 3 + canvas.getStringWidth(message)), y + gap * 2, Color.BLACK);
			
			return height;
		}
		
		boolean isDead()
		{
			long age = System.currentTimeMillis() - creationTime;
			if (age > lifeSpan) return true;
			return false;
		}
	}
	
	private class WindowEventObject
	{
		WindowEvents event;
		boolean triggerSelfListener;
		boolean triggerOtherListeners;
		
		WindowEventObject(WindowEvents event, boolean triggerSelfListener, boolean triggerOtherListeners)
		{
			this.event = event;
			this.triggerSelfListener = triggerSelfListener;
			this.triggerOtherListeners = triggerOtherListeners;
		}
	}
	
	public static interface RendererInterface
	{
		/**
		 * Called when the window is rendered
		 * @param window the window being rendered
		 */
		public void render(Window window);
	}
	
	/**
	 * A generic renderer for a window
	 */
	public abstract static class Renderer implements RendererInterface
	{
		
	}
	
	/**
	 * Abstract listener for window events
	 */
	public abstract static class WindowEventListener
	{
		/**
		 * Called whenever the window is resized
		 */
		public abstract void windowResize();
		/**
		 * Called whenever the window is moved around on the monitor
		 */
		public abstract void windowMove();
		/**
		 * Called when the window, and the rest of the engine, is closed
		 */
		public abstract void windowClose();
	}
	
	private class SelfListener extends WindowEventListener
	{
		@Override
		public void windowResize()
		{
			createDrawable(true);
		}
		
		@Override
		public void windowMove()
		{
			createDrawable(true);
		}
		
		@Override
		public void windowClose()
		{
			close();
		}
	}
	
	class ComponentEventListener implements ComponentListener
	{
		@Override
		public void componentHidden(ComponentEvent arg0)
		{
			
		}
		
		@Override
		public void componentMoved(ComponentEvent arg0)
		{
			queueEvent(new WindowEventObject(WindowEvents.MOVE, true, true));
		}
		
		@Override
		public void componentResized(ComponentEvent arg0)
		{
			queueEvent(new WindowEventObject(WindowEvents.RESIZE, true, true));
		}
		
		@Override
		public void componentShown(ComponentEvent arg0)
		{
			
		}
	}
	
	class RootWindowEventListener implements WindowListener
	{
		@Override
		public void windowActivated(WindowEvent arg0)
		{
			
		}
		
		@Override
		public void windowClosed(WindowEvent arg0)
		{
			
		}
		
		@Override
		public void windowClosing(WindowEvent arg0)
		{
			queueEvent(new WindowEventObject(WindowEvents.CLOSE, true, true));
		}
		
		@Override
		public void windowDeactivated(WindowEvent arg0)
		{
			
		}
		
		@Override
		public void windowDeiconified(WindowEvent arg0)
		{
			
		}
		
		@Override
		public void windowIconified(WindowEvent arg0)
		{
			
		}
		
		@Override
		public void windowOpened(WindowEvent arg0)
		{
			
		}
	}
	
	private class CustomBuffer
	{
		VolatileImage buffer0;
		VolatileImage buffer1;
		boolean bufferState;
		boolean currentBufferRecentlyRestored;
		boolean buffersCreated;
		

		CustomBuffer()
		{
			bufferState = true;
			buffersCreated = false;
			currentBufferRecentlyRestored = false;
			remakeBuffer();
		}
		
		void remakeBuffer()
		{
			if (buffersCreated)
			{
				buffer0.flush();
				buffer1.flush();
			}
			buffersCreated = true;
			buffer0 = frame.createVolatileImage(frame.getWidth(), frame.getHeight());
			buffer1 = frame.createVolatileImage(frame.getWidth(), frame.getHeight());
		}
		
		boolean eitherContentsLost()
		{
			boolean first = buffer0.contentsLost();
			boolean second = buffer1.contentsLost();
			if (first || second)
			{
				return true;
			}
			return false;
		}
		
		void validate()
		{
			int result = buffer0.validate(frame.getGraphicsConfiguration());
			if (result == VolatileImage.IMAGE_INCOMPATIBLE)
			{
				buffer0.flush();
				buffer0 = frame.createVolatileImage(frame.getWidth(), frame.getHeight());
			}
			result = buffer1.validate(frame.getGraphicsConfiguration());
			if (result == VolatileImage.IMAGE_INCOMPATIBLE)
			{
				buffer1.flush();
				buffer1 = frame.createVolatileImage(frame.getWidth(), frame.getHeight());
			}
		}
		
		void validateCurrent()
		{
			VolatileImage buffer = getCurrentBuffer();
			if (buffer.contentsLost())
			{
				int result = buffer.validate(frame.getGraphicsConfiguration());
				boolean recreated = false;
				if (result == VolatileImage.IMAGE_INCOMPATIBLE)
				{
					recreated = true;
					buffer.flush();
					buffer = frame.createVolatileImage(frame.getWidth(), frame.getHeight());
				}
				if (result == VolatileImage.IMAGE_RESTORED || recreated)
				{
					currentBufferRecentlyRestored = true;
				}
			}
		}
		
		void show()
		{
			VolatileImage buffer = getCurrentBuffer();
			if (!buffer.contentsLost())
			{
				Graphics graphics = frame.getGraphics();
				graphics.drawImage(buffer, 0, 0, frame);
				graphics.dispose();
			}
			bufferState = !bufferState;
			validateCurrent();
			currentBufferRecentlyRestored = false;
		}
		
		boolean contentsRestored()
		{
			return currentBufferRecentlyRestored;
		}
		
		VolatileImage getCurrentBuffer()
		{
			if (bufferState)
			{
				return buffer0;
			}
			else
			{
				return buffer1;
			}
		}
		
		Graphics2D getDrawGraphics()
		{
			return getCurrentBuffer().createGraphics();
		}
	}
	
	public VolatileImage getCurrentImage()
	{
		return buffer.getCurrentBuffer();
	}
	
	enum WindowEvents
	{
		RESIZE, MOVE, CLOSE
	}
}