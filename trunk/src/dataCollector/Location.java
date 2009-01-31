package dataCollector;

public class Location {
	public int x, y, f; 
	
	public Location () {
		
	}
	
	public Location (int x, int y, int f) {
		this.x = x; 
		this.y = y; 
		this.f = f;
	}
	
	public String toString (){
		return(x+","+y+","+f);
	}
}
