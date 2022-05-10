package globalResources.richText;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import globalResources.graphics.Canvas;
import globalResources.utilities.StringUtil;

public class RichString
{
	RichCharacter[] characters;
	
	public RichString(String str, RichColor mainColor, RichColor backgroundColor, GeneralFont font, double fontScale, byte flagByte)
	{
		characters = new RichCharacter[str.length()];
		for (int index = 0; index < str.length(); index++) characters[index] = new RichCharacter(str.charAt(index), mainColor, backgroundColor, font, fontScale, flagByte);
	}
	
	public RichString(String str, RichColor mainColor, GeneralFont font, double fontScale, byte flagByte)
	{
		characters = new RichCharacter[str.length()];
		for (int index = 0; index < str.length(); index++) characters[index] = new RichCharacter(str.charAt(index), mainColor, font, fontScale, flagByte);
	}
	
	public RichString(String str, RichColor mainColor, RichColor backgroundColor, GeneralFont font, double fontScale)
	{
		characters = new RichCharacter[str.length()];
		for (int index = 0; index < str.length(); index++) characters[index] = new RichCharacter(str.charAt(index), mainColor, backgroundColor, font, fontScale);
	}
	
	public RichString(String str, RichColor mainColor, GeneralFont font)
	{
		characters = new RichCharacter[str.length()];
		for (int index = 0; index < str.length(); index++) characters[index] = new RichCharacter(str.charAt(index), mainColor, font);
	}
	
	public RichString(String str, RichColor mainColor)
	{
		characters = new RichCharacter[str.length()];
		for (int index = 0; index < str.length(); index++) characters[index] = new RichCharacter(str.charAt(index), mainColor);
	}
	
	public RichString(String str, RichColor mainColor, RichColor backgroundColor, GeneralFont font)
	{
		characters = new RichCharacter[str.length()];
		for (int index = 0; index < str.length(); index++) characters[index] = new RichCharacter(str.charAt(index), mainColor, backgroundColor, font);
	}
	
	public RichString(String str, GeneralFont font)
	{
		characters = new RichCharacter[str.length()];
		for (int index = 0; index < str.length(); index++) characters[index] = new RichCharacter(str.charAt(index), font);
	}
	
	public RichString(String str)
	{
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
		this.characters = new RichCharacter[characters.size()];
		int index = 0;
		for (Iterator<RichCharacter> iterator = characters.iterator(); iterator.hasNext();)
		{
			this.characters[index] = iterator.next();
			index++;
		}
	}
	
	//begin methods
	
	@Override
	public RichString clone()
	{
		return (new RichStringBuilder(this)).build();
	}
	
	public void draw(Canvas canvas, int x, int y)
	{
		for (int index = 0; index < characters.length; index++)
		{
			characters[index].draw(canvas, x, y);
			x += characters[index].getWidth();
		}
	}
	
	public int getWidth()
	{
		int width = 0;
		for (int index = 0; index < characters.length; index++) width += characters[index].getWidth();
		return width;
	}
	
	public int getHeight()
	{
		int height = 0;
		for (int index = 0; index < characters.length; index++)
		{
			int characterHeight = characters[index].getHeight();
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
	
	public String toMarkupString()
	{
		return (new MarkupRoom(this)).markedUpString;
	}
	
	public String toCodedString()
	{
		int currentColor = RichColor.WHITE.color.getRGB();
		boolean bold = false;
		boolean italic = false;
		boolean underlined = false;
		boolean strikethrough = false;
		boolean obfuscate = false;
		StringBuilder builder = new StringBuilder(characters.length * 3);
		
		for (int index = 0; index < characters.length; index++)
		{
			RichCharacter ch = characters[index];
			RichColor color = ch.getMainColor();
			if (color.color.getRGB() != currentColor)
			{
				builder.append(color.toInlineCode());
				currentColor = color.color.getRGB();
			}
			if (ch.isBold() != bold)
			{
				bold = !bold;
				builder.append(RichColor.characterCodePrefix);
				builder.append(RichColor.characterCodeBold);
			}
			if (ch.isItalic() != italic)
			{
				italic = !italic;
				builder.append(RichColor.characterCodePrefix);
				builder.append(RichColor.characterCodeItalic);
			}
			if (ch.isUnderline() != underlined)
			{
				underlined = !underlined;
				builder.append(RichColor.characterCodePrefix);
				builder.append(RichColor.characterCodeUnderline);
			}
			if (ch.isStrikeThrough() != strikethrough)
			{
				strikethrough = !strikethrough;
				builder.append(RichColor.characterCodePrefix);
				builder.append(RichColor.characterCodeStrikethrough);
			}
			if (ch.isObfuscated() != obfuscate)
			{
				obfuscate = !obfuscate;
				builder.append(RichColor.characterCodePrefix);
				builder.append(RichColor.characterCodeObfuscate);
			}
			builder.append(ch.getCharacter());
		}
		builder.append(RichColor.characterCodePrefix);
		builder.append(RichColor.characterCodeReset);
		
		return builder.toString();
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
			if (StringUtil.isWordSeparator(ch.getPrimitiveCharacter()))
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
				for (int c = 0; c < StringUtil.spaceCharacters.length; c++)
				{
					if (StringUtil.spaceCharacters[c] == characters[0].getPrimitiveCharacter())
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
				for (int c = 0; c < StringUtil.spaceCharacters.length; c++)
				{
					if (StringUtil.spaceCharacters[c] == characters[length() - 1].getPrimitiveCharacter())
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
	
	public void setFont(GeneralFont font)
	{
		for (int index = 0; index < characters.length; index++)
		{
			characters[index].setFont(font);
		}
	}
	
	private static class MarkupRoom
	{
		Style currentStyle;
		StringBuilder builder;
		String markedUpString;
		RichCharacter ch;
		
		public MarkupRoom(RichString string)
		{
			builder = new StringBuilder(string.characters.length * 3);
			currentStyle = Style.PLAIN;
			
			builder.append(currentStyle.enter);
			for (int index = 0; index < string.characters.length; index++)
			{
				ch = string.characters[index];
				Style style = Style.getStyle(ch);
				if (!style.equals(currentStyle))
				{
					builder.append(currentStyle.exit);
					currentStyle = style;
					builder.append(currentStyle.enter);
				}
				builder.append(ch.getCharacter());
			}
			builder.append(currentStyle.exit);
			markedUpString = builder.toString();
		}
		
		private enum Style
		{
			PLAIN					("", ""),
			ITALIC					("*", "*"),
			BOLD					("**", "**"),
			BOLD_ITALIC				("***", "***"),
			UNDERLINE				("__", "__"),
			UNDERLINE_ITALIC		("__*", "*__"),
			UNDERLINE_BOLD			("__**", "**__"),
			UNDERLINE_BOLD_ITALIC	("__***", "***__"),
			STRIKETHROUGH			("~~", "~~"),
			HIGHLIGHT				("`", "`");
			
			String enter;
			String exit;
			
			Style(String enter, String exit)
			{
				this.enter = enter;
				this.exit = exit;
			}
			
			static Style getStyle(RichCharacter ch)
			{
				if (ch.isStrikeThrough()) return Style.STRIKETHROUGH;
				else if (ch.isHighlighted()) return Style.HIGHLIGHT;
				else if (ch.isUnderline() && ch.isItalic() && ch.isBold()) return Style.UNDERLINE_BOLD_ITALIC;
				else if (!ch.isUnderline() && ch.isItalic() && ch.isBold()) return Style.BOLD_ITALIC;
				else if (!ch.isUnderline() && !ch.isItalic() && ch.isBold()) return Style.BOLD;
				else if (!ch.isUnderline() && ch.isItalic() && !ch.isBold()) return Style.ITALIC;
				else if (ch.isUnderline() && !ch.isItalic() && ch.isBold()) return Style.UNDERLINE_BOLD;
				else if (ch.isUnderline() && !ch.isItalic() && !ch.isBold()) return Style.UNDERLINE;
				else if (ch.isUnderline() && ch.isItalic() && !ch.isBold()) return Style.UNDERLINE_ITALIC;
				return Style.PLAIN;
			}
		}
	}
}