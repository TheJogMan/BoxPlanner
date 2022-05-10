package jogLibrary.universal.commander.argument;

import jogLibrary.universal.ReturnResult;
import jogLibrary.universal.richString.RichString;

public class AdaptiveArgumentListInterpretationResult extends ReturnResult<Object[]>
{
	private final int listNumber;
	
	public AdaptiveArgumentListInterpretationResult(RichString description, boolean success, int listNumber, Object[] values)
	{
		super(description, success, values);
		this.listNumber = listNumber;
	}
	
	public AdaptiveArgumentListInterpretationResult(String description, boolean success, int listNumber, Object[] values)
	{
		this(new RichString(description), success, listNumber, values);
	}
	
	public AdaptiveArgumentListInterpretationResult(int listNumber, Object[] values)
	{
		super(values);
		this.listNumber = listNumber;
	}
	
	public AdaptiveArgumentListInterpretationResult()
	{
		super();
		listNumber = -1;
	}
	
	public AdaptiveArgumentListInterpretationResult(String description, int listNumber)
	{
		this(new RichString(description), listNumber);
	}
	
	public AdaptiveArgumentListInterpretationResult(String description, int listNumber, Object[] values)
	{
		this(new RichString(description), listNumber, values);
	}
	
	public AdaptiveArgumentListInterpretationResult(RichString description, int listNumber)
	{
		this(description, false, listNumber, null);
	}
	
	public AdaptiveArgumentListInterpretationResult(RichString description, int listNumber, Object[] values)
	{
		this(description, true, listNumber, values);
	}
	
	public AdaptiveArgumentListInterpretationResult(boolean success, int listNumber, Object[] values)
	{
		super(success, values);
		this.listNumber = listNumber;
	}
	
	public final int listNumber()
	{
		return listNumber;
	}
}