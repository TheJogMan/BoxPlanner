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
import globalResources.discorse.ArgumentListContainer;
import globalResources.richText.RichString;

public class ArgumentMultiList implements ArgumentListContainer, ArgumentEntryContainer, ArgumentContainer, ArgumentInterpreter
{
	ArrayList<ArgumentList> lists;
	ArrayList<RichString> descriptions;
	AbstractValidator validator;
	boolean locked = false;
	
	public ArgumentMultiList()
	{
		this(new BlankValidator());
	}
	
	public ArgumentMultiList(AbstractValidator validator)
	{
		this.validator = validator;
		lists = new ArrayList<ArgumentList>();
		descriptions = new ArrayList<RichString>();
	}
	
	public MultiListInterpretationResult interpret(String arguments, AbstractExecutor executor)
	{
		ValidationResult validation = null;
		if (validator != null) validation = validator.validate(executor);
		if (validation == null || validation.valid())
		{
			InterpretationResult firstResult = null;
			InterpretationResult[] results = new InterpretationResult[lists.size()];
			for (int index = 0; index < lists.size(); index++)
			{
				InterpretationResult result = lists.get(index).interpret(arguments, executor);
				if (index == 0) firstResult = result;
				results[index] = result;
				if (result.valid())
				{
					for (int subIndex = index; subIndex < lists.size(); subIndex++) results[subIndex] = lists.get(subIndex).interpret(arguments, executor);
					return new MultiListInterpretationResult(validation, true, result.endReached, result.results, index, this, lists.get(index), results, result.unconsumed);
				}
			}
			if (firstResult != null) return new MultiListInterpretationResult(validation, false, firstResult.endReached, firstResult.results, 0, this, lists.get(0), results, firstResult.unconsumed);
			return new MultiListInterpretationResult(validation, true, true, new ArgumentEntryConsumptionResult[0], -1, this, null, results, arguments);
		}
		else return new MultiListInterpretationResult(validation, false, false, new ArgumentEntryConsumptionResult[0], -1, this, null, new InterpretationResult[lists.size()], arguments);
	}
	
	public void completions(String arguments, AbstractExecutor executor, ArrayList<String> completions, boolean filterCandidates)
	{
		for (int index = 0; index < lists.size(); index++) lists.get(index).completions(arguments, executor, completions, filterCandidates);
	}
	
	public void lock()
	{
		locked = true;
		for (Iterator<ArgumentList> iterator = listIterator(); iterator.hasNext();) iterator.next().lock();
	}
	
	public boolean locked()
	{
		return locked;
	}
	
	public String getArgumentNameString()
	{
		ArgumentList list = getList(0);
		if (list != null)
		{
			return list.getArgumentNameString();
		}
		else return "";
	}
	
	@Override
	public void addList(ArgumentList list)
	{
		lists.add(list);
		descriptions.add(new RichString());
	}
	
	public void setListDescription(int listIndex, RichString description)
	{
		if (lists.get(listIndex) != null)
		{
			descriptions.set(listIndex, description);
		}
	}
	
	public void setListDescription(int listIndex, String description)
	{
		setListDescription(listIndex, new RichString(description));
	}
	
	public RichString getListDescription(int listIndex)
	{
		return descriptions.get(listIndex).clone();
	}
	
	@Override
	public ArgumentList getList(int index)
	{
		return index >= 0 && index < lists.size() ? lists.get(index) : null;
	}
	
	@Override
	public int indexOfList(ArgumentList list)
	{
		return lists.indexOf(list);
	}
	
	@Override
	public void removeList(ArgumentList list)
	{
		int index = lists.indexOf(list);
		lists.remove(index);
		descriptions.remove(index);
	}
	
	@Override
	public int getListCount()
	{
		return lists.size();
	}
	
	@Override
	public boolean hasList(ArgumentList list)
	{
		return lists.contains(list);
	}
	
	@Override
	public Iterator<ArgumentList> listIterator()
	{
		return lists.iterator();
	}
	
	public void addEntry(ArgumentEntry entry, String name)
	{
		if (lists.size() == 0) addList(new ArgumentList());
		ArgumentList list = getList(0);
		if (list != null)
		{
			list.addEntry(entry, name);
		}
	}
	
	@Override
	public void addEntry(ArgumentEntry entry)
	{
		if (lists.size() == 0) addList(new ArgumentList());
		ArgumentList list = getList(0);
		if (list != null)
		{
			list.addEntry(entry);
		}
	}
	
	@Override
	public ArgumentEntry getEntry(int index)
	{
		ArgumentList list = getList(0);
		if (list != null)
		{
			return list.getEntry(index);
		}
		return null;
	}
	
	@Override
	public int indexOfEntry(ArgumentEntry entry)
	{
		ArgumentList list = getList(0);
		if (list != null)
		{
			return list.indexOfEntry(entry);
		}
		return -1;
	}
	
	@Override
	public void removeEntry(ArgumentEntry entry)
	{
		ArgumentList list = getList(0);
		if (list != null)
		{
			list.removeEntry(entry);
		}
	}
	
	@Override
	public String getFirstArgumentName(ArgumentEntry entry)
	{
		ArgumentList list = getList(0);
		if (list != null)
		{
			return list.getFirstArgumentName(entry);
		}
		return "Invalid Argument!";
	}
	
	@Override
	public int getEntryCount()
	{
		ArgumentList list = getList(0);
		if (list != null)
		{
			return list.getEntryCount();
		}
		return 0;
	}
	
	@Override
	public boolean hasEntry(ArgumentEntry entry)
	{
		ArgumentList list = getList(0);
		if (list != null)
		{
			return list.hasEntry(entry);
		}
		return false;
	}
	
	@Override
	public Iterator<ArgumentEntry> entryIterator()
	{
		ArgumentList list = getList(0);
		if (list != null)
		{
			return list.entryIterator();
		}
		return new EmptyIterator<ArgumentEntry>();
	}
	
	class EmptyIterator<Type> implements Iterator<Type>
	{
		public boolean hasNext()
		{
			return false;
		}
		
		public Type next()
		{
			return null;
		}
	}
	
	public void addArgument(Class<? extends Argument<?>> argument, String name, Object[] data)
	{
		if (lists.size() == 0) addList(new ArgumentList());
		ArgumentList list = getList(0);
		if (list != null)
		{
			list.addArgument(argument, name, data);
		}
	}
	
	public void addArgument(Class<? extends Argument<?>> argument, String name)
	{
		if (lists.size() == 0) addList(new ArgumentList());
		ArgumentList list = getList(0);
		if (list != null)
		{
			list.addArgument(argument, name);
		}
	}
	
	public void addArgument(Class<? extends Argument<?>> argument)
	{
		if (lists.size() == 0) addList(new ArgumentList());
		ArgumentList list = getList(0);
		if (list != null)
		{
			list.addArgument(argument);
		}
	}
	
	public void addArgument(Class<? extends Argument<?>> argument, Object[] data)
	{
		if (lists.size() == 0) addList(new ArgumentList());
		ArgumentList list = getList(0);
		if (list != null)
		{
			list.addArgument(argument, data);
		}
	}
	
	@Override
	public Argument<?> getArgument(int index)
	{
		ArgumentList list = getList(0);
		if (list != null)
		{
			return list.getArgument(index);
		}
		return null;
	}
	
	@Override
	public int indexOfArgument(Argument<?> argument)
	{
		ArgumentList list = getList(0);
		if (list != null)
		{
			return list.indexOfArgument(argument);
		}
		return -1;
	}
	
	@Override
	public void removeArgument(Argument<?> argument)
	{
		ArgumentList list = getList(0);
		if (list != null)
		{
			list.removeArgument(argument);
		}
	}
	
	public void removeArgument(Argument<?> argument, boolean removeEntryIfEmpty)
	{
		ArgumentList list = getList(0);
		if (list != null)
		{
			list.removeArgument(argument, removeEntryIfEmpty);
		}
	}
	
	@Override
	public String getArgumentName(Argument<?> argument)
	{
		ArgumentList list = getList(0);
		if (list != null)
		{
			return list.getArgumentName(argument);
		}
		return argument.getName();
	}
	
	@Override
	public int getArgumentCount()
	{
		ArgumentList list = getList(0);
		if (list != null)
		{
			return list.getArgumentCount();
		}
		return 0;
	}
	
	@Override
	public boolean hasArgument(Argument<?> argument)
	{
		ArgumentList list = getList(0);
		if (list != null)
		{
			return list.hasArgument(argument);
		}
		return false;
	}
	
	@Override
	public Iterator<Argument<?>> argumentIterator()
	{
		ArgumentList list = getList(0);
		if (list != null)
		{
			return list.argumentIterator();
		}
		return new EmptyIterator<Argument<?>>();
	}
}