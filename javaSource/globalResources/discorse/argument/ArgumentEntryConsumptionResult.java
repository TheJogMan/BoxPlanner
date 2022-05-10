package globalResources.discorse.argument;

import globalResources.commander.AbstractExecutor;
import globalResources.discorse.ArgumentConsumptionResult;
import globalResources.richText.RichString;

public class ArgumentEntryConsumptionResult extends ArgumentConsumptionResult<Object>
{
	private boolean valid;
	private int argumentNumber;
	private RichString[] reasons;
	private ArgumentEntry entry;
	
	@SuppressWarnings("unchecked")
	ArgumentEntryConsumptionResult(ArgumentConsumptionResult<?> result, int argumentNumber, RichString[] reasons, ArgumentEntry entry, boolean valid)
	{
		super(result.wasValid(), result.getValue(), result.getConsumed(), result.reason(), result.getExecutor(), (Argument<Object>)result.getArgument());
		this.argumentNumber = argumentNumber;
		this.reasons = reasons;
		this.entry = entry;
		this.valid = valid;
	}
	
	@SuppressWarnings("unchecked")
	ArgumentEntryConsumptionResult(Argument<?> argument, AbstractExecutor executor, ArgumentEntry entry)
	{
		super(false, null, "", new RichString("Argument did not return a valid result (" + argument.getClass().getName() + ")"), executor, (Argument<Object>)argument);
		argumentNumber = -1;
		reasons = new RichString[0];
		this.entry = entry;
		valid = false;
	}
	
	public final boolean valid()
	{
		return valid;
	}
	
	public final ArgumentEntry getEntry()
	{
		return entry;
	}
	
	public final int argumentNumber()
	{
		return argumentNumber;
	}
	
	public final RichString[] getAllReasons()
	{
		return reasons;
	}
}