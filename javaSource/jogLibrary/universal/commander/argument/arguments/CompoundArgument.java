package jogLibrary.universal.commander.argument.arguments;

import java.util.List;

import jogLibrary.universal.ReturnResult;
import jogLibrary.universal.commander.Executor;
import jogLibrary.universal.commander.argument.Argument;
import jogLibrary.universal.commander.argument.ArgumentList;
import jogLibrary.universal.indexable.HoardingIndexer;
import jogLibrary.universal.richString.RichString;

public abstract class CompoundArgument<ValueType> extends Argument<ValueType>
{
	ArgumentList arguments = new ArgumentList();
	
	public abstract ReturnResult<ValueType> compoundInterpretation(Object[] values, Executor executor);
	
	public final ReturnResult<ValueType> interpret(HoardingIndexer<Character> source, Executor executor)
	{
		ReturnResult<Object[]> result = arguments.interpret(source, executor);
		if (result.success())
			return compoundInterpretation(result.value(), executor);
		else
			return new ReturnResult<>(result.description());
	}
	
	public final List<String> getCompletions(HoardingIndexer<Character> source, Executor executor)
	{
		return arguments.getCompletions(source, executor);
	}
	
	public String argStringName()
	{
		return "<" + arguments.toString() + ">";
	}
	
	protected final void addArgument(Class<? extends Argument<?>> argument, String name, Object[] data, RichString description)
	{
		arguments.addArgument(argument, name, data, description);
	}
	
	protected final void addArgument(Class<? extends Argument<?>> argument, String name, Object[] data, String description)
	{
		arguments.addArgument(argument, name, data, description);
	}
	
	protected final void addArgument(Class<? extends Argument<?>> argument, Object[] data, RichString description)
	{
		arguments.addArgument(argument, data, description);
	}
	
	protected final void addArgument(Class<? extends Argument<?>> argument, Object[] data, String description)
	{
		arguments.addArgument(argument, data, description);
	}
	
	protected final void addArgument(Class<? extends Argument<?>> argument, String name, Object[] data)
	{
		arguments.addArgument(argument, name, data);
	}
	
	protected final void addArgument(Class<? extends Argument<?>> argument, Object[] data)
	{
		arguments.addArgument(argument, data);
	}
	
	protected final void addArgument(Class<? extends Argument<?>> argument)
	{
		arguments.addArgument(argument);
	}
	
	protected final void addArgument(Class<? extends Argument<?>> argument, String name)
	{
		arguments.addArgument(argument, name);
	}
}