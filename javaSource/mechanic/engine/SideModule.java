package mechanic.engine;

import globalResources.ui.FrameInput.FrameInputListenerInterface;

/**
 * Abstract scene
 */
public abstract class SideModule implements FrameInputListenerInterface
{
	protected abstract void update();
	/**
	 * Called during each render cycle of the engine before Entities and Physics have been rendered
	 */
	protected abstract void preRender();
	/**
	 * Called during each render cycle of the engine after Entities and Physics have been rendered
	 */
	protected abstract void postRender();
	protected abstract void unload();
	
	boolean persistent = false;
	
	protected void setPersistent(boolean persistent)
	{
		this.persistent = persistent;
	}
	
	public SideModule()
	{
		
	}
	
	public SideModule(boolean persistent)
	{
		this.persistent = persistent;
	}
	
	void loadModule()
	{
		try
		{
			GameInput.registerWindowInputListener(this);
		}
		catch (Exception e)
		{
			System.out.println("ERROR: Exception occurred while loading module!");
			e.printStackTrace();
		}
	}
	
	void unloadModule()
	{
		try
		{
			unload();
			GameInput.removeWindowInputListener(this);
		}
		catch (Exception e)
		{
			System.out.println("ERROR: Exception occurred while unloading module!");
			e.printStackTrace();
		}
	}
	
	void updateModule()
	{
		try
		{
			update();
		}
		catch (Exception e)
		{
			System.out.println("ERROR: Exception occurred while updating module!");
			e.printStackTrace();
		}
	}
	
	void preRenderModule()
	{
		try
		{
			preRender();
		}
		catch (Exception e)
		{
			System.out.println("ERROR: Exception occurred while pre rendering module!");
			e.printStackTrace();
		}
	}
	
	void postRenderModule()
	{
		try
		{
			postRender();
		}
		catch (Exception e)
		{
			System.out.println("ERROR: Exception occurred while post rendering Module!");
			e.printStackTrace();
		}
	}
}