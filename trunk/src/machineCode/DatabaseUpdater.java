package machineCode;

import java.io.InputStream;
import java.net.URL;

public class DatabaseUpdater {
	private static final String TOGGLE_URL = "http://152.3.3.245/updater.php?pw=7002xav&action=toggle&id=";
	
	public DatabaseUpdater() {	
	}
	
	public boolean toggleTag(String tagId) {
		try {
			return read(TOGGLE_URL + tagId).equals("ok");
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	private String read(String address) throws Exception	{
		StringBuilder str = new StringBuilder();
		URL url = new URL(address);
		InputStream urlRobotStream = url.openStream();
		byte[] bytes = new byte[1000];
		int n = 0;
		while ((n = urlRobotStream.read(bytes)) != -1) {
			str.append(new String(bytes, 0, n));			
		}	    
		urlRobotStream.close();
	    return str.toString();
	}	
}
