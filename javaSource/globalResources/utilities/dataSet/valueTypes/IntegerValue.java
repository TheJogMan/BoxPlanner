package globalResources.utilities.dataSet.valueTypes;

import globalResources.utilities.ByteConverter;
import globalResources.utilities.dataSet.Value;

/**
 * Stores int values
 */
public class IntegerValue extends Value
{
	Integer value;
	
	/**
	 * Creates an empty int value
	 */
	public IntegerValue()
	{
		
	}
	
	/**
	 * Creates an int value
	 * @param value the value to hold
	 */
	public IntegerValue(Object value)
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
		if (value instanceof Integer)
		{
			this.value = (Integer)value;
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
		return ByteConverter.fromInteger(value);
	}
	
	@Override
	protected void deserializeValue(byte[] data)
	{
		value = ByteConverter.toInteger(data);
	}
	
	@Override
	protected Value getEmptyValue()
	{
		return new IntegerValue();
	}
	
	@Override
	protected String getValueID()
	{
		return "Integer";
	}
}