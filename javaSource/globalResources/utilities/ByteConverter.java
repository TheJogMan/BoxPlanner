package globalResources.utilities;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;

/**
 * Utilities for working with bytes
 */
public class ByteConverter
{
	/**
	 * Converts a byte array to an array of UUIDs
	 * @param data byte data
	 * @return UUID array
	 */
	public static UUID[] toUUIDArray(byte[] data)
	{
		byte[][] array = to2DByteArray(data);
		UUID[] newArray = new UUID[array.length];
		for (int index = 0; index < array.length; index++)
		{
			newArray[index] = toUUID(array[index]);
		}
		return newArray;
	}
	
	/**
	 * Converts a UUID array to an array of byte data
	 * @param array UUID array
	 * @return byte data
	 */
	public static byte[] fromUUIDArray(UUID[] array)
	{
		byte[][] newArray = new byte[array.length][];
		for (int index = 0; index < array.length; index++)
		{
			newArray[index] = fromUUID(array[index]);
		}
		return from2DByteArray(newArray);
	}
	
	/**
	 * Converts an array of byte data to an array of booleans
	 * @param data byte data
	 * @return boolean array
	 */
	public static boolean[] toBooleanArray(byte[] data)
	{
		boolean[] array = new boolean[data.length * 8];
		int arrayIndex = 0;
		for (int index = 0; index < data.length; index++)
		{
			array[arrayIndex] = getBit(data[index], 0);
			array[arrayIndex + 1] = getBit(data[index], 1);
			array[arrayIndex + 2] = getBit(data[index], 2);
			array[arrayIndex + 3] = getBit(data[index], 3);
			array[arrayIndex + 4] = getBit(data[index], 4);
			array[arrayIndex + 5] = getBit(data[index], 5);
			array[arrayIndex + 6] = getBit(data[index], 6);
			array[arrayIndex + 7] = getBit(data[index], 7);
			arrayIndex += 8;
		}
		return array;
	}
	
	/**
	 * Converts an array of booleans into an array of byte data
	 * @param array boolean array
	 * @return byte data
	 * <p>
	 * The size of the boolean array must be a multiple of 8
	 * </p>
	 */
	public static byte[] fromBooleanArray(boolean[] array)
	{
		if (array.length % 8 == 0)
		{
			byte[] data = new byte[array.length / 8];
			int arrayIndex = 0;
			for (int index = 0; index < data.length; index++)
			{
				byte value = 0;
				value = setBit(value, 0, array[arrayIndex]);
				value = setBit(value, 1, array[arrayIndex + 1]);
				value = setBit(value, 2, array[arrayIndex + 2]);
				value = setBit(value, 3, array[arrayIndex + 3]);
				value = setBit(value, 4, array[arrayIndex + 4]);
				value = setBit(value, 5, array[arrayIndex + 5]);
				value = setBit(value, 6, array[arrayIndex + 6]);
				value = setBit(value, 7, array[arrayIndex + 7]);
				data[index] = value;
				arrayIndex += 8;
			}
			return data;
		}
		else
		{
			throw new IllegalStateException("BYTE CONVERTER - fromBooleanArray - Array length must be multiple of 8, array length is " + array.length);
		}
	}
	
	/**
	 * Converts a byte array to an array of doubles
	 * @param data byte data
	 * @return double array
	 */
	public static double[] toDoubleArray(byte[] data)
	{
		byte[][] array = to2DByteArray(data);
		double[] newArray = new double[array.length];
		for (int index = 0; index < array.length; index++)
		{
			newArray[index] = ByteConverter.toDouble(array[index]);
		}
		return newArray;
	}
	
	/**
	 * Converts a double array to an array of byte data
	 * @param array double array
	 * @return byte data
	 */
	public static byte[] fromDoubleArray(double[] array)
	{
		byte[][] newArray = new byte[array.length][];
		for (int index = 0; index < array.length; index++)
		{
			newArray[index] = ByteConverter.fromDouble(array[index]);
		}
		return from2DByteArray(newArray);
	}
	
	/**
	 * Converts a byte array to an array of floats
	 * @param data byte data
	 * @return float array
	 */
	public static float[] toFloatArray(byte[] data)
	{
		byte[][] array = to2DByteArray(data);
		float[] newArray = new float[array.length];
		for (int index = 0; index < array.length; index++)
		{
			newArray[index] = ByteConverter.toFloat(array[index]);
		}
		return newArray;
	}
	
	/**
	 * Converts a float array to an array of byte data
	 * @param array float array
	 * @return byte data
	 */
	public static byte[] fromFloatArray(float[] array)
	{
		byte[][] newArray = new byte[array.length][];
		for (int index = 0; index < array.length; index++)
		{
			newArray[index] = ByteConverter.fromFloat(array[index]);
		}
		return from2DByteArray(newArray);
	}
	
	/**
	 * Converts a byte array to an array of longs
	 * @param data byte data
	 * @return long array
	 */
	public static long[] toLongArray(byte[] data)
	{
		byte[][] array = to2DByteArray(data);
		long[] newArray = new long[array.length];
		for (int index = 0; index < array.length; index++)
		{
			newArray[index] = ByteConverter.toLong(array[index]);
		}
		return newArray;
	}
	
	/**
	 * Converts a long array to an array of byte data
	 * @param array long array
	 * @return byte data
	 */
	public static byte[] fromLongArray(long[] array)
	{
		byte[][] newArray = new byte[array.length][];
		for (int index = 0; index < array.length; index++)
		{
			newArray[index] = ByteConverter.fromLong(array[index]);
		}
		return from2DByteArray(newArray);
	}
	
	/**
	 * Converts an array of byte data into an array of integers
	 * @param data byte data
	 * @return integer array
	 */
	public static int[] toIntegerArray(byte[] data)
	{
		byte[][] array = to2DByteArray(data);
		int[] newArray = new int[array.length];
		for (int index = 0; index < array.length; index++)
		{
			newArray[index] = ByteConverter.toInteger(array[index]);
		}
		return newArray;
	}
	
	/**
	 * Converts an integer array to an array of byte data
	 * @param array integer array
	 * @return byte data
	 */
	public static byte[] fromIntegerArray(int[] array)
	{
		byte[][] newArray = new byte[array.length][];
		for (int index = 0; index < array.length; index++)
		{
			newArray[index] = ByteConverter.fromInteger(array[index]);
		}
		return from2DByteArray(newArray);
	}
	
	/**
	 * Converts a string array to an array of UUIDs
	 * @param data string data
	 * @return UUID array
	 */
	public static String[] toStringArray(byte[] data)
	{
		byte[][] array = to2DByteArray(data);
		String[] newArray = new String[array.length];
		for (int index = 0; index < array.length; index++)
		{
			newArray[index] = ByteConverter.toString(array[index]);
		}
		return newArray;
	}
	
	/**
	 * Converts a string array to an array of byte data
	 * @param array string array
	 * @return byte data
	 */
	public static byte[] fromStringArray(String[] array)
	{
		byte[][] newArray = new byte[array.length][];
		for (int index = 0; index < array.length; index++)
		{
			newArray[index] = ByteConverter.fromString(array[index]);
		}
		return from2DByteArray(newArray);
	}
	
	/**
	 * Converts a one dimensional byte array into a 3 dimensional byte array
	 * @param data 1 dimensional byte array
	 * @return 3 dimensional byte array
	 */
	public static byte[][][] to3DByteArray(byte[] data)
	{
		byte[][] array = to2DByteArray(data);
		byte[][][] newArray = new byte[array.length][][];
		for (int index = 0; index < array.length; index++)
		{
			newArray[index] = to2DByteArray(array[index]);
		}
		return newArray;
	}
	
	/**
	 * Converts a three dimensional byte array to a one dimensional byte array
	 * @param array 3 dimensional byte array
	 * @return one dimensional byte array
	 */
	public static byte[] from3DByteArray(byte[][][] array)
	{
		byte[][] newArray = new byte[array.length][];
		for (int index = 0; index < array.length; index++)
		{
			newArray[index] = from2DByteArray(array[index]);
		}
		return from2DByteArray(newArray);
	}
	
	/**
	 * Converts a one dimensional byte array to a two dimensional byte array
	 * @param data one dimensional byte array
	 * @return two dimensional byte array
	 */
	public static byte[][] to2DByteArray(byte[] data)
	{
		if (data.length > 0)
		{
			if (data[0] == 0)
			{
				byte[][] array = new byte[1][data.length - 1];
				for (int index = 0; index < data.length - 1; index++)
				{
					array[0][index] = data[index + 1];
				}
				return array;
			}
			else
			{
				byte[][] array;
				boolean uniformLength = false;;
				int amount = 0;
				int dataIndex = 0;
				if (data[0] == 1)
				{
					uniformLength = true;
				}
				else if (data[0] == 2)
				{
					uniformLength = false;
				}
				if (data[1] == Byte.MAX_VALUE)
				{
					amount = convertUp(data[2]);
					dataIndex = 3;
				}
				else if (data[1] == Byte.MIN_VALUE)
				{
					amount = toInteger(new byte[] {data[2], data[3], data[4], data[5]});
					dataIndex = 6;
				}
				
				if (uniformLength)
				{
					int length = 0;
					if (data[dataIndex] == Byte.MAX_VALUE)
					{
						length = convertUp(data[dataIndex + 1]);
						dataIndex += 2;
					}
					else if (data[dataIndex] == Byte.MIN_VALUE)
					{
						length = toInteger(new byte[] {data[dataIndex + 1], data[dataIndex + 2], data[dataIndex + 3], data[dataIndex + 4]});
						dataIndex += 5;
					}
					array = new byte[amount][length];
					for (int index = 0; index < array.length; index++)
					{
						for (int subIndex = 0; subIndex < length; subIndex++)
						{
							array[index][subIndex] = data[dataIndex + subIndex];
						}
						dataIndex += length;
					}
				}
				else
				{
					array = new byte[amount][];
					for (int index = 0; index < array.length; index++)
					{
						int length = 0;
						if (data[dataIndex] == Byte.MAX_VALUE)
						{
							length = convertUp(data[dataIndex + 1]);
							dataIndex += 2;
						}
						else if (data[dataIndex] == Byte.MIN_VALUE)
						{
							length = toInteger(new byte[] {data[dataIndex + 1], data[dataIndex + 2], data[dataIndex + 3], data[dataIndex + 4]});
							dataIndex += 5;
						}
						array[index] = new byte[length];
						for (int subIndex = 0; subIndex < length; subIndex++)
						{
							array[index][subIndex] = data[dataIndex + subIndex];
						}
						dataIndex += length;
					}
				}
				
				return array;
			}
		}
		return new byte[0][];
	}
	
	/**
	 * Converts a two dimensional byte array to a one dimensional byte array
	 * @param array two dimensional byte array
	 * @return one dimensional byte array
	 */
	public static byte[] from2DByteArray(byte[][] array)
	{
		if (array.length == 0)
		{
			return new byte[0];
		}
		else if (array.length == 1)
		{
			byte[] data = new byte[array[0].length + 1];
			data[0] = 0;
			for (int index = 0; index < array[0].length; index++)
			{
				data[index + 1] = array[0][index];
			}
			return data;
		}
		else
		{
			boolean uniformLength = true;
			byte[] arrayData;
			int firstLength = array[0].length;
			for (int index = 1; index < array.length; index++)
			{
				if (array[index].length != firstLength)
				{
					uniformLength = false;
					break;
				}
			}
			if (uniformLength)
			{
				int dataIndex;
				if (firstLength < 256)
				{
					arrayData = new byte[2 + firstLength * array.length];
					arrayData[0] = Byte.MAX_VALUE;
					arrayData[1] = convertDown(firstLength);
					dataIndex = 2;
				}
				else
				{
					arrayData = new byte[5 + firstLength * array.length];
					arrayData[0] = Byte.MIN_VALUE;
					byte[] lengthData = fromInteger(firstLength);
					arrayData[1] = lengthData[0];
					arrayData[2] = lengthData[1];
					arrayData[3] = lengthData[2];
					arrayData[4] = lengthData[3];
					dataIndex = 5;
				}
				for (int index = 0; index < array.length; index++)
				{
					for (int subIndex = 0; subIndex < firstLength; subIndex++)
					{
						arrayData[dataIndex + subIndex] = array[index][subIndex];
					}
					dataIndex += firstLength;
				}
			}
			else
			{
				List<Byte> byteList = new ArrayList<Byte>();
				for (int index = 0; index < array.length; index++)
				{
					if (array[index].length < 256)
					{
						byteList.add(Byte.MAX_VALUE);
						byteList.add(convertDown(array[index].length));
					}
					else
					{
						byteList.add(Byte.MIN_VALUE);
						byte[] lengthData = fromInteger(array[index].length);
						byteList.add(lengthData[0]);
						byteList.add(lengthData[1]);
						byteList.add(lengthData[2]);
						byteList.add(lengthData[3]);
					}
					for (int subIndex = 0; subIndex < array[index].length; subIndex++)
					{
						byteList.add(array[index][subIndex]);
					}
				}
				arrayData = new byte[byteList.size()];
				Iterator<Byte> iterator = byteList.iterator();
				for (int index = 0; index < arrayData.length; index++)
				{
					arrayData[index] = iterator.next().byteValue();
				}
			}
			
			byte[] data;
			if (array.length < 256)
			{
				data = new byte[arrayData.length + 3];
				data[1] = Byte.MAX_VALUE;
				data[2] = convertDown(array.length);
			}
			else
			{
				data = new byte[arrayData.length + 6];
				data[1] = Byte.MIN_VALUE;
				byte[] lengthData = fromInteger(array.length);
				data[2] = lengthData[0];
				data[3] = lengthData[1];
				data[4] = lengthData[2];
				data[5] = lengthData[3];
			}
			if (uniformLength)
			{
				data[0] = 1;
			}
			else
			{
				data[0] = 2;
			}
			for (int index = 0; index < arrayData.length; index++)
			{
				data[data.length - arrayData.length + index] = arrayData[index];
			}
			return data;
		}
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
	
	/**
	 * Converts a byte to its unsigned variant in the form of an integer
	 * @param value signed byte value
	 * @return unsigned byte value
	 */
	public static int convertUp(byte value)
	{
		int newValue = (int)value;
		newValue += 128;
		return newValue;
	}
	
	/**
	 * Converts an unsigned byte in the form of an integer, into a signed byte
	 * @param value unsigned byte value
	 * @return signed byte value
	 */
	public static byte convertDown(int value)
	{
		if (value >= 0 && value <= 255)
		{
			value -= 128;
			return (byte)value;
		}
		else
		{
			throw new IllegalStateException("BYTE CONVERTER - convertDown - Value not within unsigned byte range: Expected 0-255, got " + value);
		}
	}
	
	/**
	 * Converts byte data into a UUID
	 * @param data byte data
	 * @return UUID
	 */
	public static UUID toUUID(byte[] data)
	{
		return UUID.fromString(toString(data));
	}
	
	/**
	 * Converts a UUID into byte data
	 * @param id UUID value
	 * @return byte data
	 */
	public static byte[] fromUUID(UUID id)
	{
		return fromString(id.toString());
	}
	
	/**
	 * Converts an integer value into byte data
	 * @param value integer value
	 * @return byte data
	 */
	public static byte[] fromInteger(int value)
	{
		byte[] data = new byte[4];
		data[0] = convertDown(value >>> 24);
		value -= convertUp(data[0]) << 24;
		data[1] = convertDown(value >> 16);
		value -= convertUp(data[1]) << 16;
		data[2] = convertDown(value >> 8);
		value -= convertUp(data[2]) << 8;
		data[3] = convertDown(value);
		return data;
	}
	
	/**
	 * Converts byte data into an integer value
	 * @param data byte data
	 * @return integer value
	 */
	public static int toInteger(byte[] data)
	{
		if (data.length == 4)
		{
			return ((convertUp(data[0]) << 24) | (convertUp(data[1]) << 16) | (convertUp(data[2]) << 8) | convertUp(data[3]));
		}
		else
		{
			throw new IllegalStateException("BYTE CONVERTER - toInteger - Invalid byte array size: Expected 4, got " + data.length);
		}
	}
	
	/**
	 * Converts a long value into byte data
	 * @param value long value
	 * @return byte data
	 */
	public static byte[] fromLong(long value)
	{
		byte[] data = new byte[8];
		data[0] = convertDown((int)(value >>> 56));
		value -= (long)convertUp(data[0]) << 56;
		data[1] = convertDown((int)(value >> 48));
		value -= (long)convertUp(data[1]) << 48;
		data[2] = convertDown((int)(value >> 40));
		value -= (long)convertUp(data[2]) << 40;
		data[3] = convertDown((int)(value >>> 32));
		value -= (long)convertUp(data[3]) << 32;
		data[4] = convertDown((int)(value >>> 24));
		value -= (long)convertUp(data[4]) << 24;
		data[5] = convertDown((int)(value >> 16));
		value -= (long)convertUp(data[5]) << 16;
		data[6] = convertDown((int)(value >> 8));
		value -= (long)convertUp(data[6]) << 8;
		data[7] = convertDown((int)value);
		return data;
	}
	
	/**
	 * Converts byte data into a long value
	 * @param data byte data
	 * @return long value
	 */
	public static long toLong(byte[] data)
	{
		if (data.length == 8)
		{
			return (((long)convertUp(data[0]) << 56) | ((long)convertUp(data[1]) << 48) | ((long)convertUp(data[2]) << 40) | ((long)convertUp(data[3]) << 32) | ((long)convertUp(data[4]) << 24) | ((long)convertUp(data[5]) << 16) | ((long)convertUp(data[6]) << 8) | (long)convertUp(data[7]));
		}
		else
		{
			throw new IllegalStateException("BYTE CONVERTER - toLong - Invalid byte array size: Expected 8, got " + data.length);
		}
	}
	
	/**
	 * Converts a string value into byte data
	 * @param string string value
	 * @return byte data
	 */
	public static byte[] fromString(String string)
	{
		if (string == null || string.length() == 0) return new byte[0];
		byte[] data = new byte[string.length() * 2];
		int dataIndex = 0;
		for (int index = 0; index < string.length(); index++)
		{
			int character = (int)string.charAt(index);
			byte[] charData = fromInteger(character);
			data[dataIndex] = charData[2];
			data[dataIndex + 1] = charData[3];
			dataIndex += 2;
		}
		return data;
	}
	
	/**
	 * Converts byte data into a string value
	 * @param data byte data
	 * @return string value
	 */
	public static String toString(byte[] data)
	{
		if (data.length == 0) return "";
		if (data.length % 2 == 0)
		{
			String string = "";
			for (int index = 0; index < data.length; index += 2)
			{
				byte[] charData = {0, 0, data[index], data[index + 1]};
				string += (char)(toInteger(charData));
			}
			return string;
		}
		else
		{
			throw new IllegalStateException("BYTE CONVERTER - toString - Invalid byte array size: Expected multiple of 2, got " + data.length);
		}
	}
	
	/**
	 * Converts a boolean value into byte data
	 * @param value boolean value
	 * @return byte data
	 */
	public static byte fromBoolean(boolean value)
	{
		if (value)
		{
			return Byte.MAX_VALUE;
		}
		return Byte.MIN_VALUE;
	}
	
	/**
	 * Converts byte data into a boolean value
	 * @param data byte data
	 * @return boolean value
	 */
	public static boolean toBoolean(byte data)
	{
		if (data == Byte.MAX_VALUE)
		{
			return true;
		}
		return false;
	}
	
	/**
	 * Converts a float value into byte data
	 * @param value float value
	 * @return byte data
	 */
	public static byte[] fromFloat(float value)
	{
		return fromInteger(Float.floatToIntBits(value));
	}
	
	/**
	 * Converts byte data into a float value
	 * @param data byte data
	 * @return float value
	 */
	public static float toFloat(byte[] data)
	{
		return Float.intBitsToFloat(toInteger(data));
	}
	
	/**
	 * Converts a double value into byte data
	 * @param value double value
	 * @return byte data
	 */
	public static byte[] fromDouble(double value)
	{
		return fromLong(Double.doubleToLongBits(value));
	}
	
	/**
	 * Converts byte data into a double value
	 * @param data byte data
	 * @return double value
	 */
	public static double toDouble(byte[] data)
	{
		return Double.doubleToLongBits(toLong(data));
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
