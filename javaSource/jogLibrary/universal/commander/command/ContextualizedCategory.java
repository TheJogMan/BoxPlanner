package jogLibrary.universal.commander.command;

import jogLibrary.universal.commander.Executor;
import jogLibrary.universal.richString.RichString;

public abstract class ContextualizedCategory extends Category implements ContextualizedCommandComponent
{
	Executor contextSource;
	
	public ContextualizedCategory(Category parent, Executor executor, String name, RichString description, Object[] preInitData)
	{
		super(parent, name, description, new Object[] {executor, preInitData}, false);
	}
	
	public ContextualizedCategory(Category parent, Executor executor, String name, String description, Object[] preInitData)
	{
		super(parent, name, new RichString(description), new Object[] {executor, preInitData}, false);
	}
	
	public ContextualizedCategory(Category parent, Executor executor, String name, RichString description)
	{
		super(parent, name, description, new Object[] {executor, new Object[0]}, false);
	}
	
	public ContextualizedCategory(Category parent, Executor executor, String name, String description)
	{
		super(parent, name, new RichString(description), new Object[] {executor, new Object[0]}, false);
	}
	
	@Override
	public final Executor contextSource()
	{
		return contextSource;
	}
	
	@Override
	protected final void categoryPreInit(Object[] data)
	{
		contextSource = (Executor)data[0];
		contextualizeCategoryPreInit((Object[])data[1]);
	}
	
	protected void contextualizeCategoryPreInit(Object[] data)
	{
		
	}
}