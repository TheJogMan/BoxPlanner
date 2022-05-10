package jogLibrary.universal.commander.argument.arguments;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import jogLibrary.universal.ReturnResult;
import jogLibrary.universal.commander.Executor;
import jogLibrary.universal.commander.argument.Argument;
import jogLibrary.universal.commander.command.Category;
import jogLibrary.universal.commander.command.Category.Context;
import jogLibrary.universal.commander.command.CommandComponent;
import jogLibrary.universal.dataStructures.data.values.StringValue;
import jogLibrary.universal.indexable.HoardingIndexer;
import jogLibrary.universal.richString.RichString;

public class CommandComponentArgument extends Argument<CommandComponent>
{
	@Override
	public ReturnResult<CommandComponent> interpret(HoardingIndexer<Character> source, Executor executor)
	{
		String token = StringValue.consumeString(source, ' ').value().toLowerCase();
		CommandComponent component = category.getComponent(token, executor);
		if (component != null && filter.isAssignableFrom(component.getClass()))
			return new ReturnResult<>(component);
		else
			return new ReturnResult<>("There is no component with that name.");
	}
	
	@Override
	public List<String> getCompletions(HoardingIndexer<Character> source, Executor executor)
	{
		String token = StringValue.consumeString(source, ' ').value();
		ArrayList<String> completions = new ArrayList<>();
		Context context = category.getContext(executor);
		for (Iterator<String> iterator = context.getComponentNames().iterator(); iterator.hasNext();)
		{
			String name = iterator.next();
			if (name.toLowerCase().startsWith(token) && filter.isAssignableFrom(context.getComponent(name).getClass()))
				completions.add(name);
		}
		return completions;
	}
	
	Category category;
	Class<? extends CommandComponent> filter;
	
	@SuppressWarnings("unchecked")//its actually checked, java just doesn't know it, cause java is silly
	@Override
	public void init(Object[] data)
	{
		category = (Category)data[0];
		filter = CommandComponent.class;
		if (data.length > 1)
		{
			Class<?> proposedFilter = (Class<?>)data[1];
			if (CommandComponent.class.isAssignableFrom(proposedFilter))
				filter = (Class<? extends CommandComponent>)proposedFilter;
		}
	}
	
	@Override
	public String defaultName()
	{
		return "Command Component";
	}
	
	@Override
	public RichString defaultDescription()
	{
		return null;
	}
}