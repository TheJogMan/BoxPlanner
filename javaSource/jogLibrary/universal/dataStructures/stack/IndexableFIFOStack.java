package jogLibrary.universal.dataStructures.stack;

public class IndexableFIFOStack<Type> extends IndexableStack<Type> implements FIFOStack<Type>
{
	public IndexableFIFOStack()
	{
		super();
	}
	
	public IndexableFIFOStack(int sizeLimit)
	{
		super(sizeLimit);
	}
	
	@Override
	protected int nextIndex()
	{
		return 0;
	}
}