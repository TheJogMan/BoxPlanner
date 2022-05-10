package globalResources.utilities.dataSet.valueTypes;

import globalResources.utilities.ByteConverter;
import globalResources.utilities.dataSet.Value;

/**
 * Stores double values
 */
public class DoubleValue extends Value
{
	Double value;
	
	/**
	 * Creates an empty double value
	 */
	public DoubleValue()
	{
		
	}
	
	/**
	 * Creates a double value
	 * @param value the value to hold
	 */
	public DoubleValue(Object value)
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
		if (value instanceof Double)
		{
			this.value = (Double)value;
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
		return ByteConverter.fromDouble(value);
	}
	
	@Override
	protected void deserializeValue(byte[] data)
	{
		value = ByteConverter.toDouble(data);
	}
	
	@Override
	protected Value getEmptyValue()
	{
		return new DoubleValue();
	}
	
	@Override
	protected String getValueID()
	{
		return "Double";
	}
}