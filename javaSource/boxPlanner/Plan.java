package boxPlanner;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;

import boxPlanner.scenes.Test;
import globalResources.graphics.Canvas;
import globalResources.utilities.Vector;
import jogLibrary.universal.dataStructures.data.Data;
import jogLibrary.universal.dataStructures.data.DataValue;
import jogLibrary.universal.dataStructures.data.ListValue;
import jogLibrary.universal.dataStructures.data.ListValue.ListChangeListener;
import jogLibrary.universal.dataStructures.data.Value.ValueChangeListener;
import jogLibrary.universal.dataStructures.data.values.BooleanValue;
import jogLibrary.universal.dataStructures.data.values.FloatValue;
import jogLibrary.universal.dataStructures.data.values.StringValue;
import jogLibrary.universal.dataStructures.data.values.UUIDValue;
import mechanic.engine.GameWindow;

public class Plan
{
	ArrayList<PlannedObject> objects = new ArrayList<>();
	Data data;
	ListValue<DataValue> objectValues;
	
	@SuppressWarnings("unchecked")
	public Plan(Data data)
	{
		this.data = data;
		objectValues = (ListValue<DataValue>)data.get("Objects", new ListValue<DataValue>(Data.typeRegistry.get(DataValue.class)));
		objectValues.forEach(value -> {addObject(value);});
		ObjectListListener listener = new ObjectListListener();
		objectValues.addListChangeListener(listener);
		objectValues.addChangeListener(listener);
	}
	
	@SuppressWarnings("unchecked")
	public Plan()
	{
		data = new Data();
		objectValues = (ListValue<DataValue>)data.put("Objects", new ListValue<DataValue>(Data.typeRegistry.get(DataValue.class)));
		ObjectListListener listener = new ObjectListListener();
		objectValues.addListChangeListener(listener);
		objectValues.addChangeListener(listener);
	}
	
	public Data export()
	{
		return data.duplicate();
	}
	
	public void preDraw(Canvas canvas)
	{
		objects.forEach(object ->
		{
			if (object instanceof Steel)
				object.draw(canvas);
		});
	}
	
	public void postDraw(Canvas canvas)
	{
		objects.forEach(object ->
		{
			if (!(object instanceof Steel))
				object.draw(canvas);
		});
	}
	
	void addObject(DataValue dataValue)
	{
		String type = ((StringValue)dataValue.get().get("Type", new StringValue())).get();
		if (type.equals("Steel"))
			new Steel(dataValue);
		else if (type.equals("Box"))
			new Box(dataValue);
		else if (type.equals("Case"))
			new Case(dataValue);
		else if (type.equals("Roll"))
			new Roll(dataValue);
		else if (type.equals("Legend"))
			new Legend(dataValue);
	}
	
	public Collection<PlannedObject> objects()
	{
		return objects;
	}
	
	public PlannedObject getObject(UUID id)
	{
		for (Iterator<PlannedObject> iterator = objects.iterator(); iterator.hasNext();)
		{
			PlannedObject object = iterator.next();
			if (object.id.get().equals(id))
				return object;
		}
		return null;
	}
	
	public abstract class PlannedObject
	{
		DataValue data;
		FloatValue x;
		FloatValue y;
		FloatValue width;
		FloatValue height;
		BooleanValue vertical;
		UUIDValue id;
		boolean rotateable;
		
		PlannedObject(boolean rotateable, String typeName, float x, float y, float width, float height, boolean vertical, Object[] args)
		{
			this.rotateable = rotateable;
			Data data = new Data();
			this.x = (FloatValue)data.put("X", new FloatValue(x));
			this.y = (FloatValue)data.put("Y", new FloatValue(y));
			this.width = (FloatValue)data.put("Width", new FloatValue(width));
			this.height = (FloatValue)data.put("Height", new FloatValue(height));
			this.vertical = (BooleanValue)data.put("Vertical", new BooleanValue(vertical));
			this.id = (UUIDValue)data.put("ID", new UUIDValue());
			data.put("Type", new StringValue(typeName));
			this.data = new DataValue(data);
			init(args);
			objectValues.add(this.data);
		}
		
		PlannedObject(boolean rotateable, DataValue dataValue)
		{
			this.rotateable = rotateable;
			this.data = dataValue;
			x = (FloatValue)data.get().get("X", new FloatValue());
			y = (FloatValue)data.get().get("Y", new FloatValue());
			width = (FloatValue)data.get().get("Width", new FloatValue());
			height = (FloatValue)data.get().get("Height", new FloatValue());
			vertical = (BooleanValue)data.get().get("Vertical", new BooleanValue());
			id = (UUIDValue)data.get().get("ID", new UUIDValue());
			objects.add(this);
		}
		
		public void draw(Canvas canvas)
		{
			canvas.pushSettings();
			canvas.translate(new Vector(x.get() * 2, y.get() * 2));
			drawObject(canvas);
			canvas.popSettings();
		}
		
		abstract void init(Object[] data);
		abstract void drawObject(Canvas canvas);
		public abstract String name();
		
		public void remove()
		{
			objectValues.remove(data);
		}
		
		public Vector position()
		{
			return new Vector(x(), y());
		}
		
		public float x()
		{
			return x.get();
		}
		
		public float y()
		{
			return y.get();
		}
		
		public void setPosition(Vector vector)
		{
			setPosition((float)vector.getX(), (float)vector.getY());
		}
		
		public void setPosition(float x, float y)
		{
			this.x.set(x);
			this.y.set(y);
		}
		
		public void rotate()
		{
			if (rotateable)
				this.vertical.set(!vertical.get());
		}
		
		public boolean vertical()
		{
			return vertical.get();
		}
		
		public float visualWidth()
		{
			return vertical.get() ? height.get() : width.get();
		}
		
		public float visualHeight()
		{
			return vertical.get() ? width.get() : height.get();
		}
	}
	
	static float calcLegendWidth()
	{
		Canvas canvas = GameWindow.getCanvas();
		Test.List[] lists = Test.List.values();
		int width = 5;
		for (int listNum = 0; listNum < lists.length; listNum++)
		{
			int widest = canvas.getStringLengthR(lists[listNum].displayName);
			Collection<? extends ObjectType> types = lists[listNum].types.types();
			for (Iterator<? extends ObjectType> iterator = types.iterator(); iterator.hasNext();)
			{
				ObjectType type = iterator.next();
				String line = type.label() + ": " + type.name();
				int lineWidth = canvas.getStringLengthR(line);
				if (lineWidth > widest)
					widest = lineWidth;
			}
			width += widest + 5;
		}
		return width;
	}
	
	static float calcLegendHeight()
	{
		Canvas canvas = GameWindow.getCanvas();
		Test.List[] lists = Test.List.values();
		int tallest = 0;
		for (int listNum = 0; listNum < lists.length; listNum++)
		{
			int height = (lists[listNum].types.types().size() + 1) * canvas.getFontHeightR() + 10;
			if (height > tallest)
				tallest = height;
		}
		return tallest;
	}
	
	public class Legend extends PlannedObject
	{

		public Legend(float x, float y)
		{
			super(false, "Legend", x, y, calcLegendWidth() / 2, calcLegendHeight() / 2, false, new Object[0]);
		}
		
		Legend(DataValue dataValue)
		{
			super(false, dataValue);
		}
		
		@Override
		void init(Object[] data)
		{
			
		}

		@Override
		void drawObject(Canvas canvas)
		{
			int width = (int)calcLegendWidth();
			int height = (int)calcLegendHeight();
			
			canvas.drawRect(0, 0, width, height, Color.LIGHT_GRAY);
			
			Test.List[] lists = Test.List.values();
			int x = 5;
			for (int listNum = 0; listNum < lists.length; listNum++)
			{
				int widest = canvas.getStringLengthR(lists[listNum].displayName);
				Collection<? extends ObjectType> types = lists[listNum].types.types();
				canvas.drawTextR(lists[listNum].displayName, x, 5, Color.BLACK);
				int y = 5 + canvas.getFontHeightR();
				for (Iterator<? extends ObjectType> iterator = types.iterator(); iterator.hasNext();)
				{
					ObjectType type = iterator.next();
					String line = type.label() + ": " + type.name();
					int lineWidth = canvas.getStringLengthR(line);
					if (lineWidth > widest)
						widest = lineWidth;
					canvas.drawTextR(line, x, y, Color.BLACK);
					y += canvas.getFontHeightR();
				}
				x += widest + 5;
			}
		}
		
		@Override
		public String name()
		{
			return "Legend";
		}
	}
	
	public class Steel extends PlannedObject
	{
		public static final float width = 96;
		public static final float height = 44;
		
		public Steel(float x, float y, boolean vertical)
		{
			super(true, "Steel", x, y, width, height, vertical, new Object[0]);
		}
		
		Steel(DataValue dataValue)
		{
			super(true, dataValue);
		}
		
		@Override
		void init(Object[] data)
		{
			
		}
		
		@Override
		void drawObject(Canvas canvas)
		{
			float width = visualWidth() * 2;
			float height = visualHeight() * 2;
			canvas.drawRect(0, 0, (int)width - 1, (int)height - 1, Color.GRAY);
			canvas.drawRect(0, 0, (int)width, (int)height, false, Color.GRAY.darker());
		}

		@Override
		public String name()
		{
			return "Steel";
		}
	}
	
	public class Label extends PlannedObject
	{
		StringValue label;
		
		public Label(float x, float y, String label)
		{
			super(false, "Label", x, y, 1, 1, false, new Object[] {label});
		}
		
		Label(DataValue dataValue)
		{
			super(false, dataValue);
			label = (StringValue)dataValue.get().get("Label", new StringValue("Invalid."));
			addListener();
		}
		
		@Override
		void init(Object[] data)
		{
			label = (StringValue)super.data.get().put("Label", new StringValue());
			addListener();
			label.set((String)data[0]);
		}
		
		void addListener()
		{
			label.addChangeListener((old, newValue) -> {
				int[] dims = getDims();
				x.set((float)dims[0]);
				y.set((float)dims[0]);
			});
		}
		
		int[] getDims()
		{
			String[] lines = StringValue.breakIntoLines(label.get());
			int[] dims = new int[2];
			dims[1] = GameWindow.getCanvas().getFontHeight() * lines.length + 10;
			int longest = 0;
			for (int index = 0; index < lines.length; index++)
			{
				int length = GameWindow.getCanvas().getStringWidth(lines[index]);
				if (length > longest)
					longest = length;
			}
			dims[2] = longest + 10;
			return dims;
		}
		
		@Override
		void drawObject(Canvas canvas)
		{
			
		}

		@Override
		public String name()
		{
			return "Label";
		}
	}
	
	public class Box extends PlannedObject
	{
		BoxType boxType;
		
		public Box(float x, float y, boolean vertical, BoxType type)
		{
			super(true, "Box", x, y, type.drawWidth(), type.drawHeight(), vertical, new Object[] {type});
		}
		
		Box(DataValue dataValue)
		{
			super(true, dataValue);
			latch(BoxType.getType(((StringValue)data.get().get("Box Type", new StringValue())).get()));
		}
		
		@Override
		void init(Object[] data)
		{
			latch((BoxType)data[0]);
		}
		
		void latch(BoxType type)
		{
			boxType = type;
			data.get().put("Box Type", new StringValue(type.name()));
			width.set(type.drawWidth());
			height.set(type.drawHeight());
			boxType.width.addChangeListener((old, newValue) -> {width.set(boxType.drawWidth());});
			boxType.height.addChangeListener((old, newValue) -> {height.set(boxType.drawHeight());});
			boxType.name.addChangeListener((old, newValue) -> {data.get().put("Box Type", new StringValue(type.name()));});
		}
		
		public BoxType type()
		{
			return boxType;
		}
		
		@Override
		void drawObject(Canvas canvas)
		{
			drawBox(canvas, boxType, 0, 0, vertical.get());
		}

		@Override
		public String name()
		{
			return boxType.name();
		}
	}
	
	public class Case extends PlannedObject
	{
		CaseType caseType;
		
		public Case(float x, float y, boolean vertical, CaseType type)
		{
			super(true, "Case", x, y, type.drawWidth(), type.drawHeight(), vertical, new Object[] {type});
		}
		
		Case(DataValue dataValue)
		{
			super(true, dataValue);
			latch(CaseType.getType(((StringValue)data.get().get("Case Type", new StringValue())).get()));
		}
		
		@Override
		void init(Object[] data)
		{
			latch((CaseType)data[0]);
		}
		
		void latch(CaseType type)
		{
			caseType = type;
			data.get().put("Case Type", new StringValue(type.name()));
			updateDimensions();
			caseType.width.addChangeListener((old, newValue) -> {updateDimensions();});
			caseType.length.addChangeListener((old, newValue) -> {updateDimensions();});
			caseType.height.addChangeListener((old, newValue) -> {updateDimensions();});
			caseType.name.addChangeListener((old, newValue) -> {data.get().put("Case Type", new StringValue(type.name()));});
		}
		
		void updateDimensions()
		{
			width.set(caseType.drawWidth());
			height.set(caseType.drawHeight());
		}
		
		public CaseType type()
		{
			return caseType;
		}
		
		@Override
		void drawObject(Canvas canvas)
		{
			drawCase(canvas, caseType, 0, 0, vertical.get());
		}
		
		@Override
		public String name()
		{
			return caseType.name();
		}
	}
	
	public class Roll extends PlannedObject
	{
		RollType rollType;
		
		public Roll(float x, float y, boolean vertical, RollType type)
		{
			super(true, "Roll", x, y, type.drawWidth(), type.drawHeight(), vertical, new Object[] {type});
		}
		
		Roll(DataValue dataValue)
		{
			super(true, dataValue);
			latch(RollType.getType(((StringValue)data.get().get("Roll Type", new StringValue())).get()));
		}
		
		@Override
		void init(Object[] data)
		{
			latch((RollType)data[0]);
		}
		
		void latch(RollType type)
		{
			rollType = type;
			data.get().put("Roll Type", new StringValue(type.name()));
			updateDimensions();
			rollType.diameter.addChangeListener((old, newValue) -> {updateDimensions();});
			rollType.height.addChangeListener((old, newValue) -> {updateDimensions();});
			rollType.name.addChangeListener((old, newValue) -> {data.get().put("Roll Type", new StringValue(type.name()));});
		}
		
		void updateDimensions()
		{
			width.set(rollType.drawWidth());
			height.set(rollType.drawHeight());
		}
		
		public RollType type()
		{
			return rollType;
		}
		
		@Override
		void drawObject(Canvas canvas)
		{
			drawRoll(canvas, rollType, 0, 0);
		}
		
		@Override
		public String name()
		{
			return rollType.name();
		}
	}
	
	public static void drawRoll(Canvas canvas, RollType type, int x, int y)
	{
		canvas.drawCircle((int)(x + type.diameter()), (int)(y + type.diameter()), (int)type.diameter(), true, Color.ORANGE.darker());
		canvas.drawCircle((int)(x + type.diameter()), (int)(y + type.diameter()), (int)type.diameter(), true, Color.ORANGE.darker().darker());
		
		int lx = x - (canvas.getStringLengthR(type.label()) / 2) + (int)type.diameter();
		int ly = y - (canvas.getFontHeightR() / 2) + (int)type.diameter();
		
		canvas.drawTextR(type.label(), lx, ly, Color.BLACK);
	}
	
	public static void drawCase(Canvas canvas, CaseType type, int x, int y, boolean vertical)
	{
		if (vertical)
		{
			canvas.drawRect(x, y, (int)(type.drawHeight() * 2) - 1, (int)(type.drawWidth() * 2) - 1, Color.ORANGE.darker());
			canvas.drawRect(x, y, (int)(type.drawHeight() * 2), (int)(type.drawWidth() * 2), false, Color.ORANGE.darker().darker());
		}
		else
		{
			canvas.drawRect(x, y, (int)(type.drawWidth() * 2) - 1, (int)(type.drawHeight() * 2) - 1, Color.ORANGE.darker());
			canvas.drawRect(x, y, (int)(type.drawWidth() * 2), (int)(type.drawHeight() * 2), false, Color.ORANGE.darker().darker());
		}
		canvas.drawTextR(type.label(), x, y, Color.BLACK);
	}
	
	public static void drawBox(Canvas canvas, BoxType type, int x, int y, boolean vertical)
	{
		float flap = type.length();//don't have to divide by two since we are rendering at 2x scale
		if (vertical)
		{
			canvas.drawRect(x, y, (int)(type.drawHeight() * 2) - 1, (int)(type.drawWidth() * 2) - 1, Color.ORANGE);
			canvas.drawRect(x, y, (int)(type.drawHeight() * 2), (int)(type.drawWidth() * 2), false, Color.ORANGE.darker());
			if (type.telescoped())
			{
				canvas.drawLine(x + (int)(type.height() * 2 - type.length()), y, x + (int)(type.height() * 2 - type.length()), y + (int)(type.drawWidth() * 2) - 1, Color.ORANGE.darker());
				canvas.drawLine(x, y + (int)(type.drawWidth() * 2 - flap), x + (int)(type.drawHeight() * 2) - 1, y + (int)(type.drawWidth() * 2 - flap), Color.ORANGE.darker());
			}
			else
			{
				canvas.drawLine(x + (int)flap, y, x + (int)flap, y + (int)(type.drawWidth() * 2) - 1, Color.ORANGE.darker());
				canvas.drawLine(x + (int)(type.drawHeight() * 2 - flap), y, x + (int)(type.drawHeight() * 2 - flap), y + (int)(type.drawWidth() * 2) - 1, Color.ORANGE.darker());
				canvas.drawLine(x, y + (int)(type.drawWidth() * 2 - type.length() * 2), x + (int)(type.drawHeight() * 2) - 1, y + (int)(type.drawWidth() * 2 - type.length() * 2), Color.ORANGE.darker());
			}
		}
		else
		{
			canvas.drawRect(x, y, (int)(type.drawWidth() * 2) - 1, (int)(type.drawHeight() * 2) - 1, Color.ORANGE);
			canvas.drawRect(x, y, (int)(type.drawWidth() * 2), (int)(type.drawHeight() * 2), false, Color.ORANGE.darker());
			if (type.telescoped())
			{
				canvas.drawLine(x, y + (int)type.length(), x + (int)(type.drawWidth() * 2) - 1, y + (int)type.length(), Color.ORANGE.darker());
				canvas.drawLine(x + (int)(type.drawWidth() * 2 - flap), y, x + (int)(type.drawWidth() * 2 - flap), y + (int)(type.drawHeight() * 2) - 1, Color.ORANGE.darker());
			}
			else
			{
				canvas.drawLine(x, y + (int)flap, x + (int)(type.drawWidth() * 2) - 1, y + (int)flap, Color.ORANGE.darker());
				canvas.drawLine(x, y + (int)(type.drawHeight() * 2 - flap), x + (int)(type.drawWidth() * 2) - 1, y + (int)(type.drawHeight() * 2 - flap), Color.ORANGE.darker());
				canvas.drawLine(x + (int)(type.drawWidth() * 2 - type.length() * 2), y, x + (int)(type.drawWidth() * 2 - type.length() * 2), y + (int)(type.drawHeight() * 2) - 1, Color.ORANGE.darker());
			}
		}
		canvas.drawTextR(type.label(), x, y, Color.BLACK);
	}
	
	private class ObjectListListener implements ListChangeListener<DataValue>, ValueChangeListener<List<DataValue>>
	{
		@Override
		public void cleared()
		{
			objects.clear();
		}
		
		@Override
		public void valueAdded(DataValue value)
		{
			addObject(value);
		}
		
		@Override
		public void valueRemoved(DataValue value)
		{
			objects.remove(getObject(((UUIDValue)value.get().get("ID", new UUIDValue())).get()));
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
}