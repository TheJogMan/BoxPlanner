package mechanic.entities;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.UUID;

import globalResources.graphics.Canvas;
import globalResources.utilities.SecureList;
import globalResources.utilities.Vector;
import mechanic.entities.EntityManager.Remove;
import mechanic.physics.PhysicalBody;
import mechanic.physics.Physics;

/**
 * Abstract entity
 */
public abstract class Entity
{
	PhysicalBody body;
	Vector facing;
	ArrayList<AI> aiModules;
	ArrayList<AIAlterationQueueEntry> aiAlterationQueue;
	UUID id;
	boolean useAIQueue;
	
	/**
	 * Updates this entity
	 */
	public abstract void update();
	
	/**
	 * Draws this entity
	 * @param canvas the drawable the entity will be drawn on
	 * @param x the x coordinate where the entity should be drawn
	 * @param y the y coordinate where the entity should be drawn
	 */
	public abstract void draw(Canvas canvas, int x, int y);
	
	/**
	 * Creates a new instance of this entity
	 * @param x the x position of this entity
	 * @param y the y position of this entity
	 * @param width the width of this entity
	 * @param height the height of this entity
	 * @param mass the mass of this entity
	 */
	public Entity(int x, int y, int width, int height, double mass)
	{
		body = new PhysicalBody(x, y, width, height, mass);
		facing = new Vector(0, -1);
		id = UUID.randomUUID();
		EntityManager.entities.put(id, this);
		aiModules = new ArrayList<AI>();
		aiAlterationQueue = new ArrayList<AIAlterationQueueEntry>();
		useAIQueue = false;
	}
	
	/**
	 * Gets this entities ID
	 * @return entity ID
	 */
	public UUID getID()
	{
		return id;
	}
	
	void entityUpdate()
	{
		useAIQueue = true;
		for (Iterator<AI> iterator = aiModules.iterator(); iterator.hasNext();)
		{
			iterator.next().update();
		}
		useAIQueue = false;
		processAIQueue();
		update();
	}
	
	void entityDraw(Canvas drawable, Vector cameraPosition)
	{
		int x = (int)(cameraPosition.getX() + body.getPosition().getX() - body.getWidth() / 2);
		int y = (int)(cameraPosition.getY() + body.getPosition().getY() - body.getHeight() / 2);
		draw(drawable, x, y);
		for (Iterator<AI> iterator = aiModules.iterator(); iterator.hasNext();)
		{
			iterator.next().draw(drawable, x, y);
		}
	}
	
	/**
	 * Gets the distance to another entity
	 * @param entity the entity to measure to
	 * @return the distance
	 */
	public double getDistance(Entity entity)
	{
		return body.getDistance(entity.body);
	}
	
	/**
	 * Gets a list of this entities AI modules
	 * @return SecureList of AI modules
	 */
	public SecureList<AI> getAIModules()
	{
		return new SecureList<AI>(aiModules);
	}
	
	/**
	 * Gets the PhysicalBody of this entity
	 * @return this entities physical body
	 */
	public PhysicalBody getBody()
	{
		return body;
	}
	
	/**
	 * Faces the entity towards the given location
	 * @param x the x coordinate
	 * @param y the y coordinate
	 * @see #lookTo(Vector)
	 */
	public void lookTo(int x, int y)
	{
		lookTo(new Vector(x, y));
	}
	
	/**
	 * Faces the entity towards the given location
	 * @param location the location to look towards
	 */
	public void lookTo(Vector location)
	{
		facing.set(-1, 0);
		facing.rotate(body.getPosition().getCoordinateAngle(location));
	}
	
	/**
	 * Gets the direction this entity is looking in
	 * @return the entities facing vector
	 */
	public Vector getFacing()
	{
		return facing.clone();
	}
	
	/**
	 * Sets the direction this entity is looking in
	 * @param facing the new facing vector
	 */
	public void setFacing(Vector facing)
	{
		this.facing = facing.clone();
	}
	
	/**
	 * Moves the entity relative to its facing vector
	 * @param direction direction to move in
	 * @param amount distance to move
	 */
	public void move(Direction direction, double amount)
	{
		Vector movementDirection = getVector(direction);
		movementDirection.multiply(amount);
		body.move(movementDirection);
	}
	
	/**
	 * Converts a relative direction to a precise vector using this entities facing vector
	 * @param direction relative direction
	 * @return precise vector
	 */
	public Vector getVector(Direction direction)
	{
		Vector vector = direction.getDirection();
		if (direction.isRelative())
		{
			vector = facing.clone();
			vector.rotate(direction.getRotation());
		}
		return vector;
	}
	
	/**
	 * Applies a force on this entity in a relative direction
	 * @param direction relative direction
	 * @param amount force strength
	 */
	public void applyForce(Direction direction, double amount)
	{
		Vector movementDirection = getVector(direction);
		movementDirection.multiply(amount);
		body.applyForce(movementDirection);
	}
	
	/**
	 * Applies a weak force on this entity in a relative direction
	 * @param direction relative direction
	 * @param amount force strength
	 * <p>
	 * Strength is divided by the Physics engine's weak force factor, to be more applicable to a constant force
	 * </p>
	 * @see #applyForce(Direction, double)
	 */
	public void applyWeakForce(Direction direction, double amount)
	{
		applyForce(direction, amount / Physics.getWeakForceFactor());
	}
	
	/**
	 * Checks if this entity still exists
	 * @return if this entity still exists
	 */
	public boolean exists()
	{
		return EntityManager.entities.containsKey(id);
	}
	
	/**
	 * Removes this entity
	 */
	public void remove()
	{
		remove(true);
	}
	
	void remove(boolean useQueue)
	{
		if (useQueue)
		{
			new Remove(this);
		}
		else
		{
			EntityManager.entities.remove(id);
		}
	}
	
	void processAIQueue()
	{
		for (Iterator<AIAlterationQueueEntry> iterator = aiAlterationQueue.iterator(); iterator.hasNext();)
		{
			iterator.next().process();
		}
		aiAlterationQueue.clear();
	}
	
	abstract class AIAlterationQueueEntry
	{
		AI module;
		
		AIAlterationQueueEntry(AI module)
		{
			this.module = module;
			aiAlterationQueue.add(this);
		}
		
		abstract void process();
	}
	
	class AIRemove extends AIAlterationQueueEntry
	{
		AIRemove(AI module)
		{
			super(module);
		}
		
		@Override
		void process()
		{
			module.remove();
		}
	}
}