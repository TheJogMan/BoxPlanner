package jogLibrary.universal.richString;

import java.awt.Font;

import jogLibrary.universal.dataStructures.data.values.ByteValue;

public class Style
{
	public static final double defaultFontScale = 1.0;
	public static final RichColor defaultMainColor = new RichColor(255, 255, 255, 255);
	public static final RichColor defaultBackgroundColor = new RichColor(0, 0, 0, 0);
	public static final byte defaultFlagByte = buildFlagByte(false, false, false, false);
	
	public static final byte buildFlagByte(boolean strikeThrough, boolean underline, boolean obfuscate, boolean highlighted)
	{
		return ByteValue.buildByte(
							//flagMap legend
				strikeThrough,	//strike through
				underline,		//underline
				obfuscate,		//obfuscate
				highlighted,	//highlighted
				false,			//unused
				false,			//unused
				false,			//unused
				false			//unused
		);
	}
	
	RichColor mainColor = RichColor.WHITE;
	RichColor backgroundColor = RichColor.CLEAR;
	Font font = Font.decode(null);
	byte flagByte = defaultFlagByte;
	
	public Style(RichColor mainColor, RichColor backgroundColor, Font font, byte flagByte)
	{
		this.mainColor = mainColor;
		this.backgroundColor = backgroundColor;
		this.font = font;
		this.flagByte = flagByte;
	}
	
	public Style(RichColor mainColor, byte flagByte)
	{
		this(mainColor, defaultBackgroundColor, Font.decode(null), flagByte);
	}
	
	public Style(RichColor mainColor, Font font, byte flagByte)
	{
		this(mainColor, defaultBackgroundColor, font, flagByte);
	}
	
	public Style(RichColor mainColor, RichColor backgroundColor, Font font)
	{
		this(mainColor, backgroundColor, font, defaultFlagByte);
	}
	
	public Style(RichColor mainColor, Font font)
	{
		this(mainColor, defaultBackgroundColor, font);
	}
	
	public Style(RichColor mainColor)
	{
		this(mainColor, Font.decode(null));
	}
	
	public Style(Font font)
	{
		this(defaultMainColor, font);
	}
	
	public Style()
	{
		this(defaultMainColor);
	}
	
	public static Style create()
	{
		return new Style();
	}
	
	@Override
	public boolean equals(Object object)
	{
		if (!(object instanceof Style))
			return false;
		Style otherStyle = (Style)object;
		return mainColor.equals(otherStyle.mainColor) && backgroundColor.equals(otherStyle.backgroundColor) && font.hashCode() == otherStyle.font.hashCode() && flagByte == otherStyle.flagByte;
	}
	
	@Override
	public Style clone()
	{
		return new Style(mainColor, backgroundColor, font, flagByte);
	}
	
	public RichColor getMainColor()
	{
		return mainColor.clone();
	}
	
	public Style setMainColor(RichColor mainColor)
	{
		this.mainColor = mainColor;
		return this;
	}
	
	public RichColor getBackgroundColor()
	{
		return backgroundColor.clone();
	}
	
	public Style setBackgroundColor(RichColor backgroundColor)
	{
		this.backgroundColor = backgroundColor;
		return this;
	}
	
	public float getPointSize()
	{
		return font.getSize2D();
	}
	
	public Style setPointScale(int pointSize)
	{
		font = new Font(font.getName(), font.getStyle(), pointSize);
		return this;
	}
	
	public Font getFont()
	{
		return font;
	}
	
	public Style setFont(Font font)
	{
		this.font = font;
		return this;
	}
	
	public byte getFlagByte()
	{
		return flagByte;
	}
	
	public Style setFlagByte(byte flagByte)
	{
		this.flagByte = flagByte;
		return this;
	}
	
	public boolean isBold()
	{
		return font.isBold();
	}
	
	public Style setBold(boolean bold)
	{
		font = font.deriveFont((bold ? Font.BOLD : 0) + (isItalic() ? Font.ITALIC : 0));
		return this;
	}
	
	public boolean isItalic()
	{
		return font.isItalic();
	}
	
	public Style setItalic(boolean italic)
	{
		font = font.deriveFont((isBold() ? Font.BOLD : 0) + (italic ? Font.ITALIC : 0));
		return this;
	}
	
	public boolean isStrikeThrough()
	{
		return ByteValue.getBit(flagByte, 2);
	}
	
	public Style setStrikeThrough(boolean strikeThrough)
	{
		flagByte = ByteValue.setBit(flagByte, 2, strikeThrough);
		return this;
	}
	
	public boolean isUnderline()
	{
		return ByteValue.getBit(flagByte, 3);
	}
	
	public Style setUnderline(boolean underline)
	{
		flagByte = ByteValue.setBit(flagByte, 3, underline);
		return this;
	}
	
	public boolean isObfuscated()
	{
		return ByteValue.getBit(flagByte, 4);
	}
	
	public Style setObfuscated(boolean obfuscated)
	{
		flagByte = ByteValue.setBit(flagByte, 4, obfuscated);
		return this;
	}
	
	public boolean isHighlighted()
	{
		return ByteValue.getBit(flagByte, 5);
	}
	
	public Style setHighlighted(boolean highlighted)
	{
		flagByte = ByteValue.setBit(flagByte, 5, highlighted);
		return this;
	}
}