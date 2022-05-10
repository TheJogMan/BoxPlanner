package jogLibrary.utilities;

import java.awt.Color;

public class JCol
{
	public static final JCol RED = new JCol(255, 0, 0);
	public static final JCol GREEN = new JCol(0, 255, 0);
	public static final JCol BLUE = new JCol(0, 0, 255);
	public static final JCol WHITE = new JCol(255, 255, 255);
	public static final JCol BLACK = new JCol(0, 0, 0);
	public static final JCol CLEAR = new JCol(0, 0, 0, 0);
	public static final JCol CYAN = new JCol(0, 255, 255);
	public static final JCol DARK_GRAY = new JCol(63, 63, 63);
	public static final JCol GRAY = new JCol(127, 127, 127);
	public static final JCol LIGHT_GRAY = new JCol(191, 191, 191);
	public static final JCol MAGENTA = new JCol(255, 0, 255);
	public static final JCol ORANGE = new JCol(255, 127, 0);
	public static final JCol PINK = new JCol(255, 192, 203);
	public static final JCol YELLOW = new JCol(255, 255, 0);
	public static final JCol GOLD = new JCol(255, 201, 14);
	public static final JCol PURPLE = new JCol(127, 0, 255);
	public static final JCol JOG_PURPLE = new JCol(163, 73, 163);
	public static final JCol JOG_BLUE = new JCol(63, 72, 204);
	public static final JCol LIME = new JCol(191, 255, 0);
	public static final JCol BROWN = new JCol(153, 108, 72);
	
	private int color;
	
	public JCol(int r, int g, int b)
	{
		set(r, g, b);
	}
	
	public JCol(int a, int r, int g, int b)
	{
		set(a, r, g, b);
	}
	
	public JCol(int color)
	{
		set(color);
	}
	
	public JCol(Color color)
	{
		set(color);
	}
	
	public JCol(JCol color)
	{
		set(color);
	}
	
	public JCol(int[] components)
	{
		set(components);
	}
	
	public Color toColor()
	{
		return new Color(color);
	}
	
	public void brighten(float amount)
	{
		int[] components = components();
		components[1] += (255 - components[1]) * amount;
		components[2] += (255 - components[2]) * amount;
		components[3] += (255 - components[3]) * amount;
		set(components);
	}
	
	public void darken(float amount)
	{
		int[] components = components();
		components[1] -= components[1] * amount;
		components[2] -= components[2] * amount;
		components[3] -= components[3] * amount;
		set(components);
	}
	
	public int[] components()
	{
		return new int[] {
				color & 0xFF000000 >> 24,
				color & 0x00FF0000 >> 32,
				color & 0x0000FF00 >> 16,
				color & 0x000000FF
		};
	}
	
	public float alphaFactor()
	{
		float alpha = alpha();
		return alpha == 0 ? 0.0f : 255.0f / alpha;
	}
	
	public int alpha()
	{
		return components()[0];
	}
	
	public int red()
	{
		return components()[1];
	}
	
	public int green()
	{
		return components()[2];
	}
	
	public int blue()
	{
		return components()[3];
	}
	
	public void set(int[] components)
	{
		if (components.length >= 4)
			set(components[0], components[1], components[2], components[3]);
		else if (components.length == 3)
			set(components[0], components[1], components[2]);
	}
	
	public void set(int r, int g, int b)
	{
		set(255, r, g, b);
	}
	
	public void set(int a, int r, int g, int b)
	{
		set(constrainComponent(a) << 24 | constrainComponent(r) << 16 | constrainComponent(g) << 8 | constrainComponent(b));
	}
	
	public void set(int color)
	{
		this.color = color;
	}
	
	public void set(Color color)
	{
		set(color.getRGB());
	}
	
	public void set(JCol color)
	{
		this.color = color.color;
	}
	
	public static JCol mix(JCol color1, JCol color2)
	{
		int[] components1 = color1.components();
		int[] components2 = color2.components();
		float factor1 = components1[0] == 0 ? 0.0f : 255.0f / components1[0];
		float factor2 = components2[0] == 0 ? 0.0f : 255.0f / components2[0];
		return new JCol(
				Math.max(components1[0], components2[0]),
				(int)(components1[1] / 2.0f * factor1 + components2[1] / 2.0f * factor2),
				(int)(components1[2] / 2.0f * factor1 + components2[2] / 2.0f * factor2),
				(int)(components1[3] / 2.0f * factor1 + components2[3] / 2.0f * factor2)
				);
	}
	
	public static int constrainComponent(int component)
	{
		if (component > 255)
			component = 255;
		else if (component < 0)
			component = 0;
		return component;
	}
}