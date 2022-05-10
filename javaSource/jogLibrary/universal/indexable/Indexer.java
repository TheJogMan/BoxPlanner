package jogLibrary.universal.indexable;

import java.util.Collection;
import java.util.Iterator;
import java.util.Stack;

public abstract class Indexer<Value> implements Iterator<Value>
{
	protected abstract Value nextValue();
	protected abstract boolean hasNextValue();
	/**
	 * Checks if the Indexable is in a finished state.
	 * @see jogLibrary.universal.indexable.Indexable#finished()
	 * @author TheJogMan
	 * @since 1.0
	 */
	public abstract boolean finished();
	private Value peekedValue = null;
	private Value filteredValue = null;
	
	public Indexer()
	{
		filterStateStack.push(new FilterState<>());
	}
	
	/**
	 * Checks if data is currently available to be retrieved.
	 * <p>
	 * May return false even if Indexer is not at the end of the Indexable.
	 * @see jogLibrary.universal.indexable.HoardingIndexer#atEnd()
	 * @author TheJogMan
	 * @since 1.0
	 */
	public boolean hasNext()
	{
		if (peekedValue != null || filteredValue != null)
			return true;
		else
			return hasNextValue();
	}
	
	public Value get()
	{
		if (peekedValue == null)
			peekedValue = next();
		return peekedValue;
	}
	
	@Override
	public Value next()
	{
		return next(true);
	}
	
	public Value next(boolean applyFilter)
	{
		Value value;
		if (peekedValue != null)
		{
			value = peekedValue;
			peekedValue = null;
		}
		else if (filteredValue != null)
		{
			value = filteredValue;
			filteredValue = null;
		}
		else
		{
			value = nextValue();
			if (applyFilter)
			{
				do
				{
					filteredValue = nextValue();
				}
				while (!atEnd() && !filterCheck(filteredValue));
			}
		}
		return value;
	}
	
	private boolean filterCheck(Value value)
	{
		for (int index = 0; index < filterStateStack.peek().filters.size(); index++)
		{
			IndexFilter<Value> filter = filterStateStack.peek().filters.get(index);
			if (!filter.filter(value))
				return false;
		}
		return true;
	}
	
	/**
	 * Checks if the indexer is at the end of the Indexable.
	 * <p>
	 * Even if data is currently not available, the Indexer may not be at the end if the Indexable is still expecting more data to be provided eventually.
	 * @see jogLibrary.universal.indexable.Indexable#finished()
	 * @author TheJogMan
	 * @since 1.0
	 */
	public boolean atEnd()
	{
		return !hasNext() && finished();
	}
	
	public VectorIndexable<Value> next(int count)
	{
		return next(count, true);
	}
	
	/**
	 * Retrieves the desired amount of elements.
	 * <p>
	 * If all elements are required, then null will be returned if not all could be retrieved.<br>
	 * If not all elements are required, but not all could be retrieved, then only the retrieved elements will be returned.
	 * @param count The amount of elements you want to retrieve.
	 * @param requireAll If all of the desired elements will be required.
	 * @author TheJogMan
	 * @since 1.0
	 */
	public VectorIndexable<Value> next(int count, boolean applyFilter)
	{
		VectorIndexable<Value> values = new VectorIndexable<>();
		int retrievedAmount = 0;
		while (retrievedAmount < count && !atEnd())
		for (int index = 0; index < count; index++)
		{
			values.add(next(applyFilter));
			retrievedAmount++;
			if (atEnd())
				break;
		}
		return values;
	}
	
	public void skip(int count, boolean applyFilter)
	{
		if (!applyFilter)
		{
			next(count, applyFilter);
		}
		else
			next(count);
	}
	
	public void skip(int count)
	{
		skip(count, true);
	}
	
	Stack<FilterState<Value>> filterStateStack = new Stack<>();
	
	public void addFilter(IndexFilter<Value> filter)
	{
		filterStateStack.peek().filters.add(filter);
	}
	
	public void removeFilter(IndexFilter<Value> filter)
	{
		filterStateStack.peek().filters.remove(filter);
	}
	
	static class FilterState<Value>
	{
		VectorIndexable<IndexFilter<Value>> filters = new VectorIndexable<>();
		
		protected FilterState<Value> clone()
		{
			FilterState<Value> state = new FilterState<>();
			state.filters.addAll(filters);
			return state;
		}
	}
	
	public Indexer<Value> pushFilterState()
	{
		filterStateStack.push(filterStateStack.peek().clone());
		return this;
	}
	
	public Indexer<Value> popFilterState()
	{
		if (filterStateStack.size() > 1)
			filterStateStack.pop();
		return this;
	}
	
	public interface IndexFilter<FilterValue>
	{
		public boolean filter(FilterValue value);
	}
	
	public static class ExclusionFilter<FilterValue> implements IndexFilter<FilterValue>
	{
		private Collection<FilterValue> values;
		
		public ExclusionFilter(FilterValue[] values)
		{
			this(new ArrayIndexable<>(values));
		}
		
		public ExclusionFilter(Collection<FilterValue> values)
		{
			this.values = values;
		}
		
		@Override
		public boolean filter(FilterValue value)
		{
			return !values.contains(value);
		}
	}
	
	public static class InclusionFilter<FilterValue> implements IndexFilter<FilterValue>
	{
		private Collection<FilterValue> values;
		
		public InclusionFilter(FilterValue[] values)
		{
			this(new ArrayIndexable<>(values));
		}
		
		public InclusionFilter(Collection<FilterValue> values)
		{
			this.values = values;
		}
		
		@Override
		public boolean filter(FilterValue value)
		{
			return values.contains(value);
		}
	}
}