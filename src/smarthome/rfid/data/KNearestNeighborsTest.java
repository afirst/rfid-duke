package smarthome.rfid.data;
import junit.framework.TestCase;
import java.util.*;

public class KNearestNeighborsTest extends TestCase {

	private KNearestNeighbors knn;
	
	public void setUp() {
		knn = new KNearestNeighbors();
	}
	public void testfindKMin() {
		ArrayList<Double> inputList = new ArrayList<Double>();
		inputList.add(new Double(3));
		inputList.add(new Double(13));
		inputList.add(new Double(4));
		inputList.add(new Double(18));
		inputList.add(new Double(23));
		inputList.add(new Double(50));
		inputList.add(new Double(100));
		inputList.add(new Double(1));
		inputList.add(new Double(9));
		ArrayList<Integer> ans = knn.findKMin(inputList, 3);
		ArrayList<Integer> newList = new ArrayList<Integer>();
		newList.add(new Integer(7));
		newList.add(new Integer(0));
		newList.add(new Integer(2)); //the 7th, 0th, and 2nd elements
		for (int i =0; i<newList.size(); i++) {
			assertEquals(ans.get(i),newList.get(i));
		}
	}

}


