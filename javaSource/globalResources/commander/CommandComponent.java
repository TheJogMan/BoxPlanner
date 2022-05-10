package globalResources.commander;

import globalResources.richText.RichString;
import globalResources.utilities.Logger;

public interface CommandComponent
{
	public AbstractCommandConsole getConsole();
	public Logger getLogger();
	public boolean isConsole();
	public AbstractCategory getCategory();
	public String getName();
	public String getFullName(boolean hideRoot);
	public RichString getDescription();
	public AbstractValidator getValidator();
	
	public default String getFullName()
	{
		return getFullName(true);
	}
}