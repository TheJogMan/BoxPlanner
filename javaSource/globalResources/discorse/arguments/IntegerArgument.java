package globalResources.discorse.arguments;

import java.util.ArrayList;

import globalResources.commander.AbstractExecutor;
import globalResources.discorse.AIU;
import globalResources.discorse.ArgumentConsumptionResult;
import globalResources.discorse.argument.Argument;

public class IntegerArgument implements Argument<Integer>
{
	@Override
	public void init(Object[] data)
	{
		
	}
	
	@Override
	public String getName()
	{
		return "Integer";
	}
	
	@Override
	public void getCompletions(String argumentString, ArrayList<String> completions, AbstractExecutor executor)
	{
		
	}
	
	@Override
	public ArgumentConsumptionResult<Integer> consume(String string, AbstractExecutor executor)
	{
		String arg = AIU.quickConsume(string).getConsumed();
		try
		{
			int value = Integer.parseInt(arg);
			return new ArgumentConsumptionResult<Integer>(true, value, arg, "Valid", executor, this);
		}
		catch (Exception e)
		{
			return new ArgumentConsumptionResult<Integer>(false, 0, arg, "Not a valid integer value, must be between " + Integer.MIN_VALUE + " and " + Integer.MAX_VALUE, executor, this);
		}
	}
}