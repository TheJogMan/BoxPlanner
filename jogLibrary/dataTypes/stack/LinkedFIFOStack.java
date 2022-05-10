package jogLibrary.dataTypes.stack;

public class LinkedFIFOStack<Type> implements Stack<Type>
{
	private int capacity;
	private int entryCount;
	
	private Entry<Type> top;
	private Entry<Type> bottom;
	
	public LinkedFIFOStack()
	{
		this(-1);
	}
	
	public LinkedFIFOStack(int capacity)
	{
		if (capacity < 1 && capacity != -1)
		{
			Stack.invalidCapacity();
		}
		this.capacity = capacity;
		entryCount = 0;
		top = null;
		bottom = null;
	}
	
	@Override
	public int capacity()
	{
		return capacity;
	}
	
	@Override
	public int available()
	{
		return capacity == -1 ? Integer.MAX_VALUE : capacity - entryCount;
	}
	
	@Override
	public int entryCount()
	{
		return entryCount;
	}
	
	@Override
	public Type peek()
	{
		if (entryCount == 0)
		{
			Stack.peekedEmpty();
			return null;
		}
		else
		{
			return bottom.value;
		}
	}
	
	@Override
	public LinkedFIFOStack<Type> clone()
	{
		LinkedFIFOStack<Type> newStack = new LinkedFIFOStack<Type>(capacity);
		newStack.entryCount = entryCount;
		if (bottom != null) newStack.bottom = new Entry<Type>(bottom.value);
		Entry<Type> current = bottom.next;
		Entry<Type> last = newStack.bottom;
		while (current != null)
		{
			last = new Entry<Type>(current.value, last);
			current = current.next;
		}
		newStack.top = last;
		return newStack;
	}
	
	@Override
	public Stack<Type> pop()
	{
		if (entryCount == 0)
		{
			Stack.poppedEmpty();
			return null;
		}
		else
		{
			bottom = bottom.next;
			if (bottom == null) top = null;
			entryCount--;
		}
		return this;
	}
	
	@Override
	public Stack<Type> push(Type value)
	{
		if (capacity == -1 || entryCount < capacity)
		{
			Entry<Type> entry;
			if (top == null)
			{
				entry = new Entry<Type>(value);
				bottom = entry;
			}
			else entry = new Entry<Type>(value, top);
			top = entry;
			entryCount++;
		}
		else
		{
			Stack.pushedFull();
		}
		return this;
	}
	
	@Override
	public Stack<Type> replace(Type value)
	{
		if (entryCount == 0)
		{
			Stack.replacedEmpty();
		}
		else
		{
			top.value = value;
		}
		return this;
	}
	
	@Override
	public Type peekPop()
	{
		Type value = peek();
		pop();
		return value;
	}
	
	private static class Entry<Type>
	{
		private Type value;
		private Entry<Type> next;
		
		private Entry(Type value)
		{
			this.value = value;
		}
		
		private Entry(Type value, Entry<Type> previous)
		{
			this.value = value;
			previous.next = this;
		}
	}
}