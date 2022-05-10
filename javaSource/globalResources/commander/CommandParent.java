package globalResources.commander;

import java.util.HashMap;

import globalResources.discorse.argument.MultiListInterpretationResult;
import globalResources.richText.RichString;
import globalResources.utilities.SecureList;

public abstract class CommandParent implements CommandComponent
{
	HashMap<String, AbstractCommand> commands = new HashMap<String, AbstractCommand>();
	
	abstract boolean attemptToAddCommand(AbstractCommand command);
	
	public final AbstractCommand getCommand(String name)
	{
		return commands.get(name.toLowerCase());
	}
	
	public final boolean hasCommand(String name)
	{
		return commands.containsKey(name.toLowerCase());
	}
	
	public final SecureList<AbstractCommand> getCommands()
	{
		return new SecureList<AbstractCommand>(commands.values());
	}
	
	public final AbstractCommand addCommand(String name, String description, SimpleCommandExecutor executor)
	{
		return addCommand(name, new RichString(description), executor);
	}
	
	public final AbstractCommand addCommand(String name, RichString description, SimpleCommandExecutor executor)
	{
		return new SimpleCommand(this, name, description, executor);
	}
	
	public interface SimpleCommandExecutor
	{
		public void execute(MultiListInterpretationResult result, AbstractExecutor executor);
	}
	
	private static class SimpleCommand extends AbstractCommand
	{
		private SimpleCommandExecutor executor;
		
		public SimpleCommand(CommandParent parent, String name, RichString description, SimpleCommandExecutor executor)
		{
			super(parent, name, description);
			this.executor = executor;
		}
		
		@Override
		public void executeCommand(MultiListInterpretationResult result, AbstractExecutor executor)
		{
			this.executor.execute(result, executor);
		}
	}
}