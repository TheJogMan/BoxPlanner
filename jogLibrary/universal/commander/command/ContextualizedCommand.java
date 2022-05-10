package jogLibrary.universal.commander.command;

import jogLibrary.universal.commander.Executor;
import jogLibrary.universal.richString.RichString;

public abstract class ContextualizedCommand extends Command implements ContextualizedCommandComponent
{
	Executor contextSource;
	
	public ContextualizedCommand(Executor contextSource, String name, RichString description)
	{
		super(null, name, description, false);
		this.contextSource = contextSource;
	}
	
	@Override
	public final Executor contextSource()
	{
		return contextSource;
	}
}