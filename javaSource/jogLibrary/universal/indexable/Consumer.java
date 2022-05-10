package jogLibrary.universal.indexable;

import jogLibrary.universal.ReturnResult;
import jogLibrary.universal.richString.RichString;

public interface Consumer<ResultType, InputType>
{
	public ConsumerResult<ResultType, InputType> consume(Indexer<InputType> source);
	
	public static class ConsumerResult<ResultType, InputType> extends ReturnResult<ResultType>
	{
		private Indexer<InputType> indexer;
		
		public ConsumerResult(ResultType result, Indexer<InputType> indexer, boolean success, RichString description)
		{
			super(description, success, result);
			this.indexer = indexer;
		}
		
		public ConsumerResult(ResultType result, Indexer<InputType> indexer, boolean success, String description)
		{
			this(result, indexer, success, new RichString(description));
		}
		
		public ConsumerResult(ResultType result, Indexer<InputType> indexer)
		{
			this(result, indexer, true, "No description.");
		}
		
		public ConsumerResult(ResultType result, Indexer<InputType> indexer, String description)
		{
			this(result, indexer, new RichString(description));
		}
		
		public ConsumerResult(ResultType result, Indexer<InputType> indexer, RichString description)
		{
			this(result, indexer, true, description);
		}
		
		public ConsumerResult(Indexer<InputType> indexer)
		{
			this(null, indexer, false, "No description.");
		}
		
		public ConsumerResult(Indexer<InputType> indexer, String description)
		{
			this(indexer, new RichString(description));
		}
		
		public ConsumerResult(Indexer<InputType> indexer, RichString description)
		{
			this(null, indexer, false, description);
		}
		
		public Indexer<InputType> indexer()
		{
			return indexer;
		}
	}
}