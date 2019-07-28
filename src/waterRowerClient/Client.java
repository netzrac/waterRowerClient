package waterRowerClient;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Scanner;

public class Client implements Runnable {

	private ArrayList<DataNotifier> notifiers=new ArrayList<DataNotifier>();
	private ArrayList<HeartrateNotifier> heartrateNotifiers=new ArrayList<HeartrateNotifier>();
	private Socket s;
	private Scanner in;
	private PrintWriter out;

	public enum  Commands { CMD_RESET, CMD_HELO};
	
	public Client(String host, int port) throws IOException {
		try {
			s = new Socket(host,port); // connect to server
			in = new Scanner(s.getInputStream());
			out = new PrintWriter(s.getOutputStream(), true);
		} catch (IOException e) {
			System.out.println("Exception caught connecting to server: "+e.getLocalizedMessage());
			throw e;
		} 
		
	}

	@Override
	public void run() {
		
	    while (in.hasNextLine()) {
	    	String data=in.nextLine();
	        switch (data.charAt(0)) {
	        case 'X':
	        	System.out.println("CMD : Stop receiving input from server.");
	        	break;
	        case 'A':
		        System.out.println("DATA: "+data);
		        for( DataNotifier notifier:notifiers) {
		        	try {
						notifier.readEvent(data);
					} catch (IOException e) {
						System.err.println( "Exception caught providing data to notifier: "+e.getLocalizedMessage());
						notifiers.remove(notifier);
						System.err.println( "Notifier removed from list of notifiers.");
					}
		        }
	        	break;
	        case 'P':
		        System.out.println("PLSE: "+data);
		        for( HeartrateNotifier hn:heartrateNotifiers) {
		        	//try {
						hn.heartrateEvent(data.substring(2));
					//} catch (IOException e) {
					//	System.err.println( "Exception caught providing data to notifier: "+e.getLocalizedMessage());
					//	heartrateNotifiers.remove(hn);
					//	System.err.println( "Notifier removed from list of notifiers.");
					//}
		        }
	        	break;
	        default:
	        	System.out.println("UKWN: "+data);
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
	
	void registerDataNotifier(DataNotifier notifier) {
		notifiers.add(notifier);
	}
	
	void unregisterDataNotifier(DataNotifier notifier) {
		notifiers.remove(notifier);
	}
	
	void registerHeartrateNotifier(HeartrateNotifier notifier) {
		heartrateNotifiers.add(notifier);
	}
	
	void unregisterHeartrateNotifier(HeartrateNotifier notifier) {
		heartrateNotifiers.remove(notifier);
	}
	
	void sendCommand( Commands commands) {
		if( commands==Commands.CMD_RESET) {
			out.println("R");
		} else if(commands==Commands.CMD_HELO) {
			out.println("H");
		}
	}

}
