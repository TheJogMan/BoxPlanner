package jogLibrary.universal.dataStructures.vector;

public class Vector2<Type extends Number> extends Vector<Type>
{
	static final int X = 0;
	static final int Y = X + 1;
	
	public Vector2(Type x, Type y)
	{
		super(2);
		set(x, y);
	}
	
	public void set(Type x, Type y)
	{
		setX(x);
		setY(y);
	}
	
	public void setX(Type x)
	{
		setComponent(X, x);
	}
	
	public Type x()
	{
		return getComponent(X);
	}
	
	public void setY(Type y)
	{
		setComponent(Y, y);
	}
	
	public Type y()
	{
		return getComponent(Y);
	}
	
	public void set(Vector2<Type> vector)
	{
		set(vector.x(), vector.y());
	}
	
	@Override
	public Vector2<Type> clone()
	{
		return new Vector2<>(x(), y());
	}
}