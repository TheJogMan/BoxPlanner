package jogLibrary.universal.dataStructures.stack;

import jogLibrary.universal.indexable.Indexable;
import jogLibrary.universal.indexable.VectorIndexable;

public class IndexableStack<Type> implements Stack<Type>
{
	protected VectorIndexable<Type> indexable;
	
	public IndexableStack()
	{
		indexable = new VectorIndexable<>();
	}
	
	public IndexableStack(int sizeLimit)
	{
		indexable = new VectorIndexable<>(sizeLimit);
	}
	
	protected int nextIndex()
	{
		return indexable.size() - 1;
	}
	
	@Override
	public Type get()
	{
		return indexable.isEmpty() ? null : indexable.get(nextIndex());
	}
	
	@Override
	public Type pop()
	{
		Type value = get();
		indexable.remove(value);
		return value;
	}
	
	@Override
	public void push(Type value)
	{
		indexable.add(value);
	}
	
	@Override
	public boolean hasNext()
	{
		return indexable.size() > 0;
	}
	
	public Indexable<Type> indexable()
	{
		return indexable;
	}
}