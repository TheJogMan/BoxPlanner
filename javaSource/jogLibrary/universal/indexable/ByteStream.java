package jogLibrary.universal.indexable;

import java.io.IOException;
import java.io.InputStream;

public class ByteStream extends Indexer<Byte>
{
	InputStream stream;
	boolean finished = false;
	Byte next = null;
	
	public ByteStream(InputStream stream)
	{
		this.stream = stream;
	}
	
	@Override
	protected Byte nextValue()
	{
		if (next != null)
		{
			Byte value = next;
			next = null;
			return value;
		}
		
		if (finished)
			return null;
		
		try
		{
			byte[] data = new byte[1];
			int amount = stream.read(data);
			if (amount == -1)
			{
				finished = true;
				close();
				return null;
			}
			else
				return data[0];
		}
		catch (IOException e)
		{
			finished = true;
			close();
			return null;
		}
	}
	
	@Override
	protected boolean hasNextValue()
	{
		if (next != null)
			return true;
		else
		{
			next = nextValue();
		}
		return next != null;
	}
	
	@Override
	public boolean finished()
	{
		return finished;
	}
	
	protected void close()
	{
		try
		{
			stream.close();
		}
		catch (IOException e)
		{
			
		}
	}
}