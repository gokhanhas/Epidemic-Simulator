/**
 * @author Gökhan Has
 */
package model;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

/**
 * It is the class that implements the model interface in the MVC structure.
 */
public class SimulationModel implements SimulationModelInterface {

    /**
     * It keeps the total number of individuals in the population.
     */
    private int populationNumber = 0;

    /**
     * Keeps the objects to be recorded for the Observer design pattern.
     */
    private ArrayList populationObservers;

    /**
     * It is a list of individuals in the population.
     */
    private ArrayList<Individual> populationList;

    /**
     * It is the enum that keeps the simulation running or not.
     */
    private SimulationState simulationState;

    /**
     * It is the function that keeps the information whether the simulation runs for the first time or not.
     */
    private boolean isFirst = true;

    /**
     * It is the object that will be created to notify view at every second.
     */
    private Timer timerCalculateMove;

    /**
     * The index of the random patient is kept.
     */
    private int injectedIndividualIndex;

    /**
     * The reference of the Mediator object to be given to each individual is kept.
     */
    private MediatorIndividual mediatorIndividual;

    /**
     * The reference of the Hospital object to be given to each individual is kept.
     */
    private Hospital hospital;

    /**
     *  It is the object to be used for synchronization problems.
     */
    private Object semaphore = new Object();

    /**
     * The constructor of the model object. Necessary objects are created.
     */
    public SimulationModel() {
        hospital = new Hospital(1);
        populationObservers = new ArrayList();
        populationList = new ArrayList<Individual>();

        timerCalculateMove = new Timer("Calculate Timer");
        mediatorIndividual = new MediatorIndividual();
        injectedIndividualIndex = (int) (Math.random()*(100+1));
    }

    /**
     * It is the function that will be called by the controller to start or continue the simulation.
     */
    @Override
    public void on() {
        simulationState = SimulationState.RUN;
        // System.out.println("MODEL ICI ON " + simulationState);
        if(isFirst) {
            // The class that will work for the timer is found below. It is explained in detail in the report.
            timerCalculateMove.scheduleAtFixedRate(new CalculateMove(),0,1000);
        }
        else {
            // The class that will work for the timer is found below. It is explained in detail in the report.
            timerCalculateMove = new Timer("Calculate Timer");
            timerCalculateMove.scheduleAtFixedRate(new CalculateMove(),0,1000);
        }

    }

    /**
     * It is the function that will be called by the controller to stop the simulation.
     */
    @Override
    public void off() {
        simulationState = SimulationState.STOP;
        timerCalculateMove.cancel();
        // System.out.println("MODEL ICI OFF " + simulationState);
    }

    /**
     * It is the function that will be called by the controller when individuals or individuals are added to the simulation.
     * @param number
     */
    @Override
    public void setPopulation(int number) {
        if(simulationState == SimulationState.RUN) {
            this.populationNumber += number;
            addIndividualArray(number);
            notifyPopulationObservers();
        }
    }

    /**
     * It is a helper function that enables the creation of an individual object.
     * @param number
     */
    private void addIndividualArray(int number) {
        synchronized (this.getSemaphore()) {
            for(int i = 0; i < number; i++) {
                Individual temp = new Individual(mediatorIndividual, hospital);
                if(isFirst) {
                    temp.setIfInfected(true);
                    isFirst = false;
                }
                populationList.add(temp);
            }
            hospital.setHospitalSize(populationNumber / 100);

            for(int i = 0; i < populationList.size(); i++) {
                populationList.get(i).ID = i;
            }
        }
    }

    /**
     * The return function of the list of indivuduals in the model class.
     * @return
     */
    @Override
    public ArrayList getPopulationList() {
        synchronized (this.getSemaphore()) {
            return populationList;
        }
    }

    /**
     * It is a function written to return the number of individuals in the simulation.
     * @return
     */
    @Override
    public int getPopulation() {
        return this.populationNumber;
    }

    /**
     * It is the function used for recording to perform the observer design pattern.
     * @param o
     */
    @Override
    public void registerObserver(ViewObserverPopulationInterface o) {
        populationObservers.add(o);
    }

    /**
     * Observer is the function used to remove the design pattern.
     * @param o
     */
    @Override
    public void removeObserver(ViewObserverPopulationInterface o) {
        int i = populationObservers.indexOf(o);
        if (i >= 0) {
            populationObservers.remove(i);
        }
    }

    /**
     * It is the function where the necessary notifications are made to all observer.
     */
    public void notifyPopulationObservers() {
        for (Object populationObserver : populationObservers) {
            ViewObserverPopulationInterface observer = (ViewObserverPopulationInterface) populationObserver;
            observer.updatePopulation();
        }
    }

    /**
     * The Hospital object returns the reference.
     * @return
     */
    @Override
    public Hospital getHospital() {
        return this.hospital;
    }

    /**
     *
     * @return the semaphore object written for use in synchronization problems.
     */
    @Override
    public Object getSemaphore() {
        return semaphore;
    }

    /**
     * It is the class that will be executed by the timer every second. Gets extends from the TimerTask class.
     * And in this class, it is necessary to override the run () method in the Runnable interface.
     * So there is Thread in it.
     */
    private class CalculateMove extends TimerTask {
        @Override
        public void run() {
            synchronized (getSemaphore()) {
                if(simulationState == SimulationState.RUN) {
                    for (Individual individual : populationList) {
                        try {
                            if((individual.getIndividualState() != IndividualState.DEAD) && !(individual.getIndividualState() == IndividualState.INCOLLISION))
                                individual.changedCoord();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    notifyPopulationObservers();
                }
            }
        }
    }
}
