package jogLibrary.universal.indexable;

import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class CharacterInputStreamEater extends DynamicIndexable<Character>
{
	ArrayList<Character> data = new ArrayList<Character>();
	InputStreamReader stream;
	EatingThread thread;
	
	public CharacterInputStreamEater(InputStreamReader stream)
	{
		if (stream == null)
			throw new RuntimeException("Input stream can not be null.");
		this.stream = stream;
		thread = new EatingThread(this);
		thread.start();
	}
	
	private class EatingThread extends Thread
	{
		CharacterInputStreamEater eater;
		
		EatingThread(CharacterInputStreamEater eater)
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
					char[] buffer = new char[1];
					int amount = stream.read(buffer);
					if (amount == -1)
						done = true;
					else
						synchronized (data)
						{
							data.add(buffer[0]);
							eater.wakeWaiters();
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
	public boolean add(Character e)
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
	public Character get(int index)
	{
		return data.get(index);
	}
	
	@Override
	public void put(int index, Character value)
	{
		
	}
}