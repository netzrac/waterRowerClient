package waterRowerClient;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.layout.HBox;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

public class StdHBox extends HBox {

	public synchronized void setLabel(String label) {
		this.label.setText(label);
	}

	public synchronized void setCurrValue(int value) {
		this.currValue.setText(String.valueOf(value));
	}

	public synchronized void setCurrValue(String value) {
		this.currValue.setText(value);
	}

	public synchronized void setMidValue(int value) {
		this.midValue.setText("["+String.valueOf(value)+"]");
	}

	public synchronized void setMidValue(String value) {
		this.midValue.setText("["+value+"]");
	}
	
	private Text label;
	private Text currValue;
	private Text midValue;

	public StdHBox(String labelText) {
		super();
		initBox( labelText, 36);
	}
	
	public StdHBox(String labelText, int fontSize) {
		super();
		initBox( labelText, fontSize);
	}
	
	private void initBox(String labelText, int fontSize) {
		setPadding(new Insets(15, 12, 15, 12));
		setAlignment(Pos.CENTER);
	    setSpacing(10);
	    setStyle("-fx-background-color: #d3d3d3;");
		label=new Text( labelText);
		label.setFont(Font.font ("Verdana", fontSize));
		currValue=new Text( "");
		currValue.setFont(Font.font ("Verdana", fontSize));
		midValue=new Text( "");
		midValue.setFont(Font.font ("Verdana", fontSize*2/3));
		getChildren().addAll( label, currValue, midValue);
	}

}
