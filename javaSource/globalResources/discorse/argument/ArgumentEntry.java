package globalResources.discorse.argument;

import java.util.ArrayList;
import java.util.Iterator;

import globalResources.commander.AbstractExecutor;
import globalResources.discorse.ArgumentConsumptionResult;
import globalResources.discorse.ArgumentContainer;
import globalResources.richText.RichString;

public class ArgumentEntry implements ArgumentContainer
{
	protected ArrayList<Argument<?>> arguments;
	private boolean locked = false;
	
	protected ArgumentEntry()
	{
		arguments = new ArrayList<Argument<?>>();
	}
	
	public void addArgument(Class<? extends Argument<?>> argument, Object[] data)
	{
		if (!locked)
		{
			try
			{
				Argument<?> argumentObject = argument.newInstance();
				argumentObject.init(data);
				arguments.add(argumentObject);
			}
			catch(Exception e)
			{
				e.printStackTrace();
			}
		}
	}
	
	public void addArgument(Class<? extends Argument<?>> argument)
	{
		addArgument(argument, new Object[0]);
	}
	
	public String getName()
	{
		return (arguments.size() == 0) ? "Invalid argument!" : arguments.get(0).getName();
	}
	
	public int getArgumentCount()
	{
		return arguments.size();
	}
	
	public final boolean locked()
	{
		return locked;
	}
	
	public final void lock()
	{
		locked = true;
	}
	
	public Argument<?> getArgument(int index)
	{
		return (index >= 0 && index < arguments.size()) ? arguments.get(index) : null;
	}
	
	public int indexOfArgument(Argument<?> argument)
	{
		for (int index = 0; index < arguments.size(); index++)
		{
			if (arguments.get(index).equals(argument)) return index;
		}
		return -1;
	}
	
	public ArrayList<String> getCompletions(String argumentString, AbstractExecutor executor)
	{
		return getCompletions(argumentString, new ArrayList<String>(), executor);
	}
	
	public ArrayList<String> getCompletions(String argumentString, ArrayList<String> completions, AbstractExecutor executor)
	{
		for (int index = 0; index < arguments.size(); index++) arguments.get(index).getCompletions(argumentString, completions, executor);
		return completions;
	}
	
	protected ArgumentEntryConsumptionResult consume(String string, AbstractExecutor executor)
	{
		if (arguments.size() == 1)
		{
			ArgumentConsumptionResult<?> result = arguments.get(0).consume(string, executor);
			if (result == null) return new ArgumentEntryConsumptionResult(arguments.get(0), executor, this);
			return new ArgumentEntryConsumptionResult(result, 0, new RichString[] {result.reason()}, this, result.wasValid());
		}
		else
		{
			RichString[] reasons = new RichString[arguments.size()];
			for (int index = 0; index < arguments.size(); index++)
			{
				ArgumentConsumptionResult<?> result = arguments.get(index).consume(string, executor);
				if (result == null) reasons[index] = (new ArgumentEntryConsumptionResult(arguments.get(0), executor, this)).reason();
				else
				{
					reasons[index] = result.reason();
					if (result.wasValid())
					{
						for (int subIndex = index + 1; subIndex < arguments.size(); subIndex++) reasons[subIndex] = arguments.get(subIndex).consume(string, executor).reason();
						return new ArgumentEntryConsumptionResult(result, index, reasons, this, true);
					}
				}
			}
			return new ArgumentEntryConsumptionResult(new ArgumentConsumptionResult<Object>(false, null, "", "No valid match", executor, null), -1, reasons, this, false);
		}
	}
	
	public Iterator<Argument<?>> argumentIterator()
	{
		return arguments.iterator();
	}
	
	@Override
	public void removeArgument(Argument<?> argument)
	{
		int index = indexOfArgument(argument);
		if (!locked) arguments.remove(index);
	}
	
	@Override
	public String getArgumentName(Argument<?> argument)
	{
		int index = indexOfArgument(argument);
		return (index >= 0 && index < arguments.size()) ? arguments.get(index).getName() : "Invalid Argument!";
	}
	
	@Override
	public boolean hasArgument(Argument<?> argument)
	{
		return arguments.contains(argument);
	}
}