package globalResources.newUI.event;

public abstract class Cancellable extends NUIEvent
{
	boolean cancelled = false;
	
	protected abstract void performCancellableAction();
	
	public final boolean cancelled()
	{
		return cancelled;
	}
	
	public final void setCancelled(boolean cancelled)
	{
		this.cancelled = cancelled;
	}
	
	@Override
	public void performAction()
	{
		if (!cancelled) performCancellableAction();
	}
}