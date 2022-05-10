package globalResources.dataTypes;

public final class LinkedQueue<ValueType>
{
	private QueueEntry<ValueType> firstEntry;
	private QueueEntry<ValueType> lastEntry;
	private int size = 0;
	
	public void add(ValueType value)
	{
		QueueEntry<ValueType> entry = new QueueEntry<ValueType>(value);
		if (lastEntry != null) lastEntry.nextEntry = entry;
		lastEntry = entry;
		if (firstEntry == null) firstEntry = entry;
		size++;
	}
	
	public int size()
	{
		return size;
	}
	
	public boolean hasNext()
	{
		return firstEntry != null;
	}
	
	public ValueType peek()
	{
		if (hasNext()) return firstEntry.value;
		else return null;
	}
	
	public ValueType next()
	{
		if (hasNext())
		{
			ValueType value = firstEntry.value;
			firstEntry = firstEntry.nextEntry;
			size--;
			return value;
		}
		else return null;
	}
	
	private static class QueueEntry<EntryType>
	{
		private EntryType value;
		private QueueEntry<EntryType> nextEntry;
		
		private QueueEntry(EntryType value)
		{
			this.value = value;
		}
	}
}