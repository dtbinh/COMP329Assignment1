
public class WorldMap{
	
	private int visitCount[][];
	private int occupancyCount[][];
	private int rows;
	private int columns;
	
	/**
		* Constructor for world map.
		* cellLength and cellWidth should always be 25 for now as variable movement
		* distances are unimplemented.
		*
		* @param 	arenaLength 	 Length of the arena
		* @param 	arenaWidth		 Width of the arena
		* @param 	cellLength 		 Length of individual cells.
		* @param 	cellWidth 		 Width of individual cells.
	*/
	public WorldMap(int arenaLength, int arenaWidth, int cellLength, int cellWidth){
		rows = arenaLength / cellLength; //8
		columns = arenaWidth / cellWidth;   //6
		visitCount = new int[columns][rows];
		occupancyCount = new int[columns][rows];

		for(int x = 0; x < columns; x++)
		{
			for(int y = 0; y < rows; y++)
			{
				visitCount[x][y] = 0;
				occupancyCount[x][y] = 0;
			}
		}
	}
	
	/**
		* columns getter.
		*
		* @return 	How many columns the world has.
	*/
	public int getColumnLength() {
		return columns;
	}

	/**
		* rows getter.
		*
		* @return 	How many rows the world has.
	*/
	public int getRowLength() {
		return rows;
	}
	
	/**
		* Marks a cell as having been visited once more.
		*
		* @param 	x 	Column to update visit count of.
		* @param 	y	Row to update visit count of.
	*/
	public void visitCell(int x, int y)
	{
		if(x >= getColumnLength() || x < 0 || y >= getRowLength() || y < 0) {
			return;
		}
		
		visitCount[x][y] += 1;
	}

	/**
		* Occupancy count getter for individual cells
		*
		* @param 	x 	Column to get occupancy count from.
		* @param 	y	Row to get occupancy count from.
		* @return 	The occupancy count of the cell.
	*/
	public int getOccupancyCount(int x, int y)
	{
		return occupancyCount[x][y];
	}

	/**
		* Visit count getter for individual cells
		*
		* @param 	x 	Column to get visit count from.
		* @param 	y	Row to get visit count from.
		* @return 	How many times the cell has been visited.
	*/
	public int getVisitCount(int x, int y)
	{
		return visitCount[x][y];
	}

	/**
		* Checks whether a cell has been visited or not.
		*
		* @param 	x 	Column to check.
		* @param 	y	Row to check.
		* @return 	Whether the cell has been visited or not.
	*/
	public boolean isCellVisited(int x, int y) 
	{
		if(visitCount[x][y] > 0)
		{
			return true;
		}
		else 
		{
			return false;
		}
	}

	/**
		* Checks whether a cell is thought to be occupied or not.
		* Returns true if the probability is 50% or higher.
		*
		* @param 	x 	Column to check.
		* @param 	y	Row to check.
		* @return 	Whether the cell is occupied or not.
	*/
	public boolean isCellOccupied(int x, int y)
	{
		if(getOccupancyProbabilityCell(x, y) >= 0.5)
		{
			return true;
		}
		else
		{
			return false;
		}
	}
	
	/**
		* Checks whether a cell in a given cardinal direction from a co-ordinate is
		* out of bounds..
		*
		* @param 	direction 	Cardinal direction to check in.
		* @param 	x 			X co-ordinate.
		* @param 	y			Y co-ordinate.
		* @return   Whether the cell is out of bounds or not.
	*/
	public boolean outOfBounds(Robot.Direction direction, int x, int y)
	{
		int xCoord = x;
		int yCoord = y;
		
		if(direction == Robot.Direction.FORWARDS)
		{
			yCoord += 1;
		} 
		else if(direction == Robot.Direction.BACKWARDS)
		{
			yCoord -= 1;
		} 
		else if(direction == Robot.Direction.LEFT)
		{
			xCoord += 1;
		} 
		else if(direction == Robot.Direction.RIGHT)
		{
			xCoord -= 1;
		}
		
		if(0 > xCoord || xCoord >= getColumnLength() || 0 > yCoord || yCoord >= getRowLength())
		{
			return true;
		}
		else
		{
			return false;
		}
	}

	/**
		* Decrements a cell's occupancy count by 1.
		* Also increments the cells visit count.
		*
		* @param 	x 			X co-ordinate.
		* @param 	y			Y co-ordinate.
	*/
	public void cellUnoccupied(int x, int y){
		if(x >= getColumnLength() || x < 0 || y >= getRowLength() || y < 0) {
			return;
		}
		
		occupancyCount[x][y] -= 1;
		visitCell(x, y);
	}

	/**
		* Increments a cell's occupancy count by 1.
		* Also increments the cells visit count.
		*
		* @param 	x 			X co-ordinate.
		* @param 	y			Y co-ordinate.
	*/
	public void cellOccupied(int x, int y){
		if(x >= getColumnLength() || x < 0 || y >= getRowLength() || y < 0) {
			return;
		}
		
		occupancyCount[x][y] += 1;
		visitCell(x, y);
	}

	/**
		* Gets the occupancy probability of a single cell.
		* Returns 1 (100% chance of occupancy) for out of bounds cells.
		* Returns -1 for unvisited cells.
		*
		* @param 	column 	X co-ordinate.
		* @param 	row		Y co-ordinate.
		* @return 	Probability the given cell is occupied.
	*/
	public float getOccupancyProbabilityCell(int column, int row) {
		try
		{
			int occupancy = getOccupancyCount(column, row);
			int visit = getVisitCount(column, row);
			
			if(visit == 0)
				return -1;
			
			return (occupancy + visit) / (visit * 2);
		}
		catch(ArrayIndexOutOfBoundsException e)
		{
			return 1;
		}
	}
	
	/**
		* Returns a 2d array of floats that corespond to the world map's occupancy probabilities.
		*
		* @return 2d array of occupancy probabilities.
	*/
	public float[][] getOccupancyProbabilityGrid(){
		float occupancyProbabilityGrid[][] = new float[columns][rows];
		
		for(int i = 0; i < columns; i++)
		{
			for(int j = 0; i < rows; j++)
			{
				occupancyProbabilityGrid[i][j] = getOccupancyProbabilityCell(i, j);
			}
		}
		
		return occupancyProbabilityGrid;
	}
	
	/**
		* Checks whther the entire map has been explored or not.
		*
		* @return Whether the map has been explored or not.
	*/
	public boolean fullyExplored()
	{
		for(int i = 0; i < columns; i++)
		{
			for(int j = 0; j < rows; j++)
			{
				if(visitCount[i][j] == 0)
				{
					return false;
				}
			}
		}
		
		return true;
	}
	
	/**
		* Returns the percentage of a given section of the map that has been visited.
		*
		* @param 	xStart 	 	Right boundary of section.
		* @param 	xEnd		Left boundary of section.
		* @param 	yStart 		Bottom boundary of section.
		* @param 	yEnd		Top boundary of section.
		* @return   Percentage of the section that has been visited.
	*/
	public double getSectionUnvisitedPercentage(int xStart, int xEnd, int yStart, int yEnd)
	{
		double sectionSize = getSectionSize(xStart, xEnd, yStart, yEnd);
		
		// There are no cells unvisited in an empty section.
		if(sectionSize == 0)
			return 0.0;
		
		double sectionUnvisitCount = unvisited(xStart, xEnd, yStart, yEnd);
		
		return sectionUnvisitCount / sectionSize;
	}

	/**
		* Returns the count of unvisited cells in a section
		*
		* @param 	xStart 	 	Right boundary of section.
		* @param 	xEnd		Left boundary of section.
		* @param 	yStart 		Bottom boundary of section.
		* @param 	yEnd		Top boundary of section.
		* @return   Total unvisited cells in the section.
	*/
	public double unvisited(int xStart, int xEnd, int yStart, int yEnd)
	{
		double visitCount = 0;
		
		for(int x = xStart; x < xEnd; x++)
		{
			for(int y = yStart; y < yEnd; y++)
			{
				if(!isCellVisited(x, y))
					visitCount += 1;
			}
		}
		
		return visitCount;
	}

	/**
		* Returns the size of a section
		*
		* @param 	xStart 	 	Right boundary of section.
		* @param 	xEnd		Left boundary of section.
		* @param 	yStart 		Bottom boundary of section.
		* @param 	yEnd		Top boundary of section.
		* @return   Total cells in a section.
	*/
	public double getSectionSize(int xStart, int xEnd, int yStart, int yEnd)
	{
		return (xEnd - xStart) * (yEnd - yStart);
	}
}