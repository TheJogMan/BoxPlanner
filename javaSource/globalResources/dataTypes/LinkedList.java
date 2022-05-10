package globalResources.dataTypes;

import java.util.Iterator;

public class LinkedList<ValueType> implements Iterable<ValueType>
{
	LinkedElement<ValueType> firstElement = null;
	LinkedElement<ValueType> lastElement = null;
	int size = 0;
	
	public int size()
	{
		return size;
	}
	
	public void clear()
	{
		firstElement = null;
		lastElement = null;
		size = 0;
	}
	
	public LinkedElement<ValueType> getFirstElement()
	{
		return firstElement;
	}
	
	public LinkedElement<ValueType> getLastElement()
	{
		return lastElement;
	}
	
	public LinkedElement<ValueType> getElement(int index)
	{
		LinkedElement<ValueType> element;
		int counter;
		if (index >= 0)
		{
			element = firstElement;
			counter = 0;
			while (element != null && counter < index)
			{
				element = element.nextElement;
				counter++;
			}
		}
		else
		{
			counter = index;
			element = lastElement;
			while (element != null && counter < 0)
			{
				element = element.previousElement;
				counter++;
			}
		}
		return element;
	}
	
	public LinkedElement<ValueType> addToBegining(ValueType value)
	{
		LinkedElement<ValueType> element = new LinkedElement<ValueType>(value);
		addToBegining(element);
		return element;
	}
	
	public LinkedElement<ValueType> add(ValueType value)
	{
		LinkedElement<ValueType> element = new LinkedElement<ValueType>(value);
		add(element);
		return element;
	}
	
	public void addToBegining(LinkedElement<ValueType> element)
	{
		if (firstElement == null)
		{
			element.list = this;
			firstElement = element;
			lastElement = element;
			size = 1;
		}
		else firstElement.addBefore(element);
	}
	
	public void add(LinkedElement<ValueType> element)
	{
		if (lastElement == null)
		{
			element.list = this;
			firstElement = element;
			lastElement = element;
			size = 1;
		}
		else lastElement.addAfter(element);
	}
	
	public static class LinkedElement<ElementType>
	{
		LinkedList<ElementType> list;
		ElementType value;
		LinkedElement<ElementType> previousElement;
		LinkedElement<ElementType> nextElement;
		
		private LinkedElement(ElementType value)
		{
			setValue(value);
		}
		
		public LinkedElement<ElementType> getPreviousElement()
		{
			return previousElement;
		}
		
		public LinkedElement<ElementType> getNextElement()
		{
			return nextElement;
		}
		
		public void swap(LinkedElement<ElementType> otherElement)
		{
			//create a copy of this elements link data
			LinkedList<ElementType> list = this.list;
			LinkedElement<ElementType> previousElement = this.previousElement;
			LinkedElement<ElementType> nextElement = this.nextElement;
			
			//swap previous/next links for each element
			if (otherElement.previousElement != null) otherElement.previousElement.nextElement = this;
			if (otherElement.nextElement != null) otherElement.nextElement.previousElement = this;
			if (this.previousElement != null) this.previousElement.nextElement = otherElement;
			if (this.nextElement != null) this.nextElement.previousElement = otherElement;
			
			//swap first/last links for each elements lists
			if (otherElement.list.firstElement.equals(otherElement)) otherElement.list.firstElement = this;
			if (otherElement.list.lastElement.equals(otherElement)) otherElement.list.lastElement = this;
			if (this.list.firstElement.equals(this)) this.list.firstElement = otherElement;
			if (this.list.lastElement.equals(this)) this.list.lastElement = otherElement;
			
			//set this elements link data to that of the other element
			list = otherElement.list;
			previousElement = otherElement.previousElement;
			nextElement = otherElement.nextElement;
			
			//set the other elements link data to that which we created a copy of earlier
			otherElement.list = list;
			otherElement.previousElement = previousElement;
			otherElement.nextElement = nextElement;
		}
		
		public ElementType getValue()
		{
			return value;
		}
		
		public void setValue(ElementType value)
		{
			this.value = value;
		}
		
		public LinkedElement<ElementType> addBefore(ElementType value)
		{
			LinkedElement<ElementType> element = new LinkedElement<ElementType>(value);
			addBefore(element);
			return element;
		}
		
		public void remove()
		{
			if (list != null)
			{
				if (this.equals(list.firstElement)) list.firstElement = nextElement;
				if (this.equals(list.lastElement)) list.lastElement = previousElement;
				if (nextElement != null) nextElement.previousElement = previousElement;
				if (previousElement != null) previousElement.nextElement = nextElement;
				list.size--;
				list = null;
			}
			nextElement = null;
			previousElement = null;
		}
		
		public void addTo(LinkedList<ElementType> list)
		{
			if (!list.equals(this.list))
			{
				if (this.list != null) remove();
				if (list.lastElement != null) list.lastElement.nextElement = this;
				this.previousElement = list.lastElement;
				list.lastElement = this;
				if (list.firstElement == null) list.firstElement = this;
				list.size++;
				this.list = list;
			}
		}
		
		public void addBefore(LinkedElement<ElementType> element)
		{
			if (element.list != null) element.remove();
			element.list = list;
			if (previousElement == null) list.firstElement = element;
			else previousElement.nextElement = element;
			element.previousElement = previousElement;
			previousElement = element;
			element.nextElement = this;
			list.size++;
		}
		
		public LinkedElement<ElementType> addAfter(ElementType value)
		{
			LinkedElement<ElementType> element = new LinkedElement<ElementType>(value);
			addAfter(element);
			return element;
		}
		
		public void addAfter(LinkedElement<ElementType> element)
		{
			if (element.list != null) element.remove();
			element.list = list;
			if (nextElement == null) list.lastElement = element;
			else nextElement.previousElement = element;
			element.nextElement = nextElement;
			nextElement = element;
			element.previousElement = this;
			list.size++;
		}
	}
	
	@Override
	public Iterator<ValueType> iterator()
	{
		return new LinkedListIterator<ValueType>(this, false);
	}
	
	public Iterator<ValueType> reverseIterator()
	{
		return new LinkedListIterator<ValueType>(this, true);
	}
	
	public LinkedListElementIterator<ValueType> elementIterator()
	{
		return new LinkedListElementIterator<ValueType>(this, false);
	}
	
	public LinkedListElementIterator<ValueType> reverseElementIterator()
	{
		return new LinkedListElementIterator<ValueType>(this, true);
	}
	
	public static class LinkedListIterator<ListType> implements Iterator<ListType>
	{
		LinkedListElementIterator<ListType> iterator;
		
		public LinkedListIterator(LinkedList<ListType> list, boolean reverse)
		{
			iterator = new LinkedListElementIterator<ListType>(list, reverse);
		}
		
		@Override
		public boolean hasNext()
		{
			return iterator.hasNext();
		}
		
		@Override
		public ListType next()
		{
			return iterator.next().value;
		}
	}
	
	public static class LinkedListElementIterator<ListType> implements Iterator<LinkedElement<ListType>>
	{
		LinkedElement<ListType> element;
		boolean reverse;
		
		public LinkedListElementIterator(LinkedList<ListType> list, boolean reverse)
		{
			element = reverse ? list.lastElement : list.firstElement;
			this.reverse = reverse;
		}
		
		@Override
		public boolean hasNext()
		{
			return element != null;
		}
		
		@Override
		public LinkedElement<ListType> next()
		{
			LinkedElement<ListType> currentElement = element;
			element = reverse ? element.previousElement : element.nextElement;
			return currentElement;
		}
	}
}