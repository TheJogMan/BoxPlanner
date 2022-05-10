package jogLibrary.dataTypes.stack;

public interface PersistentStack<StackType> extends Stack<StackType>
{
	public static <NewStackType> PersistentStack<NewStackType> create()
	{
		return new Empty<NewStackType>();
	}
	
	public static <NewStackType> PersistentStack<NewStackType> create(int capacity)
	{
		return new Empty<NewStackType>(capacity);
	}
	
	public static <NewStackType> PersistentStack<NewStackType> create(NewStackType initialEntry)
	{
		return new NonEmpty<NewStackType>(initialEntry);
	}
	
	public static <NewStackType> PersistentStack<NewStackType> create(NewStackType initialEntry, int capacity)
	{
		return new NonEmpty<NewStackType>(initialEntry, capacity);
	}
	
	@Override
	public default PersistentStack<StackType> push(StackType value)
	{
		if (capacity() < 1 || entryCount() < capacity() - 1)
		{
			return new NonEmpty<StackType>(this, value);
		}
		else
		{
			Stack.pushedFull();
			return null;
		}
	}
	
	@Override
	public default int available()
	{
		if (capacity() < 1)
		{
			return -1;
		}
		else
		{
			return capacity() - entryCount();
		}
	}
	
	@Override
	public default StackType peekPop()
	{
		Stack.peekPoppedPersistent();
		return null;
	}
	
	public static class NonEmpty<StackType> implements PersistentStack<StackType>
	{
		private StackType top;
		private PersistentStack<StackType> tail;
		private int depth;
		private int capacity;
		
		public NonEmpty(StackType value)
		{
			this(value, -1);
		}
		
		public NonEmpty(StackType value, int capacity)
		{
			this(new Empty<>(capacity), value);
		}
		
		public NonEmpty(PersistentStack<StackType> tail, StackType value)
		{
			top = value;
			this.tail = tail;
			depth = tail.entryCount() + 1;
			capacity = tail.capacity();
		}
		
		@Override
		public int entryCount()
		{
			return depth;
		}
		
		@Override
		public StackType peek()
		{
			return top;
		}
		
		@Override
		public PersistentStack<StackType> pop()
		{
			return tail;
		}
		
		@Override
		public int capacity()
		{
			return capacity;
		}
		
		@Override
		public PersistentStack<StackType> replace(StackType value)
		{
			return new NonEmpty<StackType>(tail, value);
		}
		
		@Override
		public NonEmpty<StackType> clone()
		{
			return new NonEmpty<StackType>((PersistentStack<StackType>)tail.clone(), top);
		}
	}
	
	public static class Empty<StackType> implements PersistentStack<StackType>
	{
		private int capacity;
		
		public Empty()
		{
			this(-1);
		}
		
		public Empty(int capacity)
		{
			if (capacity == 0)
			{
				Stack.requireNonZeroCapacity();
			}
			else
			{
				this.capacity = capacity;
			}
		}
		
		@Override
		public int entryCount()
		{
			return 0;
		}
		
		@Override
		public StackType peek()
		{
			Stack.peekedEmpty();
			return null;
		}
		
		@Override
		public PersistentStack<StackType> pop()
		{
			Stack.poppedEmpty();
			return null;
		}

		@Override
		public int capacity()
		{
			return capacity;
		}
		
		@Override
		public PersistentStack<StackType> replace(StackType value)
		{
			Stack.replacedEmpty();
			return null;
		}
		
		@Override
		public PersistentStack<StackType> clone()
		{
			return new Empty<StackType>(capacity);
		}
	}
}