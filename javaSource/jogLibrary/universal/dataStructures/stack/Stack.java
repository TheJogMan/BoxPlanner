package jogLibrary.universal.dataStructures.stack;

public interface Stack<Type>
{
	public Type get();
	public Type pop();
	public void push(Type value);
	
	public default Type replace(Type value)
	{
		Type oldValue = pop();
		push(value);
		return oldValue;
	}
	
	public default boolean hasNext()
	{
		return get() != null;
	}
}