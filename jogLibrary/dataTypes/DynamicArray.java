package jogLibrary.dataTypes;

public class DynamicArray<Type>
{
	public DynamicArray(int allocationSize)
	{
		this(allocationSize, -1);
	}
	
	public DynamicArray(int allocationSize, int maxSize)
	{
		this(allocationSize, maxSize, allocationSize);
	}
	
	private DynamicArray(int allocationSize, int maxSize, int initialSize)
	{
		if (allocationSize < 1) throw new IllegalArgumentException("Allocations must have a size of at least 1.");
		else if (allocationSize < 2) throw new SmallSizeWarning(allocationSize);
		if (maxSize != -1 && maxSize < allocationSize) throw new IllegalArgumentException();
		
		topIndex = 0;
		this.maxSize = maxSize;
		this.allocationSize = allocationSize;
		array = new Object[initialSize];
	}
	
	private int topIndex;
	private int maxSize;
	private int allocationSize;
	private Object[] array;
	
	@SuppressWarnings("unchecked")
	public final Type get(int index)
	{
		if (index < 0 || (maxSize != -1 && index >= maxSize))
		{
			throw new IndexOutOfBoundsException("Range 0-" + (maxSize - 1) + ", given index " + index);
		}
		if (index >= array.length) return null;
		else return (Type)array[index];
	}
	
	public final void set(int index, Type value)
	{
		if (index < 0 || (maxSize != -1 && index >= maxSize))
		{
			throw new IndexOutOfBoundsException("Range 0-" + (maxSize - 1) + ", given index " + index);
		}
		if (index >= array.length) growTo(index);
		array[index] = value;
		if (index > topIndex) topIndex = index;
		else if (index == topIndex && value == null)
		{
			//travel back along the array until a non-null entry is found to be the new top index
			while(array[topIndex] == null && topIndex > 0) topIndex--;
		}
	}
	
	public final void add(Type value)
	{
		try
		{
			set(topIndex + 1, value);
		}
		catch(IndexOutOfBoundsException e)
		{
			throw new IllegalStateException("DynamicArray is full.");
		}
	}
	
	public final int available()
	{
		if (maxSize == -1) return Integer.MAX_VALUE;
		else return (maxSize - topIndex) - 1;
	}
	
	public final int size()
	{
		return topIndex + 1;
	}
	
	public final int realSize()
	{
		return array.length;
	}
	
	private final void growTo(int point)
	{
		if (point > array.length - 1)
		{
			int difference = point - (array.length - 1);
			int growth = difference / allocationSize + (difference % allocationSize > 0 ? 1 : 0);
			Object[] newArray = new Object[array.length + allocationSize * growth];
			for (int index = 0; index < array.length; index++) newArray[index] = array[index];
			array = newArray;
		}
	}
	
	public final void trim()
	{
		Object[] newArray = new Object[topIndex + 1];
		for (int index = 0; index < newArray.length; index++) newArray[index] = array[index];
		array = newArray;
	}
	
	public final int allocationSize()
	{
		return allocationSize;
	}
	
	@Override
	public final DynamicArray<Type> clone()
	{
		DynamicArray<Type> newArray = new DynamicArray<Type>(allocationSize, maxSize, topIndex);
		for (int index = 0; index < newArray.array.length; index++)
		{
			newArray.array[index] = array[index];
		}
		return newArray;
	}
	
	public static class SmallSizeWarning extends IllegalArgumentException
	{
		private static final long serialVersionUID = -1105900401121361552L;
		
		private SmallSizeWarning(int givenSize)
		{
			super("An allocation size of less than 2 makes the use of a DynamicArray vs a primitive array a bit pointless");
		}
	}
}