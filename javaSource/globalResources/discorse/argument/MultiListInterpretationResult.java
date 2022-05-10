package globalResources.discorse.argument;

import globalResources.commander.ValidationResult;
import globalResources.richText.RichString;

public class MultiListInterpretationResult extends InterpretationResult
{
	private int listNumber;
	private ArgumentMultiList multiList;
	private InterpretationResult[] results;
	
	private RichString[] reasons;
	private int[] indecies;
	
	MultiListInterpretationResult(ValidationResult validationResult, boolean valid, boolean endReached, ArgumentEntryConsumptionResult[] results, int listNumber, ArgumentMultiList multiList, ArgumentList list, InterpretationResult[] allResults, String unconsumed)
	{
		super(validationResult, valid, endReached, results, list, unconsumed);
		this.listNumber = listNumber;
		this.multiList = multiList;
		this.results = allResults;
		
		reasons = new RichString[allResults.length];
		indecies = new int[allResults.length];
		for (int index = 0; index < allResults.length; index++)
		{
			reasons[index] = allResults[index].getInvalidReason();
			indecies[index] = allResults[index].getInvalidArgumentIndex();
		}
	}
	
	public int listNumber()
	{
		return listNumber;
	}
	
	public ArgumentMultiList multiList()
	{
		return multiList;
	}
	
	public InterpretationResult getResult(int index)
	{
		return (index >= 0 && index < results.length) ? results[index] : null;
	}
	
	public RichString[] getAllInvalidReasons()
	{
		return reasons;
	}
	
	public int[] getAllInvalidArgumentIndecies()
	{
		return indecies;
	}
}