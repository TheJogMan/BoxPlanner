package mechanic.engine;

import java.util.ArrayList;
import java.util.Iterator;

import globalResources.ui.FrameInput.FrameInputListenerInterface;

/**
 * Abstract scene
 */
public abstract class Scene implements FrameInputListenerInterface
{
	private ArrayList<Module> modules;
	private boolean active;
	
	/**
	 * Called when this scene is loaded into the engine
	 */
	protected abstract void load();
	/**
	 * Called when this scene is unloaded from the engine
	 */
	protected abstract void unload();
	/**
	 * Called during each update cycle of the engine
	 */
	protected abstract void update();
	/**
	 * Called during each render cycle of the engine before Entities and Physics have been rendered
	 */
	protected abstract void preRender();
	/**
	 * Called during each render cycle of the engine after Entities and Physics have been rendered
	 */
	protected abstract void postRender();
	
	public Scene()
	{
		active = true;
		modules = new ArrayList<Module>();
	}
	
	protected void addModule(Module module)
	{
		GameInput.registerWindowInputListener(module);
		modules.add(module);
	}
	
	protected void removeModule(Module module)
	{
		modules.remove(module);
		module.remove();
		if (active) GameInput.removeWindowInputListener(module);
	}
	
	void loadScene()
	{
		try
		{
			GameInput.registerWindowInputListener(this);
			load();
		}
		catch (Exception e)
		{
			System.out.println("ERROR: Exception occurred while loading scene!");
			e.printStackTrace();
		}
	}
	
	void unloadScene()
	{
		try
		{
			unload();
			active = false;
			for (Iterator<Module> iterator = modules.iterator(); iterator.hasNext();) removeModule(iterator.next());
			GameInput.removeWindowInputListener(this);
		}
		catch (Exception e)
		{
			System.out.println("ERROR: Exception occurred while unloading scene!");
			e.printStackTrace();
		}
	}
	
	void updateScene()
	{
		try
		{
			for (Iterator<Module> iterator = modules.iterator(); iterator.hasNext();) iterator.next().update();
			update();
		}
		catch (Exception e)
		{
			System.out.println("ERROR: Exception occurred while updating scene!");
			e.printStackTrace();
		}
	}
	
	void preRenderScene()
	{
		try
		{
			preRender();
			for (Iterator<Module> iterator = modules.iterator(); iterator.hasNext();) iterator.next().preRender();
		}
		catch (Exception e)
		{
			System.out.println("ERROR: Exception occurred while pre rendering scene!");
			e.printStackTrace();
		}
	}
	
	void postRenderScene()
	{
		try
		{
			postRender();
			for (Iterator<Module> iterator = modules.iterator(); iterator.hasNext();) iterator.next().postRender();
		}
		catch (Exception e)
		{
			System.out.println("ERROR: Exception occurred while post rendering scene!");
			e.printStackTrace();
		}
	}
}