package globalResources.utilities.dataSet.valueTypes;

import globalResources.utilities.dataSet.Value;

/**
 * Stores byte values
 */
public class ByteValue extends Value
{
	Byte value;
	
	/**
	 * Creates an empty byte value
	 */
	public ByteValue()
	{
		
	}
	
	/**
	 * Creates a byte value
	 * @param value the value to hold
	 */
	public ByteValue(Object value)
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
		if (value instanceof Byte)
		{
			this.value = (byte)value;
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
		return new byte[] {value};
	}
	
	@Override
	protected void deserializeValue(byte[] data)
	{
		value = data[0];
	}
	
	@Override
	protected Value getEmptyValue()
	{
		return new ByteValue();
	}
	
	@Override
	protected String getValueID()
	{
		return "Byte";
	}
}