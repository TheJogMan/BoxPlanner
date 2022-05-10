package globalResources.graphics;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Composite;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.Stroke;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import globalResources.graphics.jFont.CharacterData;
import globalResources.graphics.jFont.JFont;
import globalResources.richText.GeneralFont;
import globalResources.richText.RichColor;
import globalResources.richText.RichString;
import globalResources.richText.RichStringBuilder;
import globalResources.utilities.Vector;
import globalResources.utilities.VectorInt;
import globalResources.window.Window;
import jogLibrary.dataTypes.stack.PersistentStack;

/**
 * Handles draw commands
 */
public class Canvas
{
	Graphics2D graphics;
	Color clearColor;
	int width;
	int height;
	JFont font;
	double fontScale;
	PersistentStack <AffineTransform> stack = PersistentStack.create();
	
	/**
	 * Create a new canvas
	 * @param graphics Graphics2D object to use as a base
	 * @param shape shape of the Graphics2D object
	 */
	public Canvas(Graphics2D graphics, Shape shape)
	{
		clearColor = Color.BLACK;
		font = JFont.STANDARD;
		fontScale = 1.0;
		setGraphics(graphics, shape);
	}
	
	public void scale(Vector scale)
	{
		graphics.scale(scale.getX(), scale.getY());
	}
	
	public void translate(Vector translation)
	{
		graphics.translate(translation.getX(), translation.getY());
	}
	
	public void resetSettings()
	{
		graphics.setTransform(new AffineTransform());
	}
	
	public void pushSettings()
	{
		stack = stack.push(graphics.getTransform());
	}
	
	public void popSettings()
	{
		if (stack.entryCount() > 0)
		{
			graphics.setTransform(stack.peek());
			stack = (PersistentStack<AffineTransform>)stack.pop();
		}
	}
	
	/**
	 * Change the Graphics2D base of this canvas
	 * @param graphics Graphics2D object to use as a base
	 * @param shape shape of the Graphics2D object
	 */
	public void setGraphics(Graphics2D graphics, Shape shape)
	{
		Graphics2D oldGraphics = this.graphics;
		Color color = Color.WHITE;
		if (oldGraphics != null)
		{
			color = oldGraphics.getColor();
		}
		this.graphics = graphics;
		this.graphics.setColor(color);
		this.graphics.setClip(shape);
		width = shape.getBounds().width;
		height = shape.getBounds().height;
		clear();
		if (oldGraphics != null)
		{
			oldGraphics.dispose();
		}
	}
	
	/**
	 * Clear this canvas to the set clear color
	 * @see #setClearColor(Color)
	 */
	public void clear()
	{
		drawRect(0, 0, getWidth(), getHeight(), clearColor);
	}
	
	/**
	 * Sets the clear color for this canvas
	 * @param color the color to be used for clearing this canvas
	 */
	public void setClearColor(Color color)
	{
		clearColor = color;
	}
	
	/**
	 * Sets the default color used for drawing
	 * @param color the default color to be used for drawing
	 */
	public void setColor(Color color)
	{
		graphics.setColor(color);
	}
	
	/**
	 * Returns the current clear color
	 * @return the current clear color
	 * @see #setClearColor(Color)
	 */
	public Color getClearColor()
	{
		return clearColor;
	}
	
	/**
	 * Returns the current default color
	 * @return the current default color
	 * @see #setClearColor(Color)
	 */
	public Color getColor()
	{
		return graphics.getColor();
	}
	
	/**
	 * Returns the width of this canvas
	 * @return the width of this canvas
	 */
	public int getWidth()
	{
		return width;
	}
	
	/**
	 * Returns the height of this canvas
	 * @return the height of this canvas
	 */
	public int getHeight()
	{
		return height;
	}
	
	/**
	 * Returns the current JFont set for this canvas
	 * @return the current JFont set for this canvas
	 * @see #setFont(JFont)
	 */
	public JFont getFont()
	{
		return font;
	}
	
	/**
	 * Sets the current JFont for this canvas
	 * @param font the JFont to be used for this canvas
	 */
	public void setFont(JFont font)
	{
		this.font = font;
	}
	
	/**
	 * Gets the current scale used for text on this canvas
	 * @return font scale
	 * @see #setFontScale(double)
	 */
	public double getFontScale()
	{
		return fontScale;
	}
	
	/**
	 * Gets the height of the current JFont with the current scale
	 * @return height of the font
	 */
	public int getFontHeight()
	{
		return (int)((double)font.getCharacterHeight() * fontScale);
	}
	
	/**
	 * Gets the width of a character in the current JFont with the current scale
	 * @param character character to measure
	 * @return width of the character
	 */
	public int getCharacterWidth(char character)
	{
		return (int)((double)font.getCharacterWidth(character) * fontScale);
	}
	
	/**
	 * Gets the width of a string in the current JFont with the current scale
	 * @param text string to measure
	 * @return width of the string
	 * @see #getCharacterWidth(char)
	 */
	public int getStringWidth(String text)
	{
		return (int)((double)font.getStringWidth(text) * fontScale);
	}
	
	/**
	 * Sets the scale to be used for text on this canvas
	 * @param fontScale the new scale
	 */
	public void setFontScale(double fontScale)
	{
		this.fontScale = fontScale;
	}
	
	public void drawText(RichString text, int x, int y)
	{
		text.draw(this, x, y);
	}
	
	public void drawTextF(RichString text, int x, int y, int width, int height)
	{
		drawTextF(text, x, y, width, height, false);
	}
	
	public void drawTextF(RichString text, int x, int y, int width, int height, boolean center)
	{
		RichString[] words = text.breakIntoWords();
		List<RichString> lines = new ArrayList<RichString>();
		RichStringBuilder currentLine = new RichStringBuilder();
		int currentLineLength = 0;
		int currentWordNum = 0;
		while (currentWordNum < words.length)
		{
			if (words[currentWordNum].compareTo("\n") == 0)
			{
				lines.add(currentLine.build());
				currentLineLength = 0;
				currentLine.clear();;
				currentWordNum++;
			}
			else
			{
				int wordLength = words[currentWordNum].getWidth();
				if (currentLineLength + wordLength <= width)
				{
					currentLine.append(words[currentWordNum]);
					currentLineLength += wordLength;
					currentWordNum++;
				}
				else
				{
					if (currentLineLength == 0)
					{
						currentWordNum++;
					}
					else
					{
						lines.add(currentLine.build());
						currentLineLength = 0;
						currentLine.clear();
					}
				}
			}
		}
		lines.add(currentLine.build());
		
		while (lines.size() * getFontHeight() >= height)
		{
			lines.remove(lines.size() - 1);
		}
		
		int offY = 0;
		if (center)
		{
			int totalHeight = 0;
			for (Iterator<RichString> iterator = lines.iterator(); iterator.hasNext();)
			{
				RichString line = iterator.next();
				line.trimSpace();
				totalHeight += line.getHeight();
			}
			offY = (int)((double)height / 2.0 - (double)totalHeight / 2.0);
		}
		
		int yp = y + offY;
		for (Iterator<RichString> iterator = lines.iterator(); iterator.hasNext();)
		{
			RichString line = iterator.next();
			int offX = 0;
			if (center)
			{
				offX = (int)((double)width / 2.0 - (double)line.getWidth() / 2.0);
			}
			line.draw(this, x + offX, yp);
			yp += line.getHeight();
		}
	}
	
	/**
	 * Draws text with the current JFont formated to fit within the given region
	 * @param text the text to be drawn
	 * @param x x coordinate of the top left corner of the region
	 * @param y y coordinate of the top left corner of the region
	 * @param width width of the region
	 * @param height height of the region
	 * @param center whether the text will be centered within the region
	 * @see #setFont(JFont)
	 */
	public void drawTextF(String text, int x, int y, int width, int height, boolean center)
	{
		drawTextF(new RichString(text, new RichColor(graphics.getColor()), new GeneralFont(font)), x, y, width, height, center);
	}
	
	public void drawTextRF(String text, int x, int y, int width, int height, boolean center)
	{
		drawTextF(new RichString(text, new RichColor(graphics.getColor()), new GeneralFont(graphics.getFont())), x, y, width, height, center);
	}
	
	public void drawTextRF(String text, int x, int y, int width, int height, boolean center, Color color)
	{
		Color actualColor = getColor();
		setColor(color);
		drawTextRF(text, x, y, width, height, center);
		setColor(actualColor);
	}
	
	/**
	 * Draws text with the current JFont formated to fit within the given region
	 * @param text the text to be drawn
	 * @param x x coordinate of the top left corner of the region
	 * @param y y coordinate of the top left corner of the region
	 * @param width width of the region
	 * @param height height of the region
	 * @param center whether the text will be centered within the region
	 * @param color the color to be used for the text
	 * @see #drawTextF(String, int, int, int, int, boolean)
	 * @see #setFont(JFont)
	 */
	public void drawTextF(String text, int x, int y, int width, int height, boolean center, Color color)
	{
		Color actualColor = getColor();
		setColor(color);
		drawTextF(text, x, y, width, height, center);
		setColor(actualColor);
	}
	
	/**
	 * Draws text on the canvas with the current JFont
	 * @param text the text to be drawn
	 * @param offX where the text will be drawn
	 * @param offY where the text will be draw
	 * @see #setFont(JFont)
	 */
	public void drawText(String text, int offX, int offY)
	{
		int offset = 0;
		for (int i = 0; i < text.length(); i++)
		{
			CharacterData data = font.getCharacterData(text.charAt(i));
			int yOrigin = 0;
			if (!data.isUpperCase())
			{
				yOrigin = font.getVerticalSeperator() + 1;
			}
			int yp = offY;
			for (int y = yOrigin; y < yOrigin + font.getCharacterHeight(); y++)
			{
				int xp = offX + offset;
				for (int x = data.getOffset(); x < data.getOffset() + data.getWidth(); x++)
				{
					int pixelColor = font.getFontImage().getPixel(x, y);
					if (pixelColor == 0xffffffff)
					{
						if (xp >= 0 && xp < getWidth() && yp >= 0 && yp < getHeight())
						{
							drawRect(xp,yp,(int)fontScale,(int)fontScale);
						}
					}
					xp += fontScale;
				}
				yp += fontScale;
			}
			offset += data.getWidth() * fontScale;
		}
	}
	
	/**
	 * Draws text on the canvas with the current JFont
	 * @param text the text to be drawn
	 * @param x where the text will be drawn
	 * @param y where the text will be draw
	 * @param color the color to be used to draw the text
	 * @see #drawText(String, int, int)
	 * @see #setFont(JFont)
	 */
	public void drawText(String text, int x, int y, Color color)
	{
		Color actualColor = getColor();
		setColor(color);
		drawText(text, x, y);
		setColor(actualColor);
	}
	
	/**
	 * Sets the color of a pixel to the default color of this canvas
	 * @param x pixel x
	 * @param y pixel y
	 */
	public void setPixel(int x, int y)
	{
		drawRect(x, y, 1, 1);
	}
	
	/**
	 * Sets the color of a pixel to the given color
	 * @param x pixel x
	 * @param y pixel y
	 * @param color color that the pixel will be set to
	 */
	public void setPixel(int x, int y, Color color)
	{
		drawRect(x, y, 1, 1, color);
	}
	
	/**
	 * Sets the font to be used by this canvas
	 * @param font font to be used by this canvas
	 */
	public void setFontR(Font font)
	{
		graphics.setFont(font);
	}
	
	/**
	 * Gets the font used by this canvas
	 * @return the font used by this canvas
	 * @see #setFontR(Font)
	 */
	public Font getFontR()
	{
		return graphics.getFont();
	}
	
	public int getFontHeightR()
	{
		FontMetrics fontMetrics = graphics.getFontMetrics();
		return fontMetrics.getHeight();
	}
	
	public int getStringLengthR(String text)
	{
		FontMetrics fontMetrics = graphics.getFontMetrics();
		return (int)fontMetrics.getStringBounds(text, graphics).getWidth();
	}
	
	/**
	 * Draws text with the current font
	 * @param text text to be drawn
	 * @param x where the text will be drawn
	 * @param y where the text will be drawn
	 * @see #setFontR(Font)
	 */
	public void drawTextR(String text, int x, int y)
	{
		FontMetrics fontMetrics = graphics.getFontMetrics();
		y += fontMetrics.getAscent();
		graphics.drawString(text, x, y);
	}
	
	/**
	 * Draws text with the current font
	 * @param text text to be drawn
	 * @param x where the text will be drawn
	 * @param y where the text will be drawn
	 * @param color color to be used to draw the text
	 * @see #drawTextR(String, int, int)
	 * @see #setFontR(Font)
	 */
	public void drawTextR(String text, int x, int y, Color color)
	{
		Color actualColor = getColor();
		setColor(color);
		drawTextR(text, x, y);
		setColor(actualColor);
	}
	
	/**
	 * Draws a circle with a given color
	 * @param x center point of the circle
	 * @param y center point of the circle
	 * @param radius radius of the circle
	 * @param fill whether the circle should be filled
	 * @param color the color to be used
	 * @see #drawCircle(int, int, int, boolean)
	 */
	public void drawCircle(int x, int y, int radius, boolean fill, Color color)
	{
		Color actualColor = getColor();
		setColor(color);
		drawCircle(x, y, radius, fill);
		setColor(actualColor);
	}
	
	/**
	 * Draws a circle
	 * @param x center point of the circle
	 * @param y center point of the circle
	 * @param radius radius of the circle
	 * @param fill whether the circle should be filled
	 */
	public void drawCircle(int x, int y, int radius, boolean fill)
	{
		if (fill)
		{
			graphics.fillOval(x - radius, y - radius, radius * 2, radius * 2);
		}
		else
		{
			graphics.drawOval(x - radius, y - radius, radius * 2, radius * 2);
		}
	}
	
	/**
	 * Draws a filled circled
	 * @param x center point of the circle
	 * @param y center point of the circle
	 * @param radius radius of the circle
	 * @see #drawCircle(int, int, int, boolean)
	 */
	public void drawCircle(int x, int y, int radius)
	{
		drawCircle(x, y, radius, true);
	}
	
	/**
	 * Draws a rectangle
	 * @param x top left corner of the rectangle
	 * @param y top left corner of the rectangle
	 * @param width width of the rectangle
	 * @param height height of the rectangle
	 * @param fill whether the rectangle should be filled
	 */
	public void drawRect(int x, int y, int width, int height, boolean fill)
	{
		if (!fill)
		{
			drawLine(x, y, x + width - 1, y);
			drawLine(x, y + 1, x, y + height - 1);
			drawLine(x + width - 1, y + 1, x + width - 1, y + height - 2);
			drawLine(x + 1, y + height - 1, x + width - 1, y + height - 1);
		}
		else
		{
			drawRect(x, y, width, height);
		}
	}
	
	/**
	 * Draws a rectangle with the given color
	 * @param x top left corner of the rectangle
	 * @param y top left corner of the rectangle
	 * @param width width of the rectangle
	 * @param height height of the rectangle
	 * @param fill whether the rectangle should be filled
	 * @param color the color to be used
	 * @see #drawRect(int, int, int, int, boolean)
	 */
	public void drawRect(int x, int y, int width, int height, boolean fill, Color color)
	{
		Color actualColor = getColor();
		setColor(color);
		drawRect(x, y, width, height, fill);
		setColor(actualColor);
	}
	
	public void drawRect(int x, int y, int width, int height, boolean fill, Color color, float opacity)
	{
		Composite previousComposite = graphics.getComposite();
		AlphaComposite ac = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, opacity);
		graphics.setComposite(ac);
		Color actualColor = getColor();
		setColor(color);
		drawRect(x, y, width, height, fill);
		setColor(actualColor);
		graphics.setComposite(previousComposite);
	}
	
	public void drawPoly(Color color, boolean fill, Vector... vertecies)
	{
		drawPoly(fill, color, vertecies);
	}
	
	public void drawPoly(boolean fill, Color color, Iterable<Vector> vertecies, int vertexCount)
	{
		int[] xPoints = new int[vertexCount];
		int[] yPoints = new int[vertexCount];
		int index = 0;
		for (Iterator<Vector> iterator = vertecies.iterator(); iterator.hasNext();)
		{
			Vector vector = iterator.next();
			xPoints[index] = (int)vector.getX();
			yPoints[index] = (int)vector.getY();
			index++;
		}
		Color actualColor = getColor();
		setColor(color);
		if (fill) graphics.fillPolygon(xPoints, yPoints, vertexCount);
		else graphics.drawPolygon(xPoints, yPoints, vertexCount);
		setColor(actualColor);
	}
	
	private static class ArrayIterable<Type> implements Iterable<Type>
	{
		Type[] array;
		
		private ArrayIterable(Type[] array)
		{
			this.array = array;
		}
		
		@Override
		public Iterator<Type> iterator()
		{
			return new ArrayIterator();
		}
		
		private class ArrayIterator implements Iterator<Type>
		{
			int index = -1;
			
			@Override
			public boolean hasNext()
			{
				return index < array.length - 1;
			}
			
			@Override
			public Type next()
			{
				index++;
				return array[index];
			}
		}
	}
	
	public void drawPoly(boolean fill, Color color, Vector[] vertecies)
	{
		drawPoly(fill, color, new ArrayIterable<Vector>(vertecies), vertecies.length);
	}
	
	/**
	 * Draws a filled rectangle
	 * @param x top left corner of the rectangle
	 * @param y top left corner of the rectangle
	 * @param width width of the rectangle
	 * @param height height of the rectangle
	 * @see #drawRect(int, int, int, int, boolean)
	 */
	public void drawRect(int x, int y, int width, int height)
	{
		graphics.fillRect(x, y, width, height);
	}
	
	/**
	 * Draws a filled rectangle with the given color
	 * @param x top left corner of the rectangle
	 * @param y top left corner of the rectangle
	 * @param width width of the rectangle
	 * @param height height of the rectangle
	 * @param color color to be used
	 * @see #drawRect(int, int, int, int)
	 */
	public void drawRect(int x, int y, int width, int height, Color color)
	{
		Color actualColor = getColor();
		setColor(color);
		drawRect(x, y, width, height);
		setColor(actualColor);
	}
	
	/**
	 * Draws a portion of another canvas onto this one
	 * @param canvas the other canvas to be copied from
	 * @param x the destination point on this canvas
	 * @param y the destination point on this canvas
	 * @param originX the origin point on the other canvas
	 * @param originY the origin point on the other canvas
	 * @param width the width of the region to be copied
	 * @param height the height of the region to be copied
	 */
	public void drawPortion(Drawable canvas, int x, int y, int originX, int originY, int width, int height)
	{
		drawPortion(canvas, x, y, width, height, originX, originY, width, height);
	}
	
	public void drawPortion(Drawable canvas, int x, int y, int width, int height, int originX, int originY, int originWidth, int originHeight)
	{
		if (canvas != null) graphics.drawImage(canvas.getImage(), x, y, x + width, y + height, originX, originY, originX + originWidth, originY + originHeight, null);
	}
	
	/**
	 * Draws a drawable object onto this canvas
	 * @param drawable the object to be drawn
	 * @param position the position where the object will be drawn
	 * @see #draw(Drawable, int, int)
	 */
	public void draw(Drawable drawable, VectorInt position)
	{
		draw(drawable, position.getX(), position.getY());
	}
	
	/**
	 * Draws another canvas onto this one
	 * @param canvas the other canvas to be drawn
	 * @param x where the canvas will be drawn
	 * @param y where the canvas will be drawn
	 * @see #draw(Drawable, int, int, double, double)
	 */
	public void draw(Drawable canvas, int x, int y)
	{
		draw(canvas, x, y, 1.0, 1.0);
	}
	
	public void drawF(Drawable canvas, int x, int y, int width, int height, boolean maintainRatio)
	{
		int xOff = 0;
		int yOff = 0;
		double xScale = (double)width / (double)canvas.getWidth();
		double yScale = (double)height / (double)canvas.getHeight();
		if (maintainRatio)
		{
			if (canvas.getWidth() > canvas.getHeight())
			{
				yScale = xScale;
				yOff = (height - (int)(canvas.getHeight() * yScale)) / 2;
			}
			else
			{
				xScale = yScale;
				xOff = (width - (int)(canvas.getWidth() * xScale)) / 2;
			}
		}
		draw(canvas, x + xOff, y + yOff, xScale, yScale);
	}
	
	/**
	 * Draws another canvas onto this one with the given scale factors
	 * @param canvas the other canvas to be drawn
	 * @param x where the canvas will be drawn
	 * @param y where the canvas will be drawn
	 * @param xScale the scale factor for the x axis
	 * @param yScale the scale factor for the y axis
	 */
	public void draw(Drawable canvas, int x, int y, double xScale, double yScale)
	{
		if (canvas != null)	graphics.drawImage(canvas.getImage(), x, y, (int)(canvas.getWidth() * xScale), (int)(canvas.getHeight() * yScale), null);
	}
	
	public void draw(Drawable canvas, int x, int y, double xScale, double yScale, float opacity)
	{
		if (canvas != null)
		{
			Composite previousComposite = graphics.getComposite();
			AlphaComposite ac = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, opacity);
			graphics.setComposite(ac);
			graphics.drawImage(canvas.getImage(), x, y, (int)(canvas.getWidth() * xScale), (int)(canvas.getHeight() * yScale), null);
			graphics.setComposite(previousComposite);
		}
	}
	
	public void draw(BufferedImage image, int x, int y, double xScale, double yScale)
	{
		if (image != null) graphics.drawImage(image, x, y, (int)(image.getWidth() * xScale), (int)(image.getHeight() * yScale), null);
	}
	
	public void draw(BufferedImage image, int x, int y, double xScale, double yScale, float opacity)
	{
		if (image != null)
		{
			Composite previousComposite = graphics.getComposite();
			AlphaComposite ac = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, opacity);
			graphics.setComposite(ac);
			graphics.drawImage(image, x, y, (int)(image.getWidth() * xScale), (int)(image.getHeight() * yScale), null);
			graphics.setComposite(previousComposite);
		}
	}
	
	public void drawFromWindow(Window window, int x, int y, int sourceX, int sourceY, int sourceWidth, int sourceHeight, double xScale, double yScale)
	{
		if (window != null) graphics.drawImage(window.getCurrentImage().getSnapshot(), x, y, (int)(x + sourceWidth * sourceX), (int)(y + sourceHeight * sourceY),
												sourceX, sourceY, sourceX + sourceWidth, sourceY + sourceHeight, null);
	}
	
	/**
	 * Draws a line between two points
	 * @param startX start point
	 * @param startY start point
	 * @param endX end point
	 * @param endY end point
	 */
	public void drawLine(int startX, int startY, int endX, int endY)
	{
		graphics.drawLine(startX, startY, endX, endY);
	}
	
	public void setStroke(Stroke stroke)
	{
		graphics.setStroke(stroke);
	}
	
	/**
	 * Draws a line between two points with the given color
	 * @param startX start point
	 * @param startY start point
	 * @param endX end point
	 * @param endY end point
	 * @param color the color to be used
	 * @see #drawLine(int, int, int, int)
	 */
	public void drawLine(int startX, int startY, int endX, int endY, Color color)
	{
		Color actualColor = getColor();
		setColor(color);
		drawLine(startX, startY, endX, endY);
		setColor(actualColor);
	}
}