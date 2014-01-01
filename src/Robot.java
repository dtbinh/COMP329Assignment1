
import lejos.nxt.Motor;
import lejos.nxt.SensorPort;
import lejos.nxt.TouchSensor;
import lejos.nxt.UltrasonicSensor;
import lejos.nxt.addon.ColorHTSensor;
import lejos.nxt.comm.RConsole;
import lejos.robotics.localization.OdometryPoseProvider;
import lejos.robotics.navigation.DifferentialPilot;
import lejos.robotics.navigation.Pose;

public class Robot {
	// robot's location on the grid
	private int currentRow;
	private int currentColumn;
	
	// sensors
	private static TouchSensor leftBump;
	private static TouchSensor rightBump;
	private static UltrasonicSensor us;
	private static ColorHTSensor cs;
	
	//pilot the robot
	static DifferentialPilot dp;
	//track the distance each time when our robot moves forward
	static OdometryPoseProvider opp;
	
	//the following boolean value states whether the bumperMonitor should take control 
	static boolean takecontrol = false;
	
	// Enumerator used for cardinal direction and robot's direction.
	public static enum Direction { LEFT, RIGHT, FORWARDS, BACKWARDS }
	

	// WorldMap is composition
	private static WorldMap worldMap;
	private Compass compass;
	
	static int goalColumn;
	static int goalRow;

	// constructor
	public Robot(WorldMap map) {
		currentRow = 0;
		currentColumn = 0;
		this.worldMap = map;
		compass = new Compass(Direction.FORWARDS, Direction.FORWARDS);
		// Set up the differential pilot for our robot
		dp = new DifferentialPilot(3.245, 22, Motor.B, Motor.C);
		// Set up the Odometry Pose Provider for our robot
		opp = new OdometryPoseProvider(dp);
		// set up bumpers
		rightBump = new TouchSensor(SensorPort.S1);
		leftBump = new TouchSensor(SensorPort.S2);
		// ultrasonic sensor
		us = new UltrasonicSensor(SensorPort.S3);
		Motor.A.setSpeed(900);
		// color sensor
		cs = new ColorHTSensor(SensorPort.S4);
	}

	/**
		* Robot's current row getter.
		*
		* @return	Current row of robot.
	*/
	public int getCurrentRow() {
		return currentRow;
	}

	/**
		* Robot's current column getter.
		*
		* @return	Current column of robot.
	*/
	public int getCurrentColumn() {
		return currentColumn;
	}
	
	/**
		* WorldMap getter.
		*
		* @return	World Map that robot is using.
	*/
	public WorldMap getMap(){
		return worldMap;
	}

	/**
		* Sets robot's current row.
		* Does not allow robot location to be set outside arena.
		*
		* @param 	row 	Robot's new row.
	*/
	public void setCurrentRow(int row) {
		// if row is not negative or beyond the maximum index
		if (row >= 0 && row < worldMap.getRowLength())
		{
			currentRow = row;
		}
	}

	/**
		* Sets robot's current column.
		* Does not allow robot location to be set outside arena.
		*
		* @param 	column 	Robot's new column.
	*/
	public void setCurrentColumn(int column) {
		// if column is not negative or beyond the maximum index
		if (column >= 0 && column < worldMap.getColumnLength()) 
		{
			currentColumn = column;
		}
		
	}

	/**
		* Sets robot's current column and row.
		* Does not allow robot location to be set outside arena.
		*
		* @param 	column 	Robot's new column.
		* @param 	row 	Robot's new row.
	*/
	public void setCurrentLocation(int column, int row)
	{
		setCurrentColumn(column);
		setCurrentRow(row);
	}
	
	/**
		* Turns the robot's head and checks front, left and right 
		* of robot for obstacles. Records what it finds in the world map.
		* This includes whether the cells it looks into are unoccupied.
		* Range checked: =  25cm
	*/
	public void checkSurroundings() throws InterruptedException
	{
		// range to look
		double range = 25.0;
		boolean forwards;
		boolean left;
		boolean right;
		
		// store current values to use later.
		int currentColumn = getCurrentColumn();
		int currentRow = getCurrentRow();
		
		// Set current cell as unoccupied. It has to be since we're in it.
		worldMap.cellUnoccupied(currentColumn, currentRow);
		
		// Look dead ahead.
		forwards = checkForwards();

		// Look left.
		turnHead(Direction.LEFT);
		if(us.getRange() < range)
		{
			left = true;
		} 
		else 
		{
			left = false;
		}

		// Look right.
		turnHead(Direction.RIGHT);
		if(us.getRange() < range)
		{
			right = true;
		} 
		else 
		{
			right = false;
		}

		// Look forwards
		turnHead(Direction.FORWARDS);
		
		// Update correct parts of grid.
		if(compass.getDirection() == Direction.FORWARDS)
		{
			if(forwards)
				worldMap.cellOccupied(currentColumn, currentRow + 1);
			else
				worldMap.cellUnoccupied(currentColumn, currentRow + 1);
			
			if(left)
				worldMap.cellOccupied(currentColumn + 1, currentRow);
			else
				worldMap.cellUnoccupied(currentColumn + 1, currentRow);
			
			if(right)
				worldMap.cellOccupied(currentColumn - 1, currentRow);
			else
				worldMap.cellUnoccupied(currentColumn - 1, currentRow);
		} 
		else if(compass.getDirection() == Direction.RIGHT)
		{
			if(forwards)
				worldMap.cellOccupied(currentColumn - 1, currentRow);
			else
				worldMap.cellUnoccupied(currentColumn - 1, currentRow);
			
			if(left)
				worldMap.cellOccupied(currentColumn, currentRow + 1);
			else
				worldMap.cellUnoccupied(currentColumn, currentRow + 1);
			
			if(right)
				worldMap.cellOccupied(currentColumn, currentRow - 1);
			else
				worldMap.cellUnoccupied(currentColumn, currentRow - 1);
		} 
		else if(compass.getDirection() == Direction.BACKWARDS)
		{
			if(forwards)
				worldMap.cellOccupied(currentColumn, currentRow - 1);
			else
				worldMap.cellUnoccupied(currentColumn, currentRow - 1);
			
			if(left)
				worldMap.cellOccupied(currentColumn - 1, currentRow);
			else
				worldMap.cellUnoccupied(currentColumn - 1, currentRow);
			
			if(right)
				worldMap.cellOccupied(currentColumn + 1, currentRow);
			else
				worldMap.cellUnoccupied(currentColumn + 1, currentRow);
		} 
		else if(compass.getDirection() == Direction.LEFT)
		{
			if(forwards)
				worldMap.cellOccupied(currentColumn + 1, currentRow);
			else
				worldMap.cellUnoccupied(currentColumn + 1, currentRow);
			
			if(left)
				worldMap.cellOccupied(currentColumn, currentRow - 1);
			else
				worldMap.cellUnoccupied(currentColumn, currentRow - 1);
			
			if(right)
				worldMap.cellOccupied(currentColumn, currentRow + 1);
			else
				worldMap.cellUnoccupied(currentColumn, currentRow + 1);
		}
	}
	
	/**
	 * check whether there is an obstacle forward
	 * */
	private boolean checkForwards() {
		boolean forward;
		Motor.A.rotate(338);
		if(us.getRange() < 27)
		{
			forward =  true;
		} 
		else
		{
			forward =  false;
		}
		Motor.A.rotate(-676);
		if(us.getRange() < 27)
		{
			forward =  true;
		} 
		else
		{
			forward =  false;
		}
		Motor.A.rotate(338);
		if(us.getRange() < 25)
		{
			forward =  true;
		} 
		else
		{
			forward =  false;
		}
		return forward;
	}

	/**
		* Movement algorithm.
		* Called every time the robot moves to choose it's new direction to head.
		*
		* Splits the arena into four quadrants.
		* It then works out the percentage of each quadrant that is unvisited.
		* Each cardinal direction (north, south, east and west) is then given a priority
		* based upon the two quadrants that contain cells in it's direction.
		* I.E. 
		* North gets the priorities of northwest and northeast. 
		* South gets the priorities of southwest and southeast.
		* East gets the priorities of northeast and southeast. 
		* West gets the priorities of northwest and southwest.
		*
		* The cardinal direction with the highest priority from these additions is then
		* selected as the direction to head in.
		* Obstacles are taken into account. A higher priority direction will be ignored if
		* there is an obstacle.
		*
		*
		* Problem:
		* This algorithm suffers the same problem as the system that functions like a gradient,
		* where the robot is repelled by obstacles and drawn by the goal.
		* It will get stuck down alleys.
		* This was not seen as a problem due to the scattered nature of the arena's obstacles.
		*
		* @return Robot.Direction chosen to head in.
	*/
	public Direction chooseACell() throws InterruptedException
	{	
		// The boundaries for the quadrants.
		int xBoundary = getCurrentColumn();
		int yBoundary = getCurrentRow();
		int xMax = worldMap.getColumnLength();
		int yMax = worldMap.getRowLength();
		
		// Get the unvisited percentages for the four quadrants.
		double seUnvisitedPercentage = worldMap.getSectionUnvisitedPercentage(0, xBoundary, 0, yBoundary);
		double swUnvisitedPercentage = worldMap.getSectionUnvisitedPercentage(xBoundary , xMax, 0, yBoundary);
		double neUnvisitedPercentage = worldMap.getSectionUnvisitedPercentage(0, xBoundary, yBoundary, yMax);
		double nwUnvisitedPercentage = worldMap.getSectionUnvisitedPercentage(xBoundary , xMax, yBoundary, yMax);
		
		// Work out the cardinal direction priorities from the unvisited percentages.
		double northPriority = nwUnvisitedPercentage + neUnvisitedPercentage;
		double southPriority = swUnvisitedPercentage + seUnvisitedPercentage;
		double eastPriority = neUnvisitedPercentage + seUnvisitedPercentage;
		double westPriority = nwUnvisitedPercentage + swUnvisitedPercentage;
		
		// Put the priorities in an array.
		double[] priorities = new double[4];
		priorities[0] = northPriority;
		priorities[1] = southPriority;
		priorities[2] = eastPriority;
		priorities[3] = westPriority;

		// Debugging output for priorities.
		// Output.println("NW = " + nwUnvisitedPercentage);
		// Output.println("NE = " + neUnvisitedPercentage);
		// Output.println("SW = " + swUnvisitedPercentage);
		// Output.println("SE = " + swUnvisitedPercentage);
		// 
		// Output.println("N = " + northPriority);
		// Output.println("S = " + southPriority);
		// Output.println("E = " + eastPriority);
		// Output.println("W = " + westPriority);
		
		int indexOfHighestPriority;
		

		do
		{
			// Grab the index of the highest priority cardinal direction.
			indexOfHighestPriority = returnGreatest(priorities);
			
			// If the priority you've grabbed is -1, then all priorities are exhausted.
			// Break the loop.
			if(priorities[indexOfHighestPriority] == -1)
				break;
			
			RConsole.print("" + indexOfHighestPriority);
		
			// Check if the direction is occupied.
			// If it is set the priority of that direction to -1 and move on with the loop.
			// If it is not then return that direction as the direction chosen.
			if(checkDirectionOccupied(indexToDirection(indexOfHighestPriority)))
			{
				RConsole.println(" is occupied, setting to -1.");
				priorities[indexOfHighestPriority] = -1;
			}
			else
			{
				return indexToDirection(indexOfHighestPriority);
			}
		} while(true);
		
		// If we get here then the robot is completely surrounded and we still haven't explored 
		// the entire map.
		// The only option is to ram forwards.
		return compass.getDirection();
	}
	
	/**
		* Gets the index of the highest value in an array of priorities.
		*
		* @param 	priorities 	An array of priorities.
		* @return 	The index of the highest priority.
	*/
	public int returnGreatest(double priorities[])
	{
		double highest = priorities[0];
		int position = 0;

		for (int i = 1; i < priorities.length; i++) {
			if ( priorities[i] > highest ) {
				highest = priorities[i];
				position = i;
			}
		}
		
		return position;
	}

	/**
		* Translates an index back into the cardinal direction it represents.
		* 0 = north;
		* 1 = south;
		* 2 = east;
		* 3 = west;
		*
		* @param 	index 	The index that requires conversion.
		* @return 	The Direction that the given index refers to.
	*/
	private Direction indexToDirection(int index)
	{
		if(index == 0)
			return Direction.FORWARDS;
		else if(index == 1)
			return Direction.BACKWARDS;
		else if(index == 2)
			return Direction.RIGHT;
		else
			return Direction.LEFT;
	}

	/**
		* Takes in a cardinal direction and returns whether
		* the cell in that direction is occupied or not.
		*
		* @param 	direction 	Direction to check.
		* @return 	Whether the direction is occupied or not.
	*/
	public boolean checkDirectionOccupied(Direction direction)
	{
		if(direction == Direction.FORWARDS)
		{
			return worldMap.isCellOccupied(getCurrentColumn(), getCurrentRow() + 1);
		} 
		else if(direction == Direction.BACKWARDS)
		{
			return worldMap.isCellOccupied(getCurrentColumn(), getCurrentRow() - 1);
		} 
		else if(direction == Direction.RIGHT)
		{
			return worldMap.isCellOccupied(getCurrentColumn() - 1, getCurrentRow());
		} 
		else
		{
			return worldMap.isCellOccupied(getCurrentColumn() + 1, getCurrentRow());
		}
	}

	/**
		* Turns the head to face the direction given.
		*
		* @param 	direction The direction the head needs to turn to face, relative to the robot's body.
	*/
	public void turnHead(Direction direction) throws InterruptedException{
		Motor.A.rotate(compass.turnHeadDegrees(direction));
	}

	/**
		* Prints to the RConsole the cardinal direction the robot is facing.
		* String is of form:
		* I am now facing: DIRECTION
	*/
	private void printDirection() {
		String direction = "";
		
		if(compass.getDirection() == Direction.FORWARDS){
			direction = "forwards";
		} else if(compass.getDirection() == Direction.BACKWARDS){
			direction = "backwards";
		} else if(compass.getDirection() == Direction.LEFT){
			direction = "left";
		} else if(compass.getDirection() == Direction.RIGHT){
			direction = "right";
		}
		
		RConsole.println("\nI am now facing: " + direction);
	}

	/**
		* Moves the robot forwards one cell.
		* Also updates the robot's current location.
		* Also updates the world map's occupancy and visit counts 
		* to reflect that the cell entered has been examined.
	*/
	public void moveForwards()
	{
		takecontrol = false;
		goalColumn = getCurrentColumn();
		goalRow = getCurrentRow();
		int cellsize = 24;
		
		if(compass.getDirection() == Direction.LEFT)
		{
			goalColumn += 1;
		} 
		else if(compass.getDirection() == Direction.RIGHT)
		{
			goalColumn -= 1;
		} 
		else if(compass.getDirection() == Direction.FORWARDS)
		{
			goalRow += 1;
		} 
		else if(compass.getDirection() == Direction.BACKWARDS)
		{
			goalRow -= 1;
		}
		opp.setPose(new Pose(0,0,0));
		if(us.getRange()<25){
			//the length of 8th row is less than 25
			dp.travel(cellsize-5);
			RConsole.println("aproaching to the smaller cell");
		}else{
			dp.travel(cellsize);
		}
		if(!takecontrol){
			if(us.getRange()<5){
				dp.travel(-5);
			}
			setCurrentLocation(goalColumn, goalRow);
			worldMap.cellUnoccupied(goalColumn, goalRow);	
		}
	}
	
	/**
		* Returns whether the either bumper has been pressed.
		*
		* @return Whether the bumper has been pressed or not.
	*/
	public boolean bumperPressed()
	{
		if(leftBump.isPressed() && rightBump.isPressed())
			return true;
		else
			return false;
	}

	/**
		* Turns robot to face cardinal direction passed to it.
		* Updates compass to keep track of new direction.
		*
		* @param directionChosen Direction robot should face.
	*/
	public void turnRobotToFace(Direction directionChosen) 
	{
		// Ugly if statements to decide which way to face.
		if(compass.getDirection() == Direction.LEFT)
		{
			if(directionChosen == Direction.RIGHT)
			{
				dp.rotate(180);
				compass.robotTurned(Direction.BACKWARDS);
			}
			else if(directionChosen == Direction.BACKWARDS)
			{
				dp.rotate(-90);
				compass.robotTurned(Direction.LEFT);
			}
			else if(directionChosen == Direction.FORWARDS)
			{
				dp.rotate(90);
				compass.robotTurned(Direction.RIGHT);
			}
		}
		else if(compass.getDirection() == Direction.FORWARDS)
		{
			if(directionChosen == Direction.RIGHT)
			{
				dp.rotate(90);
				compass.robotTurned(Direction.RIGHT);
			}
			else if(directionChosen == Direction.BACKWARDS)
			{
				dp.rotate(180);
				compass.robotTurned(Direction.BACKWARDS);
			}
			else if(directionChosen == Direction.LEFT)
			{
				dp.rotate(-90);
				compass.robotTurned(Direction.LEFT);
			}
		}
		else if(compass.getDirection() == Direction.BACKWARDS)
		{
			if(directionChosen == Direction.RIGHT)
			{
				dp.rotate(-90);
				compass.robotTurned(Direction.LEFT);
			}
			else if(directionChosen == Direction.FORWARDS)
			{
				dp.rotate(180);
				compass.robotTurned(Direction.BACKWARDS);
			}
			else if(directionChosen == Direction.LEFT)
			{
				dp.rotate(90);
				compass.robotTurned(Direction.RIGHT);
			}
		}
		else if(compass.getDirection() == Direction.RIGHT)
		{
			if(directionChosen == Direction.FORWARDS)
			{
				dp.rotate(-90);
				compass.robotTurned(Direction.LEFT);
			}
			else if(directionChosen == Direction.LEFT)
			{
				dp.rotate(180);
				compass.robotTurned(Direction.BACKWARDS);
			}
			else if(directionChosen == Direction.BACKWARDS)
			{
				dp.rotate(90);
				compass.robotTurned(Direction.RIGHT);
			}
		}
		
		printDirection();
	}
}