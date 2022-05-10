package jogLibrary.universal.indexable;

import java.util.ArrayList;
import java.util.Collection;

public class VectorIndexable<Value> extends FixedIndexable<Value>
{
	private ArrayList<Value> values;
	
	public VectorIndexable(int initialSize)
	{
		values = new ArrayList<>(initialSize);
	}
	
	public VectorIndexable(Collection<? extends Value> values)
	{
		this.values = new ArrayList<>(values);
	}
	
	public VectorIndexable()
	{
		values = new ArrayList<>();
	}
	
	public VectorIndexable(Value[] values)
	{
		this(values.length);
		addAll(values);
	}
	
	@Override
	public boolean add(Value e)
	{
		return values.add(e);
	}
	
	@Override
	public void clear()
	{
		values.clear();
	}
	
	public int indexOf(Value value)
	{
		return values.indexOf(value);
	}
	
	@Override
	public boolean contains(Object o)
	{
		return values.contains(o);
	}
	
	@Override
	public boolean remove(Object o)
	{
		return values.remove(o);
	}
	
	@Override
	public int size()
	{
		return values.size();
	}
	
	@Override
	public Value get(int index)
	{
		return values.get(index);
	}
	
	@Override
	public void put(int index, Value value)
	{
		values.set(index, value);
	}
}