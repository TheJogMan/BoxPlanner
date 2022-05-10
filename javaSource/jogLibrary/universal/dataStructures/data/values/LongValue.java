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

public class LongValue extends Value<Long, Long>
{
	public LongValue(long value)
	{
		super(value);
	}
	
	public LongValue()
	{
		super();
	}
	
	@EmptyValue
	public Long emptyValue()
	{
		return 0L;
	}
	
	@Override
	public String toString()
	{
		return "" + get();
	}
	
	@CharacterConsumer
	public static Consumer<Value<?, Long>, Character> characterConsumer()
	{
		return (indexer) ->
		{
			ConsumerResult<String, Character> result = StringValue.consumeCharacters(indexer, ByteValue.numericCharacters);
			if (result.success())
			{
				try
				{
					return new ConsumerResult<>(new LongValue(Long.parseLong(result.value())), indexer);
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
	public static Consumer<Value<?, Long>, Byte> byteConsumer()
	{
		return (source) ->
		{
			VectorIndexable<Byte> bytes = source.next(8);
			if (bytes == null)
				return new ConsumerResult<>(source, "Not enough available data.");
			else
				return new ConsumerResult<>(new LongValue(fromByteData(ByteArrayBuilder.toPrimitive(bytes))), source);
		};
	}
	
	public static byte[] toByteData(long value)
	{
		ByteBuffer buffer = ByteBuffer.allocate(8);
		buffer.putLong(value);
		return buffer.array();
	}
	
	public static long fromByteData(byte[] byteData)
	{
		ByteBuffer buffer = ByteBuffer.wrap(byteData);
		return buffer.getLong();
	}
	
	@Override
	public LongValue makeDuplicate()
	{
		return new LongValue(get());
	}
	
	@Override
	public boolean equals(Value<?, ?> value)
	{
		return value != null && value.getClass().equals(getClass()) && ((LongValue)value).get().equals(get());
	}
	
	@ValidationValues
	public static List<Long> validationValues()
	{
		ArrayList<Long> list = new ArrayList<Long>(4);
		list.add(168465135843L);
		list.add(Long.MIN_VALUE);
		list.add(Long.MAX_VALUE);
		list.add(12L);
		return list;
	}
	
	@ValidationValueObjects
	public static List<Value<?, Long>> validationValueObjects()
	{
		List<Long> values = validationValues();
		ArrayList<Value<?, Long>> objects = new ArrayList<>(values.size());
		values.forEach(value -> objects.add(new LongValue(value)));
		return objects;
	}
}