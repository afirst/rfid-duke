package smarthome.rfid.service;

public interface LocationServiceListener {
	public static final int GENERAL = 0;
	public static final int ERROR = 1;
	public static final int TAG = 2;
	public void serviceEvent(String detail, int eventType);
}
