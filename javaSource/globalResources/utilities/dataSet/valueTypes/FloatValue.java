package globalResources.utilities.dataSet.valueTypes;

import globalResources.utilities.ByteConverter;
import globalResources.utilities.dataSet.Value;

/**
 * Stores float values
 */
public class FloatValue extends Value
{
	Float value;
	
	/**
	 * Creates an empty float value
	 */
	public FloatValue()
	{
		
	}
	
	/**
	 * Creates a float value
	 * @param value the value to hold
	 */
	public FloatValue(Object value)
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
		if (value instanceof Float)
		{
			this.value = (Float)value;
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
		return ByteConverter.fromFloat(value);
	}
	
	@Override
	protected void deserializeValue(byte[] data)
	{
		value = ByteConverter.toFloat(data);
	}
	
	@Override
	protected Value getEmptyValue()
	{
		return new FloatValue();
	}
	
	@Override
	protected String getValueID()
	{
		return "Float";
	}
}