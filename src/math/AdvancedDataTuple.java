package math;

public class AdvancedDataTuple {

	//X, Y, floor, orientation, readerNumber, tagNumber, signalstrengthes, 
	public int x, y, f, o, readernum, tag;
	public int[] signalStrength; 
	
	public AdvancedDataTuple (int x, int y, int f, int o, int r, int t, int[] signalstrength) {
		this.x=x;
		this.y=y;
		this.f=f;
		this.o=o;
		this.readernum=r;
		this.tag = t;
		this.signalStrength = signalstrength; 
		}
	
	public String toString() {
		String string = x+","+y+","+f+","+o+","+readernum+","+tag; 
		for (int i =0; i < signalStrength.length; i++){
			string=string+","+signalStrength[i];
		}
		return string; 
	}
}
	
