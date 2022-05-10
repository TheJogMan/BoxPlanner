package globalResources.richText;

import java.awt.Color;

public class RichColor
{
	public static final RichColor DARK_RED = new RichColor(11141120, '4');
	public static final RichColor RED = new RichColor(16733525, 'c');
	public static final RichColor ORANGE = new RichColor(16755200, '6');
	public static final RichColor YELLOW = new RichColor(16777045, 'e');
	public static final RichColor GREEN = new RichColor(43520, '2');
	public static final RichColor LIME = new RichColor(5635925, 'a');
	public static final RichColor AQUA = new RichColor(5636095, 'b');
	public static final RichColor CYAN = new RichColor(43690, '3');
	public static final RichColor BLUE = new RichColor(170, '1');
	public static final RichColor LIGHT_BLUE = new RichColor(5592575, '9');
	public static final RichColor PINK = new RichColor(16733695, 'd');
	public static final RichColor MAGENTA = new RichColor(11141290, '5');
	public static final RichColor WHITE = new RichColor(16777215, 'f');
	public static final RichColor GRAY = new RichColor(11184810, '7');
	public static final RichColor DARK_GRAY = new RichColor(5592405, '8');
	public static final RichColor BLACK = new RichColor(0, 0, 0, 255, '0');
	public static final RichColor CLEAR = new RichColor(0);
	public static final RichColor JOG_YELLOW = new RichColor(255, 201, 14);
	public static final RichColor JOG_BLUE = new RichColor(63, 72, 204);
	public static final RichColor JOG_PURPLE = new RichColor(163, 73, 164);
	public static final RichColor JOG_GRAY = new RichColor(127, 127, 127);
	
	public static final char characterCodePrefix = '§';
	public static final char characterCodeItalic = 'o';
	public static final char characterCodeUnderline = 'n';
	public static final char characterCodeStrikethrough = 'm';
	public static final char characterCodeBold = 'I';
	public static final char characterCodeObfuscate = 'k';
	public static final char characterCodeReset = 'r';
	
	Color color = Color.WHITE;
	char characterCode = 'r';
	
	public RichColor(int color, char characterCode)
	{
		this.color = new Color(color);
		this.characterCode = characterCode;
	}
	
	public RichColor(int color)
	{
		this.color = new Color(color);
	}
	
	public RichColor(int r, int g, int b, int a, char characterCode)
	{
		color = new Color(r, g, b, a);
		this.characterCode = characterCode;
	}
	
	public RichColor(int r, int g, int b, int a)
	{
		color = new Color(r, g, b, a);
	}
	
	public RichColor(int r, int g, int b, char characterCode)
	{
		color = new Color(r, g, b);
		this.characterCode = characterCode;
	}
	
	public RichColor(int r, int g, int b)
	{
		color = new Color(r, g, b);
	}
	
	public RichColor(Color color)
	{
		this.color = color;
	}
	
	public Color toColor()
	{
		return new Color(color.getRGB());
	}
	
	public String toInlineCode()
	{
		return characterCodePrefix + "" + characterCode;
	}
	
	@Override
	public RichColor clone()
	{
		return new RichColor(color.getRGB(), characterCode);
	}
}