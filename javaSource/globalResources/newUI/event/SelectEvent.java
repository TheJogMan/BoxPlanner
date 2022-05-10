package globalResources.newUI.event;

import globalResources.newUI.NUIComponent;

public class SelectEvent extends Cancellable
{
	NUIComponent focusedComponent;
	
	public SelectEvent(NUIComponent focusedComponent)
	{
		this.focusedComponent = focusedComponent;
	}
	
	public NUIComponent getSelectedComponent()
	{
		return focusedComponent;
	}
	
	@Override
	protected void performCancellableAction()
	{
		stopProcessing();
	}
}