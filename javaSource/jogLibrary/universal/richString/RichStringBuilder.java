package jogLibrary.universal.richString;

import jogLibrary.universal.dataStructures.ShiftingLinkedList;
import jogLibrary.universal.dataStructures.ShiftingLinkedList.ShiftingLinkedListIterator;

public class RichStringBuilder
{
	private ShiftingLinkedList<RichCharacter> characters;
	Style style = new Style();
	
	public static RichStringBuilder start()
	{
		return new RichStringBuilder();
	}
	
	public RichStringBuilder()
	{
		characters = new ShiftingLinkedList<RichCharacter>();
	}
	
	public RichStringBuilder(RichString base)
	{
		this();
		append(base);
	}
	
	public RichStringBuilder(String base)
	{
		this();
		append(base);
	}
	
	public RichStringBuilder newLine()
	{
		append('\n');
		return this;
	}
	
	public RichStringBuilder append(RichCharacter character)
	{
		characters.add(character);
		return this;
	}
	
	public RichStringBuilder append(char character)
	{
		append(new RichCharacter(character, style));
		return this;
	}
	
	public RichStringBuilder append(RichString string)
	{
		for (int index = 0; index < string.length(); index++) characters.add(string.charAt(index).clone());
		return this;
	}
	
	public RichStringBuilder preAppend(RichStringBuilder builder)
	{
		for (ShiftingLinkedListIterator<RichCharacter> iterator = builder.characters.reverseIterator(); iterator.hasNext();) characters.addToBeginning(iterator.next());
		return this;
	}
	
	public RichStringBuilder append(String string)
	{
		append(new RichString(string, style));
		return this;
	}
	
	public RichStringBuilder append(Style temporaryStyle, RichString string)
	{
		for (int index = 0; index < string.length(); index++)
		{
			RichCharacter character = string.charAt(index).clone();
			character.setStyle(temporaryStyle);
			characters.add(character);
		}
		return this;
	}
	
	public RichStringBuilder append(Style temporaryStyle, String string)
	{
		Style style = this.style;
		setStyle(temporaryStyle);
		append(string);
		setStyle(style);
		return this;
	}
	
	public RichString build()
	{
		return new RichString(characters);
	}
	
	public RichStringBuilder clear()
	{
		characters.clear();
		return this;
	}
	
	public int length()
	{
		return characters.size();
	}
	
	public RichStringBuilder setStyle(Style style)
	{
		this.style = style.clone();
		return this;
	}
	
	public Style getStyle()
	{
		return style.clone();
	}
}