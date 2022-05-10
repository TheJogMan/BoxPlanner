package globalResources.discorse.arguments;

import java.util.ArrayList;

import globalResources.commander.AbstractExecutor;
import globalResources.discorse.AIU;
import globalResources.discorse.ArgumentConsumptionResult;
import globalResources.discorse.argument.Argument;

public class DoubleArgument implements Argument<Double>
{
	@Override
	public void init(Object[] data)
	{
		
	}
	
	@Override
	public String getName()
	{
		return "Double";
	}
	
	@Override
	public void getCompletions(String argumentString, ArrayList<String> completions, AbstractExecutor executor)
	{
		
	}
	
	@Override
	public ArgumentConsumptionResult<Double> consume(String string, AbstractExecutor executor)
	{
		String arg = AIU.quickConsume(string).getConsumed();
		try
		{
			double value = Double.parseDouble(arg);
			return new ArgumentConsumptionResult<Double>(true, value, arg, "Valid", executor, this);
		}
		catch (Exception e)
		{
			return new ArgumentConsumptionResult<Double>(false, (double)0, arg, "Not a valid double value, must be between " + Double.MIN_VALUE + " and " + Double.MAX_VALUE, executor, this);
		}
	}
}