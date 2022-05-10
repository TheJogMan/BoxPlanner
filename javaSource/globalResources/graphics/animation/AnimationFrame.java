package globalResources.graphics.animation;

import globalResources.graphics.Image;

public class AnimationFrame extends Image
{
	Animation animation;
	int duration;
	
	AnimationFrame(Animation animation, AnimationFrameData data) throws WrongSizeException
	{
		super(data.data);
		
		if (data.getWidth() <= animation.getWidth() && data.getHeight() <= animation.getHeight())
		{
			this.duration = data.duration;
			this.animation = animation;
		}
		else throw new WrongSizeException();
	}
	
	AnimationFrame(Animation animation)
	{
		super(animation.getWidth(), animation.getHeight());
		this.duration = 1;
		this.animation = animation;
	}
	
	@Override
	public AnimationFrame clone()
	{
		try
		{
			return new AnimationFrame(animation, new AnimationFrameData(this));
		}
		catch (WrongSizeException e)
		{
			//this should never occur, but just in case
			return new AnimationFrame(animation);
		}
	}
	
	public Animation getAnimation()
	{
		return animation;
	}
	
	public long getDuration()
	{
		return duration;
	}
	
	public void setDuration(int duration)
	{
		if (duration < 1) duration = 1;
		this.duration = duration;
	}
	
	public int getIndex()
	{
		return animation.getFrameIndex(this);
	}
	
	public static final class WrongSizeException extends Exception
	{
		private static final long serialVersionUID = 6159997948927315609L;
		
		public WrongSizeException()
		{
			super("A frame can not be added to an animation if the dimensions don't match!");
		}
	}
}