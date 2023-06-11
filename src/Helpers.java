import java.util.List;
import java.util.stream.IntStream;

// Helper functions
public class Helpers {

	// Provided a 2D grid, prints the grid to the console
	public static void printGrid(List<List<Character>> grid) {
		int size = grid.size();

		// Print column numbers
		System.out.print("   ");
		for (int i = 0; i < size; i++) {
			if (i < 10)
				System.out.print("| " + i + " ");
			else
				System.out.print("|" + i + " ");
		}
		System.out.println("\t-> y");

		// Print grid rows
		for (int rowIdx = 0; rowIdx < size; rowIdx++) {
			List<Character> row = grid.get(rowIdx);

			// Print row contents
			if (rowIdx < 10)
				System.out.print(rowIdx + "  ");
			else
				System.out.print(rowIdx + " ");
			for (char cell : row) {
				System.out.print("| " + cell + " ");
			}
			System.out.println("|");
		}

		// Print x-axis indicator
		System.out.println("x↓\n");
	}

	// function to print the current position of robots in the room;
	public static void printRobots() {
		List<Robot> robots = RobotVacuumSimulator.getInstance().getRobots();
		// note: (y,x) === (horizontal, vertical)
		robots.forEach(robot -> System.out.println("Robot (" + robot.y + "," + robot.x + ") facing " + robot.getDirection()));
	}

	// Return a copy of the grid populated with robots at their current positions
	public static List<List<Character>> returnRobotsInGrid(List<List<Character>> grid) {
		List<Robot> robots = RobotVacuumSimulator.getInstance().getRobots();
		robots.forEach(robot -> grid.get(robot.x).set(robot.y, robot.getDirection()));
		return grid;
	}

	// Check if all tiles in grid have same character
	public static boolean checkGrid(List<List<Character>> grid, char c) {
		for (List<Character> row : grid) {
			for (char cell : row) {
				if (cell != c) {return false;}
			}
		}
		return true;
	}

	// Defined to reduce code duplication
	public static void printBothView(List<List<Character>> grid) {
		System.out.println("[Room View]");
		Helpers.printGrid(grid);
		System.out.println("[Room View w/ robots]");
		Helpers.printGrid(returnRobotsInGrid(grid));
	}

}
