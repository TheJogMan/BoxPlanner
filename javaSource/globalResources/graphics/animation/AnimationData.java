package globalResources.graphics.animation;

import java.awt.Color;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;

import globalResources.graphics.ImageData;
import globalResources.graphics.animation.gifReader.GIFReader;
import globalResources.graphics.animation.gifReader.ImageFrame;
import globalResources.utilities.ByteConverter;
import globalResources.utilities.FIO;

public class AnimationData
{
	int width;
	int height;
	AnimationFrameData[] frameData;
	
	public AnimationData(SourceType sourceType, String path)
	{
		boolean loadFailed = false;
		if (sourceType.equals(SourceType.ANIMATION_FILE))
		{
			if (path.charAt(0) == '/')
			{
				try
				{
					InputStream in = getClass().getResourceAsStream(path); 
					byte[] data = new byte[in.available()];
					in.read(data);
					in.close();
					loadByteData(data);
				}
				catch (Exception e)
				{
					loadFailed = true;
				}
			}
			else
			{
				File file = new File(path);
				if (FIO.canReadBytes(file)) loadByteData(FIO.readBytes(new File(path)));
				else loadFailed = true;
			}
		}
		else if (sourceType.equals(SourceType.GIF_FILE))
		{
			try
			{
				InputStream stream;
				if (path.charAt(0) == '/')
				{
					stream = getClass().getResourceAsStream(path);
				}
				else
				{
					stream = new FileInputStream(new File(path));
				}
				ImageFrame[] frames = GIFReader.readGif(stream);
				stream.close();
				if (frames.length > 0)
				{
					width = 0;
					height = 0;
					for (int index = 0; index < frames.length; index++)
					{
						if (frames[index].getWidth() > width) width = frames[index].getWidth();
						if (frames[index].getHeight() > height) height = frames[index].getHeight();
					}
					frameData = new AnimationFrameData[frames.length];
					for (int index = 0; index < frames.length; index++) frameData[index] = new AnimationFrameData(frames[index].getDelay(), new ImageData(frames[index].getImage()));
				}
				else loadFailed = true;
			}
			catch (Exception e)
			{
				loadFailed = true;
			}
		}
		else if (sourceType.equals(SourceType.ANIMATION_IMAGE))
		{
			ImageData data = new ImageData(path);
			int frameHeight = 0;
			int millisecondsPerFrame = 0;
			for (int x = 0; x < data.getWidth(); x++)
			{
				Color color = new Color(data.getPixel(x));
				frameHeight += color.getRed() + color.getGreen() + color.getBlue();
				color = new Color(data.getPixel(x + data.getWidth()));
				millisecondsPerFrame += color.getRed() + color.getGreen() + color.getBlue();
			}
			
			int y = 2;
			ArrayList<AnimationFrameData> frames = new ArrayList<AnimationFrameData>();
			while (y + frameHeight <= data.getHeight())
			{
				int start = y * data.getWidth();
				int[] pixels = new int[data.getWidth() * frameHeight];
				for (int index = 0; index < pixels.length; index++) pixels[index] = data.getPixel(index + start);
				frames.add(new AnimationFrameData(millisecondsPerFrame, new ImageData(pixels, data.getWidth(), frameHeight)));
				y += frameHeight;
			}
			width = data.getWidth();
			height = frameHeight;
			this.frameData = frames.toArray(new AnimationFrameData[frames.size()]);
		}
		else if (sourceType.equals(SourceType.DYNAMIC_ANIMATION_IMAGE))
		{
			ImageData data = new ImageData(path);
			int frameHeight = 0;
			for (int x = 0; x < data.getWidth(); x++)
			{
				Color color = new Color(data.getPixel(x));
				frameHeight += color.getRed() + color.getGreen() + color.getBlue();
			}
			
			int y = 1;
			ArrayList<AnimationFrameData> frames = new ArrayList<AnimationFrameData>();
			while (y + frameHeight + 1 <= data.getHeight())
			{
				int duration = 0;
				int start = y * data.getWidth();
				for (int x = 0; x < data.getWidth(); x++)
				{
					Color color = new Color(data.getPixel(start + x));
					duration += color.getRed() + color.getGreen() + color.getBlue();
				}
				start += data.getWidth();
				int[] pixels = new int[data.getWidth() * frameHeight];
				for (int index = 0; index < pixels.length; index++) pixels[index] = data.getPixel(index + start);
				frames.add(new AnimationFrameData(duration, new ImageData(pixels, data.getWidth(), frameHeight)));
				y += frameHeight + 1;
			}
			width = data.getWidth();
			height = frameHeight;
			this.frameData = frames.toArray(new AnimationFrameData[frames.size()]);
		}
		else loadFailed = true;
		if (loadFailed)
		{
			width = 1;
			height = 1;
			frameData = new AnimationFrameData[] {new AnimationFrameData(1, new ImageData(new int[] {0}, 1, 1))};
		}
		
		//System.out.println(width + "x" + height + ", " + frameData.length);
		//for (int index = 0; index < frameData.length; index++)
		//{
		//	System.out.println(frameData[index].duration);
		//}
	}
	
	public AnimationData(Animation animation)
	{
		this.width = animation.getWidth();
		this.height = animation.getHeight();
		frameData = new AnimationFrameData[animation.frames.length];
		for (int index = 0; index < frameData.length; index++) frameData[index] = new AnimationFrameData(animation.frames[index]);
	}
	
	public AnimationData(byte[] data)
	{
		loadByteData(data);
	}
	
	private void loadByteData(byte[] data)
	{
		byte[][] fullData = ByteConverter.to2DByteArray(data);
		width = ByteConverter.toInteger(fullData[0]);
		height = ByteConverter.toInteger(fullData[1]);
		frameData = new AnimationFrameData[fullData.length - 2];
		for (int index = 0; index < frameData.length; index++) frameData[index] = new AnimationFrameData(fullData[2 + index]);
	}
	
	public int getWidth()
	{
		return width;
	}
	
	public int getHeight()
	{
		return height;
	}
	
	public byte[] getAsByteData()
	{
		byte[][] data = new byte[frameData.length + 2][];
		data[0] = ByteConverter.fromInteger(width);
		data[1] = ByteConverter.fromInteger(height);
		for (int index = 0; index < frameData.length; index++) data[2 + index] = frameData[index].getAsByteData();
		return ByteConverter.from2DByteArray(data);
	}
	
	public enum SourceType
	{
		ANIMATION_IMAGE, ANIMATION_FILE, DYNAMIC_ANIMATION_IMAGE, GIF_FILE;
	}
}