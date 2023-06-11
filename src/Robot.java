import java.util.*;

class Robot implements Runnable {
	int x, y;
	int roomSize = RobotVacuumSimulator.getInstance().getRoomSize();
	boolean boundaryHit = false;    // flag used to fall back to alternative movement
	List<Character> moveHistory = new ArrayList<>();
	List<Character> directionEnum = List.of('U', 'L', 'D', 'R');
	int directionIndex;

	int distance = 1;
	int movementsInDirection = 0;

	// Constructor
	public Robot(int x, int y, char direction) {
		this.x = x;
		this.y = y;
		this.directionIndex = directionEnum.indexOf(direction);
	}

	public char getDirection() {return directionEnum.get(directionIndex);}

	private void move() {
		//	Calculate new position; if position is within bounds of room, move robot
		int dummyX = x;
		int dummyY = y;
		switch (directionEnum.get(directionIndex)) {
			case 'U' -> dummyX--;
			case 'L' -> dummyY--;
			case 'D' -> dummyX++;
			case 'R' -> dummyY++;
		}

		if (dummyX >= 0 && dummyY >= 0 && dummyX < roomSize && dummyY < roomSize) {
			x = dummyX;
			y = dummyY;
			movementsInDirection++;
			moveHistory.add(getDirection());
		} else {
			boundaryHit = true;
			directionIndex = (directionIndex + 1) % directionEnum.size();
		}
	}

	private void updateDirection() {
		// If robot has hit a wall, travel along walls in counterclockwise direction
		if (boundaryHit) {
			movementsInDirection = 0;
			distance = 1;
		} else {
			// If robot has travelled distance in current direction, change direction
			if (movementsInDirection == distance) {
				directionIndex = (directionIndex + 1) % directionEnum.size();
				movementsInDirection = 0;
				// If robot has changed direction twice, increase distance
				if (directionIndex % 2 == 0) {
					distance++;
				}
			}
		}
	}

	// Thread Task
	@Override
	public void run() {
		RobotVacuumSimulator simulator = RobotVacuumSimulator.getInstance();
		simulator.cleanTile(x, y);
		move();
		updateDirection();
	}

}
