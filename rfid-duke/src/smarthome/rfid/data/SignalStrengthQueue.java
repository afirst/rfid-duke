package smarthome.rfid.data;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.Queue;

public class SignalStrengthQueue implements Iterable<SignalStrength> {
	private Queue<SignalStrength> q;
	private int size;
	
	public SignalStrengthQueue(int size) {
		q = new LinkedList<SignalStrength>();
		this.size = size;
	}
	
	public void push(SignalStrength v) {
		q.add(v);
		while (q.size() > size) {
			q.remove();
		}
	}
	
	public Iterator<SignalStrength> iterator() {
		return q.iterator();
	}
}
