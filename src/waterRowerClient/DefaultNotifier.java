package waterRowerClient;

import java.io.IOException;

public class DefaultNotifier implements DataNotifier {

	private StdHBox levelBox;
	private StdHBox distBox;
	private StdHBox timeBox;

	public DefaultNotifier(StdHBox levelBox, StdHBox distBox, StdHBox timeBox) {
		this.levelBox=levelBox;
		this.distBox=distBox;
		this.timeBox=timeBox;
	}

	@Override
	public void readEvent(String rawData) throws IOException {
		
		if( !DataRecord.isDataRecord(rawData)) {
			return;
		}
		
		DataRecord dr=null;
		try {
			dr=new DataRecord(rawData);
		} catch (DataRecordException e) {
			System.err.println("Exception caught: "+e.getLocalizedMessage());
			return;
		}
		
		levelBox.setCurrValue(dr.getLevel());
		distBox.setCurrValue(dr.getTotalDistance());
		timeBox.setCurrValue(String.format("%02d:%02d", dr.getTotalSeconds()/60, dr.getTotalSeconds()%60));
	}

}
