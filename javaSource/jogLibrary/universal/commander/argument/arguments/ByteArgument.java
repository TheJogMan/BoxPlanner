package jogLibrary.universal.commander.argument.arguments;

import java.util.List;

import jogLibrary.universal.ReturnResult;
import jogLibrary.universal.commander.Executor;
import jogLibrary.universal.commander.argument.Argument;
import jogLibrary.universal.dataStructures.data.values.StringValue;
import jogLibrary.universal.indexable.HoardingIndexer;
import jogLibrary.universal.richString.RichString;

public class ByteArgument extends Argument<Byte>
{
	@Override
	public ReturnResult<Byte> interpret(HoardingIndexer<Character> source, Executor executor)
	{
		String token = StringValue.consumeString(source, ' ').value();
		try
		{
			return new ReturnResult<>(Byte.parseByte(token));
		}
		catch (Exception e)
		{
			return new ReturnResult<>("Not a valid byte.");
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
		return "Byte";
	}
	
	@Override
	public RichString defaultDescription()
	{
		return null;
	}
}