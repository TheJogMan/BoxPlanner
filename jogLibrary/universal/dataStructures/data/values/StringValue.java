package jogLibrary.universal.dataStructures.data.values;

import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

import jogLibrary.universal.ByteArrayBuilder;
import jogLibrary.universal.dataStructures.data.TypeRegistry.ByteConsumer;
import jogLibrary.universal.dataStructures.data.TypeRegistry.CharacterConsumer;
import jogLibrary.universal.dataStructures.data.TypeRegistry.EmptyValue;
import jogLibrary.universal.dataStructures.data.TypeRegistry.ValidationValueObjects;
import jogLibrary.universal.dataStructures.data.TypeRegistry.ValidationValues;
import jogLibrary.universal.dataStructures.data.Value;
import jogLibrary.universal.indexable.ArrayIndexable;
import jogLibrary.universal.indexable.Consumer;
import jogLibrary.universal.indexable.Consumer.ConsumerResult;
import jogLibrary.universal.indexable.HoardingIndexer;
import jogLibrary.universal.indexable.Indexer;

public class StringValue extends Value<String, String>
{
	public StringValue(String value)
	{
		super(value);
	}
	
	public StringValue()
	{
		super();
	}
	
	@EmptyValue
	public String emptyValue()
	{
		return "";
	}
	
	private static final char[] unsanitaryCharacters = {'"', '\\', '\n', '\r', '\t', '\b'};
	public static final char[] wordSeparators = {' ', '\n', '	'};
	public static final char[] spaceCharacters = {' ','	','\n'};
	public static final char[] formattingCharacters = {'\n', '\r', '\t'};
	
	public static String sanitize(String string)
	{
		String newString = "";
		for (int index = 0; index < string.length(); index++)
		{
			char ch = string.charAt(index);
			if (CharacterValue.containsChar(ch, unsanitaryCharacters))
				newString += '\\';
			newString += ch;
		}
		return newString;
	}
	
	public static String desanitize(String string)
	{
		String newString = "";
		boolean unsanitary = false;
		for (int index = 0; index < string.length(); index++)
		{
			char ch = string.charAt(index);
			if (unsanitary)
			{
				newString += ch;
				unsanitary = false;
			}
			else
				if (ch == '\\')
					unsanitary = true;
				else
					newString += ch;
		}
		return newString;
	}
	
	public static String pack(String string)
	{
		return '"' + sanitize(string) + '"';
	}
	
	public static String unpack(String string)
	{
		if (string.length() > 1 && string.charAt(0) == '"' && string.charAt(string.length() - 1) == '"')
		{
			if (string.length() == 2)
				return "";
			else
				return desanitize(string.substring(1, string.length() - 1));
		}
		else
			return null;
	}
	
	@Override
	public String toString()
	{
		return pack(get());
	}
	
	@Override
	public byte[] toByteData()
	{
		return toByteData(get());
	}
	
	public static byte[] toByteData(String string)
	{
		ByteArrayBuilder builder = new ByteArrayBuilder();
		string = pack(string);
		for (int index = 0; index < string.length(); index++)
			builder.add(string.charAt(index));
		byte[] bytes = builder.toPrimitiveArray();
		return bytes;
	}
	
	public static String fromByteData(byte[] byteData)
	{
		try
		{
			return Charset.defaultCharset().newDecoder().decode(ByteBuffer.wrap(byteData)).toString();
		}
		catch (Exception e)
		{
			return null;
		}
	}
	
	public static HoardingIndexer<Character> indexer(String string)
	{
		return (new ArrayIndexable<Character>(toCharObjectArray(string))).iterator();
	}
	
	@Override
	public StringValue makeDuplicate()
	{
		return new StringValue(get());
	}
	
	public static Character[] toCharObjectArray(String string)
	{
		Character[] chars = new Character[string.length()];
		for (int index = 0; index < chars.length; index++)
		{
			chars[index] = string.charAt(index);
		}
		return chars;
	}
	
	public static void skipCharacters(Indexer<Character> indexer, char[] filter)
	{
		while (!indexer.atEnd() && CharacterValue.containsChar(indexer.get(), filter))
			indexer.next();
	}
	
	public static void skipCharacters(Indexer<Character> indexer, Character[] filter)
	{
		while (!indexer.atEnd() && CharacterValue.containsChar(indexer.get(), filter))
			indexer.next();
	}
	
	/***
	 * Consumes characters up until the end marker (exclusive) or the end of the indexer.
	 * @param indexer
	 * @param endMarker
	 * @return
	 */
	public static ConsumerResult<String, Character> consumeString(Indexer<Character> indexer, char endMarker)
	{
		StringBuilder builder = new StringBuilder();
		while (!indexer.atEnd() && indexer.get() != endMarker)
			builder.append(indexer.next());
		return new ConsumerResult<>(builder.toString(), indexer);
	}
	
	public static ConsumerResult<String, Character> consumeAlphabeticalString(Indexer<Character> indexer)
	{
		StringBuilder builder = new StringBuilder();
		while (!indexer.atEnd() && Character.isLetter(indexer.get()))
			builder.append(indexer.next());
		return new ConsumerResult<>(builder.toString(), indexer);
	}
	
	public static ConsumerResult<String, Character> consumeNumericalString(Indexer<Character> indexer)
	{
		StringBuilder builder = new StringBuilder();
		while (!indexer.atEnd() && Character.isDigit(indexer.get()))
			builder.append(indexer.next());
		return new ConsumerResult<>(builder.toString(), indexer);
	}
	
	public static ConsumerResult<String, Character> consumeString(Indexer<Character> indexer, int length)
	{
		StringBuilder builder = new StringBuilder();
		for (int index = 0; index < length; index++)
		{
			if (!indexer.atEnd())
				builder.append(indexer.next());
			else
				return new ConsumerResult<>(indexer, "Desired length not available.");
		}
		return new ConsumerResult<>(builder.toString(), indexer);
	}
	
	public static ConsumerResult<String, Character> consumeString(Indexer<Character> indexer, String filter)
	{
		return consumeString(indexer, filter, true);
	}
	
	public static ConsumerResult<String, Character> consumeString(Indexer<Character> indexer, String filter, boolean caseSensitive)
	{
		for (int index = 0; index < filter.length(); index++)
		{
			if (!indexer.atEnd() && caseSensitive ? (indexer.get() == filter.charAt(index)) : (Character.toLowerCase(indexer.get()) == (Character.toLowerCase(filter.charAt(index)))))
				indexer.next();
			else
				return new ConsumerResult<>(indexer, "Did not match filter.");
		}
		return new ConsumerResult<>(filter, indexer);
	}
	
	public static ConsumerResult<String, Character> consumeCharacters(Indexer<Character> indexer, char[] characters)
	{
		StringBuilder builder = new StringBuilder();
		while (!indexer.atEnd() && CharacterValue.containsChar(indexer.get(), characters))
			builder.append(indexer.next());
		return new ConsumerResult<>(builder.toString(), indexer);
	}
	
	public static ConsumerResult<Value<?, String>, Character> consume(Indexer<Character> input)
	{
		return characterConsumer().consume(input);
	}
	
	public static ConsumerResult<String, Character> simpleCharacterConsume(Indexer<Character> input)
	{
		ConsumerResult<Value<?, String>, Character> result = characterConsumer().consume(input);
		if (!result.success())
			return new ConsumerResult<>(result.indexer(), result.description());
		else
			return new ConsumerResult<>((String)result.value().get(), result.indexer(), result.success(), result.description());
	}
	
	public static ConsumerResult<String, Byte> simpleByteConsume(Indexer<Byte> input)
	{
		ConsumerResult<Value<?, String>, Byte> result = byteConsumer().consume(input);
		return new ConsumerResult<>((String)result.value().get(), result.indexer(), result.success(), result.description());
	}
	
	@CharacterConsumer
	public static Consumer<Value<?, String>, Character> characterConsumer()
	{
		return (source) ->
		{
			if (source.get() == '"')
			{
				StringBuilder builder = new StringBuilder();
				builder.append(source.next());
				boolean unsanitary = false;
				boolean closed = false;
				while(source.hasNext() || !source.finished())
				{
					char ch = source.next();
					if (unsanitary)
					{
						builder.append(ch);
						unsanitary = false;
					}
					else
					{
						if (ch == '\\')
						{
							builder.append(ch);
							unsanitary = true;
						}
						else if (ch == '"')
						{
							builder.append(ch);
							closed = true;
							break;
						}
						else
							builder.append(ch);
					}
				}
				if (!closed)
					return new ConsumerResult<>(source, "Must end with \"");
				else
				{
					return new ConsumerResult<>(new StringValue(unpack(builder.toString())), source);
				}
			}
			else
				return new ConsumerResult<>(source, "Must begin with \", got " + source.get());
		};
	}
	
	@ByteConsumer
	public static Consumer<Value<?, String>, Byte> byteConsumer()
	{
		return (source) ->
		{
			Consumer<Value<?, Character>, Byte> characterConsumer = CharacterValue.byteConsumer();
			ConsumerResult<Value<?, Character>, Byte> result = characterConsumer.consume(source);
			if (!result.success() || (Character)result.value().get() != '"')
				return new ConsumerResult<>(source, "Must begin with \"" + (result.success() ? "" : ": " + result.description()));
			else
			{
				StringBuilder builder = new StringBuilder();
				builder.append('"');
				boolean unsanitary = false;
				boolean closed = false;
				while(!source.atEnd())
				{
					result = characterConsumer.consume(source);
					if (!result.success())
						return new ConsumerResult<>(source, "Could not retrieve character at position " + builder.length() + ": " + result.description());
					
					char ch = (Character)result.value().get();
					if (unsanitary)
					{
						builder.append(ch);
						unsanitary = false;
					}
					else
					{
						if (ch == '\\')
						{
							builder.append(ch);
							unsanitary = true;
						}
						else if (ch == '"')
						{
							builder.append(ch);
							closed = true;
							break;
						}
						else
							builder.append(ch);
					}
				}
				if (!closed)
					return new ConsumerResult<>(source, "Must end with \"");
				else
				{
					return new ConsumerResult<>(new StringValue(unpack(builder.toString())), source);
				}
			}
		};
	}
	
	@Override
	public boolean equals(Value<?, ?> value)
	{
		return value != null && value.getClass().equals(getClass()) && ((StringValue)value).get().compareTo(get()) == 0;
	}
	
	@ValidationValues
	public static List<String> validationValues()
	{
		ArrayList<String> list = new ArrayList<String>(4);
		list.add("Hello World!");
		list.add("Lorem Ipsum");
		list.add("Line 1\nLine 2");
		list.add("Look at that slide \\, oh my.");
		return list;
	}
	
	@ValidationValueObjects
	public static List<Value<?, String>> validationValueObjects()
	{
		List<String> values = validationValues();
		ArrayList<Value<?, String>> objects = new ArrayList<>(values.size());
		values.forEach(value -> objects.add(new StringValue(value)));
		return objects;
	}
	
	public static boolean isWordSeparator(char ch)
	{
		for (int index = 0; index < wordSeparators.length; index++)
		{
			if (wordSeparators[index] == ch)
			{
				return true;
			}
		}
		return false;
	}
	
	public static String[] breakIntoLines(String string)
	{
		ArrayList<String> lines = new ArrayList<>();
		StringBuilder builder = new StringBuilder();
		for (int index = 0; index < string.length(); index++)
		{
			char ch = string.charAt(index);
			if (ch == '\n')
			{
				lines.add(builder.toString());
				builder.setLength(0);
			}
			else
				builder.append(ch);
		}
		if (builder.length() > 0)
			lines.add(builder.toString());
		return lines.toArray(new String[lines.size()]);
	}
}