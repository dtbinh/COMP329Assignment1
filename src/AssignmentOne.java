import lejos.nxt.Button;
import lejos.nxt.LCD;
import lejos.nxt.Motor;
import lejos.nxt.SensorPort;
import lejos.nxt.TouchSensor;
import lejos.nxt.UltrasonicSensor;
import lejos.nxt.addon.ColorHTSensor;
import lejos.robotics.localization.OdometryPoseProvider;
import lejos.robotics.navigation.DifferentialPilot;
import java.util.Random;

public class AssignmentOne{
	
	static TouchSensor leftBump;
	static TouchSensor rightBump;
	static UltrasonicSensor us;
	static ColorHTSensor cs;
	static int startingColorID;
	static WorldMap worldMap;
	
	public static void main(String[] args) throws InterruptedException{
		// Set up the differential pilot for our robot
		DifferentialPilot dp = new DifferentialPilot(3.245, 22, Motor.B, Motor.C);
		// Create a pose provider and link it to the differential pilot
		OdometryPoseProvider opp = new OdometryPoseProvider(dp);
		
		worldMap = new WorldMap(200, 150, 25, 25);

		rightBump = new TouchSensor(SensorPort.S1);
		leftBump = new TouchSensor(SensorPort.S2);
		us = new UltrasonicSensor(SensorPort.S3);
		cs = new ColorHTSensor(SensorPort.S4);

		// Wait to start
		System.out.println("Press any button to start");
		Button.waitForAnyPress();
		LCD.clear();
		
		
		while(!worldMap.fullyExplored())
		{
			chooseACell(dp);
			moveForwards(dp);
		}
	}
	
	public static void chooseACell(DifferentialPilot dp) throws InterruptedException{
		boolean forwards = false;
		boolean left = false;
		boolean right = false;
		
		if(us.getRange() < 30.0)
			forwards = true;
		
		turnHead(true, 90);
		
		if(us.getRange() < 30.0)
			left = true;
		
		turnHead(false, 180);
		
		if(us.getRange() < 30.0)
			right = true;
		
		turnHead(true, 90);
		
		if(forwards && left && right){
			dp.rotate(180);
			return;
		}
		
		Random rng = new Random();
		
		while(true)
		{
			int roll = rng.nextInt(3);
			if(roll == 0 && !forwards)
				break;
			if(roll == 1 && !left){
				dp.rotate(-90);
				break;
			}
			if(roll == 2 && !right){
				dp.rotate(90);
				break;
			}	
		}
	}
	
	
	// Forward turns right
	// Backward turns left
	public static void turnHead(boolean Left, int degrees) throws InterruptedException
	{
		int millisecondsPerDegree = 20;
		
		if(Left)
			Motor.A.backward();
		else
			Motor.A.forward();
		Thread.sleep(degrees * millisecondsPerDegree);
		Motor.A.stop();
	}
	
	public static void moveForwards(DifferentialPilot dp){
		dp.travel(20);
	}
}