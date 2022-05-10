package globalResources.graphics;

import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.awt.image.DataBuffer;
import java.awt.image.DataBufferByte;
import java.awt.image.DataBufferInt;

import globalResources.utilities.VectorInt;

/**
 * The base object for anything that can be drawn to a canvas
 */
public class Drawable
{
	BufferedImage image;
	Canvas canvas;
	DataBuffer pixels;
	boolean byteBuffer;
	
	/**
	 * Create a new drawable
	 * @param width width of the drawable
	 * @param height height of the drawable
	 */
	public Drawable(int width, int height)
	{
		this(new VectorInt(width, height));
	}
	
	/**
	 * Create a new drawable
	 * @param dimensions dimensions of the new drawable
	 */
	public Drawable(VectorInt dimensions)
	{
		image = new BufferedImage(dimensions.getX(), dimensions.getY(), BufferedImage.TYPE_INT_ARGB);
		pixels = image.getRaster().getDataBuffer();
		byteBuffer = false;
		clear();
		canvas = new Canvas(image.createGraphics(), new Rectangle(0, 0, dimensions.getX(), dimensions.getY()));
	}
	
	public Drawable(BufferedImage image)
	{
		this.image = image;
		pixels = image.getRaster().getDataBuffer();
		byteBuffer = pixels instanceof DataBufferByte;
		canvas = new Canvas(image.createGraphics(), new Rectangle(0, 0, image.getWidth(), image.getHeight()));
	}
	
	/**
	 * Gets the BufferedImage for this drawable
	 * @return the BufferedImage for this drawable
	 */
	public BufferedImage getImage()
	{
		return image;
	}
	
	/**
	 * Get the canvas for this drawable
	 * @return this drawable's canvas
	 */
	public Canvas getCanvas()
	{
		return canvas;
	}
	
	/**
	 * Get the width of the drawable
	 * @return width of the drawable
	 */
	public int getWidth()
	{
		return image.getWidth();
	}
	
	/**
	 * Get the height of the drawable
	 * @return height of the drawable
	 */
	public int getHeight()
	{
		return image.getHeight();
	}
	
	/**
	 * Clear this drawable to fully transparent black
	 */
	public void clear()
	{
		for (int index = 0; index < getPixelCount(); index++)
		{
			setPixel(index, 0);
		}
	}
	
	public int getPixelCount()
	{
		return image.getWidth() * image.getHeight();
	}
	
	public int getIndexPixel(int index)
	{
		if (inBounds(index)) return getPixel(index);
		else return 0;
	}
	
	protected int getPixel(int index)
	{
		if (byteBuffer) return ((DataBufferByte)pixels).getData()[index];
		else return ((DataBufferInt)pixels).getData()[index];
	}
	
	protected void setPixel(int index, int value)
	{
		if (byteBuffer) ((DataBufferByte)pixels).getData()[index] = (byte)value;
		else ((DataBufferInt)pixels).getData()[index] = value;
	}
	
	boolean inBounds(int index)
	{
		if (index >= 0 && index < getPixelCount())
		{
			return true;
		}
		return false;
	}
	
	boolean inBounds(int x, int y)
	{
		if (x >= 0 && x < getWidth() && y >= 0 && y < getHeight())
		{
			return true;
		}
		return false;
	}
	
	int convertIndex(int x, int y)
	{
		return x + y * getWidth();
	}
	
	/**
	 * Get the color of a pixel
	 * @param x x coordinate of the pixel
	 * @param y y coordinate of the pixel
	 * @return ARGB color of the pixel
	 */
	public int getPixel(int x, int y)
	{
		if (inBounds(x, y))
		{
			return getPixel(convertIndex(x, y));
		}
		return 0;
	}
	
	/**
	 * Set the color of a pixel
	 * @param x x coordinate of the pixel
	 * @param y y coordinate of the pixel
	 * @param color ARGB color of the pixel
	 */
	public void setPixel(int x, int y, int color)
	{
		if (inBounds(x, y))
		{
			setPixel(convertIndex(x, y), color);
		}
	}
	
	/**
	 * Clear a region of this drawable to transparent black
	 * @param x x coordinate of the top left corner
	 * @param y y coordinate of the top left corner
	 * @param width width of the region
	 * @param height height of the region
	 */
	public void clearRect(int x, int y, int width, int height)
	{
		int color = 0x00000000;
		for (int px = x; px < x + width; px++)
		{
			for (int py = y; py < y + height; py++)
			{
				setPixel(px, py, color);
			}
		}
	}
}