package util;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.List;

/**
 * Given a one-column key file and a data file with any number of
 * tab-delimited columns, selects lines from the data file such that
 * the element in the first column appears in the key file.
 *
 * @author <a href="mailto:jmdale@gmail.com">Joseph Dale</a>
 * @version 20070921
 */
public class SelectLines {

    /**
     * Usage: java util.SelectLines <key-file> <data-file>
     */
    public static void main(String[] args) {
	if (args.length != 2) {
	    usage();
	    System.exit(1);
	}

	try {
	    List<String> keys = IOUtils.readStrings(args[0]);
	    BufferedReader data = new BufferedReader(new FileReader(args[1]));

	    while (true) {
		String line = data.readLine();

		if (line == null) {
		    break;
		}

		String[] fields = line.split("\\t");

		if (keys.contains(fields[0])) {
		    System.out.println(line);
		}
	    }
	} catch (Exception e) {
	    e.printStackTrace();
	    System.exit(1);
	}
    }

    private static void usage() {
	System.err.println("Usage: java util.SelectLines <key-file> <data-file>");
    }
}
