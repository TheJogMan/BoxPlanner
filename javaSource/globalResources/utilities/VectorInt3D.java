package globalResources.utilities;

public class VectorInt3D
{
	private int x;
	private int y;
	private int z;
	
	/**
	 * Creates a new VectorInt3D
	 * @param x x component of the vector
	 * @param y y component of the vector
	 * @param z z component of the vector
	 */
	public VectorInt3D(int x, int y, int z)
	{
		this.x = x;
		this.y = y;
		this.z = z;
	}
	
	/**
	 * Creates a new VectorInt3D using the values from a Vector3D
	 * @param vector Vector3D to take values from
	 */
	public VectorInt3D(Vector3D vector)
	{
		this.x = (int)vector.getX();
		this.y = (int)vector.getY();
		this.z = (int)vector.getZ();
	}
	
	/**
	 * Calculate the slope to another vector
	 * @param other the other vector
	 * @return the slope to the other vector
	 */
	public VectorInt3D slopeTo(VectorInt3D other)
	{
		int xDist = Math.abs(x - other.x);
		int yDist = Math.abs(y - other.y);
		int zDist = Math.abs(z - other.z);
		int distance = (int)Math.sqrt(Math.pow(xDist, 2.0) + Math.pow(yDist, 2.0) + Math.pow(zDist, 2.0));
		return new VectorInt3D(xDist / distance, yDist / distance, zDist / distance);
	}
	
	/**
	 * Calculate the distance to another vector
	 * @param other the other vector
	 * @return distance
	 */
	public int distance(VectorInt3D other)
	{
		return (int)Math.sqrt(Math.pow(Math.abs(x - other.x), 2.0) + Math.pow(Math.abs(y - other.y), 2.0) + Math.pow(Math.abs(z - other.z), 2.0));
	}
	
	/**
	 * Checks if this vector shares the same values with another
	 * @param other the other vector
	 * @return if the values are the same
	 */
	public boolean similar(VectorInt3D other)
	{
		return x == other.x && y == other.y && z == other.z;
	}
	
	/**
	 * Returns the x component of the vector
	 * @return x component of the vector
	 */
	public int getX()
	{
		return x;
	}
	
	/**
	 * Returns the y component of the vector
	 * @return y component of the vector
	 */
	public int getY()
	{
		return y;
	}
	
	/**
	 * Returns the z component of the vector
	 * @return z component of the vector
	 */
	public int getZ()
	{
		return z;
	}
	
	/**
	 * Sets the values of this vector to the values of another
	 * @param otherVector the vector to get the values from
	 */
	public void set(VectorInt3D otherVector)
	{
		x = otherVector.getX();
		y = otherVector.getY();
		z = otherVector.getZ();
	}
	
	/**
	 * Sets the values of this vector to that of another
	 * @param vector the other vector
	 */
	public void set(Vector3D vector)
	{
		this.x = (int)vector.getX();
		this.y = (int)vector.getY();
		this.z = (int)vector.getZ();
	}
	
	/**
	 * Set the x component of this vector
	 * @param x x component
	 */
	public void setX(int x)
	{
		this.x = x;
	}
	
	/**
	 * Set the y component of this vector
	 * @param y y component
	 */
	public void setY(int y)
	{
		this.y = y;
	}
	
	/**
	 * Sets the z component of this vector
	 * @param z z component
	 */
	public void setZ(int z)
	{
		this.z = z;
	}
	
	/**
	 * Rotates this vector
	 * @param angle degrees for rotation
	 */
	public void rotate(int angle)
	{
		double radian = Math.toRadians(angle);
		double cos = Math.cos(radian);
		double sin = Math.sin(radian);
		double originalX = (double)x;
		x = (int)((double)x * cos - (double)y * sin);
		y = (int)(originalX * sin + (double)y * cos);
	}
	
	/**
	 * Adds a value to each component of this vector
	 * @param number the value to add
	 */
	public void add(int number)
	{
		y += number;
		x += number;
		z += number;
	}
	
	/**
	 * Multiplies each component of this vector by a number
	 * @param number the value to multiply by
	 */
	public void multiply(int number)
	{
		x *= number;
		y *= number;
		z *= number;
	}
	
	/**
	 * Divides each component of this vector by a number
	 * @param number the value to divide by
	 */
	public void divide(int number)
	{
		x /= number;
		y /= number;
		z /= number;
	}
	
	/**
	 * Adds the value of each component from another vector to the corresponding components of this vector
	 * @param otherVector the vector to add
	 */
	public void add(VectorInt3D otherVector)
	{
		x += otherVector.getX();
		y += otherVector.getY();
		z += otherVector.getZ();
	}
	
	/**
	 * Subtracts the value of each component from another vector from the corresponding components of this vector
	 * @param otherVector the vector to subtract
	 */
	public void subtract(VectorInt3D otherVector)
	{
		x -= otherVector.getX();
		y -= otherVector.getY();
		z -= otherVector.getZ();
	}
	
	/**
	 * Multiplies each component of this vector by the corresponding components of another vector
	 * @param otherVector the vector to multiply by
	 */
	public void multiply(VectorInt3D otherVector)
	{
		x *= otherVector.getX();
		y *= otherVector.getY();
		z *= otherVector.getZ();
	}
	
	/**
	 * Creates a new vector with the same components as this one
	 * @return the new vector
	 */
	public VectorInt3D clone()
	{
		return new VectorInt3D(x,y,z);
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
		return "[X:" + x + ",Y:" + y + ",Z:" + z + "]";
	}
}
