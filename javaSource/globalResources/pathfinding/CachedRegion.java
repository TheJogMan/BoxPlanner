package globalResources.pathfinding;

import globalResources.utilities.VectorInt3D;

public abstract class CachedRegion extends Region
{
	private Cell[][][] cache;
	
	public CachedRegion(int width, int height, int depth)
	{
		cache = new Cell[width][height][depth];
	}
	
	@Override
	final Cell internalGetCell(VectorInt3D position)
	{
		if (position.getX() >= 0 && position.getX() < cache.length && position.getY() >= 0 && position.getY() < cache[0].length && position.getZ() >= 0 && position.getZ() < cache[0][0].length)
		{
			if (!isValid(cache[position.getX()][position.getY()][position.getZ()])) cache[position.getX()][position.getY()][position.getZ()] = new Cell(processCellData(position), true, this, position);
			return cache[position.getX()][position.getY()][position.getZ()];
		}
		else return new Cell(processCellData(position), false, this, position);
	}
	
	public final boolean isValid(Cell cell)
	{
		return cell != null && cell.isValid() && cell.stillConsistent();
	}
	
	public final int getWidth()
	{
		return cache.length;
	}
	
	public final int getHeight()
	{
		return cache[0].length;
	}
	
	public final int getDepth()
	{
		return cache[0][0].length;
	}
}