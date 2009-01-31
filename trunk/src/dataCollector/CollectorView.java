package dataCollector;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.*;
import javax.imageio.*;
import javax.swing.*;


public class CollectorView extends JFrame{
	private CollectorModel myModel; 
	private File BACKGROUND_FILE;
	private CollectorDisplay myDisplay;
	
	
	public CollectorView (CollectorModel model) {
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setTitle("RFID Signal Strength Manual Collector");
		myModel = model; 
		
		//JFileChooser myFileChooser = new JFileChooser();  
		//int retval = myFileChooser.showOpenDialog(null);
		//if (retval == JFileChooser.APPROVE_OPTION) {
//			BACKGROUND_FILE = myFileChooser.getSelectedFile();
		//}
		
		if (Settings.FLOOR == 1)
		BACKGROUND_FILE = new File("Level 1.jpg");
		else
			BACKGROUND_FILE = new File("Level 2.jpg");
		
		myDisplay = new CollectorDisplay(myModel, BACKGROUND_FILE);
		add(myDisplay, BorderLayout.CENTER);
			
		makeMenus();
		
		pack(); 
		//this.setBounds(0, 0, 495*2, 340*2+40);		
		setVisible(true);
	}
	
	
	
	private void makeMenus() {
		JMenuBar bar = new JMenuBar();
		JMenu fileMenu = new JMenu("File");

		fileMenu.add(new AbstractAction("New") {
			public void actionPerformed(ActionEvent ev) {
				CollectorView newView = new CollectorView(myModel);
				exit();
			}
		});
		
		fileMenu.add(new AbstractAction("Save") {
			public void actionPerformed(ActionEvent ev) {
				JFileChooser ourChooser = new JFileChooser();  
				int retval = ourChooser.showSaveDialog(null);
				if (retval == JFileChooser.APPROVE_OPTION) {
					File file = ourChooser.getSelectedFile();
					myModel.outputToFile(file.toString());
				}
			}
		});

		fileMenu.add(new AbstractAction("Load") {
			public void actionPerformed(ActionEvent ev) {
				JFileChooser ourChooser = new JFileChooser();  
				int retval = ourChooser.showOpenDialog(null);
				if (retval == JFileChooser.APPROVE_OPTION) {
					File file = ourChooser.getSelectedFile();
					myModel.loadFile(file.toString());
					myDisplay.reset();
				}
			}
		});

		fileMenu.add(new AbstractAction("Quit") {
			public void actionPerformed(ActionEvent ev) {
				System.exit(0);
			}
		});

		bar.add(fileMenu);
		setJMenuBar(bar);
	}
	
	private void exit() {
		dispose(); 
	}
}
