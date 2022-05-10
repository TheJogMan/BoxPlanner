package jogLibrary.utilities;

public final class Path<SourceType, IndexType>
{
	//private boolean maxCostIsTraversable = false;
	//private Region<SourceType, IndexType> region;
	//private IndexType start;
	//private IndexType end;
	//private TraversalMode[] availableModes;
	
	public Path(Region<SourceType, IndexType> region, IndexType start, IndexType end, TraversalMode[] availableModes)
	{
		//this.region = region;
		//this.start = start;
		//this.end = end;
		//this.availableModes = availableModes;
	}
	
	public static interface TraversalMode
	{
		
	}
	
	public static interface Region<SourceType, IndexType>
	{
		public default Cell<SourceType, IndexType> getCell(IndexType index)
		{
			return new Cell<SourceType, IndexType>(this, index);
		}
		
		public default Path<SourceType, IndexType> path(IndexType start, IndexType end, TraversalMode[] availableModes)
		{
			return new Path<SourceType, IndexType>(this, start, end, availableModes);
		}
		
		public static class Cell<SourceType, IndexType>
		{
			public final Path<SourceType, IndexType> path(IndexType end, TraversalMode[] availableModes)
			{
				return region.path(index, end, availableModes);
			}
			
			public final Path<SourceType, IndexType> path(Cell<SourceType, IndexType> end, TraversalMode[] availableModes)
			{
				return region.path(index, end.index, availableModes);
			}
			
			private Region<SourceType, IndexType> region;
			private IndexType index;
			
			public Cell(Region<SourceType, IndexType> region, IndexType index)
			{
				this.region = region;
				this.index = index;
			}
			
			public final Region<SourceType, IndexType> region()
			{
				return region;
			}
			
			public final IndexType index()
			{
				return index;
			}
			
			@SuppressWarnings("unchecked")
			public final Cell<SourceType, IndexType>[] connectedCells()
			{
				IndexType[] indecies = connectedIndecies();
				Cell<SourceType, IndexType>[] cells = (Cell<SourceType, IndexType>[])new Object[indecies.length];
				for (int index = 0; index < indecies.length; index++)
					cells[index] = region.getCell(indecies[index]);
				return cells;
			}
			
			public final SourceType source()
			{
				return region.source(index);
			}
			
			public final TraversalMode[] availableTraversalModes()
			{
				return region.availableTraversalModes(index);
			}
			
			public final boolean traversalModeAvailable(TraversalMode mode)
			{
				return region.traversalModeAvailable(index, mode);
			}
			
			public final int traversalCost(TraversalMode mode)
			{
				return region.traversalCost(index, mode);
			}
			
			public final IndexType[] connectedIndecies()
			{
				return region.connectedIndecies(index);
			}
		}
		
		public SourceType source(IndexType index);
		public TraversalMode[] availableTraversalModes(IndexType index);
		public boolean traversalModeAvailable(IndexType index, TraversalMode mode);
		public int traversalCost(IndexType index, TraversalMode mode);
		public IndexType[] connectedIndecies(IndexType index);
		
		public boolean validIndex(IndexType index);
	}
}