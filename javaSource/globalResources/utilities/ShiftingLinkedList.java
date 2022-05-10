package globalResources.utilities;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

/**
 * Stores values in a linked list that can optionally have a size limit.
 * <p>
 * The list can either be traversed from the beginning, or backwards from the end.
 * </p>
 * <p>
 * If the limit is set to be greater than 0, then it is considered to be enabled.  When enabled, if this limit is reached, then adding a new entry to the list will cause the first entry to be removed
 * </p>
 * @param <T> the value type to be stored in this list
 */
public class ShiftingLinkedList<T> implements Collection<T>
{
	private ShiftingElement<T> firstElement;
	private ShiftingElement<T> lastElement;
	private int capacity;
	private int size;
	
	/**
	 * Create a list with no limit
	 */
	public ShiftingLinkedList()
	{
		this(0);
	}
	
	/**
	 * Create a list with a size limit
	 * @param capacity maximum size of this list
	 */
	public ShiftingLinkedList(int capacity)
	{
		firstElement = null;
		lastElement = null;
		size = 0;
	}
	
	/**
	 * Get how many items are in the list
	 * @return size
	 */
	@Override
	public int size()
	{
		return size;
	}
	
	/**
	 * Get the size limit of this list
	 * @return capacity
	 */
	public int getCapacity()
	{
		return capacity;
	}
	
	/**
	 * Clear all items from this list
	 */
	@Override
	public void clear()
	{
		firstElement = null;
		lastElement = null;
		size = 0;
	}
	
	/**
	 * Convert this list into an ArrayList
	 * @return new ArrayList containing the items from this list
	 */
	public ArrayList<T> toList()
	{
		ArrayList<T> list = new ArrayList<T>();
		for (Iterator<T> iterator = iterator(); iterator.hasNext();) list.add(iterator.next());
		return list;
	}
	
	public T[] getArray(T[] array)
	{
		int index = 0;
		for (Iterator<T> iterator = iterator(); iterator.hasNext();)
		{
			if (index < array.length)
			{
				array[index] = iterator.next();
				index++;
			}
			else break;
		}
		return array;
	}
	
	private void prepForInsertion(boolean toBeginning)
	{
		if (capacity > 0) while (size >= capacity)
		{
			if (toBeginning) removeElement(firstElement);
			else removeElement(lastElement);
		}
	}
	
	/**
	 * Add a new entry to the list
	 * @return Whether or not this caused the first entry to be removed
	 */
	@Override
	public boolean add(T entry)
	{
		boolean removed = size >= capacity;
		prepForInsertion(true);
		ShiftingElement<T> element = new ShiftingElement<T>(this, entry, lastElement, null);
		if (firstElement == null) firstElement = element;
		lastElement = element;
		size++;
		return removed;
	}
	
	/**
	 * Add a new entry to the list
	 * @return Whether or not this caused the first entry to be removed
	 */
	public boolean addToBeginning(T entry)
	{
		boolean removed = size >= capacity;
		prepForInsertion(false);
		ShiftingElement<T> element = new ShiftingElement<T>(this, entry, null, firstElement);
		if (firstElement == null) firstElement = element;
		firstElement = element;
		size++;
		return removed;
	}
	
	/**
	 * Get the entry at a given position in the list
	 * <p>
	 * Being a linked list, this operation can become expensive as the list grows
	 * </p>
	 * @param position position to fetch from
	 * @return fetched entry
	 */
	public T get(int position)
	{
		ShiftingElement<T> currentElement;
		if (position >= 0)
		{
			currentElement = firstElement;
			while (currentElement != null && position > 0)
			{
				currentElement = currentElement.nextElement;
				position--;
			}
		}
		else
		{
			currentElement = lastElement;
			while (currentElement != null && position < 0)
			{
				currentElement = currentElement.previousElement;
				position++;
			}
		}
		if (currentElement != null) return currentElement.entry;
		else return null;
	}
	
	/**
	 * Removes an entry from the list
	 * <p>
	 * As the list grows, this operation will become expensive as the entry must be found before it can be removed.
	 * </p>
	 * @return Whether the entry was removed from the list
	 */
	@Override
	public boolean remove(Object entry)
	{
		ShiftingElement<T> element = getElement(entry).element;
		if (element == null) return false;
		removeElement(element);
		return true;
	}
	
	/**
	 * Get the index of an entry in this list
	 * <p>
	 * Being a linked list, this operation can become expensive as the list grows
	 * </p>
	 * @param entry entry to be added
	 * @return index of the entry
	 */
	public int indexOf(T entry)
	{
		return getElement(entry).position;
	}
	
	
	/**
	 * Checks for an entry in this list
	 * <p>
	 * As the list grows, this operation will become expensive as the entire list may need to be scanned for the entry.
	 * </p>
	 * @return Whether the entry was found
	 */
	@Override
	public boolean contains(Object entry)
	{
		return getElement(entry) != null;
	}
	
	private ElementData<T> getElement(Object entry)
	{
		ShiftingElement<T> currentElement = firstElement;
		int position = 0;
		while (currentElement != null && !currentElement.entry.equals(entry))
		{
			currentElement = currentElement.nextElement;
			position++;
		}
		return new ElementData<T>(currentElement, position);
	}
	
	private static class ElementData<F>
	{
		private ShiftingElement<F> element;
		private int position;
		
		private ElementData(ShiftingElement<F> element, int position)
		{
			this.element = element;
			this.position = position;
		}
	}
	
	private void removeElement(ShiftingElement<T> element)
	{
		if (element.list.equals(this))
		{
			if (element.previousElement != null && element.nextElement != null)
			{
				element.previousElement.nextElement = element.nextElement;
				element.nextElement.previousElement = element.previousElement;
			}
			else if (element.previousElement != null)
			{
				element.previousElement.nextElement = null;
			}
			else if (element.nextElement != null)
			{
				element.nextElement.previousElement = null;
			}
			if (element.equals(firstElement)) firstElement = element.nextElement;
			else if (element.equals(lastElement)) lastElement = element.previousElement;
			size--;
		}
	}
	
	/**
	 * Get an iterator to move through this list from the beginning to the end
	 * @return positive iterator
	 */
	@Override
	public ShiftingLinkedListIterator<T> iterator()
	{
		return new ShiftingLinkedListIterator<T>(this, false);
	}
	
	/**
	 * Get an iterator to move through the list from the end to the beginning
	 * @return negative iterator
	 */
	public ShiftingLinkedListIterator<T> reverseIterator()
	{
		return new ShiftingLinkedListIterator<T>(this, true);
	}
	
	private static class ShiftingElement<V>
	{
		private ShiftingLinkedList<V> list;
		private V entry;
		private ShiftingElement<V> previousElement;
		private ShiftingElement<V> nextElement;
		
		private ShiftingElement(ShiftingLinkedList<V> list, V entry, ShiftingElement<V> previousElement, ShiftingElement<V> nextElement)
		{
			this.list = list;
			this.entry = entry;
			this.previousElement = previousElement;
			this.nextElement = nextElement;
			if (previousElement != null) previousElement.nextElement = this;
		}
	}
	
	/**
	 * Iterator for a ShiftingLinkedList
	 * @param <C> the value type of the entries in the list
	 */
	public static class ShiftingLinkedListIterator<C> implements Iterator<C>
	{
		private boolean reverse;
		private ShiftingLinkedList<C> list;
		private ShiftingElement<C> currentElement;
		
		/**
		 * Creates an iterator for a list
		 * @param list the list to iterate through
		 * @param reverse Direction of iteration
		 */
		public ShiftingLinkedListIterator(ShiftingLinkedList<C> list, boolean reverse)
		{
			this.reverse = reverse;
			this.list = list;
			if (reverse) currentElement = list.lastElement;
			else currentElement = list.firstElement;
		}
		
		/**
		 * Whether this iterator is traveling in reverse
		 * @return Direction of iteration
		 */
		public boolean isReverse()
		{
			return reverse;
		}
		
		/**
		 * Get the list being iterated through
		 * @return list
		 */
		public ShiftingLinkedList<C> getList()
		{
			return list;
		}
		
		@Override
		public boolean hasNext()
		{
			return currentElement != null;
		}
		
		@Override
		public C next()
		{
			if (currentElement != null)
			{
				C value = currentElement.entry;
				if (reverse) currentElement = currentElement.previousElement;
				else currentElement = currentElement.nextElement;
				return value;
			}
			else return null;
		}
	}
	
	@Override
	public boolean addAll(Collection<? extends T> arg0)
	{
		for (Iterator<? extends T> iterator = arg0.iterator(); iterator.hasNext();) add(iterator.next());
		return true;
	}

	@Override
	public boolean containsAll(Collection<?> arg0)
	{
		for (Iterator<?> iterator = arg0.iterator(); iterator.hasNext();) if (!contains(iterator.next())) return false;
		return true;
	}

	@Override
	public boolean isEmpty()
	{
		return size != 0;
	}

	@Override
	public boolean removeAll(Collection<?> arg0)
	{
		boolean changed = false;
		for (Iterator<?> iterator = arg0.iterator(); iterator.hasNext();) if (remove(iterator.next())) changed = true;
		return changed;
	}

	@Override
	public boolean retainAll(Collection<?> arg0)
	{
		ArrayList<ShiftingElement<T>> elementsToRemove = new ArrayList<ShiftingElement<T>>();
		ShiftingElement<T> currentElement = firstElement;
		while (currentElement != null)
		{
			if (!arg0.contains(currentElement.entry)) elementsToRemove.add(currentElement);
			currentElement = currentElement.nextElement;
		}
		for (Iterator<ShiftingElement<T>> iterator = elementsToRemove.iterator(); iterator.hasNext();) remove(iterator.next());
		return elementsToRemove.size() > 0;
	}

	@Override
	public Object[] toArray()
	{
		return toList().toArray();
	}

	@Override
	public <B> B[] toArray(B[] a)
	{
		return toList().toArray(a);
	}
}