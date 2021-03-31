/**
 * @author Gökhan Has
 */

package view;

import controller.SimulationControllerInterface;
import model.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.lang.String;
import java.time.Clock;
import java.util.ArrayList;


/**
 * This is the SimulationView class including gui part of final project.
 */
public class SimulationView extends JPanel implements ViewObserverPopulationInterface {

    /**
     * The MVC structure maintains the controller reference necessary for its implementation.
     */
    private SimulationControllerInterface simulationController;

    /**
     * The MVC structure maintains the model reference necessary for its implementation.
     */
    private SimulationModelInterface simulationModel;

    // Frame ...
    private JFrame frame;

    // Panels ...
    private JPanel panel;
    private JPanel populationPanel;

    // Buttons ...
    private JButton startButton;
    private JButton stopButton;
    private JButton addOnePersonButton;

    // For bulkedPeople ...
    private JLabel infoMessageLabel;
    private JTextField bulkNumberTextField;
    private int bulkPeopleNumber = 0;
    private JButton addBulkButton;

    private JLabel patientCount;
    private JLabel healthyCount;
    private JLabel hospitalCount;
    private JLabel deadCount;

    private JLabel patientCountMsg;
    private JLabel healthyCountMsg;
    private JLabel hospitalCountMsg;
    private JLabel deadCountMsg;

    private double maskUsageRatio;
    private double meanSocialDistance;

    // Error message jlabel ...
    private JLabel errorMessageLabel;


    private Clock clock = Clock.systemDefaultZone();

    /**
     * Constructor method. It takes the reference of the model and controller interface as parameters. And it registers itself to the observer.
     * @param simulationController
     * @param simulationModel
     */
    public SimulationView(SimulationControllerInterface simulationController, SimulationModelInterface simulationModel) {
        this.simulationController = simulationController;
        this.simulationModel = simulationModel;

        this.simulationModel.registerObserver((ViewObserverPopulationInterface) this);

        this.createGui();
    }

    /**
     * It is the function that creates the GUI.
     */
    private void createGui() {
        frame = new JFrame("CSE443 - OOAD - FINAL PROJECT - GOKHAN HAS - 161044067");
        frame.setSize(1240, 630);
        frame.setLocation(200,100);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // First JPanel which using in buttons and statistical calculations.
        panel = new JPanel();
        panel.setPreferredSize(new Dimension(230, 630));
        panel.setBackground(Color.DARK_GRAY);
        initializeButtons(panel);
        frame.add(panel, BorderLayout.WEST);

        // Second JPanel which using simulate the population and drawing rectangles as people.
        populationPanel = new JPanel();
        //populationPanel.setBackground(Color.BLACK);
        populationPanel.setLayout(null);
        populationPanel.setPreferredSize(new Dimension(1000, 630));
        frame.add(populationPanel);

        // Frame last operations ...
        frame.setResizable(false);
        frame.setVisible(true);
    }

    /**
     * It is the function in which the components that will interact with the user are created.
     * The changes are sent to the controller.
     * @param panel
     */
    private void initializeButtons(JPanel panel) {
        panel.setLayout(null);

        // Start button operations ...
        startButton = new JButton("Start");
        startButton.setBounds(10,10,100,30);
        startButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // System.out.println("START BASILDI");
                simulationController.startSimulation();
            }
        });

        // Stop button operations ...
        stopButton = new JButton("Stop");
        stopButton.setBounds(120,10,100,30);
        stopButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // System.out.println("STOP BASILDI");
                simulationController.pauseSimulation();
            }
        });


        // Add one person button ...
        addOnePersonButton = new JButton("Add One Person");
        addOnePersonButton.setBounds(10,50,210,30);
        addOnePersonButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // System.out.println("1 EKLEME  BASILDI");
                simulationController.increasePopulation(1);
            }
        });


        infoMessageLabel = new JLabel("Add   population : ");
        infoMessageLabel.setForeground(Color.white);
        infoMessageLabel.setBounds(10, 90, 100, 30);


        // Bulk number from user ...
        bulkNumberTextField = new JTextField(" ");
        bulkNumberTextField.setBounds(110, 95, 40, 20);

        // Wrong input message, to warn the user ...
        errorMessageLabel = new JLabel("Wrong Input !");
        errorMessageLabel.setBounds(10,120,200,30);
        errorMessageLabel.setForeground(Color.red);
        errorMessageLabel.setVisible(false);

        // Add one person button ...
        addBulkButton = new JButton("Add");
        addBulkButton.setBounds(160,90,60,30);
        addBulkButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String valueStr = bulkNumberTextField.getText();
                valueStr = valueStr.replaceAll("\\s+","");
                try {
                    bulkPeopleNumber = Integer.parseInt(valueStr);
                    errorMessageLabel.setVisible(false);
                    // System.out.println("TOPLU EKLE BASILDI : " + bulkPeopleNumber);
                    simulationController.increasePopulation(bulkPeopleNumber);
                }
                catch(NumberFormatException ex) {
                    errorMessageLabel.setVisible(true);
                }
            }
        });


        patientCountMsg = new JLabel("PATIENT COUNT     : ");
        patientCountMsg.setBounds(10,410,180,30);
        patientCountMsg.setForeground(Color.RED);


        healthyCountMsg = new JLabel("HEALTY COUNT       : ");
        healthyCountMsg.setBounds(10,450,200,30);
        healthyCountMsg.setForeground(Color.GREEN);

        hospitalCountMsg = new JLabel("HOSPITAL COUNT   : ");
        hospitalCountMsg.setBounds(10,490,200,30);
        hospitalCountMsg.setForeground(Color.BLUE);


        deadCountMsg = new JLabel("DEAD COUNT           : ");
        deadCountMsg.setBounds(10,530,200,30);
        deadCountMsg.setForeground(Color.WHITE);

        // ---------------------------------------------------------------------

        patientCount = new JLabel(" ");
        patientCount.setBounds(150,410,200,30);
        patientCount.setForeground(Color.RED);


        healthyCount = new JLabel(" ");
        healthyCount.setBounds(150,450,200,30);
        healthyCount.setForeground(Color.GREEN);

        hospitalCount = new JLabel(" ");
        hospitalCount.setBounds(150,490,200,30);
        hospitalCount.setForeground(Color.BLUE);


        deadCount = new JLabel(" ");
        deadCount.setBounds(150,530,200,30);
        deadCount.setForeground(Color.WHITE);


        // Adding the components in control panel ...
        panel.add(startButton);
        panel.add(stopButton);
        panel.add(addOnePersonButton);
        panel.add(infoMessageLabel);
        panel.add(bulkNumberTextField);
        panel.add(addBulkButton);
        panel.add(errorMessageLabel);
        panel.add(patientCountMsg);
        panel.add(healthyCountMsg);
        panel.add(hospitalCountMsg);
        panel.add(deadCountMsg);
        panel.add(patientCount);
        panel.add(healthyCount);
        panel.add(hospitalCount);
        panel.add(deadCount);

    }

    /**
     * It is the function called by the model. It is used to update the panel.
     */
    @Override
    public void updatePopulation() {
        synchronized (simulationModel.getSemaphore()) {
            int patient = 0, healt = 0, dead = 0, hospital = 0;
            maskUsageRatio = 0.0;
            meanSocialDistance = 0.0;
            Graphics g = populationPanel.getGraphics(); // YENIDEN HARITA GUNCELLENECEK ...
            g.clearRect(0,0 ,populationPanel.getWidth(), populationPanel.getHeight()); // TEMIZLEME ISLEMINE BAKILACAK ...
            ArrayList populationList = simulationModel.getPopulationList();
            int hospitalUsedSize = (simulationModel.getHospital()).getSize();
            for(int i = 0; i < populationList.size(); i++) {
                Individual temp = (Individual) populationList.get(i);
                if(temp.getIndividualState() == IndividualState.DEAD) {
                    dead++;
                }
                else {
                    if(temp.isIfInfected()) {
                        g.setColor(Color.RED);
                        patient++;
                        g.fillRect(temp.getxCoord(), temp.getyCoord(), 5, 5);
                    }
                    else if(temp.getIndividualState() == IndividualState.INHOSPITAL) {
                        hospital++;
                    }
                    else {
                        g.setColor(Color.BLACK);
                        g.fillRect(temp.getxCoord(), temp.getyCoord(), 5, 5);
                        healt++;
                    }
                }
                meanSocialDistance += temp.getSocialDistance();
                if(temp.getMaskValue() == 1.0) {
                    maskUsageRatio++;
                }
            }
            patientCount.setText(String.valueOf(patient));
            healthyCount.setText(String.valueOf(healt - hospitalUsedSize));
            hospitalCount.setText(String.valueOf(hospitalUsedSize));
            deadCount.setText(String.valueOf(dead));

            maskUsageRatio = 100.0 - (100.0 * maskUsageRatio / (double) populationList.size());
            meanSocialDistance = meanSocialDistance / (double) populationList.size();
        }
    }
}
