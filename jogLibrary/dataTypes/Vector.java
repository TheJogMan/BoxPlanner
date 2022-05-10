package jogLibrary.dataTypes;

import java.util.Random;

public class Vector
{
	private double x;
	private double y;
	
	/**
	 * Creates a new vector
	 * @param x x component of the vector
	 * @param y y component of the vector
	 */
	public Vector(double x, double y)
	{
		this.x = x;
		this.y = y;
	}
	
	public Vector getDifference(Vector vector)
	{
		return new Vector(x - vector.getX(), y - vector.getY());
	}
	
	/**
	 * Picks a random location within a radius of this vector
	 * @param minRadius minimum distance from this vector, set to 0 to have no minimum distance
	 * @param maxRadius maximum distance from this vector
	 * @param random the random generator to be used
	 * @return the location that was picked
	 */
	public Vector randomPositionInRadius(double minRadius, double maxRadius, Random random)
	{
		Vector point = new Vector(0, 0);
		double distance;
		do
		{
			double x = random.nextInt((int)(maxRadius - 1.0)) + random.nextDouble();
			if (random.nextBoolean()) x *= -1;
			double y = random.nextInt((int)(maxRadius - 1.0)) + random.nextDouble();
			if (random.nextBoolean()) y *= -1;
			point.set(this.x + x, this.y + y);
			distance = point.getDistance(this);
		}
		while (distance < minRadius || distance > maxRadius);
		return point;
	}
	
	/**
	 * Returns the x component of the vector, or Double.MIN_VALUE if it is zero
	 * @return non zero x component of the vector
	 */
	public double getNonZeroX()
	{
		if (x == 0) return Double.MIN_VALUE;
		else return x;
	}
	
	/**
	 * Returns the y component of the vector, or Double.MIN_VALUE if it is zero
	 * @return non zero y component of the vector
	 */
	public double getNonZeroY()
	{
		if (y == 0) return Double.MIN_VALUE;
		else return y;
	}
	
	/**
	 * Returns the x component of the vector
	 * @return x component of the vector
	 */
	public double getX()
	{
		return x;
	}
	
	/**
	 * Returns the y component of the vector
	 * @return y component of the vector
	 */
	public double getY()
	{
		return y;
	}
	
	public Vector slopeTo(Vector other)
	{
		double xDist = x - other.x;
		double yDist = y - other.y;
		double distance = Math.sqrt(Math.pow(Math.abs(xDist), 2.0) + Math.pow(Math.abs(yDist), 2.0));
		return new Vector(xDist / distance, yDist / distance);
	}
	
	/**
	 * Sets the values of this vector
	 * @param x x component
	 * @param y y component
	 */
	public void set(double x, double y)
	{
		this.x = x;
		this.y = y;
	}
	
	/**
	 * Sets the values of this vector to the values of another
	 * @param otherVector the vector to get the values from
	 */
	public void set(Vector otherVector)
	{
		x = otherVector.getX();
		y = otherVector.getY();
	}
	
	/**
	 * Set the x component of this vector
	 * @param x x component
	 */
	public void setX(double x)
	{
		this.x = x;
	}
	
	/**
	 * Sets the y component of this vector
	 * @param y y component
	 */
	public void setY(double y)
	{
		this.y = y;
	}
	
	/**
	 * Rotates this vector
	 * @param angle degrees for rotation
	 */
	public void rotate(double angle)
	{
		double radian = Math.toRadians(angle);
		double cos = Math.cos(radian);
		double sin = Math.sin(radian);
		double originalX = x;
		x = x * cos - y * sin;
		y = originalX * sin + y * cos;
	}
	
	/**
	 * Gets the coordinate angle between this vector and a vector with the value of (0, 0)
	 * @return relative coordinate angle of this vector
	 * @see #getCoordinateAngle(Vector)
	 */
	public double getRelativeCoordinateAngle()
	{
		return getCoordinateAngle(new Vector(0, 0));
	}
	
	/**
	 * Gets the coordinate angle between this vector and another from the perspective of this vector
	 * @param otherVector the other vector
	 * @return coordinate angle between these vectors
	 */
	public double getCoordinateAngle(Vector otherVector)
	{
		double slopeX = otherVector.getX() - getX();
		double slopeY = otherVector.getY() - getY();
		if (slopeX == 0 && slopeY == 0) slopeX = Double.MIN_VALUE;
		return Math.toDegrees(Math.atan2(-slopeY, 1 - slopeX));
	}
	
	/**
	 * Gets the angle between this vector and another from the perspective of this vector
	 * @param otherVector the other vector
	 * @return the angle between these vectors
	 */
	public double getAngle(Vector otherVector)
	{
		double angle = getDot(otherVector) / (getLength() * otherVector.getLength());
		if (Double.isNaN(angle))
		{
			angle = 0;
		}
		angle = Math.acos(angle);
		angle = Math.toDegrees(angle);
		return angle;
	}
	
	/**
	 * Get the distance between this vector and another
	 * @param otherVector the other vector
	 * @return distance
	 * @see #getDistanceSquared(Vector)
	 * <p>
	 * Getting the square root of the squared distance is an expensive operation, and so that method should be used instead in situations where you are frequently comparing vector distances.
	 * </p>
	 */
	public double getDistance(Vector otherVector)
	{
		return Math.sqrt(getDistanceSquared(otherVector));
	}
	
	/**
	 * Gets the distances between this vector and another
	 * @param otherVector the other vector
	 * @return distance squared
	 */
	public double getDistanceSquared(Vector otherVector)
	{
		return Math.pow(Math.abs(x - otherVector.x), 2) + Math.pow(Math.abs(y - otherVector.y), 2);
	}
	
	/**
	 * Gets the length of this vector
	 * @return length
	 * @see #getLengthSquared()
	 * <p>
	 * Getting the square root of the squared length is an expensive operation, and so that method should be used instead in situations where you are frequently checking vector lengths.
	 * </p>
	 */
	public double getLength()
	{
		return Math.sqrt(getLengthSquared());
	}
	
	/**
	 * Gets the length of this vector
	 * @return length squared
	 */
	public double getLengthSquared()
	{
		return Math.pow(getX(), 2) + Math.pow(getY(), 2);
	}
	
	/**
	 * Gets the dot between this vector and another
	 * @param otherVector the other vector
	 * @return the dot between these vectors
	 */
	public double getDot(Vector otherVector)
	{
		return (otherVector.getX() * getX()) + (otherVector.getY() * getY());
	}
	
	/**
	 * Gets the length of this vector, or Double.MIN_VALUE if it would have been zero
	 * @return non zero length
	 */
	public double getNonZeroLength()
	{
		double length = getLength();
		if (length == 0) return Double.MIN_VALUE;
		else return length;
	}
	
	/**
	 * Gets the dot between this vector and another using non zero components
	 * @param otherVector the other vector
	 * @return the non zero dot between these vectors
	 * @see #getNonZeroX()
	 * @see #getNonZeroY()
	 */
	public double getNonZeroDot(Vector otherVector)
	{
		return (otherVector.getNonZeroX() * getNonZeroX()) + (otherVector.getNonZeroY() * getNonZeroY());
	}
	
	public void add(double number)
	{
		y += number;
		x += number;
	}
	
	public void subtract(double number)
	{
		y -= number;
		x -= number;
	}
	
	public void multiply(double number)
	{
		x *= number;
		y *= number;
	}
	
	public void divide(double number)
	{
		x /= number;
		y /= number;
	}
	
	public void add(Vector otherVector)
	{
		x += otherVector.getX();
		y += otherVector.getY();
	}
	
	public void subtract(Vector otherVector)
	{
		x -= otherVector.getX();
		y -= otherVector.getY();
	}
	
	public void multiply(Vector otherVector)
	{
		x *= otherVector.getX();
		y *= otherVector.getY();
	}
	
	public void divide(Vector otherVector)
	{
		x /= otherVector.getX();
		y /= otherVector.getY();
	}
	
	/**
	 * Creates a new vector with the same components as this one
	 * @return the new vector
	 */
	public Vector clone()
	{
		return new Vector(x,y);
	}
	
	/**
	 * Creates a string representation of this vector
	 * @return string representation of this vector
	 * <p>
	 * Vector[X: xComponent, Y: yComponent]
	 * </p>
	 */
	@Override
	public String toString()
	{
		return "Vector[X: " + x + ", Y: " + y + "]";
	}
}
