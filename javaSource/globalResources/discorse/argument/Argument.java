package globalResources.discorse.argument;

import java.util.ArrayList;

import globalResources.commander.AbstractExecutor;
import globalResources.discorse.ArgumentConsumptionResult;

public interface Argument <ValueType>
{
	public void init(Object[] data);
	public String getName();
	public void getCompletions(String argumentString, ArrayList<String> completions, AbstractExecutor executor);
	public ArgumentConsumptionResult<ValueType> consume(String string, AbstractExecutor executor);
	
	public default boolean isValid(String string, AbstractExecutor executor)
	{
		return consume(string, executor).wasValid();
	}
	
	public default ValueType getValue(String string, AbstractExecutor executor)
	{
		return consume(string, executor).getValue();
	}
}