package waterRowerClient; 
 
import javafx.animation.FillTransition; 
import javafx.application.Application; 
import javafx.scene.Group; 
import javafx.scene.Scene; 
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle; 
import javafx.stage.Stage; 
import javafx.animation.Timeline; 
import javafx.animation.ParallelTransition; 
import javafx.animation.RotateTransition; 
import javafx.animation.ScaleTransition; 
import javafx.animation.TranslateTransition; 
import javafx.util.Duration; 
 
public class AnimationApp extends Application { 
 
	private int duration = 3000;
	
	
    @Override 
    public void start(Stage stage) { 
    	
    	class Abc123 implements Runnable {
    		
    		private Circle c;

			public Abc123( Circle c) {
    			this.c=c;
    		}
    		
   			@Override
   			public void run() {

    			TranslateTransition translate = 
    	                new TranslateTransition(Duration.millis(duration)); 
    	                translate.setToX(390); 
    	                translate.setToY(390); 
    	         
                FillTransition fill_green = new FillTransition(Duration.millis(duration)); 
                fill_green.setToValue(Color.GREEN); 
         
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
                /*translate,*/ fill_green, rotate, scale_max); 
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
    	};
    	    	
        Group root = new Group(); 
        Scene scene = new Scene(root, 500, 500, Color.BLACK); 
        Circle c = new Circle(250, 250, 125); 
        c.setFill(Color.YELLOW); 
        root.getChildren().add(c); 
        
        new Thread(new Abc123(c)).start();
  
        stage.setTitle("JavaFX Scene Graph Demo"); 
        stage.setScene(scene); 
        stage.show(); 
    } 
 
    public static void main(String[] args) { 
        launch(args); 
    } 
}