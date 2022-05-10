package globalResources.commander;

import globalResources.discorse.argument.MultiListInterpretationResult;

class SegmentedCommandString
{
	AbstractCommandConsole console;
	String originalCommandString;
	
	boolean finalPartComplete;
	AbstractCategory deepestValidCategory;
	AbstractCommand command;
	String argumentString;
	MultiListInterpretationResult interpretationResult;
	
	String workingComponent;
	
	SegmentedCommandString(String command, AbstractCommandConsole console, AbstractExecutor executor)
	{
		this.originalCommandString = command;
		this.console = console;
		segment(executor);
	}
	
	private void segment(AbstractExecutor executor)
	{
		deepestValidCategory = console;
		command = null;
		argumentString = "";
		finalPartComplete = false;
		
		workingComponent = "";
		
		for (int index = 0; index < originalCommandString.length(); index++)
		{
			char ch = originalCommandString.charAt(index);
			
			if (command == null && ch == ' ') interpretComponent();
			else workingComponent += ch;
		}
		
		if (command == null) interpretComponent();
		else argumentString = workingComponent;
		
		if (command == null) finalPartComplete = originalCommandString.length() == 0 || originalCommandString.charAt(originalCommandString.length() - 1) == ' ';
		else
		{
			interpretationResult = command.argumentList.interpret(argumentString, executor);
			finalPartComplete = interpretationResult.valid();
		}
	}
	
	private void interpretComponent()
	{
		if (deepestValidCategory.hasCommand(workingComponent))
		{
			command = deepestValidCategory.getCommand(workingComponent);
			workingComponent = "";
		}
		else if (deepestValidCategory.hasCategory(workingComponent))
		{
			deepestValidCategory = deepestValidCategory.getCategory(workingComponent);
			workingComponent = "";
		}
	}
}