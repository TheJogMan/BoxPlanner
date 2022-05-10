package globalResources.utilities.dataSet.valueTypes;

import java.awt.Color;

import globalResources.utilities.ByteConverter;
import globalResources.utilities.dataSet.Value;

public class ColorValue extends Value
{
	Color value;
	
	/**
	 * Creates an empty String value
	 */
	public ColorValue()
	{
		
	}
	
	/**
	 * Creates a String value
	 * @param value the value to hold
	 */
	public ColorValue(Object value)
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
		if (value instanceof String)
		{
			this.value = (Color)value;
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
		return ByteConverter.fromInteger(value.getRGB());
	}
	
	@Override
	protected void deserializeValue(byte[] data)
	{
		value = new Color(ByteConverter.toInteger(data));
	}
	
	@Override
	protected Value getEmptyValue()
	{
		return new ColorValue();
	}
	
	@Override
	protected String getValueID()
	{
		return "Color";
	}
}