package waterRowerClient;

import java.io.IOException;
import java.util.ArrayList;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;

public class StartStopButton extends Button {
	
	protected WaterRowerClient wrc=null;
	ArrayList<String> btnLabels=new ArrayList<String>();
	int state=0;
	
	public StartStopButton( WaterRowerClient wrc) {
		this.wrc=wrc;
		btnLabels.add("START RECORDING");
		btnLabels.add("STOP  RECORDING");
		setText(btnLabels.get(state)); 
	    setOnAction(new EventHandler<ActionEvent>() {
	    	
	        @Override
	        public void handle(ActionEvent event) {
	        	switch( state) {
	        	case 0:
	        		try {
						wrc.startRecording();
					} catch (IOException e) {
						System.err.println("Exception caught start recording: "+e.getLocalizedMessage());
						System.exit(-1);
					}
	        		state =1;
	        		break;
	        	case 1:
	        		try {
						wrc.stopRecording();
					} catch (IOException e) {
						System.err.println("Exception caught stop recording: "+e.getLocalizedMessage());
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
