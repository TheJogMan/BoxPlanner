package jogLibrary.applicationEngine;

import jogLibrary.universal.indexable.HoardingIndexer;
import jogLibrary.universal.indexable.VectorIndexable;

public class WindowManager
{
	VectorIndexable<Window> windows;
	
	WindowManager()
	{
		
	}
	
	long update()
	{
		return 0;
	}
	
	public boolean hasWindow()
	{
		return windows.size() > 0;
	}
	
	public Window getWindow(long windowID)
	{
		for (HoardingIndexer<Window> indexer = windows.iterator(); indexer.hasNext();)
		{
			if (indexer.get().windowID == windowID)
				return indexer.get();
			else
				indexer.next();
		}
		return null;
	}
}