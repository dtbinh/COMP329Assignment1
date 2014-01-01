public class Compass {
	
	private Robot.Direction headDirection = Robot.Direction.FORWARDS;
	private Robot.Direction robotDirection = Robot.Direction.FORWARDS;

	/**
		* Compass constructor.
		*
		* @param	head 	Initial orientation of robot's head.
		* @param	robot 	Initial orientation of robot.
	*/
	public Compass(Robot.Direction head, Robot.Direction robot){
		headDirection = head;
		robotDirection = robot;
	}
	
	/**
		* Returns a Robot.Direction that corresponds to the direction the
		* robot's head is facing.
		*
		* @return      the direction the robot's head is facing
	*/
	public Robot.Direction gethead(){
		return headDirection;
	}

	/**
		* Returns a Robot.Direction that corresponds to the direction the
		* robot is facing.
		*
		* @return      the direction the robot is facing
	*/
	public Robot.Direction getDirection(){
		return robotDirection;
	}

	/**
		* Takes in the direction that the robot has turned and updates 
		* the compass' record of which direction the robot is facing.
		* eg. if the robot is facing left and it turns right. It will be facing
		* forwards.
		*
		* @param	direction 		The direction the robot turned
		* @return 	The Robot's new direction.
	*/
	public Robot.Direction robotTurned(Robot.Direction direction){
		if(direction == Robot.Direction.LEFT){
			if(robotDirection == Robot.Direction.LEFT){
				robotDirection = Robot.Direction.BACKWARDS;
			} else if(robotDirection == Robot.Direction.BACKWARDS){
				robotDirection = Robot.Direction.RIGHT;
			} else if(robotDirection == Robot.Direction.RIGHT){
				robotDirection = Robot.Direction.FORWARDS;
			} else {
				robotDirection = Robot.Direction.LEFT;
			}
		} else if(direction == Robot.Direction.RIGHT){
			if(robotDirection == Robot.Direction.LEFT){
				robotDirection = Robot.Direction.FORWARDS;
			} else if(robotDirection == Robot.Direction.BACKWARDS){
				robotDirection = Robot.Direction.LEFT;
			} else if(robotDirection == Robot.Direction.RIGHT){
				robotDirection = Robot.Direction.BACKWARDS;
			} else {
				robotDirection = Robot.Direction.RIGHT;
			}
		} else if(direction == Robot.Direction.BACKWARDS){
			if(robotDirection == Robot.Direction.LEFT){
				robotDirection = Robot.Direction.RIGHT;
			} else if(robotDirection == Robot.Direction.BACKWARDS){
				robotDirection = Robot.Direction.FORWARDS;
			} else if(robotDirection == Robot.Direction.RIGHT){
				robotDirection = Robot.Direction.LEFT;
			} else {
				robotDirection = Robot.Direction.BACKWARDS;
			}
		}

		return robotDirection;
	}

	/**
		* Takes in the direction that you want the robot's head to be facing and returns
		* how many degrees the head motor needs to turn to face in that direction.
		*
		* @param	direction 		The direction you want the robot's head to face.
		* @return 	Degrees required to achieve that direction.
	*/
	public int turnHeadDegrees(Robot.Direction direction) throws InterruptedException{
		int degrees = 0;
		
		if(direction == Robot.Direction.BACKWARDS)
			return 0;
		
		if(direction == Robot.Direction.LEFT){
			if(headDirection == Robot.Direction.FORWARDS)
				degrees = -660;
			else if(headDirection == Robot.Direction.RIGHT)
				degrees = -660 * 2;
		} else if(direction == Robot.Direction.RIGHT){
			if(headDirection == Robot.Direction.FORWARDS)
				degrees = 660;
			else if(headDirection == Robot.Direction.LEFT)
				degrees = 660*2;
		} else if(direction == Robot.Direction.FORWARDS){
			if(headDirection == Robot.Direction.LEFT)
				degrees = 660;
			else if(headDirection == Robot.Direction.RIGHT)
				degrees = -660;
		}
		headDirection = direction;
		return degrees;
	}

	/**
		* Takes in the direction that you want the robot to be facing and returns
		* how many degrees need to be passed to DifferentialPilot.rotate() to achieve
		* that orientation.
		*
		* @param	direction 		The direction you want the robot to face.
		* @return 	Degrees required to achieve that direction.
	*/
	public int getTurnDegrees(Robot.Direction direction)
	{
		int degrees = 0;
		
		if(direction == Robot.Direction.LEFT){
			if(robotDirection == Robot.Direction.FORWARDS)
				degrees = -90;
			else if(robotDirection == Robot.Direction.RIGHT)
				degrees = -180;
			else if(robotDirection == Robot.Direction.BACKWARDS)
				degrees = 90;
		} else if(direction == Robot.Direction.RIGHT){
			if(robotDirection == Robot.Direction.FORWARDS)
				degrees = 90;
			else if(robotDirection == Robot.Direction.LEFT)
				degrees = 180;
			else if(robotDirection == Robot.Direction.BACKWARDS)
				degrees = -90;
		} else if(direction == Robot.Direction.FORWARDS){
			if(robotDirection == Robot.Direction.LEFT)
				degrees = 90;
			else if(robotDirection == Robot.Direction.RIGHT)
				degrees = -90;
			else if(robotDirection == Robot.Direction.BACKWARDS)
				degrees = 180;
		} else if(direction == Robot.Direction.BACKWARDS){
			if(robotDirection == Robot.Direction.LEFT)
				degrees = -90;
			else if(robotDirection == Robot.Direction.RIGHT)
				degrees = 90;
			else if(robotDirection == Robot.Direction.FORWARDS)
				degrees = 180;
		}
		
		return degrees;
	}
}
