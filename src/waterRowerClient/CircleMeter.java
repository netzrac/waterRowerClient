package waterRowerClient;

import java.io.IOException;

import javafx.animation.FillTransition;
import javafx.animation.ParallelTransition;
import javafx.animation.RotateTransition;
import javafx.animation.ScaleTransition;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.util.Duration;
import waterRowerClient.DataRecord.DataRecordValueType;

public class CircleMeter implements Runnable, DataNotifier {

	private Circle c;
	int duration=ClientConfig.getIntOptionValue("animationDuration", 1000);
	private DataRecordValueType radius_valueType;
	private DataRecordValueType color_valueType;

	public CircleMeter ( Circle c) {
		this.c=c;
	}
	
	private int radius_midValue=30;
	private int radius_currentValue=0;
	
	private int color_midValue=27;
	private int color_currentValue=0;

	public int getDuration() {
		return duration;
	}
	public void setDuration(int duration) {
		this.duration = duration;
	}


	public int getRadius_midValue() {
		return radius_midValue;
	}
	public void setRadius_midValue(int radius_midValue) {
		this.radius_midValue = radius_midValue;
	}
	public void setRadius_DataRecordValueType(DataRecordValueType vt) {
		this.radius_valueType = vt;
	}
	public DataRecordValueType getRadius_DataRecordValueType() {
		return radius_valueType;
	}
	public int getRadius_currentValue() {
		return radius_currentValue;
	}
	public void setRadius_currentValue(int radius_currentValue) {
		this.radius_currentValue = radius_currentValue;
	}

	public int getColor_midValue() {
		return color_midValue;
	}
	public void setColor_midValue(int color_midValue) {
		this.color_midValue = color_midValue;
	}
	public void setColor_DataRecordValueType(DataRecordValueType vt) {
		this.color_valueType = vt;
	}
	public DataRecordValueType getColor_DataRecordValueType() {
		return color_valueType;
	}
	public int getColor_currentValue() {
		return color_currentValue;
	}
	public void setColor_currentValue(int color_currentValue) {
		this.color_currentValue = color_currentValue;
	}

	@Override
	public void run() {

		// GREEN  #008000
		// YELLOW #FFFF00
		// RED    #FF0000
		double red=0;
		double green=0;
		double blue=0;
		double opacity=0;
		Color color=new Color( red, green, blue, opacity);
		
		FillTransition fill = new FillTransition(Duration.millis(duration)); 
		fill.setToValue(Color.GREEN); 

		FillTransition fill_red = new FillTransition(Duration.millis(duration)); 
		fill_red.setToValue(Color.RED); 

		RotateTransition rotate = new RotateTransition(Duration.millis(duration)); 
		rotate.setToAngle(360); 

		ScaleTransition scale_min = new ScaleTransition(Duration.millis(duration)); 
		scale_min.setToX(0.5); 
		scale_min.setToY(0.5); 

		ScaleTransition scale_max = new ScaleTransition(Duration.millis(duration)); 
		scale_max.setToX(1.5); 
		scale_max.setToY(1.5); 

		ParallelTransition transition_green = new ParallelTransition(c, 
				/*translate,*/ fill, rotate, scale_max); 
//		transition_green.setCycleCount(Timeline.INDEFINITE);
//		transition_green.setAutoReverse(true); 

		ParallelTransition transition_red = new ParallelTransition(c, 
				/*translate,*/ fill_red, rotate, scale_min); 
//		transition_red.setCycleCount(Timeline.INDEFINITE);
//		transition_red.setAutoReverse(true); 

		int state=0;

		while( true) {

			if( state==0) {
				transition_green.stop();
				transition_red.play();
				state=1;
			} else {
				transition_red.stop();
				transition_green.play();
				state=0;
			}

			try {
				Thread.sleep(2*duration);
			} catch (InterruptedException e) {
			}

		}

	}

	@Override
	public void readEvent(String rawData) throws IOException {
		
		if( !DataRecord.isDataRecord(rawData)) {
			return;
		}
		
		DataRecord dr=null;
		try {
			dr=DataRecord.getDataRecord(rawData);
		} catch (DataRecordException e) {
			System.err.println( "Exception caught parsing data record: "+e.getLocalizedMessage());
			return;
		}
		
		int colorVal=dr.getValue( color_valueType);
		setColor_currentValue(color_currentValue);
		

		int radiusVal=dr.getValue( radius_valueType);
		setRadius_currentValue(radius_currentValue);
		
	}

}
