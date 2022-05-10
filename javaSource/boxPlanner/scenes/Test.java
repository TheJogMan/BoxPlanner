package boxPlanner.scenes;

import java.awt.Color;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.io.File;
import java.util.Collection;
import java.util.Iterator;

import boxPlanner.BoxType;
import boxPlanner.CaseType;
import boxPlanner.ObjectType;
import boxPlanner.Plan;
import boxPlanner.Plan.Box;
import boxPlanner.Plan.Case;
import boxPlanner.Plan.Legend;
import boxPlanner.Plan.PlannedObject;
import boxPlanner.Plan.Roll;
import boxPlanner.Plan.Steel;
import boxPlanner.RollType;
import boxPlanner.Settings;
import globalResources.graphics.Canvas;
import globalResources.utilities.FIO;
import globalResources.utilities.Vector;
import globalResources.utilities.VectorInt;
import jogLibrary.universal.dataStructures.data.Data;
import jogLibrary.universal.dataStructures.data.DataValue;
import jogLibrary.universal.dataStructures.data.Value;
import jogLibrary.universal.dataStructures.data.values.StringValue;
import jogLibrary.universal.indexable.Consumer.ConsumerResult;
import mechanic.engine.GameInput;
import mechanic.engine.GameWindow;
import mechanic.engine.SimpleScene;

public class Test extends SimpleScene
{
	Plan plan;
	Vector cameraPan = new Vector(0, 0);
	boolean panning = false;
	Vector grabOffset;
	Vector hoveredTypeOffset;
	PlannedObject dragged;
	PlannedObject hoveredObject;
	ObjectType hoveredType;
	boolean steelHovered;
	boolean trashHovered;
	boolean legendHovered;
	float zoom = 0;
	int boxScroll = 0;
	List list = List.BOX;
	List hoveredListOption = null;
	
	File file = new File("save.txt");
	
	public enum List
	{
		BOX("Boxes", () ->
		{
			return BoxType.getTypes();
		}, (plan, x, y, type, vertical) ->
		{
			return plan.new Box(x, y, vertical, (BoxType)type);
		}, (canvas, x, y, vertical, type) ->
		{
			Plan.drawBox(canvas, (BoxType)type, x, y, vertical);
		}),
		ROLL("Rolls", () ->
		{
			return RollType.getTypes();
		}, (plan, x, y, type, vertical) ->
		{
			return plan.new Roll(x, y, vertical, (RollType)type);
		}, (canvas, x, y, vertical, type) ->
		{
			Plan.drawRoll(canvas, (RollType)type, x, y);
		}),
		CASE("Cases", () ->
		{
			return CaseType.getTypes();
		}, (plan, x, y, type, vertical) ->
		{
			return plan.new Case(x, y, vertical, (CaseType)type);
		}, (canvas, x, y, vertical, type) ->
		{
			Plan.drawCase(canvas, (CaseType)type, x, y, vertical);
		});
		
		public String displayName;
		public Types types;
		public Create create;
		public Draw draw;
		
		List(String displayName, Types types, Create create, Draw draw)
		{
			this.displayName = displayName;
			this.types = types;
			this.create = create;
			this.draw = draw;
		}
		
		public interface Types
		{
			public Collection<? extends ObjectType> types();
		}
		
		public interface Create
		{
			public PlannedObject create(Plan plan, int x, int y, ObjectType type, boolean vertical);
		}
		
		public interface Draw
		{
			public void draw(Canvas canvas, int x, int y, boolean vertical, ObjectType type);
		}
	}
	
	void setList(List list)
	{
		this.list = list;
	}
	
	@Override
	protected void load()
	{
		if (FIO.canReadBytes(file))
		{
			ConsumerResult<Value<?, Data>, Character> result = DataValue.characterConsumer().consume(StringValue.indexer(FIO.readString(file)));
			if (result.success())
				plan = new Plan((Data)result.value().get());
			else
				plan = new Plan();
		}
		else
			plan = new Plan();
	}
	
	@Override
	protected void update()
	{
		hoveredObject = null;
		if (!GameInput.isButton(MouseEvent.BUTTON2))
			panning = false;
		Vector mouse = relativeMouse();
		for (Iterator<PlannedObject> iterator = plan.objects().iterator(); iterator.hasNext();)
		{
			PlannedObject object = iterator.next();
			if (object instanceof Steel && mouse.getX() >= object.x() && mouse.getX() < object.x() + object.visualWidth() && mouse.getY() >= object.y() && mouse.getY() < object.y() + object.visualHeight())
				hoveredObject = object;
		}
		for (Iterator<PlannedObject> iterator = plan.objects().iterator(); iterator.hasNext();)
		{
			PlannedObject object = iterator.next();
			if (!(object instanceof Steel) && mouse.getX() >= object.x() && mouse.getX() < object.x() + object.visualWidth() && mouse.getY() >= object.y() && mouse.getY() < object.y() + object.visualHeight())
				hoveredObject = object;
		}
		if (boxScroll > maxBoxScroll())
			boxScroll = maxBoxScroll();
		if (GameInput.getMouseY() < toolBoxHeight())
			hoveredObject = null;
	}
	
	@Override
	protected void preRender()
	{
		Canvas canvas = GameWindow.getCanvas();
		
		steelHovered = false;
		trashHovered = false;
		hoveredType = null;
		hoveredTypeOffset = new Vector(0, 0);
		int x = 10 - boxScroll;
		float tallest = 0;
		for (Iterator<BoxType> iterator = BoxType.getTypes().iterator(); iterator.hasNext();)
		{
			BoxType type = iterator.next();
			if (type.drawHeight() > tallest)
				tallest = type.drawHeight();
		}
		
		canvas.pushSettings();
		transform(canvas);
		plan.preDraw(canvas);
		if (hoveredObject != null && hoveredObject instanceof Steel)
			canvas.drawRect((int)(hoveredObject.x() * 2), (int)(hoveredObject.y() * 2), (int)(hoveredObject.visualWidth() * 2), (int)(hoveredObject.visualHeight() * 2), new Color(0, 255, 0, 120));
		plan.postDraw(canvas);
		if (hoveredObject != null && !(hoveredObject instanceof Steel))
			canvas.drawRect((int)(hoveredObject.x() * 2), (int)(hoveredObject.y() * 2), (int)(hoveredObject.visualWidth() * 2), (int)(hoveredObject.visualHeight() * 2), new Color(0, 255, 0, 120));
		if (hoveredObject != null)
		{
			canvas.drawRect((int)(hoveredObject.x() * 2), (int)(hoveredObject.y() * 2) - 3 - (canvas.getFontHeightR() + 6), canvas.getStringLengthR(hoveredObject.name()) + 6, canvas.getFontHeightR() + 6, Color.WHITE);
			canvas.drawRect((int)(hoveredObject.x() * 2), (int)(hoveredObject.y() * 2) - 3 - (canvas.getFontHeightR() + 6), canvas.getStringLengthR(hoveredObject.name()) + 7, canvas.getFontHeightR() + 7, false, Color.BLACK);
			canvas.drawTextR(hoveredObject.name(), (int)(hoveredObject.x() * 2) + 3, (int)(hoveredObject.y() * 2) - (canvas.getFontHeightR() + 6), Color.BLACK);
		}
		canvas.popSettings();
		
		canvas.drawRect(0, 0, canvas.getWidth(), 40 + (int)((tallest + Steel.height) * 2), Color.DARK_GRAY);
		canvas.drawRect(0, 0, canvas.getWidth(), (int)(tallest * 2) + 20, Color.DARK_GRAY.brighter());
		int listHeight = (int)(tallest * 2) + 20;
		canvas.drawRect(0, listHeight -5, canvas.getWidth(), 5, Color.BLACK);
		int width = boxListWidth();
		if (width < canvas.getWidth())
			canvas.drawRect(0, listHeight -5, canvas.getWidth(), 5, Color.WHITE);
		else
		{
			float scale = (float)width / (float)(canvas.getWidth() - 20);
			int barWidth = (int)(canvas.getWidth() / scale);
			int barX = (int)((float)boxScroll / scale);
			canvas.drawRect(barX, listHeight -5, barWidth, 5, Color.WHITE);
		}
		
		int hoveredX = 0;
		for (Iterator<? extends ObjectType> iterator = list.types.types().iterator(); iterator.hasNext();)
		{
			ObjectType type = iterator.next();
			
			if (GameInput.getMouseX() >= x && GameInput.getMouseX() < x + (int)type.drawWidth() * 2 && GameInput.getMouseY() >= 10 && GameInput.getMouseY() < 10 + (int)type.drawHeight() * 2)
			{
				if (dragged == null)
				{
					hoveredType = type;
					hoveredTypeOffset = new Vector(x - GameInput.getMouseX(), 10 - GameInput.getMouseY());
					hoveredTypeOffset.divide(2);
				}
			}
			
			list.draw.draw(canvas, x, 10, false, type);
			
			if (hoveredType != null && hoveredType.equals(type))
			{
				canvas.drawRect(x, 10, (int)type.drawWidth() * 2, (int)type.drawHeight() * 2, new Color(0, 255, 0, 120));
				hoveredX = x;
			}
			
			x += 10 + type.drawWidth() * 2;
		}
		if (hoveredType != null)
		{
			int y = 40;
			canvas.drawRect(hoveredX - 3, y - 3, canvas.getStringLengthR(hoveredType.name()) + 6, canvas.getFontHeightR() + 6, Color.WHITE);
			canvas.drawRect(hoveredX - 3, y - 3, canvas.getStringLengthR(hoveredType.name()) + 7, canvas.getFontHeightR() + 7, false, Color.WHITE);
			canvas.drawTextR(hoveredType.name(), hoveredX, y, Color.BLACK);
		}
		
		
		int steelX = 10;
		int steelY = (int)(30 + tallest * 2);
		int steelWidth = (int)(Steel.width * 2);
		int steelHeight = (int)(Steel.height * 2);
		int trashX = canvas.getWidth() - steelHeight - 10;
		if (GameInput.getMouseX() >= steelX && GameInput.getMouseX() < steelX + steelWidth && GameInput.getMouseY() >= steelY && GameInput.getMouseY() < steelY + steelHeight && dragged == null)
		{
			steelHovered = true;
			hoveredTypeOffset = new Vector(steelX - GameInput.getMouseX(), steelY - GameInput.getMouseY());
			hoveredTypeOffset.divide(2);
		}
		if (GameInput.getMouseX() >= trashX && GameInput.getMouseX() < trashX + steelHeight && GameInput.getMouseY() >= steelY && GameInput.getMouseY() < steelY + steelHeight)
			trashHovered = true;
		
		canvas.drawRect(steelX, steelY, steelWidth, steelHeight, Color.GRAY);
		if (steelHovered)
		{
			canvas.drawRect(steelX, steelY, steelWidth, steelHeight, new Color(0, 255, 0, 120));
			canvas.drawRect(steelX - 3, steelY - 3, canvas.getStringLengthR("Steel") + 6, canvas.getFontHeightR() + 6, Color.WHITE);
			canvas.drawRect(steelX - 3, steelY - 3, canvas.getStringLengthR("Steel") + 7, canvas.getFontHeightR() + 7, false, Color.WHITE);
		}
		canvas.drawTextR("Steel", steelX, steelY, Color.BLACK);
		canvas.drawRect(trashX, steelY, steelHeight, steelHeight, Color.RED);
		
		legendHovered = false;
		if (GameInput.getMouseX() >= steelX + steelWidth + 5 && GameInput.getMouseX() < steelX + steelWidth + 55 && GameInput.getMouseY() >= steelY + steelHeight - 50 && GameInput.getMouseY() < steelY + steelHeight && dragged == null)
		{
			legendHovered = true;
			hoveredTypeOffset = new Vector((steelX + steelWidth + 5) - GameInput.getMouseX(), (steelY + steelHeight - 50) - GameInput.getMouseY());
			hoveredTypeOffset.divide(2);
		}
		canvas.drawRect(steelX + steelWidth + 5, steelY + steelHeight - 50, 50, 50, legendHovered ? Color.CYAN.darker() : Color.CYAN);
		canvas.drawRect(steelX + steelWidth + 5, steelY + steelHeight - 50, 51, 51, false, Color.BLUE);
		
		int listOptionx = steelX + steelWidth + 10;
		List[] options = List.values();
		hoveredListOption = null;
		for (int index = 0; index < options.length; index++)
		{
			List option = options[index];
			if (GameInput.getMouseX() >= listOptionx - 3 && GameInput.getMouseX() < listOptionx + canvas.getStringLengthR(option.displayName) + 3 && GameInput.getMouseY() >= steelY && GameInput.getMouseY() < steelY + canvas.getFontHeightR() + 6)
				hoveredListOption = option;
			canvas.drawRect(listOptionx - 3, steelY, canvas.getStringLengthR(option.displayName) + 6, canvas.getFontHeightR() + 6, option.equals(hoveredListOption) ? Color.GREEN : Color.GRAY.darker());
			canvas.drawTextR(option.displayName, listOptionx, steelY + 3, list.equals(option) ? Color.GREEN.darker() : Color.WHITE);
			listOptionx += 15 + canvas.getStringLengthR(option.displayName);
		}
		
		
		canvas.pushSettings();
		transform(canvas);
		if (dragged != null)
		{
			dragged.draw(canvas);
			if (trashHovered)
				canvas.drawRect((int)(dragged.x() * 2), (int)(dragged.y() * 2), (int)(dragged.visualWidth() * 2), (int)(dragged.visualHeight() * 2), new Color(255, 0, 0, 120));
		}
		canvas.popSettings();
	}
	
	void transform(Canvas canvas)
	{
		canvas.translate(new Vector(0, toolBoxHeight()));
		canvas.translate(cameraPan);
		canvas.scale(new Vector(1 + (zoom / 10), 1 + (zoom / 10)));
	}
	
	public Vector relativeMouse()
	{
		float x = GameInput.getMouseX();
		float y = GameInput.getMouseY();
		y -= toolBoxHeight();
		Vector mouse = new Vector(x, y);
		Vector pan = cameraPan.clone();
		//pan.multiply(1 + (zoom / 10));
		mouse.subtract(pan);
		mouse.divide(2);
		mouse.divide(1 + (zoom / 10));
		return mouse;
	}
	
	public int boxListWidth()
	{
		float total = 0;
		for (Iterator<? extends ObjectType> iterator = list.types.types().iterator(); iterator.hasNext();)
		{
			ObjectType type = iterator.next();
			total += type.drawWidth() * 2;
			if (iterator.hasNext())
				total += 10;
		}
		return (int)total;
	}
	
	public int boxListHeight()
	{
		float tallest = 0;
		for (Iterator<? extends ObjectType> iterator = list.types.types().iterator(); iterator.hasNext();)
		{
			ObjectType type = iterator.next();
			if (type.drawHeight() > tallest)
				tallest = type.drawHeight();
		}
		return (int)((tallest * 2) + 30);
	}
	
	public int maxBoxScroll()
	{
		int width = boxListWidth();
		if (width < GameWindow.getWidth())
			return 0;
		return width - (GameWindow.getWidth() - 20);
	}
	
	public int toolBoxHeight()
	{
		float tallest = 0;
		for (Iterator<BoxType> iterator = BoxType.getTypes().iterator(); iterator.hasNext();)
		{
			BoxType type = iterator.next();
			if (type.drawHeight() > tallest)
				tallest = type.drawHeight();
		}
		return (int)((tallest + Steel.height) * 2 + 40);
	}
	
	@Override
	public void buttonPressEvent(MouseEvent event)
	{
		Vector mouse = relativeMouse();
		int toolBoxHeight = toolBoxHeight();
		if (event.getButton() == MouseEvent.BUTTON2)
		{
			if (hoveredObject != null && dragged == null)
			{
				PlannedObject object = null;
				if (hoveredObject instanceof Steel)
					object = plan.new Steel(hoveredObject.x(), hoveredObject.y(), hoveredObject.vertical());
				else if (hoveredObject instanceof Box)
					object = plan.new Box(hoveredObject.x(), hoveredObject.y(), hoveredObject.vertical(), ((Box)hoveredObject).type());
				else if (hoveredObject instanceof Case)
					object = plan.new Case(hoveredObject.x(), hoveredObject.y(), hoveredObject.vertical(), ((Case)hoveredObject).type());
				else if (hoveredObject instanceof Roll)
					object = plan.new Roll(hoveredObject.x(), hoveredObject.y(), hoveredObject.vertical(), ((Roll)hoveredObject).type());
				dragged = object;
				grabOffset = dragged.position();
				grabOffset.subtract(mouse);
			}
			else
			{
				if (GameInput.getMouseY() > toolBoxHeight)
					panning = true;
			}
		}
		else if (event.getButton() == MouseEvent.BUTTON3)
		{
			if (dragged != null)
				dragged.rotate();
		}
		else if (event.getButton() == MouseEvent.BUTTON1)
		{
			if (dragged == null)
			{
				if (hoveredObject != null && GameInput.getMouseY() > toolBoxHeight)
				{
					dragged = hoveredObject;
					grabOffset = dragged.position();
					grabOffset.subtract(mouse);
				}
			}
			else
			{
				if (trashHovered)
					dragged.remove();
				dragged = null;	
			}
			if (hoveredListOption != null)
			{
				setList(hoveredListOption);
			}
			if (hoveredType != null)
			{
				Vector position = mouse.clone();
				position.add(hoveredTypeOffset);
				
				dragged = list.create.create(plan, (int)position.getX(), (int)position.getY(), hoveredType, false);;
				grabOffset = hoveredTypeOffset;
			}
			if (steelHovered)
			{
				Vector position = mouse.clone();
				position.add(hoveredTypeOffset);
				Steel steel = plan.new Steel((int)position.getX(), (int)position.getY(), false);
				dragged = steel;
				grabOffset = hoveredTypeOffset;
			}
			if (legendHovered)
			{
				Vector position = mouse.clone();
				position.add(hoveredTypeOffset);
				Legend legend = plan.new Legend((int)position.getX(), (int)position.getY());
				dragged = legend;
				grabOffset = hoveredTypeOffset;
			}
		}
	}
	
	@Override
	public void keyPressEvent(KeyEvent event)
	{
		if (event.getKeyCode() == KeyEvent.VK_R && dragged != null)
			dragged.rotate();
		else if (event.getKeyCode() == KeyEvent.VK_S)
			save();
		else if (event.getKeyCode() == KeyEvent.VK_P)
		{
			Settings.defaults();
			Settings.save();
		}
		else if (event.getKeyCode() == KeyEvent.VK_DELETE || event.getKeyCode() == KeyEvent.VK_BACK_SPACE)
		{
			if (dragged != null)
			{
				dragged.remove();
				dragged = null;
			}
			else if (hoveredObject != null)
			{
				hoveredObject.remove();
				hoveredObject = null;
			}
		}
	}
	
	void save()
	{
		FIO.writeString(file, plan.export().toString());
	}
	
	@Override
	public void mouseMoveEvent(VectorInt delta, VectorInt newPosition)
	{
		if (dragged != null)
		{
			Vector mouse = relativeMouse();
			mouse.add(grabOffset);
			dragged.setPosition(mouse);
		}
	}
	
	@Override
	public void mouseDragEvent(VectorInt delta, VectorInt newPosition)
	{
		if (panning)
		{
			cameraPan.add(new Vector(delta));
		}
		if (dragged != null)
		{
			Vector mouse = relativeMouse();
			mouse.add(grabOffset);
			dragged.setPosition(mouse);
		}
	}
	
	@Override
	public void mouseScrollEvent(MouseWheelEvent event)
	{
		if (GameInput.getMouseY() < boxListHeight())
		{
			boxScroll += event.getWheelRotation() * 20;
			if (boxScroll < 0)
				boxScroll = 0;
			int max = maxBoxScroll();
			if (boxScroll > max)
				boxScroll = max;
		}
		else if (GameInput.getMouseY() > toolBoxHeight())
		{
			zoom -= event.getWheelRotation();
			if (zoom < 0)
				zoom = 0;
			if (zoom > 20)
				zoom = 20;
		}
	}
}