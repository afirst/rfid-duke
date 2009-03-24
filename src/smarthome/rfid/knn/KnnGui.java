/*
 * KNNgui.java
 *
 * Created on December 17, 2005, 6:27 PM
 */

package smarthome.rfid.data.weka;

import javax.swing.*;
import java.io.*;
import weka.gui.arffviewer.ArffPanel;
import java.awt.event.*;
import weka.core.*;
import weka.classifiers.*;
import weka.core.converters.ArffLoader;


/**
 * A graphical implementation to perform k-nearest-neighbor classification
 * @author  JohnChap
 */
public class KnnGui extends javax.swing.JFrame {
    
    private static String title = "John KNN";
    private File trainingFilePath = new File(".");
    private File testFilePath = new File(".");
    private Instances trainingSet;
    private Instances testSet;
    private int classifierSelect = 0;
    private int testSettingsSelect = 0;
    private int[] leaveOutAttribute;
    private boolean resultsToFile = false;
    private ArffPanel arffPanel1;
    private ArffPanel arffPanel2;
    private int testNumber = 0;
    
    /** Creates new form KNNgui */
    public KnnGui() {
        super(title);
        initComponents();
        initRadioButtons();
        initButtons();
        
        //setting up for first run
        useTestSetRadio.setEnabled(false);
        partialDistanceOptionsButton.setEnabled(false);
        
        writeResultsCheckBox.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent event) {
                if (!resultsToFile) {
                    resultsToFile = true;
                }
                else resultsToFile = false;
            }
	});
    }
    
    
    /** finish initializing buttons and binding them to actions
     */
    private void initButtons() {
        trainingLoadButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent event) {
                loadTrainingFile();
                tabPanel2.setEnabled(true);
            }
	});
        
        testLoadButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent event) {
                loadTestFile();
                useTestSetRadio.setEnabled(true);
            }
	});
        
        partialDistanceOptionsButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent event) {
                partialDistanceDialog();
            }
	});
        
        startButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent event) {
                runKnn();
            }
	});  
    }
    
    
    
    
    /** finishes the Radio button initializations not covered in initComponents
     */
    private void initRadioButtons() {
        
        // classifier selection
        simpleKNNRadio.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent event) {
                partialDistanceOptionsButton.setEnabled(false);
                classifierSelect = 0;
            }
        });
         
        partialDistanceRadio.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent event) {
                partialDistanceOptionsButton.setEnabled(true);
                classifierSelect = 1;
            }
        });
        
        // test settings
        useTestSetRadio.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent event) {
                testSettingsSelect = 0;
                startButton.setEnabled(true);
            }
        });
        
        useTrainingSetRadio.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent event) {
                testSettingsSelect = 1;
                startButton.setEnabled(true);
            }
        });
        
        
        // grouping radio buttons
        final ButtonGroup classifierButtonGroup = new ButtonGroup();
        classifierButtonGroup.add(simpleKNNRadio);
        classifierButtonGroup.add(partialDistanceRadio);
        
        final ButtonGroup settingsButtonGroup = new ButtonGroup();
        settingsButtonGroup.add(useTrainingSetRadio);
        settingsButtonGroup.add(useTestSetRadio);
    }
    
   /** displays the patial distance dialog.
    */
    private void partialDistanceDialog() {
        String leaveOut = (String)JOptionPane.showInputDialog(partialDistanceOptionsButton,
                    "Enter the indices of attributes to leave out\n"
                    + "(e.g. 1,2,3...)", "Partial distance Options",
                    JOptionPane.PLAIN_MESSAGE);

        printStatus("Read user input: " + leaveOut);
        try {
            if ((leaveOut != null) && (leaveOut.length() > 0)) {
                String[] strArray = leaveOut.split(",");
                int[] indices = new int[strArray.length];
                for (int i = 0; i < strArray.length; i++) {
                    indices[i] = Integer.parseInt(strArray[i]);
                }
            }
        }
        catch (NumberFormatException nfe) {
            printStatus("Partial distance error: " + nfe.getMessage());
        }
        return;
    }
    
    
    /**
     * Prints a message in the status text area
     * @param message  message to be printed to status text area
     */
    private void printStatus(String message) {
        statusTextArea.append(message + "\n");
    }
    
    /** Presents the training set file chooser dialog
     */
    private void loadTrainingFile() {	
        try {
            JFileChooser openChooser = new JFileChooser(trainingFilePath);
            openChooser.showOpenDialog(null);
            trainingFilePath = openChooser.getSelectedFile();
            ArffLoader arffLoad = new ArffLoader();
            arffLoad.setSource(trainingFilePath);
            arffPanel1 = new ArffPanel(trainingFilePath.getPath());
            trainingArffScrollPane.setViewportView(arffPanel1);
            trainingSet = arffLoad.getDataSet();
            printStatus("Training file: " + trainingFilePath.getPath() + " Loaded.");
        }
        catch (Exception e) {
            printStatus("Error loading training set: " + e.getMessage());
        }
  
    } 
    
      /** Presents the test set file chooser dialog
     */
    private void loadTestFile() {
        try {
            JFileChooser openChooser = new JFileChooser(testFilePath);
            openChooser.showOpenDialog(null);
            testFilePath = openChooser.getSelectedFile();
            ArffLoader arffLoad = new ArffLoader();
            arffLoad.setSource(trainingFilePath);
            arffPanel2 = new ArffPanel(testFilePath.getPath());
            testArffScrollPane.setViewportView(arffPanel2);
            useTestSetRadio.setEnabled(true);
            testSet = arffLoad.getDataSet();
            printStatus("Test file: " + testFilePath.getPath() + " Loaded.");
        }
        catch (Exception e) {
            printStatus("Error loading test set: " + e.getMessage());
        }
    } 
    
    /** builds a Weka-compliant array of arguments for Evaluation
     * @param trainingFilename  name of the file containing the training set.
     * @param testFilename  name of the file containing the test set.
     * @param classAttribute  index of the class attribute (1,2,3 etc.)
     * @return an array of arguments
     */
    
    private String[] argumentBuilder(String trainingFilename, String testFilename, int classAttribute) {
        String[] args = new String[6];
        args[0] = "-t";
        args[1] = trainingFilename;
        args[2] = "-T";
        args[3] = testFilename;
        args[4] = "-c";
        args[5] = Integer.toString(classAttribute);
        return args;
    }
    
    
    private void runKnn() {
        try {
            testNumber++;
            if (trainingSet.classIndex() < 0) {
                    trainingSet.setClassIndex(trainingSet.numAttributes() - 1);
            }
            startButton.setEnabled(false);
            //Evaluation knnEval = new Evaluation(trainingSet);
            String output;
            int classInd;
            
            if (classifierSelect == 0) {
                KNN knn = new KNN(Integer.parseInt(kValueField.getText()));

                // this code crashes the program
               /* output = Evaluation.evaluateModel(knn, argumentBuilder(trainingFilePath.getPath(),
                        testFilePath.getPath(), trainingSet.classIndex()));
                */
                if (testSettingsSelect == 0) {
                    output = evaluate(knn, trainingSet, testSet);
                }
                else output = evaluate(knn, trainingSet, trainingSet);
                
                
                resultsTextArea.append("Test [" + testNumber + "] results: simple knn\n" +
                        "-------------\n" + output);

               printStatus("KNN classification done.");
                if (resultsToFile) {
                    FileWriter outputFile = new FileWriter("results.txt");
                    outputFile.write(resultsTextArea.getText());
                    outputFile.close();
                    printStatus("Results written to file.");
                }
            }
            else {
                KnnPartialDistance knnPD = new KnnPartialDistance(Integer.parseInt(kValueField.getText()), leaveOutAttribute);
                if (testSettingsSelect == 0) {
                    output = evaluate(knnPD, trainingSet, testSet);
                }
                else output = evaluate(knnPD, trainingSet, trainingSet);
                
                resultsTextArea.append("Test [" + testNumber + "] results: knn with partial distance\n" +
                        "-------------\n" + output);

               printStatus("KNN partial distance classification done.");
                if (resultsToFile) {
                    FileWriter outputFile = new FileWriter("results.txt");
                    outputFile.write(resultsTextArea.getText());
                    outputFile.close();
                    printStatus("Results written to file.");
                }
            }
            
            startButton.setEnabled(true);
        }
        catch (Exception e) {
            printStatus("Error in execution: " + e.getMessage());
            startButton.setEnabled(true);
        }
    }
   
    /** the evaluateModel in Weka crashes my damn program, so I have to write my own
     * function. Oh it is SO not going to be as good as the weka stuff.
     */
    private String evaluate(Classifier knn, Instances trainingS, Instances testS) {
        String results = "";
        try {
            // no threads or anything, because I don't have the time.
            double[] classification = new double[testS.numInstances()];
            testS.setClassIndex(trainingSet.classIndex());
            long startTime = System.currentTimeMillis();

            int correctClassify = 0;
            knn.buildClassifier(trainingS);
            for (int i = 0; i < testS.numInstances(); i++) {
                classification[i] = knn.classifyInstance(testS.instance(i));
                if (classification[i] == testS.instance(i).classValue()) {
                    correctClassify++;
                }
            }
            
            long stopTime = System.currentTimeMillis();
            long execTime = stopTime - startTime;

            results = "Attributes:                 " + trainingS.numAttributes() + "\n" +
                      "Instances in training set:  " + trainingS.numInstances() + "\n" +
                      "Instances in test set:      " + testS.numInstances() + "\n\n" +
                      "Instances correctly classified:\n" +
                      "     " + correctClassify + " / " + testS.numInstances() + "\n\n" +
                      "Execution time: " + (double)execTime/(double)1000 + " seconds\n\n" +
                      "Average rate of classification:\n" +
                      "     " + (double)(testS.numInstances()) / ((double)execTime/(double)1000) + " samples/sec.\n\n";
        }
        catch (Exception e) {
            printStatus("Error evaluating: " + e.getMessage());
        }
        return results;
    }
    
    
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc=" Generated Code ">//GEN-BEGIN:initComponents
    private void initComponents() {
        tabbedPane = new javax.swing.JTabbedPane();
        tabPanel1 = new javax.swing.JPanel();
        fileChoosePanel = new javax.swing.JPanel();
        trainingLoadButton = new javax.swing.JButton();
        testLoadButton = new javax.swing.JButton();
        trainingArffPanel = new javax.swing.JPanel();
        trainingArffScrollPane = new javax.swing.JScrollPane();
        testArffPanel = new javax.swing.JPanel();
        testArffScrollPane = new javax.swing.JScrollPane();
        tabPanel2 = new javax.swing.JPanel();
        classifierPanel = new javax.swing.JPanel();
        simpleKNNRadio = new javax.swing.JRadioButton();
        partialDistanceRadio = new javax.swing.JRadioButton();
        partialDistanceOptionsButton = new javax.swing.JButton();
        settingsPanel = new javax.swing.JPanel();
        useTestSetRadio = new javax.swing.JRadioButton();
        useTrainingSetRadio = new javax.swing.JRadioButton();
        kValueLabel = new javax.swing.JLabel();
        kValueField = new javax.swing.JTextField();
        writeResultsCheckBox = new javax.swing.JCheckBox();
        startButton = new javax.swing.JButton();
        resultsPanel = new javax.swing.JPanel();
        resultsScrollPane = new javax.swing.JScrollPane();
        resultsTextArea = new javax.swing.JTextArea();
        statusPanel = new javax.swing.JPanel();
        statusScrollPane = new javax.swing.JScrollPane();
        statusTextArea = new javax.swing.JTextArea();

        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setResizable(false);
        tabPanel1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        fileChoosePanel.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        fileChoosePanel.setBorder(new javax.swing.border.TitledBorder(new javax.swing.border.EtchedBorder(), "Load data sets"));
        fileChoosePanel.setMaximumSize(new java.awt.Dimension(460, 100));
        fileChoosePanel.setMinimumSize(new java.awt.Dimension(460, 100));
        trainingLoadButton.setText("Load Training Set");
        fileChoosePanel.add(trainingLoadButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 20, 320, -1));

        testLoadButton.setText("Load Test Set");
        fileChoosePanel.add(testLoadButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(380, 20, 330, -1));

        tabPanel1.add(fileChoosePanel, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 10, 730, 60));

        trainingArffPanel.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        trainingArffPanel.setBorder(new javax.swing.border.TitledBorder(new javax.swing.border.EtchedBorder(), "Training Set", javax.swing.border.TitledBorder.CENTER, javax.swing.border.TitledBorder.DEFAULT_POSITION));
        trainingArffPanel.add(trainingArffScrollPane, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 20, 340, 220));

        tabPanel1.add(trainingArffPanel, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 70, 360, 250));

        testArffPanel.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        testArffPanel.setBorder(new javax.swing.border.TitledBorder(null, "Test set", javax.swing.border.TitledBorder.CENTER, javax.swing.border.TitledBorder.TOP));
        testArffPanel.add(testArffScrollPane, new org.netbeans.lib.awtextra.AbsoluteConstraints(12, 22, 350, 220));

        tabPanel1.add(testArffPanel, new org.netbeans.lib.awtextra.AbsoluteConstraints(360, 70, 370, 250));

        tabbedPane.addTab("Data sets", tabPanel1);

        tabPanel2.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        classifierPanel.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        classifierPanel.setBorder(new javax.swing.border.TitledBorder(new javax.swing.border.EtchedBorder(), "Classifier", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.TOP));
        classifierPanel.setMaximumSize(new java.awt.Dimension(320, 120));
        classifierPanel.setMinimumSize(new java.awt.Dimension(320, 120));
        simpleKNNRadio.setSelected(true);
        simpleKNNRadio.setText("Simple k-nearest-neighbor");
        classifierPanel.add(simpleKNNRadio, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 20, -1, -1));

        partialDistanceRadio.setText("knn with partial distance");
        classifierPanel.add(partialDistanceRadio, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 50, -1, -1));

        partialDistanceOptionsButton.setLabel("Options");
        classifierPanel.add(partialDistanceOptionsButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(160, 50, -1, -1));

        tabPanel2.add(classifierPanel, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 10, 250, 120));

        settingsPanel.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        settingsPanel.setBorder(new javax.swing.border.TitledBorder(new javax.swing.border.EtchedBorder(), "Settings", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.TOP));
        settingsPanel.setMaximumSize(new java.awt.Dimension(320, 150));
        settingsPanel.setMinimumSize(new java.awt.Dimension(320, 150));
        useTestSetRadio.setText("Classify test set");
        settingsPanel.add(useTestSetRadio, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 60, -1, -1));

        useTrainingSetRadio.setText("Classify training set");
        settingsPanel.add(useTrainingSetRadio, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 90, -1, -1));

        kValueLabel.setText("Value of k");
        settingsPanel.add(kValueLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 30, 60, -1));

        kValueField.setText("10");
        settingsPanel.add(kValueField, new org.netbeans.lib.awtextra.AbsoluteConstraints(80, 30, 90, -1));

        writeResultsCheckBox.setText("Write results to a file");
        settingsPanel.add(writeResultsCheckBox, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 120, -1, -1));

        tabPanel2.add(settingsPanel, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 130, 250, 150));

        startButton.setText("Start");
        startButton.setEnabled(false);
        tabPanel2.add(startButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(70, 290, 130, -1));

        resultsPanel.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        resultsPanel.setBorder(new javax.swing.border.TitledBorder(new javax.swing.border.EtchedBorder(), "Test results"));
        resultsTextArea.setEditable(false);
        resultsTextArea.setLineWrap(true);
        resultsScrollPane.setViewportView(resultsTextArea);

        resultsPanel.add(resultsScrollPane, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 20, 440, 280));

        tabPanel2.add(resultsPanel, new org.netbeans.lib.awtextra.AbsoluteConstraints(260, 10, 470, 310));

        tabbedPane.addTab("Classify", tabPanel2);

        getContentPane().add(tabbedPane, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 740, 350));

        statusPanel.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        statusPanel.setBorder(new javax.swing.border.TitledBorder(new javax.swing.border.EtchedBorder(), "Status", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.TOP));
        statusTextArea.setEditable(false);
        statusTextArea.setLineWrap(true);
        statusScrollPane.setViewportView(statusTextArea);

        statusPanel.add(statusScrollPane, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 20, 720, 110));

        getContentPane().add(statusPanel, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 350, 740, 140));

        pack();
    }
    // </editor-fold>//GEN-END:initComponents
    
 
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel classifierPanel;
    private javax.swing.JPanel fileChoosePanel;
    private javax.swing.JTextField kValueField;
    private javax.swing.JLabel kValueLabel;
    private javax.swing.JButton partialDistanceOptionsButton;
    private javax.swing.JRadioButton partialDistanceRadio;
    private javax.swing.JPanel resultsPanel;
    private javax.swing.JScrollPane resultsScrollPane;
    private javax.swing.JTextArea resultsTextArea;
    private javax.swing.JPanel settingsPanel;
    private javax.swing.JRadioButton simpleKNNRadio;
    private javax.swing.JButton startButton;
    private javax.swing.JPanel statusPanel;
    private javax.swing.JScrollPane statusScrollPane;
    private javax.swing.JTextArea statusTextArea;
    private javax.swing.JPanel tabPanel1;
    private javax.swing.JPanel tabPanel2;
    private javax.swing.JTabbedPane tabbedPane;
    private javax.swing.JPanel testArffPanel;
    private javax.swing.JScrollPane testArffScrollPane;
    private javax.swing.JButton testLoadButton;
    private javax.swing.JPanel trainingArffPanel;
    private javax.swing.JScrollPane trainingArffScrollPane;
    private javax.swing.JButton trainingLoadButton;
    private javax.swing.JRadioButton useTestSetRadio;
    private javax.swing.JRadioButton useTrainingSetRadio;
    private javax.swing.JCheckBox writeResultsCheckBox;
    // End of variables declaration//GEN-END:variables

}
