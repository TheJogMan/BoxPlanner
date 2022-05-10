package globalResources.utilities.dataSet.valueTypes;

import globalResources.utilities.ByteConverter;
import globalResources.utilities.dataSet.Value;

/**
 * Stores short values
 */
public class ShortValue extends Value
{
	Short value;
	
	/**
	 * Creates an empty short value
	 */
	public ShortValue()
	{
		
	}
	
	/**
	 * Creates a short value
	 * @param value the value to hold
	 */
	public ShortValue(Object value)
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
		if (value instanceof Short)
		{
			this.value = (Short)value;
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
		value = (short)ByteConverter.toInteger(data);
	}
	
	@Override
	protected Value getEmptyValue()
	{
		return new ShortValue();
	}
	
	@Override
	protected String getValueID()
	{
		return "Short";
	}
}