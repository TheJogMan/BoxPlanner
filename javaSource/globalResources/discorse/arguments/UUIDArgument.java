package globalResources.discorse.arguments;

import java.util.ArrayList;
import java.util.UUID;

import globalResources.commander.AbstractExecutor;
import globalResources.discorse.AIU;
import globalResources.discorse.ArgumentConsumptionResult;
import globalResources.discorse.argument.Argument;

public class UUIDArgument implements Argument<UUID>
{
	@Override
	public void init(Object[] data)
	{
		
	}
	
	@Override
	public String getName()
	{
		return "UUID";
	}
	
	@Override
	public void getCompletions(String argumentString, ArrayList<String> completions, AbstractExecutor executor)
	{
		
	}
	
	@Override
	public ArgumentConsumptionResult<UUID> consume(String string, AbstractExecutor executor)
	{
		String arg = AIU.quickConsume(string).getConsumed();
		try
		{
			UUID id = UUID.fromString(arg);
			if (id != null) return new ArgumentConsumptionResult<UUID>(true, id, arg, "Valid", executor, this);
		}
		catch (Exception e)
		{
			
		}
		return new ArgumentConsumptionResult<UUID>(false, UUID.randomUUID(), arg, "Not a valid UUID format", executor, this);
	}
}