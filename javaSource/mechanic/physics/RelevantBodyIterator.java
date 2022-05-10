package mechanic.physics;

import java.util.Iterator;

public interface RelevantBodyIterator extends Iterator<PhysicalBody>
{
	public int getBodyCount();
	public PhysicalBody getBody(int index);
	public void jumpTo(int index);
	public int getCurrentIndex();
	
	public default void reset()
	{
		jumpTo(0);
	}
}