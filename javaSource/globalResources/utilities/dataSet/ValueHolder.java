package globalResources.utilities.dataSet;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import globalResources.utilities.ByteConverter;
import globalResources.utilities.dataSet.ValueMap.ValueMapEntry;

class ValueHolder extends Value
{
	ValueMap valueMap;
	
	ValueHolder()
	{
		valueMap = new ValueMap();
	}
	
	ValueHolder(ValueMap map)
	{
		this.valueMap = map;
	}
	
	@Override
	protected String[] getStringLines()
	{
		List<String> lines = new ArrayList<String>();
		for (Iterator<ValueMapEntry> iterator = valueMap.iterator(); iterator.hasNext();)
		{
			ValueMapEntry entry = iterator.next();
			lines.add(entry.getKey() + " [" + entry.getValue().getValueID() + "]");
			String[] entryLines = entry.getValue().getAsStringLines();
			for (int index = 0; index < entryLines.length; index++)
			{
				lines.add("|" + entryLines[index]);
			}
		}
		return lines.toArray(new String[lines.size()]);
	}
	
	@Override
	public boolean setValue(Object value)
	{
		if (value instanceof ValueMap)
		{
			valueMap = (ValueMap)value;
			return true;
		}
		return false;
	}

	@Override
	public Object getValue()
	{
		return valueMap;
	}

	@Override
	public byte[] serializeValue()
	{
		byte[][] data = new byte[valueMap.size() * 2][];
		int index = 0;
		for (Iterator<ValueMapEntry> iterator = valueMap.iterator(); iterator.hasNext();)
		{
			ValueMapEntry entry = iterator.next();
			data[index] = ByteConverter.fromString(entry.getKey());
			data[index + 1] = entry.getValue().serialize();
			index += 2;
		}
		return ByteConverter.from2DByteArray(data);
	}

	@Override
	public void deserializeValue(byte[] data)
	{
		valueMap = new ValueMap();
		byte[][] array = ByteConverter.to2DByteArray(data);
		if (array != null && array.length % 2 == 0)
		{
			for (int index = 0; index < array.length; index += 2)
			{
				String key = ByteConverter.toString(array[index]);
				Value value = Value.deserialize(array[index + 1]);
				valueMap.put(key, value);
			}
		}
	}
	
	@Override
	public Value getEmptyValue()
	{
		return new ValueHolder();
	}

	@Override
	public String getValueID()
	{
		return "valueHolder";
	}
}