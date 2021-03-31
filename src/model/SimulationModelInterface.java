/**
 * @author Gökhan Has
 */

package model;

import java.util.ArrayList;

/**
 * It is the interface that will be used to implement the model in MVC structure.
 */
public interface SimulationModelInterface {

    /**
     * It is the function that will be called by the controller to start or continue the simulation.
     */
    void on();

    /**
     * It is the function that will be called by the controller to stop the simulation.
     */
    void off();

    /**
     * It is the function that will be called by the controller when individuals or individuals are added to the simulation.
     * @param number
     */
    void setPopulation(int number);

    /**
     * It is a function written to return the number of individuals in the simulation.
     * @return
     */
    int getPopulation();

    /**
     * It is the function used for recording to perform the observer design pattern.
     * @param o
     */
    void registerObserver(ViewObserverPopulationInterface o);

    /**
     * Observer is the function used to remove the design pattern.
     * @param o
     */
    void removeObserver(ViewObserverPopulationInterface o);

    /**
     * The return function of the list of indivuduals in the model class.
     * @return
     */
    ArrayList getPopulationList();

    /**
     *
     * @return the semaphore object written for use in synchronization problems.
     */
    Object getSemaphore();

    /**
     * The Hospital object returns the reference.
     * @return
     */
    Hospital getHospital();
}
