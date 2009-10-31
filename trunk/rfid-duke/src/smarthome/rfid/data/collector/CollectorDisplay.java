package smarthome.rfid.data.collector;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.*; 
import java.util.List;

import javax.imageio.*;
import javax.swing.JPanel;

import smarthome.rfid.data.Algorithm;
import smarthome.rfid.data.DecisionTreeAlgorithm;
import smarthome.rfid.data.FilteredKNN;
import smarthome.rfid.data.KNearestNeighbor;
import smarthome.rfid.data.Location;
import smarthome.rfid.data.RegressionAlgorithm;
import smarthome.rfid.data.SignalStrength;
import smarthome.rfid.data.SignalStrengthMovingAverages;
import smarthome.rfid.data.TrainingPoint;
import smarthome.rfid.data.TrainingPointList;
import smarthome.rfid.data.VotingAlgorithm;
import smarthome.rfid.service.RSSITracker;



public class CollectorDisplay extends JPanel {
	protected File BACKGROUND_FILE;
	protected CollectorModel myModel; 
	private ArrayList <Point> pointBuffer = new ArrayList <Point>();
	protected static Image backgroundImage;
	private final int circleSize = 10; 
	private CollectorDisplay myDisplay = this;	
	private SignalStrengthMovingAverages ssma = new SignalStrengthMovingAverages(5);
	private KNearestNeighbor algorithm = new KNearestNeighbor(4);
	private Algorithm algorithm2 = new DecisionTreeAlgorithm();
	private Algorithm algorithm3 = new RegressionAlgorithm(2);
	private Algorithm algorithm4 = new VotingAlgorithm(4, 2);
	private List<TrainingPoint> plots = new ArrayList<TrainingPoint>();
	
	public CollectorDisplay (CollectorModel model, File file) {
		myModel = model; 
		BACKGROUND_FILE = file; 
		initialize();		
		TrainingPointList trainingData = new TrainingPointList();
		try {
			trainingData.load("data.txt");
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		algorithm.setTrainingData(trainingData);
		algorithm2.setTrainingData(trainingData);
		algorithm3.setTrainingData(trainingData);
		algorithm4.setTrainingData(trainingData);
	}
	
	public void readTag(int tagID, int antennaID, int rssi) {
		if (tagID == 101) {			
			ssma.push(tagID, new SignalStrength(myModel.getRSSI(tagID)));
			Location loc = algorithm.getLocation(tagID, ssma.value(tagID));
			TreeMap<Double, TrainingPoint> ss = algorithm.createDistanceMap(ssma.value(tagID));
			System.out.println(ssma.value(tagID));
			System.out.println(loc);			
			plots.clear();		
			int k = 0;
			for (Double distance : ss.keySet()) {
				if (k == 4) break;
				TrainingPoint pt = ss.get(distance);
				TrainingPoint newpt = new TrainingPoint(pt.location(), k, pt.signalStrength());
				plots.add(newpt);
				k++;
			}			
			
			plots.clear();
			plots.add(new TrainingPoint(algorithm.getLocation(tagID, ssma.value(tagID)), 0, null));
			try {
			plots.add(new TrainingPoint(algorithm2.getLocation(tagID, ssma.value(tagID)), 1, null));
			}
			catch(Exception e) {

			}
			plots.add(new TrainingPoint(algorithm3.getLocation(tagID, ssma.value(tagID)), 2, null));
			plots.add(new TrainingPoint(algorithm4.getLocation(tagID, ssma.value(tagID)), 3, null));
			repaint();
		}
	}
	
	public void initialize() {

		try {			
			backgroundImage = ImageIO.read(BACKGROUND_FILE);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		this.setPreferredSize(new Dimension(837, 249));
		this.setMinimumSize(new Dimension(837, 249));
		this.setBackground(Color.yellow);
		this.addMouseListener(new MouseListener() {
			public void mouseClicked(MouseEvent arg0) {
				Point p = arg0.getPoint();
				for (int i =0; i < pointBuffer.size(); i++){
					double distance = p.distance(pointBuffer.get(i))*CollectorDisplay.this.getWidth();
					if (distance <= circleSize) {
						//change p into previous point
						p = pointBuffer.get(i); 
					}		
				}
				
				CollectorDialog dialogue = new CollectorDialog(myModel, p, myDisplay, CollectorDisplay.this.getWidth());
			}
			
			public void mouseEntered(MouseEvent arg0) {
			}
			public void mouseExited(MouseEvent arg0) {
			}
			public void mousePressed(MouseEvent arg0) {
				
			}
			public void mouseReleased(MouseEvent arg0) {
			}			
		});
	}
	
	
	public void reset() {
		//Update point buffer
		/*pointBuffer.clear();		
		Iterator <TrainingPoint> iterator = myModel.iterator();
		
		while (iterator.hasNext()){
			Location point = iterator.next().location(); 
			if(!pointBuffer.contains(point)){
				pointBuffer.add(new Point((int)point.x(), (int)point.y()));
			}
		}	*/	
		repaint();
	}
	
	@Override
	public void paint(Graphics g) {
		super.paint(g);		
		int w = this.getWidth();
		int h = this.getHeight();				
		
		g.drawImage(backgroundImage, 0, 0, w, h, null);		
		
		// draw points
		Iterator<TrainingPoint> it = myModel.iterator();
		while (it.hasNext()) {
			TrainingPoint pt = it.next();
			double x = pt.location().x() * w;
			double y = pt.location().y() * w;
			g.setColor(Color.red);
			g.fillOval((int)x-circleSize, (int)y-circleSize, circleSize*2, circleSize*2);
		}
				
		for (TrainingPoint pt : plots) {
			Location loc = pt.location();
			double x = loc.x() * w;
			double y = loc.y() * w;	
			float alpha = (float)(1 - pt.orientation() / plots.size());
			if (loc.z() >= 1.5) {
				g.setColor(new Color(1.0f, 0.0f, 0.0f, alpha));				
			}
			else {
				g.setColor(new Color(0.0f, 0.0f, 1.0f, alpha));
			}
			g.fillOval((int)x-circleSize, (int)y-circleSize, circleSize*2, circleSize*2);
		}
	}
}
