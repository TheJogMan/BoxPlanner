package globalResources;

import globalResources.utilities.dataSet.Value;

/**
 * Initializes the various components of the GlobalResources library
 */
public class GlobalResourcesInitializer
{
	private static boolean initialized = false;
	
	/**
	 * Runs the initializer
	 */
	public static void init()
	{
		if (!initialized)
		{
			Value.init();
			
			initialized = true;
		}
	}
	
	public static boolean initialized()
	{
		return initialized;
	}
}