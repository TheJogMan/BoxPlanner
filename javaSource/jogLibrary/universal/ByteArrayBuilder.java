package jogLibrary.universal;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import jogLibrary.universal.dataStructures.data.Value;
import jogLibrary.universal.dataStructures.data.values.BooleanValue;
import jogLibrary.universal.dataStructures.data.values.CharacterValue;
import jogLibrary.universal.dataStructures.data.values.IntegerValue;
import jogLibrary.universal.dataStructures.data.values.LongValue;
import jogLibrary.universal.dataStructures.data.values.ShortValue;
import jogLibrary.universal.dataStructures.data.values.StringValue;
import jogLibrary.universal.indexable.ArrayIndexable;
import jogLibrary.universal.indexable.HoardingIndexer;

public class ByteArrayBuilder
{
	ArrayList<Byte> data = new ArrayList<>();
	
	public void add(byte value)
	{
		data.add(value);
	}
	
	public void add(Byte value)
	{
		data.add(value);
	}
	
	public void add(byte[] data)
	{
		for (int index = 0; index < data.length; index++)
			this.data.add(data[index]);
	}
	
	public void add(Byte[] data)
	{
		for (int index = 0; index < data.length; index++)
			this.data.add(data[index]);
	}
	
	public void add(int value)
	{
		add(IntegerValue.toByteData(value));
	}
	
	public void add(long value)
	{
		add(LongValue.toByteData(value));
	}
	
	public void add(short value)
	{
		add(ShortValue.toByteData(value));
	}
	
	public void add(boolean value)
	{
		add(BooleanValue.toByteData(value));
	}
	
	public void add(Value<?, ?> value)
	{
		add(value.toByteData());
	}
	
	public void add(String value)
	{
		add(StringValue.toByteData(value));
	}
	
	public void add(char value)
	{
		add(CharacterValue.toByteData(value));
	}
	
	public byte[] toPrimitiveArray()
	{
		byte[] data = new byte[this.data.size()];
		for (int index = 0; index < data.length; index++)
			data[index] = this.data.get(index);
		return data;
	}
	
	public Byte[] toObjectArray()
	{
		return data.toArray(new Byte[data.size()]);
	}
	
	public static byte[] toPrimitive(Collection<Byte> bytes)
	{
		byte[] data = new byte[bytes.size()];
		int index = 0;
		for (Iterator<Byte> iterator = bytes.iterator(); iterator.hasNext();)
		{
			data[index] = iterator.next();
			index++;
		}
		return data;
	}
	
	public static byte[] toPrimitive(Byte[] bytes)
	{
		byte[] data = new byte[bytes.length];
		for (int index = 0; index < data.length; index++)
			data[index] = bytes[index];
		return data;
	}
	
	public static Byte[] toObject(byte[] bytes)
	{
		Byte[] data = new Byte[bytes.length];
		for (int index = 0; index < data.length; index++)
			data[index] = bytes[index];
		return data;
	}
	
	public static HoardingIndexer<Byte> indexer(Byte[] data)
	{
		return (new ArrayIndexable<Byte>(data)).iterator();
	}
	
	public static HoardingIndexer<Byte> indexer(byte[] data)
	{
		return (new ArrayIndexable<Byte>(toObject(data))).iterator();
	}
}