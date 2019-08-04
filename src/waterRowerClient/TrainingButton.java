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

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;

public class TrainingButton extends Button {

	protected WaterRowerClient wrc=null;
	ArrayList<String> btnLabels=new ArrayList<String>();
	int state=0;
	
	public TrainingButton(WaterRowerClient wrc) {
		this.wrc=wrc;
		btnLabels.add("START TRAINING");
		btnLabels.add("STOP  TRAINING");
		setText(btnLabels.get(state)); 
	    setOnAction(new EventHandler<ActionEvent>() {
	        @Override
	        public void handle(ActionEvent event) {
	        	switch( state) {
	        	case 0:
	        		try {
						wrc.startTraining();
					} catch (IOException e) {
						System.err.println("Exception caught start training: "+e.getLocalizedMessage());
						System.exit(-1);
					}
	        		state =1;
	        		break;
	        	case 1:
	        		try {
						wrc.stopTraining();
					} catch (IOException e) {
						System.err.println("Exception caught stop training: "+e.getLocalizedMessage());
						System.exit(-1);
					}
	        		state =0;
	        		break;
	        	}
	        	setText(btnLabels.get(state));
	        }
	    });	
	}
}
