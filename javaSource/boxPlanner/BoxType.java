package boxPlanner;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;

import jogLibrary.universal.dataStructures.data.Data;
import jogLibrary.universal.dataStructures.data.DataValue;
import jogLibrary.universal.dataStructures.data.ListValue;
import jogLibrary.universal.dataStructures.data.ListValue.ListChangeListener;
import jogLibrary.universal.dataStructures.data.Value.ValueChangeListener;
import jogLibrary.universal.dataStructures.data.values.BooleanValue;
import jogLibrary.universal.dataStructures.data.values.FloatValue;
import jogLibrary.universal.dataStructures.data.values.StringValue;

public class BoxType extends ObjectType
{
	BooleanValue telescoped;
	FloatValue width;
	FloatValue height;
	FloatValue length;
	
	public BoxType(String name, String label, float width, float length, float height, boolean telescoped)
	{
		super(name, label);
		if (types.containsKey(name))
			throw new RuntimeException("Two box types can not have the same name.");
		
		this.width = (FloatValue)data.put("Width", new FloatValue(width));
		this.height = (FloatValue)data.put("Height", new FloatValue(height));
		this.length = (FloatValue)data.put("Length", new FloatValue(length));
		this.telescoped = (BooleanValue)data.put("Telescoped", new BooleanValue(telescoped));
		valid = true;
		Settings.boxList.add(new DataValue(data));
	}
	
	private BoxType(Data data)
	{
		super(data);
		this.width = (FloatValue)data.get("Width", new FloatValue(12));
		this.height = (FloatValue)data.get("Height", new FloatValue(12));
		this.length = (FloatValue)data.get("Length", new FloatValue(12));
		telescoped = (BooleanValue)data.get("Telescoped", new BooleanValue());
		valid = true;
		types.put(name.get(), this);
	}
	
	private BoxType(String name)
	{
		super(name);
		this.width = (FloatValue)data.put("Width", new FloatValue(12));
		this.height = (FloatValue)data.put("Height", new FloatValue(12));
		this.length = (FloatValue)data.put("Length", new FloatValue(12));
		this.telescoped = (BooleanValue)data.put("Telescoped", new BooleanValue(false));
	}
	
	public float drawWidth()
	{
		if (telescoped())
			return width();
		else
			return width() + length();
	}
	
	public float drawHeight()
	{
		if (telescoped())
			return height();
		else
			return height() + length();
	}
	
	public boolean telescoped()
	{
		return telescoped.get();
	}
	
	public static BoxType getType(String name)
	{
		if (types.containsKey(name))
			return types.get(name);
		else
			return new BoxType(name);
	}
	
	public static Collection<BoxType> getTypes()
	{
		return types.values();
	}
	
	static HashMap<String, BoxType> types = new HashMap<>();
	
	@SuppressWarnings("unchecked")
	static void init()
	{
		BoxlistListener boxlistListener = new BoxlistListener();
		Settings.settingsData.addChangeListener((old, newValue) ->
		{
			boxlistListener.change(((ListValue<DataValue>)old.get("BoxList", new ListValue<DataValue>(Data.typeRegistry.get(DataValue.class)))).get(), ((ListValue<DataValue>)newValue.get("BoxList", new ListValue<DataValue>(Data.typeRegistry.get(DataValue.class)))).get());
		});
		Settings.boxList.addChangeListener(boxlistListener);
		Settings.boxList.addListChangeListener(boxlistListener);
	}
	
	private static class BoxlistListener implements ListChangeListener<DataValue>, ValueChangeListener<List<DataValue>>
	{
		@Override
		public void cleared()
		{
			types.clear();
		}
		
		@Override
		public void valueAdded(DataValue value)
		{
			new BoxType(value.get());
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