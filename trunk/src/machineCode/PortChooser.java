package machineCode;
import java.io.*;
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

public class PortChooser extends JDialog implements ItemListener {

	protected HashMap<String, CommPortIdentifier> map = new HashMap<String, CommPortIdentifier>();

	protected String selectedPortName;

	protected CommPortIdentifier selectedPortIdentifier;

	protected JComboBox serialPortsChoice;

	protected JComboBox parallelPortsChoice;

	protected JComboBox other;

	protected SerialPort ttya;

	protected JLabel choice;

	protected final int PAD = 5;

	public void itemStateChanged(ItemEvent e) {
		// Get the name
		selectedPortName = (String) ((JComboBox) e.getSource())
				.getSelectedItem();
		// Get the given CommPortIdentifier
		selectedPortIdentifier = map.get(selectedPortName);
		// Display the name.
		choice.setText(selectedPortName);
	}

	public String getSelectedName() {
		return selectedPortName;
	}
	
	public CommPortIdentifier getSelectedIdentifier() {
		return selectedPortIdentifier;
	}

	public static void main(String[] ap) {
		PortChooser c = new PortChooser(null);
		c.setVisible(true);// blocking wait
		System.out.println("You chose " + c.getSelectedName() + " (known by "
				+ c.getSelectedIdentifier() + ").");
		System.exit(0);
	}

	public PortChooser(JFrame parent) {
		super(parent, "Port Chooser", true);
		makeGUI();
		populate();
		finishGUI();
	}

	protected void makeGUI() {
		Container cp = getContentPane();
		JPanel centerPanel = new JPanel();
		cp.add(BorderLayout.CENTER, centerPanel);
		centerPanel.setLayout(new GridLayout(0, 2, PAD, PAD));
		centerPanel.add(new JLabel("Serial Ports", JLabel.RIGHT));
		serialPortsChoice = new JComboBox();
		centerPanel.add(serialPortsChoice);
		serialPortsChoice.setEnabled(false);

		centerPanel.add(new JLabel("Your choice:", JLabel.RIGHT));
		centerPanel.add(choice = new JLabel());
		JButton okButton;
		cp.add(BorderLayout.SOUTH, okButton = new JButton("OK"));
		okButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				PortChooser.this.dispose();
			}
		});
	}

	protected void populate() {
		// get list of ports available on this particular computer,
		// by calling static method in CommPortIdentifier.
			//CommPortIdentifier.addPortName("COM7", CommPortIdentifier.PORT_SERIAL, null);

		String drivername = "com.sun.comm.Win32Driver";
		try {
			CommDriver driver = (CommDriver) Class.forName(drivername).newInstance();
			driver.initialize();
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	
		
		Enumeration pList = CommPortIdentifier.getPortIdentifiers();		
		// Process the list, putting serial and parallel into ComboBoxes
		while (pList.hasMoreElements()) {
			CommPortIdentifier cpi = (CommPortIdentifier) pList.nextElement();
			// System.out.println("Port " + cpi.getName());
			map.put(cpi.getName(), cpi);
			if (cpi.getPortType() == CommPortIdentifier.PORT_SERIAL) {
				serialPortsChoice.setEnabled(true);
				serialPortsChoice.addItem(cpi.getName());
			}
		}
		serialPortsChoice.setSelectedIndex(-1);
	}
	
	protected void finishGUI() {
		serialPortsChoice.addItemListener(this);
		pack();
	}

}
