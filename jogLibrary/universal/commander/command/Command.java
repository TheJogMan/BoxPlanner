package jogLibrary.universal.commander.command;

import java.util.ArrayList;
import java.util.List;

import jogLibrary.universal.Result;
import jogLibrary.universal.ReturnResult;
import jogLibrary.universal.commander.Executor;
import jogLibrary.universal.commander.argument.AdaptiveArgumentList;
import jogLibrary.universal.commander.argument.AdaptiveArgumentList.ArgumentListEntry;
import jogLibrary.universal.commander.argument.AdaptiveArgumentListInterpretationResult;
import jogLibrary.universal.commander.argument.Argument;
import jogLibrary.universal.commander.argument.ArgumentList.ArgumentEntry;
import jogLibrary.universal.indexable.HoardingIndexer;
import jogLibrary.universal.richString.RichString;
import jogLibrary.universal.richString.RichStringBuilder;

public abstract class Command extends CommandComponent
{
	AdaptiveArgumentList arguments = new AdaptiveArgumentList();
	
	public abstract void execute(AdaptiveArgumentListInterpretationResult result, Executor executor);
	
	Command(Category parent, String name, RichString description, boolean addToParent)
	{
		super(parent, name, description, addToParent);
	}
	
	public Command(Category parent, String name, RichString description)
	{
		super(parent, name, description, true);
	}
	
	public Command(Category parent, String name, String description)
	{
		super(parent, name, new RichString(description), true);
	}
	
	@Override
	public final Console getConsole()
	{
		return parent.getConsole();
	}
	
	@Override
	public final String getFullName(boolean includePrefixCharacter)
	{
		if (parent.isConsole())
			return parent.getFullName(includePrefixCharacter) + name();
		else
			return parent.getFullName(includePrefixCharacter) + " " + name();
	}
	
	public final String getFullName(int variant)
	{
		return getFullName(false, variant);
	}
	
	public final String getFullName(boolean includePrefixCharacter, int variant)
	{
		if (parent.isConsole())
			return parent.getFullName(includePrefixCharacter) + name() + " " + getArgumentList(variant).list();
		else
			return parent.getFullName(includePrefixCharacter) + " " + name() + " " + getArgumentList(variant).list();
	}
	
	@Override
	public List<String> getCompletions(HoardingIndexer<Character> source, Executor executor)
	{
		if (canExecute(executor).success())
			return arguments.getCompletions(source, executor);
		else
			return new ArrayList<>();
	}
	
	@Override
	public final ReturnResult<Boolean> interpret(HoardingIndexer<Character> source, Executor executor)
	{
		Result canExecute = canExecute(executor);
		if (!canExecute.success())
		{
			ReturnResult<Boolean> result = new ReturnResult<>(RichStringBuilder.start().append("You can not run this command: ").append(canExecute.description()).build(), false, false);
			executor.respond(result.description());
			return result;
		}
		
		AdaptiveArgumentListInterpretationResult result = arguments.interpret(source, executor).value();
		if (result.success())
		{
			execute(result, executor);
			return new ReturnResult<>("Command executed.", true);
		}
		else
		{
			RichString response = RichStringBuilder.start().append("Could not interpret arguments: ").append(result.description()).build();
			executor.respond(response);
			return new ReturnResult<>(response, false, false);
		}
	}
	
	public final void addArgumentList()
	{
		arguments.addList();
	}
	
	public final void addArgumentList(RichString description)
	{
		arguments.addList(description);
	}
	
	public final void addArgumentList(String description)
	{
		arguments.addList(description);
	}
	
	public final int argumentListCount()
	{
		return arguments.listCount();
	}
	
	public ArgumentListEntry getArgumentList(int index)
	{
		return arguments.getList(index);
	}
	
	public void setDefaultListDescription(RichString description)
	{
		arguments.setDefaultListDescription(description);
	}
	
	public void setDefaultListDescription(String description)
	{
		arguments.setDefaultListDescription(description);
	}
	
	/***
	 * Defaults to list number 0.
	 * @return
	 */
	public int argumentCount()
	{
		return arguments.argumentCount();
	}
	
	/***
	 * Defaults to list number 0.
	 * @return
	 */
	public ArgumentEntry getArgument(int index)
	{
		return arguments.getArgument(index);
	}
	
	/***
	 * Defaults to list number 0.
	 * @return
	 */
	public void addArgument(Class<? extends Argument<?>> argument, String name, Object[] data, RichString description)
	{
		arguments.addArgument(0, argument, name, data, description);
	}
	
	/***
	 * Defaults to list number 0.
	 * @return
	 */
	public void addArgument(Class<? extends Argument<?>> argument, String name, Object[] data, String description)
	{
		arguments.addArgument(0, argument, name, data, description);
	}
	
	/***
	 * Defaults to list number 0.
	 * @return
	 */
	public void addArgument(Class<? extends Argument<?>> argument, Object[] data, RichString description)
	{
		arguments.addArgument(0, argument, data, description);
	}
	
	/***
	 * Defaults to list number 0.
	 * @return
	 */
	public void addArgument(Class<? extends Argument<?>> argument, Object[] data, String description)
	{
		arguments.addArgument(0, argument, data, description);
	}
	
	/***
	 * Defaults to list number 0.
	 * @return
	 */
	public void addArgument(Class<? extends Argument<?>> argument, String name, Object[] data)
	{
		arguments.addArgument(0, argument, name, data);
	}
	
	/***
	 * Defaults to list number 0.
	 * @return
	 */
	public void addArgument(Class<? extends Argument<?>> argument, Object[] data)
	{
		arguments.addArgument(0, argument, data);
	}
	
	/***
	 * Defaults to list number 0.
	 * @return
	 */
	public void addArgument(Class<? extends Argument<?>> argument)
	{
		arguments.addArgument(0, argument);
	}
	
	/***
	 * Defaults to list number 0.
	 * @return
	 */
	public void addArgument(Class<? extends Argument<?>> argument, String name)
	{
		arguments.addArgument(0, argument, name);
	}
	
	public void addArgument(int listNumber, Class<? extends Argument<?>> argument, String name, Object[] data, RichString description)
	{
		arguments.addArgument(listNumber, argument, name, data, description);
	}
	
	public void addArgument(int listNumber, Class<? extends Argument<?>> argument, String name, Object[] data, String description)
	{
		arguments.addArgument(listNumber, argument, name, data, description);
	}
	
	public void addArgument(int listNumber, Class<? extends Argument<?>> argument, Object[] data, RichString description)
	{
		arguments.addArgument(listNumber, argument, data, description);
	}
	
	public void addArgument(int listNumber, Class<? extends Argument<?>> argument, Object[] data, String description)
	{
		arguments.addArgument(listNumber, argument, data, description);
	}
	
	public void addArgument(int listNumber, Class<? extends Argument<?>> argument, String name, Object[] data)
	{
		arguments.addArgument(listNumber, argument, name, data);
	}
	
	public void addArgument(int listNumber, Class<? extends Argument<?>> argument, Object[] data)
	{
		arguments.addArgument(listNumber, argument, data);
	}
	
	public void addArgument(int listNumber, Class<? extends Argument<?>> argument)
	{
		arguments.addArgument(listNumber, argument);
	}
	
	public void addArgument(int listNumber, Class<? extends Argument<?>> argument, String name)
	{
		arguments.addArgument(listNumber, argument, name);
	}
}