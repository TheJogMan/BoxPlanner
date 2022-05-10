package globalResources.discorse;

import java.util.Iterator;

import globalResources.discorse.argument.ArgumentEntry;

public interface ArgumentEntryContainer
{
	public void addEntry(ArgumentEntry entry);
	public ArgumentEntry getEntry(int index);
	public int indexOfEntry(ArgumentEntry entry);
	public void removeEntry(ArgumentEntry entry);
	public String getFirstArgumentName(ArgumentEntry entry);
	public int getEntryCount();
	public boolean hasEntry(ArgumentEntry entry);
	public Iterator<ArgumentEntry> entryIterator();
}