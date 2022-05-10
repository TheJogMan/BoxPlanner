package globalResources.discorse.arguments;

import java.util.ArrayList;

import globalResources.discorse.ArgumentConsumptionResult;
import globalResources.discorse.argument.Argument;
import globalResources.commander.AbstractExecutor;
import globalResources.discorse.AIU;

public class ByteArgument implements Argument<Byte>
{
	@Override
	public void init(Object[] data)
	{
		
	}
	
	@Override
	public String getName()
	{
		return "Byte";
	}
	
	@Override
	public void getCompletions(String argumentString, ArrayList<String> completions, AbstractExecutor executor)
	{
		
	}
	
	@Override
	public ArgumentConsumptionResult<Byte> consume(String string, AbstractExecutor executor)
	{
		String arg = AIU.quickConsume(string).getConsumed();
		try
		{
			byte value = Byte.parseByte(arg);
			return new ArgumentConsumptionResult<Byte>(true, value, arg, "Valid", executor, this);
		}
		catch (Exception e)
		{
			return new ArgumentConsumptionResult<Byte>(false, (byte)0, arg, "Not a valid byte value, must be between " + Byte.MIN_VALUE + " and " + Byte.MAX_VALUE, executor, this);
		}
	}
}