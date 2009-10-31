package smarthome.rfid.data.collector;
import java.util.*;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.*;

public class FileIO {

    public static Iterator <String> read(String file) {
		
    	ArrayList <String> temp = new ArrayList <String> (); 
    	FileInputStream fstream = null;
		
    	try {
			fstream = new FileInputStream(file);
		} catch (FileNotFoundException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
			System.out.println ("FileIO: File not found: "+e2);
		}
		
		DataInputStream in = new DataInputStream(fstream);
        BufferedReader br = new BufferedReader(new InputStreamReader(in));
        String strLine; 
        
        try {
			while ((strLine = br.readLine()) != null){
				temp.add(strLine);
			}
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
			System.out.println ("FileIO: Error in reading file "+e1);
		}
        
        try {
			in.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.out.println ("FileIO: Error in closing file "+e);
		}
								    
    	return temp.iterator();
	}
    
    public static void write (String file, String [] data) {
    	try{
    	    // Create file 
    	    FileWriter fstream = new FileWriter(file);
    	        BufferedWriter out = new BufferedWriter(fstream);
    	    
    	    for (String line : data){
    	    	out.append(line+"\n");
    	    }
    	   
    	    out.close();
    	    }catch (Exception e){//Catch exception if any
    	      System.err.println("FileIO: Error in writing to file: " + e.getMessage());
    	}
    }
}