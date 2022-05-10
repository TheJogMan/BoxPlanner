package globalResources.utilities;

public interface Child<ParentType>
{
	public ParentType getParent();
	
	public void add(ParentType parent);
	public void remove();
	
	public default void add(ParentType parent, boolean remove)
	{
		if (hasParent() && remove) remove();
		add(parent);
	}
	
	public default boolean hasParent()
	{
		return getParent() != null;
	}
}