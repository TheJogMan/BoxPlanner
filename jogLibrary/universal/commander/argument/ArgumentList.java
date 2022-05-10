package jogLibrary.universal.commander.argument;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import jogLibrary.universal.Result;
import jogLibrary.universal.ReturnResult;
import jogLibrary.universal.commander.Executor;
import jogLibrary.universal.commander.ExecutorFilter;
import jogLibrary.universal.commander.Interpretable;
import jogLibrary.universal.indexable.HoardingIndexer;
import jogLibrary.universal.richString.RichString;
import jogLibrary.universal.richString.RichStringBuilder;

public class ArgumentList implements ExecutorFilter, Interpretable<Object[]>
{
	ArrayList<ArgumentEntry> arguments = new ArrayList<>();
	
	@Override
	public ReturnResult<Object[]> interpret(HoardingIndexer<Character> source, Executor executor)
	{
		Result canExecute = canExecute(executor);
		if (!canExecute.success())
			return new ReturnResult<>(RichStringBuilder.start().append("Can not execute:").append(canExecute.description()).build());
		
		Object[] values = new Object[arguments.size()];
		int index = 0;
		for (Iterator<ArgumentEntry> iterator = arguments.iterator(); iterator.hasNext();)
		{
			ArgumentEntry argument = iterator.next();
			canExecute = argument.argument.canExecute(executor);
			if (!canExecute.success())
				return new ReturnResult<>(RichStringBuilder.start().append("Can not execute with argument #" + index + ": ").append(canExecute.description()).build());
			
			ReturnResult<?> result = argument.interpret(source, executor);
			source.next();
			if (result.success())
			{
				values[index] = result.value();
				index++;
			}
			else
				return new ReturnResult<>(RichStringBuilder.start().append("Could not interpret argument #" + index + ": ").append(result.description()).build());
		}
		return new ReturnResult<>(values);
	}
	
	@Override
	public List<String> getCompletions(HoardingIndexer<Character> source, Executor executor)
	{
		if (!canExecute(executor).success())
			return new ArrayList<>();
		
		HoardingIndexer<Character> interpretationSource;
		for (Iterator<ArgumentEntry> iterator = arguments.iterator(); iterator.hasNext();)
		{
			ArgumentEntry argument = iterator.next();
			interpretationSource = source.clone();
			ReturnResult<?> result = argument.interpret(interpretationSource, executor);
			if (result.success())
			{
				source = interpretationSource;
				source.next();
			}
			else
			{
				if (argument.argument.canExecute(executor).success())
					return argument.getCompletions(source, executor);
			}
		}
		return new ArrayList<>();
	}
	
	public void addArgument(Class<? extends Argument<?>> argument, String name, Object[] data, RichString description)
	{
		arguments.add(new ArgumentEntry(argument, name, data, description));
	}
	
	public void addArgument(Class<? extends Argument<?>> argument, String name, Object[] data, String description)
	{
		arguments.add(new ArgumentEntry(argument, name, data, new RichString(description)));
	}
	
	public void addArgument(Class<? extends Argument<?>> argument, Object[] data, RichString description)
	{
		arguments.add(new ArgumentEntry(argument, null, data, description));
	}
	
	public void addArgument(Class<? extends Argument<?>> argument, Object[] data, String description)
	{
		arguments.add(new ArgumentEntry(argument, null, data, new RichString(description)));
	}
	
	public void addArgument(Class<? extends Argument<?>> argument, String name, Object[] data)
	{
		arguments.add(new ArgumentEntry(argument, name, data, null));
	}
	
	public void addArgument(Class<? extends Argument<?>> argument, Object[] data)
	{
		arguments.add(new ArgumentEntry(argument, null, data, null));
	}
	
	public void addArgument(Class<? extends Argument<?>> argument)
	{
		arguments.add(new ArgumentEntry(argument, null, new Object[0], null));
	}
	
	public void addArgument(Class<? extends Argument<?>> argument, String name)
	{
		arguments.add(new ArgumentEntry(argument, name, new Object[0], null));
	}
	
	@Override
	public String toString()
	{
		StringBuilder builder = new StringBuilder();
		for (Iterator<ArgumentEntry> iterator = arguments.iterator(); iterator.hasNext();)
		{
			ArgumentEntry entry = iterator.next();
			String name = entry.argument().argStringName();
			if (name == null)
				name = entry.name();
			builder.append("<").append(name).append(">");
			if (iterator.hasNext())
				builder.append(" ");
		}
		return builder.toString();
	}
	
	public static class ArgumentEntry
	{
		Argument<?> argument;
		String name = "Argument";
		RichString description = new RichString("No Description.");
		
		ArgumentEntry(Class<? extends Argument<?>> argumentClass, String name, Object[] data, RichString description)
		{
			try
			{
				argument = argumentClass.newInstance();
			}
			catch (Exception e)
			{
				RuntimeException ex = new RuntimeException("Could not create instance of Argument (" + argumentClass.getName() + ") " + e.toString());
				ex.setStackTrace(e.getStackTrace());
				throw ex;
			}
			try
			{
				argument.init(data);
			}
			catch (Exception e)
			{
				String dataString = "{\n";
				for (int index = 0; index < data.length; index++)
					dataString += data[index].toString() + "\n";
				dataString += "}";
				RuntimeException ex = new RuntimeException("Could not initialize argument (" + argumentClass.getName() + ") " + e.getMessage() + "\nProvided data:" + dataString);
				ex.setStackTrace(e.getStackTrace());
				throw ex;
			}
			try
			{
				String defaultName = argument.defaultName();
				if (defaultName != null)
					this.name = defaultName;
			}
			catch (Exception e)
			{
				RuntimeException ex = new RuntimeException("Could not get default name for argument (" + argumentClass.getName() + ") " + e.getMessage());
				ex.setStackTrace(e.getStackTrace());
				throw ex;
			}
			try
			{
				RichString defaultDescription = argument.defaultDescription();
				if (defaultDescription != null)
					this.description = description;
			}
			catch (Exception e)
			{
				RuntimeException ex = new RuntimeException("Could not get default description for argument (" + argumentClass.getName() + ") " + e.getMessage());
				ex.setStackTrace(e.getStackTrace());
				throw ex;
			}
			if (name != null)
				this.name = name;
			if (description != null)
				this.description = description;
		}
		
		public List<String> getCompletions(HoardingIndexer<Character> source, Executor executor)
		{
			List<String> completions;
			try
			{
				completions = argument.getCompletions(source, executor);
			}
			catch (Exception e)
			{
				RuntimeException ex = new RuntimeException("Could not getCompletions for argument (" + argument.getClass().getName() + ") " + e.getMessage());
				ex.setStackTrace(e.getStackTrace());
				throw ex;
			}
			if (completions == null)
				completions = new ArrayList<String>();
			return completions;
		}
		
		public ReturnResult<?> interpret(HoardingIndexer<Character> source, Executor executor)
		{
			try
			{
				return argument.interpret(source, executor);
			}
			catch (Exception e)
			{
				RuntimeException ex = new RuntimeException("Could not interpret argument (" + argument.getClass().getName() + ") " + e.getMessage());
				ex.setStackTrace(e.getStackTrace());
				throw ex;
			}
		}
		
		public String name()
		{
			return name;
		}
		
		public RichString description()
		{
			return description;
		}
		
		public Argument<?> argument()
		{
			return argument;
		}
	}
	
	public int argumentCount()
	{
		return arguments.size();
	}
	
	public ArgumentEntry getArgument(int index)
	{
		return arguments.get(index);
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