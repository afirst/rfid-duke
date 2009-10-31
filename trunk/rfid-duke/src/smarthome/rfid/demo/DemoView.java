package smarthome.rfid.demo;
import javax.comm.*;

import java.awt.*;
import java.awt.event.*;

import javax.swing.*;


import java.util.*;

/**
 * Choose a port, any port!
 * 
 * Java Communications is a "standard extension" and must be downloaded and
 * installed separately from the JDK before you can even compile this program.
 * 
 */

public class DemoView extends JDialog {

	private static final long serialVersionUID = 1L;
	protected HashMap<String, CommPortIdentifier> map = new HashMap<String, CommPortIdentifier>();
	protected String selectedPortName;
	protected CommPortIdentifier selectedPortIdentifier;
	protected JComboBox serialPortsChoice;
	protected JComboBox parallelPortsChoice;
	protected JComboBox other;
	protected SerialPort ttya;
	protected JLabel rssiLabel, distLabel;
	protected final int PAD = 5;
	private DemoDisplay disp;
	
	public DemoView(JFrame parent) {
		super(parent, "Test Viewer", true);
		makeGUI();
		pack();
		this.setBounds(0, 0, 495*2, 340*2+40);		

	}

	protected void makeGUI() {
		Container cp = getContentPane();
		JPanel centerPanel = new JPanel();
		cp.add(BorderLayout.CENTER, centerPanel);		
		centerPanel.setLayout(new BorderLayout());
		Container c2 = new JPanel();
		c2.setLayout(new BorderLayout());
		Font f = new Font("Trebuchet MS", Font.BOLD, 36);
		rssiLabel = new JLabel("", JLabel.LEFT);
		rssiLabel.setFont(f);
		distLabel = new JLabel("", JLabel.LEFT);
		distLabel.setFont(f);
		c2.add(rssiLabel, BorderLayout.WEST);
		Button b = new Button();
		b.setLabel("Reset");
		b.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				disp.reset();
			}
			
		});
		c2.add(b, BorderLayout.EAST);
		centerPanel.add(c2, BorderLayout.NORTH);
		//centerPanel.add(distLabel, 1, 0);
		disp = new DemoDisplay();
		centerPanel.add(disp, BorderLayout.CENTER);
	}
	
	public void update() {
		rssiLabel.setText("Signal strength: " + DemoMain.getCurrentRSSI());
		//distLabel.setText("Distance (ft): " + d);
		disp.repaint();
	}
	
	@Override
	public void hide() {
		//System.exit(0);
	}

}
