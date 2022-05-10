package jogLibrary.universal.commander.command;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import jogLibrary.universal.Result;
import jogLibrary.universal.ReturnResult;
import jogLibrary.universal.commander.Executor;
import jogLibrary.universal.dataStructures.data.values.StringValue;
import jogLibrary.universal.indexable.Consumer.ConsumerResult;
import jogLibrary.universal.indexable.HoardingIndexer;
import jogLibrary.universal.richString.RichColor;
import jogLibrary.universal.richString.RichString;
import jogLibrary.universal.richString.RichStringBuilder;

public class Category extends CommandComponent
{
	HashMap<String, CommandComponent> components = new HashMap<>();
	ArrayList<ContextualizedComponentSource> contextualSources = new ArrayList<>();
	private final boolean isConsole;
	HelpCommand helpCommand;
	
	Category(String name, RichString description, Object[] preInitData)
	{
		super(null, name, description, false);
		isConsole = true;
		categoryPreInit(preInitData);
		helpCommand = new HelpCommand(this, getConsole().helpCommandName);
	}
	
	Category(Category parent, String name, RichString description, Object[] preInitData, boolean addToParent)
	{
		super(parent, name, description, addToParent);
		isConsole = false;
		categoryPreInit(preInitData);
		helpCommand = new HelpCommand(this, getConsole().helpCommandName);
	}
	
	public Category(Category parent, String name, RichString description)
	{
		this(parent, name, description, new Object[0]);
	}
	
	public Category(Category parent, String name, String description)
	{
		this(parent, name, new RichString(description), new Object[0]);
	}
	
	protected Category(Category parent, String name, RichString description, Object[] preInitData)
	{
		this(parent, name, description, preInitData, true);
	}
	
	public final void addContextualSource(ContextualizedComponentSource source)
	{
		contextualSources.add(source);
	}
	
	public final void removeContextualSource(ContextualizedComponentSource source)
	{
		contextualSources.remove(source);
	}
	
	protected void categoryPreInit(Object[] preInitData)
	{
		
	}
	
	@Override
	public List<String> getCompletions(HoardingIndexer<Character> source, Executor executor)
	{
		if (!canExecute(executor).success())
			return new ArrayList<>();
		
		
		String token = "";
		ReturnResult<String> result = StringValue.consumeString(source, ' ');
		source.next();
		if (result.success())
			token = result.value();
		token = token.toLowerCase();
		
		Context context = getContext(executor);
		if (context.hasComponent(token) && context.getComponent(token).canExecute(executor).success())
		{
			return getComponent(token, executor).getCompletions(source, executor);
		}
		else
		{
			ArrayList<String> completions = new ArrayList<>();
			for (Iterator<CommandComponent> iterator = context.iterator(); iterator.hasNext();)
			{
				CommandComponent component = iterator.next();
				if (component.canExecute(executor).success() && component.name().toLowerCase().startsWith(token))
					completions.add(component.name());
			}
			return completions;
		}
	}
	
	@Override
	public ReturnResult<Boolean> interpret(HoardingIndexer<Character> source, Executor executor)
	{
		Result canExecute = canExecute(executor);
		if (!canExecute.success())
		{
			if (isConsole)
				executor.respond(RichStringBuilder.start().append("You can not run any commands in this console: ").append(canExecute.description()).build());
			else
				executor.respond(RichStringBuilder.start().append("You can not run any commands in this category: ").append(canExecute.description()).build());
			return new ReturnResult<>(false, false);
		}
		
		ConsumerResult<String, Character> result = StringValue.consumeString(source, ' ');
		Context context = getContext(executor);
		source.next();
		if (result.success())
		{
			String token = result.value();
			if (token.length() == 0)
			{
				HelpCommand.viewCategory(this, executor);
				return new ReturnResult<>(true);
			}
			else if (context.hasComponent(token))
				return context.getComponent(token).interpret(source, executor);
			else
			{
				RichStringBuilder builder = RichStringBuilder.start();
				builder.append(builder.getStyle().setMainColor(RichColor.RED), "There is no sub-category or command with that name, try running ");
				builder.append(builder.getStyle().setMainColor(RichColor.AQUA).setHighlighted(true), helpCommand.getFullName(true));
				builder.append(builder.getStyle().setMainColor(RichColor.RED), " to see available options.");
				executor.respond(builder.build());
				return new ReturnResult<>(false, false);
			}
		}
		else
		{
			executor.respond(RichStringBuilder.start().append("Invalid input.").build());
			return new ReturnResult<>(false, false);
		}
	}
	
	protected Result addComponent(CommandComponent component)
	{
		if (components.containsKey(component.name().toLowerCase()))
			return new Result("There is already a component with that name");
		else
		{
			components.put(component.name().toLowerCase(), component);
			return new Result();
		}
	}
	
	@Override
	public final String getFullName(boolean includePrefixCharacter)
	{
		if (isConsole)
			if (includePrefixCharacter)
				return "" + getConsole().prefix;
			else
				return "";
		else
		{
			if (parent.isConsole())
				return parent.getFullName(includePrefixCharacter) + name();
			else
				return parent.getFullName(includePrefixCharacter) + " " + name();
		}
	}
	
	@Override
	public final Console getConsole()
	{
		if (isConsole)
			return (Console)this;
		else
			return parent.getConsole();
	}
	
	public final HelpCommand helpCommand()
	{
		return helpCommand;
	}
	
	public final boolean isConsole()
	{
		return isConsole;
	}
	
	public final CommandComponent getComponent(String name)
	{
		return getComponent(name, null);
	}
	
	public final boolean hasComponent(String name)
	{
		return hasComponent(name, null);
	}
	
	public final Set<String> getComponentNames()
	{
		return getComponentNames(null);
	}
	
	public final CommandComponent getComponent(String name, Executor executor)
	{
		return getContext(executor).getComponent(name);
	}
	
	public final boolean hasComponent(String name, Executor executor)
	{
		return getContext(executor).hasComponent(name);
	}
	
	public final Set<String> getExecutableComponentNames(Executor executor)
	{
		return getContext(executor).getExecutableComponentNames();
	}
	
	public final Set<String> getComponentNames(Executor executor)
	{
		return getContext(executor).getComponentNames();
	}
	
	public final Context getContext(Executor executor)
	{
		return new Context(executor, this);
	}
	
	public static class Context implements Iterable<CommandComponent>
	{
		Executor executor;
		Category category;
		HashMap<String, CommandComponent> contextualizedComponents;
		
		Context(Executor executor, Category category)
		{
			this.executor = executor;
			this.category = category;
			verify();
		}
		
		public class ComponentCollecter
		{
			public Result add(CommandComponent component)
			{
				if (hasComponent(component.name()))
					return new Result("There is already a component with that name");
				else
				{
					contextualizedComponents.put(component.name().toLowerCase(), component);
					return new Result();
				}
			}
		}
		
		public void verify()
		{
			contextualizedComponents = new HashMap<>();
			contextualizedComponents.putAll(category.components);
			
			if (executor != null)
			{
				ComponentCollecter collecter = new ComponentCollecter();
				category.contextualSources.forEach(source ->
				{
					if (source.canExecute(executor).success())
						source.addComponents(executor, category, collecter);
				});
			}
		}
		
		public CommandComponent getComponent(String name)
		{
			return contextualizedComponents.get(name.toLowerCase());
		}
		
		public boolean hasComponent(String name)
		{
			return contextualizedComponents.containsKey(name.toLowerCase());
		}
		
		public Set<String> getComponentNames()
		{
			return contextualizedComponents.keySet();
		}
		
		public Set<String> getExecutableComponentNames()
		{
			Set<String> components = getComponentNames();
			Set<String> executableComponents = new HashSet<String>();
			for (Iterator<String> iterator = components.iterator(); iterator.hasNext();)
			{
				String name = iterator.next();
				if (getComponent(name).canExecute(executor).success())
					executableComponents.add(name);
			}
			return executableComponents;
		}
		
		public Executor executor()
		{
			return executor;
		}
		
		public Category category()
		{
			return category;
		}
		
		@Override
		public Iterator<CommandComponent> iterator()
		{
			return contextualizedComponents.values().iterator();
		}
	}
}