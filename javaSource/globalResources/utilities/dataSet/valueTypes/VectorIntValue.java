package globalResources.utilities.dataSet.valueTypes;

import globalResources.utilities.VectorInt;
import globalResources.utilities.dataSet.DataSet;
import globalResources.utilities.dataSet.Value;

/**
 * Stores Vector values
 */
public class VectorIntValue extends Value
{
	VectorInt value;
	
	/**
	 * Creates an empty Vector value
	 */
	public VectorIntValue()
	{
		
	}
	
	/**
	 * Creates a Vector value
	 * @param value the value to hold
	 */
	public VectorIntValue(Object value)
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
		if (value instanceof VectorInt)
		{
			this.value = (VectorInt)value;
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
		DataSet dataSet = new DataSet();
		dataSet.set("x", value.getX());
		dataSet.set("y", value.getY());
		return dataSet.getAsBytes();
	}
	
	@Override
	protected void deserializeValue(byte[] data)
	{
		DataSet dataSet = new DataSet(data);
		value = new VectorInt(dataSet.getInteger("x", 0), dataSet.getInteger("y", 0));
	}
	
	@Override
	protected Value getEmptyValue()
	{
		return new VectorIntValue();
	}
	
	@Override
	protected String getValueID()
	{
		return "VectorInt";
	}
}