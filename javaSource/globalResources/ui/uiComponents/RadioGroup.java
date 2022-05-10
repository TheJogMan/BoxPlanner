package globalResources.ui.uiComponents;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import globalResources.graphics.Canvas;
import globalResources.graphics.Drawable;
import globalResources.ui.UIComponent;
import globalResources.ui.UIHolder;
import globalResources.utilities.VectorInt;

/**
 * RadioGroup component
 * <p>
 * Used to allow the user to select from a list of options were only one option can be selected at a time
 * </p>
 */
public class RadioGroup extends UIComponent
{
	List<String> buttons = new ArrayList<String>();
	String selection;
	String label;
	String hovered;
	String lastSelection;
	
	ArrayList<Runnable> changeListeners;
	
	/**
	 * Creates a new radio group
	 * @param parent the UIHolder to add this RadioGroup to
	 * @param position the position of the RadioGroup
	 * @param dimensions the dimensions of the RadioGroup
	 * @param label the RadioGroup's label
	 * @param buttons the various options in this RadioGroup
	 * @param defaultValue the default value
	 */
	public RadioGroup(UIHolder parent, VectorInt position, VectorInt dimensions, String label, List<String> buttons, String defaultValue)
	{
		super(parent, position, dimensions);
		this.label = label;
		this.buttons = buttons;
		changeListeners = new ArrayList<Runnable>();
		selection = defaultValue;
	}
	
	public void addChangeListener(Runnable runnable)
	{
		changeListeners.add(runnable);
	}
	
	/**
	 * Set the selection value
	 * @param selection selection value
	 */
	public void setSelection(String selection)
	{
		this.selection = selection;
	}
	
	/**
	 * Get the current selection value
	 * @return current selection value
	 */
	public String getSelection()
	{
		return selection;
	}
	
	/**
	 * Add an option to this RadioGroup
	 * @param option the new option
	 */
	public void addOption(String option)
	{
		buttons.add(option);
	}
	
	/**
	 * Get which option is currently being hovered over by the mouse cursor
	 * @return hovered option
	 */
	public String getHovered()
	{
		return hovered;
	}
	
	/**
	 * Checks if the current selection has changed since the last time this component was updated
	 * @return if the selection has changed
	 */
	public boolean selectionChanged()
	{
		if (lastSelection != null)
		{
			return !(lastSelection.compareTo(selection) == 0);
		}
		else
		{
			return false;
		}
	}
	
	@Override
	protected void update()
	{
		lastSelection = selection;
		if (clicked())
		{
			if (hovered.length() > 0)
			{
				selection = hovered;
				for (Iterator<Runnable> iterator = changeListeners.iterator(); iterator.hasNext();) iterator.next().run();
			}
		}
	}
	
	@Override
	protected void render(Drawable canvas)
	{
		int mouseX = getAdjustedMousePos().getX() - position.getX();
		int mouseY = getAdjustedMousePos().getY() - position.getY();
		Canvas drawable = canvas.getCanvas();
		drawable.drawTextR(label, 1, 0);
		int wid = 20;
		int y = drawable.getFontHeightR();
		int x = 0;
		int boxSize = drawable.getFontHeightR() - 4;
		hovered = "";
		for (Iterator<String> iterator = buttons.iterator(); iterator.hasNext();)
		{
			String button = iterator.next();
			int strWid = drawable.getStringLengthR(button);
			if (strWid > wid)
			{
				wid = strWid;
			}
			if (mouseX >= x && mouseX < x + strWid + boxSize + 4 && mouseY >= y && mouseY < y + drawable.getFontHeightR())
			{
				hovered = button;
			}
			if (hovered.compareTo(button) == 0)
			{
				drawable.drawRect(x, y, strWid + boxSize + 4, drawable.getFontHeightR(), Color.GRAY);
			}
			drawable.drawTextR(button, x + boxSize + 3, y);
			drawable.drawRect(x + 2, y + 2, boxSize, boxSize);
			if (selection.compareTo(button) == 0)
			{
				drawable.drawRect(x + 3, y + 3, boxSize - 2, boxSize - 2, Color.BLACK);
			}
			y += drawable.getFontHeightR();
			if (y + drawable.getFontHeightR() > dimensions.getY())
			{
				x += wid + 12 + boxSize;
				y = drawable.getFontHeightR();
			}
		}
	}
}