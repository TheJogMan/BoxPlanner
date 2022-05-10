package mechanic.entities;

import globalResources.utilities.Vector;

public enum Direction
{//	Name		isRelative	Rotation	Direction
	FORWARD		(true,		0,			new Vector(0, 0)),
	LEFT		(true,		-90,		new Vector(0, 0)),
	RIGHT		(true,		90,			new Vector(0, 0)),
	BACKWARD	(true,		180,		new Vector(0, 0)),
	
	UP			(false,		0,			new Vector(0, -1)),
	NORTH		(false,		0,			new Vector(0, -1)),
	
	SLEFT		(false,		90,			new Vector(-1, 0)),
	WEST		(false,		90,			new Vector(-1, 0)),
	
	SRIGHT		(false,		-90,		new Vector(1, 0)),
	EAST		(false,		-90,		new Vector(1, 0)),
	
	DOWN		(false,		180,		new Vector(0, 1)),
	SOUTH		(false,		180,		new Vector(0, 1));
	
	private boolean isRelative;
	private double rotation;
	private Vector direction;
	
	Direction(boolean isRelative, double rotation, Vector direction)
	{
		this.isRelative = isRelative;
		this.rotation = rotation;
		this.direction = direction;
	}
	
	public boolean isRelative()
	{
		return isRelative;
	}
	
	public double getRotation()
	{
		return rotation;
	}
	
	public Vector getDirection()
	{
		return direction.clone();
	}
}