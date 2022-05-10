package jogLibrary.dataTypes.stack;

import jogLibrary.dataTypes.DynamicArray;

public class ArrayStack<Type> implements Stack<Type>
{
	DynamicArray<Type> array;
	int capacity;
	int topIndex;
	int bottomIndex;
	int entryCount;
	Mode mode;
	
	public ArrayStack()
	{
		this(-1);
	}
	
	public ArrayStack(Mode mode)
	{
		this(-1, mode);
	}
	
	public ArrayStack(int capacity)
	{
		this(capacity, Mode.TOP);
	}
	
	public ArrayStack(int capacity, Mode mode)
	{
		this(capacity, 10, mode);
	}
	
	public ArrayStack(int capacity, int allocationSize)
	{
		this(capacity, allocationSize, Mode.TOP);
	}
	
	public ArrayStack(int capacity, int allocationSize, Mode mode)
	{
		this(capacity, allocationSize, mode, new DynamicArray<Type>(allocationSize, capacity));
	}
	
	protected ArrayStack(int capacity, int allocationSize, Mode mode, DynamicArray<Type> array)
	{
		if (capacity < 1 && capacity != -1) Stack.invalidCapacity();
		this.capacity = capacity;
		this.mode = mode;
		this.array = array;
		topIndex = -1;
		bottomIndex = 0;
		entryCount = 0;
	}
	
	public final void trim()
	{
		array.trim();
	}
	
	public final int consumedSpace()
	{
		return array.realSize();
	}
	
	@Override
	public ArrayStack<Type> clone()
	{
		ArrayStack<Type> stack = new ArrayStack<Type>(array.size(), array.allocationSize(), mode, array.clone());
		stack.topIndex = topIndex;
		stack.bottomIndex = bottomIndex;
		stack.entryCount = entryCount;
		return stack;
	}
	
	@Override
	public final int capacity()
	{
		return capacity;
	}
	
	@Override
	public final int entryCount()
	{
		return entryCount;
	}
	
	@Override
	public final int available()
	{
		return capacity - entryCount;
	}
	
	@Override
	public final ArrayStack<Type> push(Type value)
	{
		if (capacity == -1 || entryCount < capacity)
		{
			topIndex++;
			if (topIndex == capacity)
			{
				topIndex = 0;
			}
			array.set(topIndex, value);
			entryCount++;
		}
		else
		{
			Stack.pushedFull();
		}
		return this;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public final Type peek()
	{
		if (entryCount == 0)
		{
			Stack.peekedEmpty();
			return null;
		}
		else
		{
			return (Type)mode.popper.peek(this);
		}
	}
	
	@Override
	public final ArrayStack<Type> replace(Type value)
	{
		if (entryCount == 0)
		{
			Stack.replacedEmpty();
			return null;
		}
		else
		{
			array.set(topIndex, value);
			return this;
		}
	}

	@Override
	public final ArrayStack<Type> pop()
	{
		if (entryCount == 0)
		{
			Stack.poppedEmpty();
		}
		else
		{
			mode.popper.pop(this);
		}
		return this;
	}
	
	@Override
	public final Type peekPop()
	{
		Type value = peek();
		pop();
		return value;
	}
	
	public static enum Mode
	{
		TOP(
			new Popper()
			{
				@Override
				public Object peek(ArrayStack<?> stack)
				{
					return stack.array.get(stack.topIndex);
				}
				
				@Override
				public void pop(ArrayStack<?> stack)
				{
					stack.topIndex--;
					if (stack.topIndex == -1)
					{
						stack.topIndex = stack.capacity - 1;
					}
				}
			}
		),
		BOTTOM(
			new Popper()
			{
				@Override
				public Object peek(ArrayStack<?> stack)
				{
					return stack.array.get(stack.bottomIndex);
				}
				
				@Override
				public void pop(ArrayStack<?> stack)
				{
					stack.bottomIndex++;
					if (stack.bottomIndex == stack.capacity)
					{
						stack.bottomIndex = 0;
					}
				}
			}
		);
		
		Popper popper;
		
		private Mode(Popper popper)
		{
			this.popper = popper;
		}
	}
	
	static interface Popper
	{
		Object peek(ArrayStack<?> stack);
		void pop(ArrayStack<?> stack);
	}
}