package globalResources.commander;

import java.util.ArrayList;
import java.util.Iterator;

import globalResources.discorse.AIU;
import globalResources.discorse.ArgumentConsumptionResult;
import globalResources.discorse.argument.Argument;
import globalResources.discorse.argument.ArgumentList;
import globalResources.discorse.argument.MultiListInterpretationResult;
import globalResources.richText.RichColor;
import globalResources.richText.RichString;
import globalResources.richText.RichStringBuilder;

public final class HelpCommand extends AbstractCommand
{
	HelpCommand(CommandParent parent, String name)
	{
		super(parent, name, new RichString("Used to navigate and learn about commands in this console."));
		ArgumentList list0 = new ArgumentList();
		list0.addArgument(CommandComponentArgument.class, "Category/Command", new Object[] {(AbstractCategory)parent});
		argumentList.addList(list0);
		ArgumentList list1 = new ArgumentList();
		argumentList.addList(list1);
		argumentList.setListDescription(0, "Shows information about a category or command.");
		argumentList.setListDescription(1, "Lists available commands and categories.");
	}
	
	@Override
	public void executeCommand(MultiListInterpretationResult result, AbstractExecutor executor)
	{
		if (result.listNumber() == 0)
		{
			CommandComponent component = (CommandComponent)result.getValues()[0];
			RichStringBuilder builder = new RichStringBuilder();
			builder.setMainColor(RichColor.YELLOW);
			if (component instanceof AbstractCategory)
			{
				AbstractCategory category = (AbstractCategory)component;
				builder.append("Category: ");
				builder.setHighlighted(true);
				builder.append(RichColor.AQUA, category.getFullName());
				builder.newLine();
				builder.append(category.getDescription());
				builder.newLine();
				builder.setHighlighted(false);
				builder.setMainColor(RichColor.ORANGE);
				builder.append("Run ");
				builder.setHighlighted(true);
				builder.append(RichColor.YELLOW, getConsole().getPrefix() + category.helpCommand.getFullName());
				builder.setMainColor(RichColor.ORANGE);
				builder.setHighlighted(false);
				builder.append(" to view commands and sub-categories");
			}
			else if (component instanceof AbstractCommand)
			{
				AbstractCommand command = (AbstractCommand)component;
				builder.append("Command: ");
				builder.setHighlighted(true);
				builder.append(RichColor.AQUA, getConsole().getPrefix() + command.getFullName());
				builder.setHighlighted(false);
				builder.newLine();
				builder.append(command.getDescription());
				builder.newLine();
				builder.append("Usage:");
				builder.newLine();
				builder.setMainColor(RichColor.WHITE);
				for (Iterator<ArgumentList> iterator = command.argumentList.listIterator(); iterator.hasNext();)
				{
					ArgumentList list = iterator.next();
					builder.append(getConsole().getPrefix() + command.getFullName());
					String argumentString = list.getArgumentNameString();
					if (argumentString.length() > 0) builder.append(" " + argumentString);
					RichString description = command.argumentList.getListDescription(command.argumentList.indexOfList(list));
					if (description.length() > 0)
					{
						builder.setMainColor(RichColor.GRAY);
						builder.append(" - ");
						builder.append(RichColor.GRAY, description);
						builder.setMainColor(RichColor.WHITE);
					}
					if (iterator.hasNext()) builder.newLine();
				}
			}
			executor.respond(builder.build());
		}
		else if (result.listNumber() == 1)
		{
			//initialize the builder to create the list
			RichStringBuilder builder = new RichStringBuilder();
			
			//list sub categories in this commands category
			builder.setMainColor(RichColor.YELLOW);
			builder.append("Sub-Categories:");
			builder.newLine();
			builder.append(" ");//initialize the name list with an indent, will only apply to the first line if the console ends up writing the name list on multiple lines but thats fine
			builder.setMainColor(RichColor.WHITE);
			builder.setHighlighted(true);
			if (getCategory().getCategories().size() == 0) builder.append("None.");
			for (Iterator<AbstractCategory> iterator = getCategory().getCategories().iterator(); iterator.hasNext();)
			{
				builder.append(iterator.next().getName());
				if (iterator.hasNext())
				{
					builder.setHighlighted(false);
					builder.setMainColor(RichColor.YELLOW);
					builder.append(", ");
					builder.setMainColor(RichColor.WHITE);
					builder.setHighlighted(true);
				}
			}
			builder.setHighlighted(false);
			builder.newLine();
			
			//list other commands in this commands category
			builder.setMainColor(RichColor.YELLOW);
			builder.append("Commands:");
			builder.newLine();
			builder.append(" ");//initialize the name list with an indent, will only apply to the first line if the console ends up writing the name list on multiple lines but thats fine
			builder.setMainColor(RichColor.WHITE);
			builder.setHighlighted(true);
			if (getCategory().getCommands().size() == 0) builder.append("None.");
			for (Iterator<AbstractCommand> iterator = getCategory().getCommands().iterator(); iterator.hasNext();)
			{
				builder.append(iterator.next().getName());
				if (iterator.hasNext())
				{
					builder.setHighlighted(false);
					builder.setMainColor(RichColor.YELLOW);
					builder.append(", ");
					builder.setMainColor(RichColor.WHITE);
					builder.setHighlighted(true);
				}
			}
			builder.setHighlighted(false);
			builder.newLine();
			builder.setMainColor(RichColor.ORANGE);
			builder.append("Run ");
			builder.setHighlighted(true);
			builder.setMainColor(RichColor.YELLOW);
			if (getConsole().prefixCharacter != '\n') builder.append(getConsole().prefixCharacter);
			builder.append(getCategory().getHelpCommand().getFullName() + " <Category/Command>");
			builder.setHighlighted(false);
			builder.setMainColor(RichColor.ORANGE);
			builder.append(" to learn more");
			//send the list back to the console where the command was entered
			executor.respond(builder.build());
		}
	}
	
	public static class CommandComponentArgument implements Argument<CommandComponent>
	{
		//the category that a particular instance of a help command is added to, needed to access the commands and categories available in that category
		//should be provided when the argument gets added, otherwise it wont work
		AbstractCategory category;
		
		@Override
		public void init(Object[] data)
		{
			if (data.length >= 1 && data[0] instanceof AbstractCategory)
			{
				category = (AbstractCategory)data[0];
			}
		}

		@Override
		public String getName()
		{
			return "Command Component";
		}

		@Override
		public void getCompletions(String argumentString, ArrayList<String> completions, AbstractExecutor executor)
		{
			if (category != null)
			{
				for (Iterator<AbstractCategory> iterator = category.categories.values().iterator(); iterator.hasNext();)
				{
					completions.add(iterator.next().getName());
				}
				for (Iterator<AbstractCommand> iterator = category.commands.values().iterator(); iterator.hasNext();)
				{
					completions.add(iterator.next().getName());
				}
			}
		}

		@Override
		public ArgumentConsumptionResult<CommandComponent> consume(String string, AbstractExecutor executor)
		{
			if (category != null)
			{
				String name = AIU.quickConsume(string).getConsumed();
				CommandComponent component = null;
				
				if (category.hasCategory(name)) component = category.getCategory(name);
				else if (category.hasCommand(name)) component = category.getCommand(name);
				
				if (component != null) return new ArgumentConsumptionResult<CommandComponent>(true, component, name, "Valid", executor, this);
				else return new ArgumentConsumptionResult<CommandComponent>(false, null, name, "There is no category or command by that name in this category", executor, this);
			}
			else return new ArgumentConsumptionResult<CommandComponent>(false, null, "", "No category provided to this argument in implementation", executor, this);
		}
		
	}
}