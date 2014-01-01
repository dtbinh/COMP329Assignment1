import lejos.nxt.LCD;
import lejos.nxt.comm.RConsole;

class Output
{
	/**
		* Takes in a message and outputs it to the LCD screen and the RConsole.
		* Includes a linebreak after the message.
		*
		* @param	message 		The message to be displayed.
	*/
	public static void println(String message)
	{
		System.out.println(message);
		RConsole.println(message);
	}

	/**
		* Takes in a message and outputs it to the LCD screen and the RConsole.
		*
		* @param	message 		The message to be displayed.
	*/
	public static void print(String message)
	{
		RConsole.print(message);
		System.out.print(message);
	}

	/**
		* Prints a new line to the LCD screen and the RConsole.
	*/
	public static void newLine()
	{
		System.out.println();
		RConsole.println("");
	}
	/**
		* Clears the LCD screen.
	*/
	public static void clear()
	{
		LCD.clear();
	}
	
	/**
		* Converts a Robot.Direction enumeration into a string for display.
		*
		* @param	direction 		Robot.Direction to be converted
		* @return 	Converted direction string.
	*/
	public static String directionToString(Robot.Direction direction) {
		String directionString = "";
		
		if(direction == Robot.Direction.FORWARDS){
			directionString = "forwards";
		} else if(direction == Robot.Direction.BACKWARDS){
			directionString = "backwards";
		} else if(direction == Robot.Direction.LEFT){
			directionString = "left";
		} else if(direction == Robot.Direction.RIGHT){
			directionString = "right";
		}
		
		return directionString;
	}
	
	/**
		* Takes in a WorldMap and Robot and prints out a map of the world as the
		* robot currently sees it.
		* Prints an ascii map to the LCD screen and a probability map to the RConsole.
		* eg.
		* LCD map
		* |?|?|?|?|?|?|
		* |?|?|?|?|?|?|
		* |?|?|?|?|?|?|
		* |?|?|?|?|?|?|
		* |?|?|!|?|?|?|
		* |?| | | | |x|
		* |?|?| | | | |
		* |?|?|?| | | |
		* ? = Unexplored cells
		* ! = Location of robot
		* Space = Unoccupied cell
		* x = Occupied cell
		*
		* The probability map replaces all of the above with probabilities.
		* Unexplored cells appear as -1 in the probability map.
		*   
		* @param	currentMap 		The WorldMap to use to obtain the map.
		* @param 	robot 			Robot used to get current location from for displaying !
	*/
	public static void printMapLCD(Robot robot) {
		LCD.clear();
		WorldMap currentMap = robot.getMap();
		for (int y = currentMap.getRowLength() - 1; y >= 0; y--)
		{
			Output.println("");
			for (int x = currentMap.getColumnLength() - 1; x >= 0; x--)
			{		
				RConsole.print("" + currentMap.getOccupancyProbabilityCell(x, y));
				if(robot.getCurrentColumn() == x && robot.getCurrentRow() == y)
				{
					System.out.print("!");
				}
				else if (currentMap.isCellOccupied(x, y))
				{
					System.out.print("X");
				}
				else if(!currentMap.isCellVisited(x, y)) 
				{
					System.out.print("?");
				}
				else
				{
					System.out.print(" ");
				}
				
				Output.print("|");
			}
		}
	}

	/**
		* Similar to printMapLCD except it only prints the occupancy count of the cells to the RConsole.
		* Used for debugging.
		*   
		* @param	currentMap 		The WorldMap to use to obtain the map.
	*/
	public static void printOccupancyGrid(WorldMap currentMap)
	{
		for (int x = currentMap.getColumnLength() - 1; x >= 0; x--)
		{
			for (int y = currentMap.getRowLength() - 1; y >= 0; y--)
			{	
				RConsole.print("" + currentMap.getOccupancyCount(x, y));
				RConsole.print("|");
			}
			RConsole.println("");
		}
	}

	/**
		* Similar to printMapLCD except it only prints the visit count of the cells to the RConsole.
		* Used for debugging.
		*   
		* @param	currentMap 		The WorldMap to use to obtain the map.
	*/
	public static void printVisitGrid(WorldMap currentMap)
	{
		for (int x = currentMap.getColumnLength() - 1; x >= 0; x--)
		{
			for (int y = currentMap.getRowLength() - 1; y >= 0; y--)
			{	
				RConsole.print("" + currentMap.getVisitCount(x, y));
				RConsole.print("|");
			}
			RConsole.println("");
		}
	}
}