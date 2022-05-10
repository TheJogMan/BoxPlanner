package jogLibrary.universal.dataStructures.data.values;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

import jogLibrary.universal.ByteArrayBuilder;
import jogLibrary.universal.dataStructures.data.TypeRegistry.ByteConsumer;
import jogLibrary.universal.dataStructures.data.TypeRegistry.CharacterConsumer;
import jogLibrary.universal.dataStructures.data.TypeRegistry.EmptyValue;
import jogLibrary.universal.dataStructures.data.TypeRegistry.ValidationValueObjects;
import jogLibrary.universal.dataStructures.data.TypeRegistry.ValidationValues;
import jogLibrary.universal.dataStructures.data.Value;
import jogLibrary.universal.indexable.Consumer;
import jogLibrary.universal.indexable.Consumer.ConsumerResult;
import jogLibrary.universal.indexable.Indexer;
import jogLibrary.universal.indexable.VectorIndexable;

public class IntegerValue extends Value<Integer, Integer>
{
	public IntegerValue(int value)
	{
		super(value);
	}
	
	public IntegerValue()
	{
		super();
	}
	
	@EmptyValue
	public Integer emptyValue()
	{
		return 0;
	}
	
	@Override
	public String toString()
	{
		return "" + get();
	}
	
	public static ConsumerResult<Integer, Character> simpleCharacterConsume(Indexer<Character> input)
	{
		ConsumerResult<Value<?, Integer>, Character> result = characterConsumer().consume(input);
		return new ConsumerResult<>((Integer)result.value().get(), result.indexer(), result.success(), result.description());
	}
	
	public static ConsumerResult<Integer, Byte> simpleByteConsume(Indexer<Byte> input)
	{
		ConsumerResult<Value<?, Integer>, Byte> result = byteConsumer().consume(input);
		return new ConsumerResult<>((Integer)result.value().get(), result.indexer(), result.success(), result.description());
	}
	
	@CharacterConsumer
	public static Consumer<Value<?, Integer>, Character> characterConsumer()
	{
		return (indexer) ->
		{
			ConsumerResult<String, Character> result = StringValue.consumeCharacters(indexer, ByteValue.numericCharacters);
			if (result.success())
			{
				try
				{
					return new ConsumerResult<>(new IntegerValue(Integer.parseInt(result.value())), indexer);
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
	public static Consumer<Value<?, Integer>, Byte> byteConsumer()
	{
		return (source) ->
		{
			VectorIndexable<Byte> bytes = source.next(4);
			if (bytes == null)
				return new ConsumerResult<>(source, "Not enough available data.");
			else
				return new ConsumerResult<>(new IntegerValue(fromByteData(ByteArrayBuilder.toPrimitive(bytes))), source);
		};
	}
	
	@Override
	public IntegerValue makeDuplicate()
	{
		return new IntegerValue(get());
	}
	
	public static byte[] toByteData(int value)
	{
		ByteBuffer buffer = ByteBuffer.allocate(4);
		buffer.putInt(value);
		return buffer.array();
	}
	
	public static int fromByteData(byte[] byteData)
	{
		ByteBuffer buffer = ByteBuffer.wrap(byteData);
		return buffer.getInt();
	}
	
	@Override
	public boolean equals(Value<?, ?> value)
	{
		return value != null && value.getClass().equals(getClass()) && ((IntegerValue)value).get().equals(get());
	}
	
	@ValidationValues
	public static List<Integer> validationValues()
	{
		ArrayList<Integer> list = new ArrayList<Integer>(3);
		list.add(128);
		list.add(Integer.MIN_VALUE);
		list.add(Integer.MAX_VALUE);
		return list;
	}
	
	@ValidationValueObjects
	public static List<Value<?, Integer>> validationValueObjects()
	{
		List<Integer> values = validationValues();
		ArrayList<Value<?, Integer>> objects = new ArrayList<>(values.size());
		values.forEach(value -> objects.add(new IntegerValue(value)));
		return objects;
	}
}