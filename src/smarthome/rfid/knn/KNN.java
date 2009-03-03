/** K-nearest neighbor rule for WEKA.
 * Class to apply the k-nearest-neighbor rule on a given
 * test set, by giving each test sample the label most
 * frequently represented among the k nearest training samples.
 * @author JohnChap
 */

package smarthome.rfid.knn;

import weka.classifiers.Classifier;
import weka.classifiers.Evaluation;
import weka.core.*;
import java.util.LinkedList;
import java.util.Hashtable;
import java.util.Enumeration;

public class KNN extends Classifier
{
    Instances trainingSet;
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
    KNN(int kValue) {
        k = kValue;
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
        // Screw this, trying to make this only accept numeric attributes
        // but accepting non-numeric class labels is too much work
        // just let the program crash for all I care.
        /*Enumeration enumAttr = data.enumerateAttributes();
        while (enumAttr.hasMoreElements()) {
            if (!((Attribute)enumAttr.nextElement()).isNumeric()) {
                throw new UnsupportedAttributeTypeException("KNN: only numeric attributes are supported");
            }
        }*/
        Enumeration en = data.enumerateInstances();
        while (en.hasMoreElements()) {
            if (((Instance)en.nextElement()).hasMissingValue()) {
                throw new NoSupportForMissingValuesException("KNN: no support for missing values.");
            }
        }
        trainingSet = new Instances(data);
        trainingSet.deleteWithMissingClass();
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

        LinkedList knnList = new LinkedList();

        double subtotal;
        DoublePair classDistance = new DoublePair(0, 0);
        // compute distances from the instance to all instances in training set
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
	/*public static void main(String[] args) 
	{
            try {
               KNN knn = new KNN();
                for (int i = 0; i < args.length; i++) {
                    if (args[i].compareTo("-kvalue") == 0) {
                       knn.k = Integer.parseInt(args[i + 1]);
                   }
                }

		System.out.println(Evaluation.evaluateModel(knn, args));
            } catch (Exception e) {
		System.out.println(e.getMessage());
            }
         }*/
}
