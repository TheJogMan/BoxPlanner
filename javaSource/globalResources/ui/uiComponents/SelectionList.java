package globalResources.ui.uiComponents;

import java.awt.Color;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.UUID;

import globalResources.graphics.Canvas;
import globalResources.graphics.Drawable;
import globalResources.ui.FrameInput.FrameInputListenerInterface;
import globalResources.utilities.VectorInt;
import globalResources.ui.UIComponent;
import globalResources.ui.UIHolder;

public class SelectionList extends UIComponent
{
	protected HashMap<UUID, String> list;
	protected ArrayList<UUID> sortedList;
	protected UUID selection;
	private UUID hoveredSelection;
	private int scroll;
	private boolean scrollBarGrabbed;
	
	private ArrayList<Runnable> changeListeners;
	private ArrayList<Runnable> doubleSelectedListeners;
	
	private TextField searchBar;
	
	public SelectionList(UIHolder parent, VectorInt position, VectorInt dimensions)
	{
		super(parent, position, dimensions);
		super.registerListener(new ComponentInputListener());
		super.addFrameListener(new FrameInputListener(this));
		this.canvas.setClearColor(Color.GRAY);
		list = new HashMap<UUID, String>();
		sortedList = new ArrayList<UUID>();
		selection = null;
		hoveredSelection = null;
		scroll = 0;
		scrollBarGrabbed = false;
		changeListeners = new ArrayList<Runnable>();
		doubleSelectedListeners = new ArrayList<Runnable>();
		
		searchBar = new TextField(this, new VectorInt(3, 3), new VectorInt(dimensions.getX() - 14, this.canvas.getFontHeightR()));
		searchBar.addTextChangeListener(() ->
		{
			doSort();
		});
	}
	
	public void updateContents(HashMap<UUID, String> contents)
	{
		updateContents(contents, false);
	}
	
	public void updateContents(HashMap<UUID, String> contents, boolean update)
	{
		list.clear();
		for (Iterator<Entry<UUID, String>> iterator = contents.entrySet().iterator(); iterator.hasNext();)
		{
			Entry<UUID, String> entry = iterator.next();
			list.put(entry.getKey(), entry.getValue());
		}
		if (!list.containsKey(selection)) deselect(update);
		doSort();
	}
	
	@Override
	protected void update()
	{
		int top = 3;
		int listHeight = sortedList.size() * canvas.getFontHeightR();
		int rawViewHeight = canvas.getHeight() - 6;
		int viewHeight = canvas.getHeight() - 6;
		int mx = getInternalMousePos().getX();
		if (getInput().isButton(MouseEvent.BUTTON1) && listHeight > viewHeight && mx >= dimensions.getX() - 8 && mx < dimensions.getX() - 3)
		{
			int barHeight = (int)((double)canvas.getHeight() / (double)listHeight * (double)viewHeight);
			int barY = (int)(-(double)scroll / (double)listHeight * (double)rawViewHeight + (double)top);
			int my = getInternalMousePos().getY();
			if (my < barY) scroll++;
			else if (my > barY + barHeight) scroll--;
		}
		restrainScroll();
	}
	
	@Override
	protected void render(Drawable drawable)
	{
		Canvas canvas = drawable.getCanvas();
		int y = searchBar.getDimensions().getY() + 3 + scroll;
		hoveredSelection = null;
		int my = this.getInternalMousePos().getY();
		boolean checkHovered = this.getInternalMousePos().getX() >= 3 && this.getInternalMousePos().getX() < dimensions.getX() - 11 && this.getInternalMousePos().getY() > searchBar.getDimensions().getY() + 3;
		for (Iterator<UUID> iterator = sortedList.iterator(); iterator.hasNext();)
		{
			UUID id = iterator.next();
			if (checkHovered && my >= y && my < y + canvas.getFontHeightR()) hoveredSelection = id;
			Color color = null;
			if (id.equals(selection)) color = Color.GREEN;
			else if (id.equals(hoveredSelection)) color = Color.WHITE;
			if (color != null) canvas.drawRect(3, y, searchBar.getDimensions().getX(), canvas.getFontHeightR(), color);
			canvas.drawTextR(list.get(id), 3, y, Color.BLACK);
			y += canvas.getFontHeightR();
		}
		
		canvas.drawRect(0, 0, dimensions.getX(), dimensions.getY(), false, Color.BLACK);
		canvas.drawRect(1, 1, dimensions.getX() - 2, dimensions.getY() - 2, false, Color.BLACK);
		canvas.drawRect(2, 2, dimensions.getX() - 4, dimensions.getY() - 4, false, Color.BLACK);
		canvas.drawRect(dimensions.getX() - 8, 3, 5, dimensions.getY() - 6, Color.DARK_GRAY);
		canvas.drawRect(dimensions.getX() - 11, 3, 3, dimensions.getY() - 6, Color.BLACK);
		canvas.drawRect(3, 3 + searchBar.getDimensions().getY(), searchBar.getDimensions().getX(), 3, Color.BLACK);
		
		Color barColor = Color.BLUE;
		int listHeight = sortedList.size() * canvas.getFontHeightR();
		int rawViewHeight = canvas.getHeight() - 6;
		int viewHeight = canvas.getHeight() - 9 - searchBar.getDimensions().getY();
		int barHeight = (int)((double)canvas.getHeight() / (double)listHeight * (double)viewHeight);
		if (barHeight == 0) barHeight = 1;
		if (listHeight > viewHeight)
		{
			int barY = (int)(-(double)scroll / (double)listHeight * (double)rawViewHeight + 3.0);
			canvas.drawRect(searchBar.getDimensions().getX() + 6, barY, 5, barHeight, barColor);
		}
		else
		{
			canvas.drawRect(searchBar.getDimensions().getX() + 6, 3, 5, rawViewHeight, barColor);
		}
		if (sortedList.size() > 0)
		{
			double itemHeight = (double)rawViewHeight / (double)viewHeight * (double)canvas.getFontHeightR();
			
			int selectedY = (int)((double)sortedList.indexOf(selection) * itemHeight + 3.0);
			int hoveredY = (int)((double)sortedList.indexOf(hoveredSelection) * itemHeight + 3.0);
			
			int renderedItemHeight = (int)itemHeight;
			if (renderedItemHeight < 0) renderedItemHeight = 1;
			if (hoveredSelection != null && sortedList.contains(hoveredSelection)) canvas.drawRect(searchBar.getDimensions().getX() + 6, hoveredY, 5, renderedItemHeight, Color.WHITE);
			if (selection != null && sortedList.contains(selection)) canvas.drawRect(searchBar.getDimensions().getX() + 6, selectedY, 5, renderedItemHeight, Color.GREEN);
		}
	}
	
	public void deselect()
	{
		deselect(false);
	}
	
	public void deselect(boolean update)
	{
		setSelection(null, update);
	}
	
	public void setSelection(UUID id)
	{
		setSelection(id, false);
	}
	
	public void addChangeListener(Runnable listener)
	{
		changeListeners.add(listener);
	}
	
	public void addDoubleSelectedListener(Runnable listener)
	{
		doubleSelectedListeners.add(listener);
	}
	
	public String getSelectedItem()
	{
		if (selection != null) return list.get(selection);
		else return null;
	}
	
	public UUID getSelectedID()
	{
		return selection;
	}
	
	public boolean hasItem(UUID id)
	{
		return list.containsKey(id);
	}
	
	public UUID addItem(String item)
	{
		UUID id = UUID.randomUUID();
		list.put(id, item);
		doSort();
		return id;
	}
	
	public void setItem(UUID id, String item)
	{
		list.put(id, item);
		doSort();
	}
	
	public void removeItem(UUID id)
	{
		list.remove(id);
		if (id.equals(selection)) selection = null;
		doSort();
	}
	
	public void clear()
	{
		list.clear();
		sortedList.clear();
		selection = null;
	}
	
	public void setSearchTerm(String term)
	{
		searchBar.setText(term);
		doSort();
	}
	
	public String getSearchTerm()
	{
		return searchBar.getText();
	}
	
	public String getItem(UUID id)
	{
		return list.get(id);
	}
	
	public UUID[] getItemIDs()
	{
		return list.keySet().toArray(new UUID[list.keySet().size()]);
	}
	
	protected void doSort()
	{
		String term = searchBar.getText().toLowerCase();
		sortedList.clear();
		if (term.length() == 0) for (Iterator<UUID> iterator = list.keySet().iterator(); iterator.hasNext();) sortedList.add(iterator.next());
		else
		{
			for (Iterator<Entry<UUID, String>> iterator = list.entrySet().iterator(); iterator.hasNext();)
			{
				Entry<UUID, String> item = iterator.next();
				if (item.getValue().toLowerCase().contains(term)) sortedList.add(item.getKey());
			}
		}
		UUID temp;
		for (int i = 0; i < sortedList.size(); i++) 
        {
			for (int j = i + 1; j < sortedList.size(); j++)
            { 
                if (sortedList.get(i).compareTo(sortedList.get(j)) > 0) 
                {
                    temp = sortedList.get(i);
                    sortedList.set(i, sortedList.get(j));
                    sortedList.set(j, temp);
                }
            }
        }
		restrainScroll();
	}
	
	protected void restrainScroll()
	{
		Canvas canvas = this.getCanvas();
		int listHeight = sortedList.size() * canvas.getFontHeightR();
		int viewHeight = canvas.getHeight() - 6 - searchBar.getDimensions().getY();
		if (listHeight < viewHeight) scroll = 0;
		else
		{
			if (scroll > 0) scroll = 0;
			if (scroll + listHeight < viewHeight) scroll = viewHeight - listHeight;
		}
	}
	
	public void setSelection(UUID id, boolean trigger)
	{
		if (id == null || list.containsKey(id))
		{
			if (selection != null && selection.equals(id)) if (trigger) for (Iterator<Runnable> iterator = doubleSelectedListeners.iterator(); iterator.hasNext();) iterator.next().run();
			selection = id;
			if (trigger) for (Iterator<Runnable> iterator = changeListeners.iterator(); iterator.hasNext();) iterator.next().run();
		}
	}
	
	class ComponentInputListener extends ComponentListener
	{
		@Override
		public void clicked()
		{
			
		}
		
		@Override
		public void unClicked()
		{
			
		}
		
		@Override
		public void hovered()
		{
			
		}
		
		@Override
		public void unHovered()
		{
			
		}
	}
	
	class FrameInputListener implements FrameInputListenerInterface
	{
		UIComponent component;
		
		FrameInputListener(UIComponent component)
		{
			this.component = component;
		}
		
		@Override
		public void buttonPressEvent(MouseEvent event)
		{
			if (event.getButton() == MouseEvent.BUTTON1 && component.hovered())
			{
				if (hoveredSelection != null) setSelection(hoveredSelection, true);
				else
				{
					int mx = component.getInternalMousePos().getX();
					if (mx >= canvas.getWidth() - 6 && mx < canvas.getWidth() - 3) scrollBarGrabbed = true;
				}
			}
		}
		
		@Override
		public void buttonReleaseEvent(MouseEvent event)
		{
			scrollBarGrabbed = false;
		}
		
		@Override
		public void keyPressEvent(KeyEvent event)
		{
			
		}
		
		@Override
		public void keyReleaseEvent(KeyEvent event)
		{
			
		}
		
		@Override
		public void textEditEvent(KeyEvent event)
		{
			
		}
		
		@Override
		public void textInputEvent(KeyEvent event)
		{
			
		}
		
		@Override
		public void mouseScrollEvent(MouseWheelEvent event)
		{
			if (component.hovered()) scroll -= event.getWheelRotation() * canvas.getFontHeightR();
		}
		
		@Override
		public void mouseMoveEvent(VectorInt delta, VectorInt newPosition)
		{
			
		}
		
		@Override
		public void mouseDragEvent(VectorInt delta, VectorInt newPosition)
		{
			int listHeight = sortedList.size() * canvas.getFontHeightR();
			int viewHeight = canvas.getHeight();
			if (scrollBarGrabbed && listHeight > viewHeight)
			{
				int amount = (int)((double)delta.getY() / (double)viewHeight * (double)listHeight);
				scroll -= amount;
			}
		}
	}
}