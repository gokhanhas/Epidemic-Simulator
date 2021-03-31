/**
 * @author Gökhan Has
 */
package controller;

/**
 * It is a Controller interface in MVC structure.
 */
public interface SimulationControllerInterface {
    /**
     * It is the function that starts the simulation or resumes it after stopping it.
     */
    void startSimulation();

    /**
     * It is a function for stopping the simulation.
     */
    void pauseSimulation();

    /**
     * It is the function that will work on the GUI side when the user wants to add an individual to the population.
     * @param number
     */
    void increasePopulation(int number);
}
