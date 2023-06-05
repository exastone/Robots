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


}
