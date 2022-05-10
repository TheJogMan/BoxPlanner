package globalResources.discorse;

import java.util.ArrayList;

import globalResources.commander.AbstractExecutor;
import globalResources.discorse.argument.InterpretationResult;

public interface ArgumentInterpreter
{
	public InterpretationResult interpret(String arguments, AbstractExecutor executor);
	public String getArgumentNameString();
	public void completions(String arguments, AbstractExecutor executor, ArrayList<String> completions, boolean filterCandidates);
	
	public default ArrayList<String> completions(String arguments, AbstractExecutor executor, boolean filterCandidates)
	{
		ArrayList<String> completions = new ArrayList<String>();
		completions(arguments, executor, completions, filterCandidates);
		return completions;
	}
}