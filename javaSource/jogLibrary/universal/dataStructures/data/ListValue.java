package jogLibrary.universal.dataStructures.data;

import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

import jogLibrary.universal.ByteArrayBuilder;
import jogLibrary.universal.dataStructures.data.TypeRegistry.ByteConsumer;
import jogLibrary.universal.dataStructures.data.TypeRegistry.CharacterConsumer;
import jogLibrary.universal.dataStructures.data.TypeRegistry.EmptyValue;
import jogLibrary.universal.dataStructures.data.TypeRegistry.RegisteredType;
import jogLibrary.universal.dataStructures.data.TypeRegistry.ValidationValueObjects;
import jogLibrary.universal.dataStructures.data.TypeRegistry.ValidationValues;
import jogLibrary.universal.dataStructures.data.values.BooleanValue;
import jogLibrary.universal.dataStructures.data.values.ByteValue;
import jogLibrary.universal.dataStructures.data.values.IntegerValue;
import jogLibrary.universal.dataStructures.data.values.StringValue;
import jogLibrary.universal.dataStructures.data.values.UUIDValue;
import jogLibrary.universal.indexable.Consumer;
import jogLibrary.universal.indexable.Consumer.ConsumerResult;

public class ListValue<Type extends Value<?, ?>> extends Value<List<Type>, List<Value<?, ?>>> implements List<Type>
{
	private final RegisteredType typeClass;
	
	public ListValue(RegisteredType typeClass)
	{
		this.typeClass = typeClass;
		if (typeClass == null)
			throw new IllegalArgumentException("List values can only be created using registered value types.");
	}
	
	public ListValue(RegisteredType typeClass, List<Type> value)
	{
		this.typeClass = typeClass;
		set(value);
		if (typeClass == null)
			throw new IllegalArgumentException("List values can only be created using registered value types.");
	}
	
	public static <ListType extends Value<?, ?>> ListValue<ListType> create(RegisteredType typeClass, List<ListType> value)
	{
		return new ListValue<ListType>(typeClass, value);
	}
	
	public static interface ListChangeListener<Type extends Value<?, ?>>
	{
		void cleared();
		void valueAdded(Type value);
		void valueRemoved(Type value);
		void collectionAdded(Collection<? extends Type> collection);
		void collectionRemoved(Collection<?> collection);
		void valueChanged(int index, Type newValue, Type oldValue);
	}
	
	ArrayList<ListChangeListener<Type>> listeners = new ArrayList<>();
	
	public ListChangeListener<Type> addListChangeListener(ListChangeListener<Type> listener)
	{
		listeners.add(listener);
		return listener;
	}
	
	public void removeListChangeListener(ListChangeListener<Type> listener)
	{
		listeners.remove(listener);
	}
	
	@Override
	public void set(List<Type> value)
	{
		if (value == null)
		{
			super.set(null);
		}
		else if (value.size() == 0)
		{
			super.set(new ArrayList<Type>());
		}
		else
		{
			ArrayList<Type> list = new ArrayList<>();
			for (int index = 0; index < value.size(); index++)
			{
				Type entry = value.get(index);
				if (typeClass.typeClass().getGenericSuperclass().equals(entry.getClass().getGenericSuperclass()))
					list.add(entry);
				else
					throw new IllegalArgumentException("Value type is not the same as list type. A " + typeClass.typeClass().getGenericSuperclass() + " list can not contain a " + entry.getClass().getGenericSuperclass() + " value.");
			}
			super.set(list);
		}
	}
	
	@EmptyValue
	public List<Value<?, ?>> emptyValue()
	{
		return new ArrayList<Value<?, ?>>();
	}
	
	/**
	 * ["Integer":
	 * 		1,
	 * 		2,
	 * 		3
	 * ]
	 */
	@Override
	public String toString()
	{
		StringBuilder builder = new StringBuilder();
		builder.append("[" + StringValue.pack(typeClass.name()) + ":\r\n");
		for (int index = 0; index < size(); index++)
		{
			builder.append('\t');
			Type value = get(index);
			String string = value.toString();
			for (int chIndex = 0; chIndex < string.length(); chIndex++)
			{
				char ch = string.charAt(chIndex);
				builder.append(ch);
				if (ch == '\n')
					builder.append('\t');
			}
			if (index < size() - 1)
				builder.append(',');
			builder.append("\r\n");
		}
		builder.append("]");
		
		String string = builder.toString();
		
		return string;
	}

	@CharacterConsumer
	public static Consumer<Value<?, List<Value<?, ?>>>, Character> characterConsumer()
	{
		return (source ->
		{
			if (source.atEnd())
				return new ConsumerResult<>(source, "Not enough data.");
			if (source.next() != '[')
				return new ConsumerResult<>(source, "Must start with '['");
			StringValue.skipCharacters(source, DataValue.formattingCharacters);
			ConsumerResult<String, Character> typeResult = StringValue.simpleCharacterConsume(source);
			if (!typeResult.success())
				return new ConsumerResult<>(source, "Could not parse list type: " + typeResult.description());
			RegisteredType type = Data.typeRegistry.get(typeResult.value());
			if (type == null)
				return new ConsumerResult<>(source, "List type (" + typeResult.value() + ") is not a registered type.");
			StringValue.skipCharacters(source, DataValue.formattingCharacters);
			if (!source.atEnd() && source.next() != ':')
				return new ConsumerResult<>(source, "Expected ':' after list type.");
			StringValue.skipCharacters(source, DataValue.formattingCharacters);
			boolean expectingNext = false;
			ArrayList<Value<?, ?>> values = new ArrayList<>();
			Consumer<Value<?, ?>, Character> consumer = type.characterConsumer();
			while (!source.atEnd() && source.get() != ']')
			{
				expectingNext = false;
				ConsumerResult<Value<?, ?>, Character> result = consumer.consume(source);
				if (!result.success())
					return new ConsumerResult<>(source, "Error parsing entry #" + values.size() + ": " + result.description());
				values.add(result.value());
				StringValue.skipCharacters(source, DataValue.formattingCharacters);
				if (!source.atEnd() && source.get() == ',')
				{
					source.next();
					expectingNext = true;
					StringValue.skipCharacters(source, DataValue.formattingCharacters);
				}
			}
			if (expectingNext)
				return new ConsumerResult<>(source, "Value entry expected after ','");
			if (!source.atEnd() && source.next() != ']')
				return new ConsumerResult<>(source, "Must end with ']'");
			return new ConsumerResult<>(create(type, values), source);
		});
	}
	
	/**
	 * name,length,{values}
	 * 	dataLength,data
	 */
	@Override
	public byte[] toByteData()
	{
		ByteArrayBuilder builder = new ByteArrayBuilder();
		List<Type> values = get();
		builder.add(typeClass.name());
		builder.add(values.size());
		values.forEach(value ->
		{
			byte[] data = value.toByteData();
			builder.add(data.length);
			builder.add(data);
		});
		return builder.toPrimitiveArray();
	}

	@ByteConsumer
	public static Consumer<Value<?, List<Value<?, ?>>>, Byte> byteConsumer()
	{
		return (source ->
		{
			Consumer<Value<?, Integer>, Byte> integerConsumer = IntegerValue.byteConsumer();
			Consumer<Value<?, String>, Byte> stringConsumer = StringValue.byteConsumer();
			
			//get the name of this lists type
			ConsumerResult<Value<?, String>, Byte> typeResult = stringConsumer.consume(source);
			if (!typeResult.success())
				return new ConsumerResult<>(source, "Could not parse type name: " + typeResult.description());
			String typeName = (String)typeResult.value().get();
			
			//get the length of this list
			ConsumerResult<Value<?, Integer>, Byte> lengthResult = integerConsumer.consume(source);
			if (!lengthResult.success())
				return new ConsumerResult<>(source, "Could not parse list length: " + lengthResult.description());
			int listLength = (int)lengthResult.value().get();
			
			//get the registered type for this list
			RegisteredType type = Data.typeRegistry.get(typeName);
			
			if (type == null)//if there is no type registered by that name, we must load the list as invalid values
			{
				ListValue<InvalidValue> list = new ListValue<>(Data.typeRegistry.get(InvalidValue.class));
				for (int index = 0; index < listLength; index++)
				{
					//get the length of this elements data
					ConsumerResult<Value<?, Integer>, Byte> dataLengthResult = integerConsumer.consume(source);
					if (!dataLengthResult.success())
						return new ConsumerResult<>(source, "Could not parse data length for list element #" + index + ": " + dataLengthResult.description());
					int dataLength = (int)dataLengthResult.value().get();
					
					//parse the elements data and add to the list
					byte[] data = ByteArrayBuilder.toPrimitive(source.next(dataLength));
					list.add(new InvalidValue(typeName, data));
				}
				return new ConsumerResult<>(list, source);
			}
			else//if a registered type exists for the given name, load the elements
			{
				Consumer<Value<?, ?>, Byte> valueConsumer = type.byteConsumer();
				ListValue<Value<?, ?>> list = new ListValue<>(type);
				for (int index = 0; index < listLength; index++)
				{
					//skip the data length, we don't need it in this case
					source.skip(4);
					
					//parse element and add to list
					ConsumerResult<Value<?, ?>, Byte> result = valueConsumer.consume(source);
					if (!result.success())
						return new ConsumerResult<>(source, "Could not parse list element #" + index + ": " + result.description());
					list.add(result.value());
				}
				
				return new ConsumerResult<>(list, source);
			}
		});
	}

	@Override
	public ListValue<Type> makeDuplicate()
	{
		return create(typeClass, get());
	}

	@Override
	public boolean equals(Value<?, ?> value)
	{
		if (!(value instanceof ListValue && ((ParameterizedType)value.getClass().getGenericSuperclass()).getActualTypeArguments()[0].equals(((ParameterizedType)this.getClass().getGenericSuperclass()).getActualTypeArguments()[0])))
			return false;
		@SuppressWarnings("unchecked")
		ListValue<Type> list = (ListValue<Type>)value;
		if (!typeClass.equals(list.typeClass))
			return false;
		if (list.size() != size())
			return false;
		for (int index = 0; index < list.size(); index++)
		{
			if (!list.get(index).equals(get(index)))
				return false;
		}
		return true;
	}

	@ValidationValues
	public static List<List<Value<?, ?>>> validationValues()
	{
		List<Value<?, List<Value<?, ?>>>> objects = validationValueObjects();
		ArrayList<List<Value<?, ?>>> values = new ArrayList<>();
		for (Iterator<Value<?, List<Value<?, ?>>>> listValueIterator = objects.iterator(); listValueIterator.hasNext();)
		{
			ListValue<?> list = (ListValue<?>)listValueIterator.next();
			ArrayList<Value<?, ?>> subList = new ArrayList<>();
			subList.addAll(list);
			values.add(subList);
		}
		return values;
	}
	
	@ValidationValueObjects
	public static List<Value<?, List<Value<?, ?>>>> validationValueObjects()
	{
		ArrayList<Value<?, List<Value<?, ?>>>> list = new ArrayList<>();
		list.add(makeValidationList(IntegerValue.class));
		list.add(makeValidationList(BooleanValue.class));
		list.add(makeValidationList(ByteValue.class));
		list.add(makeValidationList(UUIDValue.class));
		return list;
	}
	
	private static <ListType extends Value<?, ?>> ListValue<ListType> makeValidationList(Class<ListType> valueClass)
	{
		RegisteredType type = Data.typeRegistry.get(valueClass);
		@SuppressWarnings("unchecked")
		List<ListType> list = (List<ListType>)type.validationValueObjects();
		return create(type, list);
	}
	
	public Type addValue(Type e)
	{
		if (get().add(e))
			listeners.forEach(listener -> {listener.valueAdded(e);});
		return e;
	}
	
	@Override
	public boolean add(Type e)
	{
		if (get().add(e))
		{
			listeners.forEach(listener -> {listener.valueAdded(e);});
			return true;
		}
		else
			return false;
	}
	
	@Override
	public void add(int index, Type element)
	{
		get().add(index, element);
		listeners.forEach(listener -> {listener.valueAdded(element);});
	}
	
	@Override
	public boolean addAll(Collection<? extends Type> c)
	{
		boolean changed = get().addAll(c);
		listeners.forEach(listener -> {listener.collectionAdded(c);});
		return changed;
	}
	
	@Override
	public boolean addAll(int index, Collection<? extends Type> c)
	{
		boolean changed = get().addAll(index, c);
		listeners.forEach(listener -> {listener.collectionAdded(c);});
		return changed;
	}
	
	@Override
	public void clear()
	{
		get().clear();
		listeners.forEach(listener -> {listener.cleared();});
	}
	
	@Override
	public boolean contains(Object o)
	{
		return get().contains(o);
	}
	
	@Override
	public boolean containsAll(Collection<?> c)
	{
		return get().containsAll(c);
	}
	
	@Override
	public Type get(int index)
	{
		return get().get(index);
	}
	
	@Override
	public int indexOf(Object o)
	{
		return get().indexOf(o);
	}
	
	@Override
	public boolean isEmpty()
	{
		return get().isEmpty();
	}
	
	@Override
	public Iterator<Type> iterator()
	{
		return get().iterator();
	}
	
	@Override
	public int lastIndexOf(Object o)
	{
		return get().lastIndexOf(o);
	}
	
	@Override
	public ListIterator<Type> listIterator()
	{
		return get().listIterator();
	}
	
	@Override
	public ListIterator<Type> listIterator(int index)
	{
		return get().listIterator(index);
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public boolean remove(Object o)
	{
		if (get().remove(o))
		{
			listeners.forEach(listener -> {listener.valueRemoved((Type)o);});
			return true;
		}
		else
			return false;
	}
	
	@Override
	public Type remove(int index)
	{
		if (index >= 0 && index < size())
		{
			Type entry = get(index);
			remove(entry);
			return entry;
		}
		else
			throw new IndexOutOfBoundsException(index + " is not in range of 0-" + (size() - 1));
	}
	
	@Override
	public boolean removeAll(Collection<?> c)
	{
		boolean changed = get().removeAll(c);
		listeners.forEach(listener -> {listener.collectionRemoved(c);});
		return changed;
	}
	
	@Override
	public boolean retainAll(Collection<?> c)
	{
		ArrayList<Type> removed = new ArrayList<>();
		get().forEach(value ->
		{
			if (!c.contains(value))
				removed.add(value);
		});
		boolean changed = get().retainAll(c);
		listeners.forEach(listener -> {listener.collectionRemoved(removed);});
		return changed;
	}
	
	@Override
	public Type set(int index, Type element)
	{
		Type old = get().set(index, element);
		listeners.forEach(listener -> {listener.valueChanged(index, element, old);});
		return old;
	}
	
	@Override
	public int size()
	{
		return get().size();
	}
	
	@Override
	public List<Type> subList(int fromIndex, int toIndex)
	{
		return get().subList(fromIndex, toIndex);
	}
	
	@Override
	public Object[] toArray()
	{
		return get().toArray();
	}
	
	@Override
	public <T> T[] toArray(T[] a)
	{
		return get().toArray(a);
	}
}