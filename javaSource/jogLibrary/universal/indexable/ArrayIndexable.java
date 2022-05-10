package jogLibrary.universal.indexable;

public class ArrayIndexable<Value> extends FixedIndexable<Value>
{
	Value[] array;
	
	public ArrayIndexable(Value[] array)
	{
		this.array = array;
	}
	
	@Override
	public boolean add(Value e)
	{
		throw new UnsupportedOperationException("Can not add elements to an ArrayIndexable, they must be directly inserted to an index.");
	}
	
	@Override
	public void clear()
	{
		for (int index = 0; index < array.length; index++)
			array[index] = null;
	}
	
	public int indexOf(Object object)
	{
		if (object != null)
		{
			for (int index = 0; index < array.length; index++)
			{
				if (object.equals(array[index]))
					return index;
			}
		}
		
		return -1;
	}
	
	public int lastIndexOf(Object object)
	{
		if (object != null)
		{
			for (int index = array.length - 1; index >= 0; index--)
			{
				if (object.equals(array[index]))
					return index;
			}
		}
		
		return -1;
	}
	
	@Override
	public boolean contains(Object object)
	{
		return indexOf(object) != -1;
	}
	
	@Override
	public boolean remove(Object object)
	{
		int index = indexOf(object);
		if (index != -1)
		{
			array[index] = null;
			return true;
		}
		else
			return false;
	}
	
	@Override
	public int size()
	{
		return array.length;
	}
	
	@Override
	public Value get(int index)
	{
		return array[index];
	}
	
	@Override
	public void put(int index, Value value)
	{
		array[index] = value;
	}
}