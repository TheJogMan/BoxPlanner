package globalResources.commander;

import java.util.ArrayList;
import java.util.Iterator;

import globalResources.discorse.argument.MultiListInterpretationResult;
import globalResources.richText.RichColor;
import globalResources.richText.RichString;
import globalResources.richText.RichStringBuilder;
import globalResources.utilities.Logger;

public abstract class AbstractCommandConsole extends AbstractCategory implements CommandComponent
{
	Logger logger = Logger.defaultLogger;
	String helpCommandName;
	char prefixCharacter;
	
	public AbstractCommandConsole(String consoleName, String helpCommandName, char prefixCharacter, Logger logger, RichString description)
	{
		super(null, consoleName, description, helpCommandName);
		this.logger = logger;
		this.helpCommandName = helpCommandName;
		this.prefixCharacter = prefixCharacter;
	}
	
	public AbstractCommandConsole(String consoleName, char prefixCharacter, Logger logger, RichString description)
	{
		this(consoleName, "Help", prefixCharacter, logger, description);
	}
	
	public AbstractCommandConsole(String consoleName, String helpCommandName, char prefixCharacter, Logger logger, String description)
	{
		this(consoleName, helpCommandName, prefixCharacter, logger, (new RichStringBuilder(description)).build());
	}
	
	public AbstractCommandConsole(String consoleName, char prefixCharacter, Logger logger, String description)
	{
		this(consoleName, "Help", prefixCharacter, logger, description);
	}
	
	public final void setPrefixCharacter(char prefixCharacter)
	{
		this.prefixCharacter = prefixCharacter;
	}
	
	public final String getPrefix()
	{
		return prefixCharacter != '\n' ? prefixCharacter + "" : "";
	}
	
	protected boolean attemptToAddCommandToConsole(AbstractCommand command)
	{
		return true;
	}
	
	protected boolean attemptToAddCategoryToConsole(AbstractCategory category)
	{
		return true;
	}
	
	@Override
	boolean attemptToAddCategory(AbstractCategory category)
	{
		return attemptToAddCategoryToConsole(category);
	}
	
	@Override
	boolean attemptToAddCommand(AbstractCommand command)
	{
		return attemptToAddCommandToConsole(command);
	}
	
	public void respond(RichString response, AbstractExecutor executor)
	{
		getLogger().log(response);
	}
	
	public void respond(String response, AbstractExecutor executor)
	{
		getLogger().log(response);
	}
	
	public final void runCommand(String command)
	{
		runCommand(command, new Object[0]);
	}
	
	public final void runCommand(String command, Object[] additionalInfo)
	{
		runCommand(command, new BlankExecutor(additionalInfo, this));
	}
	
	public final void runCommand(String command, AbstractExecutor executor)
	{
		SegmentedCommandString bits = new SegmentedCommandString(command, this, executor);
		if (bits.command != null)
		{
			MultiListInterpretationResult result = bits.interpretationResult;
			if (result.valid() || (result.getExecutorValidation() == null || result.getExecutorValidation().valid()))
			{
				ValidationResult validation = null;
				AbstractValidator validator = bits.command.getValidator();
				if (validator != null) validation = validator.validate(executor);
				if (validation == null || validation.valid()) bits.command.executeCommand(result, executor);
				else executor.respond(validation.getReason());
			}
			else
			{
				if (!result.valid())
				{
					RichStringBuilder builder = new RichStringBuilder();
					builder.append(RichColor.RED, result.getInvalidReason());
					builder.newLine();
					builder.setMainColor(RichColor.ORANGE);
					builder.append("Run ");
					builder.append(RichColor.YELLOW, getPrefix() + bits.command.getCategory().getHelpCommand().getFullName() + " " + bits.command.getName());
					builder.append(" to learn how to use this command.");
					executor.respond(builder.build());
				}
				else
				{
					RichStringBuilder builder = new RichStringBuilder();
					builder.append("You can not use those arguments: ");
					builder.append(result.getExecutorValidation().getReason());
					executor.respond(builder.build());
				}
			}
		}
		else
		{
			RichStringBuilder builder = new RichStringBuilder();
			builder.setMainColor(RichColor.RED);
			builder.append("There is no such command in the ");
			builder.append(RichColor.AQUA, bits.deepestValidCategory.getFullName());
			builder.append(" category.");
			builder.newLine();
			builder.setMainColor(RichColor.ORANGE);
			builder.append("Run ");
			builder.append(RichColor.YELLOW, getConsole().getPrefix() + bits.deepestValidCategory.getHelpCommand().getFullName());
			builder.append(" to see available commands.");
			executor.respond(builder.build());
		}
	}
	
	public final ArrayList<String> getCompletions(String command, boolean filterCandidates, boolean requireSeparation)
	{
		return getCompletions(command, new BlankExecutor(new Object[0], this), filterCandidates, requireSeparation);
	}
	
	public final ArrayList<String> getCompletions(String command, Object[] extraData, boolean filterCandidates, boolean requireSeparation)
	{
		return getCompletions(command, new BlankExecutor(extraData, this), filterCandidates, requireSeparation);
	}
	
	public final ArrayList<String> getCompletions(String command, AbstractExecutor executor, boolean filterCandidates, boolean requireSeparation)
	{
		if (command.charAt(0) == prefixCharacter) command = command.length() > 0 ? command.substring(1) : "";
		SegmentedCommandString bits = new SegmentedCommandString(command, this, executor);
		ArrayList<String> completions = new ArrayList<String>();
		if (bits.command != null)
		{
			if (!requireSeparation || command.length() > bits.command.getFullName().length()) bits.command.argumentList.completions(bits.argumentString, executor, completions, filterCandidates);
		}
		else
		{
			if (filterCandidates)
			{
				ArrayList<String> candidates = new ArrayList<String>();
				for (Iterator<AbstractCategory> iterator = bits.deepestValidCategory.getCategories().iterator(); iterator.hasNext();) candidates.add(iterator.next().getName());
				for (Iterator<AbstractCommand> iterator = bits.deepestValidCategory.getCommands().iterator(); iterator.hasNext();) candidates.add(iterator.next().getName());
				String filter = bits.workingComponent.toLowerCase();
				for (Iterator<String> iterator = candidates.iterator(); iterator.hasNext();)
				{
					String candidate = iterator.next();
					if (candidate.toLowerCase().startsWith(filter)) completions.add(candidate);
				}
			}
			else
			{
				for (Iterator<AbstractCategory> iterator = bits.deepestValidCategory.getCategories().iterator(); iterator.hasNext();) completions.add(iterator.next().getName());
				for (Iterator<AbstractCommand> iterator = bits.deepestValidCategory.getCommands().iterator(); iterator.hasNext();) completions.add(iterator.next().getName());
			}
		}
		return completions;
	}
	
	@Override
	public AbstractValidator getValidator()
	{
		return new BlankValidator();
	}
}