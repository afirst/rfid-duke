package dataCollector;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.*; 

import javax.imageio.*;
import javax.swing.JPanel;

import math.DataTuple;

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
		
		this.setPreferredSize(new Dimension(1116, 322));
		this.setMinimumSize(new Dimension(1116, 322));
		this.setBackground(Color.yellow);
		this.addMouseListener(new MouseListener() {
			public void mouseClicked(MouseEvent arg0) {
				Point p = arg0.getPoint();
				for (int i =0; i < pointBuffer.size(); i++){
					double distance = p.distance(pointBuffer.get(i));
						if (distance <= circleSize) {
							//change p into previous point
							p = pointBuffer.get(i); 
						}		
					}
				CollectorDialogue dialogue = new CollectorDialogue(myModel, p, myDisplay);
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
		pointBuffer.clear();
		Iterator <DataTuple> iterator = myModel.iterator();
		
		while (iterator.hasNext()){
			Point point = iterator.next().getPoint(); 
			if(!pointBuffer.contains(point)){
				pointBuffer.add(point);
			}
		}
		repaint();
	}
	
	@Override
	public void paint(Graphics g) {
		super.paint(g);
		int w = this.getWidth();
		int h = this.getHeight();				
		
		g.drawImage(backgroundImage, 0, 0, w, h, null);		
		
		// draw points
		for (Point p : pointBuffer) {
			g.setColor(Color.red);
			g.fillOval(p.x-circleSize, p.y-circleSize, circleSize*2, circleSize*2);
		}
	}
}
