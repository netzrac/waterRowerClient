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
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javafx.scene.paint.Color;
import javafx.scene.text.Text;

public class Trainer implements DataNotifier {

	private Text commandText;
	private boolean init=false;
	// TODO: Use for statistics
	@SuppressWarnings("unused")
	private int baseTotalSeconds;
	// TODO: Use for statistics
	@SuppressWarnings("unused")
	private int baseTotalDistance;

	List<TrainingStep> steps=new ArrayList<TrainingStep>();
	
	Iterator<TrainingStep> stepIter=null;
	TrainingStep step=null;
	private boolean initStep=true;
	
	boolean trainingFinished=false;
	private Text commandRemain;
	
	public Trainer(Text commandText, Text commandRemain) {
		this.commandText=commandText;
		this.commandRemain=commandRemain;
		// L1 1000m
		steps.add( new TrainingStep( 1, 1000, 25));
		// L2 500m
		steps.add( new TrainingStep( 2, 300, 45));
		steps.add( new TrainingStep( 2, 100, 50));
		steps.add( new TrainingStep( 2, 100, 55));
		// L2 500m
		steps.add( new TrainingStep( 2, 300, 45));
		steps.add( new TrainingStep( 2, 100, 50));
		steps.add( new TrainingStep( 2, 100, 55));
		// L3 500m
		steps.add( new TrainingStep( 3, 500, 70));
		// L2 500m
		steps.add( new TrainingStep( 2, 500, 50));
		// L3 500m
		steps.add( new TrainingStep( 3, 500, 70));
		// L2 500m
		steps.add( new TrainingStep( 2, 500, 50));
		// L1 500m
		steps.add( new TrainingStep( 1, 500, 25));
		// L1 500m
		steps.add( new TrainingStep( 1, 500, 25));
	}

	public void startTraining() {
		commandText.setText("Training will be started.");
		stepIter=steps.iterator();
		init=true;
	}

	public void stopTraining() {
		commandText.setText("Training stopped.");
	}
	
	public boolean isTrainingFinished() {
		return trainingFinished;
	}

	@Override
	public void readEvent(String rawData) throws IOException {
		if( trainingFinished || !DataRecord.isDataRecord(rawData)) {
			return;
		}
		DataRecord dr;
		try {
			dr = DataRecord.getDataRecord( rawData);
		} catch (DataRecordException e) {
			return;
		}
		if( init) {
			// Store base values
			baseTotalSeconds=dr.getTotalSeconds();
			baseTotalDistance=dr.getTotalDistance();
			init=false;
			initStep=true;
		}
		if( initStep) {
			if( stepIter.hasNext()) {
				step=stepIter.next();
			} else {
				commandText.setText( "Training finished.");
				trainingFinished=true;
				return;
			}
			commandText.setText( step.toString());
			step.startStep(dr.getTotalDistance());
			initStep=false;
		}

		if( step.stepFinished(dr.getTotalDistance())) {
			commandText.setText( "Step finished.");
			initStep=true;
		}

		commandRemain.setText( String.format("%03d",step.getRemaining(dr.getTotalDistance())));
		
		if( step.conditionFulfilled(dr.getWatt())) {
			commandText.setFill(Color.GREEN);
		} else {
			commandText.setFill(Color.DARKBLUE);
		}
	
	}

}
