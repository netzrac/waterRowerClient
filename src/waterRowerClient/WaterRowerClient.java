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
import javafx.scene.text.Font;
import javafx.scene.text.Text;
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
	private Text commandText;
	private HBox btnHBox;
	private StartStopButton startStopBtn;
	private TrainingButton trainingBtn;
	private Trainer trainer;
	private Text commandRemain;
	private HBox commandRemainHBox;

	public void init() throws Exception {
		// init Application
		super.init();
		// Connect with Water Rower
		client=new Client( ClientConfig.getStringOptionValue("host"), ClientConfig.getIntOptionValue("port"));
		clientThread=new Thread(client);
		clientThread.start();
	}
	
	public void startRecording() throws IOException {
		if(!startStopBtn.isDisable()) {
			trainingBtn.setDisable(true);
		}
		client.sendCommand( Client.Commands.CMD_RESET);
		sessionRecorder =new SessionRecorder();
		client.registerNotifier(sessionRecorder);
		client.registerNotifier(cm);
		sessionRecorder.start();
	}
	
	public void stopRecording() throws IOException {
		if(trainingBtn.isDisable()) {
			trainingBtn.setDisable(false);
		}
		sessionRecorder.stop();
		client.unregisterNotifier(sessionRecorder);
		client.unregisterNotifier(cm);
	}
	
	public void startTraining() throws IOException {
		startStopBtn.setDisable(true);
		client.registerNotifier(trainer);
		trainer.startTraining();
		startRecording();
	}
	
	public void stopTraining() throws IOException {
		startStopBtn.setDisable(false);
		commandRemain.setText("");
		stopRecording();
		trainer.stopTraining();
		client.unregisterNotifier(trainer);
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
        
        // Trainer + Command Box
        commandText=new Text( "No active training.");
        commandText.setFont(Font.font ("Verdana", 27));
        commandRemain=new Text( "");
        commandRemainHBox=new HBox();
        commandRemainHBox.setAlignment(Pos.CENTER);
        commandRemainHBox.getChildren().add(commandRemain);
        commandRemain.setFont(Font.font ("Verdana", 36));
        
        trainer=new Trainer(commandText, commandRemain);

      // TextBoxes
        topBox=new StdHBox(" ", 36);
        leftBox=new StdVBox(" ",36);
        rightBox=new StdVBox(" ",36);
        
        centerPane=new GridPane();
	    centerPane.setStyle("-fx-background-color: #d3d3d3;");
	    centerPane.setPrefSize(1200, 1200);
        
		// Start Circlemeter
		c = new Circle( 600, 600, 100);
        cm=new CircleMeter(c, leftBox, topBox, rightBox);
        
	    centerPane.setPrefSize(600, 600);
        //centerPane.getChildren().addAll(c, commandRemain);
	    centerPane.add(c, 0, 0);
	    centerPane.add( commandRemainHBox, 0, 0);
	    centerPane.setAlignment(Pos.CENTER);

        // Buttons
        
        startStopBtn = new StartStopButton(this);
        startStopBtn.setAlignment(Pos.CENTER);
        
        trainingBtn = new TrainingButton(this);
        trainingBtn.setAlignment(Pos.CENTER);

        btnHBox=new HBox();
        btnHBox.setAlignment(Pos.CENTER);
        btnHBox.getChildren().addAll(startStopBtn, trainingBtn);
        
        // Standard Values
        levelBox=new StdHBox("LVL", 36);
        distBox=new StdHBox("DIST", 36);
        timeBox=new StdHBox("TIME", 36);

        // Standard values Box
        stdValBox=new HBox();
        stdValBox.setAlignment(Pos.CENTER);
        stdValBox.getChildren().addAll(levelBox, distBox, timeBox);

        dn=new DefaultNotifier(levelBox, distBox, timeBox);
        client.registerNotifier(dn);

        // Bottom Box
        
        bottomBox=new VBox();
        bottomBox.setStyle("-fx-background-color: #d3d3d3;");
        
        bottomBox.getChildren().addAll(commandText, stdValBox, btnHBox);
        
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
        primaryStage.setScene(new Scene(borderPane, 720, 720));
        primaryStage.show();	
        
	}

}
