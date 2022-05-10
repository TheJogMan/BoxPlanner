package jogLibrary.universal.dataStructures.data.values;

import java.util.ArrayList;
import java.util.List;

import jogLibrary.universal.ByteArrayBuilder;
import jogLibrary.universal.dataStructures.data.TypeRegistry.ByteConsumer;
import jogLibrary.universal.dataStructures.data.TypeRegistry.CharacterConsumer;
import jogLibrary.universal.dataStructures.data.TypeRegistry.EmptyValue;
import jogLibrary.universal.dataStructures.data.TypeRegistry.ValidationValueObjects;
import jogLibrary.universal.dataStructures.data.TypeRegistry.ValidationValues;
import jogLibrary.universal.dataStructures.data.Value;
import jogLibrary.universal.dataStructures.vector.Vector3Double;
import jogLibrary.universal.indexable.Consumer;
import jogLibrary.universal.indexable.Consumer.ConsumerResult;
import jogLibrary.universal.indexable.Indexer.ExclusionFilter;
import jogLibrary.universal.richString.RichStringBuilder;

public class Vector3DoubleValue extends Value<Vector3Double, Vector3Double>
{
	public Vector3DoubleValue()
	{
		super();
	}
	
	public Vector3DoubleValue(Vector3Double vector)
	{
		super(vector);
	}
	
	@Override
	public String toString()
	{
		StringBuilder builder = new StringBuilder();
		Vector3Double vector = get();
		builder.append("{X: " + (new DoubleValue(vector.x())).toString() + ",");
		builder.append("Y: " + (new DoubleValue(vector.y())).toString() + ",");
		builder.append("Z: " + (new DoubleValue(vector.z())).toString() + "}");
		return builder.toString();
	}
	
	@Override
	public byte[] toByteData()
	{
		Vector3Double vector = get();
		ByteArrayBuilder builder = new ByteArrayBuilder();
		builder.add((new DoubleValue(vector.x())).toByteData());
		builder.add((new DoubleValue(vector.y())).toByteData());
		builder.add((new DoubleValue(vector.z())).toByteData());
		return builder.toPrimitiveArray();
	}

	@Override
	public Value<Vector3Double, Vector3Double> makeDuplicate()
	{
		return new Vector3DoubleValue(get().clone());
	}

	@Override
	public boolean equals(Value<?, ?> value)
	{
		if (value == null || !(value instanceof Vector3DoubleValue))
			return false;
		else
			return get().equals(((Vector3DoubleValue)value).get());
	}
	
	@EmptyValue
	public Vector3Double emptyValue()
	{
		return new Vector3Double(0.0, 0.0, 0.0);
	}
	
	@CharacterConsumer
	public static Consumer<Value<?, Vector3Double>, Character> characterConsumer()
	{
		return ((source) ->
		{
			source.pushFilterState();
			source.addFilter(new ExclusionFilter<>(CharacterValue.toObjectArray(StringValue.formattingCharacters)));
			if (!StringValue.consumeString(source, "{X: ").success())
				return new ConsumerResult<>(source.popFilterState(), "Invalid format. X coordinate must begin with 'X: '");
			ConsumerResult<Value<?, Double>, Character> xCoordinate = DoubleValue.characterConsumer().consume(source);
			if (!xCoordinate.success())
				return new ConsumerResult<>(source.popFilterState(), RichStringBuilder.start().append("Could not parse X Coordinate: ").append(xCoordinate.description()).build());
			if (source.next() != ',')
				return new ConsumerResult<>(source.popFilterState(), "Invalid format, expected ',' after x coordinate.");
			
			if (!StringValue.consumeString(source, "Y: ").success())
				return new ConsumerResult<>(source.popFilterState(), "Invalid format. Y coordinate must begin with 'Y: '");
			ConsumerResult<Value<?, Double>, Character> yCoordinate = DoubleValue.characterConsumer().consume(source);
			if (!yCoordinate.success())
				return new ConsumerResult<>(source.popFilterState(), RichStringBuilder.start().append("Could not parse Y Coordinate: ").append(yCoordinate.description()).build());
			if (source.next() != ',')
				return new ConsumerResult<>(source.popFilterState(), "Invalid format, expected ',' after y coordinate.");
			
			if (!StringValue.consumeString(source, "Z: ").success())
				return new ConsumerResult<>(source.popFilterState(), "Invalid format. Z coordinate must begin with 'Z: '");
			ConsumerResult<Value<?, Double>, Character> zCoordinate = DoubleValue.characterConsumer().consume(source);
			if (!zCoordinate.success())
				return new ConsumerResult<>(source.popFilterState(), RichStringBuilder.start().append("Could not parse Z Coordinate: ").append(zCoordinate.description()).build());
			if (source.next() != '}')
				return new ConsumerResult<>(source.popFilterState(), "Invalid format, expected '}' after z coordinate.");
			
			return new ConsumerResult<>(new Vector3DoubleValue(new Vector3Double((double)xCoordinate.value().get(), (double)yCoordinate.value().get(), (double)zCoordinate.value().get())), source.popFilterState());
		});
	}
	
	@ByteConsumer
	public static Consumer<Value<?, Vector3Double>, Byte> byteConsumer()
	{
		return ((source) ->
		{
			ConsumerResult<Value<?, Double>, Byte> xCoordinate = DoubleValue.byteConsumer().consume(source);
			if (!xCoordinate.success())
				return new ConsumerResult<>(source, RichStringBuilder.start().append("Could not parse x coordinate: ").append(xCoordinate.description()).build());
			
			ConsumerResult<Value<?, Double>, Byte> yCoordinate = DoubleValue.byteConsumer().consume(source);
			if (!yCoordinate.success())
				return new ConsumerResult<>(source, RichStringBuilder.start().append("Could not parse y coordinate: ").append(yCoordinate.description()).build());
			
			ConsumerResult<Value<?, Double>, Byte> zCoordinate = DoubleValue.byteConsumer().consume(source);
			if (!zCoordinate.success())
				return new ConsumerResult<>(source, RichStringBuilder.start().append("Could not parse z coordinate: ").append(zCoordinate.description()).build());
			
			return new ConsumerResult<>(new Vector3DoubleValue(new Vector3Double((double)xCoordinate.value().get(), (double)yCoordinate.value().get(), (double)zCoordinate.value().get())), source);
		});
	}
	
	@ValidationValues
	public static List<Vector3Double> validationValues()
	{
		ArrayList<Vector3Double> list = new ArrayList<Vector3Double>(1);
		list.add(new Vector3Double(12.0, 34.7, 90.0));
		return list;
	}
	
	@ValidationValueObjects
	public static List<Value<?, Vector3Double>> validationValueObjects()
	{
		List<Vector3Double> values = validationValues();
		ArrayList<Value<?, Vector3Double>> objects = new ArrayList<>(values.size());
		values.forEach(value -> objects.add(new Vector3DoubleValue(value)));
		return objects;
	}
}