import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;

public class RobotVacuumSimulator {
	private static int roomSize;
	private static List<Robot> robots;
	private static List<List<Character>> room;
	private static RobotVacuumSimulator instance;

	/* Private constructor to prevent direct instantiation.
	 * required to enforce synchronized block cleanTile() and used by Helpers class  */
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

	/* This method is called once at the beginning of the simulation
	 * the program is initialized by reading room.txt and robots.txt
	 * then creates the room grid with createRoom()  */
	public void init() {
		String roomFile = "room.txt";
		String robotsFile = "robots.txt";

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
			Helpers.printGrid(Helpers.returnRobotsInGrid(room));

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

	//	Cleaning tile marks room grid with '✓'
	public synchronized void cleanTile(int x, int y) {room.get(x).set(y, '✓');}

	// Main simulation loop
	public void simulate() {
		boolean ROOM_CLEAN = false;
		int i = 0;
		/* Keep running until all tiles are clean or until sufficient iterations have passed.
		 * Since there is a robot created at center of room the max number of iterations
		 * required to clean room is equal to size of room */
		while (!ROOM_CLEAN && i < roomSize * roomSize) {
			List<Thread> threads = new ArrayList<>();
			//	CountDownLatch is used to wait for all threads to complete before checking for collision
			CountDownLatch latch = new CountDownLatch(robots.size());

			for (Robot robot : robots) {
				Thread thread = new Thread(() -> {
					simulateRobot(robot);
					latch.countDown(); // Signal completion of work
				});
				threads.add(thread);
				thread.start();
			}

			try {
				latch.await(); // Wait for all threads to complete
			} catch (InterruptedException e) {
				e.printStackTrace();
			}

			checkForCollision(i);

			/*  May be used to print grid after each iteration for debugging/inspection
				Helpers.printGrid(Helpers.returnRobotsInGrid(room)); */

			if (Helpers.checkGrid(room, '✓')) {
				ROOM_CLEAN = true;
				System.out.println("--- ROOM CLEAN after " + i + " loop iterations ---");
			}
			i++;
		}

		System.out.println("\n---End of simulation---\nPrinting final state of room after " + (i - 1) + " loop iterations:");
		Helpers.printBothView(room);
		System.out.println("Final position of robots:");
		Helpers.printRobots();
	}

	/* If two robots occupy the same cell at any iteration of the algorithm, then the
	 * program should terminate and output to standard output the following message:
	 * "COLLISION AT CELL (m,n)" */
	private void checkForCollision(int loopCount) {
		int n = robots.size();
		for (int i = 0; i < n - 1; i++) {
			Robot robotA = robots.get(i);
			for (int j = i + 1; j < n; j++) {
				Robot robotB = robots.get(j);
				if (robotA.x == robotB.x && robotA.y == robotB.y) {
					System.out.println("Throwing exception... displaying final state of room after " + loopCount + " iterations:");
					Helpers.printBothView(room);
					Helpers.printRobots();
					// (y,x) === (horizontal, vertical)
					throw new IllegalStateException("Collision detected at (" + robotA.y + "," + robotA.x + ") on loop iteration " + loopCount);
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
		}
	}

}

