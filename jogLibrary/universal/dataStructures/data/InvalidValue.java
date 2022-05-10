package jogLibrary.universal.dataStructures.data;

import java.util.ArrayList;
import java.util.List;

import jogLibrary.universal.ByteArrayBuilder;
import jogLibrary.universal.dataStructures.data.InvalidValue.InvalidValueHolder;
import jogLibrary.universal.dataStructures.data.TypeRegistry.ByteConsumer;
import jogLibrary.universal.dataStructures.data.TypeRegistry.CharacterConsumer;
import jogLibrary.universal.dataStructures.data.TypeRegistry.EmptyValue;
import jogLibrary.universal.dataStructures.data.TypeRegistry.ValidationValueObjects;
import jogLibrary.universal.dataStructures.data.TypeRegistry.ValidationValues;
import jogLibrary.universal.dataStructures.data.values.CharacterValue;
import jogLibrary.universal.dataStructures.data.values.IntegerValue;
import jogLibrary.universal.dataStructures.data.values.StringValue;
import jogLibrary.universal.indexable.Consumer;
import jogLibrary.universal.indexable.Consumer.ConsumerResult;
import jogLibrary.universal.indexable.VectorIndexable;

public class InvalidValue extends Value<InvalidValueHolder, InvalidValueHolder>
{
	public static class InvalidValueHolder
	{
		public InvalidValueHolder(String intendedValueType, byte[] data)
		{
			this.intendedValueType = intendedValueType;
			this.data = data;
		}
		
		String intendedValueType;
		byte[] data;
		
		public String intendedType()
		{
			return intendedValueType;
		}
		
		public byte[] rawData()
		{
			return data;
		}
	}
	
	public InvalidValue(InvalidValueHolder holder)
	{
		super(holder);
	}
	
	public InvalidValue()
	{
		super();
	}
	
	public InvalidValue(String intendedValueType, byte[] data)
	{
		super(new InvalidValueHolder(intendedValueType, data));
	}
	
	public String intendedValueType()
	{
		return get().intendedValueType;
	}
	
	@EmptyValue
	public InvalidValueHolder emptyValue()
	{
		return new InvalidValueHolder("InvalidValue", new byte[0]);
	}
	
	@Override
	public String toString()
	{
		String data = StringValue.pack(CharacterValue.bulkyConvert(get().data));
		String label =  StringValue.pack(get().intendedValueType);
		return label + ":" + data;
	}
	
	@CharacterConsumer
	public static Consumer<Value<?, InvalidValueHolder>, Character> characterConsumer()
	{
		return (source) ->
		{
			Consumer<Value<?, String>, Character> consumer = StringValue.characterConsumer();
			ConsumerResult<Value<?, String>, Character> result = consumer.consume(source);
			if (!result.success())
			{
				return new ConsumerResult<>(source, "Could not parse intended value type.");
			}
			String intendedValueType = (String)result.value().get();
			if (!StringValue.consumeString(source, ":").success())
			{
				return new ConsumerResult<>(source, "Invalid format.");
			}
			result = consumer.consume(source);
			if (!result.success())
			{
				return new ConsumerResult<>(source, "Could not parse value data.");
			}
			byte[] data = CharacterValue.bulkyConvert((String)result.value().get());
			return new ConsumerResult<>(new InvalidValue(intendedValueType, data), source);
		};
	}
	
	@Override
	public byte[] toByteData()
	{
		InvalidValueHolder holder = get();
		ByteArrayBuilder builder = new ByteArrayBuilder();
		builder.add(holder.intendedValueType);
		builder.add(holder.data.length);
		builder.add(holder.data);
		return builder.toPrimitiveArray();
	}
	
	@ByteConsumer
	public static Consumer<Value<?, InvalidValueHolder>, Byte> byteConsumer()
	{
		return (source) ->
		{
			ConsumerResult<Value<?, String>, Byte> nameResult = StringValue.byteConsumer().consume(source);
			if (!nameResult.success())
				return new ConsumerResult<>(source, "Could not parse name: " + nameResult.description());
			else
			{
				ConsumerResult<Value<?, Integer>, Byte> lengthResult = IntegerValue.byteConsumer().consume(source);
				if (!lengthResult.success())
					return new ConsumerResult<>(source, "Could not determine data length: " + lengthResult.description());
				else
				{
					int length = (Integer)lengthResult.value().get();
					VectorIndexable<Byte> bytes = source.next(length);
					if (bytes == null)
						return new ConsumerResult<>(source, "Not enough data available.");
					else
						return new ConsumerResult<>(new InvalidValue((String)nameResult.value().get(), ByteArrayBuilder.toPrimitive(bytes)), source);
				}
			}
		};
	}
	
	@Override
	public InvalidValue makeDuplicate()
	{
		return new InvalidValue(get().intendedValueType, get().data.clone());
	}
	
	@Override
	public boolean equals(Value<?, ?> value)
	{
		if (value != null && value.getClass().equals(getClass()))
		{
			InvalidValueHolder otherHolder = (InvalidValueHolder)value.get();
			InvalidValueHolder holder = get();
			if (holder.intendedValueType.compareTo(otherHolder.intendedValueType) != 0)
				return false;
			if (holder.data.length != otherHolder.data.length)
				return false;
			
			for (int index = 0; index < holder.data.length; index++)
			{
				if (otherHolder.data[index] != holder.data[index])
					return false;
			}
			
			return true;
		}
		else
			return false;
	}
	
	@ValidationValues
	public static List<InvalidValueHolder> validationValues()
	{
		ArrayList<InvalidValueHolder> list = new ArrayList<InvalidValueHolder>(1);
		list.add(new InvalidValueHolder("TestValue", new byte[] {126, 1, -57, 9, 11, 0}));
		return list;
	}
	
	@ValidationValueObjects
	public static List<Value<?, InvalidValueHolder>> validationValueObjects()
	{
		List<InvalidValueHolder> values = validationValues();
		ArrayList<Value<?, InvalidValueHolder>> objects = new ArrayList<>(values.size());
		values.forEach(value -> objects.add(new InvalidValue(value)));
		return objects;
	}
}