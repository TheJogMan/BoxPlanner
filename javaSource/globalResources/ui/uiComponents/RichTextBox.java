package globalResources.ui.uiComponents;

import globalResources.graphics.Canvas;
import globalResources.graphics.Drawable;
import globalResources.richText.RichString;
import globalResources.ui.UIComponent;
import globalResources.ui.UIHolder;
import globalResources.utilities.VectorInt;

public class RichTextBox extends UIComponent
{
	RichString string;
	
	public RichTextBox(UIHolder parent, VectorInt position, VectorInt dimensions)
	{
		super(parent, position, dimensions);
	}
	
	@Override
	protected void update()
	{
		
	}
	
	@Override
	protected void render(Drawable drawable)
	{
		Canvas canvas = drawable.getCanvas();
		canvas.drawTextF(string, 0, 0, canvas.getWidth(), canvas.getHeight());
	}
}