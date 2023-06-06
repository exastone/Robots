import java.util.*;

class Robot implements Runnable {
	int x, y;
	int roomSize = RobotVacuumSimulator.getInstance().getRoomSize();
	List<Character> moveHistory = new ArrayList<>();
	List<Character> directionEnum = List.of('U', 'L', 'D', 'R');
	int directionIndex;

	int distance = 1;
	int movementsInDirection = 0;

	public Robot(int x, int y, char direction) {
		this.x = x;
		this.y = y;
		this.directionIndex = directionEnum.indexOf(direction);
	}

	public char getDirection() {return directionEnum.get(directionIndex);}

	private void move() {
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
			// reset robot with new direction
			movementsInDirection = 0;
			distance = 1;
			directionIndex = (directionIndex + 1) % directionEnum.size();
		}

	}

	private void updateDirection() {
		if (movementsInDirection == distance) {
			directionIndex = (directionIndex + 1) % directionEnum.size();
			movementsInDirection = 0;

			if (directionIndex % 2 == 0) {
				distance++;
			}
		}
	}

	@Override
	public void run() {
		RobotVacuumSimulator simulator = RobotVacuumSimulator.getInstance();
		simulator.cleanTile(x, y);
		move();
		updateDirection();
	}

}
