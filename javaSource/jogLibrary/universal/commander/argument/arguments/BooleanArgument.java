package jogLibrary.universal.commander.argument.arguments;

import java.util.ArrayList;
import java.util.List;

import jogLibrary.universal.ReturnResult;
import jogLibrary.universal.commander.Executor;
import jogLibrary.universal.commander.argument.Argument;
import jogLibrary.universal.dataStructures.data.values.StringValue;
import jogLibrary.universal.indexable.HoardingIndexer;
import jogLibrary.universal.richString.RichString;

public class BooleanArgument extends Argument<Boolean>
{
	@Override
	public ReturnResult<Boolean> interpret(HoardingIndexer<Character> source, Executor executor)
	{
		String token = StringValue.consumeString(source, ' ').value();
		if (token.toLowerCase().equals("true"))
			return new ReturnResult<>(true);
		else if (token.toLowerCase().equals("false"))
			return new ReturnResult<>(false);
		else
			return new ReturnResult<>("Not a valid boolean.");
	}
	
	@Override
	public List<String> getCompletions(HoardingIndexer<Character> source, Executor executor)
	{
		String token = StringValue.consumeString(source, ' ').value();
		ArrayList<String> completions = new ArrayList<>();
		if (("true").startsWith(token.toLowerCase()))
			completions.add("True");
		if (("false").startsWith(token.toLowerCase()))
			completions.add("False");
		return completions;
	}
	
	@Override
	public void init(Object[] data)
	{
		
	}
	
	@Override
	public String defaultName()
	{
		return "Boolean";
	}
	
	@Override
	public RichString defaultDescription()
	{
		return null;
	}
}