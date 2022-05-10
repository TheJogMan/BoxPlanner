package globalResources.pathfinding;

import java.util.ArrayList;
import java.util.Iterator;

import globalResources.utilities.Vector3D;
import globalResources.utilities.VectorInt3D;

public class Path
{
	private Region region;
	private VectorInt3D from;
	private VectorInt3D to;
	private boolean complete;
	
	private ArrayList<PathCell> path;
	private ArrayList<PathCell> checked;
	private ArrayList<PathCell> candidates;
	private ArrayList<PathCell> impassible;
	
	Path(Region region, int fromX, int fromY, int fromZ, int toX, int toY, int toZ)
	{
		this(region, new VectorInt3D(fromX, fromY, fromZ), new VectorInt3D(toX, toY, toZ));
	}
	
	Path(Region region, VectorInt3D from, VectorInt3D to)
	{
		this.region = region;
		this.from = from.clone();
		this.to = to.clone();
		
		complete = false;
		
		path = new ArrayList<PathCell>();
		checked = new ArrayList<PathCell>();
		candidates = new ArrayList<PathCell>();
		impassible = new ArrayList<PathCell>();
		
		reset();
	}
	
	public final boolean step()
	{
		if (!complete && candidates.size() > 0)//only do something if the path is incomplete and has candidates to check
		{
			//first step find a candidate that is closest to the destination while requiring the least effort
			
			//start by finding all the candidates that have the same lowest effort
			int minDist = Integer.MAX_VALUE;
			ArrayList<PathCell> desirableCandidates = new ArrayList<PathCell>();
			//find the lowest effort available
			for (Iterator<PathCell> iterator = candidates.iterator(); iterator.hasNext();)
			{
				PathCell cell = iterator.next();
				if (cell.getValue(false) < minDist)
				{
					minDist = cell.getValue(false);
				}
			}
			//get all candidates with that effort level
			for (Iterator<PathCell> iterator = candidates.iterator(); iterator.hasNext();)
			{
				PathCell cell = iterator.next();
				if (cell.getValue(false) == minDist) desirableCandidates.add(cell);
			}
			
			//find the candidate cell of the lowest available effort that is closest to the destination
			minDist = Integer.MAX_VALUE;
			PathCell candidate = null;
			for (Iterator<PathCell> iterator = desirableCandidates.iterator(); iterator.hasNext();)
			{
				PathCell cell = iterator.next();
				int distance = distance(cell);
				if (distance < minDist)
				{
					minDist = distance;
					candidate = cell;
				}
			}
			
			//if this cell is the destination point, complete the path, otherwise mark any new candidates
			if (candidate.getCell().getPosition().similar(to))
			{
				complete(candidate);
				return true; //report that progress has been made in this step
			}
			else
			{
				Cell[] cells = candidate.getCell().getConnectedCells();
				for (int index = 0; index < cells.length; index++)
				{
					PathCell pathCell = new PathCell(this, cells[index], candidate);
					//if this cell is traversable, then mark it as a candidate, otherwise mark it as impassible
					if (!checkCell(cells[index]) && pathCell.traversable()) candidates.add(pathCell);
					else if (!pathCell.traversable()) impassible.add(pathCell);
				}
				//mark this candidate as checked, and remove it from the candidate list
				checked.add(candidate);
				candidates.remove(candidate);
				return true; //report that progress has been made in this step
			}
		}
		
		//if there are no remaining candidates and the destination has been reached, then complete a path leading to the checked cell closest to the destination
		if (candidates.size() == 0 && !complete)
		{
			PathCell destination = null;
			int minDist = Integer.MAX_VALUE;
			for (Iterator<PathCell> iterator = checked.iterator(); iterator.hasNext();)
			{
				PathCell cell = iterator.next();
				int distance = distance(cell);
				if (distance < minDist)
				{
					minDist = distance;
					destination = cell;
				}
			}
			complete(destination);
			return true; //report that progress has been made in this step (sort of)
		}
		
		return false; //report that progress has not been made in this step
	}
	
	private final int distance(PathCell cell)
	{
		return to.distance(cell.getCell().getPosition());
	}
	
	public final boolean containsCell(Cell cell)
	{
		for (Iterator<PathCell> iterator = path.iterator(); iterator.hasNext();)
		{
			if (iterator.next().getCell().equals(cell)) return true;
		}
		return false;
	}
	
	public final PathCell[] getRoute()
	{
		return path.toArray(new PathCell[path.size()]);
	}
	
	public final PathCell[] getCandidates()
	{
		return candidates.toArray(new PathCell[candidates.size()]);
	}
	
	public final PathCell[] getImpassibles()
	{
		return impassible.toArray(new PathCell[impassible.size()]);
	}
	
	public final PathCell[] getChecked()
	{
		return checked.toArray(new PathCell[checked.size()]);
	}
	
	private final void complete(PathCell destination)
	{
		complete = true;
		ArrayList<PathCell> refinedPath = new ArrayList<PathCell>();
		path.clear();
		//build up the path
		int distance = 0;
		while (destination.getSource() != null)
		{
			destination.reverseDistance = distance;
			path.add(destination);
			destination = destination.getSource();
			distance++;
		}
		destination.reverseDistance = distance;
		path.add(destination);
		
		//shorten any sections that are unnecessarily long
		distance = 0;
		for (int index = 0; index < path.size(); index++)
		{
			PathCell cell = path.get(index);
			if (index == path.size() - 1) refinedPath.add(cell);
			else
			{
				boolean shortened = false;
				/*
				for (int subIndex = path.size() - 1; subIndex > index; subIndex--)
				{
					Trace trace = new Trace(this, cell, path.get(subIndex));
					if (trace.success)
					{
						for (Iterator<PathCell> iterator = trace.line.iterator(); iterator.hasNext();) refinedPath.add(iterator.next());
						index = subIndex;
						shortened = true;
						break;
					}
				}
				*/
				if (!shortened) refinedPath.add(cell);
			}
		}
		
		//reverse the path to be in the right order
		path.clear();
		for (int index = refinedPath.size() - 1; index >= 0; index--)
		{
			path.add(refinedPath.get(index));
		}
	}
	
	private final boolean checkCell(Cell cell)
	{
		for (Iterator<PathCell> iterator = candidates.iterator(); iterator.hasNext();)
		{
			if (iterator.next().getCell().equals(cell)) return true;
		}
		for (Iterator<PathCell> iterator = impassible.iterator(); iterator.hasNext();)
		{
			if (iterator.next().getCell().equals(cell)) return true;
		}
		for (Iterator<PathCell> iterator = checked.iterator(); iterator.hasNext();)
		{
			if (iterator.next().getCell().equals(cell)) return true;
		}
		return false;
	}
	
	public final void fullProcess()
	{
		while(true)
		{
			if (!step()) break;
		}
	}
	
	public final Region getRegion()
	{
		return region;
	}
	
	public final boolean isComplete()
	{
		return complete;
	}
	
	public final boolean validate()
	{
		if (complete)
		{
			for (Iterator<PathCell> iterator = path.iterator(); iterator.hasNext();)
			{
				if (!iterator.next().getCell().stillConsistent()) complete = false;
			}
			return true;
		}
		else
		{
			for (Iterator<PathCell> iterator = candidates.iterator(); iterator.hasNext();) if (!iterator.next().getCell().stillConsistent()) return false;
			for (Iterator<PathCell> iterator = impassible.iterator(); iterator.hasNext();) if (!iterator.next().getCell().stillConsistent()) return false;
			for (Iterator<PathCell> iterator = checked.iterator(); iterator.hasNext();) if (!iterator.next().getCell().stillConsistent()) return false;
			return true;
		}
	}
	
	public final void reset()
	{
		checked.clear();
		candidates.clear();
		impassible.clear();
		complete = false;
		candidates.add(new PathCell(this, region.getCell(from), null));
	}
	
	@SuppressWarnings("unused")
	private class Trace
	{
		ArrayList<PathCell> line;
		boolean success;
		
		Trace(Path path, PathCell start, PathCell stop)
		{
			success = false;
			line = new ArrayList<PathCell>();
			
			Vector3D slope = new Vector3D(start.getCell().getPosition()).slopeTo(new Vector3D(stop.getCell().getPosition()));
			Vector3D position = new Vector3D(start.getCell().getPosition());
			position.add(slope);
			System.out.println(slope);
			PathCell lastCell = start;
			VectorInt3D intPosition = new VectorInt3D(position);
			while (!intPosition.similar(stop.getCell().getPosition()))
			{
				PathCell cell = new PathCell(path, region.getCell(intPosition), lastCell);
				if (!cell.getCell().equals(lastCell.getCell()))
				{
					if (cell.traversable()) line.add(cell);
					else break;
				}
				position.add(slope);
				intPosition.set(position);
				if (intPosition.similar(stop.getCell().getPosition())) success = true;
			}
		}
	}
}