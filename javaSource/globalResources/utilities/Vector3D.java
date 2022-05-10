package globalResources.utilities;

import java.util.Random;

public class Vector3D
{
	private double x;
	private double y;
	private double z;
	
	/**
	 * Creates a new vector
	 * @param x x component of the vector
	 * @param y y component of the vector
	 * @param z z component of the vector
	 */
	public Vector3D(double x, double y, double z)
	{
		this.x = x;
		this.y = y;
		this.z = z;
	}
	
	/**
	 * Creates a new vector
	 * @param vector VectorInt3D to get components from
	 */
	public Vector3D(VectorInt3D vector)
	{
		this.x = vector.getX();
		this.y = vector.getY();
		this.z = vector.getZ();
	}
	
	/**
	 * Calculate the difference between this vector and another
	 * @param vector the other vector
	 * @return difference
	 */
	public Vector3D getDifference(Vector3D vector)
	{
		return new Vector3D(x - vector.getX(), y - vector.getY(), z - vector.getZ());
	}
	
	/**
	 * Picks a random location within a radius of this vector
	 * @param minRadius minimum distance from this vector, set to 0 to have no minimum distance
	 * @param maxRadius maximum distance from this vector
	 * @param random the random generator to be used
	 * @return the location that was picked
	 */
	public Vector3D randomPositionInRadius(double minRadius, double maxRadius, Random random)
	{
		Vector3D point = new Vector3D(0, 0, 0);
		double distance;
		do
		{
			double x = random.nextInt((int)(maxRadius - 1.0)) + random.nextDouble();
			if (random.nextBoolean()) x *= -1;
			double y = random.nextInt((int)(maxRadius - 1.0)) + random.nextDouble();
			if (random.nextBoolean()) y *= -1;
			double z = random.nextInt((int)(maxRadius - 1.0)) + random.nextDouble();
			if (random.nextBoolean()) z *= -1;
			point.set(this.x + x, this.y + y, this.z + z);
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
	 * Returns the z component of the vector, or Double.MIN_VALUE if it is zero
	 * @return non zero z component of the vector
	 */
	public double getNonZeroZ()
	{
		if (z == 0) return Double.MIN_VALUE;
		else return z;
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
	
	/**
	 * Returns the z component of the vector
	 * @return z component of the vector
	 */
	public double getZ()
	{
		return z;
	}
	
	/**
	 * Sets the values of this vector
	 * @param x x component
	 * @param y y component
	 * @param z z component
	 */
	public void set(double x, double y, double z)
	{
		this.x = x;
		this.y = y;
		this.z = z;
	}
	
	/**
	 * Sets the values of this vector to the values of another
	 * @param otherVector the vector to get the values from
	 */
	public void set(Vector3D otherVector)
	{
		x = otherVector.getX();
		y = otherVector.getY();
		z = otherVector.getZ();
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
	 * Sets the z component of this vector
	 * @param z z component
	 */
	public void setZ(double z)
	{
		this.z = z;
	}
	
	/**
	 * Calculates the slop to another vector
	 * @param other other vector
	 * @return slope to the other vector
	 */
	public Vector3D slopeTo(Vector3D other)
	{
		double xDist = x - other.x;
		double yDist = y - other.y;
		double zDist = z - other.z;
		double distance = Math.sqrt(Math.pow(Math.abs(xDist), 2.0) + Math.pow(Math.abs(yDist), 2.0) + Math.pow(Math.abs(zDist), 2.0));
		return new Vector3D(xDist / distance, yDist / distance, zDist / distance);
	}
	
	/**
	 * Gets the angle between this vector and another from the perspective of this vector
	 * @param otherVector the other vector
	 * @return the angle between these vectors
	 */
	public double getAngle(Vector3D otherVector)
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
	 * @see #getDistanceSquared(Vector3D)
	 * <p>
	 * Getting the square root of the squared distance is an expensive operation, and so that method should be used instead in situations where you are frequently comparing vector distances.
	 * </p>
	 */
	public double getDistance(Vector3D otherVector)
	{
		return Math.sqrt(getDistanceSquared(otherVector));
	}
	
	/**
	 * Gets the distances between this vector and another
	 * @param otherVector the other vector
	 * @return distance squared
	 */
	public double getDistanceSquared(Vector3D otherVector)
	{
		return Math.pow(Math.abs(x - otherVector.x), 2) + Math.pow(Math.abs(y - otherVector.y), 2) + Math.pow(Math.abs(z - otherVector.z), 2);
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
		return Math.pow(getX(), 2) + Math.pow(getY(), 2) + Math.pow(getZ(), 2);
	}
	
	/**
	 * Gets the dot between this vector and another
	 * @param otherVector the other vector
	 * @return the dot between these vectors
	 */
	public double getDot(Vector3D otherVector)
	{
		return (otherVector.getX() * getX()) + (otherVector.getY() * getY()) + (otherVector.getZ() * getZ());
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
	public double getNonZeroDot(Vector3D otherVector)
	{
		return (otherVector.getNonZeroX() * getNonZeroX()) + (otherVector.getNonZeroY() * getNonZeroY()) + (otherVector.getNonZeroZ() * getNonZeroZ());
	}
	
	/**
	 * Adds a value to each component of this vector
	 * @param number the value to add
	 */
	public void add(double number)
	{
		y += number;
		x += number;
		z += number;
	}
	
	/**
	 * Subtracts a value from each component of this vector
	 * @param number the value to subtract
	 */
	public void subtract(double number)
	{
		y -= number;
		x -= number;
		z -= number;
	}
	
	/**
	 * Multiplies each component of this vector by a value
	 * @param number the value to multiply by
	 */
	public void multiply(double number)
	{
		x *= number;
		y *= number;
		z *= number;
	}
	
	/**
	 * Divides each component of this vector by a value
	 * @param number the value to divide by
	 */
	public void divide(double number)
	{
		x /= number;
		y /= number;
		z /= number;
	}
	
	/**
	 * Adds the value of each component from another vector to the corresponding components of this vector
	 * @param otherVector the vector to add
	 */
	public void add(Vector3D otherVector)
	{
		x += otherVector.getX();
		y += otherVector.getY();
		z += otherVector.getZ();
	}
	
	/**
	 * Subtracts the value of each component from another vector from the corresponding components of this vector
	 * @param otherVector the vector to subtract
	 */
	public void subtract(Vector3D otherVector)
	{
		x -= otherVector.getX();
		y -= otherVector.getY();
		z -= otherVector.getZ();
	}
	
	/**
	 * Multiplies each component of this vector by the corresponding components of another vector
	 * @param otherVector the vector to multiply by
	 */
	public void multiply(Vector3D otherVector)
	{
		x *= otherVector.getX();
		y *= otherVector.getY();
		z *= otherVector.getZ();
	}
	
	/**
	 * Creates a new vector with the same components as this one
	 * @return the new vector
	 */
	public Vector3D clone()
	{
		return new Vector3D(x,y,z);
	}
	
	/**
	 * Creates a string representation of this vector
	 * @return string representation of this vector
	 * <p>
	 * Vector[X: xComponent, Y: yComponent, Z: zComponent]
	 * </p>
	 */
	@Override
	public String toString()
	{
		return "Vector[X: " + x + ", Y: " + y + ", Z:" + z + "]";
	}
}