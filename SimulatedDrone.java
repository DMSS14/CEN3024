import java.util.Scanner;

//Author Name: Daniel McGee
//Date: 05/14/2021
//Program Name: SimulatedDrone
//Purpose: Simulation using button, drone movement in x, y, z location
public class SimulatedDrone {

	/**
	 * 
	 * Basic directional Enum that can be expanded into diagonals. 
	 *
	 */
	public enum DroneDirection{
		North(1,0),West(0,1),South(-1,0),East(0,-1);
		public final int xFactor;
		public final int zFactor;
		private DroneDirection(int x, int z) {
			xFactor = x;
			zFactor = z;
		}
		public String toString() {
			return String.format("%s(x:%d,z:%d)", this.name(), this.xFactor, this.zFactor);
		}
	}
	private int xPosition = 0;
	private int yPosition = 0;
	private int zPosition = 0;
	private DroneDirection direction = DroneDirection.North;
	
	/***
	 * Moves amount units up or down.
	 * @param amount
	 */
	private void moveY(int amount) {
		this.yPosition += amount;
	}
	public void moveUp() {
		this.moveY(1);
	}
	public void moveDown() {
		this.moveY(-1);
	}
	
	/***
	 * Moves amount units in the current direction. Negative units move backwards.
	 * @param amount
	 */
	private void moveFacing(int amount) {
		this.xPosition += this.direction.xFactor * amount;
		this.zPosition += this.direction.zFactor * amount;
	}
	public void moveForward() {
		moveFacing(1);
	}
	public void moveBackward() {
		moveFacing(-1);
	}
	
	/***
	 * Turns amount units to the left. Negative units turn right. 
	 * How many degrees a unit is depends on how many directions are defined.
	 * @param amount
	 */
	private void turn(int amount) {
		int index = this.direction.ordinal()+amount;
		DroneDirection[] allValues = DroneDirection.values();
		index = (index % allValues.length + DroneDirection.values().length) % allValues.length;
		this.direction = allValues[index];
	}
	public void turnLeft() {
		this.turn(1);
	}
	public void turnRight() {
		this.turn(-1);
	}


	public int getxPosition() {
		return xPosition;
	}
	public int getyPosition() {
		return yPosition;
	}
	public int getzPosition() {
		return zPosition;
	}
	public DroneDirection getDirection() {
		return direction;
	}
	public String toString() {
		return String.format("Simulated Drone at x=%d, y=%d, z=%d, facing %s.", this.xPosition, this.yPosition, this.zPosition, this.direction);
	}
	
	/***
	 * Very basic UI function. Loop until input is the end program signal, use a switch statement to perform commands
	 * based on the input.
	**/
	public static void main(String[] args) {
		Scanner s = new Scanner(System.in);
		String input = "";
		SimulatedDrone drone = new SimulatedDrone();
		while(!input.equals("8")) {
			System.out.print("How would you like to move the drone?\n"
					+ "1 - Upwards\n"
					+ "2 - Downwards\n"
					+ "3 - Forwards\n"
					+ "4 - Backwards\n"
					+ "5 - Turn Left\n"
					+ "6 - Turn Right\n"
					+ "7 - Display Drone\n"
					+ "8 - End Program\n");
			input = s.nextLine();
			
			switch(input) {
				case "1":
					drone.moveUp();
					break;
				case "2":
					drone.moveDown();
					break;
				case "3":
					drone.moveForward();
					break;
				case "4":
					drone.moveBackward();
					break;
				case "5":
					drone.turnLeft();
					break;
				case "6":
					drone.turnRight();
					break;
				case "7":
					System.out.println(drone);
					break;
				case "8":
					break;
				default:
					System.out.println("\"" + input + "\" is an invalid choice!");
				break;
			}
		}
		System.out.println("Ending...");
		s.close();
	}

}
