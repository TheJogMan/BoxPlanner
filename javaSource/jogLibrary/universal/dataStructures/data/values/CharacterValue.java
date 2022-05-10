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

public class CharacterValue extends Value<Character, Character>
{
	public CharacterValue()
	{
		super();
	}
	
	public CharacterValue(char character)
	{
		super(character);
	}
	
	@EmptyValue
	public Character emptyValue()
	{
		return ' ';
	}
	
	@Override
	public String toString()
	{
		return "" + get();
	}
	
	@Override
	public byte[] toByteData()
	{
		return toByteData(get());
	}
	
	public static byte[] toByteData(Character value)
	{
		ByteBuffer buffer = ByteBuffer.allocate(2);
		buffer.putChar(value);
		return buffer.array();
	}
	
	public static Character fromByteData(byte[] byteData)
	{
		ByteBuffer buffer = ByteBuffer.wrap(byteData);
		return buffer.getChar();
	}
	
	@Override
	public CharacterValue makeDuplicate()
	{
		return new CharacterValue(get());
	}
	
	@CharacterConsumer
	public static Consumer<Value<?, Character>, Character> characterConsumer()
	{
		return (indexer) ->
		{
			return new ConsumerResult<>(new CharacterValue(indexer.next()), indexer);
		};
	}
	
	@ByteConsumer
	public static Consumer<Value<?, Character>, Byte> byteConsumer()
	{
		return (source) ->
		{
			if (source.atEnd())
				return new ConsumerResult<>(source, "No available data.");
			else
			{
				VectorIndexable<Byte> bytes = source.next(2);
				if (bytes == null)
					return new ConsumerResult<>(source, "Not enough available data.");
				else
				{
					return new ConsumerResult<Value<?, Character>, Byte>(new CharacterValue(fromByteData(ByteArrayBuilder.toPrimitive(bytes))), source);
				}
			}
		};
	}
	
	public static boolean containsChar(char ch, char[] filter)
	{
		for (int index = 0; index < filter.length; index++)
		{
			if (filter[index] == ch)
				return true;
		}
		return false;
	}
	
	public static boolean containsChar(char ch, Character[] filter)
	{
		for (int index = 0; index < filter.length; index++)
		{
			if (filter[index] == ch)
				return true;
		}
		return false;
	}
	
	@Override
	public boolean equals(Value<?, ?> value)
	{
		return value != null && value.getClass().equals(getClass()) && value.get() == get();
	}
	
	@ValidationValues
	public static List<Character> validationValues()
	{
		ArrayList<Character> list = new ArrayList<Character>(5);
		list.add('A');
		list.add('\n');
		list.add('\\');
		list.add('"');
		return list;
	}
	
	@ValidationValueObjects
	public static List<Value<?, Character>> validationValueObjects()
	{
		List<Character> values = validationValues();
		ArrayList<Value<?, Character>> objects = new ArrayList<>(values.size());
		values.forEach(value -> objects.add(new CharacterValue(value)));
		return objects;
	}
	
	private static final char[] bulkyConversions = {
			'!', '#', '$', '%', '&', '(', ')', '*', '+', ',', '-', '.', '0', '1', '2', '3',
			'4', '5', '6', '7', '8', '9', ':', ';', '<', '=', '>', '?', '@', 'A', 'B', 'C',
			'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S',
			'T', 'U', 'V', 'W', 'X', 'Y', 'Z', '[', ']', '^', '_', '`', 'a', 'b', 'c', 'd',
			'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't',
			'u', 'v', 'w', 'x', 'y', 'z', '{', '|', '}', '~', '©', '€', '‚', 'ƒ', '„', '…',
			'†', '‡', '‰', 'ˆ', 'Š', '‹', 'Œ', 'Ž', '‘', '’', '“', '”', '•', '–', '—', '˜',
			'™', 'š', '›', 'œ', 'ž', 'Ÿ', '¡', '¢', '£', '¤', '¥', '¦', '§', '¨', 'ª', '«',
			'¬', '®', '¯', '°', '±', '²', '³', '´', 'µ', '¶', '·', '¸', '¹', 'º', '»', '¼',
			'½', '¾', '¿', 'À', 'Á', 'Â', 'Ã', 'Ä', 'Å', 'Æ', 'Ç', 'È', 'É', 'Ê', 'Ë', 'Ì',
			'Í', 'Î', 'Ï', 'Ð', 'Ñ', 'Ò', 'Ó', 'Ô', 'Õ', 'Ö', '×', 'Ø', 'Ù', 'Ú', 'Û', 'Ü',
			'Ý', 'Þ', 'ß', 'à', 'á', 'â', 'ã', 'ä', 'å', 'æ', 'ç', 'è', 'é', 'ê', 'ë', 'ì',
			'í', 'î', 'ï', 'ð', 'ñ', 'ò', 'ó', 'ô', 'õ', 'ö', '÷', 'ø', 'ù', 'ú', 'û', 'ü',
			'ý', 'þ', 'ÿ', 'Ā', 'ā', 'Ă', 'ă', 'Ą', 'ą', 'Ć', 'ć', 'Ĉ', 'ĉ', 'Ċ', 'ċ', 'Č',
			'č', 'Ď', 'ď', 'Đ', 'đ', 'Ē', 'ē', 'Ĕ', 'ĕ', 'Ė', 'ė', 'Ę', 'ę', 'Ě', 'ě', 'Ĝ',
			'ĝ', 'Ğ', 'ğ', 'Ġ', 'ġ', 'Ģ', 'ģ', 'Ĥ', 'ĥ', 'Ħ', 'ħ', 'Ĩ', 'ĩ', 'Ī', 'ī', 'Ĭ',
	};
	
	public static String bulkyConvert(byte[] data)
	{
		StringBuilder builder = new StringBuilder();
		for (int index = 0; index < data.length; index++)
		{
			builder.append(bulkyConvert(data[index]));
		}
		return builder.toString();
	}
	
	public static byte[] bulkyConvert(String string)
	{
		byte[] data = new byte[string.length()];
		for (int index = 0; index < string.length(); index++)
			data[index] = bulkyConvert(string.charAt(index));
		return data;
	}
	
	public static char bulkyConvert(byte value)
	{
		return bulkyConversions[((int)value) + 127];
	}
	
	public static byte bulkyConvert(char value)
	{
		for (int index = 0; index < bulkyConversions.length; index++)
		{
			if (bulkyConversions[index] == value)
				return (byte)(index - 127);
		}
		return 0;
	}
	
	public static byte[] massBulkyConvert(char[] values)
	{
		byte[] data = new byte[values.length];
		for (int index = 0; index < bulkyConversions.length; index++)
		{
			for (int valueIndex = 0; valueIndex < values.length; valueIndex++)
			{
				data[valueIndex] = 0;
				if (bulkyConversions[index] == values[valueIndex])
					data[valueIndex] = (byte)(index - 127);
			}
		}
		return data;
	}
	
	public static Character[] toObjectArray(char[] characters)
	{
		Character[] objects = new Character[characters.length];
		for (int index = 0; index < characters.length; index++)
			objects[index] = characters[index];
		return objects;
	}
	
	public static char[] toPrimitiveArray(Character[] objects)
	{
		char[] characters = new char[objects.length];
		for (int index = 0; index < characters.length; index++)
			characters[index] = objects[index];
		return characters;
	}
}