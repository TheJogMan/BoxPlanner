package globalResources.commander;

import globalResources.discorse.argument.ArgumentMultiList;
import globalResources.discorse.argument.MultiListInterpretationResult;
import globalResources.richText.RichColor;
import globalResources.richText.RichString;
import globalResources.richText.RichStringBuilder;
import globalResources.utilities.Child;
import globalResources.utilities.Logger;
import globalResources.utilities.Logger.MessageType;
import globalResources.utilities.Logger.Priority;

public abstract class AbstractCommand implements Child<CommandParent>, CommandComponent
{
	CommandParent parent;
	String name;
	RichString description;
	AbstractValidator validator;
	protected ArgumentMultiList argumentList;
	
	public AbstractCommand(CommandParent parent, String name, RichString description)
	{
		this(parent, name, description, null);
	}
	
	public AbstractCommand(CommandParent parent, String name, RichString description, AbstractValidator validator)
	{
		this.parent = parent;
		this.name = name;
		this.description = description;
		argumentList = new ArgumentMultiList();
		String lowerName = name.toLowerCase();
		if (getParent().hasCommand(name))
		{
			RichStringBuilder builder = new RichStringBuilder();
			builder.setMainColor(RichColor.RED);
			builder.append("Could not add command ");
			builder.setMainColor(RichColor.AQUA);
			builder.append(this.getClass().getName());
			builder.setMainColor(RichColor.RED);
			builder.append(" command already exists with the name ");
			builder.setMainColor(RichColor.AQUA);
			builder.append(lowerName);
			builder.setMainColor(RichColor.RED);
			builder.append(" in ");
			builder.setMainColor(RichColor.AQUA);
			builder.append(getParent().getFullName(false) + " ");
			builder.setMainColor(RichColor.GREEN);
			builder.append(getParent().commands.get(lowerName).getClass().getName());
			getParent().getLogger().log(builder.build(), MessageType.INFO, Priority.DEFAULT);
			this.parent = null;
		}
		else
		{
			if (getParent().attemptToAddCommand(this))
			{
				RichStringBuilder builder = new RichStringBuilder();
				builder.append("Command added ");
				builder.setMainColor(RichColor.AQUA);
				builder.append(name);
				builder.setMainColor(RichColor.WHITE);
				builder.append(" to ");
				builder.setMainColor(RichColor.AQUA);
				builder.append(getParent().getFullName(false) + " ");
				builder.setMainColor(RichColor.GREEN);
				builder.append(this.getClass().getName());
				getParent().getLogger().log(builder.build());
				getParent().commands.put(lowerName, this);
			}
			else
			{
				RichStringBuilder builder = new RichStringBuilder();
				builder.setMainColor(RichColor.RED);
				builder.append("Could not add command ");
				builder.setMainColor(RichColor.AQUA);
				builder.append(this.getClass().getName());
				builder.setMainColor(RichColor.RED);
				builder.append(" attempt to add to parent failed ");
				builder.setMainColor(RichColor.AQUA);
				builder.append(getParent().getFullName(false) + " ");
				builder.setMainColor(RichColor.GREEN);
				builder.append(getParent().getClass().getName());
				getParent().getLogger().log(builder.build());
			}
		}
	}
	
	public AbstractCommand(CommandParent parent, String name, String description)
	{
		this(parent, name, new RichString(description));
	}
	
	public AbstractValidator getValidator()
	{
		if (validator == null) return getParent().getValidator();
		else return validator;
	}
	
	public abstract void executeCommand(MultiListInterpretationResult result, AbstractExecutor executor);
	
	@Override
	public final String getFullName(boolean hideRoot)
	{
		String precedingName = getParent().getFullName(hideRoot);
		return isConsole() ? (hideRoot ? "" : "Root") : precedingName + (precedingName.length() > 0 ? " " : "") + getName();
	}
	
	public final String getArgumentNameString()
	{
		return argumentList.getArgumentNameString();
	}
	
	@Override
	public final AbstractCategory getCategory()
	{
		return (AbstractCategory)getParent();
	}
	
	@Override
	public final void add(CommandParent parent)
	{
		if (!hasParent())
		{
			parent.getLogger().log("Command added [" + name.toLowerCase() + "] (" + this.getClass().getName() + ")");
			parent.commands.put(name.toLowerCase(), this);
			this.parent = parent;
		}
		else
		{
			parent.getLogger().log("Could not add category (" + this.getClass().getName() + ") command already in another console [" + name.toLowerCase() + "] (" + getParent().getClass().getName() + ")",
					MessageType.INFO, Priority.DEFAULT);
		}
	}
	
	@Override
	public final void remove()
	{
		if (hasParent())
		{
			getParent().commands.remove(name.toLowerCase());
			getLogger().log("Command removed [" + name.toLowerCase() + "]", MessageType.INFO, Priority.DEFAULT);
			this.parent = null;
		}
	}
	
	@Override
	public final boolean isConsole()
	{
		return false;
	}
	
	@Override
	public final AbstractCommandConsole getConsole()
	{
		return hasParent() ? getParent().getConsole() : null;
	}
	
	@Override
	public final Logger getLogger()
	{
		return hasParent() ? getParent().getLogger() : Logger.defaultLogger;
	}
	
	@Override
	public final String getName()
	{
		return name;
	}
	
	@Override
	public final RichString getDescription()
	{
		return description.clone();
	}
	
	@Override
	public final CommandParent getParent()
	{
		return parent;
	}
}