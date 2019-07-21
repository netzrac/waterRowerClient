package waterRowerClient;

import java.io.IOException;
import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;

public class WaterRowerClient extends Application {
	
	private Thread clientThread;
	private Client client;
	private SessionRecorder sessionRecorder;
	private MonitorTextArea ta;
	private CircleMeter cm;
	private Circle c;
	private StdHBox topBox;
	private StdVBox leftBox;
	private StdVBox rightBox;
	private GridPane centerPane;
	private VBox bottomBox;

	public void init() throws Exception {
		// init Application
		super.init();
		// Connect with Water Rower
		client=new Client( ClientConfig.getStringOptionValue("host"), ClientConfig.getIntOptionValue("port"));
		clientThread=new Thread(client);
		clientThread.start();
	}
	
	public void startRecording() throws IOException {
		client.sendCommand( Client.Commands.CMD_RESET);
		sessionRecorder =new SessionRecorder();
		client.registerNotifier(sessionRecorder);
		client.registerNotifier(cm);
		ta.clear();
		client.registerNotifier(ta);
		sessionRecorder.start();
	}
	
	public void stopRecording() throws IOException {
		sessionRecorder.stop();
		client.unregisterNotifier(sessionRecorder);
		client.unregisterNotifier(cm);
		client.unregisterNotifier(ta);
	}

	public static void main(String[] args) {
		ClientConfig.setConfigOptions(args);
        launch(args);
        System.exit(0);
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		
		// Title
        primaryStage.setTitle("Hello rower!");

        // Button 
        StartStopButton btn = new StartStopButton(this);
        btn.setAlignment(Pos.CENTER);
        
        bottomBox=new VBox();
        bottomBox.setStyle("-fx-background-color: #d3d3d3;");
        bottomBox.getChildren().add(btn);
        bottomBox.setAlignment(Pos.CENTER);
        bottomBox.setSpacing(10);

        // Monitor Area
        ta=new MonitorTextArea();

        // TextBoxes
        topBox=new StdHBox(" ");
        leftBox=new StdVBox(" ");
        rightBox=new StdVBox(" ");
        
        centerPane=new GridPane();
	    centerPane.setStyle("-fx-background-color: #d3d3d3;");
	    centerPane.setPrefSize(1200, 1200);
        
		// Start Circlemeter
		c = new Circle( 400, 400, 100);
        cm=new CircleMeter(c, leftBox, topBox, rightBox);
        
	    centerPane.setPrefSize(1200, 1200);
	    centerPane.setAlignment(Pos.CENTER);
        centerPane.getChildren().add(c);

        BorderPane borderPane = new BorderPane();
        borderPane.setCenter(centerPane);
        borderPane.setTop(topBox);
        BorderPane.setAlignment(topBox, Pos.CENTER);
        borderPane.setLeft(leftBox);
        BorderPane.setAlignment(leftBox, Pos.CENTER);
        borderPane.setRight(rightBox);
        BorderPane.setAlignment(rightBox, Pos.CENTER);
        borderPane.setBottom(bottomBox);
        BorderPane.setAlignment(bottomBox, Pos.CENTER);
        primaryStage.setScene(new Scene(borderPane, 600, 600));
        primaryStage.show();	
        
	}

}
