package waterRowerClient;

import java.io.IOException;
import java.util.ArrayList;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

public class WaterRowerClient extends Application {
	
	private Thread clientThread;
	private Client client;
	private SessionRecorder sessionRecorder;

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
		sessionRecorder.start();
	}
	
	public void stopRecording() throws IOException {
		sessionRecorder.stop();
		client.unregisterNotifier(sessionRecorder);
	}

	public static void main(String[] args) {
		
		ClientConfig.setConfigOptions(args);
	
//		try {
//			new WaterRowerClient(host, port);
//		} catch (IOException e) {
//			e.printStackTrace();
//		}

        launch(args);
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		
		init();	
		
        primaryStage.setTitle("Hello World!");
        StartStopButton btn = new StartStopButton(this);
        
        StackPane root = new StackPane();
        root.getChildren().add(btn);
        primaryStage.setScene(new Scene(root, 300, 250));
        primaryStage.show();	
        
        
	}

}
