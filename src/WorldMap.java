public class WorldMap{
	
	private int visitCount[][];
	private int occupancyCount[][];
	private int yCells;
	private int xCells;
	
	public WorldMap(int arenaLength, int arenaWidth, int cellLength, int cellWidth){
		yCells = arenaWidth / cellWidth;
		xCells = arenaLength / cellLength;
		visitCount = new int[xCells][yCells];
		occupancyCount = new int[xCells][yCells];
	}
	
	public void visitCell(int x, int y){
		visitCount[x][y] += 1;
	}
	
	public void cellUnoccupied(int x, int y){
		occupancyCount[x][y] += 1;
	}
	
	public void cellOccupied(int x, int y){
		occupancyCount[x][y] -= 1;
	}
	
	public float getOccupancyProbabilityCell(int row, int column) {
		int occupancy = this.occupancyCount[row][column];
		int visit = this.visitCount[row][column];
		
		return (occupancy + visit) / (visit * 2);
	}
	
	public float[][] getOccupancyProbabilityGrid(){
		float occupancyProbabilityGrid[][] = new float[xCells][yCells];
		
		for(int i = 0; i < xCells; i++){
			for(int j = 0; i < yCells; j++)
			{
				occupancyProbabilityGrid[i][j] = getOccupancyProbabilityCell(i, j);
			}
		}
		
		return occupancyProbabilityGrid;
	}
	
	public boolean fullyExplored(){
		for(int i = 0; i < xCells; i++){
			for(int j = 0; i < yCells; j++){
				if(visitCount[i][j] == 0)
					return false;
			}
		}
		
		return true;
	}
}