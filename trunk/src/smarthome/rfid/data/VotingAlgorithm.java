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

	public VotingAlgorithm(int k, int regressionOrder) throws FileNotFoundException {
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
		double x = (a.x()+b.x()+c.x())/3;
		double y = (a.y()+b.y()+c.y())/3;
		double z = c.z()*.5 + a.z()*.25+b.z()*.25;
		return new Location(x,y,z);
	}
	
	public void setTrainingData(TrainingPointList trainingData) {
		this.data = trainingData;
	}
	
}
