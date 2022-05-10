package globalResources.graphics.jFont;

/**
 * Contains information for a specific character in a JFont
 */
public class CharacterData
{
	private char character;
	private JFont font;
	private int characterX;
	private boolean isUpperCase;
	private int characterWidth;
	
	CharacterData(char character, JFont font, int characterX, boolean isUpperCase, int characterWidth)
	{
		this.character = character;
		this.font = font;
		this.characterX = characterX;
		this.isUpperCase = isUpperCase;
		this.characterWidth = characterWidth;
	}
	
	/**
	 * Gets the character represented by this CharacterData
	 * @return the character represented by this CharacterData
	 */
	public char getCharacter()
	{
		return character;
	}
	
	/**
	 * Gets the JFont that this CharacterData belongs to
	 * @return the JFont that this CharacterData belongs to
	 */
	public JFont getFont()
	{
		return font;
	}
	
	/**
	 * Get the x Offset of this character within the font image
	 * @return x offset
	 */
	public int getOffset()
	{
		return characterX;
	}
	
	/**
	 * Gets the width of this character
	 * @return width of the character
	 */
	public int getWidth()
	{
		return characterWidth;
	}
	
	/**
	 * Gets whether this character is upper case
	 * @return whether this character is upper case
	 */
	public boolean isUpperCase()
	{
		return isUpperCase;
	}
}