package jogLibrary.dataTypes.stack;

public interface Stack<Type>
{
	//these two methods will return -1 if the stack has infinite capacity
	public int capacity();
	public int available();
	
	public int entryCount();
	public Type peek();
	public Stack<Type> clone();
	
	//a persistent stack would return a new stack object reflecting the effects of the pop/push
	//a non persistent stack would just return itself after performing the pop/push
	public Stack<Type> pop();
	public Stack<Type> push(Type value);
	public Stack<Type> replace(Type value);
	
	// peeks and pops the stack in a single call, can not be supported by a persistent stack
	public Type peekPop();
	
	/*
		following methods are to provide consistent error reporting from all stack implementations
	*/
	
	static void requireFiniteCapacity()
	{
		//get the name of the class which called this method
		String name = Thread.currentThread().getStackTrace()[2].getClassName();
		name = name.substring(name.lastIndexOf('.') + 1); //remove package prefix
		
		throw new IllegalArgumentException(name + " does not support infinite capacity.");
	}
	
	static void replacedEmpty()
	{
		throw new IllegalStateException("Can not replace the top element of an empty stack.");
	}
	
	static void peekedEmpty()
	{
		throw new IllegalStateException("Can not peek at an empty stack.");
	}
	
	static void poppedEmpty()
	{
		throw new IllegalStateException("Can not pop from an empty stack.");
	}
	
	static void pushedFull()
	{
		throw new IllegalStateException("Can not push to a full stack.");
	}
	
	static void peekPoppedPersistent()
	{
		throw new UnsupportedOperationException("Can not peekPop a persistent stack.");
	}
	
	static void requireNonZeroCapacity()
	{
		throw new IllegalArgumentException("A stack's capacity can not be zero.");
	}
	
	static void invalidCapacity()
	{
		throw new IllegalArgumentException("A stack's capacity must be greater than zero; Some implementations can accept -1 as an infinite capacity.");
	}
}