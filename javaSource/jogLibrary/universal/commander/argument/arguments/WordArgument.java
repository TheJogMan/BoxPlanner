package jogLibrary.universal.commander.argument.arguments;

import java.util.List;

import jogLibrary.universal.ReturnResult;
import jogLibrary.universal.commander.Executor;
import jogLibrary.universal.commander.argument.Argument;
import jogLibrary.universal.dataStructures.data.values.StringValue;
import jogLibrary.universal.indexable.HoardingIndexer;
import jogLibrary.universal.richString.RichString;

public class WordArgument extends Argument<String>
{
	@Override
	public ReturnResult<String> interpret(HoardingIndexer<Character> source, Executor executor)
	{
		return new ReturnResult<>(StringValue.consumeString(source, ' ').value());
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
		return "Word";
	}
	
	@Override
	public RichString defaultDescription()
	{
		return null;
	}
}