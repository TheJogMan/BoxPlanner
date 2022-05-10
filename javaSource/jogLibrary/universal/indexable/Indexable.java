package jogLibrary.universal.indexable;

import java.util.Collection;
import java.util.Iterator;

public abstract class Indexable<Value> implements Collection<Value>
{
	public abstract Value get(int index);
	public abstract void put(int index, Value value);
	public abstract boolean finished();
	
	private WaitingPoint waitingPoint = new WaitingPoint();
	
	private static class WaitingPoint
	{
		boolean hasWaiting = false;
	}
	
	Indexable()
	{
		
	}
	
	public final void waitForData()
	{
		synchronized(waitingPoint)
		{
			waitingPoint.hasWaiting = true;
			while (waitingPoint.hasWaiting)
			{
				try
				{
					waitingPoint.wait();
				}
				catch (InterruptedException e)
				{
					
				}
			}
		}
	}
	
	protected final void wakeWaiters()
	{
		synchronized(waitingPoint)
		{
			waitingPoint.hasWaiting = false;
			waitingPoint.notifyAll();
		}
	}
	
	@Override
	public boolean addAll(Collection<? extends Value> collection)
	{
		boolean changed = false;
		for (Iterator<? extends Value> iterator = collection.iterator(); iterator.hasNext();)
		{
			if (add(iterator.next()))
				changed = true;
		}
		return changed;
	}
	
	public boolean addAll(Value[] values)
	{
		boolean changed = false;
		for (int index = 0; index < values.length; index++)
		{
			if (add(values[index]))
				changed = true;
		}
		return changed;
	}
	
	@Override
	public boolean containsAll(Collection<?> collection)
	{
		boolean contained = false;
		for (Iterator<?> iterator = collection.iterator(); iterator.hasNext();)
		{
			if (contains(iterator.next()))
				contained = true;
		}
		return contained;
	}
	
	public boolean containsAll(Value[] values)
	{
		boolean contained = false;
		for (int index = 0; index < values.length; index++)
		{
			if (contains(values[index]))
				contained = true;
		}
		return contained;
	}
	
	@Override
	public boolean isEmpty()
	{
		return size() != 0;
	}
	
	@Override
	public HoardingIndexer<Value> iterator()
	{
		return new HoardingIndexer<>(this);
	}
	
	@Override
	public boolean removeAll(Collection<?> collection)
	{
		boolean changed = false;
		for (Iterator<?> iterator = collection.iterator(); iterator.hasNext();)
		{
			if (remove(iterator.next()))
				changed = true;
		}
		return changed;
	}
	
	public boolean removeAll(Value[] values)
	{
		boolean changed = false;
		for (int index = 0; index < values.length; index++)
		{
			if (remove(values[index]))
				changed = true;
		}
		return changed;
	}
	
	@Override
	public boolean retainAll(Collection<?> collection)
	{
		boolean changed = false;
		for (Iterator<Value> iterator = iterator(); iterator.hasNext();)
		{
			Value value = iterator.next();
			if (!collection.contains(value) && remove(value))
				changed = true;
		}
		return changed;
	}
	
	public boolean retainAll(Value[] values)
	{
		boolean changed = false;
		for (Iterator<Value> iterator = iterator(); iterator.hasNext();)
		{
			Value value = iterator.next();
			boolean contained = false;
			
			for (int index = 0; index < values.length; index++)
			{
				if (value.equals(values[index]))
				{
					contained = true;
					break;
				}
			}
			
			if (!contained && remove(value))
				changed = true;
		}
		return changed;
	}
	
	@Override
	public Object[] toArray()
	{
		Object[] values = new Object[size()];
		for (int index = 0; index < size(); index++)
		{
			values[index] = get(index);
		}
		return values;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public <T> T[] toArray(T[] values)
	{
		for (int index = 0; index < size(); index++)
		{
			values[index] = (T)get(index);
		}
		return values;
	}
}