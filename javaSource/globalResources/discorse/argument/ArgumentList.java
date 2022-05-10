package globalResources.discorse.argument;

import java.util.ArrayList;
import java.util.Iterator;

import globalResources.commander.AbstractExecutor;
import globalResources.commander.AbstractValidator;
import globalResources.commander.BlankValidator;
import globalResources.commander.ValidationResult;
import globalResources.discorse.ArgumentContainer;
import globalResources.discorse.ArgumentEntryContainer;
import globalResources.discorse.ArgumentInterpreter;

public class ArgumentList implements ArgumentContainer, ArgumentEntryContainer, ArgumentInterpreter
{
	ArrayList<ArgumentListEntry> arguments;
	AbstractValidator validator;
	boolean locked = false;
	
	public ArgumentList()
	{
		this(new BlankValidator());
	}
	
	public ArgumentList(AbstractValidator validator)
	{
		arguments = new ArrayList<ArgumentListEntry>();
		this.validator = validator;
	}
	
	public InterpretationResult interpret(String arguments, AbstractExecutor executor)
	{
		ValidationResult validation = null;
		if (validator != null) validation = validator.validate(executor);
		if (validation == null || validation.valid())
		{
			ArgumentEntryConsumptionResult[] results = new ArgumentEntryConsumptionResult[this.arguments.size()];
			boolean valid = true;
			boolean endReached = false;
			for (int index = 0; index < this.arguments.size(); index++)
			{
				results[index] = this.arguments.get(index).argument.consume(arguments, executor);
				if (results[index].getConsumedLength() + 1 < arguments.length()) arguments = arguments.substring(results[index].getConsumedLength() + 1);
				else
				{
					arguments = "";
					endReached = true;
				}
				if (!results[index].valid()) valid = false;
			}
			return new InterpretationResult(validation, valid, endReached, results, this, arguments);
		}
		else return new InterpretationResult(validation, false, false, new ArgumentEntryConsumptionResult[this.arguments.size()], this, arguments);
	}
	
	public void completions(String arguments, AbstractExecutor executor, ArrayList<String> completions, boolean filterCandidates)
	{
		InterpretationResult result = interpret(arguments, executor);
		if (!result.valid())
		{
			ArgumentEntry entry = this.arguments.get(result.getInvalidArgumentIndex()).argument;
			for (Iterator<Argument<?>> iterator = entry.argumentIterator(); iterator.hasNext();)
			{
				if (filterCandidates)
				{
					ArrayList<String> candidates = new ArrayList<String>();
					iterator.next().getCompletions(result.unconsumed, candidates, executor);
					String filter = result.results[result.getInvalidArgumentIndex()].getConsumed().toLowerCase();
					for (Iterator<String> candidateIterator = candidates.iterator(); candidateIterator.hasNext();)
					{
						String candidate = candidateIterator.next();
						if (candidate.toLowerCase().startsWith(filter)) completions.add(candidate);
					}
				}
				else iterator.next().getCompletions(result.unconsumed, completions, executor);
			}
		}
	}
	
	public String getArgumentNameString()
	{
		String list = "";
		for (int index = 0; index < arguments.size(); index++)
		{
			list += "<" + arguments.get(index).name + ">";
			if (index < arguments.size() - 1) list += " ";
		}
		return list;
	}
	
	public void lock()
	{
		locked = true;
		for (Iterator<ArgumentEntry> iterator = entryIterator(); iterator.hasNext();) iterator.next().lock();
	}
	
	public boolean locked()
	{
		return locked;
	}
	
	public void addArgument(Class<? extends Argument<?>> argument, String name, Object[] data)
	{
		ArgumentEntry entry = new ArgumentEntry();
		entry.addArgument(argument, data);
		addEntry(entry, name);
	}
	
	public void addArgument(Class<? extends Argument<?>> argument, String name)
	{
		addArgument(argument, name, new Object[0]);
	}
	
	public void addArgument(Class<? extends Argument<?>> argument)
	{
		addArgument(argument, null, new Object[0]);
	}
	
	public void addArgument(Class<? extends Argument<?>> argument, Object[] data)
	{
		addArgument(argument, null, data);
	}
	
	public void addEntry(ArgumentEntry argument, String name)
	{
		if (!locked)
		{
			if (name == null) name = argument.getName();
			arguments.add(new ArgumentListEntry(argument, name));
		}
	}
	
	public void addEntry(ArgumentEntry argument)
	{
		addEntry(argument, null);
	}
	
	public int getArgumentCount()
	{
		return arguments.size();
	}
	
	public Argument<?> getArgument(int index)
	{
		return (index >= 0 && index < arguments.size()) ? arguments.get(index).argument.getArgument(0) : null;
	}
	
	public ArgumentEntry getEntry(int index)
	{
		return (index >= 0 && index < arguments.size()) ? arguments.get(index).argument : null;
	}
	
	public void removeEntry(ArgumentEntry entry)
	{
		if (!locked) arguments.remove(indexOfEntry(entry));
	}
	
	public int indexOfEntry(ArgumentEntry argument)
	{
		for (int index = 0; index < arguments.size(); index++)
		{
			if (arguments.get(index).argument.equals(argument)) return index;
		}
		return -1;
	}
	
	public ArrayList<String> getCompletions(String argumentString, AbstractExecutor executor)
	{
		return getCompletions(argumentString, new ArrayList<String>(), executor);
	}
	
	public ArrayList<String> getCompletions(String argumentString, ArrayList<String> completions, AbstractExecutor executor)
	{
		for (int index = 0; index < arguments.size(); index++) arguments.get(index).argument.getCompletions(argumentString, completions, executor);
		return completions;
	}
	
	class ArgumentListEntry
	{
		ArgumentEntry argument;
		String name;
		
		ArgumentListEntry(ArgumentEntry argument, String name)
		{
			this.argument = argument;
			this.name = name;
		}
	}
	
	public Iterator<ArgumentEntry> entryIterator()
	{
		return new ArgumentListEntryIterator();
	}
	
	class ArgumentListEntryIterator implements Iterator<ArgumentEntry>
	{
		int index = 0;
		
		@Override
		public boolean hasNext()
		{
			return index >= 0 && index < arguments.size();
		}
		
		@Override
		public ArgumentEntry next()
		{
			ArgumentEntry argument = hasNext() ? arguments.get(index).argument : null;
			index++;
			return argument;
		}
	}
	
	@Override
	public boolean hasArgument(Argument<?> argument)
	{
		for (int index = 0; index < arguments.size(); index++) if (arguments.get(index).argument.hasArgument(argument)) return true;
		return false;
	}
	
	@Override
	public int indexOfArgument(Argument<?> argument)
	{
		for (int index = 0; index < arguments.size(); index++) if (arguments.get(index).argument.hasArgument(argument)) return index;
		return -1;
	}
	
	public void removeArgument(Argument<?> argument, boolean removeEntryIfEmpty)
	{
		int index = indexOfArgument(argument);
		if (index >= 0 && index < arguments.size())
		{
			ArgumentEntry entry = arguments.get(index).argument;
			entry.removeArgument(argument);
			if (removeEntryIfEmpty && entry.getArgumentCount() == 0) removeEntry(entry);
		}
	}
	
	@Override
	public void removeArgument(Argument<?> argument)
	{
		removeArgument(argument, true);
	}

	@Override
	public String getArgumentName(Argument<?> argument)
	{
		int index = indexOfArgument(argument);
		if (index >= 0 && index < arguments.size()) return arguments.get(index).name;
		else return "Invalid Argument!";
	}
	
	@Override
	public Iterator<Argument<?>> argumentIterator()
	{
		return new ArgumentIterator();
	}
	
	class ArgumentIterator implements Iterator<Argument<?>>
	{
		int index = 0;
		
		public boolean hasNext()
		{
			return index >= 0 && index < arguments.size();
		}
		
		public Argument<?> next()
		{
			Argument<?> argument = arguments.get(index).argument.getArgument(0);
			index++;
			return argument;
		}
	}
	
	@Override
	public String getFirstArgumentName(ArgumentEntry entry)
	{
		int index = indexOfEntry(entry);
		if (index >= 0 && index < arguments.size()) return arguments.get(index).argument.getName();
		return "Invalid Argument!";
	}
	
	@Override
	public int getEntryCount()
	{
		return arguments.size();
	}
	
	@Override
	public boolean hasEntry(ArgumentEntry entry)
	{
		int index = indexOfEntry(entry);
		return index >= 0 && index < arguments.size();
	}
}