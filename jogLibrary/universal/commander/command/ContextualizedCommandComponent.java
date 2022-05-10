package jogLibrary.universal.commander.command;

import jogLibrary.universal.commander.Executor;

public interface ContextualizedCommandComponent
{
	public Executor contextSource();
}