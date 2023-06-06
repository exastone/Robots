import java.util.List;
import java.util.stream.IntStream;

public class Helpers {

	// Provided a 2D grid, prints the grid to the console
	public static void printGrid(List<List<Character>> grid) {
		for (int i = 0; i < grid.size(); i++) {
			System.out.print(" " + i);
			if (i == grid.size() - 1) {
				System.out.println(" \t-> y");
			}
		}
		IntStream.range(0, grid.size()).forEach(rowIdx -> {
			List<Character> row = grid.get(rowIdx);
			IntStream.range(0, row.size()).forEach(colIdx -> {
				char cell = row.get(colIdx);
				if (colIdx == row.size() - 1) {
					System.out.print("|" + cell + "| " + rowIdx);
					if (rowIdx == 0) {
						System.out.print(" ↓ x");
					}
				} else {
					System.out.print("|" + cell);
				}
			});
			System.out.println();
		});
	}
	public static void printBothView(List<List<Character>> grid) {
		System.out.println("[Room View]");
		Helpers.printGrid(grid);
		System.out.println("[Room View w/ robots]");
		Helpers.printGrid(RobotVacuumSimulator.getInstance().returnRobotsInGrid(grid));
	}

	//	checks if all tiles in grid have same character
	public static boolean checkGrid(List<List<Character>> grid, char c) {
		boolean allSame = false;
		for (var row : grid) {
			for (var col : row) {
				if (col != c) {
					break;
				} else {
					allSame = true;
				}
			}
		}
		return allSame;
	}

}
