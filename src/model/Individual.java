/**
 * @author Gökhan Has
 */
package model;


import java.util.concurrent.TimeUnit;

/**
 *
 */
public class Individual{

    private double Z = 0.2;
    private final int deadTime = (int) (100 *  (1 - this.Z));

    private MediatorIndividual mediatorIndividual;
    private Hospital hospital;

    private int xCoord;
    private int yCoord;
    private IndividualDirections direction;
    private volatile IndividualState individualState;
    private int speed;
    private int cTime;
    private int socialDistance;
    private float maskValue;
    private boolean ifInfected = false;
    private boolean ifFirstInfected = true;
    private double probability;

    private Individual tempIndividual;
    public volatile int ID;

    /**
     * Constructor method.
     * @param mediatorIndividual
     * @param hospital
     */
    public Individual(MediatorIndividual mediatorIndividual, Hospital hospital) {

        this.mediatorIndividual = mediatorIndividual;

        this.xCoord = getRandom(0,1000 - 5);
        this.yCoord = getRandom(0,600  - 5);

        this.direction = IndividualDirections.values()[getRandom(0,3)];
        this.individualState = IndividualState.NORMAL;
        this.speed = getRandom(1, 500);
        this.cTime = getRandom(1, 5);
        this.socialDistance = getRandom(0, 9);

        this.maskValue = getMaskRandomValue();

        this.mediatorIndividual.add(this);
        this.hospital = hospital;
        this.tempIndividual = this;
    }

    /**
     *
     * @param min
     * @param max
     * @return the random number.
     */
    public int getRandom(int min, int max) {
        return (int) (Math.random()*(max-min+1))+min;
    }

    /**
     *
     * @return the mask value of individual.
     */
    private float getMaskRandomValue() {
        int randomValue = getRandom(0,1);
        if(randomValue == 0)
            return (float) 0.2;
        return (float) 1.0;
    }

    /**
     * It changes its coordinates and notifies the mediator object. It tells the Mediator to see if my new coordinates are the ones there.
     * @throws InterruptedException
     */
    public void changedCoord() throws InterruptedException {
        if(!(this.getIndividualState() == IndividualState.INCOLLISION) && !(this.getIndividualState() == IndividualState.DEAD)) {
            switch (this.getDirection()) {
                case TOP:
                    this.setyCoord(this.getyCoord() - this.getSpeed());
                    break;
                case LEFT:
                    this.setxCoord(this.getxCoord() - this.getSpeed());
                    break;
                case RIGHT:
                    this.setxCoord(this.getxCoord() + this.getSpeed());
                    break;
                case BOTTOM:
                    this.setyCoord(this.getyCoord() + this.getSpeed());
                    break;
            }
            controlBounds();
            mediatorIndividual.notify(this);
        }
    }

    /**
     * It changes its direction when it hits corners on the map.
     */
    private void controlBounds() {

        if(this.getxCoord() >= 1000) {
            this.setxCoord(this.getxCoord() - this.getSpeed());
            findCorrectDirection();
        }

        if(this.getxCoord() <= 0) {
            this.setxCoord(this.getxCoord() + this.getSpeed());
            findCorrectDirection();
        }

        if(this.getyCoord() >= 595) {
            this.setyCoord(this.getyCoord() - this.getSpeed());
            findCorrectDirection();
        }

        if(this.getyCoord() <= 0) {
            this.setyCoord(this.getyCoord() + this.getSpeed());
            findCorrectDirection();
        }
    }

    /**
     * It is a new direction finding function.
     */
    private void findCorrectDirection() {
        IndividualDirections temp;
        do {
            temp = IndividualDirections.values()[getRandom(0,3)];
        } while (temp == this.getDirection());
        this.setDirection(temp);
    }

    /**
     *
     * @return x coordinate.
     */
    public int getxCoord() {
        return xCoord;
    }

    /**
     * Setter for x coordinate.
     * @param xCoord
     */
    public void setxCoord(int xCoord) {
        this.xCoord = xCoord;
    }

    /**
     *
     * @return y coordinate.
     */
    public int getyCoord() {
        return yCoord;
    }

    /**
     * Setter for y coordinate.
     * @param yCoord
     */
    public void setyCoord(int yCoord) {
        this.yCoord = yCoord;
    }

    /**
     *
     * @return mask value.
     */
    public float getMaskValue() {
        return maskValue;
    }

    /**
     *
     * @return individual directions.
     */
    public IndividualDirections getDirection() {
        return direction;
    }

    /**
     * Setter for individual directions
     * @param direction
     */
    public void setDirection(IndividualDirections direction) {
        this.direction = direction;
    }

    /**
     *
     * @return individual speed.
     */
    public int getSpeed() {
        return speed;
    }

    /**
     *
     * @return individual collision time.
     */
    public int getcTime() {
        return cTime;
    }

    /**
     *
     * @return social distance.
     */
    public int getSocialDistance() {
        return socialDistance;
    }

    /**
     *
     * @return individual state.
     */
    public synchronized IndividualState getIndividualState() {
        return individualState;
    }

    /**
     * Setter for individual state.
     * @param individualState
     */
    public synchronized void setIndividualState(IndividualState individualState) {
        this.individualState = individualState;
    }

    /**
     * It is used to understand that the individual has recovered and returned healthy after the illness.
     * @return
     */
    public synchronized boolean isIfFirstInfected() {
        return ifFirstInfected;
    }

    /**
     * It is used to understand that the individual has recovered and returned healthy after the illness.
     * @param ifFirstInfected
     */
    public synchronized void setIfFirstInfected(boolean ifFirstInfected) {
        this.ifFirstInfected = ifFirstInfected;
    }

    /**
     * It is the function in which the collision is simulated and runs in a separate thread.
     * @param c
     * @throws InterruptedException
     */
    public void waiting(int c) throws InterruptedException {

        Thread.sleep(c * 1000); // c saniye bekleyin ...
        this.direction = IndividualDirections.values()[getRandom(0,3)]; // yönü değişsin ...

        // CARPISMA BITTI !!!
        // HASTA OLAN VAR MI KONTROLU ...
        if(this.isIfInfected() && this.isIfFirstInfected()) { // ben hastalandım mı ?
            this.setIndividualState(IndividualState.INFECTED); // durumumu hasta olarak değiştir..
            Thread goHospitalThread = new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        // İyileşme, ölme fonksiyonu çalışsın
                        infectedThreadMethod();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            });
            goHospitalThread.start();
        } else {
            // Demekki iki sağlıklı birey çarpışmış, INCOLLISION olan durumumu NORMAL yap
            this.setIndividualState(IndividualState.NORMAL);
        }
    }

    public synchronized boolean isIfInfected() {
        return ifInfected;
    }

    public synchronized void setIfInfected(boolean ifInfected) {
        this.ifInfected = ifInfected;
    }

    public double getProbability() {
        return probability;
    }

    public void setProbability(double probability) {
        this.probability = probability;
    }

    /**
     * Procedure- Consumer paradigm referance : https://docs.oracle.com/javase/7/docs/api/java/util/concurrent/locks/Condition.html
     * @throws InterruptedException
     */
    private void infectedThreadMethod() throws InterruptedException {
        // System.out.println("THREAD : " + this.ID);
        this.setIfFirstInfected(false);
        Thread.sleep(25 * 1000); // 25 saniye gezmem lazım, 25 saniye sonra en erken tedavi olabilirim

        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                Hospital.lock.lock(); // hastaneye girebiliyom mu, ortak değişkenleri kullanabilmek için
                //System.out.println("HOSPITAL SIZE : " + hospital.getHospitalSize() + " LIST SIZE : " + hospital.getSize());
                while (hospital.getHospitalSize() == hospital.getSize()) {
                    try {
                        //
                        if(Hospital.empty.await(deadTime - 25, TimeUnit.MILLISECONDS)) {
                            // 25 saniye geçti + ölüm sayısı kadar geçti, hastanede yer yok, öldüm :(
                            setIndividualState(IndividualState.DEAD); // durumumu ölü olarak ayarla
                            Hospital.lock.unlock();
                            return;
                        }
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                hospital.addElement(tempIndividual); // hastane sırasına girebiliyorum, beni hastaneye götür.
                Hospital.full.signal();
                Hospital.lock.unlock();
            }
        });

        Thread thread_2 = new Thread(new Runnable() {
            @Override
            public void run() {
                Hospital.lock.lock();
                while(hospital.getSize() == 0) {
                    try {
                        // Hastane sırasında kimse yoksa uyumam lazım.
                        Hospital.full.await();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                // Bireyi aldım 10 saniye sonra tedavi edeceğim.
                Individual x = hospital.getList().get(0);
                //System.out.println("--> HOSPITAL SIZE : " + hospital.getHospitalSize() + " LIST SIZE : " + hospital.getSize());
                x.setIndividualState(IndividualState.INHOSPITAL); // durumunu hastanede diye güncelledim
                inHospitalIndividual(x);
                Hospital.empty.signal(); // başka sırada bekleyen varsa uyansın girebilirler
                Hospital.lock.unlock();
                try {
                    Thread.sleep(10 * 1000); // 10 saniye bekle
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                Hospital.lock.lock();
                hospital.getList().remove(x); // listeden bireyi kaldır, çünkü birey artık tedavi oldu ve iyileşti :)
                Hospital.lock.unlock();
                x.setIndividualState(IndividualState.NORMAL); // durumumu normal olarak güncelle
                x.setIfInfected(false); // birey artık başka hastatık kapabilir, umarım kapmaz
                x.setIfFirstInfected(true);
            }
        });
        thread.start();
        thread_2.start();

        // ###################################################################################################
        // # Farklı şekilde procedure-consumer paradigması uygulama çalıştım. En iyisi yukarıdaki gibi oldu. #
        // ###################################################################################################

        /*
        this.hospital.lock.lock();
        try {
            System.out.println("HASTANEDE BOS KALAN YER SAYISI : " + this.hospital.getCount() + " ID : " + this.ID);
           while (this.hospital.getCount() == 0) {
               //this.hospital.lock.unlock();
               if(!(this.hospital.full.await(this.deadTime, TimeUnit.MILLISECONDS))) {
                   this.setIndividualState(IndividualState.DEAD);
                   System.out.println("öldüm");
                   // this.hospital.lock.unlock();
                   return;
               }
               //this.hospital.lock.lock();
           }

           synchronized (semaphore) {
                if(this.getIndividualState() != IndividualState.DEAD) {
                    this.hospital.increaseUsedCount();
                    System.out.println("IYILESMEK ICIN 10 SANIYE BEKLICEM " + this.ID);
                    this.setIndividualState(IndividualState.INHOSPITAL);
                    this.setxCoord(this.getRandom(0,1000));
                    this.setyCoord(this.getRandom(0,600));
                    this.setIfInfected(false);
                    this.hospital.lock.unlock();
                    Thread.sleep(10 * 1000);
                    this.hospital.lock.lock();
                    this.hospital.decreaseUsedCount();
                    this.hospital.full.signal();
                    System.out.println("IYILESTIM BEN SAGLIKLI DONUYORUM " + this.ID);
                    this.setIndividualState(IndividualState.NORMAL);

                    this.setIfInfected(false);
                    this.setIfFirstInfected(true);
                }
           }

        }  finally {
            this.hospital.lock.unlock();
        }
        */
        /*
        if((this.getIndividualState() == IndividualState.DEAD)) {
            System.out.println("___ÖLDÜM____");
            return ;
        }

        synchronized (semaphore) {
            if(this.getIndividualState() != IndividualState.DEAD) {
                synchronized (MediatorIndividual.semaphore) {
                    while (!this.hospital.isHospitalEmpty()) {
                        if((this.getIndividualState() == IndividualState.DEAD)) {
                            System.out.println("HASTANE SIRASI BEKLERKEN ÖLDÜ " + this.ID);
                            return;
                        }
                    }
                }
                this.hospital.increaseUsedCount();
                System.out.println("IYILESMEK IÇIN 10 SANİYE BEKLICEM " + this.ID);
                this.setIndividualState(IndividualState.INHOSPITAL);
                this.setxCoord(this.getRandom(0,1000));
                this.setyCoord(this.getRandom(0,600));
                this.setIfInfected(false);

                Thread.sleep(10 * 1000);

                this.hospital.decreaseUsedCount();
                System.out.println("IYILESTIM BEN SAGLIKLI DONUYORUM " + this.ID);
                this.setIndividualState(IndividualState.NORMAL);

                this.setIfInfected(false);
                this.setIfFirstInfected(true);
            }
        }
        */
    }

    /**
     * It is a function that enables the individual to return to health after hospital.
     * @param x
     */
    private void inHospitalIndividual(Individual x) {
        x.setxCoord(getRandom(0,1000));
        x.setyCoord(getRandom(0,600));
        x.setIfInfected(false);
    }
}
