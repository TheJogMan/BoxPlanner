package jogLibrary.utilities;

public final class Result<ReturnType>
{
	private ReturnType result;
	private boolean success;
	private String description;
	private Result<?> subResult;
	private Object[] data;
	
	public ReturnType result()
	{
		return result;
	}
	
	public boolean success()
	{
		return success;
	}
	
	public String description()
	{
		return description;
	}
	
	public Result<?> getSubResult()
	{
		return subResult;
	}
	
	public Object[] getData()
	{
		return data;
	}
	
	public Result(ReturnType result, boolean success, String description, Result<?> subResult, Object[] data)
	{
		if (description == null) description = "No description given.";
		if (data == null) data = new Object[0];
		
		this.result = result;
		this.success = success;
		this.description = description;
		this.subResult = subResult;
	}
	
	public Result()
	{
		this(null, true, null, null, null);
	}
	
	public Result(ReturnType result)
	{
		this(result, false, null, null, null);
	}
	
	public Result(ReturnType result, Object... data)
	{
		this(result, false, null, null, data);
	}
	
	public Result(ReturnType result, Result<?> subResult, Object... data)
	{
		this(result, true, null, subResult, data);
	}
	
	public Result(String description)
	{
		this(null, false, description, null, null);
	}
	
	public Result(String description, Object... data)
	{
		this(null, false, description, null, data);
	}
	
	public Result(String description, Result<?> subResult)
	{
		this(null, false, description, subResult, null);
	}
	
	public Result(String description, Result<?> subResult, Object... data)
	{
		this(null, false, description, subResult, data);
	}
	
	public Result(ReturnType result, String description)
	{
		this(result, false, description, null, null);
	}
	
	public Result(ReturnType result, String description, Object... data)
	{
		this(result, false, description, null, data);
	}
	
	public Result(ReturnType result, String description, Result<?> subResult)
	{
		this(result, false, description, subResult, null);
	}
	
	public Result(ReturnType result, String description, Result<?> subResult, Object... data)
	{
		this(result, false, description, subResult, data);
	}
}