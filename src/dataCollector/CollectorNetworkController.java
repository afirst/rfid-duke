package dataCollector;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;

/*
 * This class requests signalStrength from the server
 *
 */
public class CollectorNetworkController extends Thread{
	
	private static final String HOST = "127.0.0.1"; 
		
	private CollectorModel myModel;
	private ObjectInputStream in;
	private ObjectOutputStream out;
	Socket sock ;
	
	public CollectorNetworkController(CollectorModel model){
		myModel = model;
		try {
			sock = new Socket(HOST, 8793);
			out = new ObjectOutputStream(sock.getOutputStream());
			in = new ObjectInputStream(sock.getInputStream());
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	//TODO: Finish this class
	
	@Override
	public void run() {
		// TODO Auto-generated method stub
		super.run();
		while (true) {
			String s;
			try {
				s = (String)in.readObject();
				String[] ss = s.split(",");				
				myModel.updateSignalStrength(Integer.parseInt(ss[0]), Integer.parseInt(ss[1]));
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				break;
			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				break;
			}			
		}
	}
	
}
