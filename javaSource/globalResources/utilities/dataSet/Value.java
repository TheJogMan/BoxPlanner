package globalResources.utilities.dataSet;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import globalResources.utilities.ByteConverter;
import globalResources.utilities.dataSet.valueTypes.BooleanValue;
import globalResources.utilities.dataSet.valueTypes.ByteArrayValue;
import globalResources.utilities.dataSet.valueTypes.ByteValue;
import globalResources.utilities.dataSet.valueTypes.CharacterValue;
import globalResources.utilities.dataSet.valueTypes.DoubleValue;
import globalResources.utilities.dataSet.valueTypes.FloatValue;
import globalResources.utilities.dataSet.valueTypes.IntegerValue;
import globalResources.utilities.dataSet.valueTypes.LongValue;
import globalResources.utilities.dataSet.valueTypes.ShortValue;
import globalResources.utilities.dataSet.valueTypes.StringValue;
import globalResources.utilities.dataSet.valueTypes.UUIDValue;
import globalResources.utilities.dataSet.valueTypes.VectorIntValue;
import globalResources.utilities.dataSet.valueTypes.VectorValue;

/**
 * Abstract value type that can be stored in a DataSet
 */
public abstract class Value
{
	/**
	 * Sets the value of this value type
	 * @param value the value to set
	 * @return whether the given value was valid for this value type
	 */
	public abstract boolean setValue(Object value);
	/**
	 * Gets the value of this value type
	 * @return value of the value type
	 */
	public abstract Object getValue();
	protected abstract byte[] serializeValue();
	protected abstract void deserializeValue(byte[] data);
	protected abstract Value getEmptyValue();
	protected abstract String getValueID();
	protected abstract String[] getStringLines();
	
	static List<Value> registeredValues;
	static boolean initialized = false;
	
	/**
	 * Creates a new instance of this value type with a null value
	 */
	public Value()
	{
		
	}
	
	/**
	 * Creates a new instance of this value type
	 * @param value the value to be assigned
	 */
	public Value(Object value)
	{
		boolean success = setValue(value);
		if (!success)
		{
			String valueName = "NULL";
			if (value != null) valueName = value.getClass().getName();
			throw new IllegalArgumentException("Cannot assign " + valueName + " type to " + getValueID() + " value!");
		}
	}
	
	/**
	 * Converts this value into a string in individual lines
	 * @return the individual lines of the string representation of this value
	 * @see #getAsString()
	 */
	final String[] getAsStringLines()
	{
		if (getValue() == null)
		{
			return new String[] {"NULL"};
		}
		else
		{
			return getStringLines();
		}
	}
	
	/**
	 * Converts this value into a string
	 * @return string representation of this value
	 */
	final String getAsString()
	{
		String[] lines = getAsStringLines();
		String line = "";
		for (int index = 0; index < lines.length; index++)
		{
			line = line + lines[index];
			if (index < lines.length - 1)
			{
				line += "\n";
			}
		}
		return line;
	}
	
	/**
	 * Converts this value into a byte array
	 * @return byte array containing this value
	 */
	final byte[] serialize()
	{
		byte[][] data = new byte[3][];
		data[0] = ByteConverter.fromString(getValueID());
		if (getValue() == null)
		{
			data[1] = new byte[] {ByteConverter.fromBoolean(false)};
			data[2] = new byte[0];
		}
		else
		{
			data[1] = new byte[] {ByteConverter.fromBoolean(true)};
			data[2] = serializeValue();
		}
		return ByteConverter.from2DByteArray(data);
	}
	
	/**
	 * Loads a byte array into a value type
	 * @param data the byte array to load
	 * @return the value type loaded from the byte array
	 */
	static Value deserialize(byte[] data)
	{
		byte[][] array = ByteConverter.to2DByteArray(data);
		Value value = getEmptyValue(ByteConverter.toString(array[0]));
		if (value != null)
		{
			if (ByteConverter.toBoolean(array[1][0]))
			{
				value.deserializeValue(array[2]);
			}
			return value;
		}
		return null;
	}
	
	/**
	 * Initially registers the fundamental value types
	 * <p>
	 * DataSets cannot be used until this method is called.
	 * Running this method multiple times will have no additional effect.
	 * </p>
	 */
	public static void init()
	{
		if (!initialized)
		{
			registeredValues = new ArrayList<Value>();
			registerValue(new ValueHolder());
			
			registerValue(new BooleanValue());
			registerValue(new ByteArrayValue());
			registerValue(new ByteValue());
			registerValue(new CharacterValue());
			registerValue(new DoubleValue());
			registerValue(new FloatValue());
			registerValue(new IntegerValue());
			registerValue(new LongValue());
			registerValue(new ShortValue());
			registerValue(new StringValue());
			registerValue(new UUIDValue());
			registerValue(new VectorValue());
			registerValue(new VectorIntValue());
			initialized = true;
		}
	}
	
	/**
	 * Registers a new value type
	 * @param value an empty instance of the value type to be registered
	 */
	public static void registerValue(Value value)
	{
		if (!registeredValues.contains(value))
		{
			registeredValues.add(value);
		}
	}
	
	static Value getEmptyValue(String valueID)
	{
		for (Iterator<Value> iterator = registeredValues.iterator(); iterator.hasNext();)
		{
			Value value = iterator.next();
			if (value.getValueID().compareTo(valueID) == 0)
			{
				return value.getEmptyValue();
			}
		}
		return null;
	}
}