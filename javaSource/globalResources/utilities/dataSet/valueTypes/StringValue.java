package globalResources.utilities.dataSet.valueTypes;

import globalResources.utilities.ByteConverter;
import globalResources.utilities.dataSet.Value;

/**
 * Stores String values
 */
public class StringValue extends Value
{
	String value;
	
	/**
	 * Creates an empty String value
	 */
	public StringValue()
	{
		
	}
	
	/**
	 * Creates a String value
	 * @param value the value to hold
	 */
	public StringValue(Object value)
	{
		super(value);
	}
	
	@Override
	protected String[] getStringLines()
	{
		return new String[] {value};
	}
	
	@Override
	public boolean setValue(Object value)
	{
		if (value instanceof String)
		{
			this.value = (String)value;
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
		return ByteConverter.fromString(value);
	}
	
	@Override
	protected void deserializeValue(byte[] data)
	{
		value = ByteConverter.toString(data);
	}
	
	@Override
	protected Value getEmptyValue()
	{
		return new StringValue();
	}
	
	@Override
	protected String getValueID()
	{
		return "String";
	}
}