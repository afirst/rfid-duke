/** K-nearest neighbor with partial distance
 * This works by omitting certain specified attributes in each
 * distance calculation. If the distance is greater than the distance
 * of the greatest full-dimensional k-nearest-neighbor, then it will
 * be dismissed with fewer computations. If not, the full d-dimensional
 * sample will be tested against the current nearest neighbors.
 * @author JohnChap
 */

package smarthome.rfid.knn;

import weka.classifiers.Classifier;
import weka.classifiers.Evaluation;
import weka.core.*;
import java.util.LinkedList;
import java.util.Hashtable;
import java.util.Enumeration;

public class KnnPartialDistance extends Classifier
{
    Instances trainingSet;
    Instance[] moddedTrainingSet;
    int[] leaveOutAttributes;
    boolean[] isIncluded;
    int k;

    // inner class to hold a pair of doubles 
    // (for maintaining a running list of nearest neighbors)
    class DoublePair 
    {
        double _class;
        double distance;

        DoublePair(double a, double b) {
            _class = a;
            distance = b;
        }
    }


    /** Set k constructor
     * @param k
     */
    KnnPartialDistance(int kValue, int[] leaveOut) {
        k = kValue;
        leaveOutAttributes = leaveOut;
    }
 
        
    /** Returns the description of the classifier. 
     * @return description of the KNN class.
     */
    public String globalInfo()
    {
        return "Class to apply the k-nearest-neighbor rule on a given" +
                "test set, by giving each test sample the label most" +
                "frequently represented among the k nearest training" +
                "samples.";
    }

    /** sets k 
     * @param new_k  new k value.
     */
    public void set_k(int new_k) {
        k = new_k;
    }

    /**gets k
     * @return  k
     */
    public int get_k() {
        return k;
    }
        
       
    /** Builds the classifier using the given training set.
     * @param Instances
     */
    public void buildClassifier(Instances data) throws Exception
    {
        Enumeration en = data.enumerateInstances();
        while (en.hasMoreElements()) {
            if (((Instance)en.nextElement()).hasMissingValue()) {
                throw new NoSupportForMissingValuesException("KNN: no support for missing values.");
            }
        }
        trainingSet = new Instances(data);
        trainingSet.deleteWithMissingClass();
        
        // too complicated for words
        isIncluded = new boolean[trainingSet.numAttributes()];
        for (int i = 0; i < trainingSet.numAttributes(); i++) {
            isIncluded[i] = true;
            for (int j = 0; j < leaveOutAttributes.length; j++) {
                if (leaveOutAttributes[j] == i) {
                    isIncluded[i] = false;
                    break;
                }
            }
        }
        
        moddedTrainingSet = new Instance[trainingSet.numInstances()];
        Instance tempInstance;
        int blankIndex;
        int modSize = trainingSet.numAttributes() - leaveOutAttributes.length;
        for (int i = 0; i < trainingSet.numInstances(); i++) {
            tempInstance = new Instance(modSize);
            blankIndex = 0;
            for (int j = 0; j < trainingSet.numAttributes(); j++) {
                if (isIncluded[j]) {
                    tempInstance.setValue(blankIndex, trainingSet.instance(i).value(j));
                    blankIndex++;
                }
            }
            moddedTrainingSet[i] = tempInstance;
        }
    }

    /** Classifies a single test instance by comparing distances
     * between the instance and the points in the training set.
     * @param instance  instance to classify.
     * @return  predicted most likely class for the instance or Instance.missingValue() if no prediction is made.
     */
    public double classifyInstance(Instance instance) throws Exception
    {
        Double greatestClass = new Double(0.0);
        if (instance.hasMissingValue()) {
            throw new NoSupportForMissingValuesException("KNN: no support for missing values.");
        }
        
        Instance moddedInstance = new Instance(instance.numAttributes() - leaveOutAttributes.length);
     
        // pre-make the modified instance
        int blankIndex = 0;
        for (int j = 0; j < instance.numAttributes(); j++) {
            if (isIncluded[j]) {
                moddedInstance.setValue(blankIndex, instance.value(j));
                blankIndex++;
            }
        }
        
        LinkedList knnList = new LinkedList();

        double subtotal;
        double subtotalP;
        DoublePair classDistance = new DoublePair(0, 0);
        
        // partial distance
        for (int p = 0; p < moddedTrainingSet.length; p++) {
            subtotalP = 0;			
            for (int q = 0; q < moddedInstance.numAttributes(); q++) {
                if (q != moddedInstance.numAttributes() - 1) {
                    // Euclidean
                    subtotalP += Math.pow((moddedTrainingSet[p].value(q) - moddedInstance.value(q)), 2.0);
                }
            }
            subtotalP = Math.sqrt(subtotalP);	

            // manage the current k nearest neighbors
            for (int q = 0; q < k; q++) { 
                if (knnList.size() < k) {
                    if (q == knnList.size()) {
                            // DO REGULAR KNN list managing
                            for (int i = 0; i < trainingSet.numInstances(); i++) {
                                subtotal = 0;			
                                for (int j = 0; j < instance.numAttributes(); j++) {
                                    if (j != instance.classIndex()) {
                                        // Euclidean
                                        subtotal += Math.pow((trainingSet.instance(i).value(j) - instance.value(j)), 2.0);
                                    }
                                }
                                subtotal = Math.sqrt(subtotal);	

                                // manage the current k nearest neighbors
                                for (int j = 0; j < k; j++) { 
                                    if (knnList.size() < k) {
                                        if (j == knnList.size()) {
                                            classDistance._class = trainingSet.instance(i).classValue();
                                            classDistance.distance = subtotal;
                                            knnList.addLast(classDistance);
                                            break;
                                        }
                                    }
                                    if (subtotal < ((DoublePair)knnList.get(j)).distance) {
                                        classDistance._class = trainingSet.instance(i).classValue();
                                        classDistance.distance = subtotal;
                                        knnList.add(j, classDistance);

                                        if (knnList.size() > k) {
                                            knnList.remove(k);
                                        }	
                                        break;
                                    }
                                }
                            }
                      //  classDistance._class = trainingSet.instance(i).classValue();
                     //   classDistance.distance = subtotal;
                      //  knnList.addLast(classDistance);
                        break;
                    }
                }
                if (subtotalP < ((DoublePair)knnList.get(q)).distance) {
                   // COMPUTE REGULAR KNN AGAIN
                    for (int i = 0; i < trainingSet.numInstances(); i++) {
                        subtotal = 0;			
                        for (int j = 0; j < instance.numAttributes(); j++) {
                            if (j != instance.classIndex()) {
                                // Euclidean
                                subtotal += Math.pow((trainingSet.instance(i).value(j) - instance.value(j)), 2.0);
                            }
                        }
                        subtotal = Math.sqrt(subtotal);	

                        // manage the current k nearest neighbors
                        for (int j = 0; j < k; j++) { 
                            if (knnList.size() < k) {
                                if (j == knnList.size()) {
                                    classDistance._class = trainingSet.instance(i).classValue();
                                    classDistance.distance = subtotal;
                                    knnList.addLast(classDistance);
                                    break;
                                }
                            }
                            if (subtotal < ((DoublePair)knnList.get(j)).distance) {
                                classDistance._class = trainingSet.instance(i).classValue();
                                classDistance.distance = subtotal;
                                knnList.add(j, classDistance);

                                if (knnList.size() > k) {
                                    knnList.remove(k);
                                }	
                                break;
                            }
                        }
                    }
                }
            }
        }
        // I guess that will work?
        // it is 5:34am

        // The hash table is an efficient way to store
        // classes (key) and number of occurances (values)
        // in the k nearest neighbors.
        Hashtable hashT = new Hashtable((trainingSet.numClasses() * 2));

        DoublePair dubPair;
        Integer counter;
        while (knnList.size() > 0) {
            dubPair = (DoublePair)knnList.removeFirst();
            counter = (Integer)(hashT.get(new Double(dubPair._class)));
            if (counter == null) {
                // iff the key doesn't yet exist in hash table
                counter = new Integer(0);
            }
            hashT.put(new Double(dubPair._class), new Integer(counter.intValue() + 1));
        }	

        Integer mostFrequent = new Integer(0);
        Double currentClass;
        Integer currentValue;

        // what's interesting about this part of the code is that if 
        // there are two classes with the same number of occurances, 
        // then we're in trouble.
        Enumeration enumClass = hashT.keys();
        while (enumClass.hasMoreElements()) {
            currentClass = (Double)(enumClass.nextElement());
            currentValue = (Integer)(hashT.get(currentClass));
            if (currentValue.compareTo(mostFrequent) > 0) {
                greatestClass = currentClass;
                mostFrequent = currentValue;
            }
        }
        return greatestClass.doubleValue();

    }


	@Override
	public String getRevision() {
		// TODO Auto-generated method stub
		return null;
	}


	/*
	 * main method
	 */
	public static void main(String[] args) 
	{
            try {
               KNN knn = new KNN(4);
                for (int i = 0; i < args.length; i++) {
                    if (args[i].compareTo("-kvalue") == 0) {
                       knn.k = Integer.parseInt(args[i + 1]);
                   }
                }

		System.out.println(Evaluation.evaluateModel(knn, args));
            } catch (Exception e) {
		System.out.println(e.getMessage());
            }
    }
}
