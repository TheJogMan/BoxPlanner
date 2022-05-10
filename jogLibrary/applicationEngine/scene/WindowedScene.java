package jogLibrary.applicationEngine.scene;

import jogLibrary.applicationEngine.Engine;
import jogLibrary.applicationEngine.Scene;
import jogLibrary.applicationEngine.Window;
import jogLibrary.applicationEngine.WindowManager;

public abstract class WindowedScene extends Scene
{
	Window window;
	
	public WindowedScene(WindowManager manager, int width, int height, String title)
	{
		
	}
	
	@Override
	protected void load(Engine engine)
	{
		//show window
	}
	
	@Override
	protected void unload(Scene scene)
	{
		//hide window
	}
	
	public final Window window()
	{
		return window;
	}
}