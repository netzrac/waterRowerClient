package waterRowerClient;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Date;

public class SessionRecorder implements DataNotifier {

	private String path;
	private File file;
	private PrintWriter out;

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
	
	void start() throws IOException {  
		// file name
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss") ;
		// open file
		String filePath=path+File.separator+dateFormat.format(new Date()) + ".data";
		file = new File(filePath) ;
		out=new PrintWriter( new BufferedWriter( new FileWriter(file)));
	}
	
	void stop() throws IOException {
		// close file
		out.flush();
		out.close();
		// create statistics
	}
	
	void delete() throws IOException {
		stop();
		file.delete();
	}
	
	@Override
	public void readEvent(String s) throws IOException {
		out.println(s);
		out.flush();
	}

}
