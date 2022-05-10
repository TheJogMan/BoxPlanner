package jogLibrary.universal.commander.argument;

import java.util.ArrayList;
import java.util.Iterator;

import jogLibrary.universal.Result;
import jogLibrary.universal.commander.Executor;
import jogLibrary.universal.commander.ExecutorFilter;
import jogLibrary.universal.commander.Interpretable;
import jogLibrary.universal.richString.RichString;

public abstract class Argument<ValueType> implements ExecutorFilter, Interpretable<ValueType>
{
	public abstract void init(Object[] data);
	public abstract String defaultName();
	public abstract RichString defaultDescription();
	
	public String argStringName()
	{
		return null;
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