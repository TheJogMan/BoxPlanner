package jogLibrary.applicationEngine;

import java.util.UUID;

import jogLibrary.universal.EventQueue;

public class Engine implements Runnable
{
	private WindowManager windowManager;
	private SceneManager sceneManager;
	private EventQueue eventQueue;
	private UUID eventKey = UUID.randomUUID();
	
	public Engine()
	{
		eventQueue = new EventQueue(eventKey);
		windowManager = new WindowManager();
		sceneManager = new SceneManager(this);
	}
	
	public void run()
	{
		sceneManager.processEvents();
		while (sceneManager.hasScene())
		{
			eventQueue.process(eventKey);
			long timeTillNextTick = sceneManager.update();
			long timeTillNextFrame = windowManager.update();
			long sleepTime = timeTillNextTick > timeTillNextFrame ? timeTillNextFrame : timeTillNextTick;
			try
			{
				Thread.sleep(sleepTime);
			}
			catch (Exception e)
			{
				
			}
		}
	}
	
	public WindowManager windowManager()
	{
		return windowManager;
	}
	
	public SceneManager sceneManager()
	{
		return sceneManager;
	}
	
	public EventQueue eventQueue()
	{
		return eventQueue;
	}
}