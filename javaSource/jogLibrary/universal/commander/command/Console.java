package jogLibrary.universal.commander.command;

import java.util.List;

import jogLibrary.universal.ReturnResult;
import jogLibrary.universal.commander.Executor;
import jogLibrary.universal.indexable.HoardingIndexer;
import jogLibrary.universal.richString.RichString;

public class Console extends Category
{
	String helpCommandName;
	char prefix;
	
	public Console(String name, RichString description, String helpCommandName, char prefix)
	{
		super(name, description, new Object[] {new Object[0], helpCommandName, prefix});
	}
	
	protected Console(String name, RichString description, String helpCommandName, char prefix, Object[] preInitData)
	{
		super(name, description, new Object[] {preInitData, helpCommandName, prefix});
	}
	
	@Override
	protected final void categoryPreInit(Object[] preInitData)
	{
		helpCommandName = (String)preInitData[1];
		prefix = (Character)preInitData[2];
		consolePreInit((Object[])preInitData[0]);
	}
	
	protected void consolePreInit(Object[] preInitData)
	{
		
	}
	
	@Override
	public final List<String> getCompletions(HoardingIndexer<Character> source, Executor executor)
	{
		if (source.get() == prefix)
			source.next();
		return super.getCompletions(source, executor);
	}
	
	@Override
	public final ReturnResult<Boolean> interpret(HoardingIndexer<Character> source, Executor executor)
	{
		if (source.get() == prefix)
			source.next();
		return super.interpret(source, executor);
	}
}