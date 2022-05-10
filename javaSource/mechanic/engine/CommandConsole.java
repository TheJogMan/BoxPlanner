package mechanic.engine;

import java.awt.Color;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.util.ArrayList;
import java.util.Iterator;

import globalResources.commander.AbstractCommandConsole;
import globalResources.graphics.Canvas;
import globalResources.richText.RichString;
import globalResources.ui.FrameInput.FrameInputListenerInterface;
import globalResources.ui.uiComponents.TextField;
import globalResources.utilities.Logger;
import globalResources.utilities.Logger.MessageType;
import globalResources.utilities.Logger.Priority;
import globalResources.utilities.VectorInt;
import globalResources.window.Window;
import globalResources.window.Window.WindowEventListener;
import globalResources.window.WindowManager;

public class CommandConsole extends AbstractCommandConsole
{
	static ArrayList<String> commandQueue = new ArrayList<String>();
	
	CommandConsole()
	{
		super("Mechanic Engine Console", "Help", '\n', Logger.defaultLogger, "Base Command Console.");
		
		Engine.addSideModule(new CoreModule());
	}
	
	public boolean hasAWindow()
	{
		for (Iterator<SideModule> iterator = Engine.moduleIterator(); iterator.hasNext();) if (iterator.next().getClass().equals(Console.class)) return true;
		return false;
	}
	
	public Window createWindow()
	{
		Console console = new Console(this);
		Engine.addSideModule(console);
		return console.window;
	}
	
	class CoreModule extends SimpleSideModule
	{
		CoreModule()
		{
			super(true);
		}
		
		@Override
		protected void update()
		{
			ArrayList<String> executedCommands = new ArrayList<String>();
			int size = commandQueue.size();
			for (int queueIndex = 0; queueIndex < size; queueIndex++)
			{
				String command = commandQueue.get(queueIndex);
				executedCommands.add(command);
				getLogger().log("Command entered: " + command);
				runCommand(command);
			}
			for (Iterator<String> iterator = executedCommands.iterator(); iterator.hasNext();) commandQueue.remove(iterator.next());
		}
		
		@Override
		protected void preRender()
		{
			
		}
		
		@Override
		protected void unload()
		{
			
		}
	}
	
	class Console extends SimpleSideModule
	{
		Window window;
		TextField input;
		int scroll = 0;
		ArrayList<String> completions;
		CommandConsole console;
		
		Console(CommandConsole console)
		{
			super(true);
			Engine.addSideModule(this);
			window = WindowManager.createWindow(800, 600, "Mechanic Engine Console");
			window.addListener(new WindowListener(this));
			window.getInput().registerInputListener(new InputListener());
			int height = window.getCanvas().getFontHeightR();
			input = new TextField(window, new VectorInt(0, window.getHeight() - height), new VectorInt(window.getWidth(), height));
			input.setPermSelected(true);
			completions = new ArrayList<String>();
			
			input.addTextChangeListener(() -> 
			{
				String message = input.getText();
				completions.clear();
				if (message.length() > 0)
				{
					completions = console.getCompletions(message, true, true);
				}
			});
		}
		
		@Override
		protected void update()
		{
			
		}
		
		@Override
		protected void preRender()
		{
			Canvas canvas = window.getCanvas();
			int y = input.getPosition().getY() + scroll;
			for (Iterator<Message> iterator = Engine.history.reverseIterator(); iterator.hasNext();)
			{
				Message message = iterator.next();
				ArrayList<RichString> lines = Engine.makeLines(message.message, message.type, message.loggerName, message.timeStamp, message.priority);
				y -= canvas.getFontHeight() * lines.size();
				int dy = y;
				for (Iterator<RichString> lineIterator = lines.iterator(); lineIterator.hasNext();)
				{
					RichString string = lineIterator.next();
					canvas.drawText(string, 0, dy);
					dy += string.getHeight();
				}
			}
		}
		
		@Override
		public void postRender()
		{
			Canvas canvas = window.getCanvas();
			if (completions.size() > 0)
			{
				int y = input.getPosition().getY() - 10 - canvas.getFontHeightR() * completions.size();
				int width = 0;
				for (Iterator<String> iterator = completions.iterator(); iterator.hasNext();)
				{
					int wid = canvas.getStringLengthR(iterator.next());
					if (wid > width) width = wid;
				}
				canvas.drawRect(input.getPosition().getX() + 5, y - 5, width + 10, 10 + canvas.getFontHeightR() * completions.size(), Color.WHITE);
				int x = input.getPosition().getX() + 10;
				for (Iterator<String> iterator = completions.iterator(); iterator.hasNext();)
				{
					String str = iterator.next();
					canvas.drawTextR(str, x, y, Color.BLACK);
					y += canvas.getFontHeightR();
				}
			}
		}
		
		@Override
		public void unload()
		{
			window.close();
		}
		
		public class InputListener implements FrameInputListenerInterface
		{
			@Override
			public void buttonPressEvent(MouseEvent event)
			{
				
			}
			
			@Override
			public void buttonReleaseEvent(MouseEvent event)
			{
				
			}
			
			@Override
			public void keyPressEvent(KeyEvent event)
			{
				
				if (event.getKeyCode() == KeyEvent.VK_ENTER)
				{
					commandQueue.add(input.getText());
					input.setText("");
					completions.clear();
				}
			}
			
			@Override
			public void keyReleaseEvent(KeyEvent event)
			{
				
			}
			
			@Override
			public void textEditEvent(KeyEvent event)
			{
				
			}
			
			@Override
			public void textInputEvent(KeyEvent event)
			{
				
			}
			
			@Override
			public void mouseScrollEvent(MouseWheelEvent event)
			{
				
			}
			
			@Override
			public void mouseMoveEvent(VectorInt delta, VectorInt newPosition)
			{
				
			}
			
			@Override
			public void mouseDragEvent(VectorInt delta, VectorInt newPosition)
			{
				
			}
		}
		
		class WindowListener extends WindowEventListener
		{
			Console console;
			
			WindowListener(Console console)
			{
				this.console = console;
			}
			
			@Override
			public void windowResize()
			{
				input.setPosition(new VectorInt(0, window.getHeight() - input.getDimensions().getY()));
				input.setDimensions(new VectorInt(window.getWidth(), input.getDimensions().getY()));
			}
			
			@Override
			public void windowMove()
			{
				
			}
			
			@Override
			public void windowClose()
			{
				Engine.removeSideModule(console);
			}
		}
	}
	
	static class Message
	{
		RichString message;
		RichString loggerName;
		MessageType type;
		Priority priority;
		long time;
		String timeStamp;
		
		Message(RichString message, RichString loggerName, MessageType type, long time, Priority priority, String timeStamp)
		{
			this.message = message;
			this.loggerName = loggerName;
			this.type = type;
			this.time = time;
			this.priority = priority;
			this.timeStamp = timeStamp;
		}
	}
}