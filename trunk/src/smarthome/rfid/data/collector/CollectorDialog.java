package smarthome.rfid.data.collector;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
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
import smarthome.rfid.data.Vector;



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
	
	public CollectorDialog(CollectorModel model, Point point, CollectorDisplay display){
        myModel = model; 
		myPoint = point; 		
		myDisplay = display; 
		
		dataModel = new DefaultListModel(); 
		dataModelList = new JList(dataModel);
		
		setTitle("Add or delete data");
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        
        refresh(); 
        
        initialize();
          
        pack();
        setVisible(true);
    }
	
	private void refresh() {
		    
			dataModel.clear();
			Iterator<TrainingPoint> iterator = myModel.iterator();
	                
	        while (iterator.hasNext()){
	        	TrainingPoint temp = iterator.next();
	        	if ((int)temp.location().x()==(myPoint.x) && (int)temp.location().y() == (myPoint.y)) {
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
				String s = "<html><body>";
				for (int tag : Settings.TAG_NUMBER) {
					Vector rssi = myModel.getRSSI(tag);
					s += "Tag: " + tag + ": " + rssi;
					s += "<br>";
				}
				s += "</body></html>";
				rssiInfo.setText(s);
			}
			
		}).start();
    }
	
	private void addPoint() {
		for (int orientation = 0; orientation < Settings.TAG_NUMBER.length; orientation++) {
			int x = myPoint.x;
			int y = myPoint.y;
			int floor = Settings.FLOOR;
			int tagNumber = Settings.TAG_NUMBER[orientation];
			myModel.logPoint(x, y, floor, orientation, tagNumber);			
		}
	}
}
