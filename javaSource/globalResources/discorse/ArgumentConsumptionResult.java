package globalResources.discorse;

import globalResources.commander.AbstractExecutor;
import globalResources.discorse.argument.Argument;
import globalResources.richText.RichString;

public class ArgumentConsumptionResult <ValueType>
{
	private boolean valid;
	private ValueType value;
	private String consumed;
	private RichString reason;
	private AbstractExecutor executor;
	private Argument<ValueType> argument;
	
	public ArgumentConsumptionResult(boolean valid, ValueType value, String consumed, RichString reason, AbstractExecutor executor, Argument<ValueType> argument)
	{
		this.valid = valid;
		this.value = value;
		this.consumed = consumed;
		this.reason = reason;
		this.executor = executor;
		this.argument = argument;
	}
	
	public ArgumentConsumptionResult(boolean valid, ValueType value, String consumed, String reason, AbstractExecutor executor, Argument<ValueType> argument)
	{
		this(valid, value, consumed, new RichString(reason), executor, argument);
	}
	
	public final boolean wasValid()
	{
		return valid;
	}
	
	public final ValueType getValue()
	{
		return value;
	}
	
	public final int getConsumedLength()
	{
		return consumed.length();
	}
	
	public final String getConsumed()
	{
		return consumed;
	}
	
	public final RichString reason()
	{
		return reason;
	}
	
	public final AbstractExecutor getExecutor()
	{
		return executor;
	}
	
	public final Argument<ValueType> getArgument()
	{
		return argument;
	}
}