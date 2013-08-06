package util;

/**
 * Given the name of a file, prints the lines of the file to
 * System.out in reverse order.
 *
 * @author <a href="mailto:jmdale@gmail.com">Joseph Dale</a>
 * @version 20130805
 */
public class ReverseLines {

    /*
     * Usage: java util.ReverseLines <file>
     */
    public static void main(String[] args) {
	if (args.length != 1) {
	    usage();
	    System.exit(1);
	}

	try {
	    String[] lines = (String[]) IOUtils.readStrings(args[0]).toArray(new String[0]);
	    for (int i = lines.length - 1; i >= 0; --i) {
		System.out.println(lines[i]);
	    }
	} catch (Exception e) {
	    e.printStackTrace();
	    System.exit(1);
	}
    }

    private static void usage() {
	System.err.println("Usage: java util.ReverseLines <file>");
    }
}
