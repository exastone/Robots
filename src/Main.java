public class Main {
	public static void main(String[] args) {
		RobotVacuumSimulator simulator = RobotVacuumSimulator.getInstance();
		simulator.init();

		long startTime = System.nanoTime();
		simulator.simulate();
		long endTime = System.nanoTime();

		long elapsedTimeMs = (endTime - startTime) / 1_000_000;
		System.out.println("Execution Time: " + elapsedTimeMs + " ms");
	}
}
