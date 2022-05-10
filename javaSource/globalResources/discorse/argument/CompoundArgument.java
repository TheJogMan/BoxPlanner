package globalResources.discorse.argument;

import java.util.ArrayList;

import globalResources.commander.AbstractExecutor;
import globalResources.discorse.ArgumentConsumptionResult;

public abstract class CompoundArgument<T> implements Argument<T>
{
	protected ArgumentMultiList argumentList;
	
	public CompoundArgument()
	{
		argumentList = new ArgumentMultiList();
	}
	
	public abstract T getValue(MultiListInterpretationResult result, AbstractExecutor executor);
	
	@Override
	public String getName()
	{
		return argumentList.getArgumentNameString();
	}
	
	@Override
	public void getCompletions(String argumentString, ArrayList<String> completions, AbstractExecutor executor)
	{
		argumentList.completions("", executor, completions, false);
	}
	
	@Override
	public ArgumentConsumptionResult<T> consume(String string, AbstractExecutor executor)
	{
		MultiListInterpretationResult result = argumentList.interpret(string, executor);
		if (result.valid())
		{
			return new ArgumentConsumptionResult<T>(true, getValue(result, executor), string.substring(0, string.length() - result.unconsumed.length()), "Valid", executor, this);
		}
		else
		{
			return new ArgumentConsumptionResult<T>(false, null, "", result.getInvalidReason(), executor, this);
		}
	}
}