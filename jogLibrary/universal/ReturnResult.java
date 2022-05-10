package jogLibrary.universal;

import jogLibrary.universal.richString.RichString;

public class ReturnResult<Type> extends Result
{
	Type value = null;
	
	public ReturnResult(RichString description, boolean success, Type value)
	{
		super(description, success);
		this.value = value;
	}
	
	public ReturnResult(String description, boolean success, Type value)
	{
		this(new RichString(description), success, value);
	}
	
	public ReturnResult(Type value)
	{
		super(true);
		this.value = value;
	}
	
	public ReturnResult()
	{
		super(false);
	}
	
	public ReturnResult(String description)
	{
		this(new RichString(description));
	}
	
	public ReturnResult(String description, Type value)
	{
		this(new RichString(description), value);
	}
	
	public ReturnResult(RichString description)
	{
		this(description, false, null);
	}
	
	public ReturnResult(RichString description, Type value)
	{
		this(description, true, value);
	}
	
	public ReturnResult(boolean success, Type value)
	{
		super(success);
		this.value = value;
	}
	
	public Type value()
	{
		return value;
	}
}