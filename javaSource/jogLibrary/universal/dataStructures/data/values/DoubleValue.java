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

public class DoubleValue extends Value<Double, Double>
{
	public DoubleValue(double value)
	{
		super(value);
	}
	
	public DoubleValue()
	{
		super();
	}
	
	@EmptyValue
	public Double emptyValue()
	{
		return 0.0;
	}
	
	@Override
	public String toString()
	{
		return "" + get();
	}
	
	@CharacterConsumer
	public static Consumer<Value<?, Double>, Character> characterConsumer()
	{
		return (indexer) ->
		{
			ConsumerResult<String, Character> result = StringValue.consumeCharacters(indexer, FloatValue.floatingPointNumericCharacters);
			if (result.success())
			{
				try
				{
					return new ConsumerResult<>(new DoubleValue(Double.parseDouble(result.value())), indexer);
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
	public static Consumer<Value<?, Double>, Byte> byteConsumer()
	{
		return (source) ->
		{
			VectorIndexable<Byte> bytes = source.next(8);
			if (bytes == null)
				return new ConsumerResult<>(source, "Not enough available data.");
			else
				return new ConsumerResult<>(new DoubleValue(fromByteData(ByteArrayBuilder.toPrimitive(bytes))), source);
		};
	}
	
	public static byte[] toByteData(double value)
	{
		ByteBuffer buffer = ByteBuffer.allocate(8);
		buffer.putDouble(value);
		return buffer.array();
	}
	
	public static double fromByteData(byte[] byteData)
	{
		ByteBuffer buffer = ByteBuffer.wrap(byteData);
		return buffer.getDouble();
	}
	
	@Override
	public DoubleValue makeDuplicate()
	{
		return new DoubleValue(get());
	}
	
	@Override
	public boolean equals(Value<?, ?> value)
	{
		return value != null && value.getClass().equals(getClass()) && ((DoubleValue)value).get().equals(get());
	}
	
	@ValidationValues
	public static List<Double> validationValues()
	{
		ArrayList<Double> list = new ArrayList<Double>(4);
		list.add(124.33421);
		list.add(Double.MIN_VALUE);
		list.add(Double.MAX_VALUE);
		list.add(12.0);
		return list;
	}
	
	@ValidationValueObjects
	public static List<Value<?, Double>> validationValueObjects()
	{
		List<Double> values = validationValues();
		ArrayList<Value<?, Double>> objects = new ArrayList<>(values.size());
		values.forEach(value -> objects.add(new DoubleValue(value)));
		return objects;
	}
}