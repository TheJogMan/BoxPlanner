package globalResources.richText;

import globalResources.graphics.Canvas;
import globalResources.utilities.ByteConverter;

public class RichCharacter
{
	public static final char defaultCharacter = ' ';
	public static final double defaultFontScale = 1.0;
	public static final RichColor defaultMainColor = new RichColor(255, 255, 255, 255);
	public static final RichColor defaultBackgroundColor = new RichColor(0, 0, 0, 0);
	public static final byte defaultFlagByte = buildFlagByte(false, false, false, false, false, false);
	
	public static final byte buildFlagByte(boolean bold, boolean italic, boolean strikeThrough, boolean underline, boolean obfuscate, boolean highlighted)
	{
		return ByteConverter.buildByte(
							//flagMap legend
				bold,			//bold
				italic,			//italic
				strikeThrough,	//strike through
				underline,		//underline
				obfuscate,		//obfuscate
				highlighted,	//unused
				false,			//unused
				false			//unused
		);
	}
	
	private Character character;
	private RichColor mainColor;
	private RichColor backgroundColor;
	private byte flagByte;
	private GeneralFont font;
	private double fontScale;
	
	public RichCharacter(char ch, RichColor mainColor, RichColor backgroundColor, GeneralFont font, double fontScale, byte flagByte)
	{
		character = new Character(ch);
		this.mainColor = mainColor;
		this.backgroundColor = backgroundColor;
		this.font = font;
		this.fontScale = fontScale;
		this.flagByte = flagByte;
	}
	
	public RichCharacter(char ch, RichColor mainColor, byte flagByte)
	{
		this(ch, mainColor, defaultBackgroundColor, GeneralFont.defaultFont, defaultFontScale, flagByte);
	}
	
	public RichCharacter(char ch, RichColor mainColor, GeneralFont font, double fontScale, byte flagByte)
	{
		this(ch, mainColor, defaultBackgroundColor, font, fontScale, flagByte);
	}
	
	public RichCharacter(char ch, RichColor mainColor, RichColor backgroundColor, GeneralFont font, double fontScale)
	{
		this(ch, mainColor, backgroundColor, font, fontScale, defaultFlagByte);
	}
	
	public RichCharacter(char ch, RichColor mainColor, GeneralFont font, double fontScale)
	{
		this(ch, mainColor, defaultBackgroundColor, font, fontScale);
	}
	
	public RichCharacter(char ch, RichColor mainColor, GeneralFont font)
	{
		this(ch, mainColor, font, defaultFontScale);
	}
	
	public RichCharacter(char ch, RichColor mainColor)
	{
		this(ch, mainColor, GeneralFont.defaultFont);
	}
	
	public RichCharacter(char ch, RichColor mainColor, RichColor backgroundColor, GeneralFont font)
	{
		this(ch, mainColor, backgroundColor, font, defaultFontScale);
	}
	
	public RichCharacter(char ch, GeneralFont font)
	{
		this(ch, defaultMainColor, font);
	}
	
	public RichCharacter(char ch)
	{
		this(ch, defaultMainColor);
	}
	
	public RichCharacter()
	{
		this(defaultCharacter);
	}
	
	//begin methods
	
	public RichCharacter clone()
	{
		RichCharacter character = new RichCharacter();
		character.character = this.character;
		character.mainColor = mainColor;
		character.backgroundColor = backgroundColor;
		character.flagByte = flagByte;
		character.font = font;
		character.fontScale = fontScale;
		return character;
	}
	
	public void draw(Canvas canvas, int x, int y)
	{
		font.draw(canvas, x, y, flagByte, fontScale, character, mainColor.color, backgroundColor.color);
	}
	
	public int getHeight()
	{
		return font.getHeight(fontScale, flagByte);
	}
	
	public int getWidth()
	{
		return font.getCharacterWidth(character, flagByte, fontScale);
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
	
	public Character getCharacter()
	{
		return character;
	}
	
	public char getPrimitiveCharacter()
	{
		return character.charValue();
	}
	
	public void setCharacter(Character character)
	{
		this.character = character;
	}
	
	public void setCharacter(char ch)
	{
		character = new Character(ch);
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