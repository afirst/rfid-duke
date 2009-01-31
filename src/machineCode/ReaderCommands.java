package machineCode;
	/**
	 * The list of reader commands and other constants.
	 * 
	 * @author Xiaodi Huang
	 * 
	 */
	
	public class ReaderCommands {		
		public static final int COMMAND_HEADER = 0xAA; 	
		public static final int RESPONSE_HEADER = 0x55; 
		
		public static int RESET_NETWORK = 0x00;
		public static int ENABLE_AUTO_POLLING = 0x01;	
		public static int DISABLE_AUTO_POLLING = 0x02;	
		public static int PING_READER = 0x03;	
		public static int SET_NETWORK_ID = 0x04;	
		public static int SET_READER_ID = 0x05;	
		public static int GET_TAG_PACKET = 0x06;	
		public static int GET_RSSI_VALUE = 0x07;	
		public static int SET_RSSI_VALUE = 0x08;	
		public static int SET_SITE_CODE = 0x09;	
		public static int GET_SITE_CODE = 0x0A;	
		public static int SET_RECEIVER_GAIN = 0x0B;	
		public static int GET_RECEIVER_GAIN = 0x0C;	
		public static int SET_ALARM_FILTER = 0x0D;	
		public static int GET_ALARM_FILTER = 0x0E;	
		public static int GET_NUMBER_VALID_TAGS = 0x0F;	
		public static int GET_SUPPLY_VOLTAGE = 0x10;	
		public static int START_RF_WHITE_NOISE_CALCULATION = 0x11;	
		public static int GET_RF_WHITE_NOISE_RESULT = 0x12;	
		public static int SET_BAUD_RATE = 0xFE;	
		public static int GET_VERSION_INFORMATION = 0xFF;	
	}
