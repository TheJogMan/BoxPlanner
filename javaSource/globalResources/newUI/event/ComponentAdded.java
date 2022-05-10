package globalResources.newUI.event;

import globalResources.dataTypes.LinkedList.LinkedElement;
import globalResources.newUI.NUIComponent;
import globalResources.newUI.NUIHolder;

public class ComponentAdded extends Cancellable
{
	NUIComponent component;
	NUIHolder parent;
	LinkedElement<NUIComponent> element;
	
	public ComponentAdded(NUIComponent component, NUIHolder parent)
	{
		this.component = component;
		this.parent = parent;
	}
	
	public NUIComponent getComponent()
	{
		return component;
	}
	
	public NUIHolder getParent()
	{
		return parent;
	}
	
	public void setElement(LinkedElement<NUIComponent> element)
	{
		this.element = element;
	}
	
	public LinkedElement<NUIComponent> getElement()
	{
		return element;
	}
	
	@Override
	protected void performCancellableAction()
	{
		
	}
}