package machineCode;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

import javax.comm.CommPortIdentifier;


public class DataCollectionServer {
	public static void main(String[] args) throws Exception {
		ServerSocket ss = new ServerSocket(8793);
		Socket s = ss.accept();
		final ObjectInputStream in = new ObjectInputStream(s.getInputStream());
		final ObjectOutputStream out = new ObjectOutputStream(s.getOutputStream());
		
		PortChooser myChooser = new PortChooser(null); 
		myChooser.setVisible(true);// blocking wait
		CommPortIdentifier myIdentifier = myChooser.getSelectedIdentifier();
		Adapter adapter = new Adapter(myIdentifier, new AdapterListener() {
			public void readTag(int antennaID, int tagID, int rssi) {
				if (tagID == 1143) {
					try {
						out.writeObject(tagID + "," + rssi);
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		});
		adapter.enableAutoPolling();
	}
}
