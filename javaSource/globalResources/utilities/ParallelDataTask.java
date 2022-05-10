package globalResources.utilities;

public abstract class ParallelDataTask<InputValue, OutputValue>
{
	protected abstract OutputValue process(InputValue value);
	
	private InputValue[] input;
	private OutputValue[] output;
	private int threadCount;
	
	public ParallelDataTask(InputValue[] input, OutputValue[] output, int threadCount)
	{
		this.input = input;
		this.output = output;
		this.threadCount = threadCount;
	}
	
	public final OutputValue[] run()
	{
		int indexCount = input.length > output.length ? output.length : input.length;
		int indeciesPerThread = indexCount / threadCount;
		ProcessThread[] threads = new ProcessThread[threadCount];
		int startingIndex = 0;
		for (int index = 0; index < threads.length; index++)
		{
			int endingIndex = startingIndex + indeciesPerThread;
			if (endingIndex > indexCount) endingIndex = indexCount;
			threads[index] = new ProcessThread(startingIndex, endingIndex, this);
			if (index == threads.length - 1) threads[index].run();
			else threads[index].start();
			startingIndex += indeciesPerThread;
		}
		boolean stillAlive;
		do
		{
			stillAlive = false;
			for (int index = 0; index < threads.length; index++)
			{
				if (threads[index].isAlive())
				{
					stillAlive = true;
					break;
				}
			}
		}
		while (stillAlive);
		return output;
	}
	
	private final void processIndex(int index)
	{
		output[index] = process(input[index]);
	}
	
	private static final class ProcessThread extends Thread
	{
		int startIndex;
		int endIndex;
		ParallelDataTask<?, ?> task;
		
		private ProcessThread(int startIndex, int endIndex, ParallelDataTask<?, ?> task)
		{
			this.startIndex = startIndex;
			this.endIndex = endIndex;
			this.task = task;
		}
		
		@Override
		public void run()
		{
			for (int index = startIndex; index < endIndex; index++)
			{
				task.processIndex(index);
			}
		}
	}
}