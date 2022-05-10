package mechanic.entities;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.UUID;

import globalResources.graphics.Canvas;
import globalResources.utilities.Logger;
import globalResources.utilities.SecureList;
import globalResources.utilities.Vector;
import mechanic.engine.Engine;
import mechanic.engine.GameWindow;

/**
 * Manages all the entities
 */
public class EntityManager
{
	static HashMap<UUID, Entity> entities;
	static ArrayList<AlterationQueueEntry> alterationQueue;
	static boolean useQueue;
	
	public static void init()
	{
		Logger.defaultLogger.log("Initializing Entity Manager");
		entities = new HashMap<UUID, Entity>();
		alterationQueue = new ArrayList<AlterationQueueEntry>();
		useQueue = true;
	}
	
	/**
	 * Performs an update on all entities
	 */
	public static void update()
	{
		for (Iterator<Entity> iterator = entities.values().iterator(); iterator.hasNext();)
		{
			iterator.next().entityUpdate();
		}
		int count = alterationQueue.size();
		ArrayList<AlterationQueueEntry> processedEntries = new ArrayList<AlterationQueueEntry>();
		for (int index = 0; index < count; index++)
		{
			AlterationQueueEntry entry = alterationQueue.get(index);
			entry.process();
			processedEntries.add(entry);
		}
		for (Iterator<AlterationQueueEntry> iterator = processedEntries.iterator(); iterator.hasNext();) alterationQueue.remove(iterator.next());
	}
	
	/**
	 * Draws all entities
	 */
	public static void draw()
	{
		Canvas drawable = GameWindow.getCanvas();
		Vector cameraPosition = Engine.getCameraOffset();
		useQueue = false;
		for (Iterator<Entity> iterator = entities.values().iterator(); iterator.hasNext();)
		{
			iterator.next().entityDraw(drawable, cameraPosition);
		}
		useQueue = true;
		for (Iterator<AlterationQueueEntry> iterator = alterationQueue.iterator(); iterator.hasNext();)
		{
			iterator.next().process();
		}
		alterationQueue.clear();
	}
	
	public static Entity getEntity(UUID id)
	{
		return entities.get(id);
	}
	
	public static Entity getNearestEntity(Vector point)
	{
		return getNearestEntityInRange(point, -1);
	}
	
	public static Entity getNearestEntityInRange(Vector point, double range)
	{
		return getNearestEntityInRangeFromList(point, entities.values(), range);
	}
	
	public static Entity getNearestEntityFromList(Vector point, Collection<Entity> list)
	{
		return getNearestEntityInRangeFromList(point, list, -1);
	}
	
	public static Entity getNearestEntityInRangeFromList(Vector point, Collection<Entity> list, double range)
	{
		Entity nearest = null;
		double nearestDistance = Double.MAX_VALUE;
		for (Iterator<Entity> iterator = list.iterator(); iterator.hasNext();)
		{
			Entity entity = iterator.next();
			double distance = entity.getBody().getPosition().getDistance(point);
			if (distance < nearestDistance && (range < 0 || distance <= range))
			{
				nearest = entity;
				nearestDistance = distance;
			}
		}
		return nearest;
	}
	
	/**
	 * Gets a list of all the entities of a certain type
	 * @param type the type of entities to collect
	 * @return the list of all entities of the given type
	 */
	public static ArrayList<Entity> getEntitiesOfType(Class<? extends Entity> type)
	{
		ArrayList<Entity> ents = new ArrayList<Entity>();
		for (Iterator<Entity> iterator = entities.values().iterator(); iterator.hasNext();)
		{
			Entity entity = iterator.next();
			if (type.isInstance(entity)) ents.add(entity);
		}
		return ents;
	}
	
	/**
	 * Returns a SecureList containing all entities
	 * @return list of all entities
	 */
	public static SecureList<Entity> getEntities()
	{
		return new SecureList<Entity>(entities.values());
	}
	
	/**
	 * Removes all entities
	 */
	public static void clear()
	{
		for (Iterator<Entity> iterator = entities.values().iterator(); iterator.hasNext();) iterator.next().remove();
	}
	
	static abstract class AlterationQueueEntry
	{
		Entity entity;
		
		AlterationQueueEntry(Entity entity)
		{
			this.entity = entity;
			alterationQueue.add(this);
		}
		
		abstract void process();
	}
	
	static class Remove extends AlterationQueueEntry
	{
		Remove(Entity entity)
		{
			super(entity);
		}
		
		@Override
		void process()
		{
			entity.remove(false);
		}
	}
}