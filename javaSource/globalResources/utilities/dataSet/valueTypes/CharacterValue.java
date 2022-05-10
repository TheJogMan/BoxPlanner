package globalResources.utilities.dataSet.valueTypes;

import globalResources.utilities.ByteConverter;
import globalResources.utilities.dataSet.Value;

/**
 * Stores char values
 */
public class CharacterValue extends Value
{
	Character value;
	
	/**
	 * Creates an empty char value
	 */
	public CharacterValue()
	{
		
	}
	
	/**
	 * Creates a char value
	 * @param value the value to hold
	 */
	public CharacterValue(Object value)
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
		if (value instanceof Character)
		{
			this.value = (Character)value;
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
		value = (char)ByteConverter.toInteger(data);
	}
	
	@Override
	protected Value getEmptyValue()
	{
		return new CharacterValue();
	}
	
	@Override
	protected String getValueID()
	{
		return "Character";
	}
}