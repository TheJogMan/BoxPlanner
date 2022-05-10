package jogLibrary.universal.commander.command;

import java.util.ArrayList;
import java.util.Iterator;

import jogLibrary.universal.commander.Executor;
import jogLibrary.universal.commander.argument.AdaptiveArgumentList.ArgumentListEntry;
import jogLibrary.universal.commander.argument.AdaptiveArgumentListInterpretationResult;
import jogLibrary.universal.commander.argument.ArgumentList.ArgumentEntry;
import jogLibrary.universal.commander.argument.arguments.CommandComponentArgument;
import jogLibrary.universal.commander.argument.arguments.IntegerArgument;
import jogLibrary.universal.richString.RichColor;
import jogLibrary.universal.richString.RichString;
import jogLibrary.universal.richString.RichStringBuilder;

public class HelpCommand extends Command
{
	public HelpCommand(Category parent, String name)
	{
		super(parent, name, new RichString("Provides a simple means of learning about available commands."));
		
		setDefaultListDescription("See a list of available commands and sub-categories.");
		addArgumentList("See information about a specific component.");
		addArgument(1, CommandComponentArgument.class, "Command/Category", new Object[] {parent});
		addArgumentList("See information about a specific variation of a commands arguments.");
		addArgument(2, CommandComponentArgument.class, "Command/Category", new Object[] {parent, Command.class});
		addArgument(2, IntegerArgument.class, "Variant");
	}
	
	@Override
	public void execute(AdaptiveArgumentListInterpretationResult result, Executor executor)
	{
		if (result.listNumber() == 0)
			viewCategory(parent, executor);
		else if (result.listNumber() == 1)
		{
			CommandComponent component = (CommandComponent)result.value()[0];
			if (component instanceof Category)
				viewCategory((Category)component, executor);
			else if (component instanceof Command)
			{
				if (((Command)component).argumentListCount() == 1)
					viewCommandVariant((Command)component, 0, executor);
				else
					viewCommand((Command)component, executor);
			}
		}
		else if (result.listNumber() == 2)
		{
			Command command = (Command)result.value()[0];
			int variant = (Integer)result.value()[1] - 1;
			if (variant >= 0 && variant < command.argumentListCount())
				viewCommandVariant(command, variant, executor);
			else
				executor.respond("That command has " + command.argumentListCount() + " variants, " + (variant + 1) + " is not in the range of 1-" + command.argumentListCount());
		}
	}
	
	void viewCommand(Command command, Executor executor)
	{
		RichStringBuilder builder = RichStringBuilder.start();
		builder.append(builder.getStyle().setMainColor(RichColor.ORANGE), "Description:").newLine().append(' ').append(builder.getStyle().setMainColor(RichColor.AQUA), command.description()).newLine();
		builder.append(builder.getStyle().setMainColor(RichColor.ORANGE), "Variants:");
		for (int index = 0; index < command.argumentListCount(); index++)
			builder.newLine().append((index + 1) + ": ").append(builder.getStyle().setMainColor(RichColor.AQUA), command.getFullName(true, index));
		builder.newLine().append(builder.getStyle().setMainColor(RichColor.ORANGE), "Use ").setStyle(builder.getStyle().setMainColor(RichColor.GRAY)).append(command.getParent().helpCommand().getFullName(true));
		builder.append(" ").append(command.name()).append(" <").append(this.getArgumentList(2).list().getArgument(1).name()).append(">").append(builder.getStyle().setMainColor(RichColor.ORANGE), " to learn about a specific variant.");
		executor.respond(builder.build());
	}
	
	static void viewCommandVariant(Command command, int variant, Executor executor)
	{
		RichStringBuilder builder = RichStringBuilder.start();
		builder.append(builder.getStyle().setMainColor(RichColor.ORANGE), "Usage:").newLine();
		builder.append(' ').append(builder.getStyle().setMainColor(RichColor.AQUA), command.getFullName(true, variant));
		ArgumentListEntry argumentList = command.getArgumentList(variant);
		builder.newLine().append(builder.getStyle().setMainColor(RichColor.ORANGE), "Description:").newLine().append(' ').append(builder.getStyle().setMainColor(RichColor.AQUA), argumentList.description());
		builder.newLine().append(builder.getStyle().setMainColor(RichColor.ORANGE), "Arguments:");
		if (argumentList.list().argumentCount() == 0)
			builder.newLine().append(builder.getStyle().setHighlighted(true), "None.");
		else
			for (int index = 0; index < argumentList.list().argumentCount(); index++)
			{
				ArgumentEntry argument = argumentList.list().getArgument(index);
				builder.newLine().append(' ').append(builder.getStyle().setHighlighted(true).setMainColor(RichColor.AQUA), argument.name()).append(" - ").append(argument.description());
			}
		executor.respond(builder.build());
	}
	
	static void viewCategory(Category category, Executor executor)
	{
		ArrayList<Command> commands = new ArrayList<>();
		ArrayList<Category> categories = new ArrayList<>();
		for (Iterator<String> iterator = category.getExecutableComponentNames(executor).iterator(); iterator.hasNext();)
		{
			CommandComponent component = category.getComponent(iterator.next(), executor);
			if (component instanceof Command)
				commands.add((Command)component);
			else if (component instanceof Category)
				categories.add((Category)component);
		}
		RichStringBuilder builder = RichStringBuilder.start();
		builder.append(builder.getStyle().setMainColor(RichColor.ORANGE), "Description:");
		builder.newLine().append(" ");
		builder.append(builder.getStyle().setHighlighted(true).setMainColor(RichColor.AQUA), category.description());
		builder.newLine().append(builder.getStyle().setMainColor(RichColor.ORANGE), "Sub-Categories:");
		builder.newLine().append(" ");
		if (categories.size() == 0)
			builder.append(builder.getStyle().setHighlighted(true).setMainColor(RichColor.AQUA), "None.");
		else
		{
			for (Iterator<Category> iterator = categories.iterator(); iterator.hasNext();)
			{
				builder.append(builder.getStyle().setHighlighted(true).setMainColor(RichColor.AQUA), iterator.next().name());
				if (iterator.hasNext())
					builder.append(", ");
			}
		}
		builder.newLine().append(builder.getStyle().setMainColor(RichColor.ORANGE), "Commands:");
		builder.newLine().append(" ");
		if (commands.size() == 0)
			builder.append(builder.getStyle().setHighlighted(true).setMainColor(RichColor.AQUA), "None.");
		else
		{
			for (Iterator<Command> iterator = commands.iterator(); iterator.hasNext();)
			{
				builder.append(builder.getStyle().setHighlighted(true).setMainColor(RichColor.AQUA), iterator.next().name());
				if (iterator.hasNext())
					builder.append(", ");
			}
		}
		builder.newLine().append(builder.getStyle().setMainColor(RichColor.ORANGE), "Use ").append(builder.getStyle().setHighlighted(true).setMainColor(RichColor.GRAY), category.helpCommand().getFullName(true, 1)).append(builder.getStyle().setMainColor(RichColor.ORANGE), " to learn more.");
		executor.respond(builder.build());
	}
}