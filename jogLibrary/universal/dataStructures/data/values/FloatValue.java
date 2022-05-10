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

public class FloatValue extends Value<Float, Float>
{
	public FloatValue(float value)
	{
		super(value);
	}
	
	public FloatValue()
	{
		super();
	}
	
	@EmptyValue
	public Float emptyValue()
	{
		return 0.0f;
	}
	
	@Override
	public String toString()
	{
		return "" + get();
	}
	
	public static final char[] floatingPointNumericCharacters = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', '0', '.', '-', 'E'};
	
	@CharacterConsumer
	public static Consumer<Value<?, Float>, Character> characterConsumer()
	{
		return (indexer) ->
		{
			ConsumerResult<String, Character> result = StringValue.consumeCharacters(indexer, floatingPointNumericCharacters);
			if (result.success())
			{
				try
				{
					return new ConsumerResult<>(new FloatValue(Float.parseFloat(result.value())), indexer);
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
	public static Consumer<Value<?, Float>, Byte> byteConsumer()
	{
		return (source) ->
		{
			VectorIndexable<Byte> bytes = source.next(4);
			if (bytes == null)
				return new ConsumerResult<>(source, "Not enough available data.");
			else
				return new ConsumerResult<>(new FloatValue(fromByteData(ByteArrayBuilder.toPrimitive(bytes))), source);
		};
	}
	
	public static byte[] toByteData(float value)
	{
		ByteBuffer buffer = ByteBuffer.allocate(4);
		buffer.putFloat(value);
		return buffer.array();
	}
	
	public static float fromByteData(byte[] byteData)
	{
		ByteBuffer buffer = ByteBuffer.wrap(byteData);
		return buffer.getFloat();
	}
	
	@Override
	public FloatValue makeDuplicate()
	{
		return new FloatValue(get());
	}

	@Override
	public boolean equals(Value<?, ?> value)
	{
		return value != null && value.getClass().equals(getClass()) && ((FloatValue)value).get().equals(get());
	}

	@ValidationValues
	public static List<Float> validationValues()
	{
		ArrayList<Float> list = new ArrayList<Float>(4);
		list.add(56.9981f);
		list.add(Float.MIN_VALUE);
		list.add(Float.MAX_VALUE);
		list.add(12f);
		return list;
	}
	
	@ValidationValueObjects
	public static List<Value<?, Float>> validationValueObjects()
	{
		List<Float> values = validationValues();
		ArrayList<Value<?, Float>> objects = new ArrayList<>(values.size());
		values.forEach(value -> objects.add(new FloatValue(value)));
		return objects;
	}
}