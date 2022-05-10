package jogLibrary.universal.commander.argument;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import jogLibrary.universal.Result;
import jogLibrary.universal.ReturnResult;
import jogLibrary.universal.commander.Executor;
import jogLibrary.universal.commander.ExecutorFilter;
import jogLibrary.universal.commander.Interpretable;
import jogLibrary.universal.commander.argument.ArgumentList.ArgumentEntry;
import jogLibrary.universal.indexable.HoardingIndexer;
import jogLibrary.universal.richString.RichString;
import jogLibrary.universal.richString.RichStringBuilder;

public class AdaptiveArgumentList implements ExecutorFilter, Interpretable<AdaptiveArgumentListInterpretationResult>
{
	ArrayList<ArgumentListEntry> lists = new ArrayList<>();
	
	public AdaptiveArgumentList()
	{
		addList();
	}
	
	@Override
	public ReturnResult<AdaptiveArgumentListInterpretationResult> interpret(HoardingIndexer<Character> source, Executor executor)
	{
		Result canExecute = canExecute(executor);
		if (!canExecute.success())
			return new ReturnResult<>(false, new AdaptiveArgumentListInterpretationResult(RichStringBuilder.start().append("Can not execute: ").append(canExecute.description()).build(), -1));
		
		ReturnResult<Object[]> accessDeniedResult = null;
		int accessDeniedIndex = 0;
		for (int index = 0; index < lists.size(); index++)
		{
			HoardingIndexer<Character> sourceCopy = source.clone();
			ReturnResult<Object[]> result = lists.get(index).list.interpret(sourceCopy, executor);
			if (result.success() && sourceCopy.atEnd())
				return new ReturnResult<>(true, new AdaptiveArgumentListInterpretationResult(result.description(), result.success(), index, result.value()));
				
			if (accessDeniedResult == null && result.description().toString().startsWith("Can not execute"))
			{
				accessDeniedResult = result;
				accessDeniedIndex = index;
			}
		}
		if (accessDeniedResult != null)
			return new ReturnResult<>(false, new AdaptiveArgumentListInterpretationResult(RichStringBuilder.start().append("Can not execute: ").append(accessDeniedResult.description()).build(), accessDeniedIndex));
		else
			return new ReturnResult<>(false, new AdaptiveArgumentListInterpretationResult("Provided arguments don't match any list variant.", 0));
	}

	@Override
	public List<String> getCompletions(HoardingIndexer<Character> source, Executor executor)
	{
		if (!canExecute(executor).success())
			return new ArrayList<>();
		
		ArrayList<String> completions = new ArrayList<>();
		for (Iterator<ArgumentListEntry> iterator = lists.iterator(); iterator.hasNext();)
			completions.addAll(iterator.next().list.getCompletions(source.clone(), executor));
		return completions;
	}
	
	public void addList()
	{
		addList(new RichString());
	}
	
	public void addList(RichString description)
	{
		lists.add(new ArgumentListEntry(description));
	}
	
	public void addList(String description)
	{
		addList(new RichString(description));
	}
	
	public int listCount()
	{
		return lists.size();
	}
	
	public ArgumentListEntry getList(int index)
	{
		return lists.get(index);
	}
	
	public void setDefaultListDescription(RichString description)
	{
		lists.get(0).description = description;
	}
	
	public void setDefaultListDescription(String description)
	{
		setDefaultListDescription(new RichString(description));
	}
	
	/***
	 * Defaults to list number 0.
	 * @return
	 */
	public int argumentCount()
	{
		return lists.get(0).list.arguments.size();
	}
	
	/***
	 * Defaults to list number 0.
	 * @return
	 */
	public ArgumentEntry getArgument(int index)
	{
		return lists.get(0).list.arguments.get(index);
	}
	
	/***
	 * Defaults to list number 0.
	 * @return
	 */
	public void addArgument(Class<? extends Argument<?>> argument, String name, Object[] data, RichString description)
	{
		addArgument(0, argument, name, data, description);
	}
	
	/***
	 * Defaults to list number 0.
	 * @return
	 */
	public void addArgument(Class<? extends Argument<?>> argument, String name, Object[] data, String description)
	{
		addArgument(0, argument, name, data, description);
	}
	
	/***
	 * Defaults to list number 0.
	 * @return
	 */
	public void addArgument(Class<? extends Argument<?>> argument, Object[] data, RichString description)
	{
		addArgument(0, argument, data, description);
	}
	
	/***
	 * Defaults to list number 0.
	 * @return
	 */
	public void addArgument(Class<? extends Argument<?>> argument, Object[] data, String description)
	{
		addArgument(0, argument, data, description);
	}
	
	/***
	 * Defaults to list number 0.
	 * @return
	 */
	public void addArgument(Class<? extends Argument<?>> argument, String name, Object[] data)
	{
		addArgument(0, argument, name, data);
	}
	
	/***
	 * Defaults to list number 0.
	 * @return
	 */
	public void addArgument(Class<? extends Argument<?>> argument, Object[] data)
	{
		addArgument(0, argument, data);
	}
	
	/***
	 * Defaults to list number 0.
	 * @return
	 */
	public void addArgument(Class<? extends Argument<?>> argument)
	{
		addArgument(0, argument);
	}
	
	/***
	 * Defaults to list number 0.
	 * @return
	 */
	public void addArgument(Class<? extends Argument<?>> argument, String name)
	{
		addArgument(0, argument, name);
	}
	
	public void addArgument(int listNumber, Class<? extends Argument<?>> argument, String name, Object[] data, RichString description)
	{
		lists.get(listNumber).list.addArgument(argument, name, data, description);
	}
	
	public void addArgument(int listNumber, Class<? extends Argument<?>> argument, String name, Object[] data, String description)
	{
		lists.get(listNumber).list.addArgument(argument, name, data, description);
	}
	
	public void addArgument(int listNumber, Class<? extends Argument<?>> argument, Object[] data, RichString description)
	{
		lists.get(listNumber).list.addArgument(argument, data, description);
	}
	
	public void addArgument(int listNumber, Class<? extends Argument<?>> argument, Object[] data, String description)
	{
		lists.get(listNumber).list.addArgument(argument, data, description);
	}
	
	public void addArgument(int listNumber, Class<? extends Argument<?>> argument, String name, Object[] data)
	{
		lists.get(listNumber).list.addArgument(argument, name, data);
	}
	
	public void addArgument(int listNumber, Class<? extends Argument<?>> argument, Object[] data)
	{
		lists.get(listNumber).list.addArgument(argument, data);
	}
	
	public void addArgument(int listNumber, Class<? extends Argument<?>> argument)
	{
		lists.get(listNumber).list.addArgument(argument);
	}
	
	public void addArgument(int listNumber, Class<? extends Argument<?>> argument, String name)
	{
		lists.get(listNumber).list.addArgument(argument, name);
	}
	
	public static class ArgumentListEntry
	{
		ArgumentList list;
		RichString description;
		
		public ArgumentListEntry(RichString description)
		{
			list = new ArgumentList();
			this.description = description;
		}
		
		public ArgumentList list()
		{
			return list;
		}
		
		public RichString description()
		{
			return description;
		}
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