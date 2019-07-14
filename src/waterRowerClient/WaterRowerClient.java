package waterRowerClient;

import java.io.IOException;

public class WaterRowerClient {
	
	private Thread clientThread;
	private Client client;
	private SessionRecorder sessionRecorder;

	public WaterRowerClient(String host, int port) throws IOException {
		// Connect with Water Rower
		client=new Client( host, port);
		clientThread=new Thread(client);
		clientThread.start();
		// Send some commands...
		while( true) {
			client.sendCommand( Client.Commands.CMD_RESET);
			sessionRecorder =new SessionRecorder();
			client.registerNotifier(sessionRecorder);
			sessionRecorder.start();
			try {
				Thread.sleep( 120000);
			} catch (InterruptedException e) {
			}
			sessionRecorder.stop();
			client.unregisterNotifier(sessionRecorder);
		}
	}

	public static void main(String[] args) {
		
		ClientConfig.setConfigOptions(args);

        String host = ClientConfig.getStringOptionValue("host");
        int port = ClientConfig.getIntOptionValue("port");

        System.out.println("Connection parameter: "+host+":"+port);
		
		try {
			new WaterRowerClient(host, port);
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

}
