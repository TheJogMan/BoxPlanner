package jogLibrary.universal.commander.command;

import java.util.ArrayList;
import java.util.Iterator;

import jogLibrary.universal.Result;
import jogLibrary.universal.commander.Executor;
import jogLibrary.universal.commander.ExecutorFilter;
import jogLibrary.universal.commander.Interpretable;
import jogLibrary.universal.richString.RichString;

public abstract class CommandComponent implements ExecutorFilter, Interpretable<Boolean>
{
	private final String name;
	private final RichString description;
	Category parent;
	
	CommandComponent(Category parent, String name, RichString description, boolean addToParent)
	{
		this.parent = parent;
		this.name = name;
		this.description = description;
		
		if (parent != null && addToParent)
		{
			Result result = parent.addComponent(this);
			if (!result.success())
			{
				String fullName = getFullName();
				parent = null;
				throw new RuntimeException("Could not add component (" + fullName + "): " + result.description());
			}
		}
	}
	
	public abstract Console getConsole();
	public abstract String getFullName(boolean includePrefixCharacter);
	
	public String getFullName()
	{
		return getFullName(false);
	}
	
	public Category getParent()
	{
		return parent;
	}
	
	public String name()
	{
		return name;
	}
	
	public RichString description()
	{
		return description.clone();
	}
	
	ArrayList<Filter> filters = new ArrayList<>();
	
	public void addFilter(Filter filter)
	{
		filters.add(filter);
	}
	
	public void removeFilter(Filter filter)
	{
		filters.remove(filter);
	}
	
	public Result canExecute(Executor executor)
	{
		for (Iterator<Filter> iterator = filters.iterator(); iterator.hasNext();)
		{
			Result result = iterator.next().canExecute(executor);
			if (!result.success())
				return result;
		}
		return new Result();
	}
}