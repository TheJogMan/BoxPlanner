package jogLibrary.universal.commander.argument.arguments;

import java.util.List;

import jogLibrary.universal.ReturnResult;
import jogLibrary.universal.commander.Executor;
import jogLibrary.universal.commander.argument.Argument;
import jogLibrary.universal.dataStructures.data.values.StringValue;
import jogLibrary.universal.indexable.HoardingIndexer;
import jogLibrary.universal.richString.RichString;

public class LongArgument extends Argument<Long>
{
	@Override
	public ReturnResult<Long> interpret(HoardingIndexer<Character> source, Executor executor)
	{
		String token = StringValue.consumeString(source, ' ').value();
		try
		{
			return new ReturnResult<>(Long.parseLong(token));
		}
		catch (Exception e)
		{
			return new ReturnResult<>("Not a valid long.");
		}
	}
	
	@Override
	public List<String> getCompletions(HoardingIndexer<Character> source, Executor executor)
	{
		return null;
	}
	
	@Override
	public void init(Object[] data)
	{
		
	}
	
	@Override
	public String defaultName()
	{
		return "Long";
	}
	
	@Override
	public RichString defaultDescription()
	{
		return null;
	}
}