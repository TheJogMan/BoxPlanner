package globalResources.utilities.dataSet;

import java.awt.Color;
import java.util.UUID;

import globalResources.utilities.SecureList;
import globalResources.utilities.Vector;
import globalResources.utilities.VectorInt;
import globalResources.utilities.dataSet.valueTypes.BooleanValue;
import globalResources.utilities.dataSet.valueTypes.ByteArrayValue;
import globalResources.utilities.dataSet.valueTypes.ByteValue;
import globalResources.utilities.dataSet.valueTypes.CharacterValue;
import globalResources.utilities.dataSet.valueTypes.ColorValue;
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
 * Used to store data for the purpose of IO
 */
public class DataSet
{
	ValueMap map;
	
	/**
	 * Creates a new DataSet
	 */
	public DataSet()
	{
		map = new ValueMap();
	}
	
	DataSet(ValueMap map)
	{
		this.map = map;
	}
	
	/**
	 * Creates a new DataSet and loads data from the given byte array
	 * @param data byte array to load data from
	 */
	public DataSet(byte[] data)
	{
		ValueHolder holder = new ValueHolder();
		holder.deserializeValue(data);
		map = (ValueMap)holder.getValue();
	}
	
	/**
	 * Convert this DataSet into a string
	 * @return string representation of this data
	 */
	public String getAsString()
	{
		ValueHolder holder = new ValueHolder();
		holder.setValue(map);
		return holder.getAsString();
	}
	
	/**
	 * Converts this DataSet into a string in individual lines
	 * @return individual lines from the string representation of this data
	 * @see #getAsString()
	 */
	public String[] getAsStringLines()
	{
		ValueHolder holder = new ValueHolder();
		holder.setValue(map);
		return holder.getAsStringLines();
	}
	
	/**
	 * Convert this DataSet into a byte array
	 * @return byte array containing the data from this DataSet
	 */
	public byte[] getAsBytes()
	{
		ValueHolder holder = new ValueHolder();
		holder.setValue(map);
		return holder.serializeValue();
	}
	
	/**
	 * Gets a list of all the keys in this DataSet
	 * @return key list
	 */
	public SecureList<String> getKeyList()
	{
		return this.map.keyList();
	}
	
	/**
	 * Gets the number of keys in this DataSet
	 * @return key count
	 */
	public int size()
	{
		return this.map.size();
	}
	
	/**
	 * Sets the value of a key to a DataSet
	 * @param key the key to set
	 * @param set the DataSet to store
	 */
	public void set(String key, DataSet set)
	{
		ValueHolder holder = new ValueHolder(set.map);
		this.map.put(key, holder);
	}
	
	/**
	 * Checks if this DataSet contains a certain key
	 * @param key the key to check
	 * @return if this DataSet contains the key
	 */
	public boolean contains(String key)
	{
		return map.containsKey(key);
	}
	
	/**
	 * Gets the value of a key as a dataSet
	 * @param key the key to get
	 * @return the DataSet that was retrieved from the given key
	 */
	public DataSet getSubSet(String key)
	{
		if (map.containsKey(key))
		{
			Value value = map.get(key);
			if (value instanceof ValueHolder)
			{
				if (map.get(key).getValue() == null)
				{
					return new DataSet();
				}
				return new DataSet((ValueMap)map.get(key).getValue());
			}
		}
		return new DataSet();
	}
	
	public void removeEntry(String key)
	{
		if (map.containsKey(key)) map.remove(key);
	}
	
	
	
	
	/**
	 * Sets the value of a key to a generic value type
	 * @param key the key to set
	 * @param value the value to store
	 */
	public void set(String key, Value value)
	{
		map.put(key, value);
	}
	
	/**
	 * Sets the value of a key to a boolean
	 * @param key the key to set
	 * @param value the boolean to store
	 * @see #set(String, Value)
	 */
	public void set(String key, boolean value)
	{
		map.put(key, new BooleanValue(value));
	}
	
	public void set(String key, VectorInt value)
	{
		map.put(key, new VectorIntValue(value));
	}
	
	public void set(String key, Color value)
	{
		map.put(key, new ColorValue(value));
	}
	
	/**
	 * Sets the value of a key to a byte array
	 * @param key the key to set
	 * @param value the byte array to store
	 * @see #set(String, Value)
	 */
	public void set(String key, byte[] value)
	{
		map.put(key, new ByteArrayValue(value));
	}
	
	/**
	 * Sets the value of a key to a byte
	 * @param key the key to set
	 * @param value the byte to store
	 * @see #set(String, Value)
	 */
	public void set(String key, byte value)
	{
		map.put(key, new ByteValue(value));
	}
	
	/**
	 * Sets the value of a key to a char
	 * @param key the key to set
	 * @param value the char to store
	 * @see #set(String, Value)
	 */
	public void set(String key, char value)
	{
		map.put(key, new CharacterValue(value));
	}
	
	/**
	 * Sets the value of a key to a double
	 * @param key the key to set
	 * @param value the double to store
	 * @see #set(String, Value)
	 */
	public void set(String key, double value)
	{
		map.put(key, new DoubleValue(value));
	}
	
	/**
	 * Sets the value of a key to a float
	 * @param key the key to set
	 * @param value the float to store
	 * @see #set(String, Value)
	 */
	public void set(String key, Float value)
	{
		map.put(key, new FloatValue(value));
	}
	
	/**
	 * Sets the value of a key to an int
	 * @param key the key to set
	 * @param value the int to store
	 * @see #set(String, Value)
	 */
	public void set(String key, int value)
	{
		map.put(key, new IntegerValue(value));
	}
	
	/**
	 * Sets the value of a key to a long
	 * @param key the key to set
	 * @param value the long to store
	 * @see #set(String, Value)
	 */
	public void set(String key, long value)
	{
		map.put(key, new LongValue(value));
	}
	
	/**
	 * Sets the value of a key to a short
	 * @param key the key to set
	 * @param value the short to store
	 * @see #set(String, Value)
	 */
	public void set(String key, Short value)
	{
		map.put(key, new ShortValue(value));
	}
	
	/**
	 * Sets the value of a key to a String
	 * @param key the key to set
	 * @param value the String to store
	 * @see #set(String, Value)
	 */
	public void set(String key, String value)
	{
		map.put(key, new StringValue(value));
	}
	
	/**
	 * Sets the value of a key to a UUID
	 * @param key the key to set
	 * @param value the UUID to store
	 * @see #set(String, Value)
	 */
	public void set(String key, UUID value)
	{
		map.put(key, new UUIDValue(value));
	}
	
	/**
	 * Sets the value of a key to a Vector
	 * @param key the key to set
	 * @param value the Vector to store
	 * @see #set(String, Value)
	 */
	public void set(String key, Vector value)
	{
		map.put(key, new VectorValue(value));
	}
	
	
	
	
	
	/**
	 * Gets the value of a key as a generic value type
	 * @param key the key to get
	 * @param fallBack the default value to return if the key doesn't exist or contains a different value type
	 * @return the value that was retrieved from the given key
	 */
	public Value getValue(String key, Value fallBack)
	{
		if (map.containsKey(key))
		{
			Value value = map.get(key);
			if (value.getClass().equals(fallBack.getClass()))
			{
				if (value.getValue() == null)
				{
					return fallBack;
				}
				return value;
			}
		}
		return fallBack;
	}
	
	/**
	 * Gets the value of a key as a boolean
	 * @param key the key to get
	 * @param fallBack the default value to return if the key doesn't exist or contains a different value type
	 * @return the boolean that was retrieved from the given key
	 * @see #getValue(String, Value)
	 */
	public boolean getBoolean(String key, boolean fallBack)
	{
		if (map.containsKey(key))
		{
			Value value = map.get(key);
			if (value instanceof BooleanValue)
			{
				if (value.getValue() == null)
				{
					return fallBack;
				}
				return (boolean)value.getValue();
			}
		}
		return fallBack;
	}
	
	public VectorInt getVectorInt(String key, VectorInt fallBack)
	{
		if (map.containsKey(key))
		{
			Value value = map.get(key);
			if (value instanceof VectorIntValue)
			{
				if (value.getValue() == null)
				{
					return fallBack;
				}
				return (VectorInt)value.getValue();
			}
		}
		return fallBack;
	}
	
	public Color getColor(String key, Color fallBack)
	{
		if (map.containsKey(key))
		{
			Value value = map.get(key);
			if (value instanceof ColorValue)
			{
				if (value.getValue() == null)
				{
					return fallBack;
				}
				return (Color)value.getValue();
			}
		}
		return fallBack;
	}
	
	/**
	 * Gets the value of a key as a byte array
	 * @param key the key to get
	 * @param fallBack the default value to return if the key doesn't exist or contains a different value type
	 * @return the byte array that was retrieved from the given key
	 * @see #getValue(String, Value)
	 */
	public byte[] getByteArray(String key, byte[] fallBack)
	{
		if (map.containsKey(key))
		{
			Value value = map.get(key);
			if (value instanceof ByteArrayValue)
			{
				if (value.getValue() == null)
				{
					return fallBack;
				}
				return (byte[])value.getValue();
			}
		}
		return fallBack;
	}
	
	/**
	 * Gets the value of a key as a byte
	 * @param key the key to get
	 * @param fallBack the default value to return if the key doesn't exist or contains a different value type
	 * @return the byte that was retrieved from the given key
	 * @see #getValue(String, Value)
	 */
	public byte getByte(String key, byte fallBack)
	{
		if (map.containsKey(key))
		{
			Value value = map.get(key);
			if (value instanceof ByteValue)
			{
				if (value.getValue() == null)
				{
					return fallBack;
				}
				return (byte)value.getValue();
			}
		}
		return fallBack;
	}
	
	/**
	 * Gets the value of a key as a char
	 * @param key the key to get
	 * @param fallBack the default value to return if the key doesn't exist or contains a different value type
	 * @return the char that was retrieved from the given key
	 * @see #getValue(String, Value)
	 */
	public char getCharacter(String key, char fallBack)
	{
		if (map.containsKey(key))
		{
			Value value = map.get(key);
			if (value instanceof CharacterValue)
			{
				if (value.getValue() == null)
				{
					return fallBack;
				}
				return (char)value.getValue();
			}
		}
		return fallBack;
	}
	
	/**
	 * Gets the value of a key as a double
	 * @param key the key to get
	 * @param fallBack the default value to return if the key doesn't exist or contains a different value type
	 * @return the double that was retrieved from the given key
	 * @see #getValue(String, Value)
	 */
	public double getDouble(String key, double fallBack)
	{
		if (map.containsKey(key))
		{
			Value value = map.get(key);
			if (value instanceof DoubleValue)
			{
				if (value.getValue() == null)
				{
					return fallBack;
				}
				return (double)value.getValue();
			}
		}
		return fallBack;
	}
	
	/**
	 * Gets the value of a key as a float
	 * @param key the key to get
	 * @param fallBack the default value to return if the key doesn't exist or contains a different value type
	 * @return the float that was retrieved from the given key
	 * @see #getValue(String, Value)
	 */
	public float getFloat(String key, float fallBack)
	{
		if (map.containsKey(key))
		{
			Value value = map.get(key);
			if (value instanceof FloatValue)
			{
				if (value.getValue() == null)
				{
					return fallBack;
				}
				return (float)value.getValue();
			}
		}
		return fallBack;
	}
	
	/**
	 * Gets the value of a key as an int
	 * @param key the key to get
	 * @param fallBack the default value to return if the key doesn't exist or contains a different value type
	 * @return the int that was retrieved from the given key
	 * @see #getValue(String, Value)
	 */
	public int getInteger(String key, int fallBack)
	{
		if (map.containsKey(key))
		{
			Value value = map.get(key);
			if (value instanceof IntegerValue)
			{
				if (value.getValue() == null)
				{
					return fallBack;
				}
				return (int)value.getValue();
			}
		}
		return fallBack;
	}
	
	/**
	 * Gets the value of a key as a long
	 * @param key the key to get
	 * @param fallBack the default value to return if the key doesn't exist or contains a different value type
	 * @return the long that was retrieved from the given key
	 * @see #getValue(String, Value)
	 */
	public long getLong(String key, long fallBack)
	{
		if (map.containsKey(key))
		{
			Value value = map.get(key);
			if (value instanceof LongValue)
			{
				if (value.getValue() == null)
				{
					return fallBack;
				}
				return (long)value.getValue();
			}
		}
		return fallBack;
	}
	
	/**
	 * Gets the value of a key as a short
	 * @param key the key to get
	 * @param fallBack the default value to return if the key doesn't exist or contains a different value type
	 * @return the short that was retrieved from the given key
	 * @see #getValue(String, Value)
	 */
	public short getShort(String key, short fallBack)
	{
		if (map.containsKey(key))
		{
			Value value = map.get(key);
			if (value instanceof ShortValue)
			{
				if (value.getValue() == null)
				{
					return fallBack;
				}
				return (short)value.getValue();
			}
		}
		return fallBack;
	}
	
	/**
	 * Gets the value of a key as a string
	 * @param key the key to get
	 * @param fallBack the default value to return if the key doesn't exist or contains a different value type
	 * @return the string that was retrieved from the given key
	 * @see #getValue(String, Value)
	 */
	public String getString(String key, String fallBack)
	{
		if (map.containsKey(key))
		{
			Value value = map.get(key);
			if (value instanceof StringValue)
			{
				if (value.getValue() == null)
				{
					return fallBack;
				}
				return (String)value.getValue();
			}
		}
		return fallBack;
	}
	
	/**
	 * Gets the value of a key as a UUID
	 * @param key the key to get
	 * @param fallBack the default value to return if the key doesn't exist or contains a different value type
	 * @return the UUID that was retrieved from the given key
	 * @see #getValue(String, Value)
	 */
	public UUID getUUID(String key, UUID fallBack)
	{
		if (map.containsKey(key))
		{
			Value value = map.get(key);
			if (value instanceof UUIDValue)
			{
				if (value.getValue() == null)
				{
					return fallBack;
				}
				return (UUID)value.getValue();
			}
		}
		return fallBack;
	}
	
	/**
	 * Gets the value of a key as a vector
	 * @param key the key to get
	 * @param fallBack the default value to return if the key doesn't exist or contains a different value type
	 * @return the vector that was retrieved from the given key
	 * @see #getValue(String, Value)
	 */
	public Vector getVector(String key, Vector fallBack)
	{
		if (map.containsKey(key))
		{
			Value value = map.get(key);
			if (value instanceof VectorValue)
			{
				if (value.getValue() == null)
				{
					return fallBack;
				}
				return (Vector)value.getValue();
			}
		}
		return fallBack;
	}
}