package jogLibrary.collision;

public class Collision
{
	public static boolean pointVsRect(Vector point, Rectangle rectangle)
	{
		return	point.x >= rectangle.position.x && point.x < rectangle.position.x + rectangle.size.x &&
				point.y >= rectangle.position.y && point.y < rectangle.position.y + rectangle.size.y;
	}
	
	public static boolean rectVsRect(Rectangle rectangle1, Rectangle rectangle2)
	{
		return 	rectangle1.position.x < rectangle2.position.x + rectangle2.size.x && rectangle1.position.x + rectangle1.size.x > rectangle2.position.x &&
				rectangle1.position.y < rectangle2.position.y + rectangle2.size.y && rectangle1.position.y + rectangle1.size.y > rectangle2.position.y;
	}
	
	public static RayVsRect rayVsRect(Vector origin, Vector direction, Rectangle target)
	{
		return new RayVsRect(origin, direction, target);
	}
	
	public static DynamicRectVsRect DynamicRectVsRect(Rectangle in, Rectangle target, float elapsedTime)
	{
		return new DynamicRectVsRect(in, target, elapsedTime);
	}
	
	public static class DynamicRectVsRect
	{
		Rectangle in;
		Rectangle target;
		RayVsRect rayVsRect;
		float elapsedTime;
		
		boolean collision;
		
		DynamicRectVsRect(Rectangle in, Rectangle target, float elapsedTime)
		{
			this.elapsedTime = elapsedTime;
			Vector origin = in.size.divide(2).add(in.position);
			float halfX = in.size.x / 2;
			float halfY = in.size.y / 2;
			Vector inflation = in.size;
			Vector shift = new Vector(-halfX, -halfY);
			
			if (in.velocity.x == 0 && in.velocity.y == 0)
			{
				rayVsRect = new RayVsRect(false, new Vector(0, 0), new Vector(0, 0), 0, origin, in.velocity.multiply(elapsedTime), target, inflation, shift);
				collision = false;
				return;
			}
			
			rayVsRect = new RayVsRect(origin, in.velocity.multiply(elapsedTime), target, inflation, shift);
			collision = rayVsRect.trueCollision();
		}
		
		public Vector contactPoint()
		{
			return rayVsRect.contactPoint;
		}
		
		public Vector contactNormal()
		{
			return rayVsRect.contactNormal;
		}
		
		public float contactTime()
		{
			return rayVsRect.tHitNear;
		}
		
		public boolean collision()
		{
			return collision;
		}
	}
	
	public static class RayVsRect
	{
		Vector origin;
		Vector direction;
		Vector inflation;
		Vector shift;
		Rectangle target;
		boolean collision;
		Vector contactPoint = new Vector(0, 0);
		Vector contactNormal = new Vector(0, 0);
		float tHitNear = 0;
		
		RayVsRect(boolean collision, Vector contactPoint, Vector contactNormal, float tHitNear, Vector origin, Vector direction, Rectangle target, Vector inflation, Vector shift)
		{
			this.collision = collision;
			this.contactPoint = contactPoint;
			this.contactNormal = contactNormal;
			this.tHitNear = tHitNear;
			
			this.origin = origin;
			this.direction = direction;
			this.target = target;
			this.inflation = inflation;
			this.shift = shift;
		}
		
		RayVsRect(Vector origin, Vector direction, Rectangle target)
		{
			this(origin, direction, target, new Vector(0, 0), new Vector(0, 0));
		}
		
		RayVsRect(Vector origin, Vector direction, Rectangle target, Vector inflation, Vector shift)
		{
			this.origin = origin;
			this.direction = direction;
			this.target = target;
			this.inflation = inflation;
			this.shift = shift;
			
			Vector shiftedTargetPostion = target.position.add(shift);
			Vector tNear = shiftedTargetPostion.subtract(origin).divide(direction);
			Vector tFar = shiftedTargetPostion.add(target.size.add(inflation)).subtract(origin).divide(direction);
			
			if (tNear.x > tFar.x)
			{
				float x = tNear.x;
				tNear.x = tFar.x;
				tFar.x = x;
			}
			if (tNear.y > tFar.y)
			{
				float y = tNear.y;
				tNear.y = tFar.y;
				tFar.y = y;
			}
			
			if (tNear.x > tFar.y || tNear.y > tFar.x)
			{
				collision = false;
				return;
			}
			
			tHitNear = Math.max(tNear.x, tNear.y);
			float tHitFar = Math.min(tFar.x, tFar.y);
			
			if (tHitFar < 0)
			{
				collision = false;
				return;
			}
			
			contactPoint = direction.multiply(tHitNear).add(origin);
			
			if (tNear.x > tNear.y)
			{
				if (direction.x < 0)
				{
					contactNormal = new Vector(1, 0);
				}
				else
				{
					contactNormal = new Vector(-1, 0);
				}
			}
			else if (tNear.x < tNear.y)
			{
				if (direction.y < 0)
				{
					contactNormal = new Vector(0, 1);
				}
				else
				{
					contactNormal = new Vector(0, -1);
				}
			}
			
			collision = true;
		}
		
		public Vector contactPoint()
		{
			return contactPoint;
		}
		
		public Vector contactNormal()
		{
			return contactNormal;
		}
		
		public float contactTime()
		{
			return tHitNear;
		}
		
		public boolean collision()
		{
			return collision;
		}
		
		public boolean trueCollision()
		{
			return collision && tHitNear <= 1;
		}
	}
	
	public static class Vector
	{
		public float x;
		public float y;
		
		public Vector(float x, float y)
		{
			this.x = x;
			this.y = y;
		}
		
		public float length()
		{
			return (float)Math.sqrt(x * x + y * y);
		}
		
		public Vector normalize()
		{
			float length = length();
			return new Vector(x / length, y / length);
		}
		
		public Vector subtract(Vector other)
		{
			return new Vector(x - other.x, y - other.y);
		}
		
		public Vector multiply(Vector other)
		{
			return new Vector(x * other.x, y * other.y);
		}
		
		public Vector multiply(float value)
		{
			return new Vector(x * value, y * value);
		}
		
		public Vector divide(Vector other)
		{
			return new Vector(x / other.x, y / other.y);
		}
		
		public Vector divide(float value)
		{
			return new Vector(x / value, y / value);
		}
		
		public Vector add(Vector other)
		{
			return new Vector(x + other.x, y + other.y);
		}
	}
	
	public static class Rectangle
	{
		public Vector position;
		public Vector size;
		
		public Vector velocity;
		
		public Rectangle(Vector position, Vector size)
		{
			this.position = position;
			this.size = size;
			velocity = new Vector(0, 0);
		}
	}
}