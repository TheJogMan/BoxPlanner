package globalResources.discorse;

import java.util.Iterator;

import globalResources.discorse.argument.ArgumentList;

public interface ArgumentListContainer
{
	public void addList(ArgumentList list);
	public ArgumentList getList(int index);
	public int indexOfList(ArgumentList list);
	public void removeList(ArgumentList list);
	public int getListCount();
	public boolean hasList(ArgumentList list);
	public Iterator<ArgumentList> listIterator();
}