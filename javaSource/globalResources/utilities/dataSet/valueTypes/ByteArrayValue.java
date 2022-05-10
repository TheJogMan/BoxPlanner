package globalResources.utilities.dataSet.valueTypes;

import globalResources.utilities.dataSet.Value;

/**
 * Stores byte array values
 */
public class ByteArrayValue extends Value
{
	byte[] value;
	
	/**
	 * Creates an empty byte array value
	 */
	public ByteArrayValue()
	{
		
	}
	
	/**
	 * Creates a byte array value
	 * @param value the value to hold
	 */
	public ByteArrayValue(Object value)
	{
		super(value);
	}
	
	@Override
	protected String[] getStringLines()
	{
		String[] lines = new String[value.length];
		for (int index = 0; index < value.length; index++)
		{
			lines[index] = "" + value[index];
		}
		return lines;
	}
	
	@Override
	public boolean setValue(Object value)
	{
		if (value instanceof byte[])
		{
			this.value = (byte[])value;
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
		return value;
	}
	
	@Override
	protected void deserializeValue(byte[] data)
	{
		value = data;
	}
	
	@Override
	protected Value getEmptyValue()
	{
		return new ByteArrayValue();
	}
	
	@Override
	protected String getValueID()
	{
		return "ByteArray";
	}
}