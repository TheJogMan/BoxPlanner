package globalResources.discorse.arguments;

import java.util.ArrayList;

import globalResources.commander.AbstractExecutor;
import globalResources.discorse.AIU;
import globalResources.discorse.ArgumentConsumptionResult;
import globalResources.discorse.argument.Argument;

public class FloatArgument implements Argument<Float>
{
	@Override
	public void init(Object[] data)
	{
		
	}
	
	@Override
	public String getName()
	{
		return "Float";
	}
	
	@Override
	public void getCompletions(String argumentString, ArrayList<String> completions, AbstractExecutor executor)
	{
		
	}
	
	@Override
	public ArgumentConsumptionResult<Float> consume(String string, AbstractExecutor executor)
	{
		String arg = AIU.quickConsume(string).getConsumed();
		try
		{
			float value = Float.parseFloat(arg);
			return new ArgumentConsumptionResult<Float>(true, value, arg, "Valid", executor, this);
		}
		catch (Exception e)
		{
			return new ArgumentConsumptionResult<Float>(false, (float)0, arg, "Not a valid float value, must be between " + Float.MIN_VALUE + " and " + Float.MAX_VALUE, executor, this);
		}
	}
}