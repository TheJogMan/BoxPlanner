package globalResources.discorse.arguments;

import java.util.ArrayList;

import globalResources.commander.AbstractExecutor;
import globalResources.discorse.AIU;
import globalResources.discorse.ArgumentConsumptionResult;
import globalResources.discorse.argument.Argument;

public class BooleanArgument implements Argument<Boolean>
{
	@Override
	public void init(Object[] data)
	{
		
	}
	
	@Override
	public String getName()
	{
		return "Boolean";
	}
	
	@Override
	public void getCompletions(String argumentString, ArrayList<String> completions, AbstractExecutor executor)
	{
		completions.add("True");
		completions.add("False");
	}
	
	@Override
	public ArgumentConsumptionResult<Boolean> consume(String string, AbstractExecutor executor)
	{
		String arg = AIU.quickConsume(string, ' ').getConsumed().toLowerCase();
		if (arg.compareTo("true") == 0) return new ArgumentConsumptionResult<Boolean>(true, true, arg, "valid", executor, this);
		else if (arg.compareTo("false") == 0) return new ArgumentConsumptionResult<Boolean>(true, false, arg, "valid", executor, this);
		else return new ArgumentConsumptionResult<Boolean>(false, false, arg, "must be either True or False", executor, this);
	}
}