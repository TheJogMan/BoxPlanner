package globalResources.graphics.animation;

import globalResources.graphics.ImageData;
import globalResources.utilities.ByteConverter;

class AnimationFrameData
{
	int duration;
	ImageData data;
	
	AnimationFrameData(int duration, ImageData data)
	{
		this.duration = duration;
		this.data = data;
	}
	
	AnimationFrameData(AnimationFrame frame)
	{
		this.duration = frame.duration;
		this.data = new ImageData(frame);
	}
	
	AnimationFrameData(byte[] data)
	{
		byte[] imageData = new byte[data.length - 4];
		byte[] durationData = new byte[] {data[0], data[1], data[2], data[3]};
		for (int index = 0; index < imageData.length; index++) imageData[index] = data[index + 4];
		this.data = new ImageData(imageData);
		duration = ByteConverter.toInteger(durationData);
	}
	
	byte[] getAsByteData()
	{
		byte[] imageData = data.getAsBytes();
		byte[] durationData = ByteConverter.fromInteger(duration);
		byte[] total = new byte[imageData.length + 4];
		for (int index = 0; index < 4; index++) total[index] = durationData[index];
		for (int index = 0; index < imageData.length; index++) total[index + 4] = imageData[index];
		return total;
	}
	
	int getWidth()
	{
		return data.getWidth();
	}
	
	int getHeight()
	{
		return data.getHeight();
	}
}