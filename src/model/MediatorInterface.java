/**
 * @author Gökhan Has
 */
package model;

/**
 * It is the interface to be used in Mediator design pattern.
 */
public interface MediatorInterface {

    /**
     * It is the add function that takes the individual reference.
     * @param individual
     */
    void add(Individual individual);

    /**
     * It is the function that is necessary to understand which individual is called
     * and whether there is a collision or not.
     * @param individual
     * @throws InterruptedException
     */
    void notify(Individual individual) throws InterruptedException;
}
