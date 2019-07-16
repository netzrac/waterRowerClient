package waterRowerClient;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

public class ClientConfig {

    private static CommandLine cmd = null;
	
	public static void setConfigOptions(String[] args) {
	    Options options = new Options();

        Option host = new Option("h", "host", true, "host name or IP address of water rower service");
        host.setRequired(true);
        options.addOption(host);

        Option port = new Option("p", "port", true, "water rower service port");
        port.setRequired(true);
        options.addOption(port);

        Option path = new Option("f", "folder", true, "path to folder for storing session recordings");
        path.setRequired(false);
        options.addOption(path);

        Option ad = new Option("ad", "animationDuration", true, "duration for graphical animation");
        ad.setRequired(false);
        options.addOption(ad);

        
        
        CommandLineParser parser = new DefaultParser();
        HelpFormatter formatter = new HelpFormatter();

        try {
            cmd = parser.parse(options, args);
        } catch (ParseException e) {
            System.out.println(e.getMessage());
            formatter.printHelp("utility-name", options);

            System.exit(1);
        }
	}

	public static String getStringOptionValue(String option) {
	       return cmd.getOptionValue(option);

	}

	public static int getIntOptionValue(String option, int defaultValue) {
		if( cmd.hasOption(option)) {
			return Integer.parseInt(cmd.getOptionValue(option));
		}
        return defaultValue;
	}

	public static int getIntOptionValue(String option) {
        return Integer.parseInt(cmd.getOptionValue(option));
	}

}
