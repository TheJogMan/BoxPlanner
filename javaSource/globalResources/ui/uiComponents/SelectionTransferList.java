package globalResources.ui.uiComponents;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.UUID;

import globalResources.graphics.Canvas;
import globalResources.graphics.Drawable;
import globalResources.ui.UIComponent;
import globalResources.ui.UIHolder;
import globalResources.utilities.VectorInt;

public class SelectionTransferList extends UIComponent
{
	boolean selectionInSelected;
	String selection;
	
	ArrayList<Runnable> changeListeners;
	
	Button add;
	Button remove;
	Button clear;
	Button addAll;
	SelectionList selected;
	SelectionList selectable;
	
	public SelectionTransferList(UIHolder parent, VectorInt position, VectorInt dimensions)
	{
		super(parent, position, dimensions);
		this.canvas.setClearColor(Color.DARK_GRAY);
		changeListeners = new ArrayList<Runnable>();
		
		int listWidth = (dimensions.getX() - 10 - this.canvas.getStringLengthR("Remove")) / 2;
		
		selected = new SelectionList(this, new VectorInt(0, 0), new VectorInt(listWidth, dimensions.getY()));
		selected.addChangeListener(() ->
		{
			selectable.deselect();
			selectionInSelected = true;
		});
		selected.addDoubleSelectedListener(() -> deselectItem(selected.getSelectedID(), true));
		
		selectable = new SelectionList(this, new VectorInt(dimensions.getX() - selected.getDimensions().getX(), 0), selected.getDimensions());
		selectable.addChangeListener(() -> 
		{
			selected.deselect();
			selectionInSelected = false;
		});
		selectable.addDoubleSelectedListener(() -> selectItem(selectable.getSelectedID(), true));
		
		VectorInt dim = new VectorInt(canvas.getStringLengthR("Remove") + 4, canvas.getFontHeightR() + 4);
		clear = new Button(this, new VectorInt(listWidth + 3, dimensions.getY() / 2 - dim.getY() - 2), dim, "Clear");
		clear.registerClickEvent(() -> deselectAll(true));
		
		add = new Button(this, new VectorInt(clear.getPosition().getX(), clear.getPosition().getY() - dim.getY() - 4), dim, "Add");
		add.registerClickEvent(() ->
		{
			if (!selectionInSelected)
			{
				selectItem(selectable.getSelectedID(), true);
			}
		});
		
		remove = new Button(this, new VectorInt(clear.getPosition().getX(), clear.getPosition().getY() + dim.getY() + 4), dim, "Remove");
		remove.registerClickEvent(() ->
		{
			if (selectionInSelected)
			{
				deselectItem(selected.getSelectedID(), true);
			}
		});
		
		addAll = new Button(this, new VectorInt(remove.getPosition().getX(), remove.getPosition().getY() + dim.getY() + 4), dim, "Add All");
		addAll.registerClickEvent(() -> selectAll(true));
	}
	
	@Override
	protected void update()
	{
		
	}
	
	@Override
	protected void render(Drawable drawable)
	{
		Canvas canvas = drawable.getCanvas();
		int listWidth = (dimensions.getX() - 10 - canvas.getStringLengthR("Remove")) / 2;
		int gap = dimensions.getX() - listWidth * 2;
		
		canvas.drawRect(listWidth, 0, gap, 3, Color.BLACK);
		canvas.drawRect(listWidth, dimensions.getY() - 3, gap, 3, Color.BLACK);
	}
	
	public void deselectListItem()
	{
		selected.deselect();
		selectable.deselect();
		selectionInSelected = false;
	}
	
	public void selectItem(UUID id)
	{
		selectItem(id, false);
	}
	
	public void deselectItem(UUID id)
	{
		deselectItem(id, false);
	}
	
	public void addChangeListener(Runnable runnable)
	{
		changeListeners.add(runnable);
	}
	
	public void selectItem(UUID id, boolean trigger)
	{
		if (selectable.hasItem(id))
		{
			String name = selectable.getItem(id);
			selectable.removeItem(id);
			selected.setItem(id, name);
			selected.setSelection(id);
			selectable.deselect();
			selectionInSelected = true;
			if (trigger) for (Iterator<Runnable> iterator = changeListeners.iterator(); iterator.hasNext();) iterator.next().run();
		}
	}
	
	public void deselectItem(UUID id, boolean trigger)
	{
		if (selected.hasItem(id))
		{
			String name = selected.getItem(id);
			selected.removeItem(id);
			selectable.setItem(id, name);
			selectable.setSelection(id);
			selected.deselect();
			selectionInSelected = false;
			if (trigger) for (Iterator<Runnable> iterator = changeListeners.iterator(); iterator.hasNext();) iterator.next().run();
		}
	}
	
	public void deselectAll()
	{
		deselectAll(false);
	}
	
	public void deselectAll(boolean trigger)
	{
		UUID[] selected = getSelectedIDs();
		for (int index = 0; index < selected.length; index++) deselectItem(selected[index], trigger);
	}
	
	public void selectAll()
	{
		selectAll(false);
	}
	
	public void selectAll(boolean trigger)
	{
		UUID[] selectables = selectable.getItemIDs();
		for (int index = 0; index < selectables.length; index++) selectItem(selectables[index], trigger);
	}
	
	public boolean hasItem(UUID id)
	{
		if (selected.hasItem(id) || selectable.hasItem(id)) return true;
		else return false;
	}
	
	public UUID addItem(String item)
	{
		return selectable.addItem(item);
	}
	
	public void clear(boolean trigger)
	{
		deselectAll(trigger);
		selectable.clear();
	}
	
	public void removeItem(UUID id)
	{
		if (selected.hasItem(id)) deselectItem(id, true);
		selectable.removeItem(id);
	}
	
	public void setItem(UUID id, String item)
	{
		selectable.setItem(id, item);
	}
	
	public String getItem(UUID id)
	{
		return selectable.getItem(id);
	}
	
	public UUID[] getItemIDs()
	{
		UUID[] selectables = selectable.getItemIDs();
		UUID[] selecteds = selected.getItemIDs();
		UUID[] all = new UUID[selectables.length + selecteds.length];
		for (int index = 0; index < selectables.length; index++) all[index] = selectables[index];
		for (int index = 0; index < selecteds.length; index++) all[index + selectables.length] = selecteds[index];
		return all;
	}
	
	public UUID[] getSelectedIDs()
	{
		return selected.getItemIDs();
	}
	
	public String[] getSelectedItems()
	{
		UUID[] ids = getSelectedIDs();
		String[] items = new String[ids.length];
		for (int index = 0; index < items.length; index++) items[index] = selectable.getItem(ids[index]);
		return items;
	}
}