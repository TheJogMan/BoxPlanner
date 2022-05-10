package jogLibrary.universal.dataStructures.data.values;

import java.util.ArrayList;
import java.util.List;

import jogLibrary.universal.dataStructures.data.TypeRegistry.ByteConsumer;
import jogLibrary.universal.dataStructures.data.TypeRegistry.CharacterConsumer;
import jogLibrary.universal.dataStructures.data.TypeRegistry.EmptyValue;
import jogLibrary.universal.dataStructures.data.TypeRegistry.ValidationValueObjects;
import jogLibrary.universal.dataStructures.data.TypeRegistry.ValidationValues;
import jogLibrary.universal.dataStructures.data.Value;
import jogLibrary.universal.indexable.Consumer;
import jogLibrary.universal.indexable.Consumer.ConsumerResult;

public class ByteValue extends Value<Byte, Byte>
{
	public ByteValue(byte value)
	{
		super(value);
	}
	
	public ByteValue()
	{
		super();
	}
	
	@EmptyValue
	public Byte emptyValue()
	{
		return 0;
	}
	
	@Override
	public String toString()
	{
		return "" + get();
	}
	
	@Override
	public byte[] toByteData()
	{
		return new byte[] {get()};
	}
	
	@Override
	public ByteValue makeDuplicate()
	{
		return new ByteValue(get());
	}
	
	public static final char[] numericCharacters = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', '-'};
	
	@ByteConsumer
	public static Consumer<Value<?, Byte>, Byte> byteConsumer()
	{
		return (indexer) ->
		{
			if (indexer.atEnd())
				return new ConsumerResult<>(indexer, "No available data.");
			else
				return new ConsumerResult<>(new ByteValue(indexer.next()), indexer);
		};
	}
	
	@CharacterConsumer
	public static Consumer<Value<?, Byte>, Character> characterConsumer()
	{
		return (indexer) ->
		{
			ConsumerResult<String, Character> result = StringValue.consumeCharacters(indexer, ByteValue.numericCharacters);
			if (result.success())
			{
				try
				{
					return new ConsumerResult<>(new ByteValue(Byte.parseByte(result.value())), indexer);
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
	public boolean equals(Value<?, ?> value)
	{
		return value != null && value.getClass().equals(getClass()) && value.get() == get();
	}
	
	@ValidationValues
	public static List<Byte> validationValues()
	{
		ArrayList<Byte> list = new ArrayList<Byte>(3);
		list.add((byte)-127);
		list.add((byte)127);
		list.add((byte)0);
		return list;
	}
	
	@ValidationValueObjects
	public static List<Value<?, Byte>> validationValueObjects()
	{
		List<Byte> values = validationValues();
		ArrayList<Value<?, Byte>> objects = new ArrayList<>(values.size());
		values.forEach(value -> objects.add(new ByteValue(value)));
		return objects;
	}
	
	/**
	 * Toggles a bit within a byte
	 * @param value the byte to work with
	 * @param index the index of the bit to toggle (0-7)
	 * @return the byte with the bit toggled
	 */
	public static byte toggleBit(byte value, int index)
	{
		if (index >= 0 && index <= 7)
		{
			return setBit(value, index, !getBit(value, index));
		}
		else
		{
			throw new IllegalStateException("BYTE CONVERTER - toggleBit - Index not in valid range: Expected 0-7, got " + index);
		}
	}
	
	/**
	 * Toggles a bit within a byte
	 * @param value the byte to work with
	 * @param index the index of the bit to set (0-7)
	 * @param state the new state of the bit
	 * @return the byte with the bit set
	 */
	public static byte setBit(byte value, int index, boolean state)
	{
		if (index >= 0 && index <= 7)
		{
			if (index == 0)
			{
				value = (byte)((value >>> 1) << 1);
				if (state)
				{
					value = (byte)(value | 1);
				}
			}
			else if (index == 7)
			{
				byte tempValue = (byte)(value >>> 7);
				value = (byte)(value - tempValue);
				if (state)
				{
					value = (byte)(value | (1 << 7));
				}
			}
			else
			{
				byte tempVal1 = (byte)((value >>> index + 1) << index + 1);
				byte tempVal2 = (byte)((value >>> index) << index);
				value = (byte)(tempVal1 | tempVal2);
				if (state)
				{
					value = (byte)(value | (1 << index - 1));
				}
			}
			return value;
		}
		else
		{
			throw new IllegalStateException("BYTE CONVERTER - setBit - Index not in valid range: Expected 0-7, got " + index);
		}
	}
	
	/**
	 * Toggles a bit within a byte
	 * @param value the byte to work with
	 * @param index the index of the bit to get (0-7)
	 * @return the state of the bit
	 */
	public static boolean getBit(byte value, int index)
	{
		if (index >= 0 && index <= 7)
		{
			if (index == 0)
			{
				byte tempVal = (byte)(value >>> 1);
				value -= (tempVal << 1);
			}
			else if (index == 7)
			{
				value = (byte)(value >>> 7);
			}
			else
			{
				byte tempVal1 = (byte)((value >>> index + 1) << index + 1);
				byte tempVal2 = (byte)((value >>> index) << index);
				value = (byte) (value - tempVal1);
				value = (byte) (value - tempVal2);
				value = (byte)(value >>> index - 1);
			}
			return (value == 1 ? true : false);
		}
		else
		{
			throw new IllegalStateException("BYTE CONVERTER - getBit - Index not in valid range: Expected 0-7, got " + index);
		}
	}
	
	public static byte buildByte(boolean bit0, boolean bit1, boolean bit2, boolean bit3, boolean bit4, boolean bit5, boolean bit6, boolean bit7)
	{
		byte byt = 0;
		byt = setBit(byt, 0, bit0);
		byt = setBit(byt, 1, bit1);
		byt = setBit(byt, 2, bit2);
		byt = setBit(byt, 3, bit3);
		byt = setBit(byt, 4, bit4);
		byt = setBit(byt, 5, bit5);
		byt = setBit(byt, 6, bit6);
		byt = setBit(byt, 7, bit7);
		return byt;
	}
}