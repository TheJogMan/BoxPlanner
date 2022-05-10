package globalResources.graphics.jFont;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import globalResources.graphics.Image;
import globalResources.graphics.ImageData;
import globalResources.utilities.StringUtil;

/**
 * Jog Font System
 */
public class JFont
{
	public static final JFont STANDARD = new JFont("/resources/fonts/set1.png");
	
	private Image fontImage;
	private int[] upperOffsets;
	private int[] upperWidths;
	private int[] lowerOffsets;
	private int[] lowerWidths;
	private int verticalSeperator;
	private int characterHeight;
	private char[] upperChars = {'A','B','C','D','E','F','G','H','I','J','K','L','M','N','O','P','Q','R','S','T','U','V','W','X','Y','Z',')','!','@','#','$','%','^','&','*','(','>','<',':','"','{','}','|','+','_','~','?'};
	private char[] lowerChars = {' ','a','b','c','d','e','f','g','h','i','j','k','l','m','n','o','p','q','r','s','t','u','v','w','x','y','z','0','1','2','3','4','5','6','7','8','9','.',',',';','\'','[',']','\\','=','-','`','/'};
	
	private String path;
	
	/**
	 * Create a new JFont
	 * @param path file path leading to the font image
	 */
	public JFont(String path)
	{
		this.path = path;
		fontImage = new Image(new ImageData(path));
		upperOffsets = new int[upperChars.length];
		upperWidths = new int[upperChars.length];
		lowerOffsets = new int[lowerChars.length];
		lowerWidths = new int[lowerChars.length];
		upperOffsets[0] = 0;
		lowerOffsets[0] = 0;
		
		int currentChar = 0;
		for (int i = 0; i < fontImage.getWidth(); i++)
		{
			if (fontImage.getPixel(i, 0) == 0xffff0000)
			{
				if (currentChar + 1 < upperOffsets.length)
				{
					upperOffsets[currentChar + 1] = i + 1;
				}
				upperWidths[currentChar] = i - 1 - upperOffsets[currentChar];
				currentChar++;
			}
		}
		upperWidths[upperWidths.length - 1] = fontImage.getWidth() - upperOffsets[upperOffsets.length - 1];
		
		for (int i = 0; i < fontImage.getHeight(); i++)
		{
			if (fontImage.getPixel(0, i) == 0xffff0000)
			{
				verticalSeperator = i;
				characterHeight = i;
				break;
			}
		}
		
		currentChar = 0;
		for (int i = 0; i < fontImage.getWidth(); i++)
		{
			if (fontImage.getPixel(i, verticalSeperator + 1) == 0xffff0000)
			{
				if (currentChar + 1 < lowerOffsets.length)
				{
					lowerOffsets[currentChar + 1] = i + 1;
				}
				lowerWidths[currentChar] = i - 1 - lowerOffsets[currentChar];
				currentChar++;
			}
		}
		lowerWidths[lowerWidths.length - 1] = fontImage.getWidth() - lowerOffsets[lowerOffsets.length - 1];
	}
	
	public boolean sameFont(JFont font)
	{
		return path.compareTo(font.path) == 0;
	}
	
	/**
	 * Get the width of a string
	 * @param str the string to measure
	 * @return the width of the string
	 */
	public int getStringWidth(String str)
	{
		int wid = 0;
		for (int index = 0; index < str.length(); index++)
		{
			wid += getCharacterWidth(str.charAt(index));
		}
		return wid;
	}
	
	/**
	 * Get the y position of the vertical separator in the font image
	 * @return y position of the vertical separator
	 */
	public int getVerticalSeperator()
	{
		return verticalSeperator;
	}
	
	/**
	 * Get the height of the font
	 * @return height of the font
	 */
	public int getCharacterHeight()
	{
		return characterHeight;
	}
	
	/**
	 * Get the font image
	 * @return font image
	 */
	public Image getFontImage()
	{
		return fontImage;
	}
	
	/**
	 * Gets CharacterData for a given character
	 * @param character the character to get data for
	 * @return the CharacterData
	 */
	public CharacterData getCharacterData(char character)
	{
		int offset = 0;
		int width = 0;
		boolean isUpperCase = true;
		boolean found = false;
		
		for (int i = 0; i < lowerChars.length; i++)
		{
			if (lowerChars[i] == character)
			{
				offset = lowerOffsets[i];
				width = lowerWidths[i];
				isUpperCase = false;
				found = true;
				break;
			}
		}
		if (isUpperCase)
		{
			for (int i = 0; i < upperChars.length; i++)
			{
				if (upperChars[i] == character)
				{
					offset = upperOffsets[i];
					width = upperWidths[i];
					found = true;
					break;
				}
			}
		}
		
		if (!found)
		{
			return getCharacterData(' ');
		}
		
		return new CharacterData(character, this, offset, isUpperCase, width);
	}
	
	/**
	 * Get the x position of a character within the font image
	 * @param character the character to locate
	 * @return x position of the character
	 */
	public int getCharacterX(char character)
	{
		CharacterData data = getCharacterData(character);
		return data.getOffset();
	}
	
	/**
	 * Get the width of a character
	 * @param character the character to measure
	 * @return the width of the character
	 */
	public int getCharacterWidth(char character)
	{
		if (character == '	')
		{
			CharacterData data = getCharacterData(' ');
			return data.getWidth() * 3;
		}
		else
		{
			CharacterData data = getCharacterData(character);
			return data.getWidth();
		}
	}
	
	/**
	 * Get the y position of a character within the font image
	 * @param character the character to locate
	 * @return y position of the character
	 */
	public int getCharacterY(char character)
	{
		CharacterData data = getCharacterData(character);
		if (data.isUpperCase())
		{
			return 0;
		}
		else
		{
			return verticalSeperator + 1;
		}
	}
	
	/**
	 * Formats lines of text to fit with a specified width
	 * @param text lines to be formated
	 * @param font font to be used
	 * @param scale scale to be used
	 * @param width width to fit within
	 * @return formated text
	 */
	public static List<String> formatToWidth(List<String> text, JFont font, double scale, int width)
	{
		List<String> lines = new ArrayList<String>();
		for (Iterator<String> textIterator = text.iterator(); textIterator.hasNext();)
		{
			List<String> textLines = formatToWidth(textIterator.next(), font, scale, width);
			for (Iterator<String> lineIterator = textLines.iterator(); lineIterator.hasNext();)
			{
				lines.add(lineIterator.next());
			}
		}
		return lines;
	}
	
	/**
	 * Formats a single line of text to fit with a specified width
	 * @param text lines to be formated
	 * @param font font to be used
	 * @param scale scale to be used
	 * @param width width to fit within
	 * @return formated text
	 */
	public static List<String> formatToWidth(String text, JFont font, double scale, int width)
	{
		List<String> lines = new ArrayList<String>();
		List<String> words = new ArrayList<String>();
		String currentWord = "";
		String currentLine = "";
		
		for (int c = 0; c < text.length(); c++)
		{
			char ch = text.charAt(c);
			boolean seperator = false;
			for (int a = 0; a < StringUtil.wordSeparators.length; a++)
			{
				if (StringUtil.wordSeparators[a] == ch)
				{
					seperator = true;
					break;
				}
			}
			if (seperator)
			{
				words.add(currentWord);
				words.add("" + ch);
				currentWord = "";
			}
			else
			{
				currentWord = currentWord + ch;
			}
		}
		if (currentWord.length() > 0) words.add(currentWord);
		
		if (words.size() > 0)
		{
			int currentIndex = 0;
			do
			{
				currentWord = words.get(currentIndex);
				if (font.getStringWidth(currentLine + currentWord) * scale <= width)
				{
					currentLine = currentLine + currentWord;
					currentIndex++;
				}
				else
				{
					if (currentLine.length() > 0)
					{
						lines.add(currentLine);
						currentLine = "";
					}
					else currentIndex++;
				}
			}
			while(currentIndex < words.size());
			if (currentLine.length() > 0) lines.add(currentLine);
		}
		
		return lines;
	}
	
	/**
	 * Truncates lines of text to fit within a given height
	 * @param lines text to truncate
	 * @param font font to be used
	 * @param scale scale to be used
	 * @param height height to be formated to
	 * @return formated lines
	 */
	public static List<String> trimToHeight(List<String> lines, JFont font, double scale, int height)
	{
		while (lines.size() * scale > height)
		{
			lines.remove(lines.size() - 1);
		}
		return lines;
	}
}