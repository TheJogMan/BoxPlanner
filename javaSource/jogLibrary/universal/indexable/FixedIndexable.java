package jogLibrary.universal.indexable;

public abstract class FixedIndexable<Value> extends DynamicIndexable<Value>
{
	@Override
	public final boolean finished()
	{
		return true;
	}
}