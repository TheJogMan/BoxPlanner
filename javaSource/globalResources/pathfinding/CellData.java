package globalResources.pathfinding;

public final class CellData
{
	private CellType type;
	
	public CellData()
	{
		defaults();
	}
	
	public CellData(CellType type)
	{
		this.type = type;
	}
	
	public final void defaults()
	{
		type = CellType.TRAVERSABLE;
	}
	
	@Override
	public final CellData clone()
	{
		CellData newData = new CellData();
		newData.type = this.type;
		return newData;
	}
	
	public final CellType getType()
	{
		return type;
	}
	
	public final void setType(CellType type)
	{
		this.type = type;
	}
}