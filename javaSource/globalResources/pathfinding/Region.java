package globalResources.pathfinding;

import globalResources.utilities.VectorInt3D;

public abstract class Region
{
	public abstract CellData processCellData(int x, int y, int z);
	public abstract boolean stillConsistent(Cell cell);
	public abstract Cell[] getConnectedCells(Cell cell);
	
	public final CellData processCellData(VectorInt3D position)
	{
		return processCellData(position.getX(), position.getY(), position.getZ());
	}
	
	public final CellData processCellData(int x, int y)
	{
		return processCellData(x, 0, y);
	}
	
	public final Cell getCell(VectorInt3D position)
	{
		return internalGetCell(position);
	}
	
	public final Cell getCell(int x, int y, int z)
	{
		return getCell(new VectorInt3D(x, y, z));
	}
	
	public final Cell getCell(int x, int y)
	{
		return getCell(x, 0, y);
	}
	
	Cell internalGetCell(VectorInt3D position)
	{
		return new Cell(processCellData(position), true, this, position);
	}
	
	public final Path getPath(int originX, int originY, int originZ, int destinationX, int destinationY, int destinationZ)
	{
		return getPath(new VectorInt3D(originX, originY, originZ), new VectorInt3D(destinationX, destinationY, destinationZ));
	}
	
	public final Path getPath(VectorInt3D origin, VectorInt3D destination)
	{
		return new Path(this, origin, destination);
	}
}