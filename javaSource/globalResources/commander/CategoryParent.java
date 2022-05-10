package globalResources.commander;

import java.util.HashMap;

import globalResources.utilities.SecureList;

public abstract class CategoryParent extends CommandParent implements CommandComponent
{
	HashMap<String, AbstractCategory> categories = new HashMap<String, AbstractCategory>();
	
	abstract boolean attemptToAddCategory(AbstractCategory category);
	
	public final AbstractCategory getCategory(String name)
	{
		return categories.get(name);
	}
	
	public final boolean hasCategory(String name)
	{
		return categories.containsKey(name.toLowerCase());
	}
	
	public final SecureList<AbstractCategory> getCategories()
	{
		return new SecureList<AbstractCategory>(categories.values());
	}
}