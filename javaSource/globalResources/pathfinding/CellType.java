package globalResources.pathfinding;

public enum CellType
{
	IMPASSABLE				(Integer.MAX_VALUE),
	TRAVERSABLE				(4),
	UNDESIREABLE			(10),
	DANGEROUS				(15),
	DEADLY					(20),
	PREFERABLE				(3),
	VERY_PREFERABLE			(2),
	EXTREMELY_PREFERABLE	(1);
	
	private int value;
	
	CellType(int value)
	{
		this.value = value;
	}
	
	public int getValue()
	{
		return value;
	}
}