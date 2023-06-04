import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class RobotVacuumSimulator {
	private static int roomSize;
	private static List<Robot> robots;
	private static List<List<Character>> room;
	private static boolean verbose = true;
	private static RobotVacuumSimulator instance;

	private RobotVacuumSimulator() {
		// Private constructor to prevent direct instantiation
	}
	public static synchronized RobotVacuumSimulator getInstance() {
		if (instance == null) {
			instance = new RobotVacuumSimulator();
		}
		return instance;
	}

	/* Getters */
	public List<List<Character>> getRoom() {
		return room;
	}

	public List<Robot> getRobots() {
		return robots;
	}


	/*This method is called once at the beginning of the simulation
	 * the program is initialized by reading room.txt and robots.txt
	 * then creates the room grid with createRoom()  */
	public void init(String... args) {
		String roomFile = "room.txt";
		String robotsFile = "robots.txt";
//		String robotsFile = "1robot.txt";

		try {
			roomSize = ReadFile.readRoomFile(roomFile);
			robots = ReadFile.readRobotsFile(robotsFile);
			robots.add(createCenterRobot());
			room = createRoom();

			if (verbose) {
				System.out.println("[Room Initialized]");
				Helpers.printGrid(room);
			}

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	// function to create room grid initialized with '*' to represent dirty tiles
	private List<List<Character>> createRoom() {
		room = new ArrayList<>();
		for (int x = 0; x < roomSize; x++) {
			List<Character> row = new ArrayList<>();
			for (int y = 0; y < roomSize; y++) {
				row.add('*');
			}
			room.add(row);
		}
		return room;
	}

	//	function to print the current position of robots in the room
	public void printRobots() {
		for (Robot robot : robots) {
			System.out.println("Robot at (" + robot.x + "," + robot.y + ") facing " + robot.direction);
		}
	}

	//	function to create a robot at the center of the room with initial direction 'U'
	private Robot createCenterRobot() {
		int x = roomSize / 2;
		int y = roomSize / 2;
		char direction = 'U';
		return (new Robot(x, y, direction));
	}

	public List<List<Character>> updateRobots(List<List<Character>> grid) {
		List<List<Character>> room = grid;
		// Populate the room with robots
		for (Robot robot : robots) {
			room.get(robot.x-1).set(robot.y-1, robot.direction);
		}
		if (verbose) {
			System.out.println("[Positions Updated]");
			Helpers.printGrid(room);
		}
		return room;
	}

	public synchronized void cleanTile(int x, int y) {
		room.get(x).set(y, '✓');
	}

	public void simulate() {
		List<Thread> threads = new ArrayList<>();

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
				updateRobots(room);
			}
		}
		if (verbose) {
			System.out.println("[Room Updated]");
			Helpers.printGrid(room);
		}
	}

	public void simulateRobot(Robot robot) {

		try {
			if (verbose) {
				System.out.println("Thread " + Thread.currentThread().getId() + " is cleaning tile (" + robot.x + "," + robot.y + ")");
			}
			Thread.sleep(2000);
			robot.run();
		} catch (InterruptedException e) {
			e.printStackTrace();
		} finally {
			if (verbose) {
				System.out.println("Thread " + Thread.currentThread().getId() + " is done.");
			}
		}
	}
}

