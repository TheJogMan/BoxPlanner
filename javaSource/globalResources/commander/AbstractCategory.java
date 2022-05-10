package globalResources.commander;

import globalResources.richText.RichColor;
import globalResources.richText.RichString;
import globalResources.richText.RichStringBuilder;
import globalResources.utilities.Child;
import globalResources.utilities.Logger;
import globalResources.utilities.Logger.MessageType;
import globalResources.utilities.Logger.Priority;

public class AbstractCategory extends CategoryParent implements Child<CategoryParent>
{
	CategoryParent parent;
	String name;
	RichString description;
	HelpCommand helpCommand;
	AbstractValidator validator;
	
	public AbstractCategory(CategoryParent parent, String name, RichString description)
	{
		this(parent, name, description, null);
	}
	
	public AbstractCategory(CategoryParent parent, String name, RichString description, String helpCommandName)
	{
		this(parent, name, description, helpCommandName, null);
	}
	
	AbstractCategory(CategoryParent parent, String name, RichString description, String helpCommandName, AbstractValidator validator)
	{
		this.parent = parent;
		this.name = name;
		this.description = description;
		String lowerName = name.toLowerCase();
		if (getParent() != null && getParent() != this)
		{
			if (getParent().hasCategory(name))
			{
				RichStringBuilder builder = new RichStringBuilder();
				builder.setMainColor(RichColor.RED);
				builder.append("Could not add category ");
				builder.setMainColor(RichColor.AQUA);
				builder.append(this.getClass().getName());
				builder.setMainColor(RichColor.RED);
				builder.append(" category already exists with the name ");
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
				if (getParent().attemptToAddCategory(this))
				{
					RichStringBuilder builder = new RichStringBuilder();
					builder.append("Category added ");
					builder.setMainColor(RichColor.AQUA);
					builder.append(name);
					builder.setMainColor(RichColor.WHITE);
					builder.append(" to ");
					builder.setMainColor(RichColor.AQUA);
					builder.append(getParent().getFullName(false) + " ");
					builder.setMainColor(RichColor.GREEN);
					builder.append(this.getClass().getName());
					getParent().getLogger().log(builder.build());
					getParent().categories.put(lowerName, this);
				}
				else
				{
					RichStringBuilder builder = new RichStringBuilder();
					builder.setMainColor(RichColor.RED);
					builder.append("Could not add category ");
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
		
		helpCommand = new HelpCommand(this, helpCommandName == null ? getConsole().helpCommandName : helpCommandName);
	}
	
	public AbstractCategory(CategoryParent parent, String name, String description)
	{
		this(parent, name, (new RichStringBuilder(description)).build());
	}
	
	@Override
	public AbstractValidator getValidator()
	{
		if (validator == null) return getParent().getValidator();
		else return validator;
	}
	
	public final HelpCommand getHelpCommand()
	{
		return helpCommand;
	}
	
	@Override
	public final String getFullName(boolean hideRoot)
	{
		String precedingName = isConsole() ? (hideRoot ? "" : "Root") : getParent().getFullName(hideRoot);
		return isConsole() ? (hideRoot ? "" : "Root") : precedingName + (precedingName.length() > 0 ? " " : "") + getName();
	}
	
	@Override
	public final AbstractCategory getCategory()
	{
		return (AbstractCategory)getParent();
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
	public final void add(CategoryParent parent)
	{
		if (!hasParent())
		{
			parent.getLogger().log("Category added [" + name.toLowerCase() + "] (" + this.getClass().getName() + ")");
			parent.categories.put(name.toLowerCase(), this);
			this.parent = parent;
		}
		else
		{
			parent.getLogger().log("Could not add category (" + this.getClass().getName() + ") category already in another console [" + name.toLowerCase() + "] (" + getParent().getClass().getName() + ")",
					MessageType.INFO, Priority.DEFAULT);
		}
	}
	
	@Override
	public final void remove()
	{
		if (hasParent())
		{
			parent.categories.remove(name.toLowerCase());
			getLogger().log("Command removed [" + name.toLowerCase() + "]", MessageType.INFO, Priority.DEFAULT);
			this.parent = null;
		}
	}
	
	@Override
	boolean attemptToAddCategory(AbstractCategory category)
	{
		return isConsole() ? getConsole().attemptToAddCategory(category) : true;
	}
	
	@Override
	boolean attemptToAddCommand(AbstractCommand command)
	{
		return isConsole() ? getConsole().attemptToAddCommand(command) : true;
	}
	
	@Override
	public final CategoryParent getParent()
	{
		return isConsole() ? (CategoryParent)this : parent;
	}
	
	@Override
	public final AbstractCommandConsole getConsole()
	{
		return isConsole() ? (AbstractCommandConsole)this : getParent().getConsole();
	}
	
	@Override
	public final Logger getLogger()
	{
		return isConsole() ? (((AbstractCommandConsole)this).logger != null ? ((AbstractCommandConsole)this).logger : Logger.defaultLogger) : getConsole().getLogger();
	}
	
	@Override
	public final boolean isConsole()
	{
		return this instanceof AbstractCommandConsole;
	}
}