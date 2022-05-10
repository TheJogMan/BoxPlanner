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

public class RollType extends ObjectType
{
	FloatValue diameter;
	FloatValue height;
	
	public RollType(String name, String label, float diameter, float height)
	{
		super(name, label);
		if (types.containsKey(name))
			throw new RuntimeException("Two roll types can not have the same name.");
		this.diameter = (FloatValue)data.put("Diameter", new FloatValue(diameter));
		this.height = (FloatValue)data.put("Height", new FloatValue(height));
		Settings.rollList.add(new DataValue(data));
	}
	
	protected RollType(Data data)
	{
		super(data);
		this.diameter = (FloatValue)data.get("Diameter", new FloatValue(12));
		this.height = (FloatValue)data.get("Height", new FloatValue(12));
		types.put(name.get(), this);
	}
	
	private RollType(String name)
	{
		super(name);
		this.diameter = (FloatValue)data.put("Diameter", new FloatValue(12));
		this.height = (FloatValue)data.put("Height", new FloatValue(12));
	}
	
	public float diameter()
	{
		return diameter.get();
	}

	public static RollType getType(String name)
	{
		if (types.containsKey(name))
			return types.get(name);
		else
			return new RollType(name);
	}
	
	public static Collection<RollType> getTypes()
	{
		return types.values();
	}
	
	static HashMap<String, RollType> types = new HashMap<>();
	
	@SuppressWarnings("unchecked")
	static void init()
	{
		RollListListener rollListListener = new RollListListener();
		Settings.settingsData.addChangeListener((old, newValue) ->
		{
			rollListListener.change(((ListValue<DataValue>)old.get("RollList", new ListValue<DataValue>(Data.typeRegistry.get(DataValue.class)))).get(), ((ListValue<DataValue>)newValue.get("RollList", new ListValue<DataValue>(Data.typeRegistry.get(DataValue.class)))).get());
		});
		Settings.rollList.addChangeListener(rollListListener);
		Settings.rollList.addListChangeListener(rollListListener);
	}
	
	private static class RollListListener implements ListChangeListener<DataValue>, ValueChangeListener<List<DataValue>>
	{
		@Override
		public void cleared()
		{
			types.clear();
		}
		
		@Override
		public void valueAdded(DataValue value)
		{
			new RollType(value.get());
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
		return diameter.get();
	}

	@Override
	public float length()
	{
		return diameter.get();
	}

	@Override
	public float height()
	{
		return height.get();
	}

	@Override
	public float drawWidth()
	{
		return width();
	}

	@Override
	public float drawHeight()
	{
		return length();
	}
}