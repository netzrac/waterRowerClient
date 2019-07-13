package waterRowerClient;

import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Scanner;

public class Client implements Runnable {

	private String host;
	private int port;
	private ArrayList<DataNotifier> notifiers=new ArrayList<DataNotifier>();
	private long waitTime=100;
	private Socket s;
	private Scanner in;

	public Client(String host, int port) throws IOException {
		this.host=host;
		this.port=port;
		try {
			s = new Socket(host,port); // connect to server
			in = new Scanner(s.getInputStream());
		} catch (IOException e) {
			System.out.println("Exception caught connecting to server: "+e.getLocalizedMessage());
			throw e;
		} 
		
	}

	@Override
	public void run() {
		
	    while (in.hasNextLine()) {
	    	String data=in.nextLine();
	        System.out.println("RCVD: "+data);
	        if( "X".equals(data)) {
		        System.out.println("Stop receiving input from server.");
		        break;
	        }
	    }
	
		try {
			s.close();
		} catch (IOException e) {
			System.out.println("Exception closing connection to server: "+e.getLocalizedMessage());
			return;
		} 
		
	}
	
	void registerNotifier(DataNotifier notifier) {
		notifiers.add(notifier);
	}
	
	void unregisterNotifier(DataNotifier notifier) {
		notifiers.remove(notifier);
	}

}
