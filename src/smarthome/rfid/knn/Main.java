/*
 * Main.java
 *
 * Created on December 19, 2005, 6:02 AM
 *
 * To change this template, choose Tools | Options and locate the template under
 * the Source Creation and Management node. Right-click the template and choose
 * Open. You can then make changes to the template in the Source Editor.
 */

package smarthome.rfid.knn;

import javax.swing.*;

/**
 *
 * @author John
 */
public class Main {
    
    /** Creates a new instance of Main */
    public Main() {
    }
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
        } catch (Exception e) { }
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new KnnGui().setVisible(true);
            }
        });
    }
    
}
