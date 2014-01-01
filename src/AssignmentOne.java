import lejos.nxt.Button;
import lejos.nxt.comm.RConsole;


public class AssignmentOne{
	
	public static WorldMap worldMap;
	public static Robot robot;
	
	public static void main(String[] args) throws InterruptedException
	{
		// Wait for bluetooth/usb connection to computer.
		RConsole.open();

		int arenaLength = 200;
		int arenaWidth = 150;
		int cellSize = 25;
		
		// Set up the world map.
		// Length, width, cellHeight, cellWidth.
		worldMap = new WorldMap(arenaLength, arenaWidth, cellSize, cellSize);

		// Set up the robot and pass it the world map.
		robot = new Robot(worldMap);
		
		// Wait to start
		System.out.println("Press any button to start");
		Button.waitForAnyPress();
		Output.clear();
		
		BumperMonitor bm = new BumperMonitor(robot);
		bm.start();
		
		// Main robot loop.
		// Keeps going till fully explored.
		while(!robot.getMap().fullyExplored())
		{
			if(!Robot.takecontrol){
				// Print the map before every movement.
				Output.printMapLCD(robot);
				// Check for obstacles to the left, right and front of robot.
				robot.checkSurroundings();
				// Choose a direction to move in.
				Robot.Direction directionChosen = robot.chooseACell();
				// Turn in the chosen direction.
				robot.turnRobotToFace(directionChosen);
				// Move forwards.
				robot.moveForwards();	
			}
		}
		
		// Print out the finished world map.
		Output.println("Final world map");
		Output.printMapLCD(robot);
		
		/* 
			Wait before ending.
			This is done because the world map kept not being 
			printed correctly for some reason and this fixes it.
		*/
		RConsole.println("\nPress any button to end.");
		Button.waitForAnyPress();
		RConsole.close();
	}
}