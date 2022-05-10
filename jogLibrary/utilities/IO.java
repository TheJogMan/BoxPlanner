package jogLibrary.utilities;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;

public class IO
{
	public static boolean recursiveDelete(File file)
	{
		if (file != null)
		{
			if (file.isDirectory())
			{
				File[] contents = file.listFiles();
				for (int index = 0; index < contents.length; index++)
				{
					boolean succeeded = recursiveDelete(contents[index]);
					if (!succeeded)
					{
						//if a contained file could not be deleted, then the parent directory can't be deleted, so lets not waste time trying to delete the remaining files
						return false;
					}
				}
			}
			return file.delete();
		}
		else return true;
	}
	
	public static String readString(InputStreamReader input) //abstracts away the error handling if its not desired
	{
		return readStringFull(input).result();
	}
	
	public static void writeString(OutputStreamWriter output, String string) //abstracts away the error handling if its not desired
	{
		writeStringFull(output, string);
	}
	
	public static byte[] readBytes(InputStream input) //abstracts away the error handling if its not desired
	{
		return readBytesFull(input).result();
	}
	
	public static void writeBytes(OutputStream output, byte[] data) //abstracts away the error handling if its not desired
	{
		writeBytesFull(output, data);
	}
	
	
	
	
	
	
	
	
	
	
	
	public static String readStringRaw(InputStreamReader input) throws IOException //un-handles the error, if manual handling is desired
	{
		Result<String> result = readStringFull(input);
		if (result.success()) return result.result();
		else throw (IOException)result.getData()[0];
	}
	
	public static Result<String> readStringFull(InputStreamReader input)
	{
		try
		{
			StringBuilder builder = new StringBuilder();
			while (input.ready()) builder.append((char)input.read());
			return new Result<String>(builder.toString(), true, null, null, null);
		}
		catch (IOException exception)
		{
			return new Result<String>("", "An IOException occurred.", exception);
		}
	}
	
	public static void writeStringRaw(OutputStreamWriter output, String string) throws IOException //un-handles the error, if manual handling is desired
	{
		Result<?> result = writeStringFull(output, string);
		if (!result.success()) throw (IOException)result.getData()[0];
	}
	
	public static Result<?> writeStringFull(OutputStreamWriter output, String string)
	{
		try
		{
			output.write(string);
			return new Result<String>();
		}
		catch (IOException exception)
		{
			return new Result<String>(null, "An IOException occurred.", exception);
		}
	}
	
	public static byte[] readBytesRaw(InputStream input) throws IOException //un-handles the error, if manual handling is desired
	{
		Result<byte[]> result = readBytesFull(input);
		if (result.success()) return result.result();
		else throw (IOException)result.getData()[0];
	}
	
	public static Result<byte[]> readBytesFull(InputStream input)
	{
		try
		{
			byte[] data = new byte[input.available()];
			input.read(data);
			return new Result<byte[]>(data);
		}
		catch (IOException exception)
		{
			return new Result<byte[]>(new byte[0], "An IOException occurred.", exception);
		}
	}
	
	public static void writeBytesRaw(OutputStream output, byte[] data) throws IOException //un-handles the error, if manual handling is desired
	{
		Result<?> result = writeBytesFull(output, data);
		if (!result.success()) throw (IOException)result.getData()[0];
	}
	
	public static Result<?> writeBytesFull(OutputStream output, byte[] data)
	{
		try
		{
			output.write(data);
			return new Result<String>();
		}
		catch (IOException exception)
		{
			return new Result<String>(null, "An IOException occurred.", exception);
		}
	}
}