package util;

import math.DoubleVector;
import math.IntVector;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.IOException;
import java.io.Reader;

import java.util.List;
import java.util.ArrayList;

/**
 * Utilities for input and output.
 *
 * @author <a href="mailto:jmdale@gmail.com">Joseph Dale</a>
 * @version 20100125
 */
public class IOUtils {

    /**
     * Reads a list of doubles from a Reader. The input is expected to
     * contain one double per line.
     */
    public static DoubleVector readDoubles(Reader r) throws IOException {
	BufferedReader br = new BufferedReader(r);
	List<Double> doubles = new ArrayList<Double>();

	while (true) {
	    String line = br.readLine();
	    if (line == null) {
		break;
	    } else {
		doubles.add(new Double(line));
	    }
	}

	DoubleVector dv = new DoubleVector(doubles.size());
	int i = 0;
	for (double d : doubles) {
	    dv.set(i++, d);
	}

	return dv;
    }

    /**
     * Reads a list of doubles from a File.
     */
    public static DoubleVector readDoubles(File file) throws IOException {
	return readDoubles(new FileReader(file));
    }

    /**
     * Reads a list of doubles from a file named by the given String.
     */
    public static DoubleVector readDoubles(String fileName) throws IOException {
	return readDoubles(new FileReader(fileName));
    }

    /**
     * Reads lines from the given InputStream, turning each line into
     * a double, and returns a vector containing the doubles read.
     */
    public static DoubleVector readDoubles(InputStream is) throws IOException {
	return readDoubles(new InputStreamReader(is));
    }

    /**
     * Reads a list of integers from a Reader. The input is expected
     * to contain one integer per line.
     */
    public static IntVector readInts(Reader r) throws IOException {
	BufferedReader br = new BufferedReader(r);
	List<Integer> integers = new ArrayList<Integer>();

	while (true) {
	    String line = br.readLine();
	    if (line == null) {
		break;
	    } else {
		integers.add(new Integer(line));
	    }
	}

	IntVector iv = new IntVector(integers.size());
	int i = 0;
	for (int x : integers) {
	    iv.set(i++, x);
	}

	return iv;
    }

    /**
     * Reads a list of integers from a File.
     */
    public static IntVector readInts(File file) throws IOException {
	return readInts(new FileReader(file));
    }

    /**
     * Reads a list of integers from a file named by the given String.
     */
    public static IntVector readInts(String fileName) throws IOException {
	return readInts(new FileReader(fileName));
    }

    /**
     * Reads lines from the given InputStream, turning each line into
     * an integer, and returns a vector containing the integers read.
     */
    public static IntVector readInts(InputStream is) throws IOException {
	return readInts(new InputStreamReader(is));
    }

    /**
     * Reads lines from the given Reader and returns a List containing
     * the Strings read.
     */
    public static List<String> readStrings(Reader r) throws IOException {
	BufferedReader br = new BufferedReader(r);
	List<String> stringList = new ArrayList<String>();

	while (true) {
	    String line = br.readLine();
	    if (line == null) {
		break;
	    } else {
		stringList.add(line);
	    }
	}

	return stringList;
    }

    /**
     * Reads lines from the given InputStream and returns a List
     * containing the Strings read.
     */
    public static List<String> readStrings(InputStream is) throws IOException {
	return readStrings(new InputStreamReader(is));
    }

    /**
     * Reads lines from the given File and returns a List containing
     * the Strings read.
     */
    public static List<String> readStrings(File file) throws IOException {
	return readStrings(new FileReader(file));
    }

    /**
     * Reads lines from the given file and returns a List containing
     * the Strings read.
     */
    public static List<String> readStrings(String filename) throws IOException {
	return readStrings(new FileReader(filename));
    }

    /**
     * Reads tab-delimited data from a Reader; the given column
     * (indexed from 0) of the input is expected to contain doubles,
     * which are returned in a DoubleVector.
     */
    public static DoubleVector readDoubles(Reader r, int col) throws IOException {
	BufferedReader br = new BufferedReader(r);
	List<Double> doubles = new ArrayList<Double>();

	while (true) {
	    String line = br.readLine();
	    if (line == null) {
		break;
	    } else {
		String[] tokens = Strings.tokenize(line, "\t");
		doubles.add(new Double(tokens[col]));
	    }
	}

	DoubleVector dv = new DoubleVector(doubles.size());
	int i = 0;
	for (double d : doubles) {
	    dv.set(i++, d);
	}

	return dv;
    }

    /**
     * Reads tab-delimited data from a file; the given column (indexed
     * from 0) of the input is expected to contain doubles, which are
     * returned in a DoubleVector.
     */
    public static DoubleVector readDoubles(File file, int col) throws IOException {
	return readDoubles(new FileReader(file), col);
    }

    /**
     * Reads tab-delimited data from a file; the given column (indexed
     * from 0) of the input is expected to contain doubles, which are
     * returned in a DoubleVector.
     */
    public static DoubleVector readDoubles(String filename, int col) throws IOException {
	return readDoubles(new File(filename), col);
    }

    /**
     * Used by yornp.
     */
    private static BufferedReader in;

    /**
     * Prints a prompt to System.out, then reads a line from
     * System.in. If the response starts with 'y', returns true; if
     * the response starts with 'n', returns false; otherwise loops
     * until a valid response is received.
     */
    public static boolean yornp(String prompt) {
	if (in == null) {
	    in = new BufferedReader(new InputStreamReader(System.in));
	}

	while (true) {
	    System.err.print(prompt);
	    try {
		String response = in.readLine();
		if (response.startsWith("y")) {
		    return true;
		} else if (response.startsWith("n")) {
		    return false;
		} else {
		    System.err.println("Unrecognized response: '" + response + "'");
		    continue;
		}
	    } catch (IOException ioe) {
		return false;
	    }
	}
    }

}
