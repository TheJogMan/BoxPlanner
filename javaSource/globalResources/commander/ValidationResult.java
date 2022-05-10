package globalResources.commander;

import globalResources.richText.RichString;

public class ValidationResult
{
	private boolean valid;
	private RichString reason;
	private AbstractExecutor executor;
	
	public ValidationResult(AbstractExecutor executor, boolean valid, String reason)
	{
		this(executor, valid, new RichString(reason));
	}
	
	public ValidationResult(AbstractExecutor executor, boolean valid, RichString reason)
	{
		this.executor = executor;
		this.valid = valid;
		this.reason = reason;
	}
	
	public final boolean valid()
	{
		return valid;
	}
	
	public final RichString getReason()
	{
		return reason.clone();
	}
	
	public final AbstractExecutor getExecutor()
	{
		return executor;
	}
}