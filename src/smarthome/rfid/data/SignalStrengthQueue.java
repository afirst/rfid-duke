package smarthome.rfid.data;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.Queue;

public class SignalStrengthQueue implements Iterable<Vector> {
	private Queue<Vector> q;
	private int size;
	
	public SignalStrengthQueue(int size) {
		q = new LinkedList<Vector>();
		this.size = size;
	}
	
	public void push(Vector v) {
		q.add(v);
		while (q.size() > size) {
			q.remove();
		}
	}
	
	public Iterator<Vector> iterator() {
		return q.iterator();
	}
}
