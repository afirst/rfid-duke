package math;

import dataCollector.Location;

public interface Algorithm {
	void setTrainingData(AdvancedDataTuple[] tags, Antenna[] antennas);
	Location getLocation(int tagId, int[] signalStrength);
}
