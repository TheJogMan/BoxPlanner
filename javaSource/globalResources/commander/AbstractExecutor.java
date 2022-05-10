package globalResources.commander;

import globalResources.richText.RichString;

public abstract class AbstractExecutor
{
	private Object[] extraData;
	
	public AbstractExecutor()
	{
		this.extraData = new Object[0];
	}
	
	public AbstractExecutor(Object[] extraData)
	{
		this.extraData = extraData;
	}
	
	public Object[] getExtraData()
	{
		return extraData;
	}
	
	public void respond(RichString string)
	{
		System.out.println(string.toString());
	}
	
	public void respond(String string)
	{
		System.out.println(string);
	}
	
	public final Object[] getRawExtraData()
	{
		return extraData;
	}
}