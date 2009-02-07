package smarthome.rfid.data;

import java.io.*;
import java.util.*;

public class ProbabilityKNN {
	private static double DIST = 100.0;
	private static double INF = 1000000.0;
	public double posteriorDistribution(Location prior) {
		//Given this location, consider only places 
		//that are within DIST from this location
		return 0.0;
	}
	
	public double computeDistance(Location prior, String[] controlSignal, String[] testSignal) {
		//controlSignal has both location
		double dist = 0.0;
		double[] pr = prior.data();
		double[] con = toDouble(controlSignal);
		dist = (pr[0]-con[0])*(pr[0]-con[0]) + (pr[1]-con[1])*(pr[1]-con[1]) + (pr[2]*80-con[2]*80)*(pr[2]*80-con[2]*80);
		if (dist <= DIST) {
			//assign infinity
			return INF;
		} else {
			for (int i=4; i<controlSignal.length; i++) {
				dist = Double.parseDouble(controlSignal[i])+ dist;
			}
		}
		return dist;
	}
	
	private double[] toDouble(String[] list) {
		double[] ret = new double[list.length];
		for (int i=0; i<list.length; i++) {
			ret[i] = Double.parseDouble(list[i]);
		}
		return ret;
	}
}
