package waterRowerClient;

import com.sun.javafx.binding.StringFormatter;

/**
 * 
 * @author cp
 *
 *
 * Byte	Description
 * 00-02	? (fixed to 0xA 0x8 0x0)
 * 03-04	Total Minutes
 * 05-06	Total Seconds
 * 07-11	Total Distance
 * 12	?
 * 13-14	Minutes to 500m
 * 15-16	Seconds to 500m
 * 17-19	SPM
 * 20-22	Watt
 * 23-26	Cal/h
 * 27-28	Level
 * 29-31    Heartrate
 * 32	\n
 *
 */
public class DataRecord {

	public static enum DataRecordValueType { TOTAL_SECS, TOTAL_DIST, CURR500M_SECS, SPM, WATT, CAL_HR, LEVEL, HEARTRATE}
	
	public int getValue( DataRecordValueType vt) {
		switch (vt) {
		case TOTAL_DIST: 
			return getTotalDistance();
		case TOTAL_SECS:
			return getTotalSeconds();
		case CURR500M_SECS:
			return getCurr500mSeconds();
		case SPM:
			return getSpm();
		case WATT:
			return getWatt();
		case CAL_HR:
			return getCalPerHr();
		case LEVEL:
			return getLevel();
		case HEARTRATE:
			return getHeartrate();
		}
		return Integer.MAX_VALUE;
	}
	
	public String getStringValue( DataRecordValueType vt) {
		switch (vt) {
		case TOTAL_DIST: 
			return String.valueOf(getTotalDistance());
		case TOTAL_SECS:
			//return new String(String.valueOf(getTotalSeconds()/60)+":"+String.valueOf(getTotalSeconds()%60));
			return String.format("%02d:%02d", getTotalSeconds()/60, getTotalSeconds()%60);
		case CURR500M_SECS:
			//return new String(String.valueOf(getCurr500mSeconds()/60)+":"+String.valueOf(getCurr500mSeconds()%60));
			return String.format("%02d:%02d", getCurr500mSeconds()/60, getCurr500mSeconds()%60);
		case SPM:
			return String.valueOf(getSpm());
		case WATT:
			return String.valueOf(getWatt());
		case CAL_HR:
			return String.valueOf(getCalPerHr());
		case LEVEL:
			return String.valueOf(getLevel());
		case HEARTRATE:
			return String.valueOf(getHeartrate());
		}
		return "UNKNOWN";
	}
	
	public int getTotalSeconds() {
		return (Integer.parseInt( rawData.substring(3,5))*60
				+ Integer.parseInt( rawData.substring(5,7)));
	}

	public int getTotalDistance() {
		return (Integer.parseInt( 
				rawData.substring(7,12)));
	}


	public int getCurr500mSeconds() {
		return (Integer.parseInt( rawData.substring(13,15))*60
				+ Integer.parseInt( rawData.substring(15,17)));
	}

	public int getSpm() {
		return (Integer.parseInt( 
				rawData.substring(17,20)));
	}


	public int getWatt() {
		return (Integer.parseInt( 
				rawData.substring(20,23)));
	}


	public int getCalPerHr() {
		return (Integer.parseInt( 
				rawData.substring(23,27)));
	}


	public int getLevel() {
		return (Integer.parseInt( 
				rawData.substring(27,29)));		
	}

	public int getHeartrate() {
		return (Integer.parseInt( 
				rawData.substring(29,32)));		
	}

	private String rawData=null;
	
	public static String dataId="A80"; 

	DataRecord( String rawData) throws DataRecordException {
		if( !isDataRecord(rawData)) {
			throw new DataRecordException("This is not a data record.");
		}
		this.rawData=rawData;
	}
	
	public String toString() {
		return StringFormatter.format( "%d %d\n", 
				getTotalSeconds(), 
				getTotalDistance())
				.getValue();
	}
	
	public static DataRecord getDataRecord( String s) throws DataRecordException {
		if( isDataRecord(s)) {
			return new DataRecord( s);
		} else {
			throw new DataRecordException("No data record.");
		}
	}

	public static boolean isDataRecord(String rawData) {
		String id=rawData.substring(0,3);
		if( rawData.length()!=32 || !dataId.equals(id)) {
			return false;
		} 
		return true;
	}
	
	public String getRawData() {
		return rawData;
	}
	
	public static enum DataRecordType { UNKNOWN, DATA, C_REPL, F_REPL, V_REPL, LEVEL, H_REPL, R_REPL, WARN, QUIT, T_REPL}
	
	public static DataRecordType getDataRecordType(String rawData) {
		DataRecordType et=DataRecordType.UNKNOWN;
		switch( rawData.charAt(0)) {
		case 'A':
			et=DataRecordType.DATA;
			break;
		case 'C':
			et=DataRecordType.C_REPL;
			break;
		case 'V':
			et=DataRecordType.V_REPL;
			break;
		case 'L':
			et=DataRecordType.LEVEL;
			break;
		case 'H':
			et=DataRecordType.H_REPL;
			break;
		case 'R':
			et=DataRecordType.R_REPL;
			break;
		case 'T':
			et=DataRecordType.T_REPL;
			break;
		case 'W':
			et=DataRecordType.WARN;
			break;
		case 'X':
			et=DataRecordType.QUIT;
			break;
		case 'F':
			et=DataRecordType.F_REPL;
			break;
		}
		return et;
	}
}
