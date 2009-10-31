package smarthome.rfid;
import javax.comm.CommPortIdentifier;
import javax.comm.PortInUseException;
import javax.comm.SerialPort;
import javax.comm.UnsupportedCommOperationException;

import java.io.*;

public class LRX201Adapter {

	private static final int SEND_INTERVAL_MS = 10;
	
	private SerialPort serialPort1;
	private InputStream inputStream;
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
            serialPort1.setFlowControlMode(SerialPort.FLOWCONTROL_NONE);
            serialPort1.setDTR(true);
            serialPort1.setInputBufferSize(16384);
        } catch (UnsupportedCommOperationException e) {
			e.printStackTrace();
		}
        serialPort1.notifyOnDataAvailable(true);
        try {
            serialPort1.setSerialPortParams(115200,
                SerialPort.DATABITS_8,
                SerialPort.STOPBITS_1,
                SerialPort.PARITY_NONE);
        } catch (UnsupportedCommOperationException e) {e.printStackTrace();}
	}	

	public void setDebug(boolean debug) {
		this.debug = debug;
	}
	
	private void debugPrint(String text) {
		if (debug) {
			System.out.println(text);
		}
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
			
	private long readNum = 0;
	private int numReaders = 0;
	public void send() {
		for (int k = 0; k < 3; k++) {
		for (int i = numReaders; i >= 0; i--) {
			getTagPacket(0, 0, i + 1);
			try {
				Thread.sleep(SEND_INTERVAL_MS);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		}
	}
	public void start(final int numReaders) {
		running = true;
		this.numReaders = numReaders;
		new Thread(new Runnable() {
			@Override
			public void run() {
				send();
				debugPrint("Sending");
				int reads = 0;
				while (running) {
					boolean commandHeader = false;
					try {
						int header = inputStream.read();
						boolean invalidHeader = false;
						debugPrint("Got packet");
						if (header == LRX201ReaderCommands.COMMAND_HEADER) {
							invalidHeader = true;
							commandHeader = true;
						} else if (header != LRX201ReaderCommands.RESPONSE_HEADER) {
							invalidHeader = true;
							debugPrint("Invalid header");
						}
						int length = inputStream.read();
						int networkID = inputStream.read();
						int receiverID = inputStream.read();
						int nodeID = inputStream.read();
						int command = inputStream.read();
						int actualChecksum = length ^ networkID
								^ receiverID ^ nodeID ^ command;
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
							debugPrint("Invalid response checksum "
									+ checksum + ", " + actualChecksum);
						}
						debugPrint("Received: "
								+ command
								+ ", data: "
								+ s
								+ (commandHeader ? ", command header" : (invalidHeader ? ", invalid header "
										+ header : "")));
						if (command == 6 && !invalidHeader) {
							if (length >= 23) {// && checksum == actualChecksum) {
								int tagID = d[16] * 256 * 256 * 256 + d[17]
									* 256 * 256 + d[18] * 256 + d[19];
								int rssi = d[22];
								if (myListener != null) {
									myListener.readTag(nodeID, tagID, rssi);
									readNum++;
									reads++;
								}
							} else {
								readNum++;
								reads++;
							}
						}
					} catch (IOException e) {
						e.printStackTrace();
					} finally {
						if (!commandHeader) {
							if (reads == numReaders*3) {
								reads = 0;
								send();
								debugPrint("Sending");
							}
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
	public boolean enableAutoPolling() {
		return sendCommand(LRX201ReaderCommands.ENABLE_AUTO_POLLING);
	}
	public boolean enableAutoPolling(int networkId, int receiverId, int nodeId) {
		return sendCommand(networkId, receiverId, nodeId, LRX201ReaderCommands.ENABLE_AUTO_POLLING, new int[0]);
	}	
	public boolean disableAutoPolling(int networkId, int receiverId, int nodeId) {
		return sendCommand(networkId, receiverId, nodeId, LRX201ReaderCommands.DISABLE_AUTO_POLLING, new int[0]);
	}
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
