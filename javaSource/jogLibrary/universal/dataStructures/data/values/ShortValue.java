package jogLibrary.universal.dataStructures.data.values;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

import jogLibrary.universal.ByteArrayBuilder;
import jogLibrary.universal.dataStructures.data.Value;
import jogLibrary.universal.dataStructures.data.TypeRegistry.ByteConsumer;
import jogLibrary.universal.dataStructures.data.TypeRegistry.CharacterConsumer;
import jogLibrary.universal.dataStructures.data.TypeRegistry.EmptyValue;
import jogLibrary.universal.dataStructures.data.TypeRegistry.ValidationValueObjects;
import jogLibrary.universal.dataStructures.data.TypeRegistry.ValidationValues;
import jogLibrary.universal.indexable.Consumer;
import jogLibrary.universal.indexable.Consumer.ConsumerResult;
import jogLibrary.universal.indexable.VectorIndexable;

public class ShortValue extends Value<Short, Short>
{
	public ShortValue(short value)
	{
		super(value);
	}
	
	public ShortValue()
	{
		super();
	}
	
	@EmptyValue
	public Short emptyValue()
	{
		return 0;
	}
	
	@Override
	public String toString()
	{
		return "" + get();
	}
	
	@CharacterConsumer
	public static Consumer<Value<?, Short>, Character> characterConsumer()
	{
		return (indexer) ->
		{
			ConsumerResult<String, Character> result = StringValue.consumeCharacters(indexer, ByteValue.numericCharacters);
			if (result.success())
			{
				try
				{
					return new ConsumerResult<>(new ShortValue(Short.parseShort(result.value())), indexer);
				}
				catch (Exception e)
				{
					return new ConsumerResult<>(indexer, "Invalid number format");
				}
			}
			else
				return new ConsumerResult<>(indexer, "Could not parse number");
		};
	}
	
	@Override
	public byte[] toByteData()
	{
		return toByteData(get());
	}
	
	@ByteConsumer
	public static Consumer<Value<?, Short>, Byte> byteConsumer()
	{
		return (source) ->
		{
			VectorIndexable<Byte> bytes = source.next(2);
			if (bytes == null)
				return new ConsumerResult<>(source, "Not enough available data.");
			else
				return new ConsumerResult<>(new ShortValue(fromByteData(ByteArrayBuilder.toPrimitive(bytes))), source);
		};
	}
	
	public static byte[] toByteData(short value)
	{
		ByteBuffer buffer = ByteBuffer.allocate(2);
		buffer.putShort(value);
		return buffer.array();
	}
	
	public static short fromByteData(byte[] byteData)
	{
		ByteBuffer buffer = ByteBuffer.wrap(byteData);
		return buffer.getShort();
	}
	
	@Override
	public ShortValue makeDuplicate()
	{
		return new ShortValue(get());
	}
	
	@Override
	public boolean equals(Value<?, ?> value)
	{
		return value != null && value.getClass().equals(getClass()) && ((ShortValue)value).get().equals(get());
	}
	
	@ValidationValues
	public static List<Short> validationValues()
	{
		ArrayList<Short> list = new ArrayList<Short>(4);
		list.add((short)1684);
		list.add(Short.MIN_VALUE);
		list.add(Short.MAX_VALUE);
		list.add((short)12);
		return list;
	}
	
	@ValidationValueObjects
	public static List<Value<?, Short>> validationValueObjects()
	{
		List<Short> values = validationValues();
		ArrayList<Value<?, Short>> objects = new ArrayList<>(values.size());
		values.forEach(value -> objects.add(new ShortValue(value)));
		return objects;
	}
}