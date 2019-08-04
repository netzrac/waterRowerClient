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

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Date;

public class SessionRecorder implements DataNotifier, HeartrateNotifier {

	private String path;
	private File file;
	private PrintWriter out;
	SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss") ;
	private boolean writeTimestamp=false;
	private String heartrate="000";

	public SessionRecorder() {
		path = ClientConfig.getStringOptionValue( "folder");
		if(path==null||path.isEmpty()) {
			path=System.getProperty("user.home")+File.separator+"waterRower";
		}
		File f=new File(path);
		if( !f.exists()) {
			f.mkdirs();
		}
	}
	
	public void start() throws IOException {  
		// file name
		String filePath=path+File.separator+dateFormat.format(new Date()) + ".data";
		// open file
		file = new File(filePath) ;
		out=new PrintWriter( new BufferedWriter( new FileWriter(file)));
		// write timestamp
		out.println("DS:"+dateFormat.format(new Date()));
	}
	
	public void stop() throws IOException {
		// write timestamp
		out.println("DX:"+dateFormat.format(new Date()));
		// close file
		out.flush();
		out.close();
		// create statistics
	}
	
	public void delete() throws IOException {
		stop();
		file.delete();
	}
	
	@Override
	public void readEvent(String s) throws IOException {
		if (writeTimestamp) {
			// write timestamp
			out.println("DL:"+dateFormat.format(new Date()));
			writeTimestamp=false;
		} else if( "000000".contentEquals(s.substring(17,23))) {
			writeTimestamp=true;
		} 
		out.println(s+heartrate);
	}

	@Override
	public void heartrateEvent(String heartrate) {
		this.heartrate=heartrate;
	}

}
