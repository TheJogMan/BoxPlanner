package globalResources.utilities;

public class VectorInt
{
	private int x;
	private int y;
	
	/**
	 * Creates a new vectorInt
	 * @param x x component of the vector
	 * @param y y component of the vector
	 */
	public VectorInt(int x, int y)
	{
		this.x = x;
		this.y = y;
	}
	
	public VectorInt(Vector step)
	{
		this.x = (int)step.getX();
		this.y = (int)step.getY();
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
	 * Sets the values of this vector to the values of another
	 * @param otherVector the vector to get the values from
	 */
	public void set(VectorInt otherVector)
	{
		x = otherVector.getX();
		y = otherVector.getY();
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
	}
	
	/**
	 * Multiplies each component of this vector by a number
	 * @param number the value to multiply by
	 */
	public void multiply(int number)
	{
		x *= number;
		y *= number;
	}
	
	/**
	 * Divides each component of this vector by a value
	 * @param number the value to divide by
	 */
	public void divide(int number)
	{
		x /= number;
		y /= number;
	}
	
	/**
	 * Adds the value of each component from another vector to the corresponding components of this vector
	 * @param otherVector the vector to add
	 */
	public void add(VectorInt otherVector)
	{
		x += otherVector.getX();
		y += otherVector.getY();
	}
	
	/**
	 * Subtracts the value of each component from another vector from the corresponding components of this vector
	 * @param otherVector the vector to subtract
	 */
	public void subtract(VectorInt otherVector)
	{
		x -= otherVector.getX();
		y -= otherVector.getY();
	}
	
	/**
	 * Multiplies each component of this vector by the corresponding components of another vector
	 * @param otherVector the vector to multiply by
	 */
	public void multiply(VectorInt otherVector)
	{
		x *= otherVector.getX();
		y *= otherVector.getY();
	}
	
	/**
	 * Creates a new vector with the same components as this one
	 * @return the new vector
	 */
	public VectorInt clone()
	{
		return new VectorInt(x,y);
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
		return "[X:" + x + ",Y:" + y + "]";
	}
}
