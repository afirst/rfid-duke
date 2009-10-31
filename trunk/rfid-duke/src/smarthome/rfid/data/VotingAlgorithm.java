package smarthome.rfid.data;

import java.io.FileNotFoundException;

/**
 * voting algorithm that incorporates KNearestNeighbor, Decision Tree, and Regression Algorithm
 * @author Perry Zheng
 *
 */
public class VotingAlgorithm implements Algorithm {
	int k; 
	int order; 
	KNearestNeighbor knn;
	DecisionTreeAlgorithm tree;
	RegressionAlgorithm reg;
	private TrainingPointList data;

	public VotingAlgorithm(int k, int regressionOrder){
		this.k = k;
		this.order = regressionOrder;
		knn = new KNearestNeighbor(k);
		tree = new DecisionTreeAlgorithm();
		reg = new RegressionAlgorithm(order);
	}
	
	public Location getLocation(int tagId, Vector signalStrength) {
		Location neiLoc = knn.getLocation(tagId, signalStrength);
		Location treeLoc = tree.getLocation(tagId, signalStrength);
		Location regLoc = reg.getLocation(tagId, signalStrength);
		return averageLoc(neiLoc, treeLoc, regLoc);
	}

	public Location averageLoc(Location a, Location b, Location c) {
		int n = 3;
		if (a == null) { a = new Location(); n--; }
		 { b = new Location(); n--; }
		if (c == null) { c = new Location(); n--; }
		double x = (a.x()*1.25+b.x()+c.x()*.75)/n;
		double y = (a.y()+b.y()+c.y())/n;
		double z = c.z()*.5 + a.z()*.25+b.z()*.25;
		return new Location(x,y,z);
	}
	
	public void setTrainingData(TrainingPointList trainingData) {
		this.data = trainingData;
		knn.setTrainingData(trainingData);
		tree.setTrainingData(trainingData);
		reg.setTrainingData(trainingData);
	}
	
}
