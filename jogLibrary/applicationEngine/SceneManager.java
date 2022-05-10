package jogLibrary.applicationEngine;

import java.util.UUID;

import jogLibrary.universal.EventQueue;
import jogLibrary.universal.EventQueue.Event;
import jogLibrary.universal.EventQueue.EventHandler;
import jogLibrary.universal.dataStructures.stack.LinkedStack;

public class SceneManager
{
	private LinkedStack<Scene> sceneStack = new LinkedStack<>();
	private UUID eventKey = UUID.randomUUID();
	private EventQueue eventQueue = new EventQueue(eventKey);
	private Engine engine;
	
	SceneManager(Engine engine)
	{
		this.engine = engine;
		eventQueue.addListener(new ChangeListener());
	}
	
	public void setScene(Scene scene)
	{
		new SetScene(scene);
	}
	
	public void switchScene(Scene scene)
	{
		new SwitchScene(scene);
	}
	
	public boolean hasScene()
	{
		return sceneStack.get() != null;
	}
	
	public Scene activeScene()
	{
		return sceneStack.get();
	}
	
	void processEvents()
	{
		eventQueue.process(eventKey);
	}
	
	long update()
	{
		processEvents();
		Scene scene = activeScene();
		long timeTillNextUpdate = 0;
		if (scene != null && !scene.kill)
		{
			timeTillNextUpdate = scene.updateScene();
			if (scene.kill)
			{
				sceneStack.pop();
				Scene newScene = activeScene();
				scene.unloadScene(newScene);
				scene.active = false;
				if (newScene != null)
				{
					newScene.active = true;
					newScene.loadScene(engine);
					scene = newScene;
				}
			}
		}
		return timeTillNextUpdate;
	}
	
	private class ChangeListener
	{
		@EventHandler
		public void onSceneChange(SceneChange change)
		{
			change.process();
		}
	}
	
	private abstract class SceneChange extends Event
	{
		Scene newScene;
		
		SceneChange(Scene newScene)
		{
			this.newScene = newScene;
			eventQueue.trigger(this);
		}
		
		protected abstract void process();
	}
	
	private class SetScene extends SceneChange
	{
		SetScene(Scene newScene)
		{
			super(newScene);
		}
		
		@Override
		protected void process()
		{
			if (hasScene())
			{
				Scene scene = activeScene();
				scene.unloadScene(newScene);
				scene.active = false;
			}
			sceneStack.replace(newScene);
			newScene.active = true;
			newScene.loadScene(engine);
		}
	}
	
	private class SwitchScene extends SceneChange
	{
		SwitchScene(Scene newScene)
		{
			super(newScene);
		}
		
		@Override
		protected void process()
		{
			if (hasScene())
			{
				Scene scene = activeScene();
				scene.unloadScene(newScene);
				scene.active = false;
			}
			sceneStack.push(newScene);
			newScene.active = true;
			newScene.loadScene(engine);
		}
	}
}