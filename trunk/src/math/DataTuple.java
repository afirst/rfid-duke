package math;
import java.awt.Point;

/*
 * Int x, int y, int floor, signalStrength, int orientation, int reader number
 * 
 */

public class DataTuple {
	private int X, Y, floor, signalStrength, orientation, readerNumber, tagNumber, time; 
	
	public DataTuple(int x, int y, int f, int o, int r, int t, int ss, int time){
		X=x; 
		Y=y; 
		floor=f; 
		orientation = o; 
		readerNumber = r; 
		signalStrength = ss;
		tagNumber = t;
		this.time = time;
	}
	
	public DataTuple(String string) {
		String [] data = string.split(",");
		X=Integer.valueOf(data[0]); 
		Y=Integer.valueOf(data[1]); 
		floor=Integer.valueOf(data[2]); 
		orientation = Integer.valueOf(data[3]); 
		readerNumber = Integer.valueOf(data[4]); 		
		tagNumber = Integer.valueOf(data[5]);
		signalStrength = Integer.valueOf(data[6]);
		time = Integer.valueOf(data[7]);
	}	
	
	public int getX(){
		return X; 
	}
	
	public int getY(){
		return Y; 
	}
	
	public int getFloor(){
		return floor; 
	}
	
	public int getSignalStrength(){
		return signalStrength; 
	}
	
	public int getOrientation(){
		return orientation; 
	}
	
	public int getReaderNumber(){
		return readerNumber; 
	}
	
	public int getTagNumber() {
		return tagNumber;
	}
	
	public Point getPoint(){
		return new Point(X, Y);
	}
	
	public int getTime() {
		return time;
	}
	
	public String toString(){
		return X+","+Y+","+floor+","+orientation+","+readerNumber+","+tagNumber+","+signalStrength+","+time; 
	}
	
	public boolean equals (DataTuple data) {
		if (X == (data.getX()) && Y == (data.getY()) && floor == (data.getFloor())
				&& signalStrength == (data.getSignalStrength())
				&& orientation == (data.getOrientation())
				&& readerNumber == (data.getReaderNumber()) 
				&& tagNumber == (data.getTagNumber())
				&& time == (data.getTime())) {
			return true; 
		}
		
		return false;
	}
}

