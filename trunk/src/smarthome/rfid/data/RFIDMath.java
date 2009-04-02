package smarthome.rfid.data;

import java.awt.Point;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class RFIDMath {
	
	public static double getDistance(Vector x, Vector y) {
		double distance = 0;
		for (int i = 0; i < y.size(); i++) {
			distance += (x.get(i) - y.get(i)) * (x.get(i) - y.get(i));
		}
		return Math.sqrt(distance);
	}
	
	public static Vector average(Vector[] vectors) {
		Vector a = new Vector(vectors[0].size());
		for (int i = 0; i < vectors.length; i++) {
			a.add(vectors[i]);
		}
		a.scale(1.0 / vectors.length);
		return a;
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
	
	public static double mean(Vector v) {
		double u = 0;
		for (int i = 0; i < v.size(); i++) {
			u += v.vector[i];
		}
		return u / v.size();
	}	
	
	public static double variance(Vector v) {
		double u = mean(v);
		double s = 0;
		for (int i = 0; i < v.size(); i++) {
			s += Math.pow((v.vector[i] - u), 2);
		}
		return s / v.size();
	}
	
	public static double stdev(Vector v) {
		return Math.sqrt(variance(v));
	}
	
	public static double mode(Vector v) {
		Vector sorted = v.copy();
		Arrays.sort(sorted.vector);
		double prev = Double.NaN;
		int count = 1;
		int maxCount = 0;
		List<Double> max = new ArrayList<Double>();
		for (int i = 0; i < v.size(); i++) {
			double d = sorted.vector[i];
			if (prev == d) {
				count++;				
			}
			else {				
				prev = d;
				count = 1;
			}
			if (count > maxCount) {
				maxCount = count;
				max.clear();
				max.add(d);
			}
			else if (count == maxCount) {
				max.add(d);
			}
		}		
		return mean(new Vector(max));
	}
	
	public static Vector removeAll(Vector v, double remove) {
		List<Double> result = new ArrayList<Double>();
		for (double d : v.vector) {
			if (d != remove) {
				result.add(d);
			}
		}
		return new Vector(result);
	}
}
