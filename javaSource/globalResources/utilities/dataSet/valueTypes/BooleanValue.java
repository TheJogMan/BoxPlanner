package globalResources.utilities.dataSet.valueTypes;

import globalResources.utilities.ByteConverter;
import globalResources.utilities.dataSet.Value;

/**
 * Stores boolean values
 */
public class BooleanValue extends Value
{
	Boolean value;
	
	/**
	 * Creates an empty boolean value
	 */
	public BooleanValue()
	{
		
	}
	
	/**
	 * Creates a boolean value
	 * @param value the value to hold
	 */
	public BooleanValue(Object value)
	{
		super(value);
	}
	
	@Override
	protected String[] getStringLines()
	{
		return new String[] {value ? "TRUE" : "FALSE"};
	}
	
	@Override
	public boolean setValue(Object value)
	{
		if (value instanceof Boolean)
		{
			this.value = (Boolean)value;
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
		return new byte[] {ByteConverter.fromBoolean(value)};
	}
	
	@Override
	protected void deserializeValue(byte[] data)
	{
		value = ByteConverter.toBoolean(data[0]);
	}
	
	@Override
	protected Value getEmptyValue()
	{
		return new BooleanValue();
	}
	
	@Override
	protected String getValueID()
	{
		return "Boolean";
	}
}