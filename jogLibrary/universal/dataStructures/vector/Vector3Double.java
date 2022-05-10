package jogLibrary.universal.dataStructures.vector;

import jogLibrary.universal.richString.RichColor;
import jogLibrary.universal.richString.RichString;
import jogLibrary.universal.richString.RichStringBuilder;

public class Vector3Double extends Vector3<Double>
{
	public Vector3Double(Double x, Double y, Double z)
	{
		super(x, y, z);
	}
	
	public Vector3Double(Vector3<?> vector)
	{
		super(vector.x().doubleValue(), vector.y().doubleValue(), vector.z().doubleValue());
	}
	
	@Override
	public Vector3Double clone()
	{
		return new Vector3Double(x(), y(), z());
	}
	
	@Override
	public String toString()
	{
		return "{X: " + x() + ", Y: " + y() + ", Z: " + z() + "}";
	}
	
	public RichString toRichString()
	{
		RichStringBuilder builder = RichStringBuilder.start();
		builder.setStyle(builder.getStyle().setMainColor(RichColor.ORANGE));
		builder.append(builder.getStyle().setMainColor(RichColor.AQUA), "{");
		builder.append("X: ");
		builder.append(builder.getStyle().setMainColor(RichColor.WHITE), x().toString());
		builder.append(builder.getStyle().setMainColor(RichColor.AQUA), ", ");
		builder.append("Y: ");
		builder.append(builder.getStyle().setMainColor(RichColor.WHITE), y().toString());
		builder.append(builder.getStyle().setMainColor(RichColor.AQUA), ", ");
		builder.append("Z: ");
		builder.append(builder.getStyle().setMainColor(RichColor.WHITE), z().toString());
		builder.append(builder.getStyle().setMainColor(RichColor.AQUA), "}");
		return builder.build();
	}
	
	public int intX()
	{
		return x().intValue();
	}
	
	public int intY()
	{
		return y().intValue();
	}
	
	public int intZ()
	{
		return z().intValue();
	}
	
	public Vector3Double add(Vector3<?> vector)
	{
		setX(x() + vector.x().doubleValue());
		setY(y() + vector.y().doubleValue());
		setZ(z() + vector.z().doubleValue());
		return this;
	}
	
	public Vector3Double add(double value)
	{
		setX(x() + value);
		setY(y() + value);
		setZ(z() + value);
		return this;
	}
	
	public Vector3Double subtract(Vector3<?> vector)
	{
		setX(x() - vector.x().doubleValue());
		setY(y() - vector.y().doubleValue());
		setZ(z() - vector.z().doubleValue());
		return this;
	}
	
	public Vector3Double subtract(double value)
	{
		setX(x() - value);
		setY(y() - value);
		setZ(z() - value);
		return this;
	}
	
	public Vector3Double multiply(Vector3<?> vector)
	{
		setX(x() * vector.x().doubleValue());
		setY(y() * vector.y().doubleValue());
		setZ(z() * vector.z().doubleValue());
		return this;
	}
	
	public Vector3Double multiply(double value)
	{
		setX(x() * value);
		setY(y() * value);
		setZ(z() * value);
		return this;
	}
	
	public Vector3Double divide(Vector3<?> vector)
	{
		setX(x() / vector.x().doubleValue());
		setY(y() / vector.y().doubleValue());
		setZ(z() / vector.z().doubleValue());
		return this;
	}
	
	public Vector3Double divide(double value)
	{
		setX(x() / value);
		setY(y() / value);
		setZ(z() / value);
		return this;
	}
	
	public double distance(Vector3<?> vector)
	{
		return Math.sqrt(squaredDistance(vector));
	}
	
	public double squaredDistance(Vector3<?> vector)
	{
		return Math.pow(Math.abs(x() - vector.x().doubleValue()), 2) + Math.pow(Math.abs(y() - vector.y().doubleValue()), 2) + Math.pow(Math.abs(z() - vector.z().doubleValue()), 2);
	}
	
	public double angleRadians(Vector3Double vector)
	{
		double angle = dot(vector) / (length() * vector.length());
		return Math.acos(angle);
	}
	
	public double angleDegrees(Vector3Double vector)
	{
		return Math.toDegrees(angleRadians(vector));
	}
	
	public double dot(Vector3<?> vector)
	{
		return x() * vector.x().doubleValue() + y() * vector.y().doubleValue() + z() * vector.z().doubleValue();
	}
	
	public double length()
	{
		return Math.sqrt(lengthSquared());
	}
	
	public double lengthSquared()
	{
		return x() * x() + y() * y() + z() * z();
	}
	
	public Vector3Double normalize()
	{
		return divide(length());
	}
	
	public Vector3Double crossProduct(Vector3<?> vector)
	{
		return new Vector3Double(
				y() * vector.z().doubleValue() - vector.y().doubleValue() * z(),
				z() * vector.x().doubleValue() - vector.z().doubleValue() * x(),
				x() * vector.y().doubleValue() - vector.x().doubleValue() * y()
				);
	}
	
	public Vector3Double maximum(Vector3<?> vector)
	{
		return new Vector3Double(
				x() > vector.x().doubleValue() ? x() : vector.x().doubleValue(),
				y() > vector.y().doubleValue() ? y() : vector.y().doubleValue(),
				z() > vector.z().doubleValue() ? z() : vector.z().doubleValue()
				);
	}
	
	public Vector3Double minimum(Vector3<?> vector)
	{
		return new Vector3Double(
				x() < vector.x().doubleValue() ? x() : vector.x().doubleValue(),
				y() < vector.y().doubleValue() ? y() : vector.y().doubleValue(),
				z() < vector.z().doubleValue() ? z() : vector.z().doubleValue()
				);
	}
}