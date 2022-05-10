package jogLibrary.universal;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import jogLibrary.universal.richString.RichString;

public class Result
{
	private boolean success;
	private RichString description;
	
	public Result(RichString description, boolean success)
	{
		this.success = success;
		this.description = description;
	}
	
	public Result(String description, boolean success)
	{
		this(new RichString(description), success);
	}
	
	public Result(boolean result)
	{
		this("No description.", result);
	}
	
	public Result()
	{
		this(true);
	}
	
	public Result(RichString description)
	{
		this(description, false);
	}
	
	public Result(String description)
	{
		this(new RichString(description));
	}
	
	public boolean success()
	{
		return success;
	}
	
	public RichString description()
	{
		return description;
	}
	
	public static String describeException(Exception e)
	{
		return e + " at " + e.getStackTrace()[0].getClassName() + "." + e.getStackTrace()[0].getMethodName() + "(" + e.getStackTrace()[0].getFileName() + ":" + e.getStackTrace()[0].getLineNumber() + ")";
	}
	
	public static String describeExceptionFull(Exception e)
	{
		ByteArrayOutputStream stream = new ByteArrayOutputStream();
		e.printStackTrace(new PrintStream(stream));
		return stream.toString();
	}
}