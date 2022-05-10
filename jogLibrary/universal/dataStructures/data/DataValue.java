package jogLibrary.universal.dataStructures.data;

import java.util.ArrayList;
import java.util.List;

import jogLibrary.universal.ByteArrayBuilder;
import jogLibrary.universal.dataStructures.data.TypeRegistry.ByteConsumer;
import jogLibrary.universal.dataStructures.data.TypeRegistry.CharacterConsumer;
import jogLibrary.universal.dataStructures.data.TypeRegistry.EmptyValue;
import jogLibrary.universal.dataStructures.data.TypeRegistry.RegisteredType;
import jogLibrary.universal.dataStructures.data.TypeRegistry.ValidationValueObjects;
import jogLibrary.universal.dataStructures.data.TypeRegistry.ValidationValues;
import jogLibrary.universal.dataStructures.data.values.BooleanValue;
import jogLibrary.universal.dataStructures.data.values.IntegerValue;
import jogLibrary.universal.dataStructures.data.values.StringValue;
import jogLibrary.universal.dataStructures.data.values.UUIDValue;
import jogLibrary.universal.indexable.Consumer;
import jogLibrary.universal.indexable.Consumer.ConsumerResult;

public class DataValue extends Value<Data, Data>
{
	public static final Character[] formattingCharacters = {' ', '\r', '\n', '\t', '\b'};
	
	public DataValue()
	{
		
	}
	
	public static DataValue fromByteData(byte[] data)
	{
		ConsumerResult<Value<?, Data>, Byte> result = byteConsumer().consume(ByteArrayBuilder.indexer(data));
		if (result.success())
			return (DataValue)result.value();
		else
		{
			System.out.println(result.description());
			return new DataValue();
		}
	}
	
	public DataValue(Data value)
	{
		super(value);
	}
	
	@EmptyValue
	public Data emptyValue()
	{
		return new Data();
	}
	
	/*
	 * 4 bytes : int - size of type index (entry count)
	 * ---------------------------------- type index entry
	 * ? bytes : packed string - name
	 * ----------------------------------
	 * 4 bytes : int - size of object (entry count)
	 * ---------------------------------- value entry
	 * 4 bytes : int - type number
	 * ? bytes : packed string - name
	 * 4 bytes : int - size of value data
	 * ? bytes : raw byte stream - value
	 * ----------------------------------
	 */
	@Override
	public byte[] toByteData()
	{
		return get().toByteData();
	}
	
	@ByteConsumer
	public static Consumer<Value<?, Data>, Byte> byteConsumer()
	{
		return (source) ->
		{
			Consumer<Value<?, Integer>, Byte> integerConsumer = IntegerValue.byteConsumer();
			Consumer<Value<?, String>, Byte> stringConsumer = StringValue.byteConsumer();
			
			//get size of type index
			ConsumerResult<Value<?, Integer>, Byte> indexSizeResult = integerConsumer.consume(source);
			if (!indexSizeResult.success())
				return new ConsumerResult<>(source, "Could not parse size of type index: " + indexSizeResult.description());
			int indexSize = (int)indexSizeResult.value().get();
			
			//parse type index
			ArrayList<String> typeIndex = new ArrayList<>();
			for (int index = 0; index < indexSize; index++)
			{
				//parse type name
				ConsumerResult<Value<?, String>, Byte> entryResult = stringConsumer.consume(source);
				if (!entryResult.success())
					return new ConsumerResult<>(source, "Could not parse type index entry #" + index + ": " + entryResult.description());
				String typeName = (String)entryResult.value().get();
				
				typeIndex.add(typeName);
			}
			//get data size
			ConsumerResult<Value<?, Integer>, Byte> sizeResult = integerConsumer.consume(source);
			if (!sizeResult.success())
				return new ConsumerResult<>(source, "Could not parse data entry count: " + sizeResult.description());
			int size = (int)sizeResult.value().get();
			
			Data data = new Data();
			//parse data entries
			for (int index = 0; index < size; index++)
			{
				//parse type index
				ConsumerResult<Value<?, Integer>, Byte> indexResult = integerConsumer.consume(source);
				if (!indexResult.success())
					return new ConsumerResult<>(source, "Could not parse type index for data entry #" + index + ": " + indexResult.description());
				int indexEntry = (int)indexResult.value().get();
				
				//parse entry name
				ConsumerResult<Value<?, String>, Byte> nameResult = stringConsumer.consume(source);
				if (!nameResult.success())
					return new ConsumerResult<>(source, "Could not parse name for data entry #" + index + ": " + nameResult.description());
				String name = (String)nameResult.value().get();
				
				//parse value
				String typeName = typeIndex.get(indexEntry);
				RegisteredType type = Data.typeRegistry.get(typeName);
				if (type == null)
				{
					//parse the data length
					ConsumerResult<Value<?, Integer>, Byte> lengthResult = integerConsumer.consume(source);
					if (!lengthResult.success())
						return new ConsumerResult<>(source, "Could not parse length for data entry #" + index + ": " + lengthResult.description());
					int length = (int)lengthResult.value().get();
					
					//create InvalidValue and add to data object
					byte[] valueData = ByteArrayBuilder.toPrimitive(source.next(length));
					data.put(name, new InvalidValue(typeName, valueData));
				}
				else
				{
					//skip the data length, we don't need it for valid value types
					source.skip(4);
					
					//parse the value and add it to the data object
					ConsumerResult<Value<?, ?>, Byte> valueResult = type.byteConsumer().consume(source);
					if (!valueResult.success())
						return new ConsumerResult<>(source, "Could not parse data entry #" + index + ": " + valueResult.description());
					Value<?, ?> value = valueResult.value();
					data.put(name, value);
				}
			}
			return new ConsumerResult<>(new DataValue(data), source);
		};
	}
	
	@Override
	public DataValue makeDuplicate()
	{
		return new DataValue(get().duplicate());
	}
	
	/**
	 * {
	 * 		"String":"test1": "Hello World!",
	 * 		"Integer":"test2": 1,
	 * 		"UUID":"test3": 4cd96274-43a8-474e-8658-a1ccd24d67ac
	 * }
	 */
	@Override
	public String toString()
	{
		return get().toString();
	}
	
	@CharacterConsumer
	public static Consumer<Value<?, Data>, Character> characterConsumer()
	{
		return (source) ->
		{
			if (source.next() != '{')
				return new ConsumerResult<>(source, "Must begin with '{'");
			
			boolean expectingNext = false;
			Data data = new Data();
			while (!source.atEnd() && source.get() != '}')
			{
				expectingNext = false;
				StringValue.skipCharacters(source, DataValue.formattingCharacters);
				
				//parse value type
				ConsumerResult<String, Character> typeResult = StringValue.simpleCharacterConsume(source);
				if (!typeResult.success())
					return new ConsumerResult<>(source, "Could not parse value type for data entry #" + data.size() + ": " + typeResult.description());
				String typeName = typeResult.value();
				
				if (!source.atEnd() && source.next() != ':')
					return new ConsumerResult<>(source, "Expected ':' after type for data entry #" + data.size());
				
				//parse entry name
				ConsumerResult<String, Character> nameResult = StringValue.simpleCharacterConsume(source);
				if (!nameResult.success())
					return new ConsumerResult<>(source, "Could not parse name for data entry #" + data.size() + ": " + nameResult.description());
				String name = nameResult.value();
				
				if (!source.atEnd() && source.next() != ':')
					return new ConsumerResult<>(source, "Expected ':' after name for data entry #" + data.size());
				StringValue.skipCharacters(source, DataValue.formattingCharacters);
				
				//get registered value type
				RegisteredType type = Data.typeRegistry.get(typeName);
				if (type == null)
					return new ConsumerResult<>(source, "Value type (" + typeName + ") for data entry #" + data.size() + " is not a registered value type.");
				else
				{
					//parse value
					ConsumerResult<Value<?, ?>, Character> valueResult = type.characterConsumer().consume(source);
					if (!valueResult.success())
						return new ConsumerResult<>(source, "Could not parse value for data entry #" + data.size() + ": " + valueResult.description());
					
					//add value to data object
					data.put(name, valueResult.value());
				}
				
				StringValue.skipCharacters(source, DataValue.formattingCharacters);
				if (!source.atEnd() && source.get() == ',')
				{
					source.next();
					expectingNext = true;
					StringValue.skipCharacters(source, DataValue.formattingCharacters);
				}
			}
			if (expectingNext)
				return new ConsumerResult<>(source, "Expecting value entry after ','");
			if (source.next() != '}')
				return new ConsumerResult<>(source, "Must end with '}'");
			
			return new ConsumerResult<>(new DataValue(data), source);
		};
	}
	
	@Override
	public boolean equals(Value<?, ?> value)
	{
		if (value != null && value.getClass().equals(getClass()))
		{
			Data otherData = ((DataValue)value).get();
			Data data = get();
			if (otherData == null && data == null)
				return true;
			else
			{
				if (otherData == null || data == null)
					return false;
				if (otherData.size() != data.size())
					return false;
				
				String[] names = data.names();
				for (int index = 0; index < names.length; index++)
				{
					if (!otherData.has(names[index]))
						return false;
					
					Value<?, ?> value1 = data.get(names[index], new InvalidValue());
					Value<?, ?> value2 = otherData.get(names[index], new InvalidValue());
					if (!value1.equals(value2))
						return false;
				}
				return true;
			}
		}
		else
			return false;
	}
	
	@ValidationValues
	public static List<Data> validationValues()
	{
		ArrayList<Data> list = new ArrayList<>(1);
		
		Data data = new Data();
		
		data.put("test1", IntegerValue.validationValueObjects().get(0));
		data.put("test2", StringValue.validationValueObjects().get(0));
		data.put("test3", ListValue.validationValueObjects().get(0));
		data.put("test4", BooleanValue.validationValueObjects().get(0));
		data.put("test5", UUIDValue.validationValueObjects().get(0));
		
		list.add(data);
		
		return list;
	}
	
	@ValidationValueObjects
	public static List<Value<?, Data>> validationValueObjects()
	{
		List<Data> values = validationValues();
		ArrayList<Value<?, Data>> objects = new ArrayList<>(values.size());
		values.forEach(value -> objects.add(new DataValue(value)));
		return objects;
	}
}