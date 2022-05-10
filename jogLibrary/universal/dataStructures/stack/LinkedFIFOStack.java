package jogLibrary.universal.dataStructures.stack;

public class LinkedFIFOStack<Type> extends LinkedStack<Type> implements FIFOStack<Type>
{
	protected FIFOState bottom;
	
	//TODO popping last element gets a bit fucky, we need to unfucky-ify it, its currently 2:50am and i lack the mental capacity to put into words how its currently fucky, but im sure you will be able to figure it out, your a smart boi.
	
	public LinkedFIFOStack()
	{
		bottom = new FIFORootState();
		top = bottom;
	}
	
	@Override
	public Type get()
	{
		return bottom.get();
	}
	
	@Override
	public Type pop()
	{
		Type value = bottom.get();
		bottom = bottom.pop();
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
		return bottom.hasNext();
	}
	
	protected abstract class FIFOState implements State<Type>
	{
		FIFOState parent;
		FIFOState child;
		
		FIFOState(FIFOState parent)
		{
			this.parent = parent;
		}
		
		public abstract FIFOState pop();
		public abstract FIFOState set(Type value);
		public abstract boolean hasNext();
	}
	
	protected class FIFOValueState extends FIFOState
	{
		Type value;
		
		FIFOValueState(Type value, FIFOState parent)
		{
			super(parent);
			parent.child = this;
			child = new FIFORootState();
			this.value = value;
		}
		
		public Type get()
		{
			return value;
		}
		
		public FIFOState pop()
		{
			parent.child = child;
			return child;
		}
		
		@Override
		public FIFOState set(Type value)
		{
			this.value = value;
			return this;
		}
		
		@Override
		public FIFOState push(Type value)
		{
			FIFOValueState state = new FIFOValueState(value, this);
			child = state;
			return state;
		}
		
		@Override
		public boolean hasNext()
		{
			return child.isValue();
		}
		
		@Override
		public boolean isValue()
		{
			return true;
		}
	}
	
	protected class FIFORootState extends FIFOState
	{
		FIFORootState()
		{
			super(null);
			child = null;
		}
		
		@Override
		public Type get()
		{
			return null;
		}
		
		@Override
		public FIFOState pop()
		{
			return this;
		}
		
		@Override
		public FIFOState set(Type value)
		{
			FIFOValueState state = new FIFOValueState(value, this);
			bottom = state;
			return state;
		}
		
		@Override
		public FIFOState push(Type value)
		{
			FIFOValueState state = new FIFOValueState(value, this);
			bottom = state;
			return state;
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