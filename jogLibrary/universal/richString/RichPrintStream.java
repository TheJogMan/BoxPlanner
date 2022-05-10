package jogLibrary.universal.richString;

import java.io.OutputStream;
import java.io.PrintStream;
import java.io.UnsupportedEncodingException;
import java.util.Formatter;
import java.util.Locale;

import jogLibrary.universal.dataStructures.data.Value;

public class RichPrintStream extends PrintStream
{
	private Style lastPrefixed = null;
	private Style style = new Style();
	private final EncodingType encodingType;
	
	public RichPrintStream(OutputStream out, EncodingType type)
	{
		super(out, false);
		this.encodingType = type;
	}
	
	public RichPrintStream(OutputStream out, String encoding, EncodingType type) throws UnsupportedEncodingException
	{
		super(out, false, encoding);
		this.encodingType = type;
	}
	
	public void setStyle(Style style)
	{
		this.style = style.clone();
	}
	
	public Style getStyle()
	{
		return style.clone();
	}
	
	public void print(Value<?, ?> value)
	{
		print(value.toString());
	}
	
	public void println(Value<?, ?> value)
	{
		print(value);
		print('\n');
	}
	
	//Final output methods \/ \/ \/ \/ \/ \/ \/ \/ \/ \/ \/ \/
	
	private boolean settingPrefix = false;
	
	private void setPrefix(Style style)
	{
		if (!settingPrefix)
		{
			settingPrefix = true;
			if (lastPrefixed == null)
				super.print(encodingType.encodingPrefix(style));
			else if (!lastPrefixed.equals(style))
				super.print(encodingType.transition(lastPrefixed, style));
			lastPrefixed = style;
			settingPrefix = false;
		}
	}
	
	@Override
	public RichPrintStream append(char c)
	{
		setPrefix(style);
		if (c == '\n')
			flush();
		if (!encodingType.isSafe(c))
			super.print(encodingType.escapeCharacter());
		super.print(encodingType.morphCharacter(style, c));
		return this;
	}
	
	@Override
	public void write(int b)
	{
		setPrefix(style);
		super.write(b);
	}
	
	public RichPrintStream append(RichCharacter c)
	{
		setPrefix(c.style);
		if (!encodingType.isSafe(c.character))
			super.print(encodingType.escapeCharacter());
		super.print(encodingType.morphCharacter(style, c.character));
		return this;
	}
	
	@Override
	public void flush()
	{
		if (lastPrefixed != null)
			super.print(encodingType.encodingSuffix(lastPrefixed));
		lastPrefixed = null;
		super.flush();
	}
	
	public void print(RichString string)
	{
		for (int index = 0; index < string.length(); index++)
			print(string.charAt(index));
	}
	
	public void println(RichString string)
	{
		print(string);
		print('\n');
	}
	
	public void print(RichCharacter c)
	{
		append(c);
		flush();
	}
	
	public void println(RichCharacter c)
	{
		print(c);
		print('\n');
	}
	
	//Final output methods /\ /\ /\ /\ /\ /\ /\ /\ /\ /\ /\ /\
	
	public RichPrintStream append(CharSequence csq)
	{
		return append(csq, 0, csq.length());
	}
	
	public RichPrintStream append(CharSequence csq, int start, int end)
	{
		for (int index = start; index < end; index++)
		{
			append(csq.charAt(index));
		}
		return this;
	}
	
	public void print(boolean b)
	{
		print(Boolean.toString(b));
	}
	
	public void println(boolean b)
	{
		println(Boolean.toString(b));
	}
	
	public void print(char c)
	{
		append(c);
	}
	
	public void println(char c)
	{
		print(c);
		print('\n');
	}
	
	public void print(char[] c)
	{
		for (int index = 0; index < c.length; index++)
			print(c[index]);
	}
	
	public void println(char[] c)
	{
		for (int index = 0; index < c.length; index++)
			print(c[index]);
		print('\n');
	}
	
	public void print(double d)
	{
		print(Double.toString(d));
	}
	
	public void println(double d)
	{
		println(Double.toString(d));
	}
	
	public void print(float f)
	{
		print(Float.toString(f));
	}
	
	public void println(float f)
	{
		println(Float.toString(f));
	}
	
	public void print(int i)
	{
		print(Integer.toString(i));
	}
	
	public void println(int i)
	{
		println(Integer.toString(i));
	}
	
	public void print(long l)
	{
		print(Long.toString(l));
	}
	
	public void println(long l)
	{
		println(Long.toString(l));
	}
	
	public void print(Object obj)
	{
		print(String.valueOf(obj));
	}
	
	public void println(Object obj)
	{
		println(String.valueOf(obj));
	}
	
	public void print(String string)
	{
		if (string == null)
			string = "null";
		for (int index = 0; index < string.length(); index++)
			print(string.charAt(index));
		flush();
	}
	
	public void println(String string)
	{
		print(string + '\n');
	}
	
	public void println()
	{
		print('\n');
	}
	
	public void write(byte[] buf, int off, int len)
	{
		for (int index = off; index < off + len; index++)
			write(buf[index]);
	}
	
	public RichPrintStream format(Locale l, String format, Object... args)
	{
		Formatter formatter = new Formatter();
		formatter.format(l, format, args);
		print(formatter.toString());
		formatter.close();
		return this;
	}
	
	public RichPrintStream format(String format, Object... args)
	{
		Formatter formatter = new Formatter();
		formatter.format(format, args);
		print(formatter.toString());
		formatter.close();
		return this;
	}
	
	public RichPrintStream printf(Locale l, String format, Object... args)
	{
		Formatter formatter = new Formatter();
		formatter.format(l, format, args);
		print(formatter.toString());
		formatter.close();
		return this;
	}
	
	public RichPrintStream printf(String format, Object... args)
	{
		Formatter formatter = new Formatter();
		formatter.format(format, args);
		print(formatter.toString());
		formatter.close();
		return this;
	}
}