package globalResources.commander;

import globalResources.discorse.argument.MultiListInterpretationResult;
import globalResources.richText.RichString;

public abstract class PickyCommand<ExecutorType extends AbstractExecutor> extends AbstractCommand
{
	public PickyCommand(CommandParent parent, String name, RichString description)
	{
		super(parent, name, description);
	}
	
	public PickyCommand(CommandParent parent, String name, RichString description, AbstractValidator validator)
	{
		super(parent, name, description, validator);
	}
	
	public PickyCommand(CommandParent parent, String name, String description)
	{
		super(parent, name, description);
	}
	
	protected abstract void executePickyCommand(MultiListInterpretationResult result, ExecutorType executor);
	
	@SuppressWarnings("unchecked")
	public final void executeCommand(MultiListInterpretationResult result, AbstractExecutor executor)
	{
		ExecutorType pickyExecutor;
		try
		{
			pickyExecutor = (ExecutorType)executor;
		}
		catch (Exception e)
		{
			pickyExecutor = null;
		}
		if (pickyExecutor != null) executePickyCommand(result, pickyExecutor);
	}
}