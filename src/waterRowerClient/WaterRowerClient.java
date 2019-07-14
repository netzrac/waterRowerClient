package waterRowerClient;

import java.io.IOException;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

public class WaterRowerClient extends Application {
	
	private Thread clientThread;
	private Client client;
	private SessionRecorder sessionRecorder;
	private MonitorTextArea ta;

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
		ta.clear();
		client.registerNotifier(ta);
		sessionRecorder.start();
	}
	
	public void stopRecording() throws IOException {
		sessionRecorder.stop();
		client.unregisterNotifier(sessionRecorder);
		client.unregisterNotifier(ta);
	}

	public static void main(String[] args) {
		ClientConfig.setConfigOptions(args);
        launch(args);
        System.exit(0);
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		
        primaryStage.setTitle("Hello World!");
        StartStopButton btn = new StartStopButton(this);
        
        ta=new MonitorTextArea();
        
        StackPane root = new StackPane();
        root.getChildren().add(ta);
        root.getChildren().add(btn);
        primaryStage.setScene(new Scene(root, 300, 250));
        primaryStage.show();	
        
	}

}
