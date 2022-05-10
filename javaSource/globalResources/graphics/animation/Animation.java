package globalResources.graphics.animation;

import java.awt.image.BufferedImage;

import globalResources.graphics.Canvas;
import globalResources.graphics.Drawable;
import globalResources.graphics.animation.AnimationFrame.WrongSizeException;
import globalResources.utilities.Logger;

public class Animation extends Drawable
{
	AnimationFrame[] frames;
	long originTime = 0;
	boolean frozen = false;
	
	public Animation(AnimationData data)
	{
		super(data.width, data.height);
		frames = new AnimationFrame[data.frameData.length];
		for (int index = 0; index < frames.length; index++)
		{
			try
			{
				frames[index] = new AnimationFrame(this, data.frameData[index]);
			}
			catch (WrongSizeException e)
			{
				Logger.defaultLogger.log("Creating blank frame.", e);
				frames[index] = new AnimationFrame(this);
			}
		}
		restart();
	}
	
	public Animation(int width, int height)
	{
		super(width, height);
		frames = new AnimationFrame[0];
		createFrame();
		restart();
	}
	
	@Override
	public Animation clone()
	{
		Animation animation = new Animation(getWidth(), getHeight());
		animation.frames = new AnimationFrame[0];
		for (int index = 0; index < frames.length; index++) animation.addFrame(frames[index]);
		animation.originTime = originTime;
		animation.frozen = frozen;
		return animation;
	}
	
	public int getFrameIndex(AnimationFrame frame)
	{
		if (!frame.animation.equals(this)) return -1;
		else
		{
			for (int index = 0; index < frames.length; index++) if (frames[index].equals(frame)) return index;
			return -1;
		}
	}
	
	public boolean isPaused()
	{
		return frozen;
	}
	
	public void pause()
	{
		if (!frozen)
		{
			frozen = true;
			originTime = System.currentTimeMillis() - getCurrentProgress();
		}
	}
	
	public void resume()
	{
		frozen = false;
	}
	
	public void setOriginTime(long originTime)
	{
		this.originTime = originTime;
	}
	
	public void restart()
	{
		originTime = System.currentTimeMillis();
	}
	
	public void adjustTime(long deltaTime)
	{
		originTime += deltaTime;
	}
	
	public void setTimeToFrame(int index)
	{
		originTime = System.currentTimeMillis() - getTimeOfFrame(index);
	}
	
	public long getTimeOfFrame(int frameIndex)
	{
		if (frameIndex < 0 || frameIndex >= frames.length) return 0;
		long time = 0;
		for (int index = 0; index < frameIndex; index++) time += frames[index].duration;
		return time;
	}
	
	public AnimationFrame createFrame()
	{
		return createFrame(frames.length);
	}
	
	public AnimationFrame createFrame(int index)
	{
		AnimationFrame frame = new AnimationFrame(this);
		addFrame(frame, index);
		return frame;
	}
	
	long getCurrentProgress()
	{
		long time = getOriginTime();
		long length = getAnimationLength();
		time -= length * (time / length);
		return time;
	}
	
	long getOriginTime()
	{
		if (frozen) return originTime;
		else return System.currentTimeMillis() - originTime;
	}
	
	public int getFrame()
	{
		long time = getCurrentProgress();
		for (int index = 0; index < frames.length; index++)
		{
			if (time > frames[index].duration) time -= frames[index].duration;
			else return index;
		}
		return 0;
	}
	
	public int getFrameCount()
	{
		return frames.length;
	}
	
	public void addFrame(AnimationFrame frame, int position)
	{
		if (frame.getWidth() == getWidth() && frame.getHeight() == getHeight())
		{
			frame = frame.clone();
			frame.animation = this;
			if (position < 0) position = 0;
			else if (position > frames.length) position = frames.length;
			AnimationFrame[] newFrames = new AnimationFrame[frames.length + 1];
			for (int index = 0; index < newFrames.length; index++)
			{
				if (index == position) newFrames[index] = frame;
				else if (index < position) newFrames[index] = frames[index];
				else newFrames[index + 1] = frames[index];
			}
			frames = newFrames;
		}
	}
	
	public void addFrame(AnimationFrame frame)
	{
		addFrame(frame, frames.length);
	}
	
	public AnimationFrame removeFrame(int frame)
	{
		if (frame >= 0 && frame < frames.length)
		{
			AnimationFrame[] newFrames = new AnimationFrame[frames.length - 1];
			AnimationFrame removedFrame = null;
			for (int index = 0; index < frames.length; index++)
			{
				if (index == frame) removedFrame = frames[index];
				else if (index < frame) newFrames[index] = frames[index];
				else newFrames[index - 1] = frames[index];
			}
			frames = newFrames;
			return removedFrame;
		}
		else return null;
	}
	
	public int removeFrame(AnimationFrame frame)
	{
		int index = getFrameIndex(frame);
		removeFrame(index);
		return index;
	}
	
	public long getAnimationLength()
	{
		long length = 0;
		for (int index = 0; index < frames.length; index++) length += frames[index].duration;
		return length;
	}
	
	public Drawable getFrameDrawable(int frame)
	{
		return frames[frame];
	}
	
	@Override
	public BufferedImage getImage()
	{
		return frames[getFrame()].getImage();
	}
	
	public Canvas getCanvas(int frame)
	{
		return frames[frame].getCanvas();
	}
	
	@Override
	public Canvas getCanvas()
	{
		return getCanvas(getFrame());
	}
	
	public void clear(int frame)
	{
		frames[frame].clear();
	}
	
	@Override
	public void clear()
	{
		if (frames != null) for (int index = 0; index < frames.length; index++) clear(index);
	}
	
	public int getPixel(int frame, int x, int y)
	{
		return frames[frame].getPixel(x, y);
	}
	
	@Override
	public int getPixel(int x, int y)
	{
		return getPixel(getFrame(), x, y);
	}
	
	public void setPixel(int frame, int x, int y, int color)
	{
		frames[frame].setPixel(x, y, color);
	}
	
	@Override
	public void setPixel(int x, int y, int color)
	{
		setPixel(getFrame(), x, y, color);
	}
	
	public void clearRect(int frame, int x, int y, int width, int height)
	{
		int color = 0x00000000;
		for (int px = x; px < x + width; px++)
		{
			for (int py = y; py < y + height; py++)
			{
				setPixel(frame, px, py, color);
			}
		}
	}
	
	@Override
	public void clearRect(int x, int y, int width, int height)
	{
		clearRect(getFrame(), x, y, width, height);
	}
}