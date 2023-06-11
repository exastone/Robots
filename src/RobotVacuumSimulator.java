import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class RobotVacuumSimulator {
	private static int roomSize;
	private static List<Robot> robots;
	private static List<List<Character>> room;
	private static RobotVacuumSimulator instance;

	/* Private constructor to prevent direct instantiation.
	 * this is required to enforce synchronized block cleanTile() among threads  */
	private RobotVacuumSimulator() {}

	public static synchronized RobotVacuumSimulator getInstance() {
		if (instance == null) {instance = new RobotVacuumSimulator();}
		return instance;
	}

	public List<List<Character>> getRoom() {return room;}
	public int getRoomSize() {return roomSize;}
	public List<Robot> getRobots() {return robots;}
	private final int THREAD_SLEEP_TIME = 50;   // Change to whatever you want

	public static boolean checkOneRobotPerCell() {
		int n = robots.size();
		for (int i = 0; i < n - 1; i++) {
			Robot robotA = robots.get(i);
			for (int j = i + 1; j < n; j++) {
				Robot robotB = robots.get(j);
				if (robotA.x == robotB.x && robotA.y == robotB.y) {
					return false; // Found duplicate coordinates
				}
			}
		}
		return true; // No duplicate coordinates found
	}

	/*This method is called once at the beginning of the simulation
	 * the program is initialized by reading room.txt and robots.txt
	 * then creates the room grid with createRoom()  */
	public void init() {
		String roomFile = "room.txt";
		// String robotsFile = "robot.txt";
		String robotsFile = "robots3.txt";

		try {
			roomSize = ReadFile.readRoomFile(roomFile);
			robots = ReadFile.readRobotsFile(robotsFile);
			//  Create robot at centre of room
			robots.add(new Robot(roomSize / 2, roomSize / 2, 'U'));

			if (!checkOneRobotPerCell()) {
				System.out.println("Error: Multiple robots in the same cell");
				System.exit(1);
			}

			room = createRoom();
			System.out.println("[Room Initialized]");
			Helpers.printGrid(room);
			System.out.println("[Starting Position of Robots]");
			Helpers.printGrid(returnRobotsInGrid(room));

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	// function to create room grid initialized with '.' to represent dirty tiles
	private List<List<Character>> createRoom() {
		room = new ArrayList<>();
		for (int x = 0; x < roomSize; x++) {
			List<Character> row = new ArrayList<>();
			for (int y = 0; y < roomSize; y++) {
				row.add('.');
			}
			room.add(row);
		}
		return room;
	}

	//	function to print the current position of robots in the room
	public void printRobots() {
		robots.forEach(robot -> System.out.println("Robot (" + robot.x + "," + robot.y + ") facing " + robot.getDirection()));
	}

	//	Cleaning tile marks room grid with '✓'
	public synchronized void cleanTile(int x, int y) {room.get(x).set(y, '✓');}

	// Return a grid populated robots at their current positions
	public List<List<Character>> returnRobotsInGrid(List<List<Character>> grid) {
		robots.forEach(robot -> grid.get(robot.x).set(robot.y, robot.getDirection()));
		return grid;
	}

	// Main simulation loop
	public void simulate() {
		List<Thread> threads = new ArrayList<>();

		boolean ROOM_CLEAN = false;
		int i = 0;

		/* Keep running until all tiles are clean or until sufficient iterations have passed.
		 * Since there is a robot created at center of room the max number of iterations
		 * required to clean room is equal to size of room */
		while (!ROOM_CLEAN || i >= roomSize * roomSize) {
			for (Robot robot : robots) {
				Thread thread = new Thread(() -> simulateRobot(robot));
				threads.add(thread);
				thread.start();
			}

			// Wait for all threads to complete
			for (Thread thread : threads) {
				try {
					thread.join();
				} catch (InterruptedException e) {
					e.printStackTrace();
				} finally {
					checkForCollision();
				}
			}
			if (ROOM_CLEAN = Helpers.checkGrid(room, '✓')) {
				System.out.println("\n--- ROOM CLEAN after " + i + " cycles ---");
			}
			i++;
		}

		System.out.println("\n---End of simulation---\nPrinting final state of room:");
		Helpers.printBothView(returnRobotsInGrid(room));
	}

	/* If two vacuums occupy the same cell at any iteration of the algorithm,
	 * then the program should terminate and output to standard output the following message:
	 * "COLLISION AT CELL (m,n)" */
	private void checkForCollision() {
		int n = robots.size();
		for (int i = 0; i < n - 1; i++) {
			Robot robotA = robots.get(i);
			for (int j = i + 1; j < n; j++) {
				Robot robotB = robots.get(j);
				if (robotA.x == robotB.x && robotA.y == robotB.y) {
					System.out.println("Throwing exception... display final state of room:");
					Helpers.printBothView(returnRobotsInGrid(room));
					throw new IllegalStateException("Collision detected at (" + robotA.x + "," + robotA.y + ")");
				}
			}
		}
	}

	// Wrapper around robot.run()
	public void simulateRobot(Robot robot) {
		try {
			Thread.sleep(THREAD_SLEEP_TIME);
			robot.run();
		} catch (InterruptedException e) {
			e.printStackTrace();
		} finally {
			// may place additional print statements here if needed for debugging
		}
	}

}

