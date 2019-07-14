package waterRowerClient;

import java.io.IOException;

import javafx.scene.control.TextArea;

public class MonitorTextArea extends TextArea implements DataNotifier {

	@Override
	public void readEvent(String s) throws IOException {
		appendText(s+"\n");
	}

}
