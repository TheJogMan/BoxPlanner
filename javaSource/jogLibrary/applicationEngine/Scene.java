package jogLibrary.applicationEngine;

import jogLibrary.universal.Time;

public abstract class Scene
{
	private int targetUPS = 60;
	boolean active = false;
	boolean kill = false;
	private Engine engine;
	private long timeOfLastUpdate = 0;
	private long durationOfLastUpdate = 0;
	private long updateDelta = 1000 / 60;
	
	protected abstract void update(long deltaMillis);
	
	protected void load(Engine engine)
	{
		
	}
	
	protected void unload(Scene newScene)
	{
		
	}
	
	final long updateScene()
	{
		long time = Time.currentTimeMillis() - timeOfLastUpdate;
		while (time > updateDelta)
		{
			time -= updateDelta;
			long currentTime = Time.currentTimeMillis();
			long timeSinceLastUpdate = currentTime - timeOfLastUpdate;
			timeOfLastUpdate = currentTime;
			try
			{
				update(timeSinceLastUpdate);
			}
			catch (Exception e)
			{
				System.err.println("Error occurred while updating scene.");
				e.printStackTrace(System.err);
			}
			durationOfLastUpdate = Time.currentTimeMillis() - currentTime;
			time += durationOfLastUpdate;
		}
		return timeOfLastUpdate + updateDelta - Time.currentTimeMillis();
	}
	
	final void loadScene(Engine engine)
	{
		this.engine = engine;
		engine.eventQueue().addListener(this);
		try
		{
			load(engine);
		}
		catch (Exception e)
		{
			System.err.println("Error occured while loading scene.");
			e.printStackTrace(System.err);
		}
	}
	
	final void unloadScene(Scene newScene)
	{
		try
		{
			unload(newScene);
		}
		catch (Exception e)
		{
			System.err.println("Error occurred while unloading scene.");
			e.printStackTrace(System.err);
		}
		engine.eventQueue().removeListener(this);
		engine = null;
	}
	
	protected final int targetUPS()
	{
		return targetUPS;
	}
	
	protected final void setUPS(int target)
	{
		targetUPS = target;
		updateDelta = 1000 / target;
	}
	
	public final Engine engine()
	{
		return engine;
	}
	
	public final float ups()
	{
		if (durationOfLastUpdate > 1000)
			return 1000.0f / (float)durationOfLastUpdate;
		else
			return 1000 / durationOfLastUpdate;
	}
	
	protected final void kill()
	{
		kill = true;
	}
	
	public final boolean active()
	{
		return active;
	}
}