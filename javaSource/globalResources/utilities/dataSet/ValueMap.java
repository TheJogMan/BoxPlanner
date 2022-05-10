package globalResources.utilities.dataSet;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import globalResources.utilities.SecureList;
import globalResources.utilities.dataSet.ValueMap.ValueMapEntry;

class ValueMap implements Iterable<ValueMapEntry>
{
	List<String> keys;
	List<Value> values;
	
	public ValueMap()
	{
		init();
	}
	
	protected ValueMap(ValueMap map)
	{
		init();
		for (Iterator<ValueMapEntry> iterator = map.iterator(); iterator.hasNext();)
		{
			add(iterator.next());
		}
	}
	
	void init()
	{
		keys = new ArrayList<String>();
		values = new ArrayList<Value>();
	}
	
	void clear()
	{
		keys.clear();
		values.clear();
	}
	
	protected ValueMap clone()
	{
		return new ValueMap(this);
	}
	
	boolean containsKey(String key)
	{
		return keys.contains(key);
	}
	
	boolean containsValue(Value value)
	{
		return values.contains(value);
	}
	
	Value get(String key)
	{
		if (containsKey(key))
		{
			return values.get(keys.indexOf(key));
		}
		return null;
	}
	
	boolean isEmpty()
	{
		return keys.isEmpty();
	}
	
	SecureList<String> keyList()
	{
		return new SecureList<String>(keys);
	}
	
	boolean recursiveCheckValueMap(ValueMap subMap)
	{
		if (subMap.equals(this)) return false;
		for (Iterator<ValueMapEntry> iterator = subMap.iterator(); iterator.hasNext();)
		{
			ValueMapEntry entry = iterator.next();
			if (entry.getValue() instanceof ValueHolder)
			{
				boolean valid = recursiveCheckValueMap((ValueMap)((ValueHolder)entry.getValue()).getValue());
				if (!valid)
				{
					return false;
				}
			}
		}
		return true;
	}
	
	boolean selfContaining(ValueMap subMap)
	{
		return !recursiveCheckValueMap(subMap);
	}
	
	Value put(String key, Value value)
	{
		if (value instanceof ValueHolder)
		{
			if (!recursiveCheckValueMap((ValueMap)((ValueHolder)value).getValue()))
			{
				throw new IllegalArgumentException("A ValueMap cannot contain itself on any level!");
			}
		}
		
		if (containsKey(key))
		{
			Value previousValue = get(key);
			values.set(keys.indexOf(key), value);
			return previousValue;
		}
		else
		{
			keys.add(key);
			values.add(value);
			return null;
		}
	}
	
	void putAll(ValueMap map)
	{
		for (Iterator<ValueMapEntry> iterator = map.iterator(); iterator.hasNext();)
		{
			add(iterator.next());
		}
	}
	
	Value remove(String key)
	{
		if (containsKey(key))
		{
			Value value = get(key);
			values.remove(value);
			keys.remove(key);
			return value;
		}
		else
		{
			return null;
		}
	}
	
	int size()
	{
		return keys.size();
	}
	
	List<Value> values()
	{
		List<Value> valueList = new ArrayList<Value>();
		for (Iterator<Value> iterator = values.iterator(); iterator.hasNext();)
		{
			valueList.add(iterator.next());
		}
		return valueList;
	}
	
	Value add(ValueMapEntry entry)
	{
		return put(entry.key, entry.value);
	}
	
	@Override
	public Iterator<ValueMapEntry> iterator()
	{
		return new ValueMapEntryIterator(this);
	}
	
	class ValueMapEntry
	{
		String key;
		Value value;
		
		ValueMapEntry(String key, Value value)
		{
			this.key = key;
			this.value = value;
		}
		
		Value getValue()
		{
			return value;
		}
		
		String getKey()
		{
			return key;
		}
	}
	
	class ValueMapEntryIterator implements Iterator<ValueMapEntry>
	{
		String[] keys;
		Value[] values;
		int currentIndex;
		
		ValueMapEntryIterator(ValueMap map)
		{
			keys = map.keys.toArray(new String[map.keys.size()]);
			values = map.values.toArray(new Value[map.values.size()]);
			currentIndex = 0;
		}
		
		@Override
		public boolean hasNext()
		{
			return currentIndex < keys.length;
		}
		
		@Override
		public ValueMapEntry next()
		{
			if (hasNext())
			{
				ValueMapEntry entry = new ValueMapEntry(keys[currentIndex], values[currentIndex]);
				currentIndex++;
				return entry;
			}
			else
			{
				return null;
			}
		}
		
		@Override
		public void remove()
		{
			throw new UnsupportedOperationException();
		}
	}
}