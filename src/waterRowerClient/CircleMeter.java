package waterRowerClient;

import com.sun.corba.se.impl.orb.ORBConfiguratorImpl.ConfigParser;

import javafx.animation.FillTransition;
import javafx.animation.ParallelTransition;
import javafx.animation.RotateTransition;
import javafx.animation.ScaleTransition;
import javafx.animation.Timeline;
import javafx.animation.TranslateTransition;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.util.Duration;

public class CircleMeter implements Runnable {

	private Circle c;
	int duration=ClientConfig.getIntOptionValue("animationDuration", 1000);

	public CircleMeter ( Circle c) {
		this.c=c;
	}
	
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



	public int getColor_currentValue() {
		return color_currentValue;
	}



	public void setColor_currentValue(int color_currentValue) {
		this.color_currentValue = color_currentValue;
	}

	int radius_midValue=30;
	int radius_currentValue=0;
	
	int color_midValue=27;
	int color_currentValue=0;
	
	

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
		transition_green.setCycleCount(Timeline.INDEFINITE);
		transition_green.setAutoReverse(true); 

		ParallelTransition transition_red = new ParallelTransition(c, 
				/*translate,*/ fill_red, rotate, scale_min); 
		transition_red.setCycleCount(Timeline.INDEFINITE);
		transition_red.setAutoReverse(true); 

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

}
