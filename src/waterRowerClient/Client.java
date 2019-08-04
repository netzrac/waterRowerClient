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
