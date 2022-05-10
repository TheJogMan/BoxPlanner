package jogLibrary.universal.commander;

import java.util.List;

import jogLibrary.universal.ReturnResult;
import jogLibrary.universal.indexable.HoardingIndexer;

public interface Interpretable<InterpretationResult>
{
	public abstract ReturnResult<InterpretationResult> interpret(HoardingIndexer<Character> source, Executor executor);
	public abstract List<String> getCompletions(HoardingIndexer<Character> source, Executor executor);
}