package jogLibrary.universal.richString;

import java.awt.font.FontRenderContext;
import java.awt.font.TextLayout;

public class RichCharacter
{
	public static final char defaultCharacter = ' ';
	
	Character character;
	Style style;
	
	public RichCharacter(Character ch, Style style)
	{
		this.character = ch;
		this.style = style.clone();
	}
	
	public RichCharacter(Character ch)
	{
		this(ch, new Style());
	}
	
	public RichCharacter()
	{
		this(defaultCharacter);
	}
	
	public RichCharacter(char ch, Style style)
	{
		this(new Character(ch), style);
	}
	
	public RichCharacter(char ch)
	{
		this(ch, new Style());
	}
	
	public RichCharacter clone()
	{
		RichCharacter character = new RichCharacter();
		character.character = this.character;
		character.style = style.clone();
		return character;
	}
	
	public void setStyle(Style style)
	{
		this.style = style.clone();
	}
	
	public Style getStyle()
	{
		return style.clone();
	}
	
	/*
	TODO: implement draw method in RichCharacter
	public void draw(Canvas canvas, int x, int y)
	{
		font.draw(canvas, x, y, flagByte, fontScale, character, mainColor.color, backgroundColor.color);
	}
	*/
	
	public int getLogicalHeight()
	{
		return style.font.getStringBounds("" + character, new FontRenderContext(style.font.getTransform(), true, true)).getBounds().height;
	}
	
	public int getLogicalWidth()
	{
		return style.font.getStringBounds("" + character, new FontRenderContext(style.font.getTransform(), true, true)).getBounds().width;
	}
	
	public int getVisualHeight()
	{
		return (new TextLayout("" + character, style.font, new FontRenderContext(style.font.getTransform(), true, true))).getPixelBounds(null, 0, 0).height;
	}
	
	public int getVisualWidth()
	{
		return (new TextLayout("" + character, style.font, new FontRenderContext(style.font.getTransform(), true, true))).getPixelBounds(null, 0, 0).width;
	}
	
	public Character getCharacter()
	{
		return character;
	}
	
	public char getPrimitiveCharacter()
	{
		return character.charValue();
	}
	
	public void setCharacter(Character character)
	{
		this.character = character;
	}
	
	public void setCharacter(char ch)
	{
		character = new Character(ch);
	}
	
	public String encode(EncodingType type)
	{
		return type.encode(this);
	}
}