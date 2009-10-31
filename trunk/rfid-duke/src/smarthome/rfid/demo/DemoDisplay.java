package smarthome.rfid.demo;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.*;
import java.util.HashMap;
import java.util.Map;

import javax.imageio.*;
import javax.swing.JPanel;



/**
 * Demo GUI
 * @author Andrew First
 */
public class DemoDisplay extends JPanel {	
	private static final long serialVersionUID = 1L;
	private static final File BACKGROUND_FILE = new File("images/fuqua_classroom.PNG");
	protected static Image backgroundImage;
	private Map<Point, Integer> map = new HashMap<Point, Integer>();
	
	static
	{
		// Load images
		try {			
			backgroundImage = ImageIO.read(BACKGROUND_FILE);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}	
	
	public DemoDisplay()
	{
		this.setPreferredSize(new Dimension(512, 512));
		this.setMinimumSize(new Dimension(512, 512));
		this.setBackground(Color.yellow);
		this.addMouseListener(new MouseListener() {
			public void mouseClicked(MouseEvent arg0) {
			}
			public void mouseEntered(MouseEvent arg0) {
			}
			public void mouseExited(MouseEvent arg0) {
			}
			public void mousePressed(MouseEvent arg0) {
				Point p = arg0.getPoint();
				int r = DemoMain.getCurrentRSSI();
				map.put(p, r);
				repaint();
			}
			public void mouseReleased(MouseEvent arg0) {
			}			
		});
	}
	
	public void reset() {
		map.clear();
		repaint();
	}
	
	@Override
	public void paint(Graphics g) {
		super.paint(g);
		
		// draw background
	//	g.drawImage(backgroundImage, 0, 0, w, h, null);		
		
		// draw points
		int pointSize = 20;
		for (Point p : map.keySet()) {
			g.setColor(Color.red);
			g.fillOval(p.x-pointSize, p.y-pointSize, pointSize*2, pointSize*2);
		}
		
		int c = DemoMain.getCurrentRSSI();
		int minDist = Integer.MAX_VALUE;
		Point closest = null;
		for (Point p : map.keySet()) {
			Integer r = map.get(p);
			int dist = Math.abs(r - c);
			if (dist < minDist) {
				minDist = dist;
				closest = p;
			}
		}
		if (closest != null) {
			g.setColor(Color.blue);
			pointSize = 30;
			g.fillOval(closest.x-pointSize, closest.y-pointSize, pointSize*2, pointSize*2);
		}
	}	
}
