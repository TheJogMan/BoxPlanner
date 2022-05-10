package mechanic.physics;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Iterator;

import globalResources.graphics.Canvas;
import globalResources.utilities.SecureList;
import globalResources.utilities.Vector;
import mechanic.physics.Physics.Remove;
import mechanic.physics.Physics.SetDormant;
import mechanic.physics.Physics.SetStatic;
import mechanic.physics.Physics.TotalIterator;

/**
 * PhysicalBody, used by the Physics engine to calculate movement and collisions for various game objects
 * <p>
 * A PhysicalBody has 4 vectors that get combined into 2 delta vectors for calculating movement.
 * The normal delta vector gets segmented for the purpose of collision checking, while the forced delta vector will have no collision checks performed
 * </p>
 * <p>
 * A PhysicalBody also has a set of tags, which can be used to track certain properties of a particular body
 * </p>
 */
public class PhysicalBody
{
	static int collisionCheckFactor = 0;
	
	Vector position;
	Vector movement;
	Vector velocity;
	Vector forcedVelocity;
	Vector forcedMovement;
	int width;
	int height;
	double mass;
	boolean staticBody;
	boolean dormant;
	boolean visualized;
	boolean inUpdate;
	boolean hasGravity;
	boolean hasFriction;
	boolean removed = false;
	ArrayList<Collision> collisions;
	ArrayList<String> tags;
	RelevantBodyIterator relevantBodyIterator;
	
	double angleToTopLeft;
	double angleToTopRight;
	double angleToBottomLeft;
	double angleToBottomRight;
	
	/**
	 * Creates a new PhysicalBody
	 * @param x X position of the new body
	 * @param y Y position of the new body
	 * @param width Width of the new body
	 * @param height Height of the new body
	 * @param mass Mass of the new body
	 */
	public PhysicalBody(double x, double y, int width, int height, double mass)
	{
		position = new Vector(x, y);
		movement = new Vector(0, 0);
		velocity = new Vector(0, 0);
		forcedVelocity = new Vector(0, 0);
		forcedMovement = new Vector(0, 0);
		setDimensions(width, height);
		this.mass = mass;
		staticBody = false;
		dormant = false;
		visualized = false;
		hasGravity = true;
		hasFriction = true;
		inUpdate = false;
		collisions = new ArrayList<Collision>();
		tags = new ArrayList<String>();
		relevantBodyIterator = new TotalIterator();
		Physics.dynamicBodies.add(this);
	}
	
	public RelevantBodyIterator getRelevantBodyIterator()
	{
		return relevantBodyIterator;
	}
	
	public void setRelevantBodyIterator(RelevantBodyIterator relevantBodyIterator)
	{
		this.relevantBodyIterator = relevantBodyIterator;
	}
	
	public boolean hasGravity()
	{
		return hasGravity;
	}
	
	public void setGravity(boolean gravity)
	{
		this.hasGravity = gravity;
	}
	
	public boolean hasFriction()
	{
		return hasFriction;
	}
	
	public void setFriction(boolean friction)
	{
		this.hasFriction = friction;
	}
	
	void update()
	{
		if (!inUpdate)
		{
			inUpdate = true;
			for (Iterator<PhysicalBody> iterator = Physics.dynamicBodies.iterator(); iterator.hasNext();)
			{
				PhysicalBody otherBody = iterator.next();
				if (!otherBody.equals(this))
				{
					BoundCheckResult result = checkBounds(otherBody);
					if (result.collision() && result.getOverStep() > Physics.overStepTolerance)
					{
						Vector vector = getVectorTo(otherBody);
						otherBody.applyForce(vector, true);
					}
				}
			}
			Vector delta = movement.clone();
			delta.add(velocity);
			if (collisionCheckFactor < 0)
			{
				position.add(delta);
				constrain();
			}
			else
			{
				int factor = collisionCheckFactor;
				if (collisionCheckFactor == 0) factor = (int)delta.getLength();
				if (factor < 1) factor = 1;
				Vector step = delta.clone();
				step.divide(factor);
				collisions.clear();
				for (int index = 0; index < factor;)
				{
					boolean canStep = true;
					Vector checkPosition = position.clone();
					checkPosition.add(step);
					
					relevantBodyIterator.reset();
					while (relevantBodyIterator.hasNext())
					{
						PhysicalBody otherBody = relevantBodyIterator.next();
						
						if (!otherBody.equals(this))
						{
							BoundCheckResult result = otherBody.checkBounds(checkPosition, width, height);
							boolean bumped = false;
							if (result.collision())
							{
								collisions.add(new Collision(result, this));
								if (!otherBody.staticBody)
								{
									Vector push = new Vector(0, 0);
									if ((result.bottomCollision() && step.getY() < 0) || (result.topCollision() && step.getY() > 0)) push.setY(step.getY());
									if ((result.rightCollision() && step.getX() < 0) || (result.leftCollision() && step.getX() > 0)) push.setX(step.getX());
									Vector previousVelocity = otherBody.velocity.clone();
									Vector previousMovement = otherBody.movement.clone();
									if (otherBody.mass > mass) push.multiply(1 / otherBody.mass * mass);
									otherBody.velocity.set(0, 0);
									otherBody.movement.set(0, 0);
									otherBody.move(push);
									otherBody.update();
									otherBody.velocity = previousVelocity;
									otherBody.movement = previousMovement;
									BoundCheckResult newResult = otherBody.checkBounds(checkPosition, width, height);
									if (newResult.collision())
									{
										bumped = true;
									}
								}
								else
								{
									bumped = true;
								}
							}
							if (bumped)
							{
								if ((result.bottomCollision() && step.getY() < 0) || (result.topCollision() && step.getY() > 0))
								{
									step.setY(0);
									canStep = false;
								}
								if ((result.rightCollision() && step.getX() < 0) || (result.leftCollision() && step.getX() > 0))
								{
									step.setX(0);
									canStep = false;
								}
							}
						}
					}
					
					if (canStep)
					{
						index++;
						position.set(checkPosition);
						constrain();
					}
					if (step.getX() == 0 && step.getY() == 0) break;
				}
			}
			Vector forcedDelta = forcedMovement.clone();
			forcedDelta.add(forcedVelocity);
			position.add(forcedDelta);
			constrain();
			if (hasFriction) velocity.divide(Physics.friction);
			movement.set(0, 0);
			forcedVelocity.divide(Physics.friction);
			forcedMovement.set(0, 0);
			inUpdate = false;
		}
	}
	
	
	void constrain()
	{
		double x = Physics.worldDimensions.getX();
		double y = Physics.worldDimensions.getY();
		if (x >= 0)
		{
			if (position.getX() < -x) position.setX(-x);
			if (position.getX() > x) position.setX(x);
		}
		if (y >= 0)
		{
			if (position.getY() < -y) position.setY(-y);
			if (position.getY() > y) position.setY(y);
		}
	}
	
	/**
	 * Checks if this body is colliding with another
	 * @param body the other body to check
	 * @return if the two bodies are colliding
	 */
	public boolean isColliding(PhysicalBody body)
	{
		for (Iterator<Collision> iterator = collisions.iterator(); iterator.hasNext();)
		{
			Collision collision = iterator.next();
			if (collision.body.equals(body)) return true;
		}
		return false;
	}
	
	/**
	 * Gets the distance to another PhysicalBody
	 * @param body the body to measure to
	 * @return the distance
	 */
	public double getDistance(PhysicalBody body)
	{
		return position.getDistance(body.position);
	}
	
	/**
	 * Draws debug information for this body
	 * @param drawable the drawable that the body will be drawn to
	 * @param cameraPosition the position of the camera
	 */
	public void debugDraw(Canvas drawable, int x, int y)
	{
		Color color = staticBody ? Color.GREEN : Color.RED;
		drawable.drawRect(x, y, width, height, false, color);
		drawable.drawLine(x, y, x + width, y + height, color);
		drawable.drawLine(x, y + height, x + width, y, color);
	}
	
	/**
	 * To be overridden by subclasses that want to have a visual representation of the body
	 * @param canvas the canvas to be drawn to
	 * @param x x coordinate to draw from
	 * @param y y coordinate to draw from
	 */
	public void draw(Canvas canvas, int x, int y)
	{
		//placeholder, does nothing
	}
	
	void bodyDraw(Canvas canvas, Vector cameraPosition)
	{
		int x = (int)(cameraPosition.getX() + position.getX()) - width / 2;
		int y = (int)(cameraPosition.getY() + position.getY()) - height / 2;
		if (visualized) draw(canvas, x, y);
		if (Physics.doPhysicsDraw) debugDraw(canvas, x, y);
	}
	
	/**
	 * force parameter defaults to false
	 * @param vector the movement vector
	 * @see #move(Vector, boolean)
	 */
	public void move(Vector vector)
	{
		move(vector, false);
	}
	
	/**
	 * Adds the given vector to the corresponding movement vector
	 * @param vector the movement vector
	 * @param force if this movement is forced
	 */
	public void move(Vector vector, boolean force)
	{
		if (force) forcedMovement.add(vector);
		else movement.add(vector);
	}
	
	/**
	 * Adds the given tag
	 * @param tag Tag to be added
	 */
	public void addTag(String tag)
	{
		if (!tags.contains(tag)) tags.add(tag);
	}
	
	/**
	 * Removes the given tag
	 * @param tag Tag to be removed
	 */
	public void removeTag(String tag)
	{
		if (tags.contains(tag)) tags.remove(tag);
	}
	
	/**
	 * Checks if this body has the given tag
	 * @param tag Tag to be given
	 * @return true if the body has the given tag, otherwise false
	 */
	public boolean hasTag(String tag)
	{
		return tags.contains(tag);
	}
	
	/**
	 * Retrieves a SecureList of this bodies tags
	 * @return SecureList of this bodies tags
	 */
	public SecureList<String> getTags()
	{
		return new SecureList<String>(tags);
	}
	
	/**
	 * Retrieves a SecureList of the collisions this body was involved in during the last update cycle
	 * @return SecureList of collisions
	 * @see PhysicalBody.Collision
	 */
	public SecureList<Collision> getCollisions()
	{
		return new SecureList<Collision>(collisions);
	}
	
	/**
	 * Gets a vector pointing from this PhysicalBody to a given position
	 * @param position target position
	 * @return vector
	 */
	public Vector getVectorTo(Vector position)
	{
		Vector vector = new Vector(-1, 0);
		vector.rotate(this.position.getCoordinateAngle(position));
		return vector;
	}
	
	/**
	 * Gets a vector pointing from this PhysicalBody to another
	 * @param body target body
	 * @return vector
	 * @see #getVectorTo(Vector)
	 */
	public Vector getVectorTo(PhysicalBody body)
	{
		return getVectorTo(body.position);
	}
	
	/**
	 * forced parameter defaults to false
	 * @param position the origin point
	 * @param weakForce the strength of the point force
	 * @param radiusSquared the radius of the point force
	 * @see #applyWeakPointForce(Vector, double, int, boolean)
	 */
	public void applyWeakPointForce(Vector position, double weakForce, int radiusSquared)
	{
		applyWeakPointForce(position, weakForce, radiusSquared, false);
	}
	
	/**
	 * Applies a weak point force on this body
	 * @param position the origin point
	 * @param weakForce the strength of the point force
	 * @param radiusSquared the radius of the point force
	 * @param forced if this force is forced
	 * <p>
	 * Similar to a normal point force, but the intended force should be divided by the Physics engine's weakForceFactor, intended for use as a constant force.
	 * The force is also scaled by the distance from the origin; 100% at the center, 0% at the edge
	 * </p>
	 * @see #applyPointForce(Vector, double, int, boolean)
	 */
	public void applyWeakPointForce(Vector position, double weakForce, int radiusSquared, boolean forced)
	{
		double distance = this.position.getDistanceSquared(position);
		if (distance < radiusSquared)
		{
			Vector forceVector = new Vector(weakForce - (weakForce / radiusSquared * distance), 0);
			forceVector.rotate(this.position.getCoordinateAngle(position));
			applyForce(forceVector, forced);
		}
	}
	
	/**
	 * forced parameter defaults to false
	 * @param position the origin point
	 * @param force the strength of the point force
	 * @param radiusSquared the radius of the point force
	 * @see #applyPointForce(Vector, double, int, boolean)
	 */
	public void applyPointForce(Vector position, double force, int radiusSquared)
	{
		applyPointForce(position, force, radiusSquared, false);
	}
	
	/**
	 * Applies a point force on this body
	 * @param position the origin point
	 * @param force the strength of the point force
	 * @param radiusSquared the radius of the point force
	 * @param forced if this force is forced
	 * <p>
	 * If the body is within the given radius, then the force will be applied in a vector that pushes the body away from the origin point
	 * </p>
	 * @see #applyForce(Vector, boolean)
	 */
	public void applyPointForce(Vector position, double force, int radiusSquared, boolean forced)
	{
		if (position.getDistanceSquared(position) < radiusSquared)
		{
			Vector forceVector = new Vector(force, 0);
			forceVector.rotate(this.position.getCoordinateAngle(position));
			applyForce(forceVector, forced);
		}
	}
	
	/**
	 * forced parameter defaults to false
	 * @param vector the force vector
	 * @see #applyForce(Vector, boolean)
	 */
	public void applyForce(Vector vector)
	{
		applyForce(vector, false);
	}
	
	/**
	 * Adds the given vector to the corresponding velocity
	 * @param vector the force vector
	 * @param forced if this force is forced
	 */
	public void applyForce(Vector vector, boolean forced)
	{
		if (forced) forcedVelocity.add(vector);
		else velocity.add(vector);
	}
	
	/**
	 * Gets the width of the body.
	 * @return the bodies width
	 */
	public int getWidth()
	{
		return width;
	}
	
	/**
	 * Gets the height of the body
	 * @return the bodies height
	 */
	public int getHeight()
	{
		return height;
	}
	
	/**
	 * Sets the dimensions of the body
	 * @param width width of the body
	 * @param height height of the body
	 */
	public void setDimensions(int width, int height)
	{
		this.width = width;
		this.height = height;
		
		angleToTopLeft = position.getCoordinateAngle(new Vector(position.getX() - width / 2, position.getY() - height / 2));
		angleToTopRight = position.getCoordinateAngle(new Vector(position.getX() + width / 2, position.getY() - height / 2));
		angleToBottomLeft = position.getCoordinateAngle(new Vector(position.getX() - width / 2, position.getY() + height / 2));
		angleToBottomRight = position.getCoordinateAngle(new Vector(position.getX() + width / 2, position.getY() + height / 2));
	}
	
	/**
	 * Gets the position of the body
	 * @return the bodies position
	 */
	public Vector getPosition()
	{
		return position;
	}
	
	public Vector getVelocity()
	{
		return velocity;
	}
	
	/**
	 * Gets whether this body is static
	 * @return if this body is static
	 */
	public boolean isStatic()
	{
		return staticBody;
	}
	
	/**
	 * Gets whether this body is dormant
	 * @return if this body is dormant
	 */
	public boolean isDormant()
	{
		return dormant;
	}
	
	/**
	 * Gets whether this body is visualized
	 * @return if this body is visualized
	 */
	public boolean isVisualized()
	{
		return visualized;
	}
	
	/**
	 * Sets whether this body is dormant
	 * @param dormant the new dormant state
	 */
	public void setDormant(boolean dormant)
	{
		if (Physics.useQueue)
		{
			new SetDormant(this, dormant);
		}
		else
		{
			if (dormant != this.dormant)
			{
				this.dormant = dormant;
				if (dormant)
				{
					if (staticBody) Physics.staticBodies.remove(this);
					else Physics.dynamicBodies.remove(this);
				}
				else
				{
					if (staticBody) Physics.staticBodies.add(this);
					else Physics.dynamicBodies.add(this);
				}
			}
		}
	}
	
	/**
	 * Sets whether this body is visualized
	 * @param visualized the new visualized state
	 */
	public void setVisualized(boolean visualized)
	{
		this.visualized = visualized;
	}
	
	/**
	 * Sets whether this body is dormant
	 * @param staticBody the new static state
	 */
	public void setStatic(boolean staticBody)
	{
		if (Physics.useQueue)
		{
			new SetStatic(this, staticBody);
		}
		else
		{
			if (staticBody != this.staticBody)
			{
				this.staticBody = staticBody;
				if (staticBody)
				{
					Physics.dynamicBodies.remove(this);
					Physics.staticBodies.add(this);
				}
				else
				{
					Physics.staticBodies.remove(this);
					Physics.dynamicBodies.add(this);
				}
			}
		}
	}
	
	/**
	 * Removes this body
	 */
	public void remove()
	{
		if (!removed)
		{
			if (Physics.useQueue)
			{
				new Remove(this);
			}
			else
			{
				removed = true;
				if (dormant)
				{
					Physics.dormantBodies.remove(this);
				}
				else
				{
					if (staticBody)
					{
						Physics.staticBodies.remove(this);
					}
					else
					{
						Physics.dynamicBodies.remove(this);
					}
				}
			}
		}
	}
	
	/**
	 * Checks if another body is overlapping this one
	 * @param body the other body
	 * @param offset the offset vector
	 * @return the bound check result
	 * <p>
	 * The given PhysicalBodies position is offset by the given vector
	 * </p>
	 * @see #checkBounds(Vector, int, int)
	 */
	public BoundCheckResult checkBounds(PhysicalBody body, Vector offset)
	{
		Vector offsetPosition = offset.clone();
		offsetPosition.add(body.position);
		return checkBounds(offsetPosition, body.width, body.height);
	}
	
	/**
	 * Checks if another body is overlapping this one
	 * @param body the other body
	 * @return the bound check result
	 * @see #checkBounds(Vector, int, int)
	 */
	public BoundCheckResult checkBounds(PhysicalBody body)
	{
		return checkBounds(body.position, body.width, body.height);
	}
	
	/**
	 * Checks if a given region is overlapping this body
	 * @param position location of the region
	 * @param width width of the region
	 * @param height height of the region
	 * @return the bound check result
	 */
	public BoundCheckResult checkBounds(Vector position, int width, int height)
	{
		if (!(position.getX() == this.position.getX() && position.getY() == this.position.getY()))
		{
			int xRange = this.width / 2 + width / 2;
			int yRange = this.height / 2 + height / 2;
			double xDistance = Math.abs(position.getX() - this.position.getX());
			double yDistance = Math.abs(position.getY() - this.position.getY());
			if (xDistance < xRange && yDistance < yRange)
			{
				double xOverStep = xRange - xDistance;
				double yOverStep = yRange - yDistance;
				double overStep = xOverStep;
				if (yOverStep > xOverStep) overStep = yOverStep;
				double angle = this.position.getCoordinateAngle(position);
				return new BoundCheckResult((angle <= angleToTopRight && angle >= angleToTopLeft), (angle <= angleToTopLeft && angle >= angleToBottomLeft),
						(angle <= angleToBottomRight || angle >= angleToTopRight), (angle <= angleToBottomLeft && angle >= angleToBottomRight), overStep, this);
			}
			else return new BoundCheckResult(false, false, false, false, 0, this);
		}
		else return new BoundCheckResult(true, true, true, true, Double.MAX_VALUE, this);
	}
	
	/**
	 * Represents a collision between two PhysicalBodies
	 */
	public class Collision
	{
		boolean topCollision;
		boolean leftCollision;
		boolean rightCollision;
		boolean bottomCollision;
		PhysicalBody body;
		PhysicalBody ownBody;
		
		Collision(BoundCheckResult result, PhysicalBody ownBody)
		{
			topCollision = result.bottomCollision;
			bottomCollision = result.topCollision;
			leftCollision = result.rightCollision;
			rightCollision = result.leftCollision;
			this.body = result.getBody();
			this.ownBody = ownBody;
		}
		
		/**
		 * Did this collision occur along the top of this body
		 * @return if the collision occurred along the top
		 */
		public boolean topCollision()
		{
			return topCollision;
		}
		
		/**
		 * Did this collision occur along the left of this body
		 * @return if the collision occurred along the left
		 */
		public boolean leftCollision()
		{
			return leftCollision;
		}
		
		/**
		 * Did this collision occur along the right of this body
		 * @return if the collision occurred along the right
		 */
		public boolean rightCollision()
		{
			return rightCollision;
		}
		
		/**
		 * Did this collision occur along the bottom of this body
		 * @return if the collision occurred along the bottom
		 */
		public boolean bottomCollision()
		{
			return bottomCollision;
		}
		
		/**
		 * Did this collision occur along the left or right of this body
		 * @return if the collision occurred along the left or right
		 */
		public boolean xCollision()
		{
			return leftCollision || rightCollision;
		}
		
		/**
		 * Did this collision occur along the top or bottom of this body
		 * @return if the collision occurred along the top or bottom
		 */
		public boolean yCollision()
		{
			return topCollision || bottomCollision;
		}
		
		/**
		 * Returns the other body involved in this collision
		 * @return the other body
		 */
		public PhysicalBody getBody()
		{
			return body;
		}
		
		/**
		 * Returns the primary body involved in this collision
		 * @return The primary body
		 */
		public PhysicalBody getOwnBody()
		{
			return ownBody;
		}
	}
	
	/**
	 * The result of a bound check
	 */
	public class BoundCheckResult
	{
		boolean topCollision;
		boolean leftCollision;
		boolean rightCollision;
		boolean bottomCollision;
		double overStep;
		PhysicalBody body;
		
		BoundCheckResult(boolean topCollision, boolean leftCollision, boolean rightCollision, boolean bottomCollision, double overStep, PhysicalBody body)
		{
			this.topCollision = topCollision;
			this.leftCollision = leftCollision;
			this.rightCollision = rightCollision;
			this.bottomCollision = bottomCollision;
			this.overStep = overStep;
			this.body = body;
		}
		
		/**
		 * Did the region overlap the body
		 * @return if the region overlapped the body
		 */
		public boolean collision()
		{
			return topCollision || leftCollision || rightCollision || bottomCollision;
		}
		
		/**
		 * Did this collision occur along the top of this body
		 * @return if the collision occurred along the top
		 */
		public boolean topCollision()
		{
			return topCollision;
		}
		
		/**
		 * Did this collision occur along the left of this body
		 * @return if the collision occurred along the left
		 */
		public boolean leftCollision()
		{
			return leftCollision;
		}
		
		/**
		 * Did this collision occur along the right of this body
		 * @return if the collision occurred along the right
		 */
		public boolean rightCollision()
		{
			return rightCollision;
		}
		
		/**
		 * Did this collision occur along the bottom of this body
		 * @return if the collision occurred along the bottom
		 */
		public boolean bottomCollision()
		{
			return bottomCollision;
		}
		
		/**
		 * Did this collision occur along the left or right of this body
		 * @return if the collision occurred along the left or right
		 */
		public boolean xCollision()
		{
			return leftCollision || rightCollision;
		}
		
		/**
		 * Did this collision occur along the top or bottom of this body
		 * @return if the collision occurred along the top or bottom
		 */
		public boolean yCollision()
		{
			return topCollision || bottomCollision;
		}
		
		/**
		 * How far did the region overlap this body
		 * @return the overlap amount
		 */
		public double getOverStep()
		{
			return overStep;
		}
		
		public String toString()
		{
			return "Top:" + topCollision + ",Left:" + leftCollision + ",Right:" + rightCollision + ",Bottom:" + bottomCollision + ",OverStep:" + overStep;
		}
		
		/**
		 * Gets the body used for this bound check
		 * @return the body used for this bound check
		 */
		public PhysicalBody getBody()
		{
			return body;
		}
	}
}