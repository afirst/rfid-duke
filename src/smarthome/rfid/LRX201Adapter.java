package smarthome.rfid;
import javax.comm.CommPortIdentifier;
import javax.comm.PortInUseException;
import javax.comm.SerialPort;
import javax.comm.SerialPortEvent;
import javax.comm.SerialPortEventListener;
import javax.comm.UnsupportedCommOperationException;

import java.util.*;

import java.io.*;
import java.net.*;
import java.util.*;

public class LRX201Adapter implements Runnable, SerialPortEventListener {

	private Thread readThread;
	private SerialPort serialPort1;
	private InputStream inputStream;
	private String TimeStamp;
	private OutputStream outputStream;
	private LRX201AdapterListener myListener;
	private boolean debug = false;
	private boolean running = false;
	
	public LRX201Adapter (CommPortIdentifier Identifier, LRX201AdapterListener listener) {
		myListener = listener;
		
        try {
        	serialPort1 = (SerialPort)Identifier.open("Adapter", 2000);
        } catch (PortInUseException e) {e.printStackTrace();}
        try {
            inputStream = serialPort1.getInputStream();
            outputStream = serialPort1.getOutputStream();
        } catch (IOException e) {e.printStackTrace();}
        try {
            serialPort1.addEventListener(this);
        } catch (TooManyListenersException e) {e.printStackTrace();}
        serialPort1.notifyOnDataAvailable(true);
        try {
            serialPort1.setSerialPortParams(115200,
                SerialPort.DATABITS_8,
                SerialPort.STOPBITS_1,
                SerialPort.PARITY_NONE);
            serialPort1.setDTR(false);
            serialPort1.setRTS(false);
        } catch (UnsupportedCommOperationException e) {e.printStackTrace();}
        
        readThread = new Thread(this);
        readThread.start();
	}	
	
	public void setDebug(boolean debug) {
		this.debug = debug;
	}
	
	private void debugPrint(String text) {
		if (debug) {
			System.out.println(text);
		}
	}
	
	public void run() {
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {}
    }
	
	boolean inEvent = false; 
    public void serialEvent(SerialPortEvent event) {
    	if (inEvent) return;
    	inEvent = true;    	
        switch(event.getEventType()) {
        case SerialPortEvent.BI:
        case SerialPortEvent.OE:
        case SerialPortEvent.FE:
        case SerialPortEvent.PE:
        case SerialPortEvent.CD:
        case SerialPortEvent.CTS:
        case SerialPortEvent.DSR:
        case SerialPortEvent.RI:
        case SerialPortEvent.OUTPUT_BUFFER_EMPTY:
            break;
        case SerialPortEvent.DATA_AVAILABLE:
            try {
//            	while (inputStream.available() > 0) {
//            		System.out.println(inputStream.read());
//            	}
            	while (inputStream.available() > 0) {
            		int header = inputStream.read();        	   
            		boolean invalidHeader = false;
            		if (header != LRX201ReaderCommands.RESPONSE_HEADER) {
            			invalidHeader = true;
            		}
            		int length = inputStream.read();
            		int networkID = inputStream.read();
            		int receiverID = inputStream.read();
            		int nodeID = inputStream.read();
            		int command = inputStream.read();
            		int actualChecksum = length ^ networkID ^ receiverID ^ nodeID ^ command;
            		String s = "";
            		int[] d = new int[length];
            		for (int i = 0; i < length; i++) {
            			int data = inputStream.read();
            			d[i] = data;
            			actualChecksum ^= data;
            			s += data + ", ";
            		}
            		int checksum = inputStream.read();
            		if (checksum != actualChecksum) {            			
            			debugPrint("Invalid response checksum " + checksum + ", " + actualChecksum);
            		}
            		debugPrint("Received: " + command + ", data: " + s + (invalidHeader?", invalid header "+header:""));
            		if (command == 6 && !invalidHeader && checksum == actualChecksum && length >= 23) {            			
            			int tagID = d[16] * 256*256*256 + d[17] * 256*256 + d[18] * 256 + d[19];
            			int rssi = d[22];
            			if (myListener != null) {
            				myListener.readTag(nodeID, tagID, rssi);
            			}
            		}
            	}
            }
            catch (IOException e) {
            	e.printStackTrace();
            }
            break;
        }
        inEvent = false;
    }	

    private int networkId = 255;
    private int receiverId = 255;
    private int nodeId = 0;
    
	public boolean sendCommand(int command) {
		return sendCommand(command, new int[0]);
	}
	public boolean sendCommand(int command, int[] data) {
		return sendCommand(networkId, receiverId, nodeId, command, data);
	}
	public boolean sendCommand(int networkID, int receiverID, int nodeID, int command, int[] data) {
		int checksum = data.length ^ networkID ^ receiverID ^ nodeID ^ command;
		for (int i = 0; i < data.length; i++) {
			checksum ^= data[i];
		}
		try {
			outputStream.write(LRX201ReaderCommands.COMMAND_HEADER);
			outputStream.write(data.length);
			outputStream.write(networkID);
			outputStream.write(receiverID);
			outputStream.write(nodeID);
			outputStream.write(command);
			for (int i = 0; i < data.length; i++) {
				outputStream.write(data[i]);			
			}
			outputStream.write(checksum);
			outputStream.flush();
		}
		catch (IOException ex) {
			ex.printStackTrace();
			return false;
		}
		return true;
	}
	
	/*public boolean breakAutoPolling() {
		try {
			byte[] b = new byte[800];
			for (int i = 0; i < 800; i++) {
				if (i % 2 == 0) {
					b[i] = (byte)255;					
				}
				else {
					b[i] = (byte)'*';
				}
			}
			outputStream.write(b);
			outputStream.flush();
		}
		catch (IOException ex) {
			ex.printStackTrace();
			return false;
		}
		return true;
	}	*/
			
	public void start(final int numReaders) {
		running = true;
		new Thread(new Runnable() {
			public void run() {
				while (running) {
					for (int j = 1; j <= numReaders; j++) {
						getTagPacket(0, j, j);
						try {
							Thread.sleep(10);
						} catch (InterruptedException e) {
						}						
					}
				}
			}			
		}).start();		
	}
	
	public void stop() {
		running = false;
	}
	
	public boolean resetNetwork() {
		return sendCommand(LRX201ReaderCommands.RESET_NETWORK);
	}
	public boolean getTagPacket(int networkId, int receiverId, int nodeId) {
		return sendCommand(networkId, receiverId, nodeId, LRX201ReaderCommands.GET_TAG_PACKET, new int[0]);
	}
	public boolean resetNetwork(int networkId, int receiverId, int nodeId) {
		return sendCommand(networkId, receiverId, nodeId, LRX201ReaderCommands.RESET_NETWORK, new int[0]);
	}	
	//public boolean enableAutoPolling() {
//		return sendCommand(LRX201ReaderCommands.ENABLE_AUTO_POLLING);
//	}
//	public boolean enableAutoPolling(int networkId, int receiverId, int nodeId) {
		//return sendCommand(networkId, receiverId, nodeId, LRX201ReaderCommands.ENABLE_AUTO_POLLING, new int[0]);
	//}	
	//public boolean disableAutoPolling(int networkId, int receiverId, int nodeId) {
//		return sendCommand(networkId, receiverId, nodeId, LRX201ReaderCommands.DISABLE_AUTO_POLLING, new int[0]);
//	}
	public boolean pingReader() {
		return sendCommand(LRX201ReaderCommands.PING_READER);
	}
	public boolean setNetworkID() {
		return sendCommand(LRX201ReaderCommands.SET_NETWORK_ID);
	}
	public boolean setReaderID() {
		return sendCommand(LRX201ReaderCommands.SET_READER_ID);
	}
	public boolean setSiteCode() {
		return sendCommand(LRX201ReaderCommands.SET_SITE_CODE);
	}
	public boolean getSiteCode() {
		return sendCommand(LRX201ReaderCommands.GET_SITE_CODE);
	}	
	public boolean setReceiverGain(boolean highGain) {
		return sendCommand(LRX201ReaderCommands.SET_RECEIVER_GAIN, new int[] {highGain ? 1 : 0});
	}
	public boolean setReceiverGain(int networkId, int receiverId, int nodeId, boolean highGain) {
		return sendCommand(networkId, receiverId, nodeId, LRX201ReaderCommands.SET_RECEIVER_GAIN, new int[] {highGain ? 1 : 0});
	}	
	public boolean getReceiverGain() {
		return sendCommand(LRX201ReaderCommands.GET_RECEIVER_GAIN);
	}	
	public boolean setAlarmFilter() {
		return sendCommand(LRX201ReaderCommands.SET_ALARM_FILTER);
	}
	public boolean getAlarmFilter() {
		return sendCommand(LRX201ReaderCommands.GET_ALARM_FILTER);
	}	
	public boolean getNumberOfInvalidTags() {
		return sendCommand(LRX201ReaderCommands.GET_NUMBER_VALID_TAGS);
	}
	public boolean getSupplyVoltage() {
		return sendCommand(LRX201ReaderCommands.GET_SUPPLY_VOLTAGE);
	}	
	public boolean startRFWhiteNoiseCalculation() {
		return sendCommand(LRX201ReaderCommands.START_RF_WHITE_NOISE_CALCULATION);
	}
	public boolean getRFWhiteNoiseResult() {
		return sendCommand(LRX201ReaderCommands.GET_RF_WHITE_NOISE_RESULT);
	}	
	public boolean setBaudRate() {
		return sendCommand(LRX201ReaderCommands.SET_BAUD_RATE);
	}
	public boolean getVersionInformation() {
		return sendCommand(LRX201ReaderCommands.GET_VERSION_INFORMATION);
	}	
}
