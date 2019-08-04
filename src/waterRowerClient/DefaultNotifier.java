/**
 *
 * Copyright 2019 Carsten Pratsch
 *
 * This file is part of waterRowerClient.
 *
 * waterRowerClient is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * waterRowerClient is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with waterRowerClient.  If not, see <http://www.gnu.org/licenses/>.
 *
 */
package waterRowerClient;

import java.io.IOException;

import javafx.scene.text.Text;

public class DefaultNotifier implements DataNotifier, HeartrateNotifier {

	private StdHBox levelBox;
	private StdHBox distBox;
	private StdHBox timeBox;
	private Text heartRateText;

	public DefaultNotifier(StdHBox levelBox, StdHBox distBox, StdHBox timeBox, Text hearBeatText) {
		this.levelBox=levelBox;
		this.distBox=distBox;
		this.timeBox=timeBox;
		this.heartRateText=hearBeatText;
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

	@Override
	public void heartrateEvent(String heartrate) {
		heartRateText.setText(String.format("%03d", Integer.parseInt(heartrate)));
	}

}
