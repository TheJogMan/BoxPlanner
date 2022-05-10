package jogLibrary.universal.commander;

import jogLibrary.universal.richString.RichString;

public interface Executor
{
	public void respond(RichString string);
	
	public default void respond(String string)
	{
		respond(new RichString(string));
	}
}