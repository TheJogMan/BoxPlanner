package jogLibrary.universal.commander;

import jogLibrary.universal.Result;

public interface ExecutorFilter
{
	public void addFilter(Filter filter);
	public void removeFilter(Filter filter);
	public Result canExecute(Executor executor);
	
	public static interface Filter
	{
		public Result canExecute(Executor executor);
	}
}