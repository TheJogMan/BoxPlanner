package globalResources.discorse.argument;

import globalResources.commander.ValidationResult;
import globalResources.richText.RichString;

public class InterpretationResult
{
	private boolean valid;
	boolean endReached;
	ArgumentList list;
	ArgumentEntryConsumptionResult[] results;
	String unconsumed;
	ValidationResult validation;
	
	Object[] values;
	
	InterpretationResult(ValidationResult validation, boolean valid, boolean endReached, ArgumentEntryConsumptionResult[] results, ArgumentList list, String unconsumed)
	{
		this.validation = validation;
		this.valid = valid;
		this.results = results;
		this.endReached = endReached;
		this.unconsumed = unconsumed;
		
		values = new Object[results.length];
		for (int index = 0; index < results.length; index++) values[index] = results[index].getValue();
	}
	
	public ValidationResult getExecutorValidation()
	{
		return validation;
	}
	
	public String unconsumed()
	{
		return unconsumed;
	}
	
	public Object[] getValues()
	{
		return values;
	}
	
	public boolean valid()
	{
		return valid;
	}
	
	public boolean endReached()
	{
		return endReached;
	}
	
	public RichString getInvalidReason()
	{
		if (valid) return new RichString("Valid");
		int index = getInvalidArgumentIndex();
		if (index != -1) return results[index].reason();
		return new RichString("unknown");
	}
	
	public int getInvalidArgumentIndex()
	{
		for (int index = 0; index < results.length; index++)
		{
			if (!results[index].valid()) return index;
		}
		return -1;
	}
	
	public ArgumentList getList()
	{
		return list;
	}
}