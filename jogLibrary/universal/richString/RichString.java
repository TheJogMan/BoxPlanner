package jogLibrary.universal.richString;

import java.awt.Font;
import java.awt.font.FontRenderContext;
import java.awt.font.TextLayout;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import jogLibrary.universal.dataStructures.data.values.StringValue;

public class RichString
{
	RichCharacter[] characters;
	
	public RichString(String str, Style style)
	{
		if (str == null)
			str = "null";
		characters = new RichCharacter[str.length()];
		for (int index = 0; index < str.length(); index++) characters[index] = new RichCharacter(str.charAt(index), style);
	}
	
	public RichString(String str)
	{
		if (str == null)
			str = "null";
		characters = new RichCharacter[str.length()];
		for (int index = 0; index < str.length(); index++) characters[index] = new RichCharacter(str.charAt(index));
	}
	
	public RichString()
	{
		characters = new RichCharacter[0];
	}
	
	public RichString(RichCharacter... characters)
	{
		this.characters = characters;
	}
	
	public RichString(Collection<RichCharacter> characters)
	{
		if (characters == null)
			characters = new ArrayList<>();
		this.characters = new RichCharacter[characters.size()];
		int index = 0;
		for (Iterator<RichCharacter> iterator = characters.iterator(); iterator.hasNext();)
		{
			this.characters[index] = iterator.next();
			index++;
		}
	}
	
	@Override
	public RichString clone()
	{
		return (new RichStringBuilder(this)).build();
	}
	
	/*
	TODO: implement draw method in RichString
	public void draw(Canvas canvas, int x, int y)
	{
		for (int index = 0; index < characters.length; index++)
		{
			characters[index].draw(canvas, x, y);
			x += characters[index].getWidth();
		}
	}
	*/
	
	
	public int getLogicalWidth()
	{
		ArrayList<String> segments = new ArrayList<String>();
		ArrayList<Font> fonts = new ArrayList<Font>();
		String segment = "";
		RichCharacter lastCharacter = null;
		for (int index = 0; index < length(); index++)
		{
			RichCharacter character = charAt(index);
			if (lastCharacter == null || lastCharacter.style.font.hashCode() == character.style.font.hashCode())
				segment += character.character;
			else
			{
				fonts.add(lastCharacter.style.font);
				segments.add(segment);
				segment = "" + character.character;
			}
			lastCharacter = character;
		}
		if (segment.length() > 0)
		{
			fonts.add(lastCharacter.style.font);
			segments.add(segment);
		}
		
		int width = 0;
		
		for (int index = 0; index < segments.size(); index++)
		{
			segment = segments.get(index);
			Font font = fonts.get(index);
			width += font.getStringBounds(segment, new FontRenderContext(font.getTransform(), true, true)).getBounds().width;
		}
		return width;
	}
	
	public int getLogicalHeight()
	{
		int height = 0;
		for (int index = 0; index < characters.length; index++)
		{
			int characterHeight = characters[index].getLogicalHeight();
			if (characterHeight > height) height = characterHeight;
		}
		return height;
	}
	
	public int getVisualWidth()
	{
		ArrayList<String> segments = new ArrayList<String>();
		ArrayList<Font> fonts = new ArrayList<Font>();
		String segment = "";
		RichCharacter lastCharacter = null;
		for (int index = 0; index < length(); index++)
		{
			RichCharacter character = charAt(index);
			if (lastCharacter == null || lastCharacter.style.font.hashCode() == character.style.font.hashCode())
				segment += character.character;
			else
			{
				fonts.add(lastCharacter.style.font);
				segments.add(segment);
				segment = "" + character.character;
			}
			lastCharacter = character;
		}
		if (segment.length() > 0)
		{
			fonts.add(lastCharacter.style.font);
			segments.add(segment);
		}
		
		int width = 0;
		
		for (int index = 0; index < segments.size(); index++)
		{
			segment = segments.get(index);
			Font font = fonts.get(index);
			width += (new TextLayout(segment, font, new FontRenderContext(font.getTransform(), true, true))).getPixelBounds(null, 0, 0).width;
		}
		return width;
	}
	
	public int getVisualHeight()
	{
		int height = 0;
		for (int index = 0; index < characters.length; index++)
		{
			int characterHeight = characters[index].getVisualHeight();
			if (characterHeight > height) height = characterHeight;
		}
		return height;
	}
	
	
	
	
	
	
	@Override
	public String toString()
	{
		StringBuilder builder = new StringBuilder(characters.length);
		for (int index = 0; index < characters.length; index++) builder.append(characters[index].getPrimitiveCharacter());
		return builder.toString();
	}
	
	public String encode(EncodingType type)
	{
		return type.encode(this);
	}
	
	public int length()
	{
		return characters.length;
	}
	
	public RichCharacter charAt(int index)
	{
		return characters[index];
	}
	
	public int compareTo(RichString anotherRichString)
	{
		//TODO: Custom implementation to boost efficiency
		return toString().compareTo(anotherRichString.toString());
	}
	
	public int compareTo(String anotherString)
	{
		return toString().compareTo(anotherString);
	}
	
	public int compareToIgnoreCase(RichString anotherRichString)
	{
		//TODO: Custom implementation to boost efficiency
		return toString().compareToIgnoreCase(anotherRichString.toString());
	}
	
	public int compareToIgnoreCase(String anotherString)
	{
		return toString().compareToIgnoreCase(anotherString);
	}
	
	public void concat(RichString anotherRichString)
	{
		RichCharacter[] newChars = new RichCharacter[characters.length + anotherRichString.characters.length];
		for (int index = 0; index < characters.length; index++) newChars[index] = characters[index];
		for (int index = 0; index < anotherRichString.characters.length; index++) newChars[characters.length + index] = anotherRichString.characters[index];
		this.characters = newChars;
	}
	
	public RichString safeConcat(RichString anotherRichString)
	{
		RichString newString = new RichString();
		newString.characters = new RichCharacter[characters.length + anotherRichString.characters.length];
		for (int index = 0; index < characters.length; index++) newString.characters[index] = characters[index];
		for (int index = 0; index < anotherRichString.characters.length; index++) newString.characters[characters.length + index] = anotherRichString.characters[index];
		return newString;
	}
	
	public void concat(String anotherString)
	{
		concat(new RichString(anotherString));
	}
	
	public RichString safeConcat(String anotherString)
	{
		return safeConcat(new RichString(anotherString));
	}
	
	public RichString[] breakIntoWords()
	{
		List<RichString> words = new ArrayList<RichString>();
		RichStringBuilder currentWord = new RichStringBuilder();
		for (int index = 0; index < characters.length; index++)
		{
			RichCharacter ch = characters[index];
			if (StringValue.isWordSeparator(ch.getPrimitiveCharacter()))
			{
				if (currentWord.length() > 0) words.add(currentWord.build());
				currentWord.clear();
				words.add(new RichString(ch));
			}
			else currentWord.append(ch);
		}
		if (currentWord.length() > 0) words.add(currentWord.build());
		return words.toArray(new RichString[words.size()]);
	}
	
	public RichCharacter[] getSection(int start, int end)
	{
		end++;
		if (start >= 0 && end <= characters.length && end > start)
		{
			RichCharacter[] newChars = new RichCharacter[end - start];
			for (int index = start; index < end; index++) newChars[index - start] = characters[index];
			return newChars;
		}
		return new RichCharacter[0];
	}
	
	public void substring(int start, int end)
	{
		characters = getSection(start, end);
	}
	
	public RichString safeSubstring(int start, int end)
	{
		return new RichString(getSection(start, end));
	}
	
	public void trimSpace()
	{
		if (characters.length > 0)
		{
			boolean hadSpace;
			do
			{
				hadSpace = false;
				for (int c = 0; c < StringValue.spaceCharacters.length; c++)
				{
					if (StringValue.spaceCharacters[c] == characters[0].getPrimitiveCharacter())
					{
						hadSpace = true;
						break;
					}
				}
				if (hadSpace) substring(1, length() - 1);
			}
			while (hadSpace);
			do
			{
				hadSpace = false;
				for (int c = 0; c < StringValue.spaceCharacters.length; c++)
				{
					if (StringValue.spaceCharacters[c] == characters[length() - 1].getPrimitiveCharacter())
					{
						hadSpace = true;
						break;
					}
				}
				if (hadSpace) substring(0, length() - 2);
			}
			while (hadSpace);
		}
	}
	
	public void setFont(Font font)
	{
		for (int index = 0; index < characters.length; index++)
		{
			characters[index].style.setFont(font);
		}
	}
}