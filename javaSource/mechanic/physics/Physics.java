package mechanic.physics;

import java.util.ArrayList;
import java.util.Iterator;

import globalResources.graphics.Canvas;
import globalResources.utilities.Logger;
import globalResources.utilities.Vector;
import mechanic.engine.Engine;
import mechanic.engine.GameWindow;

/**
 * Manages all the PhysicalBodies
 */
public class Physics
{
	static ArrayList<PhysicalBody> dynamicBodies;
	static ArrayList<PhysicalBody> staticBodies;
	static ArrayList<PhysicalBody> dormantBodies;
	static ArrayList<AlterationQueueEntry> alterationQueue;
	static boolean useQueue;
	static boolean doPhysicsDraw = false;
	
	static double friction = 1.1;
	static double weakForceFactor = 10;
	static double overStepTolerance = 3;
	static Vector worldDimensions = new Vector(-1, -1);
	static Vector gravity = new Vector(0, 0);
	static boolean applyGravity = false;
	
	public static void init()
	{
		Logger.defaultLogger.log("Initializing Physics engine");
		dynamicBodies = new ArrayList<PhysicalBody>();
		staticBodies = new ArrayList<PhysicalBody>();
		dormantBodies = new ArrayList<PhysicalBody>();
		alterationQueue = new ArrayList<AlterationQueueEntry>();
		useQueue = true;
	}
	
	public static void update()
	{
		useQueue = true;
		for (Iterator<PhysicalBody> iterator = dynamicBodies.iterator(); iterator.hasNext();)
		{
			PhysicalBody body = iterator.next();
			if (applyGravity && body.hasGravity) body.applyForce(gravity);
			body.update();
		}
		//useQueue = false;
		processQueue();
	}
	
	/**
	 * Performs debug drawing for all non-dormant PhysicalBodies
	 */
	public static void draw()
	{
		Canvas drawable = GameWindow.getCanvas();
		Vector cameraPosition = Engine.getCameraOffset();
		for (Iterator<PhysicalBody> iterator = dynamicBodies.iterator(); iterator.hasNext();)
		{
			iterator.next().bodyDraw(drawable, cameraPosition);
		}
		for (Iterator<PhysicalBody> iterator = staticBodies.iterator(); iterator.hasNext();)
		{
			iterator.next().bodyDraw(drawable, cameraPosition);
		}
		processQueue();
	}
	
	static void processQueue()
	{
		useQueue = false;
		for (Iterator<AlterationQueueEntry> iterator = alterationQueue.iterator(); iterator.hasNext();)
		{
			iterator.next().process();
		}
		alterationQueue.clear();
		useQueue = true;
	}
	
	/**
	 * Removes all physical bodies
	 */
	public static void clear()
	{
		for (Iterator<PhysicalBody> iterator = dynamicBodies.iterator(); iterator.hasNext();) iterator.next().remove();
		for (Iterator<PhysicalBody> iterator = staticBodies.iterator(); iterator.hasNext();) iterator.next().remove();
		for (Iterator<PhysicalBody> iterator = dormantBodies.iterator(); iterator.hasNext();) iterator.next().remove();
	}
	
	/**
	 * Checks if a given point is within the boundaries of the world
	 * @param vector the point to check
	 * @return if the point is in the world
	 */
	public static boolean inWorld(Vector vector)
	{
		double x = worldDimensions.getX();
		double y = worldDimensions.getY();
		if (x >= 0 && (vector.getX() < -x || vector.getX() > x)) return false;
		if (y >= 0 && (vector.getY() < -y || vector.getY() > y)) return false;
		return true;
	}
	
	/**
	 * Gets the current dimensions of the world
	 * @return current dimensions of the world
	 * @see #setWorldSize(Vector)
	 */
	public static Vector getWorldSize()
	{
		return new Vector(worldDimensions.getX() * 2, worldDimensions.getY() * 2);
	}
	
	/**
	 * Sets the current dimensions of the world
	 * <p>
	 * Set either dimension to a negative value to have no size restriction on that axis
	 * </p>
	 * @param dimensions desired world dimensions
	 */
	public static void setWorldSize(Vector dimensions)
	{
		setWorldSize(dimensions.getX(), dimensions.getY());
	}
	
	/**
	 * Sets the current dimensions of the world
	 * @param x size of the world on the x axis
	 * @param y size of the world on the y axis
	 * @see #setWorldSize(Vector)
	 */
	public static void setWorldSize(double x, double y)
	{
		worldDimensions.set(x / 2, y / 2);
	}
	
	public static void setGravityVector(Vector vector)
	{
		Physics.gravity = vector.clone();
	}
	
	public static Vector getGravityVector()
	{
		return gravity.clone();
	}
	
	public static void setApplyGravityVector(boolean applyGravityVector)
	{
		applyGravity = applyGravityVector;
	}
	
	public static boolean getApplyGravityVector()
	{
		return applyGravity;
	}
	
	/**
	 * Applies a weak point force to all dynamic bodies
	 * @param position the origin point
	 * @param force the strength of the point force
	 * @param radius the radius of the point force
	 */
	public static void weakPointForce(Vector position, double force, int radius)
	{
		force /= weakForceFactor;
		useQueue = true;
		radius *= radius;
		for (Iterator<PhysicalBody> iterator = dynamicBodies.iterator(); iterator.hasNext();)
		{
			iterator.next().applyWeakPointForce(position, force, radius);
		}
		//useQueue = false;
		processQueue();
	}
	
	/**
	 * Applies a point force to all dynamic bodies
	 * @param position position of the origin point
	 * @param force strength of the point force
	 * @param radius the radius of the point force
	 */
	public static void pointForce(Vector position, double force, int radius)
	{
		useQueue = true;
		radius *= radius;
		for (Iterator<PhysicalBody> iterator = dynamicBodies.iterator(); iterator.hasNext();)
		{
			iterator.next().applyPointForce(position, force, radius);
		}
		//useQueue = false;
		processQueue();
	}
	
	/**
	 * Gets the Physics engine's weak force factor
	 * @return the weak force factor
	 */
	public static double getWeakForceFactor()
	{
		return weakForceFactor;
	}
	
	/**
	 * Sets the Physics engine's weak force factor
	 * @param weakForceFactor the new weak force factor
	 * <p>
	 * A value of 0 will be converted to 1
	 * </p>
	 */
	public static void setWeakForceFactor(double weakForceFactor)
	{
		if (weakForceFactor == 0) weakForceFactor = 1;
		Physics.weakForceFactor = weakForceFactor;
	}
	
	/**
	 * Gets the Physics engine's friction factor
	 * @return friction factor
	 */
	public static double getFriction()
	{
		return friction;
	}
	
	/**
	 * Sets the Physics engine's friction factor
	 * @param friction the new friction factor
	 * <p>
	 * A value of 0 will be converted to 1
	 * </p>
	 */
	public static void setFriction(double friction)
	{
		if (friction == 0) friction = 1;
		Physics.friction = friction;
	}
	
	static abstract class AlterationQueueEntry
	{
		PhysicalBody body;
		
		AlterationQueueEntry(PhysicalBody body)
		{
			this.body = body;
			alterationQueue.add(this);
		}
		
		abstract void process();
	}
	
	static class SetDormant extends AlterationQueueEntry
	{
		boolean dormant;
		
		SetDormant(PhysicalBody body, boolean dormant)
		{
			super(body);
		}
		
		@Override
		void process()
		{
			body.setDormant(dormant);
		}
	}
	
	static class SetStatic extends AlterationQueueEntry
	{
		boolean staticBody;
		
		SetStatic(PhysicalBody body, boolean staticBody)
		{
			super(body);
			this.staticBody = staticBody;
		}
		
		@Override
		void process()
		{
			body.setStatic(staticBody);
		}
	}
	
	static class Remove extends AlterationQueueEntry
	{
		Remove(PhysicalBody body)
		{
			super(body);
		}
		
		@Override
		void process()
		{
			body.remove();
		}
	}
	
	/**
	 * Iterator for every non-dormant PhysicalBody
	 */
	public static class TotalIterator implements RelevantBodyIterator
	{
		int position;
		
		/**
		 * Creates a new TotalIterator
		 */
		public TotalIterator()
		{
			position = 0;
		}
		
		/**
		 * Gets the next body, or null if every body has been iterated over
		 * @return the next body, or null
		 */
		public PhysicalBody next()
		{
			PhysicalBody body = null;
			if (hasNext())
			{
				if (position < dynamicBodies.size())
				{
					body = dynamicBodies.get(position);
				}
				else
				{
					body = staticBodies.get(position - dynamicBodies.size());
				}
				position++;
			}
			return body;
		}
		
		/**
		 * Gets whether there are more bodies to iterate over
		 * @return if there are more bodies to iterate over
		 */
		public boolean hasNext()
		{
			return (position < dynamicBodies.size() + staticBodies.size());
		}
		
		@Override
		public int getBodyCount()
		{
			return dynamicBodies.size() + staticBodies.size();
		}
		
		@Override
		public PhysicalBody getBody(int index)
		{
			if (position < dynamicBodies.size())
			{
				return dynamicBodies.get(index);
			}
			else if (index < dynamicBodies.size() + staticBodies.size())
			{
				return staticBodies.get(index - dynamicBodies.size());
			}
			return null;
		}
		
		@Override
		public void jumpTo(int index)
		{
			position = index;
		}
		
		@Override
		public int getCurrentIndex()
		{
			return position;
		}
	}
}