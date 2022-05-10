package jogLibrary.universal.dataStructures.vector;

import java.lang.reflect.ParameterizedType;

public abstract class Vector<Type extends Number>
{
	Type[] components;
	
	protected void setComponent(int component, Type value)
	{
		components[component] = value;
	}
	
	protected Type getComponent(int component)
	{
		return components[component];
	}
	
	@SuppressWarnings("unchecked")
	protected Vector(int componentCount)
	{
		components = (Type[]) new Number[componentCount];
	}
	
	@Override
	public boolean equals(Object object)
	{
		if (object == null || !(object instanceof Vector))
			return false;
		if (!((ParameterizedType)this.getClass().getGenericSuperclass()).getActualTypeArguments()[0].equals(((ParameterizedType)object.getClass().getGenericSuperclass()).getActualTypeArguments()[0]))
			return false;
		if (components.length != ((Vector<?>)object).components.length)
			return false;
		for (int index = 0; index < components.length; index++)
			if (!components[index].equals(((Vector<?>)object).components[index]))
				return false;
		return true;
	}
	
	public abstract Vector<Type> clone();
}