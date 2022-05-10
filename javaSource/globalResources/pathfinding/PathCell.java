package globalResources.pathfinding;

public final class PathCell
{
	private Path path;
	private Cell cell;
	private int distance;
	private int rawDistance;
	private int value;
	private PathCell source;
	private long processTime;
	int reverseDistance;
	
	PathCell(Path path, Cell cell, PathCell source)
	{
		this.path = path;
		this.cell = cell;
		this.source = source;
		getValue(true);
	}
	
	public int getValue(boolean reCalculate)
	{
		if (reCalculate)
		{
			value = cell.getData().getType().getValue();
			if (path.containsCell(cell)) value /= 2;
			if (source != null)
			{
				distance = source.distance + value;
				rawDistance = source.rawDistance + 1;
			}
			else
			{
				distance = value;
				rawDistance = 0;
			}
			processTime = System.currentTimeMillis();
		}
		return value;
	}
	
	public boolean traversable()
	{
		return !cell.getData().getType().equals(CellType.IMPASSABLE);
	}
	
	public final Cell getCell()
	{
		return cell;
	}
	
	public final PathCell getSource()
	{
		return source;
	}
	
	public final int getDistance()
	{
		return distance;
	}
	
	public final int getRawDistance()
	{
		return rawDistance;
	}
	
	public final long getTimeOfProcess()
	{
		return processTime;
	}
	
	public final Path getPath()
	{
		return path;
	}
}