package smarthome.rfid;

import java.io.InputStream;
import java.net.URL;

import smarthome.rfid.data.Location;



public class DatabaseUpdater {
	private static final String WEB_SERVICE_URL = "http://localhost/request.php?pw=7002xav&";
	
	public DatabaseUpdater() {	
	}
	
	public boolean toggleTag(int tagId) {
		try {
			return callWebService("q=toggle_tag&tag_id="+tagId).equals("0");
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}
	
	public boolean setHome(int tagId, Location location) {
		try {
			String x = "" + location.x();
			String y = "" + location.y();
			String z = "" + location.z();			
			return callWebService("q=set_home&tag_id="+tagId+"&x="+x+"&y="+y+"&z="+z).equals("0");
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}		
	}
	
	public boolean setAway(int tagId) {
		try {
			return callWebService("q=set_away&tag_id="+tagId).equals("0");
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}		
	}

	private String callWebService(String query) throws Exception	{
		StringBuilder str = new StringBuilder();		
		URL url = new URL(WEB_SERVICE_URL + query);
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
