package globalResources.pathfinding;

import globalResources.utilities.VectorInt3D;

public final class Cell
{
	private CellData data;
	private boolean valid;
	private Region region;
	private long timeOfCreation;
	private VectorInt3D position;
	
	Cell(CellData data, boolean validCell, Region region, VectorInt3D position)
	{
		this.data = data.clone();
		this.valid = validCell;
		this.region = region;
		this.timeOfCreation = System.nanoTime();
		this.position = position.clone();
	}
	
	public final void reprocess()
	{
		data = region.processCellData(position.getX(), position.getY(), position.getZ());
		timeOfCreation = System.nanoTime();
	}
	
	public final CellData getData()
	{
		return data.clone();
	}
	
	public final boolean isValid()
	{
		return valid;
	}
	
	public final Region getRegion()
	{
		return region;
	}
	
	public final long getTimeOfCreation()
	{
		return timeOfCreation;
	}
	
	public final boolean stillConsistent()
	{
		return region.stillConsistent(this);
	}
	
	public final Cell[] getConnectedCells()
	{
		return region.getConnectedCells(this);
	}
	
	public final VectorInt3D getPosition()
	{
		return position.clone();
	}
	
	public final int getX()
	{
		return position.getX();
	}
	
	public final int getY()
	{
		return position.getY();
	}
	
	public final int getZ()
	{
		return position.getZ();
	}
	
	public final Cell getAdjacentCell(int mx, int my, int mz)
	{
		return region.getCell(position.getX() + mx, position.getY() + my, position.getZ() + mz);
	}
	
	@Override
	public final boolean equals(Object object)
	{
		return object != null && (super.equals(object) || (object instanceof Cell && samePosition((Cell)object)));
	}
	
	public boolean samePosition(Cell cell)
	{
		return cell != null && cell.region.equals(this.region) && this.position.similar(cell.position);
	}
}