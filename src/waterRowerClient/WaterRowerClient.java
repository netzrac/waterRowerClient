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
import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
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
	private Text heartrateText;

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
		client.registerDataNotifier(sessionRecorder);
		client.registerHeartrateNotifier(sessionRecorder);
		client.registerDataNotifier(cm);
		sessionRecorder.start();
	}
	
	public void stopRecording() throws IOException {
		if(trainingBtn.isDisable()) {
			trainingBtn.setDisable(false);
		}
		sessionRecorder.stop();
		client.unregisterDataNotifier(sessionRecorder);
		client.unregisterHeartrateNotifier(sessionRecorder);
		client.unregisterDataNotifier(cm);
	}
	
	public void startTraining() throws IOException {
		startStopBtn.setDisable(true);
		client.registerDataNotifier(trainer);
		trainer.startTraining();
		startRecording();
	}
	
	public void stopTraining() throws IOException {
		startStopBtn.setDisable(false);
		commandRemain.setText("");
		stopRecording();
		trainer.stopTraining();
		client.unregisterDataNotifier(trainer);
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
        commandRemain.setFont(Font.font ("Verdana", 36));
        heartrateText=new Text( "");
        heartrateText.setFont(Font.font ("Verdana", 36));
        commandRemainHBox=new HBox();
        commandRemainHBox.setAlignment(Pos.CENTER);
        commandRemainHBox.getChildren().addAll(commandRemain, heartrateText);
        
        trainer=new Trainer(commandText, commandRemain);

      // TextBoxes
        topBox=new StdHBox(" ", 36);
        leftBox=new StdVBox(" ",36);
        rightBox=new StdVBox(" ",36);
        
        centerPane=new GridPane();
	    centerPane.setStyle("-fx-background-color: #d3d3d3;");
	    centerPane.setPrefSize(1200, 1200);
        
		// Start Circlemeter
		c = new Circle( 800, 800, 100);
		c.setFill(Color.LIGHTCYAN);
        cm=new CircleMeter(c, leftBox, topBox, rightBox);
        
	    centerPane.setPrefSize(800, 800);
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

        dn=new DefaultNotifier(levelBox, distBox, timeBox, heartrateText);
        client.registerDataNotifier(dn);
        client.registerHeartrateNotifier(dn);

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
