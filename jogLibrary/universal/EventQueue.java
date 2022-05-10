package jogLibrary.universal;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

import jogLibrary.universal.dataStructures.stack.IndexableFIFOStack;

public class EventQueue
{
	private UUID key;
	private IndexableFIFOStack<Event> eventQueue = new IndexableFIFOStack<>();
	private HashMap<Order, ArrayList<Handler>> handlers = new HashMap<>();
	private HashMap<Type, HashMap<Order, ArrayList<Handler>>> cachedHandlers = new HashMap<>();
	
	public EventQueue(UUID key)
	{
		this.key = key;
		Order[] positions = Order.values();
		for (int index = 0; index < positions.length; index++)
		{
			handlers.put(positions[index], new ArrayList<>());
		}
	}
	
	public void process(UUID key)
	{
		if (key.equals(this.key))
		{
			synchronized (handlers)
			{
				while (eventQueue.hasNext())
				{
					asyncTrigger(eventQueue.pop());
				}
			}
		}
	}
	
	public void trigger(Event event)
	{
		eventQueue.push(event);
	}
	
	public void asyncTrigger(Event event)
	{
		synchronized (handlers)
		{
			if (event instanceof ListenerEvent)
				((ListenerEvent)event).process();
			else
			{
				//if this is a high frequency event, then we should check for cached handler lists before trying to compile a list from scratch
				HashMap<Order, ArrayList<Handler>> handlerMap;
				if (event instanceof HighFrequency)
				{
					Type type = event.getClass().getGenericSuperclass();
					handlerMap = cachedHandlers.get(type);
					//if no cached list was found, then we compile it from scratch and cache it for later
					if (handlerMap == null)
					{
						handlerMap = getHandlers(event);
						cachedHandlers.put(type, handlerMap);
					}
				}
				else
					handlerMap = getHandlers(event);
				
				Order[] orders = Order.values();
				for (int order = 0; order < orders.length; order++)
				{
					ArrayList<Handler> handlers = handlerMap.get(orders[order]);
					if (handlers != null)
						handlers.forEach(handler -> handler.handleEvent(event));
				}
			}
		}
	}
	
	public void addListener(Object listener)
	{
		new AddListener(listener);
	}
	
	public void removeListener(Object listener)
	{
		new RemoveListener(listener);
	}
	
	public interface HighFrequency
	{
		
	}
	
	public static class Event
	{
		
	}
	
	public static class CancellableEvent extends Event
	{
		private boolean cancelled = false;
		
		public final boolean cancelled()
		{
			return cancelled;
		}
		
		public final void cancel()
		{
			cancelled = true;
		}
		
		protected final void setCancelled(boolean cancelled)
		{
			this.cancelled = cancelled;
		}
	}
	
	public static enum Order
	{
		FIRST, SECOND, LAST;
	}
	
	@Documented
	@Retention(RetentionPolicy.RUNTIME)
	@Target(ElementType.METHOD)
	public static @interface EventHandler
	{
		Order order() default Order.SECOND;
		boolean highFrequency() default false;
	}
	
	private abstract class ListenerEvent extends Event
	{
		Object listener;
		
		ListenerEvent(Object listener)
		{
			this.listener = listener;
			asyncTrigger(this);
		}
		
		abstract void process();
	}
	
	private class AddListener extends ListenerEvent
	{
		AddListener(Object listener)
		{
			super(listener);
		}
		
		@Override
		void process()
		{
			Method[] methods = listener.getClass().getMethods();
			for (int method = 0; method < methods.length; method++)
			{
				Handler handler = Handler.makeHandler(listener, methods[method]);
				if (handler != null)
				{
					handlers.get(handler.order).add(handler);
					if (handler.highFrequencyEvent)
					{
						Type type = handler.method.getGenericParameterTypes()[0];
						HashMap<Order, ArrayList<Handler>> handlers = cachedHandlers.get(type);
						if (handlers == null)
						{
							handlers = new HashMap<>();
							Order[] positions = Order.values();
							for (int index = 0; index < positions.length; index++)
							{
								handlers.put(positions[index], new ArrayList<>());
							}
							cachedHandlers.put(type, handlers);
						}
						handlers.get(handler.order).add(handler);
					}
				}
			}
		}
	}
	
	private class RemoveListener extends ListenerEvent
	{
		RemoveListener(Object listener)
		{
			super(listener);
		}
		
		@Override
		void process()
		{
			Method[] methods = listener.getClass().getMethods();
			for (int index = 0; index < methods.length; index++)
			{
				Handler handler = Handler.makeHandler(listener, methods[index]);
				if (handler != null)
				{
					handlers.get(handler.order).remove(handler);
					if (handler.highFrequencyEvent)
					{
						Type type = handler.method.getGenericParameterTypes()[0];
						cachedHandlers.get(type).get(handler.order).remove(handler);
					}
				}
			}
		}
	}
	
	private HashMap<Order, ArrayList<Handler>> getHandlers(Event event)
	{
		HashMap<Order, ArrayList<Handler>> handlers = new HashMap<>();
		handlers.entrySet().forEach(entry ->
		{
			Order order = entry.getKey();
			
			entry.getValue().forEach(handler ->
			{
				if (handler.applicable(event))
					handlers.get(order).add(handler);
			});
		});
		return handlers;
	}
	
	private static class Handler
	{
		Method method;
		Order order;
		Object listener;
		boolean highFrequency;
		boolean highFrequencyEvent;
		
		Handler(Method method, Object listener, boolean highFrequency, Order order, boolean highFrequencyEvent)
		{
			this.method = method;
			this.listener = listener;
			this.highFrequency = highFrequency;
			this.order = order;
			this.highFrequencyEvent = highFrequencyEvent;
		}
		
		private void handleEvent(Event event)
		{
			try
			{
				method.invoke(listener, event);
			}
			catch (Exception e)
			{
				System.err.println("Error occurred while handling event: " + event.getClass());
				e.printStackTrace(System.err);
			}
		}
		
		private boolean applicable(Event event)
		{
			if (highFrequency && !(event instanceof HighFrequency))
				return false;
			return method.getGenericParameterTypes()[0].getClass().isInstance(event);
		}
		
		private static Handler makeHandler(Object listener, Method method)
		{
			Type[] parameters = method.getGenericParameterTypes();
			EventHandler[] annotations = method.getAnnotationsByType(EventHandler.class);
			if (annotations.length == 1 && parameters.length == 1 && Event.class.isAssignableFrom(parameters[0].getClass()))
			{
				return new Handler(method, listener, annotations[0].highFrequency(), annotations[0].order(), HighFrequency.class.isAssignableFrom(parameters[0].getClass()));
			}
			return null;
		}
	}
}