package globalResources.ui.uiComponents;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Iterator;

import globalResources.graphics.Canvas;
import globalResources.graphics.Drawable;
import globalResources.ui.UIComponent;
import globalResources.ui.UIHolder;
import globalResources.utilities.VectorInt;

public class ColorSelector extends UIComponent
{
	Color color;
	
	TextField redText;
	TextField greenText;
	TextField blueText;
	
	Slider redSlider;
	Slider greenSlider;
	Slider blueSlider;
	
	ArrayList<Runnable> changeListeners;
	
	public ColorSelector(UIHolder parent, VectorInt position, VectorInt dimensions)
	{
		super(parent, position, dimensions);
		color = Color.BLACK;
		changeListeners = new ArrayList<Runnable>();
		
		Canvas canvas = drawable.getCanvas();
		int width = canvas.getStringLengthR("###");
		blueText = new TextField(this, new VectorInt(dimensions.getX() - 3 - width - 4, dimensions.getY() - 3 - canvas.getFontHeightR() - 4), new VectorInt(width + 4, canvas.getFontHeightR() + 4));
		blueText.addTextChangeListener(() -> changeColor(color.getRed(), color.getGreen(), Integer.parseInt(blueText.getText())));
		greenText = new TextField(this, new VectorInt(blueText.getPosition().getX(), blueText.getPosition().getY() - blueText.getDimensions().getY()), blueText.getDimensions());
		greenText.addTextChangeListener(() -> changeColor(color.getRed(), Integer.parseInt(greenText.getText()), color.getBlue()));
		redText = new TextField(this, new VectorInt(blueText.getPosition().getX(), greenText.getPosition().getY() - blueText.getDimensions().getY()), blueText.getDimensions());
		redText.addTextChangeListener(() -> changeColor(Integer.parseInt(redText.getText()), color.getGreen(), color.getBlue()));
		
		VectorInt pos = new VectorInt(canvas.getStringLengthR("Green") + 4, redText.getPosition().getY() + 1);
		VectorInt dim = new VectorInt(redText.getPosition().getX() - pos.getX() - 3, redText.getDimensions().getY() - 2);
		redSlider = new Slider(this, pos, dim, 0, 255, 255);
		redSlider.setBarColor(Color.RED);
		redSlider.addChangeListener(() -> changeColor((int)redSlider.getValue(), color.getGreen(), color.getBlue()));
		pos.setY(greenText.getPosition().getY() + 1);
		greenSlider = new Slider(this, pos, dim, 0, 255, 255);
		greenSlider.setBarColor(Color.GREEN);
		greenSlider.addChangeListener(() -> changeColor(color.getRed(), (int)greenSlider.getValue(), color.getBlue()));
		pos.setY(blueText.getPosition().getY() + 1);
		blueSlider = new Slider(this, pos, dim, 0, 255, 255);
		blueSlider.setBarColor(Color.BLUE);
		blueSlider.addChangeListener(() -> changeColor(color.getRed(), color.getGreen(), (int)blueSlider.getValue()));
	}
	
	public Color getColor()
	{
		return color;
	}
	
	public void setColor(Color color)
	{
		this.color = color;
		redText.setText(color.getRed() + "");
		greenText.setText(color.getGreen() + "");
		blueText.setText(color.getBlue() + "");
		redSlider.setValue(color.getRed());
		greenSlider.setValue(color.getGreen());
		blueSlider.setValue(color.getBlue());
	}
	
	public void addChangeListener(Runnable runnable)
	{
		changeListeners.add(runnable);
	}
	
	@Override
	protected void update()
	{
		
	}
	
	@Override
	protected void render(Drawable drawable)
	{
		Canvas canvas = drawable.getCanvas();
		canvas.drawRect(3, 3, dimensions.getX() - 6, dimensions.getY() - (12 + redText.getDimensions().getY() * 3), color);
		canvas.drawTextR("Red", 1, redText.getPosition().getY(), Color.RED);
		canvas.drawTextR("Green", 1, greenText.getPosition().getY(), Color.GREEN);
		canvas.drawTextR("Blue", 1, blueText.getPosition().getY(), Color.BLUE);
	}
	
	void changeColor(int red, int green, int blue)
	{
		color = new Color(red, green, blue);
		redText.setText(color.getRed() + "");
		greenText.setText(color.getGreen() + "");
		blueText.setText(color.getBlue() + "");
		redSlider.setValue(color.getRed());
		greenSlider.setValue(color.getGreen());
		blueSlider.setValue(color.getBlue());
		for (Iterator<Runnable> iterator = changeListeners.iterator(); iterator.hasNext();) iterator.next().run();
	}
}