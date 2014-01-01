
import lejos.robotics.localization.OdometryPoseProvider;

public class BumperMonitor extends Thread{
	private Robot robot;
	
	public BumperMonitor(Robot r){
		robot = r;
	}
	
	
	public void run(){
		//always do check
		while(true){
			//if both bumpers have been pressed
			if(robot.bumperPressed()){
				//stop the robot
				Robot.dp.stop();
				Robot.takecontrol = true;
				//make ture that cell is occupied
				robot.getMap().cellOccupied(Robot.goalColumn, Robot.goalRow);
				//get the current OdometryPoseProvider
				OdometryPoseProvider op = Robot.opp;
				float x = op.getPose().getX();
				float y = op.getPose().getY();
				//find the distance
				float distance = Math.max(x, y);
				//go back
				Robot.dp.travel(-distance);
			}
			Robot.takecontrol = false;
		}
	}
	
}
