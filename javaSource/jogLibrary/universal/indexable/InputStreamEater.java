package jogLibrary.universal.indexable;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

public class InputStreamEater extends DynamicIndexable<Byte>
{
	ArrayList<Byte> data = new ArrayList<Byte>();
	InputStream stream;
	EatingThread thread;
	
	public InputStreamEater(InputStream stream)
	{
		if (stream == null)
			throw new RuntimeException("Input stream can not be null.");
		this.stream = stream;
		thread = new EatingThread(this);
		thread.start();
	}
	
	private class EatingThread extends Thread
	{
		InputStreamEater eater;
		
		EatingThread(InputStreamEater eater)
		{
			this.eater = eater;
		}
		
		@Override
		public void run()
		{
			boolean done = false;
			try
			{
				while (!done)
				{
					byte[] buffer = new byte[stream.available()];
					if (buffer.length == 0)
						buffer = new byte[1];
					int	amount = stream.read(buffer);
					if (amount == -1)
						done = true;
					else
					{
						synchronized (data)
						{
							for (int index = 0; index < amount; index++)
							{
								data.add(buffer[index]);
							}
							eater.wakeWaiters();
						}
					}
				}
			}
			catch (IOException e)
			{
				
			}
			try
			{
				stream.close();
			}
			catch (IOException e)
			{
				
			}
			eater.finish();
			eater.wakeWaiters();
		}
	}
	
	@Override
	public boolean add(Byte e)
	{
		return false;
	}
	
	@Override
	public void clear()
	{
		
	}
	
	@Override
	public boolean contains(Object o)
	{
		return false;
	}
	
	@Override
	public boolean remove(Object o)
	{
		return false;
	}
	
	@Override
	public int size()
	{
		return data.size();
	}
	
	@Override
	public Byte get(int index)
	{
		return data.get(index);
	}
	
	@Override
	public void put(int index, Byte value)
	{
		
	}
}