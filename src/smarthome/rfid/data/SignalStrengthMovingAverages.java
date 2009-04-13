package smarthome.rfid.data;

import java.util.Map;
import java.util.TreeMap;

import smarthome.rfid.data.filters.SignalStrengthMerger;

public class SignalStrengthMovingAverages {
	private Map<Integer, SignalStrengthQueue> ssm;
	private int queueSize;
	
	public SignalStrengthMovingAverages(int queueSize) {
		this.queueSize = queueSize;
		ssm = new TreeMap<Integer, SignalStrengthQueue>();
	}
	
	private SignalStrengthQueue tryGet(int tagId) {
		if (!ssm.containsKey(tagId)) {
			ssm.put(tagId, new SignalStrengthQueue(queueSize));
		}
		return ssm.get(tagId);
	}
	
	public void push(int tagId, SignalStrength ss) {
		SignalStrengthQueue ssq = tryGet(tagId);
		ssq.push(ss);
	}
	
	public SignalStrength value(int tagId) {
		SignalStrengthQueue ssq = tryGet(tagId);
		SignalStrength average = SignalStrengthMerger.filter(ssq);
		return average;
	}
}
