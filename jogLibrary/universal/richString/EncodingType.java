package jogLibrary.universal.richString;

import java.util.Random;

public enum EncodingType
{
	CODED((style, prefix) ->
	{
		if (prefix)
		{
			String code = "";
			if (style.isBold())
				code = (code + RichColor.characterCodePrefix) + RichColor.characterCodeBold;
			if (style.isItalic())
				code = (code + RichColor.characterCodePrefix) + RichColor.characterCodeItalic;
			if (style.isUnderline())
				code = (code + RichColor.characterCodePrefix) + RichColor.characterCodeUnderline;
			if (style.isStrikeThrough())
				code = (code + RichColor.characterCodePrefix) + RichColor.characterCodeStrikethrough;
			if (style.isObfuscated())
				code = (code + RichColor.characterCodePrefix) + RichColor.characterCodeObfuscate;
			code += style.mainColor.toInlineCode();
			return code;
		}
		else
			return RichColor.characterCodePrefix + "" + RichColor.characterCodeReset;
	},
	(from, to) ->
	{
		String code = "";
		if (from.isBold() != to.isBold())
			code = (code + RichColor.characterCodePrefix) + RichColor.characterCodeBold;
		if (from.isItalic() != to.isItalic())
			code = (code + RichColor.characterCodePrefix) + RichColor.characterCodeItalic;
		if (from.isUnderline() != to.isUnderline())
			code = (code + RichColor.characterCodePrefix) + RichColor.characterCodeUnderline;
		if (from.isStrikeThrough() != to.isStrikeThrough())
			code = (code + RichColor.characterCodePrefix) + RichColor.characterCodeStrikethrough;
		if (from.isObfuscated() != to.isObfuscated())
			code = (code + RichColor.characterCodePrefix) + RichColor.characterCodeObfuscate;
		if (!from.mainColor.equals(to.mainColor))
			code += to.mainColor.toInlineCode();
		return code;
	}, new PlainMorpher(), '\\', new char[] {RichColor.characterCodePrefix}),
	MARKUP(new MarkupEncoder(), '\\', new char[] {'*', '_', '~', '~'}),
	PLAIN((style, prefix) ->
	{
		return "";
	},
	(from, to) ->
	{
		return "";
	}, new ObfuscationMorpher(), '\\', new char[0]);
	
	private interface Transition
	{
		String transition(Style from, Style to);
	}
	
	private interface Encode
	{
		String encode(Style style, boolean prefix);
	}
	
	private interface CharacterMorpher
	{
		char morph(Style style, char character);
	}
	
	private interface Encoder extends Encode, Transition, CharacterMorpher
	{
		
	}
	
	private Transition transition;
	private Encode encode;
	private CharacterMorpher morph;
	private char[] unsafeCharacters;
	private char escapeCharacter;
	
	private EncodingType(Encoder encoder, char escapeCharacter, char[] unsafeCharacters)
	{
		this(encoder, encoder, encoder, escapeCharacter, unsafeCharacters);
	}
	
	private EncodingType(Encode encode, Transition transition, CharacterMorpher morpher, char escapeCharacter, char[] unsafeCharacters)
	{
		this.encode = encode;
		this.transition = transition;
		this.escapeCharacter = escapeCharacter;
		this.unsafeCharacters = unsafeCharacters;
		this.morph = morpher;
	}
	
	public char escapeCharacter()
	{
		return escapeCharacter;
	}
	
	public boolean isSafe(char ch)
	{
		for (int index = 0; index < unsafeCharacters.length; index++)
			if (unsafeCharacters[index] == ch)
				return false;
		return true;
	}
	
	public char morphCharacter(Style style, char character)
	{
		return morph.morph(style, character);
	}
	
	public String transition(Style from, Style to)
	{
		return transition.transition(from, to);
	}
	
	public String encodingPrefix(Style style)
	{
		return encode.encode(style, true);
	}
	
	public String encodingSuffix(Style style)
	{
		return encode.encode(style, false);
	}
	
	public String encode(RichString string)
	{
		StringBuilder builder = new StringBuilder();
		Style lastPrefixedStyle = null;
		for (int index = 0; index < string.length(); index++)
		{
			RichCharacter ch = string.charAt(index);
			if (lastPrefixedStyle == null)
			{
				builder.append(encodingPrefix(ch.style));
				lastPrefixedStyle = ch.style;
			}
			else if (!lastPrefixedStyle.equals(ch.style))
			{
				builder.append(transition(lastPrefixedStyle, ch.style));
				lastPrefixedStyle = ch.style;
			}
			if (!isSafe(ch.character))
				builder.append(escapeCharacter());
			builder.append(ch.character);
		}
		if (lastPrefixedStyle != null)
			builder.append(encodingSuffix(lastPrefixedStyle));
		return builder.toString();
	}
	
	public String encode(RichCharacter character)
	{
		return encodingPrefix(character.style) + character.character + encodingSuffix(character.style);
	}
	
	private static class ObfuscationMorpher implements CharacterMorpher
	{
		Random random = new Random();
		
		@Override
		public char morph(Style style, char character)
		{
			if (style.isObfuscated())
				return (char)(random.nextInt(94) + 32);//generate a random number between 32 and 126 (inclusive)
			else
				return character;
		}
	}
	
	private static class PlainMorpher implements CharacterMorpher
	{
		@Override
		public char morph(Style style, char character)
		{
			return character;
		}
	}
	
	private static class MarkupEncoder extends ObfuscationMorpher implements Encoder
	{
		private enum MarkupStyle
		{
			PLAIN					("", ""),
			ITALIC					("*", "*"),
			BOLD					("**", "**"),
			BOLD_ITALIC				("***", "***"),
			UNDERLINE				("__", "__"),
			UNDERLINE_ITALIC		("__*", "*__"),
			UNDERLINE_BOLD			("__**", "**__"),
			UNDERLINE_BOLD_ITALIC	("__***", "***__"),
			STRIKETHROUGH			("~~", "~~"),
			HIGHLIGHT				("`", "`");
			
			String enter;
			String exit;
			
			MarkupStyle(String enter, String exit)
			{
				this.enter = enter;
				this.exit = exit;
			}
			
			static MarkupStyle getStyle(Style ch)
			{
				if (ch.isStrikeThrough()) return MarkupStyle.STRIKETHROUGH;
				else if (ch.isHighlighted()) return MarkupStyle.HIGHLIGHT;
				else if (ch.isUnderline() && ch.isItalic() && ch.isBold()) return MarkupStyle.UNDERLINE_BOLD_ITALIC;
				else if (!ch.isUnderline() && ch.isItalic() && ch.isBold()) return MarkupStyle.BOLD_ITALIC;
				else if (!ch.isUnderline() && !ch.isItalic() && ch.isBold()) return MarkupStyle.BOLD;
				else if (!ch.isUnderline() && ch.isItalic() && !ch.isBold()) return MarkupStyle.ITALIC;
				else if (ch.isUnderline() && !ch.isItalic() && ch.isBold()) return MarkupStyle.UNDERLINE_BOLD;
				else if (ch.isUnderline() && !ch.isItalic() && !ch.isBold()) return MarkupStyle.UNDERLINE;
				else if (ch.isUnderline() && ch.isItalic() && !ch.isBold()) return MarkupStyle.UNDERLINE_ITALIC;
				return MarkupStyle.PLAIN;
			}
		}
		
		@Override
		public String encode(Style style, boolean prefix)
		{
			MarkupStyle markup = MarkupStyle.getStyle(style);
			return prefix ? markup.enter : markup.exit;
		}
		
		@Override
		public String transition(Style from, Style to)
		{
			return encode(from, false) + encode(to, true);
		}
	}
}