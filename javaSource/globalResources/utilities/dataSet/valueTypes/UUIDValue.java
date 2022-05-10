package globalResources.utilities.dataSet.valueTypes;

import java.util.UUID;

import globalResources.utilities.ByteConverter;
import globalResources.utilities.dataSet.Value;

/**
 * Stores UUID values
 */
public class UUIDValue extends Value
{
	UUID value;
	
	/**
	 * Creates an empty UUID value
	 */
	public UUIDValue()
	{
		
	}
	
	/**
	 * Creates a UUID value
	 * @param value the value to hold
	 */
	public UUIDValue(Object value)
	{
		super(value);
	}
	
	@Override
	protected String[] getStringLines()
	{
		return new String[] {value.toString()};
	}
	
	@Override
	public boolean setValue(Object value)
	{
		if (value == null)
		{
			this.value = (UUID)value;
			return true;
		}
		else if (value instanceof UUID)
		{
			this.value = (UUID)value;
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
		return ByteConverter.fromUUID(value);
	}
	
	@Override
	protected void deserializeValue(byte[] data)
	{
		value = ByteConverter.toUUID(data);
	}
	
	@Override
	protected Value getEmptyValue()
	{
		return new UUIDValue();
	}
	
	@Override
	protected String getValueID()
	{
		return "UUID";
	}
}