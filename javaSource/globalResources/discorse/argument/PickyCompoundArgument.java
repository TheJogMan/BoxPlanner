package globalResources.discorse.argument;

import java.util.ArrayList;

import globalResources.commander.AbstractExecutor;
import globalResources.discorse.ArgumentConsumptionResult;

public abstract class PickyCompoundArgument<ValueType, ExecutorType extends AbstractExecutor> extends CompoundArgument<ValueType>
{
	protected abstract ValueType getPickyValue(MultiListInterpretationResult result, ExecutorType executor);
	
	@SuppressWarnings("unchecked")
	@Override
	public final ValueType getValue(MultiListInterpretationResult result, AbstractExecutor executor)
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
		if (pickyExecutor != null) return getPickyValue(result, pickyExecutor);
		else return null;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public final void getCompletions(String argumentString, ArrayList<String> completions, AbstractExecutor executor)
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
		if (executor != null) argumentList.completions("", pickyExecutor, completions, false);
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public final ArgumentConsumptionResult<ValueType> consume(String string, AbstractExecutor executor)
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
		if (pickyExecutor != null)
		{
			MultiListInterpretationResult result = argumentList.interpret(string, executor);
			if (result.valid())
			{
				return new ArgumentConsumptionResult<ValueType>(true, getValue(result, executor), string.substring(0, string.length() - result.unconsumed.length()), "Valid", executor, this);
			}
			else
			{
				return new ArgumentConsumptionResult<ValueType>(false, null, "", result.getInvalidReason(), executor, this);
			}
		}
		else return new ArgumentConsumptionResult<ValueType>(false, null, "", "Invalid executor", executor, this);
	}
}