/**
 * @author Gökhan Has
 */

package model;

import java.util.ArrayList;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * It is the class used for hospital operations.
 */
public class Hospital {
    /**
     * It is the variable that keeps the number of ventilators in the hospital.
     */
    private volatile int hospitalSize;

    /**
     * It is the list used to keep those during the hospital.
     */
    private ArrayList<Individual> list = new ArrayList<>();

    /**
     * These are the variables that will be used to implement the Procedure Consumer paradigm.
     */
    public static volatile Lock lock = new ReentrantLock();
    public static volatile Condition full = lock.newCondition();
    public static volatile Condition empty = lock.newCondition();

    /**
     * Constructor method.
     * @param hospitalSize
     */
    public Hospital(int hospitalSize) {
       this.hospitalSize = hospitalSize;
    }

    /**
     *
     * @return the hospital size.
     */
    public  int getHospitalSize() {
        return hospitalSize;
    }

    /**
     * The size of the hospital increases as new individuals are added, but does not decrease as it decreases.
     * @param hospitalSize
     */
    public  void setHospitalSize(int hospitalSize) {
        this.hospitalSize = hospitalSize;
    }

    /**
     *
     * @return the list that holds the hospital bench.
     */
    public  ArrayList<Individual> getList() {
        return list;
    }

    /**
     *
     * @return the list, which holds the hospital bench, size.
     */
    public int getSize() { return list.size(); }

    /**
     * Adds an individual to the hospital.
     * @param individual
     */
    public  void addElement(Individual individual) {
        list.add(individual);
    }
}
