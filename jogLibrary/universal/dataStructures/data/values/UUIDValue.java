package jogLibrary.universal.dataStructures.data.values;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

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

public class UUIDValue extends Value<UUID, UUID>
{
	public UUIDValue(UUID id)
	{
		super(id);
	}
	
	public UUIDValue()
	{
		super();
	}
	
	@EmptyValue
	public UUID emptyValue()
	{
		return UUID.randomUUID();
	}
	
	@Override
	public UUIDValue makeDuplicate()
	{
		return new UUIDValue(UUID.fromString(get().toString()));
	}
	
	@Override
	public String toString()
	{
		return get().toString();
	}
	
	@CharacterConsumer
	public static Consumer<Value<?, UUID>, Character> characterConsumer()
	{
		return (source) ->
		{
			ConsumerResult<String, Character> result = StringValue.consumeString(source, 36);
			if (result.success())
			{
				try
				{
					return new ConsumerResult<>(new UUIDValue(UUID.fromString(result.value())), source);
				}
				catch (IllegalArgumentException e)
				{
					return new ConsumerResult<>(source, "Invalid UUID format.");
				}
			}
			else
				return new ConsumerResult<>(source, "Invalid UUID format.");
		};
	}
	
	@Override
	public byte[] toByteData()
	{
		return toByteData(get());
	}
	
	@ByteConsumer
	public static Consumer<Value<?, UUID>, Byte> byteConsumer()
	{
		return (source) ->
		{
			VectorIndexable<Byte> bytes = source.next(16);
			if (bytes == null)
				return new ConsumerResult<>(source, "Not enough available data.");
			else
				return new ConsumerResult<>(new UUIDValue(fromByteData(ByteArrayBuilder.toPrimitive(bytes))), source);
		};
	}
	
	public static byte[] toByteData(UUID id)
	{
		byte[] most = LongValue.toByteData(id.getMostSignificantBits());
		byte[] least = LongValue.toByteData(id.getLeastSignificantBits());
		byte[] data = new byte[16];
		for (int index = 0; index < 8; index++)
		{
			data[index] = most[index];
			data[index + 8] = least[index];
		}
		return data;
	}
	
	public static UUID fromByteData(byte[] byteData)
	{
		byte[] most = new byte[8];
		byte[] least = new byte[8];
		for (int index = 0; index < 8; index++)
		{
			most[index] = byteData[index];
			least[index] = byteData[index + 8];
		}
		return new UUID(LongValue.fromByteData(most), LongValue.fromByteData(least));
	}
	
	@Override
	public boolean equals(Value<?, ?> value)
	{
		return value != null && value.getClass().equals(getClass()) && ((value.get() == null && get() == null) || ((UUIDValue)value).get().equals(get()));
	}
	
	@ValidationValues
	public static List<UUID> validationValues()
	{
		ArrayList<UUID> list = new ArrayList<UUID>(3);
		list.add(UUID.fromString("4cd96274-43a8-474e-8658-a1ccd24d67ac"));
		list.add(UUID.fromString("e7cf7766-1a77-474f-8a99-d0d7d9e9a711"));
		list.add(UUID.fromString("f8331f26-1fef-40e2-8099-ac2b7fb62505"));
		return list;
	}
	
	@ValidationValueObjects
	public static List<Value<?, UUID>> validationValueObjects()
	{
		List<UUID> values = validationValues();
		ArrayList<Value<?, UUID>> objects = new ArrayList<>(values.size());
		values.forEach(value -> objects.add(new UUIDValue(value)));
		return objects;
	}
}