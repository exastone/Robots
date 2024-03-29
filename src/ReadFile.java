import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ReadFile {
	// Read room file and return room size
	public static int readRoomFile(String filename) throws IOException {
		BufferedReader reader = new BufferedReader(new FileReader(filename));
		String line = reader.readLine();
		reader.close();
		return Integer.parseInt(line);
	}

	// Read robots file and return list of robots
	public static List<Robot> readRobotsFile(String filename) throws IOException {
		BufferedReader reader = new BufferedReader(new FileReader(filename));
		int numRobots = Integer.parseInt(reader.readLine());
		List<Robot> robots = new ArrayList<>();

		for (int i = 0; i < numRobots; i++) {
			String[] robotData = reader.readLine().split(" ");
			// Note: y is horizontal axis, x is vertical axis
			int y = Integer.parseInt(robotData[0]);
			int x = Integer.parseInt(robotData[1]);
			char direction = robotData[2].charAt(0);

			robots.add(new Robot(x, y, direction));
		}
		reader.close();
		return robots;
	}
}
