package jogLibrary.universal.dataStructures.data;

import java.util.ArrayList;
import java.util.Iterator;

import jogLibrary.universal.ByteArrayBuilder;
import jogLibrary.universal.dataStructures.KeyedList;
import jogLibrary.universal.dataStructures.KeyedList.KeyedEntry;
import jogLibrary.universal.dataStructures.data.values.StringValue;

public class Data implements Iterable<Value<?, ?>>
{
	public static final TypeRegistry typeRegistry = new TypeRegistry();
	public static final boolean defaultValuesValidated = typeRegistry.init(true);
	KeyedList<String, Value<?, ?>> values = new KeyedList<>();
	
	public static final void init()
	{
		
	}
	
	public int size()
	{
		return values.size();
	}
	
	public boolean has(String name)
	{
		return values.containsKey(name);
	}
	
	/**
	 * Adds a new value entry to this Data object.
	 * <p>
	 * If the given value is already stored in a Data object, then a new copy unique copy
	 * will be created to be stored in this one. In either case, whatever value gets stored
	 * in this object will be returned for external reference.
	 * <p>
	 * If a value has already been entered into this object with the same name,
	 * it will be overwritten with the new value. Any external references to the old value
	 * will remain intact, and the old value can still be used externally.
	 * @param name The name to be given to this value.
	 * @param value The value to be stored.
	 * @return A reference to the internal copy of the value that was stored.
	 * @since 1.0
	 * @author TheJogMan
	 */
	public Value<?, ?> put(String name, Value<?, ?> value)
	{
		if (value.parent != null)
			value = value.duplicate();
		
		if (values.containsKey(name))
			values.remove(name);
		
		values.put(name, value);
		value.parent = this;
		value.name = name;
		return value;
	}
	
	/**
	 * Retrieves a value from this Data object.
	 * <p>
	 * If there is no value in this object with the given name, then the provided default value will be returned instead.
	 * It will also be put into this data object, but it's persistence isn't guaranteed. See {@link jogLibrary.universal.dataStructures.data.Data} to learn more about value persistence.<br>
	 * If the given default value was part of a Data object, then a new copy of it will be used.
	 * @param name The name of the value to be retrieved.
	 * @param defaultValue The value to be returned if one is not present in this object.
	 * @return The retrieved value, or the provided default if one isn't present.
	 * @since 1.0
	 * @author TheJogMan
	 */
	public Value<?, ?> get(String name, Value<?, ?> defaultValue)
	{
		if (values.containsKey(name))
			return values.get(name);
		else
			return put(name, defaultValue);
	}
	
	/**
	 * Removes a value from this data object.
	 * @param name The name of the value to be removed.
	 * @return The value that was removed, or null if no value was present.
	 * @since 1.0
	 * @author TheJogMan
	 */
	public Value<?, ?> remove(String name)
	{
		if (values.containsKey(name))
		{
			Value<?, ?> value = values.get(name);
			values.remove(name);
			value.parent = null;
			value.name = null;
			return value;
		}
		else
			return null;
	}
	
	/**
	 * Creates an identical copy of this Data object.
	 * <p>
	 * All of the contained values are also duplicated.
	 * @return Duplicate copy of this Data object.
	 * @since 1.0
	 * @author TheJogMan
	 */
	public Data duplicate()
	{
		Data data = new Data();
		for (Iterator<Value<?, ?>> iterator = iterator(); iterator.hasNext();)
		{
			Value<?, ?> value = iterator.next();
			data.put(value.name, value.duplicate());
		}
		return data;
	}
	
	/**
	 * Returns an iterator of the values in this Data object.
	 * @return Iterator of type Value<?>
	 * @since 1.0
	 * @author TheJogMan
	 */
	@Override
	public Iterator<Value<?, ?>> iterator()
	{
		return values.iterator();
	}
	
	/**
	 * Provides a list of all the names of the values in this Data object.
	 * @return Name list.
	 * @since 1.0
	 * @author TheJogMan
	 */
	public String[] names()
	{
		String[] names = new String[values.size()];
		int index = 0;
		for (Iterator<Value<?, ?>> iterator = iterator(); iterator.hasNext();)
		{
			names[index] = iterator.next().name;
			index++;
		}
		return names;
	}
	
	@Override
	public String toString()
	{
		StringBuilder builder = new StringBuilder();
		builder.append("{\r\n");
		for (int valueIndex = 0; valueIndex < values.size(); valueIndex++)
		{
			KeyedEntry<String, Value<?, ?>> entry = values.get(valueIndex);
			String name = entry.getKey();
			Value<?, ?> value = entry.getValue();
			
			@SuppressWarnings("unchecked")
			Class<? extends Value<?, ?>> clss = (Class<? extends Value<?, ?>>)value.getClass();
			
			builder.append("\t" + StringValue.pack(Data.typeRegistry.get(clss).name()) + ":");
			
			builder.append(StringValue.pack(name) + ": ");
			String string = value.toString();
			for (int index = 0; index < string.length(); index++)
			{
				char ch = string.charAt(index);
				builder.append(ch);
				if (ch == '\n')
					builder.append('\t');
			}
			if (valueIndex < values.size() - 1)
				builder.append(',');
			builder.append("\r\n");
		}
		builder.append('}');
		
		return builder.toString();
	}
	
	public byte[] toByteData()
	{
		ByteArrayBuilder builder = new ByteArrayBuilder();
		ArrayList<Class<? extends Value<?, ?>>> typeIndex = new ArrayList<>();
		for (Iterator<Value<?, ?>> iterator = iterator(); iterator.hasNext();)
		{
			@SuppressWarnings("unchecked")
			Class<? extends Value<?, ?>> clss = (Class<? extends Value<?, ?>>)iterator.next().getClass();
			if (!typeIndex.contains(clss))
				typeIndex.add(clss);
		}
		builder.add(typeIndex.size());
		for (int index = 0; index < typeIndex.size(); index++)
			builder.add(Data.typeRegistry.get(typeIndex.get(index)).name());
		
		builder.add(size());
		for (Iterator<Value<?, ?>> iterator = iterator(); iterator.hasNext();)
		{
			Value<?, ?> value = iterator.next();
			builder.add(typeIndex.indexOf(value.getClass()));
			builder.add(value.name);
			byte[] valueData = value.toByteData();
			builder.add(valueData.length);
			builder.add(valueData);
		}
		return builder.toPrimitiveArray();
	}
}