package boxPlanner;

import jogLibrary.universal.dataStructures.data.Data;
import jogLibrary.universal.dataStructures.data.values.StringValue;

public abstract class ObjectType
{
	protected Data data;
	
	StringValue name;
	StringValue label;
	boolean valid;
	
	public ObjectType(String name, String label)
	{
		data = new Data();
		this.name = (StringValue)data.put("Name", new StringValue(name));
		this.label = (StringValue)data.put("Label", new StringValue(label));
		valid = true;
	}
	
	protected ObjectType(Data data)
	{
		this.data = data;
		this.name = (StringValue)data.get("Name", new StringValue("[Invalid]"));
		this.label = (StringValue)data.get("Label", name);
		valid = true;
	}
	
	protected ObjectType(String name)
	{
		data = new Data();
		this.name = (StringValue)data.put("Name", new StringValue(name));
		this.label = (StringValue)data.put("Label", new StringValue(name));
		valid = false;
	}
	
	public boolean valid()
	{
		return valid;
	}
	
	public String name()
	{
		return name.get() + (valid ? "" : "[Invalid]");
	}
	
	public String label()
	{
		return label.get();
	}
	
	public abstract float width();
	
	public abstract float length();
	
	public abstract float height();
	
	public abstract float drawWidth();
	
	public abstract float drawHeight();
}
