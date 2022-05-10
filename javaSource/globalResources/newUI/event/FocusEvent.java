package globalResources.newUI.event;

import globalResources.newUI.NUIComponent;

public class FocusEvent extends Cancellable
{
	NUIComponent focusedComponent;
	
	public FocusEvent(NUIComponent focusedComponent)
	{
		this.focusedComponent = focusedComponent;
	}
	
	@Override
	protected void performCancellableAction()
	{
		stopProcessing();
	}
	
	public NUIComponent getFocusedComponent()
	{
		return focusedComponent;
	}
}