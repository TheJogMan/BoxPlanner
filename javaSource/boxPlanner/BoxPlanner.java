package boxPlanner;

import java.io.File;

import boxPlanner.scenes.Test;
import globalResources.utilities.FIO;
import mechanic.engine.Engine;
import mechanic.engine.Engine.EngineConfig;

public class BoxPlanner
{
	public static void main(String[] args)
	{
		BoxType.init();
		RollType.init();
		CaseType.init();
		if (FIO.canReadBytes(new File("Settings.txt")))
			Settings.load();
		else
		{
			Settings.defaults();
			Settings.save();
		}
		
		Engine.run(new Test(), 800, 600, "Box Planner", EngineConfig.minimal());
	}
}