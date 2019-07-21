package waterRowerClient;

import java.io.IOException;
import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;

public class WaterRowerClient extends Application {
	
	private Thread clientThread;
	private Client client;
	private SessionRecorder sessionRecorder;
	private CircleMeter cm;
	private Circle c;
	private StdHBox topBox;
	private StdVBox leftBox;
	private StdVBox rightBox;
	private GridPane centerPane;
	private VBox bottomBox;
	private DefaultNotifier dn;
	private StdHBox levelBox;
	private StdHBox distBox;
	private StdHBox timeBox;
	private HBox stdValBox;

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
		sessionRecorder.start();
	}
	
	public void stopRecording() throws IOException {
		sessionRecorder.stop();
		client.unregisterNotifier(sessionRecorder);
		client.unregisterNotifier(cm);
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

      // TextBoxes
        topBox=new StdHBox(" ", 24);
        leftBox=new StdVBox(" ",24);
        rightBox=new StdVBox(" ",24);
        
        centerPane=new GridPane();
	    centerPane.setStyle("-fx-background-color: #d3d3d3;");
	    centerPane.setPrefSize(1200, 1200);
        
		// Start Circlemeter
		c = new Circle( 400, 400, 100);
        cm=new CircleMeter(c, leftBox, topBox, rightBox);
        
	    centerPane.setPrefSize(1200, 1200);
	    centerPane.setAlignment(Pos.CENTER);
        centerPane.getChildren().add(c);


        // Button 
        StartStopButton btn = new StartStopButton(this);
        btn.setAlignment(Pos.CENTER);
        
        // Standard Values
        levelBox=new StdHBox("LVL", 24);
        distBox=new StdHBox("DIST", 24);
        timeBox=new StdHBox("TIME", 24);

        // Standard values Box
        stdValBox=new HBox();
        stdValBox.setAlignment(Pos.CENTER);
        stdValBox.getChildren().addAll(levelBox, distBox, timeBox);

        dn=new DefaultNotifier(levelBox, distBox, timeBox);
        client.registerNotifier(dn);

        // Bottom Box
        
        bottomBox=new VBox();
        bottomBox.setStyle("-fx-background-color: #d3d3d3;");
        
        bottomBox.getChildren().addAll(stdValBox, btn);
        
        bottomBox.setAlignment(Pos.CENTER);
        bottomBox.setSpacing(10);

        // create BorderPane
        
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
        primaryStage.setScene(new Scene(borderPane, 450, 450));
        primaryStage.show();	
        
	}

}
