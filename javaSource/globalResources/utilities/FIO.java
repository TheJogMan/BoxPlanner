package globalResources.utilities;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import globalResources.utilities.Logger.MessageType;

/**
 * Reads and writes files
 */
public class FIO
{
	static Logger logger;
	
	/**
	 * Sets the logger to be used for the FIO library
	 * @param logger the logger to be used
	 */
	public static void setLogger(Logger logger)
	{
		FIO.logger = logger;
	}
	
	/**
	 * Deletes a directory and all its contents
	 * @param file the directory to delete
	 */
	public static void deleteDirectory(File file)
	{
		if (file.isDirectory())
		{
			File[] contents = file.listFiles();
			for (int index = 0; index < contents.length; index++) deleteDirectory(contents[index]);
		}
		file.delete();
	}
	
	/**
	 * Writes a string to a file
	 * @param file the file to be written to
	 * @param string the string to be written
	 */
	public static void writeString(File file, String string)
	{
		try
		{
			FileWriter writer = new FileWriter(file);
			writer.write(string);
			writer.close();
		}
		catch (Exception e)
		{
			
		}
	}
	
	/**
	 * Reads a string from a file
	 * @param file the file to be read
	 * @return the string read from the file
	 */
	public static String readString(File file)
	{
		try
		{
			FileReader reader = new FileReader(file);
			String string = "";
			while (reader.ready())
			{
				string += (char)reader.read();
			}
			reader.close();
			return string;
		}
		catch (Exception e)
		{
			return "";
		}
	}
	
	/**
	 * Ensures that a directory exists at the given file path
	 * @param path the directory to be ensured
	 * @see #ensureDirectory(File)
	 */
	public static void ensureDirectory(String path)
	{
		ensureDirectory(new File(path));
	}
	
	/**
	 * Ensures that directories exist at the given files' file paths
	 * @param files files to be ensured to be directories
	 * @see #ensureDirectory(File)
	 */
	public static void ensureDirectories(File[] files)
	{
		for (int index = 0; index < files.length; index++)
		{
			ensureDirectory(files[index]);
		}
	}
	
	/**
	 * Ensures that a directory exists at the given files file's file path
	 * @param file file to be ensured to be a directory
	 * <p>
	 * If a directory does not already exist at the given files file path, a new one will be created
	 * </p>
	 */
	public static void ensureDirectory(File file)
	{
		if (file != null && !file.exists())
		{
			ensureDirectory(file.getParentFile());
			file.mkdir();
		}
	}
	
	/**
	 * Checks if data can be read from a file
	 * @param file the file to be checked
	 * @return if data can be read
	 */
	public static boolean canReadBytes(File file)
	{
		if (file.exists() && file.isFile() && file.canRead())
		{
			return true;
		}
		else
		{
			return false;
		}
	}
	
	/**
	 * Checks if data can be written to a file
	 * @param file the file to be checked
	 * @return if data can be written
	 */
	public static boolean canWriteBytes(File file)
	{
		if (file.canWrite())
		{
			return true;
		}
		else
		{
			return false;
		}
	}
	
	/**
	 * Reads data from the file
	 * @param file the file to be read
	 * @return the data that was read from the file
	 */
	public static byte[] readBytes(File file)
	{
		if (canReadBytes(file))
		{
			try
			{
				FileInputStream reader = new FileInputStream(file);
				byte[] data = new byte[reader.available()];
				reader.read(data);
				reader.close();
				return data;
			}
			catch (IOException e)
			{
				log("Could not read bytes from file! " + file.getAbsolutePath(), e);
			}
		}
		else
		{
			log("ERROR: Could not read bytes from file! " + file.getAbsolutePath() + "\nPath does not exist as a writable file!", Logger.MessageType.ERROR);
		}
		return new byte[0];
	}
	
	/**
	 * Writes data to the file
	 * @param file the file to be written to
	 * @param data the data to be written
	 * @return if the data was written successfully
	 */
	public static boolean writeBytes(File file, byte[] data)
	{
		try
		{
			boolean exists = false;
			if (file.exists() && file.isFile())
			{
				exists = true;
			}
			else
			{
				exists = file.createNewFile();
			}
			
			if (exists && file.canWrite())
			{
				FileOutputStream writer = new FileOutputStream(file);
				writer.write(data);
				writer.close();
				return true;
			}
			else
			{
				log("Could not read bytes from file! " + file.getAbsolutePath() + "\nPath does not exist as a writable file!", Logger.MessageType.ERROR);
			}
		}
		catch (IOException e)
		{
			log("Could not write bytes to file " + file.getAbsolutePath(), Logger.MessageType.ERROR);
		}
		return false;
	}
	
	private static void log(String message, Exception exception)
	{
		if (FIO.logger != null) FIO.logger.log(message, exception);
		else Logger.defaultLogger.log(message, exception);
	}
	
	private static void log(String message, MessageType type)
	{
		if (FIO.logger != null) FIO.logger.log(message, type);
		else Logger.defaultLogger.log(message, type);
	}
}