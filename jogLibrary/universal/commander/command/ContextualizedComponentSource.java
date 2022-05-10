package jogLibrary.universal.commander.command;

import java.util.ArrayList;
import java.util.Iterator;

import jogLibrary.universal.Result;
import jogLibrary.universal.commander.Executor;
import jogLibrary.universal.commander.ExecutorFilter;
import jogLibrary.universal.commander.command.Category.Context.ComponentCollecter;

public abstract class ContextualizedComponentSource implements ExecutorFilter
{
	ArrayList<Filter> filters = new ArrayList<>();
	
	public final void addFilter(Filter filter)
	{
		filters.add(filter);
	}
	
	public final void removeFilter(Filter filter)
	{
		filters.remove(filter);
	}
	
	public final Result canExecute(Executor executor)
	{
		for (Iterator<Filter> iterator = filters.iterator(); iterator.hasNext();)
		{
			Result result = iterator.next().canExecute(executor);
			if (!result.success())
				return result;
		}
		return new Result();
	}
	
	public abstract void addComponents(Executor executor, Category category, ComponentCollecter collecter);
}