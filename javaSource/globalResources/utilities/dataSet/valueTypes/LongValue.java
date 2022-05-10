package globalResources.utilities.dataSet.valueTypes;

import globalResources.utilities.ByteConverter;
import globalResources.utilities.dataSet.Value;

/**
 * Stores long values
 */
public class LongValue extends Value
{
	Long value;
	
	/**
	 * Creates an empty long value
	 */
	public LongValue()
	{
		
	}
	
	/**
	 * Creates a long value
	 * @param value the value to hold
	 */
	public LongValue(Object value)
	{
		super(value);
	}
	
	@Override
	protected String[] getStringLines()
	{
		return new String[] {"" + value};
	}
	
	@Override
	public boolean setValue(Object value)
	{
		if (value instanceof Long)
		{
			this.value = (Long)value;
			return true;
		}
		return false;
	}
	
	@Override
	public Object getValue()
	{
		return value;
	}
	
	@Override
	protected byte[] serializeValue()
	{
		return ByteConverter.fromLong(value);
	}
	
	@Override
	protected void deserializeValue(byte[] data)
	{
		value = ByteConverter.toLong(data);
	}
	
	@Override
	protected Value getEmptyValue()
	{
		return new LongValue();
	}
	
	@Override
	protected String getValueID()
	{
		return "Long";
	}
}