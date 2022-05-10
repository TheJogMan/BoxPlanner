package mechanic.engine;

import globalResources.ui.FrameInput.FrameInputListenerInterface;

public abstract class Module implements FrameInputListenerInterface
{
	public abstract void update();
	public abstract void preRender();
	public abstract void postRender();
	public abstract void remove();
}