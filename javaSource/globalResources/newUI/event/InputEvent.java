package globalResources.newUI.event;

import globalResources.newUI.NUIComponent;
import globalResources.utilities.VectorInt;

public class InputEvent extends NUIEvent
{
	ComponentInput input;
	
	public InputEvent(ComponentInput input)
	{
		this.input = input;
	}
	
	public ComponentInput getInput()
	{
		return input;
	}
	
	@Override
	public void performAction()
	{
		
	}
	
	
	public static interface NUIInputListener
	{
		public default void keyPress(int key)
		{
			
		}
		
		public default void keyRelease(int key)
		{
			
		}
		
		public default void buttonPress(int button)
		{
			
		}
		
		public default void buttonRelease(int button)
		{
			
		}
		
		public default void curseorMove(VectorInt start, VectorInt stop)
		{
			
		}
		
		public default void cursorDrag(VectorInt start, VectorInt stop)
		{
			
		}
		
		public default void scroll(int amount)
		{
			
		}
		
		public default void textInput(int key, char chr)
		{
			
		}
		
		public default void textEdit(int key, char chr)
		{
			
		}
	}
	
	
	
	public static class CursorMove implements CursorInput
	{
		VectorInt start;
		VectorInt stop;
		
		public CursorMove(VectorInt start, VectorInt stop)
		{
			this.start = start.clone();
			this.stop = stop.clone();
		}
		
		@Override
		public VectorInt getPosition()
		{
			return stop.clone();
		}
		
		public VectorInt getStart()
		{
			return start.clone();
		}
		
		@Override
		public ComponentInput contextualize(NUIComponent component)
		{
			VectorInt newStart = start.clone();
			VectorInt newStop = stop.clone();
			newStart.subtract(component.getPosition());
			newStop.subtract(component.getPosition());
			return new CursorMove(newStart, newStop);
		}
		
		@Override
		public void processOnListener(NUIInputListener listener)
		{
			listener.curseorMove(start, stop);
		}
	}
	
	public static class CursorDrag extends CursorMove
	{
		public CursorDrag(VectorInt start, VectorInt stop)
		{
			super(start, stop);
		}
		
		@Override
		public ComponentInput contextualize(NUIComponent component)
		{
			VectorInt newStart = start.clone();
			VectorInt newStop = stop.clone();
			newStart.subtract(component.getPosition());
			newStop.subtract(component.getPosition());
			return new CursorDrag(newStart, newStop);
		}
		
		@Override
		public void processOnListener(NUIInputListener listener)
		{
			listener.cursorDrag(start, stop);
		}
	}
	
	public static abstract class AbstractBinaryInput implements BinaryInput
	{
		public enum Type
		{
			KEY, BUTTON
		}
		
		int input;
		boolean state;
		Type type;
		
		public AbstractBinaryInput(int input, boolean state, Type type)
		{
			this.input = input;
			this.state = state;
			this.type = type;
		}
		
		public boolean getState()
		{
			return state;
		}
		
		public int getInput()
		{
			return input;
		}
		
		public Type getType()
		{
			return type;
		}
	}
	
	public static class TextInput extends AbstractBinaryInput
	{
		char chr;
		
		public TextInput(int input, char chr)
		{
			super(input, true, Type.KEY);
			this.chr = chr;
		}
		
		@Override
		public ComponentInput contextualize(NUIComponent component)
		{
			return new TextInput(getInput(), chr);
		}
		
		@Override
		public void processOnListener(NUIInputListener listener)
		{
			listener.textInput(getInput(), chr);
		}
	}
	
	public static class TextEdit extends AbstractBinaryInput
	{
		char chr;
		
		public TextEdit(int input, char chr)
		{
			super(input, true, Type.KEY);
			this.chr = chr;
		}
		
		@Override
		public ComponentInput contextualize(NUIComponent component)
		{
			return new TextEdit(getInput(), chr);
		}
		
		@Override
		public void processOnListener(NUIInputListener listener)
		{
			listener.textEdit(getInput(), chr);
		}
	}
	
	public static class KeyPress extends AbstractBinaryInput
	{
		public KeyPress(int input)
		{
			super(input, true, Type.KEY);
		}
		
		@Override
		public ComponentInput contextualize(NUIComponent component)
		{
			return new KeyPress(getInput());
		}
		
		@Override
		public void processOnListener(NUIInputListener listener)
		{
			listener.keyPress(getInput());
		}
	}
	
	public static class KeyRelease extends AbstractBinaryInput
	{
		public KeyRelease(int input)
		{
			super(input, false, Type.KEY);
		}
		
		@Override
		public ComponentInput contextualize(NUIComponent component)
		{
			return new KeyRelease(getInput());
		}
		
		@Override
		public void processOnListener(NUIInputListener listener)
		{
			listener.keyRelease(getInput());
		}
	}
	
	public static class ButtonPress extends AbstractBinaryInput implements CursorInput
	{
		VectorInt position;
		
		public ButtonPress(int input, VectorInt position)
		{
			super(input, true, Type.BUTTON);
			this.position = position.clone();
		}
		
		@Override
		public ComponentInput contextualize(NUIComponent component)
		{
			VectorInt newPosition = position.clone();
			newPosition.subtract(component.getPosition());
			return new ButtonPress(getInput(), newPosition);
		}
		
		@Override
		public VectorInt getPosition()
		{
			return position.clone();
		}
		
		@Override
		public void processOnListener(NUIInputListener listener)
		{
			listener.buttonPress(getInput());
		}
	}
	
	public static class ButtonRelease extends AbstractBinaryInput implements CursorInput
	{
		VectorInt position;
		
		public ButtonRelease(int input, VectorInt position)
		{
			super(input, false, Type.BUTTON);
			this.position = position.clone();
		}
		
		@Override
		public ComponentInput contextualize(NUIComponent component)
		{
			VectorInt newPosition = position.clone();
			newPosition.subtract(component.getPosition());
			return new ButtonRelease(getInput(), newPosition);
		}
		
		@Override
		public VectorInt getPosition()
		{
			return position.clone();
		}
		
		@Override
		public void processOnListener(NUIInputListener listener)
		{
			listener.buttonRelease(getInput());
		}
	}
	
	public static class Scroll implements IntegerInput, CursorInput
	{
		int amount;
		VectorInt position;
		
		public Scroll(int amount, VectorInt position)
		{
			this.amount = amount;
			this.position = position.clone();
		}
		
		@Override
		public ComponentInput contextualize(NUIComponent component)
		{
			VectorInt newPosition = position.clone();
			newPosition.subtract(component.getPosition());
			return new Scroll(amount, newPosition);
		}
		
		@Override
		public int getValue()
		{
			return amount;
		}
		
		@Override
		public VectorInt getPosition()
		{
			return position.clone();
		}
		
		@Override
		public void processOnListener(NUIInputListener listener)
		{
			listener.scroll(getValue());
		}
	}
	
	
	
	
	
	
	public static interface ComponentInput
	{
		ComponentInput contextualize(NUIComponent component);
		void processOnListener(NUIInputListener listener);
	}
	
	public static interface BinaryInput extends ComponentInput
	{
		public boolean getState();
	}
	
	public static interface CursorInput extends ComponentInput
	{
		public VectorInt getPosition();
	}
	
	public static interface KeyInput extends BinaryInput
	{
		public int getKey();
	}
	
	public static interface ButtonInput extends BinaryInput
	{
		public int getButton();
	}
	
	public static interface IntegerInput extends ComponentInput
	{
		public int getValue();
	}
}