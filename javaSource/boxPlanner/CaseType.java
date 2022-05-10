package boxPlanner;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;

import jogLibrary.universal.dataStructures.data.Data;
import jogLibrary.universal.dataStructures.data.DataValue;
import jogLibrary.universal.dataStructures.data.ListValue;
import jogLibrary.universal.dataStructures.data.ListValue.ListChangeListener;
import jogLibrary.universal.dataStructures.data.Value.ValueChangeListener;
import jogLibrary.universal.dataStructures.data.values.FloatValue;
import jogLibrary.universal.dataStructures.data.values.StringValue;

public class CaseType extends ObjectType
{
	FloatValue width;
	FloatValue height;
	FloatValue length;
	
	public CaseType(String name, String label, float width, float length, float height)
	{
		super(name, label);
		if (types.containsKey(name))
			throw new RuntimeException("Two case types can not have the same name.");
		
		this.width = (FloatValue)data.put("Width", new FloatValue(width));
		this.height = (FloatValue)data.put("Height", new FloatValue(height));
		this.length = (FloatValue)data.put("Length", new FloatValue(length));
		valid = true;
		Settings.caseList.add(new DataValue(data));
	}
	
	private CaseType(Data data)
	{
		super(data);
		this.width = (FloatValue)data.get("Width", new FloatValue(12));
		this.height = (FloatValue)data.get("Height", new FloatValue(12));
		this.length = (FloatValue)data.get("Length", new FloatValue(12));
		valid = true;
		types.put(name.get(), this);
	}
	
	private CaseType(String name)
	{
		super(name);
		this.width = (FloatValue)data.put("Width", new FloatValue(12));
		this.height = (FloatValue)data.put("Height", new FloatValue(12));
		this.length = (FloatValue)data.put("Length", new FloatValue(12));
	}
	
	public float drawWidth()
	{
		return width();
	}
	
	public float drawHeight()
	{
		return length();
	}
	
	public static CaseType getType(String name)
	{
		if (types.containsKey(name))
			return types.get(name);
		else
			return new CaseType(name);
	}
	
	public static Collection<CaseType> getTypes()
	{
		return types.values();
	}
	
	static HashMap<String, CaseType> types = new HashMap<>();
	
	@SuppressWarnings("unchecked")
	static void init()
	{
		CaseListListener caseListListener = new CaseListListener();
		Settings.settingsData.addChangeListener((old, newValue) ->
		{
			caseListListener.change(((ListValue<DataValue>)old.get("CaseList", new ListValue<DataValue>(Data.typeRegistry.get(DataValue.class)))).get(), ((ListValue<DataValue>)newValue.get("CaseList", new ListValue<DataValue>(Data.typeRegistry.get(DataValue.class)))).get());
		});
		Settings.boxList.addChangeListener(caseListListener);
		Settings.boxList.addListChangeListener(caseListListener);
	}
	
	private static class CaseListListener implements ListChangeListener<DataValue>, ValueChangeListener<List<DataValue>>
	{
		@Override
		public void cleared()
		{
			types.clear();
		}
		
		@Override
		public void valueAdded(DataValue value)
		{
			new CaseType(value.get());
		}
		
		@Override
		public void valueRemoved(DataValue value)
		{
			types.remove(((StringValue)value.get().get("Name", new StringValue())).get());
		}
		
		@Override
		public void collectionAdded(Collection<? extends DataValue> collection)
		{
			collection.forEach(entry -> {valueAdded(entry);});
		}
		
		@Override
		public void collectionRemoved(Collection<?> collection)
		{
			collection.forEach(entry ->
			{
				if (entry instanceof DataValue)
					valueRemoved((DataValue)entry);
			});
		}
		
		@Override
		public void valueChanged(int index, DataValue newValue, DataValue oldValue)
		{
			valueRemoved(oldValue);
			valueAdded(newValue);
		}
		
		@Override
		public void change(List<DataValue> oldValue, List<DataValue> newValue)
		{
			cleared();
			newValue.forEach(value -> {valueAdded(value);});
		}
	}
	
	@Override
	public float width()
	{
		return width.get();
	}
	
	@Override
	public float length()
	{
		return length.get();
	}
	
	@Override
	public float height()
	{
		return height.get();
	}
}