package smarthome.rfid.data.collector;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.TreeSet;

import javax.swing.*; 
import javax.swing.event.ListDataEvent;
import javax.swing.event.ListDataListener;

import smarthome.rfid.data.RSSIReading;
import smarthome.rfid.data.TrainingPoint;
import smarthome.rfid.data.U;
import smarthome.rfid.data.Vector;
import smarthome.rfid.data.Location;


/**
 * This is the gui of the Messenger. 
 * 
 * @author Xiaodi Huang
 *	
 */

public class CollectorDialog extends JFrame {
	private static String lastFloor="", lastOrientationNumber="", lastReader="", lastTags="";
	
	private CollectorModel myModel;
	private Point myPoint; 
	private CollectorDisplay myDisplay; 	
	
	private JList dataModelList;
	private DefaultListModel dataModel;
			
	private JScrollPane dataSetPane;
	
	private JButton goButton;
	private JButton deleteButton;
	private JLabel rssiInfo;
	
	private double w;
	
	public CollectorDialog(CollectorModel model, Point point, CollectorDisplay display, int w){
        myModel = model; 
		myPoint = point; 		
		myDisplay = display;
		this.w = w;
		
		dataModel = new DefaultListModel(); 
		dataModelList = new JList(dataModel);
		
		setTitle("Add or delete data");
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        
        refresh(); 
        
        initialize();
          
        pack();
        setVisible(true);
    }
	
	private double x() {
	
		return 1.0 * myPoint.x / w;
	}
	private double y() {
	
		return 1.0 * myPoint.y / w;
	}
	
	private void refresh() {
		    
			dataModel.clear();
			Iterator<TrainingPoint> iterator = myModel.iterator();
	                
	        while (iterator.hasNext()){
	        	TrainingPoint temp = iterator.next();
	        	if (Math.abs(temp.location().x()-x()) < 0.01 && Math.abs(temp.location().y() -y()) < 0.01) {
	        		dataModel.addElement(temp.toString());
	        	}
	        }
	        dataModelList.revalidate();  
	        myDisplay.reset();
	}
	
	private int count = 0;
	private Timer t;
	private void initialize(){
		
        JPanel content = (JPanel) getContentPane();  
        
        rssiInfo = new JLabel();
        rssiInfo.setPreferredSize(new Dimension(450, 150));
        dataSetPane = new JScrollPane(dataModelList);
		dataSetPane.setBorder(BorderFactory.createTitledBorder("Data at this point"));
		dataSetPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        dataSetPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		dataSetPane.setPreferredSize(new Dimension(400, 200));
      			
		deleteButton = new JButton("Delete seleted Tuple");
		deleteButton.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent arg0) {
				// TODO Auto-generated method stub
				int index = dataModelList.getSelectedIndex();
				myModel.removeAt(index);				
				refresh(); 
			}
		});
		
		deleteButton.setPreferredSize(new Dimension(400, 25));
		deleteButton.setAlignmentX(Component.CENTER_ALIGNMENT);
		
		goButton = new JButton("GO");
		goButton.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent arg0) {
				CollectorDialog.this.setEnabled(false);
				count = 0;
				t = new Timer(Settings.POLL_INTERVAL, new ActionListener() {
					public void actionPerformed(ActionEvent arg0) {
						addPoint();
						refresh();
						count++;
						System.out.println(count);
						if (count == Settings.NUM_READINGS) {
							System.out.println(count);
							System.out.println(count);
							CollectorDialog.this.setEnabled(true);
							CollectorDialog.this.setVisible(false);
							t.stop();
						}						
					}					
				});
				t.start();				
			}
		});
		
		goButton.setPreferredSize(new Dimension(400, 25));
		goButton.setAlignmentX(Component.CENTER_ALIGNMENT);
		
		setLayout(new BoxLayout(getContentPane(), BoxLayout.Y_AXIS));
		
		content.add(rssiInfo);
		content.add(goButton);		
		content.add(dataSetPane);
		content.add(deleteButton);		
		
		new Timer(100, new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				Location loc = new Location(x(),y(),Settings.FLOOR);
				String s = "<html><body>";
				for (int tag : Settings.TAG_NUMBER) {
					Vector rssi = myModel.getRSSI(tag);
					s += "Tag: " + tag + ": " + rssi;
					s += "<br>";
				}
				s += "(Point Location: " + x() + "," + y() + ")<br>";
				try {
					s += "Room it belongs: " + loc.getRoom();
				} catch (FileNotFoundException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				s += "</body></html>";
				rssiInfo.setText(s);
			}
			
		}).start();
    }
	
	private void addPoint() {
		for (int orientation = 0; orientation < Settings.TAG_NUMBER.length; orientation++) {
			double x = x();
			double y = y();
			int floor = Settings.FLOOR;
			int tagNumber = Settings.TAG_NUMBER[orientation];
			myModel.logPoint(x, y, floor, orientation, tagNumber);			
		}
	}
}
