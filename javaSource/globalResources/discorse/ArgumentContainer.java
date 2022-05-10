package globalResources.discorse;

import java.util.Iterator;

import globalResources.discorse.argument.Argument;

public interface ArgumentContainer
{
	public void addArgument(Class<? extends Argument<?>> argument, Object[] data);
	public Argument<?> getArgument(int index);
	public int indexOfArgument(Argument<?> argument);
	public void removeArgument(Argument<?> argument);
	public String getArgumentName(Argument<?> argument);
	public int getArgumentCount();
	public boolean hasArgument(Argument<?> argument);
	public Iterator<Argument<?>> argumentIterator();
}