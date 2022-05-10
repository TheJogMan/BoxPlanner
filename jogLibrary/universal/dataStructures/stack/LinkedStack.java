package jogLibrary.universal.dataStructures.stack;

public class LinkedStack<Type> implements Stack<Type>
{
	protected State<Type> top;
	
	public LinkedStack()
	{
		top = new RootState<>();
	}
	
	@Override
	public Type get()
	{
		return top.get();
	}
	
	@Override
	public Type pop()
	{
		Type value = top.get();
		top = top.pop();
		return value;
	}
	
	@Override
	public void push(Type value)
	{
		top = top.push(value);
	}
	
	@Override
	public Type replace(Type value)
	{
		Type oldValue = top.get();
		top = top.set(value);
		return oldValue;
	}
	
	@Override
	public boolean hasNext()
	{
		return top.hasNext();
	}
	
	protected static interface State<Type>
	{
		Type get();
		State<Type> pop();
		State<Type> set(Type value);
		boolean hasNext();
		boolean isValue();
		
		default State<Type> push(Type value)
		{
			return new ValueState<>(value, this);
		}
	}
	
	protected static class ValueState<Type> implements State<Type>
	{
		Type value;
		State<Type> parent;
		
		ValueState(Type value, State<Type> parent)
		{
			this.value = value;
			this.parent = parent;
		}
		
		public Type get()
		{
			return value;
		}
		
		public State<Type> pop()
		{
			return parent;
		}
		
		@Override
		public State<Type> set(Type value)
		{
			this.value = value;
			return this;
		}
		
		@Override
		public boolean hasNext()
		{
			return parent.isValue();
		}
		
		@Override
		public boolean isValue()
		{
			return true;
		}
	}
	
	protected static class RootState<Type> implements State<Type>
	{
		@Override
		public Type get()
		{
			return null;
		}
		
		@Override
		public State<Type> pop()
		{
			return this;
		}
		
		@Override
		public State<Type> set(Type value)
		{
			return new ValueState<>(value, this);
		}
		
		@Override
		public boolean hasNext()
		{
			return false;
		}
		
		@Override
		public boolean isValue()
		{
			return false;
		}
	}
}