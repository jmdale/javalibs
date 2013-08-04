package util;

import java.util.HashSet;
import java.util.Set;

/**
 * Utilities for working with arrays.
 *
 * @author <a href="mailto:jmdale@gmail.com">Joseph Dale</a>
 * @version 20130803
 */
public class Arrays {

    /**
     * Converts an array of ints to an array of doubles.
     */
    public static double[] toDoubles(int[] x) {
	double[] d = new double[x.length];
	for (int i = 0; i < x.length; ++i) {
	    d[i] = x[i];
	}
	return d;
    }

    /**
     * Copies an array of doubles.
     */
    public static double[] copy(double[] d) {
	double[] copy = new double[d.length];
	System.arraycopy(d, 0, copy, 0, d.length);
	return copy;
    }

    /**
     * Copies a two-dimensional array of doubles.
     */
    public static double[][] copy(double[][] x) {
	double[][] copy = new double[x.length][];
	for (int i = 0; i < x.length; ++i) {
	    copy[i] = copy(x[i]);
	}
	return copy;
    }

    /**
     * Returns a string displaying the contents of an array of
     * doubles.
     */
    public static String toString(double[] d) {
	if (d.length == 0) {
	    return "[]";
	} else {
	    StringBuilder sb = new StringBuilder("[");
	    sb.append(d[0]);
	    for (int i = 1; i < d.length; ++i) {
		sb.append(", ");
		sb.append(d[i]);
	    }
	    sb.append("]");
	    return sb.toString();
	}
    }

    /**
     * Returns an array containing all but the first nSkip elements of
     * s.
     */
    public static String[] subarray(String[] s, int nSkip) {
	String[] sSub = new String[s.length - nSkip];
	System.arraycopy(s, nSkip, sSub, 0, sSub.length);
	return sSub;
    }

    /**
     * Returns a set containing all but the first nSkip elements of s.
     */
    public static Set<String> subset(String[] s, int nSkip) {
	Set<String> ss = new HashSet<String>();
	for (int i = nSkip; i < s.length; i++) {
	    ss.add(s[i]);
	}
	return ss;
    }

    public static double[] parseDoubles(String[] s) {
	double[] d = new double[s.length];
	for (int i = 0; i < s.length; ++i) {
	    d[i] = Double.parseDouble(s[i]);
	}
	return d;
    }
}
