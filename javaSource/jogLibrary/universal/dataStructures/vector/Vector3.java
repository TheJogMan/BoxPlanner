package jogLibrary.universal.dataStructures.vector;

public class Vector3<Type extends Number> extends Vector<Type>
{
	static final int X = Vector2.X;
	static final int Y = Vector2.Y;
	static final int Z = Y + 1;
	
	public Vector3(Type x, Type y, Type z)
	{
		super(3);
		set(x, y, z);
	}
	
	public void set(Type x, Type y, Type z)
	{
		setX(x);
		setY(y);
		setZ(z);
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
	
	public void setZ(Type z)
	{
		setComponent(Z, z);
	}
	
	public Type z()
	{
		return getComponent(Z);
	}
	
	public void set(Vector3<Type> vector)
	{
		set(vector.x(), vector.y(), vector.z());
	}
	
	@Override
	public Vector3<Type> clone()
	{
		return new Vector3<>(x(), y(), z());
	}
}