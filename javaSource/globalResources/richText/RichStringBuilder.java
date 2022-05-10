package globalResources.richText;

import globalResources.utilities.ByteConverter;
import globalResources.utilities.ShiftingLinkedList;
import globalResources.utilities.ShiftingLinkedList.ShiftingLinkedListIterator;

public class RichStringBuilder
{
	private ShiftingLinkedList<RichCharacter> characters;
	private RichColor mainColor;
	private RichColor backgroundColor;
	private GeneralFont font;
	private double fontScale;
	private byte flagByte;
	
	public RichStringBuilder()
	{
		characters = new ShiftingLinkedList<RichCharacter>();
		mainColor = RichColor.WHITE;
		backgroundColor = RichColor.CLEAR;
		font = GeneralFont.defaultFont;
		fontScale = 1.0;
		flagByte = RichCharacter.defaultFlagByte;
	}
	
	public RichStringBuilder(RichString base)
	{
		this();
		append(base);
	}
	
	public RichStringBuilder(String base)
	{
		this();
		append(base);
	}
	
	public void newLine()
	{
		append('\n');
	}
	
	public void append(RichCharacter character)
	{
		characters.add(character);
	}
	
	public void append(char character)
	{
		append(new RichCharacter(character, mainColor, backgroundColor, font, fontScale, flagByte));
	}
	
	public void append(RichString string)
	{
		for (int index = 0; index < string.length(); index++) characters.add(string.charAt(index).clone());
	}
	
	public void preAppend(RichStringBuilder builder)
	{
		for (ShiftingLinkedListIterator<RichCharacter> iterator = builder.characters.reverseIterator(); iterator.hasNext();) characters.addToBeginning(iterator.next());
	}
	
	public void append(String string)
	{
		append(new RichString(string, mainColor, backgroundColor, font, fontScale, flagByte));
	}
	
	public void append(RichColor temporaryMainColor, RichString string)
	{
		for (int index = 0; index < string.length(); index++)
		{
			RichCharacter character = string.charAt(index).clone();
			character.setMainColor(temporaryMainColor);
			characters.add(character);
		}
	}
	
	public void append(RichColor temporaryMainColor, String string)
	{
		RichColor mainColor = getMainColor();
		setMainColor(temporaryMainColor);
		append(string);
		setMainColor(mainColor);
	}
	
	public RichString build()
	{
		return new RichString(characters);
	}
	
	public void clear()
	{
		characters.clear();
	}
	
	public int length()
	{
		return characters.size();
	}
	
	public RichColor getMainColor()
	{
		return mainColor.clone();
	}
	
	public void setMainColor(RichColor mainColor)
	{
		this.mainColor = mainColor;
	}
	
	public RichColor getBackgroundColor()
	{
		return backgroundColor.clone();
	}
	
	public void setBackgroundColor(RichColor backgroundColor)
	{
		this.backgroundColor = backgroundColor;
	}
	
	public double getFontScale()
	{
		return fontScale;
	}
	
	public void setFontScale(double fontScale)
	{
		this.fontScale = fontScale;
	}
	
	public GeneralFont getFont()
	{
		return font;
	}
	
	public void setFont(GeneralFont font)
	{
		this.font = font;
	}
	
	public byte getFlagByte()
	{
		return flagByte;
	}
	
	public void setFlagByte(byte flagByte)
	{
		this.flagByte = flagByte;
	}
	
	public boolean isBold()
	{
		return ByteConverter.getBit(flagByte, 0);
	}
	
	public void setBold(boolean bold)
	{
		flagByte = ByteConverter.setBit(flagByte, 0, bold);
	}
	
	public boolean isItalic()
	{
		return ByteConverter.getBit(flagByte, 1);
	}
	
	public void setItalic(boolean italic)
	{
		flagByte = ByteConverter.setBit(flagByte, 1, italic);
	}
	
	public boolean isStrikeThrough()
	{
		return ByteConverter.getBit(flagByte, 2);
	}
	
	public void setStrikeThrough(boolean strikeThrough)
	{
		flagByte = ByteConverter.setBit(flagByte, 2, strikeThrough);
	}
	
	public boolean isUnderline()
	{
		return ByteConverter.getBit(flagByte, 3);
	}
	
	public void setUnderline(boolean underline)
	{
		flagByte = ByteConverter.setBit(flagByte, 3, underline);
	}
	
	public boolean isObfuscated()
	{
		return ByteConverter.getBit(flagByte, 4);
	}
	
	public void setObfuscated(boolean obfuscated)
	{
		flagByte = ByteConverter.setBit(flagByte, 4, obfuscated);
	}
	
	public boolean isHighlighted()
	{
		return ByteConverter.getBit(flagByte, 5);
	}
	
	public void setHighlighted(boolean highlighted)
	{
		flagByte = ByteConverter.setBit(flagByte, 5, highlighted);
	}
}