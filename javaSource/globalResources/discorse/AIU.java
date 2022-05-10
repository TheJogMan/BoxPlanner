package globalResources.discorse;

public class AIU
{
	public static QuickConsumptionResult quickConsume(String string)
	{
		return quickConsume(string, ' ');
	}
	
	public static QuickConsumptionResult quickConsume(String string, char character)
	{
		return quickConsume(string, character, true);
	}
	
	public static QuickConsumptionResult quickConsume(String string, char character, boolean dropFlagCharacter)
	{
		return new QuickConsumptionResult(string, character, dropFlagCharacter);
	}
	
	public static class QuickConsumptionResult
	{
		private String consumed;
		private String remaining = "";
		
		private QuickConsumptionResult(String string, char character, boolean dropFlagCharacter)
		{
			int index = string.indexOf(character);
			if (index == -1) consumed = string;
			else
			{
				consumed = string.substring(0, index);
				if (dropFlagCharacter) index++;
				if (index < string.length()) remaining = string.substring(index);
				else remaining = "";
			}
		}
		
		public String getConsumed()
		{
			return consumed;
		}
		
		public String getRemaining()
		{
			return remaining;
		}
	}
}