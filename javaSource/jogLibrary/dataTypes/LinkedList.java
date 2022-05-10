package jogLibrary.dataTypes;

import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

import jogLibrary.dataTypes.LinkedList.LinkedElementCollectionResult.CollectedLinkedElement;
import jogLibrary.dataTypes.LinkedList.LinkedElementCollectionResult.CollectionMethod;
import jogLibrary.dataTypes.LinkedList.LinkedIterator.Direction;

public class LinkedList<ListType> implements List<ListType>
{
	private LinkedElement<ListType> firstElement;
	private LinkedElement<ListType> lastElement;
	private int size;
	
	public LinkedList()
	{
		firstElement = null;
		lastElement = null;
	}
	
	public boolean add(LinkedElement<ListType> element)
	{
		if (lastElement == null)
		{
			element.setList(this);
			firstElement = element;
			lastElement = element;
		}
		else element.putAfter(lastElement);
		return true;	//always returns true per specification of Collection interface
	}
	
	public void add(int index, LinkedElement<ListType> element)
	{
		if (index < 0 || index >= size()) throw new IndexOutOfBoundsException("Got index of " + index + " for range of 0-" + (size() - 1));
		
		if (size() == 0) add(element);
		else element.putBefore(getElement(index));
	}
	
	@Override
	public boolean add(ListType value)
	{
		return add(new LinkedElement<ListType>(value));
	}
	
	@Override
	public void add(int index, ListType value)
	{
		add(index, new LinkedElement<ListType>(value));
	}
	
	@Override
	public boolean addAll(Collection<? extends ListType> collection)
	{
		boolean everChanged = false;
		for (Iterator<? extends ListType> iterator = collection.iterator(); iterator.hasNext();)
		{
			boolean changed = add(iterator.next());
			everChanged = everChanged || changed;
		}
		return everChanged;
	}
	
	@Override
	public boolean addAll(int index, Collection<? extends ListType> collection)
	{
		LinkedElement<ListType> current = getElement(index);
		for (Iterator<? extends ListType> iterator = collection.iterator(); iterator.hasNext();)
		{
			ListType value = iterator.next();
			if (current != null)
			{
				current.set(value);
				current = current.next;
			}
			else add(value);
		}
		return true;
	}
	
	@Override
	public void clear()
	{
		firstElement = null;
		lastElement = null;
		size = 0;
	}
	
	@Override
	public boolean contains(Object value)
	{
		return collect(value, CollectionMethod.ALL).element() != null;
	}
	
	@Override
	public boolean containsAll(Collection<?> collection)
	{
		if (collection.size() > size)
		{
			for (Iterator<?> iterator = collection.iterator(); iterator.hasNext();)
			{
				Object value = iterator.next();
				if (value == null) return false;
				LinkedElement<ListType> current = firstElement;
				while (current != null)
				{
					if (!value.equals(current.value)) return false;
					current = current.next;
				}
			}
		}
		else 
		{
			LinkedElement<ListType> current = firstElement;
			while (current != null)
			{
				for (Iterator<?> iterator = collection.iterator(); iterator.hasNext();)
				{
					Object value = iterator.next();
					if (value == null || !value.equals(current.value)) return false;
				}
				current = current.next;
			}
		}
		return true;
	}
	
	public LinkedElement<ListType> getFirstElement()
	{
		return firstElement;
	}
	
	public LinkedElement<ListType> getLastElement()
	{
		return lastElement;
	}
	
	public LinkedElement<ListType> getElement(int index)
	{
		if (index < -size || index >= size) throw new IndexOutOfBoundsException("Index of (" + index + ") does not fall within range of either (0 - " + (size - 1) + ") or (-" + size + " - -1)");
		if (index >= 0)
		{
			LinkedElement<ListType> current = firstElement;
			while (current != null && index > 0)
			{
				current = current.next;
				index--;
			}
			return current;
		}
		else
		{
			LinkedElement<ListType> current = lastElement;
			while (current != null && index < -1)
			{
				current = current.previous;
				index++;
			}
			return current;
		}
	}
	
	@Override
	public ListType get(int index)
	{
		LinkedElement<ListType> element = getElement(index);
		if (element == null) return null;
		else return element.value();
	}
	
	@Override
	public int indexOf(Object value)
	{
		LinkedElement<CollectedLinkedElement<ListType>> element = collect(value, CollectionMethod.FIRST).collectedElements.firstElement;
		if (element == null) return -1;
		else return element.value.index;
	}
	
	public int[] indeciesOf(Object value)
	{
		LinkedElementCollectionResult<ListType> result = collect(value, CollectionMethod.ALL);
		int[] indecies = new int[result.collectedElements.size];
		LinkedElement<CollectedLinkedElement<ListType>> current = result.collectedElements.firstElement;
		while (current != null)
		{
			indecies[current.value.instanceNumber] = current.value.index;
			current = current.next;
		}
		return indecies;
	}
	
	@Override
	public boolean isEmpty()
	{
		return size == 0;
	}
	
	@Override
	public LinkedIterator<ListType> iterator()
	{
		return firstElement.iterateFrom();
	}
	
	public LinkedIterator<ListType> reverseIterator()
	{
		return lastElement.iterateFrom(Direction.BACKWARD);
	}
	
	@Override
	public int lastIndexOf(Object value)
	{
		LinkedElement<CollectedLinkedElement<ListType>> element = collect(value, CollectionMethod.LAST).collectedElements.firstElement;
		if (element == null) return -1;
		else return element.value.index;
	}
	
	@Override
	public ListIterator<ListType> listIterator()
	{
		return firstElement.listIterateFrom();
	}
	
	public ListIterator<ListType> reverseListIterator()
	{
		return lastElement.listIterateFrom(Direction.BACKWARD);
	}
	
	@Override
	public ListIterator<ListType> listIterator(int index)
	{
		return getElement(index).listIterateFrom();
	}
	
	@Override
	public boolean remove(Object value)
	{
		LinkedElement<ListType> element = collect(value, CollectionMethod.FIRST).element();
		if (element == null) return false;
		else
		{
			element.remove();
			return true;
		}
	}
	
	public boolean removeAll(Object value)
	{
		return removeAll(collect(value, CollectionMethod.ALL).collectedElements);
	}
	
	public boolean removeLast(Object value)
	{
		LinkedElement<ListType> element = collect(value, CollectionMethod.LAST).element();
		if (element == null) return false;
		else
		{
			element.remove();
			return true;
		}
	}
	
	@Override
	public ListType remove(int index)
	{
		LinkedElement<ListType> element = getElement(index);
		if (element == null) return null;
		else
		{
			element.remove();
			return element.value;
		}
	}
	
	@Override
	public boolean removeAll(Collection<?> list)
	{
		LinkedElement<ListType> current = firstElement;
		boolean changed = false;
		while (current != null)
		{
			LinkedElement<ListType> next = current.next;
			if (list.contains(current.value))
			{
				current.remove();
				changed = true;
			}
			current = next;
		}
		return changed;
	}
	
	@Override
	public boolean retainAll(Collection<?> list)
	{
		LinkedElement<ListType> current = firstElement;
		boolean changed = false;
		while (current != null)
		{
			LinkedElement<ListType> next = current.next;
			if (!list.contains(current.value))
			{
				current.remove();
				changed = true;
			}
			current = next;
		}
		return changed;
	}
	
	@Override
	public ListType set(int index, ListType value)
	{
		LinkedElement<ListType> element = getElement(index);
		ListType previousValue = element.value;
		element.set(value);
		return previousValue;
	}
	
	@Override
	public int size()
	{
		return size;
	}
	
	@Override
	public LinkedList<ListType> subList(int beginIndex, int stopIndex)
	{
		LinkedList<ListType> subList = new LinkedList<ListType>();
		LinkedElement<ListType> current = getElement(beginIndex);
		LinkedElement<ListType> stop = getElement(stopIndex);
		while (current != null && current != stop.next)
		{
			subList.add(current.value);
			current = current.next;
		}
		return subList;
	}
	
	@Override
	public Object[] toArray()
	{
		return toArray(new Object[size()]);
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public <ArrayType> ArrayType[] toArray(ArrayType[] array)
	{
		if (array.length < size()) array = Arrays.copyOf(array, size());
		int index = 0;
		LinkedElement<ListType> current = firstElement;
		if (array.getClass().isAssignableFrom(this.getClass().getTypeParameters()[0].getClass()))
		{
			while (current != null)
			{
				array[index] = (ArrayType)current.value;
				index++;
				current = current.next;
			}
		}
		return array;
	}
	
	public static class LinkedElement<ElementType>
	{
		private ElementType value;
		private LinkedElement<ElementType> previous;
		private LinkedElement<ElementType> next;
		
		private LinkedList<ElementType> list;
		
		public void set(ElementType value)
		{
			this.value = value;
		}
		
		public ElementType value()
		{
			return value;
		}
		
		public LinkedElement<ElementType> next()
		{
			return next;
		}
		
		public LinkedElement<ElementType> previous()
		{
			return previous;
		}
		
		public LinkedList<ElementType> getList()
		{
			return list;
		}
		
		public void remove()
		{
			if (list != null)
			{
				list.size--;
				if (previous != null) previous.next = next;
				else list.firstElement = next;
				if (next != null) next.previous = previous;
				else list.lastElement = previous;
				list = null;
			}
		}
		
		private void setList(LinkedList<ElementType> list)
		{
			if ((this.list != null && !this.list.equals(list)) || (this.list == null && list != null))	//should only have to take action if the new list is different from the current one
			{
				if (this.list != null) remove();	//if already in a list, remove from it
				if (list != null) list.size++;		//if new list exists, increment its size
				this.list = list;					//set current list to new list
			}
		}
		
		private void ensureList()
		{
			if (list == null)
			{
				list = new LinkedList<ElementType>();
				list.add(this);
			}
		}
		
		public int index()
		{
			if (list == null) return 0;
			else
			{
				LinkedElement<ElementType> current = list.firstElement;
				int index = 0;
				while (current != null)
				{
					if (current.equals(this)) return index;
					index++;
					current = current.next;
				}
				return 0;
			}
		}
		
		public int reverseIndex()
		{
			if (list == null) return 0;
			else
			{
				LinkedElement<ElementType> current = list.lastElement;
				int index = -1;
				while (current != null)
				{
					if (current.equals(this)) return index;
					index--;
					current = current.previous;
				}
				return 0;
			}
		}
		
		public LinkedIterator<ElementType> iterateFrom(Direction direction)
		{
			return new LinkedIterator<ElementType>(this, direction);
		}
		
		public LinkedIterator<ElementType> iterateFrom()
		{
			return iterateFrom(Direction.FORWARD);
		}
		
		public LinkedListIterator<ElementType> listIterateFrom(Direction direction)
		{
			return new LinkedListIterator<ElementType>(this, direction);
		}
		
		public LinkedListIterator<ElementType> listIterateFrom()
		{
			return listIterateFrom(Direction.FORWARD);
		}
		
		public void putAfter(LinkedElement<ElementType> element)
		{
			if (element == null) throw new IllegalArgumentException("Previous element can not be null!");
			element.ensureList();	//if the given element isn't in a list, then create a new one to add both elements to
			setList(element.list);	//see method for details
			if (element.next != null)
			{
				element.next.previous = this;
				next = element.next;
			}
			else
			{
				element.list.lastElement = this;
				next = null;
			}
			element.next = this;
			previous = element;
		}
		
		public void putBefore(LinkedElement<ElementType> element)
		{
			if (element == null) throw new IllegalArgumentException("Following element can not be null!");
			element.ensureList();	//if the given element isn't in a list, then create a new one to add both elements to
			setList(element.list);	//see method for details
			if (element.previous != null)
			{
				element.previous.next = this;
				previous = element.previous;
			}
			else
			{
				element.list.firstElement = this;
				previous = null;
			}
			element.previous = this;
			next = element;
		}
		
		private LinkedElement()
		{
			value = null;
			previous = null;
			next = null;
			
			list = null;
		}
		
		private LinkedElement(ElementType value)
		{
			this();
			this.value = value;
		}
		
		private LinkedElement(LinkedElement<ElementType> previous)
		{
			this();
			putAfter(previous);
		}
		
		private LinkedElement(LinkedElement<ElementType> previous, ElementType value)
		{
			this(value);
			putAfter(previous);
		}
	}
	
	public static class LinkedListIterator<ListIteratorType> extends LinkedIterator<ListIteratorType> implements ListIterator<ListIteratorType>
	{
		LinkedElement<ListIteratorType> lastElement;
		
		private LinkedListIterator(LinkedElement<ListIteratorType> startingPoint, Direction direction)
		{
			super(startingPoint, direction);
		}
		
		private LinkedElement<ListIteratorType> peek(Direction direction)
		{
			Direction actualDirection = super.direction;
			if (!direction.equals(actualDirection)) actualDirection = Direction.opposite(actualDirection);
			
			if (actualDirection.equals(Direction.FORWARD)) return super.current.next;
			else return super.current.previous;
		}
		
		@Override
		public void add(ListIteratorType value)
		{
			super.current.putBefore(new LinkedElement<ListIteratorType>(value));
		}
		
		@Override
		public int nextIndex()
		{
			return peek(Direction.FORWARD).index();
		}
		
		@Override
		public int previousIndex()
		{
			return peek(Direction.BACKWARD).index();
		}
		
		@Override
		public void remove()
		{
			if (lastElement != null) lastElement.remove();
		}
		
		@Override
		public void set(ListIteratorType value)
		{
			if (lastElement != null) lastElement.set(value);
		}
		
		@Override
		public LinkedElement<ListIteratorType> nextElement()
		{
			LinkedElement<ListIteratorType> element = super.nextElement();
			lastElement = element;
			return element;
		}
		
		@Override
		public LinkedElement<ListIteratorType> previousElement()
		{
			LinkedElement<ListIteratorType> element = super.previousElement();
			lastElement = element;
			return element;
		}
	}
	
	public static class LinkedIterator<IteratorType> implements Iterator<IteratorType>
	{
		private LinkedElement<IteratorType> current;
		private Direction direction;
		
		private LinkedIterator(LinkedElement<IteratorType> startingPoint, Direction direction)
		{
			this.current = startingPoint;
			this.direction = direction;
		}
		
		public LinkedList<IteratorType> list()
		{
			return current.list;
		}
		
		public LinkedElement<IteratorType> nextElement()
		{
			if (!hasNext()) return null;
			LinkedElement<IteratorType> element;
			if (direction.equals(Direction.FORWARD))
			{
				element = current.next;
				current = current.next;
			}
			else
			{
				element = current.previous;
				current = current.previous;
			}
			return element;
		}
		
		public LinkedElement<IteratorType> previousElement()
		{
			if (!hasPrevious()) return null;
			LinkedElement<IteratorType> element;
			if (direction.equals(Direction.FORWARD))
			{
				element = current.previous;
				current = current.previous;
			}
			else
			{
				element = current.next;
				current = current.next;
			}
			return element;
		}
		
		@Override
		public boolean hasNext()
		{
			if (direction.equals(Direction.FORWARD)) return current.next != null;
			else return current.previous != null;
		}
		
		public boolean hasPrevious()
		{
			if (direction.equals(Direction.FORWARD)) return current.previous != null;
			else return current.next != null;
		}

		@Override
		public IteratorType next()
		{
			if (!hasNext()) return null;
			return nextElement().value;
		}
		
		public IteratorType previous()
		{
			if (!hasPrevious()) return null;
			return previousElement().value;
		}
		
		public enum Direction
		{
			FORWARD, BACKWARD;
			
			public static Direction opposite(Direction direction)
			{
				if (direction.equals(FORWARD)) return BACKWARD;
				else return FORWARD;
			}
		}
	}
	
	@SuppressWarnings("unchecked")
	private LinkedElementCollectionResult<ListType> collect(Object value, CollectionMethod method)
	{
		return new LinkedElementCollectionResult<ListType>(this, method, (ListType)value);
	}
	
	public static class LinkedElementCollectionResult<CollectedType>
	{
		private LinkedList<CollectedType> list;
		private LinkedList<CollectedLinkedElement<CollectedType>> collectedElements;
		private CollectionMethod collectionMethod;
		private CollectedType value;
		
		private LinkedElementCollectionResult(LinkedList<CollectedType> list, CollectionMethod method, CollectedType value)
		{
			this.list = list;
			collectionMethod = method;
			collectedElements = new LinkedList<CollectedLinkedElement<CollectedType>>();
			this.value = value;
			method.collector.collect(this);
		}
		
		public CollectedType collectedValue()
		{
			return value;
		}
		
		public LinkedList<CollectedType> list()
		{
			return list;
		}
		
		public int collectionSize()
		{
			return collectedElements.size;
		}
		
		public CollectionMethod method()
		{
			return collectionMethod;
		}
		
		@SuppressWarnings("unchecked")
		public CollectedLinkedElement<CollectedType>[] elements()
		{
			return (CollectedLinkedElement<CollectedType>[])collectedElements.toArray();
		}
		
		public LinkedElement<CollectedType> element()
		{
			if (collectedElements.firstElement == null) return null;
			else return collectedElements.firstElement.value.element;
		}
		
		@SuppressWarnings("unchecked")//not intended for use in situations where types could potentially be incompatible
		private CollectedLinkedElement<CollectedType> createCollectedElement(LinkedElement<?> element, int index, int instanceNumber)
		{
			return new CollectedLinkedElement<CollectedType>((LinkedElement<CollectedType>)element, index, this, instanceNumber);
		}
		
		public static class CollectedLinkedElement<CollectedElementType>
		{
			private LinkedElement<CollectedElementType> element;
			private int index;
			private int instanceNumber;
			private LinkedElementCollectionResult<CollectedElementType> collectionResult;
			
			private CollectedLinkedElement(LinkedElement<CollectedElementType> element, int index, LinkedElementCollectionResult<CollectedElementType> collectionResult, int instanceNumber)
			{
				this.element = element;
				this.index = index;
				this.instanceNumber = instanceNumber;
				this.collectionResult = collectionResult;
			}
			
			public LinkedElement<CollectedElementType> element()
			{
				return element;
			}
			
			public LinkedElementCollectionResult<CollectedElementType> collectionResult()
			{
				return collectionResult;
			}
			
			public int listIndex()
			{
				return index;
			}
			
			public int instanceNumber()
			{
				return instanceNumber;
			}
		}
		
		public enum CollectionMethod
		{
			FIRST((result) ->
			{
				LinkedElement<?> current = result.list.firstElement;
				int index = 0;
				int instanceNumber = 0;
				while (current != null)
				{
					if (current.value.equals(result.value))
					{
						result.collectedElements.addCollectedElement(result.createCollectedElement(current, index, instanceNumber));
						instanceNumber++;
						return;
					}
					index++;
					current = current.next;
				}
			}),
			LAST((result) ->
			{
				LinkedElement<?> current = result.list.lastElement;
				int index = -1;
				int instanceNumber = 0;
				while (current != null)
				{
					if (current.value.equals(result.value))
					{
						result.collectedElements.addCollectedElement(result.createCollectedElement(current, index, instanceNumber));
						instanceNumber++;
						return;
					}
					index--;
					current = current.previous;
				}
			}),
			ALL((result) ->
			{
				LinkedElement<?> current = result.list.firstElement;
				int index = 0;
				int instanceNumber = 0;
				while (current != null)
				{
					if (current.value.equals(result.value))
					{
						result.collectedElements.addCollectedElement(result.createCollectedElement(current, index, instanceNumber));
						instanceNumber++;
					}
					index++;
					current = current.next;
				}
			}),
			ALL_REVERSED((result) ->
			{
				LinkedElement<?> current = result.list.lastElement;
				int index = -1;
				int instanceNumber = 0;
				while (current != null)
				{
					if (current.value.equals(result.value))
					{
						result.collectedElements.addCollectedElement(result.createCollectedElement(current, index, instanceNumber));
						instanceNumber++;
					}
					index--;
					current = current.previous;
				}
			});
			
			private LinkedElementCollector collector;
			
			private CollectionMethod(LinkedElementCollector collector)
			{
				this.collector = collector;
			}
		}
		
		private static interface LinkedElementCollector
		{
			void collect(LinkedElementCollectionResult<?> result);
		}
	}
	
	//used exclusively for element collection
	@SuppressWarnings("unchecked")//not intended for use in situations where types could potentially be incompatible
	private boolean addCollectedElement(CollectedLinkedElement<?> collectedLinkedElement)
	{
		return add(new LinkedElement<ListType>((ListType)collectedLinkedElement));
	}
}