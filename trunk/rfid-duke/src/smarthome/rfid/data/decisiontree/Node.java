package smarthome.rfid.data.decisiontree;

import smarthome.rfid.data.U;

public class Node {
	private int signal;
	private double value;
	private int left;
	private int right;
	private String name;
	
	public Node(int signal, double value, int left, int right) {
		this.signal = signal;
		this.value=value;
		this.left=left;
		this.right=right;
	}
	
	public Node(String name) {
		this.name=name;
	}
	
	public boolean isRoom() {
		return name!=null;
	}
	
	public int signal() {
		return signal;
	}
	
	public double value() {
		return value;
	}
	
	public int left() {
		return left;
	}
	
	public int right() {
		return right;
	}
	
	public String name() {
		return name;
	}
	
	public String toString() {
		return "{Signal="+signal+", value="+value+", left="+left+", right="+right+"}";
	}
	
	public static void main(String[] args) {
		Node n = new Node(0,0,0,0);
		U.Print(n.isRoom());
	}
}
