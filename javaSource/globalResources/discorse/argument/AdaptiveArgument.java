package globalResources.discorse.argument;

import java.util.ArrayList;
import java.util.Iterator;

import globalResources.commander.AbstractExecutor;
import globalResources.discorse.ArgumentConsumptionResult;
import globalResources.richText.RichString;

public abstract class AdaptiveArgument<ValueType> implements Argument<ValueType>
{
	private ArgumentEntry entry = new ArgumentEntry();
	
	public final void getCompletions(String argumentString, ArrayList<String> completions, AbstractExecutor executor)
	{
		entry.getCompletions(argumentString, completions, executor);
	}
	
	public final AdaptiveArgumentConsumptionResult consume(String string, AbstractExecutor executor)
	{
		return new AdaptiveArgumentConsumptionResult(entry.consume(string, executor), this);
	}
	
	protected abstract ValueType convertValue(ArgumentEntryConsumptionResult result);
	
	public class AdaptiveArgumentConsumptionResult extends ArgumentConsumptionResult<ValueType>
	{
		private Argument<ValueType> argumentUsed;
		private RichString[] allReasons;
		
		private AdaptiveArgumentConsumptionResult(ArgumentEntryConsumptionResult result, AdaptiveArgument<ValueType> argument)
		{
			super(result.valid(), convertValue(result), result.getConsumed(), result.reason(), result.getExecutor(), argument);
			allReasons = result.getAllReasons();
		}
		
		public final Argument<?> getArgumentUsed()
		{
			return argumentUsed;
		}
		
		public final RichString[] allReasons()
		{
			return allReasons;
		}
	}
	
	protected final void addArgument(Class<? extends Argument<?>> argument, Object[] data)
	{
		entry.addArgument(argument, data);
	}
	
	protected final void addArgument(Class<? extends Argument<?>> argument)
	{
		addArgument(argument, new Object[0]);
	}
	
	protected final int getArgumentCount()
	{
		return entry.getArgumentCount();
	}
	
	protected final boolean locked()
	{
		return entry.locked();
	}
	
	protected final void lock()
	{
		entry.lock();
	}
	
	protected final Argument<?> getArgument(int index)
	{
		return entry.getArgument(index);
	}
	
	protected final int indexOfArgument(Argument<?> argument)
	{
		return entry.indexOfArgument(argument);
	}
	
	protected final Iterator<Argument<?>> argumentIterator()
	{
		return entry.argumentIterator();
	}
	
	protected final void removeArgument(Argument<?> argument)
	{
		entry.removeArgument(argument);
	}
	
	protected final String getArgumentName(Argument<?> argument)
	{
		return entry.getArgumentName(argument);
	}
	
	protected final boolean hasArgument(Argument<?> argument)
	{
		return entry.hasArgument(argument);
	}
}