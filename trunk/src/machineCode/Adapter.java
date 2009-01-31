package machineCode;
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

public class Adapter implements Runnable, SerialPortEventListener {

	private Thread readThread;
	private SerialPort serialPort1;
	private InputStream inputStream;
	private String TimeStamp;
	private OutputStream outputStream;
	private AdapterListener myListener;
	
	public Adapter (CommPortIdentifier Identifier, AdapterListener listener) {
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
            serialPort1.setSerialPortParams(57600,
                SerialPort.DATABITS_8,
                SerialPort.STOPBITS_1,
                SerialPort.PARITY_NONE);
            serialPort1.setDTR(false);
            serialPort1.setRTS(false);
        } catch (UnsupportedCommOperationException e) {e.printStackTrace();}
        
        readThread = new Thread(this);
        readThread.start();
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
            		if (header != ReaderCommands.RESPONSE_HEADER) {
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
            			System.out.println("Invalid response checksum " + checksum + ", " + actualChecksum);
            		}
            		System.out.println("Received: " + command + ", data: " + s + (invalidHeader?", invalid header "+header:""));
            		if (command == 6 && !invalidHeader && checksum == actualChecksum && length >= 23) {            			
            			int tagID = d[16] * 256*256*256 + d[17] * 256*256 + d[18] * 256 + d[19];
            			int rssi = d[22];
            			if (myListener != null) {
            				myListener.readTag(receiverID, tagID, rssi);
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

	public boolean sendCommand(int command) {
		return sendCommand(command, new int[0]);
	}
	public boolean sendCommand(int command, int[] data) {
		return sendCommand(255, 255, 0, command, data);
	}
	public boolean sendCommand(int networkID, int receiverID, int nodeID, int command, int[] data) {
		int checksum = data.length ^ networkID ^ receiverID ^ nodeID ^ command;
		for (int i = 0; i < data.length; i++) {
			checksum ^= data[i];
		}
		try {
			outputStream.write(ReaderCommands.COMMAND_HEADER);
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
	
	public boolean resetNetwork() {
		return sendCommand(ReaderCommands.RESET_NETWORK);
	}
	public boolean enableAutoPolling() {
		return sendCommand(ReaderCommands.ENABLE_AUTO_POLLING);
	}
	public boolean disableAutoPolling() {
		return sendCommand(ReaderCommands.DISABLE_AUTO_POLLING);
	}
	public boolean pingReader() {
		return sendCommand(ReaderCommands.PING_READER);
	}
	public boolean setNetworkID() {
		return sendCommand(ReaderCommands.SET_NETWORK_ID);
	}
	public boolean setReaderID() {
		return sendCommand(ReaderCommands.SET_READER_ID);
	}
	public boolean setSiteCode() {
		return sendCommand(ReaderCommands.SET_SITE_CODE);
	}
	public boolean getSiteCode() {
		return sendCommand(ReaderCommands.GET_SITE_CODE);
	}	
	public boolean setReceiverGain(boolean highGain) {
		return sendCommand(ReaderCommands.SET_RECEIVER_GAIN, new int[] {highGain ? 1 : 0});
	}
	public boolean getReceiverGain() {
		return sendCommand(ReaderCommands.GET_RECEIVER_GAIN);
	}	
	public boolean setAlarmFilter() {
		return sendCommand(ReaderCommands.SET_ALARM_FILTER);
	}
	public boolean getAlarmFilter() {
		return sendCommand(ReaderCommands.GET_ALARM_FILTER);
	}	
	public boolean getNumberOfInvalidTags() {
		return sendCommand(ReaderCommands.GET_NUMBER_VALID_TAGS);
	}
	public boolean getSupplyVoltage() {
		return sendCommand(ReaderCommands.GET_SUPPLY_VOLTAGE);
	}	
	public boolean startRFWhiteNoiseCalculation() {
		return sendCommand(ReaderCommands.START_RF_WHITE_NOISE_CALCULATION);
	}
	public boolean getRFWhiteNoiseResult() {
		return sendCommand(ReaderCommands.GET_RF_WHITE_NOISE_RESULT);
	}	
	public boolean setBaudRate() {
		return sendCommand(ReaderCommands.SET_BAUD_RATE);
	}
	public boolean getVersionInformation() {
		return sendCommand(ReaderCommands.GET_VERSION_INFORMATION);
	}	
	
	public void scan ()
	{
//		if (!lastScan.containsKey(tag.getTagID())
//				|| System.currentTimeMillis() > lastScan.get(tag
//						.getTagID()) + 5000) {
//			updater.toggleTag(tag.getTagID().replace(" ", ""));
//			printMessage(tag.getTagID() + " toggled on Facebook");
//		}
//		
//		lastScan.put(tag.getTagID(), System.currentTimeMillis());
	}
}
