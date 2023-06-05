import java.util.ArrayList;
import java.util.List;

class Robot implements Runnable {
	int x, y;
	char direction;
	List<List<Integer>> positionHistory = new ArrayList<>();
	private boolean verbose = true;
	List<List<Character>> room;

	public Robot(int x, int y, char direction) {
		this.x = x;
		this.y = y;
		this.direction = direction;
		positionHistory.add(List.of(x, y));
	}

	// update x,y position based on the direction then add the move to the positionHistory
	private void move() {
		switch (this.direction) {
			case 'U' -> {
				x--;
				positionHistory.add(List.of(x, y));
			}
			case 'L' -> {
				y--;
				positionHistory.add(List.of(x, y));
			}
			case 'D' -> {
				x++;
				positionHistory.add(List.of(x, y));
			}
			case 'R' -> {
				y++;
				positionHistory.add(List.of(x, y));
			}
		}
		moveHistory.add(this.direction);
		if (verbose) {
			System.out.println("Thread " + Thread.currentThread().getId() + ": " + "Robot moved to (" + x + "," + y + ")");
		}
	}

	// function to calculate the next move based on positionHistory and direction
	// robots move in a counter-clockwise direction
	private char nextMove() {
		List<Integer> startingPosition = positionHistory.get(0);
		List<Integer> currentPosition = positionHistory.get(positionHistory.size() - 1);
		int largestX = 0, largestY = 0;
		int smallestX = 0, smallestY = 0;
		char newDirection;

		// loop through positionHistory to find the largest and smallest x and y values
		for (List<Integer> position : positionHistory) {
			if (position.get(0) > largestX) {
				largestX = position.get(0);
			}
			if (position.get(1) > largestY) {
				largestY = position.get(1);
			}
			if (position.get(0) < smallestX) {
				smallestX = position.get(0);
			}
			if (position.get(1) < smallestY) {
				smallestY = position.get(1);
			}
		}

		// if the robot is at the starting position, move in direction it is facing
		if (currentPosition.equals(startingPosition)) {
			newDirection = direction;
		} else {
			// if the robot is at the largest x value, move down
			if (currentPosition.get(0) == largestX) {
				newDirection = 'D';
			}
			// if the robot is at the largest y value, move left
			if (currentPosition.get(1) == largestY) {
				newDirection = 'L';
			}
			// if the robot is at the smallest x value, move up
			if (currentPosition.get(0) == smallestX) {
				newDirection = 'U';
			}
			// if the robot is at the smallest y value, move right
			if (currentPosition.get(1) == smallestY) {
				newDirection = 'R';
			}
			newDirection = 'U';
		}
		if (verbose) {
			System.out.println("Thread " + Thread.currentThread().getId() + ": " + "Robot is moving " + newDirection);
		}
		return newDirection;
	}

	@Override
	public void run() {
		RobotVacuumSimulator simulator = RobotVacuumSimulator.getInstance();
		System.out.println("Thread " + Thread.currentThread().getId() + " started running");
		simulator.cleanTile(x, y);

		move(nextMove());
	}


	// Getters and setters if needed
}
