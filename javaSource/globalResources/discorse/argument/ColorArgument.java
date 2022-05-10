package globalResources.discorse.argument;

import java.awt.Color;
import java.util.ArrayList;
import java.util.HashMap;

import globalResources.commander.AbstractExecutor;
import globalResources.discorse.AIU;
import globalResources.discorse.ArgumentConsumptionResult;
import globalResources.richText.RichString;

public class ColorArgument implements Argument<Color>
{
	private static HashMap<String, Color> colors = createMap();
	
	private static HashMap<String, Color> createMap()
	{
		HashMap<String, Color> map = new HashMap<String, Color>();
		
		map.put("white", new Color(255, 255, 255));
		map.put("black", new Color(0, 0, 0));
		map.put("red", new Color(255, 0, 0));
		map.put("green", new Color(0, 255, 0));
		map.put("blue", new Color(0, 0, 255));
		
		return map;
	}
	
	@Override
	public void init(Object[] data)
	{
		
	}
	
	@Override
	public String getName()
	{
		return "Color";
	}
	
	@Override
	public void getCompletions(String argumentString, ArrayList<String> completions, AbstractExecutor executor)
	{
		
	}
	
	@Override
	public ArgumentConsumptionResult<Color> consume(String string, AbstractExecutor executor)
	{
		String argument = AIU.quickConsume(string).getConsumed().toLowerCase();
		if (colors.containsKey(argument))
		{
			return new ArgumentConsumptionResult<Color>(true, colors.get(argument), argument, new RichString("color"), executor, this);
		}
		else
		{
			return new ArgumentConsumptionResult<Color>(false, Color.BLACK, argument, new RichString("invalid color"), executor, this);
		}
	}
}