package globalResources.utilities;

import java.util.Calendar;

import globalResources.richText.RichString;
import globalResources.richText.RichStringBuilder;

/**
 * Used for console output
 */
public class Logger
{
	public static final Logger defaultLogger = new Logger(new RichString("JogGlobal"), new DefaultMessageWriter());
	
	private Priority outputPriority = Priority.DEFAULT;
	private MessageWriter writer = null;
	private RichString loggerName;
	
	private Logger(RichString name, MessageWriter writer)
	{
		this(name);
		this.writer = writer;
	}
	
	/**
	 * Initializes a new logger
	 * @param name the name of this logger
	 */
	public Logger(RichString name)
	{
		loggerName = name;
	}
	
	/**
	 * Get the name of this logger
	 * @return the name of this logger
	 */
	public RichString getName()
	{
		return loggerName;
	}
	
	public void setName(RichString name)
	{
		this.loggerName = name;
	}
	
	/**
	 * Sets the MessageWriter that will be used
	 * @param writer the writer to be used
	 */
	public void setMessageWriter(MessageWriter writer)
	{
		if (this.equals(defaultLogger) && writer == null) return;
		this.writer = writer;
	}
	
	private MessageWriter getMessageWriter()
	{
		return writer != null ? writer : defaultLogger.writer;
	}
	
	/**
	 * Sets the minimum priority level that will be output to a console
	 * @param priority minimum priority level for output
	 */
	public void setOutputPriority(Priority priority)
	{
		outputPriority = priority;
	}
	
	/**
	 * Outputs the current stack trace of the thread this was called from
	 */
	public void logStackTrace()
	{
		StackTraceElement[] elements = Thread.currentThread().getStackTrace();
		String message = "";
		for (int index = 0; index < elements.length; index++)
		{
			message += elements[index].toString();
			if (index < elements.length - 1) message += "\n";
		}
		log(message);
	}
	
	/**
	 * Logs a standard Info message
	 * @param message message to be logged
	 * @see #log(String, MessageType, Priority)
	 * @see globalResources.utilities.Logger.MessageType
	 * @see globalResources.utilities.Logger.Priority
	 */
	public void log(RichString message)
	{
		log(message, MessageType.INFO, Priority.DEFAULT);
	}
	
	public void log(String message)
	{
		log(new RichString(message));
	}
	
	/**
	 * Logs a message
	 * @param message the message to be logged
	 * @param type the type of the message
	 * @param priority the priority level of this message
	 */
	public void log(RichString message, MessageType type, Priority priority)
	{
		getMessageWriter().rawWrite(message, loggerName, type, priority);
		if (outputPriority.passes(priority) && message != null)
		{
			getMessageWriter().write(message, loggerName, type);
		}
	}
	
	public void log(String message, MessageType type, Priority priority)
	{
		log(new RichString(message), type, priority);
	}
	
	/**
	 * Logs a message
	 * @param message the message to be logged
	 * @param type the type of the message
	 */
	public void log(RichString message, MessageType type)
	{
		log(message, type, Priority.DEFAULT);
	}
	
	public void log(String message, MessageType type)
	{
		log(new RichString(message), type);
	}
	
	/**
	 * Logs the exception as an Error
	 * @param message the message to be logged with the exception
	 * @param exception the exception to be logged
	 * @see globalResources.utilities.Logger.MessageType
	 */
	public void log(RichString message, Exception exception)
	{
		StackTraceElement[] stack = exception.getStackTrace();
		RichStringBuilder builder = new RichStringBuilder(message);
		builder.append("\n" + exception.getClass().getName() + ": " + exception.getMessage());
		for (int index = 0; index < stack.length; index++) builder.append("\n       " + stack[index].toString());
		if (exception.getCause() != null)
		{
			builder.append("\nCaused by: " + exception.getCause().getClass().getName() + ": " + exception.getCause().getMessage());
			StackTraceElement[] causeStack = exception.getCause().getStackTrace();
			for (int index = 0; index < causeStack.length; index++)
			{
				String line = causeStack[index].toString();
				if (line.compareTo(stack[0].toString()) == 0)
				{
					builder.append("\n       ... " + (causeStack.length - index) + " more");
					break;
				}
				else builder.append("\n       " + causeStack[index].toString());
			}
		}
		log(builder.build(), MessageType.ERROR, Priority.DEFAULT);
	}
	
	public void log(String message, Exception exception)
	{
		log(new RichString(message), exception);
	}
	
	/**
	 * Create a string containing the current time and date
	 * @return time stamp
	 */
	public static String getTime()
	{
		Calendar calendar = Calendar.getInstance();
		String[] values = {""+calendar.get(Calendar.HOUR_OF_DAY), ""+calendar.get(Calendar.MINUTE), ""+calendar.get(Calendar.SECOND), ""+calendar.get(Calendar.DAY_OF_MONTH), ""+(calendar.get(Calendar.MONTH) + 1)};
		for (int index = 0; index < values.length; index++)
		{
			if (values[index].length() == 1) values[index] = "0" + values[index];
		}
		return values[0] + ":" + values[1] + ":" + values[2] + "-" + values[3] + "/" + values[4] + "/" + calendar.get(Calendar.YEAR);
	}
	
	/**
	 * Represents different types of messages
	 */
	public static enum MessageType
	{
		INFO("INFO"),
		ERROR("ERROR"),
		WARNING("WARNING");
		
		static int targetLength;
		static int valueCount = 0;
		
		String name;
		String padding;
		boolean paddingGenerated = false;
		
		private static void ensurePadding(MessageType messageType)
		{
			MessageType[] messageTypes = MessageType.values();
			if (messageTypes.length != valueCount)
			{
				int longestNameLength = 0;
				for (int index = 0; index < messageTypes.length; index++)
				{
					int length = messageTypes[index].name.length();
					if (length > longestNameLength) longestNameLength = length;
				}
				targetLength = longestNameLength;
				valueCount = messageTypes.length;
			}
			if (!messageType.paddingGenerated)
			{
				messageType.padding = "";
				for (int i = messageType.name.length(); i < targetLength; i++)
				{
					messageType.padding += ' ';
				}
				messageType.paddingGenerated = true;
			}
		}
		
		MessageType(String name)
		{
			this.name = name;
		}
		
		/**
		 * Gets the name of this message type
		 * @return name of this message type
		 */
		public String getName()
		{
			return name;
		}
		
		/**
		 * Gets the padded name of this message type
		 * @return padded name of this message type
		 * <p>
		 * Message type names can be padded to all be the same length.
		 * </p>
		 */
		public String getPaddedName()
		{
			ensurePadding(this);
			return padding + name;
		}
	}
	
	/**
	 * Various priority levels
	 */
	public static enum Priority
	{
		DEFAULT	(0),
		LOW		(-1),
		HIGH	(1);
		
		int value;
		
		Priority(int value)
		{
			this.value = value;
		}
		
		public boolean passes(Priority priority)
		{
			return (priority.value >= value);
		}
	}
	
	/**
	 * Formats and writes a message to a console
	 */
	public static interface MessageWriter
	{
		/**
		 * Called for messages that have passed the current priority filter
		 * @param message
		 * @param loggerName
		 * @param type
		 */
		public void write(RichString message, RichString loggerName, MessageType type);
		/**
		 * Called for all logged messages
		 * @param message
		 * @param loggerName
		 * @param type
		 * @param priority
		 */
		public void rawWrite(RichString message, RichString loggerName, MessageType type, Priority priority);
	}
	
	static class DefaultMessageWriter implements MessageWriter
	{
		public void write(RichString message, RichString loggerName, MessageType type)
		{
			System.out.println("[" + getTime() + "] - [" + type.getPaddedName() + "] [" + loggerName + "] " + message);
		}
		
		public void rawWrite(RichString message, RichString loggerName, MessageType type, Priority priority)
		{
			
		}
	}
}