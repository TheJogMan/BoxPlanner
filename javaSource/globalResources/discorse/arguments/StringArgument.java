package globalResources.discorse.arguments;

import java.util.ArrayList;

import globalResources.commander.AbstractExecutor;
import globalResources.discorse.AIU;
import globalResources.discorse.ArgumentConsumptionResult;
import globalResources.discorse.argument.Argument;

public final class StringArgument implements Argument<String>
{
	@Override
	public void init(Object[] data)
	{
		
	}
	
	@Override
	public String getName()
	{
		return "String";
	}
	
	@Override
	public void getCompletions(String argumentString, ArrayList<String> completions, AbstractExecutor executor)
	{
		
	}
	
	@Override
	public ArgumentConsumptionResult<String> consume(String string, AbstractExecutor executor)
	{
		if (string.length() > 0)
		{
			if (string.charAt(0) == '"')
			{
				String arg = "";
				int consumed = 1;
				boolean ignore = false;
				for (int index = 1; index < string.length(); index++)
				{
					char ch = string.charAt(index);
					if (ignore)
					{
						arg += ch;
						consumed++;
						ignore = false;
					}
					else
					{
						if (ch == '\\')
						{
							ignore = true;
							consumed++;
						}
						else if (ch == '"')
						{
							consumed++;
							return new ArgumentConsumptionResult<String>(true, arg, string.substring(0, consumed), "Valid", executor, this);
						}
						else
						{
							arg += ch;
							consumed++;
						}
					}
				}
				return new ArgumentConsumptionResult<String>(false, arg, string.substring(0, consumed), "Quotation left open", executor, this);
			}
			else
			{
				String arg = AIU.quickConsume(string).getConsumed();
				return new ArgumentConsumptionResult<String>(true, arg, arg, "Valid", executor, this);
			}
		}
		else return new ArgumentConsumptionResult<String>(false, "", "", "Empty", executor, this);
	}
}