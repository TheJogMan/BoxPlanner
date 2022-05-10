package globalResources.newUI.event;

public abstract class NUIEvent
{
	boolean continueProcessing = true;
	
	public abstract void performAction();
	
	public final boolean continueProcessing()
	{
		return continueProcessing;
	}
	
	public final void stopProcessing()
	{
		continueProcessing = false;
	}
}