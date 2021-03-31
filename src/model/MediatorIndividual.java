/**
 * @author Gökhan Has
 */

package model;

import java.util.ArrayList;
import static java.lang.Math.min;

/**
 * It is the class that implements the mediator interface.
 */
public class MediatorIndividual implements MediatorInterface{

    private double R = 0.5;
    private double Z = 0.9;

    /**
     * It is the list to be kept in order to calculate whether there was a collision
     * with other individuals except the one who called the mediator.
     */
    private ArrayList individuals= new ArrayList();

    //public static volatile Object semaphore = new Object();

    /**
     * It is the add function that takes the individual reference.
     * @param individual
     */
    @Override
    public void add(Individual individual) {
        individuals.add(individual);
    }

    /**
     * It is the function that is necessary to understand which individual is called
     * and whether there is a collision or not.
     * @param individual
     */
    @Override
    public void notify(Individual individual)  {
        for(int i=0; i < individuals.size(); i++) {
            Individual temp = (Individual) individuals.get(i);
            if(temp != individual && (temp.getIndividualState() != IndividualState.INCOLLISION && individual.getIndividualState() != IndividualState.INCOLLISION)
                        && (temp.getIndividualState() != IndividualState.DEAD && individual.getIndividualState() != IndividualState.DEAD)) {

                double distance = Math.sqrt((Math.pow(individual.getxCoord() - temp.getxCoord(), 2) + Math.pow(individual.getyCoord() - temp.getyCoord(), 2)));
                int minD = min(individual.getSocialDistance(), temp.getSocialDistance());

                if(distance <= minD) {
                    individual.setIndividualState(IndividualState.INCOLLISION);
                    temp.setIndividualState(IndividualState.INCOLLISION);

                    int maxC = Math.max(individual.getcTime(), temp.getcTime());
                    double P = min(this.R * (1 + maxC / 10) * temp.getMaskValue() * individual.getMaskValue() * (1 - minD / 10), 1);
                    if(individual.isIfInfected()) {
                        temp.setIfInfected(true); // temp enfekte oldu ...
                        temp.setProbability(P);
                    }
                    else if(temp.isIfInfected()) {
                        individual.setIfInfected(true); // mediatore haber veren enfekte oldu ...
                        individual.setProbability(P);
                    }

                    Thread sleepThread1 = new Thread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                // The standby function of the interacting individual is called. This function should run in a separate thread.
                                individual.waiting(maxC);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                    });

                    Thread sleepThread2 = new Thread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                // The standby function of the interacting individual is called. This function should run in a separate thread.
                                temp.waiting(maxC);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                    });

                    sleepThread1.start();
                    sleepThread2.start();
                    break;
                }
            }
        }
    }
}
