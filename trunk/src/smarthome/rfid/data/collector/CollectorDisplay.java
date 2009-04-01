package smarthome.rfid.data.collector;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.*; 

import javax.imageio.*;
import javax.swing.JPanel;

import smarthome.rfid.data.Location;
import smarthome.rfid.data.TrainingPoint;



public class CollectorDisplay extends JPanel {
	protected File BACKGROUND_FILE;
	protected CollectorModel myModel; 
	private ArrayList <Point> pointBuffer = new ArrayList <Point>();
	protected static Image backgroundImage;
	private final int circleSize = 10; 
	private CollectorDisplay myDisplay = this;  
	
	public CollectorDisplay (CollectorModel model, File file) {
		myModel = model; 
		BACKGROUND_FILE = file; 
		initialize(); 		
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
	}
}
