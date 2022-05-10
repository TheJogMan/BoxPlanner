package jogLibrary.universal.indexable;

import java.io.IOException;
import java.io.Reader;

public class CharacterStream extends Indexer<Character>
{
	Reader stream;
	boolean finished = false;
	boolean closed = false;
	
	public CharacterStream(Reader stream)
	{
		this.stream = stream;
	}
	
	@Override
	protected Character nextValue()
	{
		if (finished)
			return null;
		
		try
		{
			int value = stream.read();
			if (value == -1)
			{
				finished = true;
				close();
				return null;
			}
			else
				return (char)value;
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
		if (finished)
			return false;
		
		try
		{
			return stream.ready();
		}
		catch (IOException e)
		{
			finished = true;
			close();
			return false;
		}
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