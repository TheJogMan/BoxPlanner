package globalResources.utilities.dataSet.valueTypes;

import globalResources.utilities.Vector;
import globalResources.utilities.dataSet.DataSet;
import globalResources.utilities.dataSet.Value;

/**
 * Stores Vector values
 */
public class VectorValue extends Value
{
	Vector value;
	
	/**
	 * Creates an empty Vector value
	 */
	public VectorValue()
	{
		
	}
	
	/**
	 * Creates a Vector value
	 * @param value the value to hold
	 */
	public VectorValue(Object value)
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
		if (value instanceof Vector)
		{
			this.value = (Vector)value;
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
		value = new Vector(dataSet.getDouble("x", 0), dataSet.getDouble("y", 0));
	}
	
	@Override
	protected Value getEmptyValue()
	{
		return new VectorValue();
	}
	
	@Override
	protected String getValueID()
	{
		return "Vector";
	}
}