package waterRowerClient;

import java.io.IOException;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

public class WaterRowerClient {
	
	private Thread clientThread;

	public WaterRowerClient(String host, int port) throws IOException {
		// Connect with Water Rower
		clientThread=new Thread(new Client( host, port));
		clientThread.start();
	}

	public static void main(String[] args) {
		
	    Options options = new Options();

        Option input = new Option("h", "host", true, "host name or IP address of water rower service");
        input.setRequired(true);
        options.addOption(input);

        Option output = new Option("p", "port", true, "water rower service port");
        output.setRequired(true);
        options.addOption(output);

        CommandLineParser parser = new DefaultParser();
        HelpFormatter formatter = new HelpFormatter();
        CommandLine cmd = null;

        try {
            cmd = parser.parse(options, args);
        } catch (ParseException e) {
            System.out.println(e.getMessage());
            formatter.printHelp("utility-name", options);

            System.exit(1);
        }

        String host = cmd.getOptionValue("host");
        int port = Integer.parseInt(cmd.getOptionValue("port"));

        System.out.println("Connection parameter: "+host+":"+port);
		
		try {
			new WaterRowerClient(host, port);
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

}
