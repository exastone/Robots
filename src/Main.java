public class Main {
	public static void main(String[] args) {
		RobotVacuumSimulator simulator = RobotVacuumSimulator.getInstance();
		simulator.init();
		simulator.simulate();
	}
}