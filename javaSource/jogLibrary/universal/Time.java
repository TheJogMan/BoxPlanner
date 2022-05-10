package jogLibrary.universal;

public final class Time
{
	private static final long originPoint = System.currentTimeMillis();
	
	public static long currentTimeMillis()
	{
		return System.currentTimeMillis() - originPoint;
	}
}