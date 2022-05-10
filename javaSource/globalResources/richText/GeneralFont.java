package globalResources.richText;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.GraphicsEnvironment;
import java.io.InputStream;

import globalResources.graphics.Canvas;
import globalResources.graphics.Image;
import globalResources.graphics.jFont.CharacterData;
import globalResources.graphics.jFont.JFont;
import globalResources.utilities.ByteConverter;

public class GeneralFont
{
	public static final GeneralFont defaultFont = new GeneralFont(JFont.STANDARD);
	public static final GeneralFont defaultRealFont = new GeneralFont(safelyCreateFont(Font.TRUETYPE_FONT, Image.class.getResourceAsStream("/resources/fonts/courier.ttf"), false));
	
	private static Font safelyCreateFont(int type, InputStream stream, boolean getDefaultRealFontIfFailed)
	{
		try
		{
			return Font.createFont(type, stream);
		}
		catch (Exception e)
		{
			System.out.println("Failed to create font");
			e.printStackTrace();
			if (getDefaultRealFontIfFailed) return defaultRealFont.getFontR();
			else return GraphicsEnvironment.getLocalGraphicsEnvironment().getAllFonts()[0];
		}
	}
	
	private Object font;
	private boolean realFont;
	
	public GeneralFont(JFont font)
	{
		this.font = font;
		realFont = false;
	}
	
	public GeneralFont(Font font)
	{
		this.font = font;
		realFont = true;
	}
	
	public int getHeight(double scale, byte flagByte)
	{
		if (realFont)
		{
			FontMetrics metrics = getMetrics(scale, ByteConverter.getBit(flagByte, 0), ByteConverter.getBit(flagByte, 1));
			return metrics.getHeight();
		}
		else
		{
			return (int)(getFont().getCharacterHeight() * scale);
		}
	}
	
	public int getStringWidth(String text, byte flagByte, double scale)
	{
		if (realFont)
		{
			FontMetrics metrics = getMetrics(scale, ByteConverter.getBit(flagByte, 0), ByteConverter.getBit(flagByte, 1));
			return metrics.stringWidth(text);
		}
		else
		{
			return (int)(getFont().getStringWidth(text) * scale);
		}
	}
	
	public int getCharacterWidth(Character character, byte flagByte, double scale)
	{
		if (realFont)
		{
			FontMetrics metrics = getMetrics(scale, ByteConverter.getBit(flagByte, 0), ByteConverter.getBit(flagByte, 1));
			return metrics.charWidth(character.charValue());
		}
		else
		{
			return (int)(getFont().getCharacterWidth(character.charValue()) * scale);
		}
	}
	
	public void draw(Canvas canvas, int dx, int dy, byte flagByte, double fontScale, Character character, Color mainColor, Color backgroundColor)
	{
		JFont drawFont;
		if (realFont) drawFont = JFont.STANDARD;
		else drawFont = (JFont)font;
		
		CharacterData data = drawFont.getCharacterData(character.charValue());
		int yOrigin = 0;
		if (!data.isUpperCase())
		{
			yOrigin = drawFont.getVerticalSeperator() + 1;
		}
		int yp = dy;
		for (int y = yOrigin; y < yOrigin + drawFont.getCharacterHeight(); y++)
		{
			int xp = dx;
			for (int x = data.getOffset(); x < data.getOffset() + data.getWidth(); x++)
			{
				if (xp >= 0 && xp < canvas.getWidth() && yp >= 0 && yp < canvas.getHeight())
				{
					int pixelColor = drawFont.getFontImage().getPixel(x, y);
					if (pixelColor == 0xffffffff) canvas.drawRect(xp,yp,(int)fontScale,(int)fontScale, mainColor);
					else canvas.drawRect(xp,yp,(int)fontScale,(int)fontScale, backgroundColor);
				}
				xp += fontScale;
			}
			yp += fontScale;
		}
	}
	
	public boolean isRealFont()
	{
		return realFont;
	}
	
	public Font getFontR()
	{
		if (realFont) return (Font)font;
		else return null;
	}
	
	public JFont getFont()
	{
		if(!realFont) return (JFont)font;
		else return null;
	}
	
	private FontMetrics getMetrics(double scale, boolean bold, boolean italic)
	{
		java.awt.Canvas canvas = new java.awt.Canvas();
		return canvas.getFontMetrics(getFontRF(scale, bold, italic));
	}
	
	private static int getStyle(boolean bold, boolean italic)
	{
		int style = 0;
		if (bold) style = style | Font.BOLD;
		if (italic) style = style | Font.ITALIC;
		return style;
	}
	
	public Font getFontRF(double scale, boolean bold, boolean italic)
	{
		return new Font(getFontR().getFamily(), getStyle(bold, italic), (int)((double)getFontR().getSize() * scale));
	}
	
	public boolean sameFont(GeneralFont font)
	{
		if (font.realFont == realFont)
		{
			if (realFont)
			{
				Font thisFont = getFontR();
				Font thatFont = font.getFontR();
				return thisFont.getFamily().compareTo(thatFont.getFamily()) == 0 && thisFont.getSize() == thatFont.getSize();
			}
			else return getFont().sameFont(font.getFont());
		}
		return false;
	}
}