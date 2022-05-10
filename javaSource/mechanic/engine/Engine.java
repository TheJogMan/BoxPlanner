package mechanic.engine;

import java.awt.Color;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Iterator;

import globalResources.GlobalResourcesInitializer;
import globalResources.richText.RichCharacter;
import globalResources.richText.RichColor;
import globalResources.richText.RichString;
import globalResources.richText.RichStringBuilder;
import globalResources.utilities.FIO;
import globalResources.utilities.Logger;
import globalResources.utilities.Logger.MessageType;
import globalResources.utilities.Logger.MessageWriter;
import globalResources.utilities.Logger.Priority;
import globalResources.utilities.ShiftingLinkedList;
import globalResources.utilities.Vector;
import mechanic.engine.CommandConsole.Message;
import mechanic.entities.EntityManager;
import mechanic.physics.PhysicalBody;
import mechanic.physics.Physics;

/**
 * This is the core of the game engine
 */
public class Engine
{
	static private CommandConsole console;
	
	static private boolean initializing = true;
	static private boolean shuttingDown = false;
	static private boolean running = false;
	static private boolean allowSleep;
	static private boolean keepRunning;
	static private EngineConfig config;
	
	static private long timeSinceLastUpdate;
	static private double updatesPerSecond;
	static private double updateTime;
	static private long timeOfLastUpdate;
	static private boolean updatesCapped;
	
	static private Scene currentScene;
	static Vector cameraPosition;
	static private PhysicalBody cameraBind;
	
	static private ArrayList<SideModule> sideModules;
	static private ArrayList<SideModule> moduleAdditionQueue;
	static private ArrayList<SideModule> moduleRemovalQueue;
	
	/**
	 * Runs the engine
	 * @param initialScene the initial game scene to be used
	 * @param windowWidth the width of the game window
	 * @param windowHeight the height of the game window
	 * @param windowTitle the title of the game window
	 * <p>
	 * The window parameters are irrelevant if the engine has already been initialized.
	 * </p>
	 */
	public static void run(Scene initialScene, int windowWidth, int windowHeight, String windowTitle, EngineConfig config)
	{
		if (initializing && !running)
		{
			init(windowWidth, windowHeight, windowTitle, config);
			run(initialScene);
		}
	}
	
	public static void run(Scene initialScene, String windowTitle)
	{
		run(initialScene, 800, 600, windowTitle);
	}
	
	public static void run(Scene initialScene, String windowTitle, boolean strippedDown)
	{
		run(initialScene, 800, 600, windowTitle, EngineConfig.create().setUseEntities(false).setUseParticles(false).setUsePhysics(false));
	}
	
	public static void run(Scene initialScene, int windowWidth, int windowHeight, String windowTitle)
	{
		run(initialScene, windowWidth, windowHeight, windowTitle, EngineConfig.create());
	}
	
	public static void run(Scene initialScene)
	{
		if (!initializing && !running)
		{
			try
			{
				running = true;
				setScene(initialScene);
				while (keepRunning)
				{
					GameWindow.update();
					if (GameWindow.closed()) break;
					if (nextUpdateDue())
					{
						update();
						if (!keepRunning) break;
					}
					if (GameWindow.nextFrameDue())
					{
						GameWindow.render();
					}
					if (keepRunning && allowSleep)
					{
						sleep();
					}
				}
				if (currentScene != null) currentScene.unloadScene();
				running = false;
				shuttingDown = true;
			}
			catch (Exception e)
			{
				Logger.defaultLogger.log("A Fatal Error has occurred! The engine will now shutdown!", e);
			}
			GameWindow.destroy();
		}
	}
	
	public static void resetEngine()
	{
		if (!initializing && !running && !shuttingDown)
		{
			initializing = true;
		}
	}
	
	public static Scene getCurrentScene()
	{
		return currentScene;
	}
	
	public static void reloadScene()
	{
		reset();
		currentScene.load();
	}
	
	static void reset()
	{
		if (currentScene != null) currentScene.unload();
		if (config.useEntities) EntityManager.clear();
		if (config.usePhysics) Physics.clear();
		//if (config.useParticles) Particle.clear();
		Engine.bindCamera(null);
		Engine.setCameraPosition(new Vector(0, 0));
		for (Iterator<SideModule> iterator = sideModules.iterator(); iterator.hasNext();)
		{
			SideModule module = iterator.next();
			if (!module.persistent) removeSideModule(module);
		}
	}
	
	/**
	 * Initializes the engine.
	 * @param windowWidth the width of the game window
	 * @param windowHeight the height of the game window
	 * @param windowTitle the title of the game window
	 * <p>
	 * It is not required for this to be called manually, if not already initialized, this will be called automatically when the engine is run.
	 * Calling this manually can be useful when you need certain features of the engine before running it.
	 * </p>
	 */
	public static void init(int windowWidth, int windowHeight, String windowTitle, EngineConfig config)
	{
		if (initializing)
		{
			Engine.config = config;
			writer = new ConsoleMessageWriter(System.out);
			Logger.defaultLogger.setMessageWriter(writer);
			
			System.setOut(new PrintStream(System.out)
			{
				@Override
				public void println(String x)
				{
					Logger.defaultLogger.log(x);
				}
			});
			RichStringBuilder loggerName = new RichStringBuilder();
			loggerName.setMainColor(RichColor.CYAN);
			loggerName.append("ENGINE");
			Logger.defaultLogger.setName(loggerName.build());
			Logger.defaultLogger.log("Initializing Engine");
			updatesCapped = true;
			keepRunning = true;
			allowSleep = true;
			timeSinceLastUpdate = 1;
			timeOfLastUpdate = System.nanoTime();
			sideModules = new ArrayList<SideModule>();
			moduleAdditionQueue = new ArrayList<SideModule>();
			moduleRemovalQueue = new ArrayList<SideModule>();
			setUpdatesPerSecond(60.0);
			
			GlobalResourcesInitializer.init();
			GameWindow.init(windowWidth, windowHeight, windowTitle);
			cameraPosition = new Vector(0, 0);
			cameraBind = null;
			if (config.usePhysics) Physics.init();
			if (config.useEntities) EntityManager.init();
			if (config.useParticles) Particle.init();
			
			console = new CommandConsole();
			
			initializing = false;
		}
	}
	
	public static void init(int windowWidth, int windowHeight, String windowTitle)
	{
		init(windowWidth, windowHeight, windowTitle, EngineConfig.create());
	}
	
	public static class EngineConfig
	{
		boolean usePhysics = true;
		boolean useEntities = true;
		boolean useParticles = true;
		
		public static EngineConfig create()
		{
			return new EngineConfig();
		}
		
		public static EngineConfig minimal()
		{
			return new EngineConfig().setUseEntities(false).setUseParticles(false).setUsePhysics(false);
		}
		
		public EngineConfig setUsePhysics(boolean usePhysics)
		{
			this.usePhysics = usePhysics;
			return this;
		}
		
		public EngineConfig setUseEntities(boolean useEntities)
		{
			this.useEntities = useEntities;
			return this;
		}
		
		public EngineConfig setUseParticles(boolean useParticles)
		{
			this.useParticles = useParticles;
			return this;
		}
	}
	
	public static CommandConsole getConsole()
	{
		return console;
	}
	
	static void draw()
	{
		currentScene.preRenderScene();
		for (Iterator<SideModule> iterator = sideModules.iterator(); iterator.hasNext();) iterator.next().preRenderModule();
		if (config.useEntities) EntityManager.draw();
		if (config.useParticles) Particle.draw();
		if (config.usePhysics) Physics.draw();
		currentScene.postRenderScene();
		for (Iterator<SideModule> iterator = sideModules.iterator(); iterator.hasNext();) iterator.next().postRenderModule();
	}
	
	static void update()
	{
		timeSinceLastUpdate = System.nanoTime() - timeOfLastUpdate;
		timeOfLastUpdate = System.nanoTime();
		processModuleQueue();
		if (config.usePhysics) Physics.update();
		if (config.useEntities) EntityManager.update();
		if (config.useParticles) Particle.update();
		currentScene.updateScene();
		for (Iterator<SideModule> iterator = sideModules.iterator(); iterator.hasNext();) iterator.next().updateModule();
		if (cameraBind != null)
		{
			cameraPosition.set(cameraBind.getPosition());
			cameraPosition.multiply(-1);
		}
	}
	
	static void sleep()
	{
		long updateSleep = getTimeTillNextUpdate();
		long renderSleep = GameWindow.getTimeTillNextFrame();
		long sleep = updateSleep;
		if (renderSleep < updateSleep) sleep = renderSleep;
		sleep -= 5000;
		sleep /= 1000000;
		try
		{
			if (sleep > 0)
			{
				Thread.sleep(sleep);
			}
		}
		catch (InterruptedException e)
		{
			Logger.defaultLogger.log("Could not put engine main loop to sleep for necessary amount of time.", e);
		}
	}
	
	/**
	 * Binds the camera position to a PhysicalBody
	 * @param body the body to bind the camera to
	 */
	public static void bindCamera(PhysicalBody body)
	{
		Engine.cameraBind = body;
	}
	
	/**
	 * Get the current camera position
	 * @return the position of the camera
	 */
	public static Vector getCameraPosition()
	{
		return cameraPosition.clone();
	}
	
	/**
	 * Gets an offset for rendering relative to the camera position
	 * @return cameraOffset
	 */
	public static Vector getCameraOffset()
	{
		Vector cameraPosition = new Vector(GameWindow.getWidth() / 2, GameWindow.getHeight() / 2);
		cameraPosition.add(Engine.getCameraPosition());
		return cameraPosition;
	}
	
	/**
	 * Set the current camera position
	 * @param cameraPosition the position of the camera
	 */
	public static void setCameraPosition(Vector cameraPosition)
	{
		Engine.cameraPosition.set(cameraPosition);
	}
	
	/**
	 * Set the current scene
	 * @param newScene the new scene
	 */
	public static void setScene(Scene newScene)
	{
		Logger.defaultLogger.log("Setting scene to " + newScene.getClass(), MessageType.INFO, Priority.LOW);
		reset();
		currentScene = newScene;
		newScene.loadScene();
		timeOfLastUpdate = System.nanoTime();
	}
	
	/**
	 * Gets whether the engine is running
	 * @return if the engine is running
	 */
	public static boolean isRunning()
	{
		return running;
	}
	
	/**
	 * Gets whether the engine is currently initializing
	 * @return if the engine is initializing
	 */
	public static boolean isInitializing()
	{
		return initializing;
	}
	
	/**
	 * Gets whether the engine is currently shutting down
	 * @return if the engine is shutting down
	 */
	public static boolean isShuttingDown()
	{
		return shuttingDown;
	}
	
	/**
	 * Sets how many times the Engine will try to run an update cycle per second
	 * @param updatesPerSecond intended UPS
	 */
	public static void setUpdatesPerSecond(double updatesPerSecond)
	{
		Engine.updatesPerSecond = updatesPerSecond;
		updateTime = 1.0 / updatesPerSecond;
	}
	
	/**
	 * Gets how many times the Engine will try to run an update cycle per second
	 * @return intended UPS
	 * @see #setUpdatesPerSecond(double)
	 */
	public static double getUpdatesPerSecondSetting()
	{
		return updatesPerSecond;
	}
	
	/**
	 * Sets whether or not update cycles will be restricted to a set UPS
	 * @param updatesCapped whether or not the UPS will be capped
	 * @see #setUpdatesPerSecond(double)
	 */
	public static void capUpdates(boolean updatesCapped)
	{
		Engine.updatesCapped = updatesCapped;
	}
	
	/**
	 * Gets whether or not update cycles will be restricted to a set UPS
	 * @return whether or not the UPS will be capped
	 * @see #setUpdatesPerSecond(double)
	 */
	public static boolean updatesCapped()
	{
		return updatesCapped;
	}
	
	/**
	 * Gets how many nanoseconds until the next update cycle
	 * @return time until next update cycle
	 * <p>
	 * If the UPS is not set to be capped, then this will always return 0.
	 * </p>
	 * @see #setUpdatesPerSecond(double)
	 */
	public static long getTimeTillNextUpdate()
	{
		long time = timeOfLastUpdate + (long)(updateTime * 1000000000.0) - System.nanoTime();
		if (time < 0 || !updatesCapped)
		{
			time = 0;
		}
		return time;
	}
	
	/**
	 * Gets whether an update cycle will be run during the next Engine loop cycle
	 * @return if an update cycle will be run
	 * @see #getTimeTillNextUpdate()
	 */
	public static boolean nextUpdateDue()
	{
		if (updatesCapped)
		{
			return (getTimeTillNextUpdate() <= 0);
		}
		return true;
	}
	
	/**
	 * Stops the main loop
	 */
	public static void stopEngine()
	{
		keepRunning = false;
	}
	
	/**
	 * Gets whether the Engine thread will be put to sleep during periods where nothing needs to be done
	 * @return whether sleeping is allowed
	 */
	public static boolean sleepAllowed()
	{
		return allowSleep;
	}
	
	/**
	 * Sets whether the Engine thread will be put to sleep during periods where nothing needs to be done
	 * @param allowSleep whether sleeping is allowed
	 * @see #sleepAllowed()
	 */
	public static void allowSleep(boolean allowSleep)
	{
		Engine.allowSleep = allowSleep;
	}
	
	/**
	 * Gets how many updates are occurring each second
	 * @return the current UPS
	 */
	public static int getUpdatesPerSecond()
	{
		return (int)(1000000000 / (timeSinceLastUpdate + 1));
	}
	
	/**
	 * Gets the time since the last update cycle in milliseconds
	 * @return time since the last update cycle
	 */
	public static long getDelta()
	{
		return timeSinceLastUpdate / 1000000;
	}
	
	private static void processModuleQueue()
	{
		int size = moduleAdditionQueue.size();
		ArrayList<SideModule> processedModules = new ArrayList<SideModule>();
		for (int index = 0; index < size; index++)
		{
			SideModule module = moduleAdditionQueue.get(index);
			sideModules.add(module);
			module.loadModule();
			processedModules.add(module);
		}
		for (Iterator<SideModule> iterator = processedModules.iterator(); iterator.hasNext();) moduleAdditionQueue.remove(iterator.next());
		size = moduleRemovalQueue.size();
		processedModules.clear();
		for (int index = 0; index < size; index++)
		{
			SideModule module = moduleRemovalQueue.get(index);
			sideModules.remove(module);
			module.unloadModule();
			processedModules.add(module);
		}
		for (Iterator<SideModule> iterator = processedModules.iterator(); iterator.hasNext();) moduleRemovalQueue.remove(iterator.next());
	}
	
	public static void addSideModule(SideModule module)
	{
		moduleAdditionQueue.add(module);
	}
	
	public static void removeSideModule(SideModule module)
	{
		moduleRemovalQueue.add(module);
	}
	
	public static boolean hasSideModule(SideModule module)
	{
		return (sideModules.contains(module) || moduleAdditionQueue.contains(module)) && !moduleRemovalQueue.contains(module);
	}
	
	public static Iterator<SideModule> moduleIterator()
	{
		return sideModules.iterator();
	}
	
	static ShiftingLinkedList<Message> history = new ShiftingLinkedList<Message>(200);
	static ShiftingLinkedList<Message> totalHistory = new ShiftingLinkedList<Message>(200);
	static ConsoleMessageWriter writer;
	
	static ArrayList<RichString> makeLines(RichString message, MessageType type, RichString loggerName, String timeStamp, Priority priority)
	{
		RichStringBuilder leading = new RichStringBuilder();
		leading.append("[" + timeStamp + "] - [");
		if (type.equals(MessageType.ERROR)) leading.append(new RichString(type.getPaddedName(), new RichColor(Color.RED)));
		else leading.append(type.getPaddedName());
		leading.append("] [");
		leading.append(loggerName);
		leading.append("] ");
		RichStringBuilder padding = new RichStringBuilder();
		for (int index = 0; index < leading.length() - 2; index++) padding.append(' ');
		padding.append(" | ");
		
		ArrayList<RichString> lines = new ArrayList<RichString>();
		RichStringBuilder line = new RichStringBuilder();
		for (int index = 0; index < message.length(); index++)
		{
			RichCharacter ch = message.charAt(index);
			if (ch.getCharacter() == '\n' && line.length() > 0)
			{
				if (lines.size() == 0) line.preAppend(leading);
				else line.preAppend(padding);
				lines.add(line.build());
				line.clear();
			}
			else line.append(ch);
		}
		if (line.length() > 0)
		{
			if (lines.size() == 0) line.preAppend(leading);
			else line.preAppend(padding);
			lines.add(line.build());
		}
		return lines;
	}
	
	static String makeValid(String name)
	{
		String newName = "";
		for (int index = 0; index < name.length(); index++)
		{
			char ch = name.charAt(index);
			if (ch == ':' || ch == '/') ch = '_';
			newName += ch;
		}
		return newName;
	}
	
	static class ConsoleMessageWriter implements MessageWriter
	{
		File logFile = null;
		PrintWriter writer = null;
		PrintStream out;
		
		ConsoleMessageWriter(PrintStream out)
		{
			this.out = out;
			try
			{
				FIO.ensureDirectory("logs");
				logFile = new File("logs/" + makeValid(Logger.getTime()) + ".txt");
				writer = new PrintWriter(logFile);
			}
			catch (FileNotFoundException e)
			{
				Logger.defaultLogger.log("Could not create log file writer", e);
			}
		}
		
		@Override
		public void write(RichString message, RichString loggerName, MessageType type)
		{
			history.add(new Message(message, loggerName, type, System.nanoTime(), Priority.DEFAULT, Logger.getTime()));
		}
		
		@Override
		public void rawWrite(RichString message, RichString loggerName, MessageType type, Priority priority)
		{
			totalHistory.add(new Message(message, loggerName, type, System.nanoTime(), priority, Logger.getTime()));
			if (writer != null)
			{
				ArrayList<RichString> lines = makeLines(message, type, loggerName, Logger.getTime(), priority);
				for (Iterator<RichString> iterator = lines.iterator(); iterator.hasNext();)
				{
					RichString str = iterator.next();
					writer.println(str);
					out.println(str);
				}
				writer.flush();
			}
		}
	}
}