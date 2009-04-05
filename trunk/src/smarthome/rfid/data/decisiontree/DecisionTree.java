package smarthome.rfid.data.decisiontree;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;
import java.util.regex.Pattern;

import smarthome.rfid.data.Location;
import smarthome.rfid.data.Rectangle;
import smarthome.rfid.data.Room;
import smarthome.rfid.data.U;
import smarthome.rfid.data.Vector;

public class DecisionTree extends ArrayList<Node> {
	public void load(String address) throws FileNotFoundException {
		File f= new File(address);
		Scanner s = new Scanner(f);
		this.add(null);
		while (s.hasNextLine()) {
			int index = s.nextInt();
			String st = s.next();
			Node node;
			if(!st.equals("if")) {
				s.next(); 
				String name = s.next();
				node = new Node(name);			
			} else {
				st = s.next();
				String[] temp = st.split("<");
				int signal = Integer.parseInt(temp[0].substring(1)); //get 4 in x4
				double value = Double.parseDouble(temp[1]);
				s.next(); s.next();
				int left = s.nextInt();
				s.next(); s.next();
				int right = s.nextInt();
				node = new Node(signal, value, left, right);
			}
			this.add(node);
			
		}
		s.close();
	}
	
	public Room getRoom(int index, Vector signalStrength) {
		if(this.get(index).isRoom()) {
			return new Room(this.get(index).name());
		}
		Node node = this.get(index);
		if(signalStrength.get(node.signal()-1)<node.value()) {
			//go to left
			return getRoom(node.left(),signalStrength);
		} else {
			return getRoom(node.right(),signalStrength);
		}
	}
	
	public Room getRoom(Vector signalStrength) {
		return this.getRoom(1,signalStrength);
	}
	
	public static void main(String[] args) throws FileNotFoundException {
		DecisionTree tree = new DecisionTree();
		tree.load("decisionTree.txt");
		U.Print("done loading");
		//0.0	67.0	69.0	95.0	68.0
		double[] data = {0.0,	67.0,	69.0,	95.0,	68.0};
		Vector signalStrength = new Vector(data);
		U.Print(tree.getRoom(signalStrength));
	}
}
