package jogLibrary.universal.dataStructures.data.values;

import java.util.ArrayList;
import java.util.List;

import jogLibrary.universal.dataStructures.data.Value;
import jogLibrary.universal.dataStructures.data.TypeRegistry.ByteConsumer;
import jogLibrary.universal.dataStructures.data.TypeRegistry.CharacterConsumer;
import jogLibrary.universal.dataStructures.data.TypeRegistry.EmptyValue;
import jogLibrary.universal.dataStructures.data.TypeRegistry.ValidationValueObjects;
import jogLibrary.universal.dataStructures.data.TypeRegistry.ValidationValues;
import jogLibrary.universal.indexable.Consumer;
import jogLibrary.universal.indexable.Consumer.ConsumerResult;

public class BooleanValue extends Value<Boolean, Boolean>
{
	public BooleanValue(boolean value)
	{
		super(value);
	}
	
	public BooleanValue()
	{
		super();
	}
	
	@EmptyValue
	public Boolean emptyValue()
	{
		return false;
	}
	
	@Override
	public String toString()
	{
		return get() ? "True" : "False";
	}
	
	@CharacterConsumer
	public static Consumer<Value<?, Boolean>, Character> characterConsumer()
	{
		return (source) ->
		{
			char ch = source.get();
			if (Character.toLowerCase(ch) == 't')
			{
				ConsumerResult<String, Character> result = StringValue.consumeString(source, "true", false);
				if (result.success())
					return new ConsumerResult<>(new BooleanValue(true), source);
				else
					return new ConsumerResult<>(source, "Invalid format.");
			}
			else
			{
				ConsumerResult<String, Character>result = StringValue.consumeString(source, "false", false);
				if (result.success())
					return new ConsumerResult<>(new BooleanValue(false), source);
				else
					return new ConsumerResult<>(source, "Invalid format.");
			}
		};
	}
	
	@Override
	public byte[] toByteData()
	{
		return toByteData(get());
	}
	
	@ByteConsumer
	public static Consumer<Value<?, Boolean>, Byte> byteConsumer()
	{
		return (source) ->
		{
			if (source.atEnd())
				return new ConsumerResult<>(source, "No available data.");
			else
				return new ConsumerResult<>(new BooleanValue(source.next() == 1), source);
		};
	}
	
	public static byte[] toByteData(boolean value)
	{
		return new byte[] {(byte)(value ? 1 : 0)};
	}
	
	@Override
	public BooleanValue makeDuplicate()
	{
		return new BooleanValue(get());
	}

	@Override
	public boolean equals(Value<?, ?> value)
	{
		return value != null && value.getClass().equals(getClass()) && value.get() == get();
	}
	
	@ValidationValues
	public static List<Boolean> validationValues()
	{
		ArrayList<Boolean> list = new ArrayList<Boolean>(2);
		list.add(true);
		list.add(false);
		return list;
	}
	
	@ValidationValueObjects
	public static List<Value<?, Boolean>> validationValueObjects()
	{
		List<Boolean> values = validationValues();
		ArrayList<Value<?, Boolean>> objects = new ArrayList<>(values.size());
		values.forEach(value -> objects.add(new BooleanValue(value)));
		return objects;
	}
}