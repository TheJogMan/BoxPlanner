package globalResources.utilities;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

/**
 * Creates a read only access point for a List
 * @param <T> The type to be used for the SecureList
 * @see java.util.List
 */
public class SecureList<T>
{
	private List<T> list;
	
	/**
	 * Creates a SecureList from the given list
	 * @param list the list to create a SecureList for
	 */
	public SecureList(List<T> list)
	{
		init(list);
	}
	
	/**
	 * Creates a SecureList from the given collection
	 * <p>
	 * Unlike creating a SecureList with a List, a SecureList created from a Collection will not be able to work with a live reference to the original Collection.
	 * </p>
	 * <p>
	 * If the given Collection is an instance of a List, then the SecureList will be created in that manner to have the live reference.
	 * </p>
	 * @param collection the list to create a SecureList for
	 */
	public SecureList(Collection<T> collection)
	{
		if (collection instanceof List) init((List<T>)collection);
		else
		{
			ArrayList<T> list = new ArrayList<T>();
			list.addAll(collection);
			init(list);
		}
	}
	
	private void init(List<T> list)
	{
		this.list = list;
	}
	
	/**
	 * Adds the contents of this SecureList to another list
	 * @param otherList the list to be added to
	 */
	public void addTo(List<T> otherList)
	{
		for (Iterator<T> iterator = list.iterator(); iterator.hasNext();)
		{
			otherList.add(iterator.next());
		}
	}
	
	/**
	 * Gets an Iterator for this SecureList
	 * @return iterator for this SecureList
	 */
	public Iterator<T> iterator()
	{
		return list.iterator();
	}
	
	/**
	 * Gets the object at the given index
	 * @param index index to get from
	 * @return object at the given index
	 */
	public T get(int index)
	{
		return list.get(index);
	}
	
	/**
	 * Checks if this SecureList contains an object
	 * @param object the object to check
	 * @return if this SecureList contains the object
	 */
	public boolean contains(Object object)
	{
		return list.contains(object);
	}
	
	/**
	 * Checks if this SecureList contains the objects in a given collection
	 * @param collection the collection to check
	 * @return if this SecureList contains the objects in the collection
	 */
	public boolean containsAll(Collection<?> collection)
	{
		return list.containsAll(collection);
	}
	
	/**
	 * Checks if the given object equals the list this SecureList is based on
	 * @param object the object to check
	 */
	public boolean equals(Object object)
	{
		return list.equals(object);
	}
	
	/**
	 * Gets the hash code for the list
	 */
	public int hashCode()
	{
		return list.hashCode();
	}
	
	/**
	 * Gets the index of an object in the list
	 * @param object the object to check
	 * @return the index of the object
	 */
	public int indexOf(Object object)
	{
		return list.indexOf(object);
	}
	
	/**
	 * Checks if this list is empty
	 * @return if this list is empty
	 */
	public boolean isEmpty()
	{
		return list.isEmpty();
	}
	
	/**
	 * Gets the last index where a given object is found in this list
	 * @param object the object to check
	 * @return the last index where the object is found
	 */
	public int lastIndexOf(Object object)
	{
		return list.lastIndexOf(object);
	}
	
	/**
	 * Gets a list iterator for this list
	 * @return list iterator for this list
	 */
	public ListIterator<?> listIterator()
	{
		return list.listIterator();
	}
	
	/**
	 * Gets a list iterator for this list starting at the given index
	 * @param index the start index
	 * @return list iterator for this list
	 */
	public ListIterator<?> listIterator(int index)
	{
		return list.listIterator(index);
	}
	
	/**
	 * Returns the size of the list
	 * @return size of the list
	 */
	public int size()
	{
		return list.size();
	}
	
	/**
	 * Creates an array with the contents of this list
	 * @return array created from this list
	 */
	@SuppressWarnings("unchecked")
	public T[] toArray()
	{
		try
		{
			return (T[])list.toArray();
		}
		catch (Exception e)
		{
			return null;
		}
	}
	
	/**
	 * Creates an array with the contents of this list
	 * @param array the array to be used
	 * @return array created from this list
	 */
	public T[] toArray(T[] array)
	{
		return list.toArray(array);
	}
	
	/**
	 * Creates a new SecureList containing objects from this list in a given range
	 * @param fromIndex start of the range
	 * @param toIndex end of the range
	 * @return the new SecureList
	 */
	public SecureList<T> subList(int fromIndex, int toIndex)
	{
		return new SecureList<T>(list.subList(fromIndex, toIndex));
	}
}