package globalResources.discorse.argument;

import java.util.ArrayList;

import globalResources.commander.AbstractExecutor;
import globalResources.discorse.ArgumentConsumptionResult;

public interface PickyArgument<ValueType, ExecutorType extends AbstractExecutor> extends Argument<ValueType>
{
	abstract void getPickyCompletions(String argumentString, ArrayList<String> completions, ExecutorType executor);
	abstract ArgumentConsumptionResult<ValueType> pickyConsume(String string, ExecutorType executor);
	
	@SuppressWarnings("unchecked")
	public default void getCompletions(String argumentString, ArrayList<String> completions, AbstractExecutor executor)
	{
		ExecutorType pickyExecutor;
		try
		{
			pickyExecutor = (ExecutorType)executor;
		}
		catch (Exception e)
		{
			pickyExecutor = null;
		}
		if (pickyExecutor != null) getPickyCompletions(argumentString, completions, pickyExecutor);
	}
	
	@SuppressWarnings("unchecked")
	public default ArgumentConsumptionResult<ValueType> consume(String string, AbstractExecutor executor)
	{
		ExecutorType pickyExecutor;
		try
		{
			pickyExecutor = (ExecutorType)executor;
		}
		catch (Exception e)
		{
			pickyExecutor = null;
		}
		if (pickyExecutor != null) return pickyConsume(string, pickyExecutor);
		else return new ArgumentConsumptionResult<ValueType>(false, null, "", "Invalid executor", executor, this);
	}
}