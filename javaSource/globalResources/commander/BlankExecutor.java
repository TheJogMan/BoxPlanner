package globalResources.commander;

import globalResources.richText.RichString;

public final class BlankExecutor extends AbstractExecutor
{
	private AbstractCommandConsole console;
	
	BlankExecutor(Object[] extraData, AbstractCommandConsole console)
	{
		super(extraData);
		this.console = console;
	}
	
	@Override
	public final void respond(RichString string)
	{
		console.respond(string, this);
	}
	
	@Override
	public final void respond(String string)
	{
		console.respond(string, this);
	}
}