/**
 * @author Gökhan Has
 */

package controller;

import model.SimulationModelInterface;
import view.SimulationView;

/**
 * It is the class that functions as a controller in the MVC structure.
 */
public class SimulationController implements SimulationControllerInterface {

    /**
     * The MVC structure maintains the view reference necessary for its implementation.
     */
    private SimulationView simulationView;

    /**
     * The MVC structure maintains the model reference necessary for its implementation.
     */
    private SimulationModelInterface simulationModel;

    /**
     * It keeps the information of simulation running for the first time.
     */
    private boolean isFirst = true;

    /**
     * It is the constructor for creating the controller object. It takes the model reference as a parameter.
     * @param simulationModel
     */
    public SimulationController(SimulationModelInterface simulationModel) {
        // GUI OLUSTURULACAK, PARAMETRE OLARAK MODEL ALACAK ...
        this.simulationModel = simulationModel;
        simulationView = new SimulationView(this, this.simulationModel);
    }

    /**
     * It is the function that starts the simulation or resumes it after stopping it.
     */
    @Override
    public void startSimulation() {
        if(isFirst == true) {
            simulationModel.on();
            simulationModel.setPopulation(100);
            isFirst = false;
        }
        else
            simulationModel.on();
    }

    /**
     * It is a function for stopping the simulation.
     */
    @Override
    public void pauseSimulation() {
        simulationModel.off();
    }

    /**
     * It is the function that will work on the GUI side when the user wants to add an individual to the population.
     * @param number
     */
    @Override
    public void increasePopulation(int number) {
        simulationModel.setPopulation(number);
    }

}
