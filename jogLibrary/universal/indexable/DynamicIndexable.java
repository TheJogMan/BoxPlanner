package jogLibrary.universal.indexable;

public abstract class DynamicIndexable<Value> extends Indexable<Value>
{
	private boolean finished = false;
	
	protected void finish()
	{
		finished = true;
	}
	
	@Override
	public boolean finished()
	{
		return finished;
	}
}