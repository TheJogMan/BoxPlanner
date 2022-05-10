package jogLibrary.universal.indexable;

public class HoardingIndexer<Value> extends Indexer<Value>
{
	int position = 0;
	Indexable<Value> indexable;
	
	public HoardingIndexer(Value[] values)
	{
		indexable = new ArrayIndexable<Value>(values);
	}
	
	HoardingIndexer(Indexable<Value> indexable)
	{
		this.indexable = indexable;
	}
	
	HoardingIndexer(Indexable<Value> indexable, int position)
	{
		this.indexable = indexable;
		this.position = position;
	}
	
	public HoardingIndexer<Value> clone()
	{
		return new HoardingIndexer<>(indexable, position);
	}
	
	public Indexable<Value> indexable()
	{
		return indexable;
	}
	
	@Override
	public boolean finished()
	{
		return indexable.finished();
	}
	
	@Override
	protected boolean hasNextValue()
	{
		return position < indexable.size();
	}
	
	public int getPosition()
	{
		return position;
	}
	
	public void setPosition(int position)
	{
		this.position = position;
	}
	
	private void ensureDataPresence()
	{
		if (!hasNext() && !atEnd())
			indexable.waitForData();
	}
	
	public int remaining()
	{
		return indexable.size() - position;
	}

	@Override
	protected Value nextValue()
	{
		if (position >= 0 && position < indexable.size())
		{
			ensureDataPresence();
			int index = position;
			position++;
			return indexable.get(index);
		}
		else
			return null;
	}
}