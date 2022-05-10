package globalResources.discorse.arguments;

import java.util.ArrayList;

import globalResources.commander.AbstractExecutor;
import globalResources.discorse.AIU;
import globalResources.discorse.ArgumentConsumptionResult;
import globalResources.discorse.argument.Argument;

public class LongArgument implements Argument<Long>
{
	@Override
	public void init(Object[] data)
	{
		
	}
	
	@Override
	public String getName()
	{
		return "Long";
	}
	
	@Override
	public void getCompletions(String argumentString, ArrayList<String> completions, AbstractExecutor executor)
	{
		
	}
	
	@Override
	public ArgumentConsumptionResult<Long> consume(String string, AbstractExecutor executor)
	{
		String arg = AIU.quickConsume(string).getConsumed();
		try
		{
			long value = Long.parseLong(arg);
			return new ArgumentConsumptionResult<Long>(true, value, arg, "Valid", executor, this);
		}
		catch (Exception e)
		{
			return new ArgumentConsumptionResult<Long>(false, (long)0, arg, "Not a valid long value, must be between " + Long.MIN_VALUE + " and " + Long.MAX_VALUE, executor, this);
		}
	}
}