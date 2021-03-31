import controller.SimulationController;
import controller.SimulationControllerInterface;
import model.SimulationModel;
import model.SimulationModelInterface;

public class Main {
    public static void main(String[] args) throws InterruptedException {
        SimulationModelInterface simulationModel = new SimulationModel();
        SimulationControllerInterface simulationController = new SimulationController(simulationModel);
    }
}
