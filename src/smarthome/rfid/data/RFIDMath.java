package smarthome.rfid.data;

import java.awt.Point;

public class RFIDMath {
	public static double getDifference (double x, double y) {
		double difference = x - y; 
		difference = Math.pow(difference, 2); 
		
		//System.out.println("Distance is "+ difference);
		
		return difference; 
	}
	
	public static double getDistance (double[] numbers) {
		double sum = 0; 
		
		for (int i =0; i < numbers.length; i++) {
			sum = sum + numbers[i]; 
		}
		
		double distance = Math.sqrt(sum);
		
		//System.out.println("Average is "+ distance);
		
		return distance; 
	}
	
	public static double getWeight (double[] x, double[] y) {
		double[] distances = new double[y.length]; 
		
		for (int i = 0; i < y.length; i++) {
			distances[i] = RFIDMath.getDifference(x[i], y[i]);
		}
		
		double weight = RFIDMath.getDistance(distances);
		
		//System.out.println("Weight is "+weight);
		
		return weight; 
	}
	
	public Point centroidOfTriangle(Point one, Point two, Point three) {
		double xSum = one.x + two.x + three.x;
		double ySum = two.y + two.y + three.y;

		double x = xSum / 3;
		double y = ySum / 3;

		return new Point((int) x, (int) y);
	}

	public Point intersectionOfTwoLines(Point point11, Point point12, Point point21, Point point22) {

		double x1 = point11.x; 
		double x2 = point12.x; 
		double x3 = point21.x; 
		double x4 = point22.x; 
		double y1 = point11.y; 
		double y2 = point12.y; 
		double y3 = point21.y; 
		double y4 = point22.y; 
			
		double d = (x1 - x2) * (y3 - y4) - (y1 - y2) * (x3 - x4);
		if (d == 0)
			return null;

		double xi = ((x3 - x4) * (x1 * y2 - y1 * x2) - (x1 - x2)
				* (x3 * y4 - y3 * x4))
				/ d;
		double yi = ((y3 - y4) * (x1 * y2 - y1 * x2) - (y1 - y2)
				* (x3 * y4 - y3 * x4))
				/ d;

		return new Point((int)xi, (int) yi);

	}

	public Point centroidOfQuadrilateral(Point one, Point two, Point three,
			Point four) {

		Point bimedian1 = new Point ((one.x+two.x)/2, (one.y+two.y)/2);
		Point bimedian2 = new Point ((two.x+three.x)/2, (two.y+three.y)/2);
		Point bimedian3 = new Point ((three.x+four.x)/2, (three.y+four.y)/2);
		Point bimedian4 = new Point ((four.x+one.x)/2, (four.y+one.y)/2);
				
		return intersectionOfTwoLines(bimedian1, bimedian2, bimedian3, bimedian4);
	}
}
