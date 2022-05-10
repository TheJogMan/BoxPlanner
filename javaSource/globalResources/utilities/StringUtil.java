package globalResources.utilities;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Various utilities for working with strings
 */
public class StringUtil
{
	public static final char[] wordSeparators = {' ', '\n', '	'};
	public static final char[] spaceCharacters = {' ','	','\n'};
	
	
	
	
	
	
	
	public static class JogStringBuilder
	{
		ShiftingLinkedList<Character> characters;
		
		public JogStringBuilder()
		{
			characters = new ShiftingLinkedList<Character>();
		}
		
		public JogStringBuilder(String string)
		{
			this();
			append(string);
		}
		
		public JogStringBuilder(Object object)
		{
			this();
			append(object);
		}
		
		public JogStringBuilder(String[] strings, String separator)
		{
			this();
			for (int index = 0; index < strings.length; index++)
			{
				append(strings[index]);
				if (index < strings.length - 1) append(separator);
			}
		}
		
		public JogStringBuilder(String[] strings)
		{
			this(strings, "");
		}
		
		public JogStringBuilder(Object[] objects, String separator)
		{
			this();
			for (int index = 0; index < objects.length; index++)
			{
				append(objects[index]);
				if (index < objects.length - 1) append(separator);
			}
		}
		
		public JogStringBuilder(Object[] objects)
		{
			this(objects, "");
		}
		
		public JogStringBuilder(char character)
		{
			this();
			append(character);
		}
		
		public JogStringBuilder(char[] characters)
		{
			this(characters, "");
		}
		
		public JogStringBuilder(char[] characters, String separator)
		{
			this();
			for (int index = 0; index < characters.length; index++)
			{
				append(characters[index]);
				if (index < characters.length - 1) append(separator);
			}
		}
		
		public JogStringBuilder(Character character)
		{
			this(character.charValue());
		}
		
		public JogStringBuilder(Character[] characters)
		{
			this(characters, "");
		}
		
		public JogStringBuilder(Character[] characters, String separator)
		{
			this();
			for (int index = 0; index < characters.length; index++)
			{
				append(characters[index].charValue());
				if (index < characters.length - 1) append(separator);
			}
		}
		
		public void append(String string)
		{
			for (int index = 0; index < string.length(); index++) append(string.charAt(index));
		}
		
		public void append(char character)
		{
			characters.add(character);
		}
		
		public void append(Object object)
		{
			append(object.toString());
		}
		
		@Override
		public String toString()
		{
			StringBuilder builder = new StringBuilder(characters.size());
			for (Iterator<Character> iterator = characters.iterator(); iterator.hasNext();)
			{
				builder.append(iterator.next().charValue());
			}
			return builder.toString();
		}
		
		public void clear()
		{
			characters.clear();
		}
		
		public int size()
		{
			return characters.size();
		}
	}
	
	public static String[] split(String string, char separator)
	{
		return split(string, separator, false);
	}
	
	public static String[] split(String string, char separator, boolean includeSeparators)
	{
		ShiftingLinkedList<String> strings = new ShiftingLinkedList<String>();
		JogStringBuilder builder = new JogStringBuilder();
		for (int index = 0; index < string.length(); index++)
		{
			char ch = string.charAt(index);
			if (ch == separator)
			{
				strings.add(builder.toString());
				builder.clear();
				if (includeSeparators) strings.add(String.valueOf(ch));
			}
			else builder.append(ch);
		}
		if (builder.size() > 0) strings.add(builder.toString());
		return strings.getArray(new String[strings.size()]);
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	/**
	 * Checks if a given character is a word separator
	 * @param ch the character to check
	 * @return if the character is a word separator
	 */
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
	
	/**
	 * Breaks a single string into lines
	 * @param text the string to be broken up
	 * @return the individual lines
	 */
	public static String[] breakIntoLines(String text)
	{
		List<String> lines = new ArrayList<String>();
		String currentLine = "";
		for (int index = 0; index < text.length(); index++)
		{
			char ch = text.charAt(index);
			if (ch == '\n')
			{
				if (currentLine.length() > 0)
				{
					lines.add(currentLine);
				}
				currentLine = "";
			}
			else
			{
				currentLine += ch;
			}
		}
		if (currentLine.length() > 0)
		{
			lines.add(currentLine);
		}
		return lines.toArray(new String[lines.size()]);
	}
	
	/**
	 * Breaks down a string into individual words
	 * <p>
	 * the keepSeparators boolean argument in the primary breakIntoWords method will default to true.
	 * </p>
	 * @param text the string to be broken down
	 * @return the individual words
	 * @see #breakIntoWords(String, boolean)
	 */
	public static String[] breakIntoWords(String text)
	{
		return breakIntoWords(text, true);
	}
	
	/**
	 * Breaks down a string into individual words
	 * @param text the string to be broken down
	 * @param keepSeparators if the word separators should be included in the word list
	 * @return the individual words
	 */
	public static String[] breakIntoWords(String text, boolean keepSeparators)
	{
		List<String> words = new ArrayList<String>();
		String currentWord = "";
		for (int index = 0; index < text.length(); index++)
		{
			char ch = text.charAt(index);
			if (isWordSeparator(ch))
			{
				if (currentWord.length() > 0)
				{
					words.add(currentWord);
				}
				currentWord = "";
				if (keepSeparators) words.add("" + ch);
			}
			else
			{
				currentWord += ch;
			}
		}
		if (currentWord.length() > 0)
		{
			words.add(currentWord);
		}
		return words.toArray(new String[words.size()]);
	}
	
	/**
	 * Shortens the string until the final character is not the given character
	 * @param text the string to shorten
	 * @param ch the character to remove
	 * @return the shortened string
	 */
	public static String removeCharsFromEnd(String text, char ch)
	{
		if (text.length() > 1)
		{
			while (text.charAt(text.length() - 1) == ch)
			{
				text = text.substring(0, text.length() - 1);
			}
		}
		else
		{
			if (text.compareTo("" + ch) == 0)
			{
				text = "";
			}
		}
		return text;
	}
	
	/**
	 * Removes space characters from the beginnings and ends from a list of strings
	 * @param lines the strings to be trimmed
	 * @return the trimmed strings
	 */
	public static List<String> trimSpace(List<String> lines)
	{
		for (Iterator<String> iterator = lines.iterator(); iterator.hasNext();)
		{
			String line = iterator.next();
			int index = lines.indexOf(line);
			if (line.length() > 0)
			{
				boolean hadSpace;
				do
				{
					hadSpace = false;
					for (int c = 0; c < spaceCharacters.length; c++)
					{
						if (spaceCharacters[c] == line.charAt(0))
						{
							hadSpace = true;
							break;
						}
					}
					if (hadSpace) line = line.substring(1, line.length() - 1);
				}
				while (hadSpace);
				do
				{
					hadSpace = false;
					for (int c = 0; c < spaceCharacters.length; c++)
					{
						if (spaceCharacters[c] == line.charAt(line.length() - 1))
						{
							hadSpace = true;
							break;
						}
					}
					if (hadSpace) line = line.substring(0, line.length() - 1);
				}
				while (hadSpace);
			}
			lines.set(index, line);
		}
		return lines;
	}
	
	/**
	 * Formats a string of text
	 * @param text the string of text to be formated
	 * @return formated lines of text
	 */
	public static List<String> format(String text)
	{
		List<String> lines = new ArrayList<String>();
		String currentLine = "";
		for (int c = 0; c < text.length(); c++)
		{
			char ch = text.charAt(c);
			if (ch == '\n')
			{
				lines.add(currentLine + "\n");
				currentLine = "";
			}
			else
			{
				currentLine = currentLine + ch;
			}
		}
		if (currentLine.length() > 0) lines.add(currentLine);
		if (lines.size() == 0) lines.add("");
		return lines;
	}
}