package globalResources.graphics;

import java.awt.Color;
import java.awt.image.BufferedImage;

/**
 * A canvas that can be created from ImageData
 */
public class Image extends Drawable
{
	/**
	 * Creates an Image from ImageData
	 * @param data the data to be used
	 */
	public Image(ImageData data)
	{
		super(data.getWidth(), data.getHeight());
		for (int index = 0; index < getPixelCount(); index++)
		{
			setPixel(index, data.pixels[index]);
		}
	}
	
	/**
	 * Creates an Image from another Canvas
	 * @param canvas the canvas to be used
	 */
	public Image(Drawable canvas)
	{
		super(canvas.getWidth(), canvas.getHeight());
		for (int index = 0; index < getPixelCount(); index++)
		{
			setPixel(index, canvas.getPixel(index));
		}
	}
	
	public Image(BufferedImage image)
	{
		super(image.getWidth(), image.getHeight());
		for (int y = 0; y < image.getHeight(); y++) for (int x = 0; x < image.getWidth(); x++)
		{
			setPixel(x, y, image.getRGB(x, y));
		}
	}
	
	/**
	 * Creates an Image with the given dimension
	 * @param width width of the image
	 * @param height height of the image
	 */
	public Image(int width, int height)
	{
		super(width, height);
	}
	
	public Image(int width, int height, Color color)
	{
		super(width, height);
		int colorInt = color.getRGB();
		for (int index = 0; index < getPixelCount(); index++)
		{
			setPixel(index, colorInt);
		}
	}
}