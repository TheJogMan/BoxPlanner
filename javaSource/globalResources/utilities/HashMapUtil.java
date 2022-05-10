package globalResources.utilities;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.Set;
import java.util.UUID;

/**
 * Utilities for working with HashMaps
 */
public class HashMapUtil
{
	/**
	 * @param <V> value type
	 * @param map
	 * @return
	 */
	public static <V> UUID[] getUUIDKeyArray(HashMap<UUID, V> map)
	{
		Set<UUID> set = map.keySet();
		return set.toArray(new UUID[set.size()]);
	}
	
	/**
	 * @param <K> key type
	 * @param map
	 * @return
	 */
	public static <K> Integer[] getIntegerValueArray(HashMap<K, Integer> map)
	{
		ArrayList<Integer> list = new ArrayList<Integer>();
		for (Iterator<Entry<K, Integer>> iterator = map.entrySet().iterator(); iterator.hasNext();) list.add(iterator.next().getValue());
		return list.toArray(new Integer[list.size()]);
	}
	
	/**
	 * Converts an array of Integer objects into a primitive int array
	 * @param array
	 * @param <V> should be Integer
	 * @return
	 */
	public static <V> int[] makeIntegerPrimitive(V[] array)
	{
		int[] primitive = new int[array.length];
		for (int index = 0; index < array.length; index++)
		{
			int value = 0;
			try
			{
				value = (int)array[index];
			}
			catch (Exception e)
			{
				
			}
			primitive[index] = value;
		}
		return primitive;
	}
}