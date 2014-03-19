package util.str;

import java.util.Vector;

/**
 * An abstract class which defines an interface to an arbitrary
 * string-matching algorithm.
 *
 * @author <a href="mailto:jdale@berkeley.edu">Joseph Dale</a>
 * @version 20050429
 */
public abstract class StringMatcher {

    /**
     * Returns the index of the next occurrence of the pattern in the
     * text, starting from the position after the previous position
     * returned.
     */
    public abstract int nextMatch();

    /**
     * Resets the string matcher so that the next call to nextMatch()
     * will return the first occurrence of the pattern in the text.
     */
    public abstract void reset();

    /**
     * Returns an array containing the indices in the "text" string of
     * each occurrence of the "pattern" string.
     */
    public int[] matches() {
	reset();

	Vector<Integer> matches = new Vector<Integer>();
	int nm;

	while ((nm = nextMatch()) != -1) {
	    matches.add(nm);
	}

	int[] mm = new int[matches.size()];
	int i = 0;

	for (int m : matches) {
	    mm[i] = m;
	    i += 1;
	}

	return mm;
    }

    /**
     * Returns the number of occurrences of the pattern string in the
     * text string.
     */
    public int countMatches() {
	reset();
	int m = 0;
	int nm;
	
	while ((nm = nextMatch()) != -1) {
	    m += 1;
	}

	return m;
    }

    public static void main(String[] args) {
	String p = args[0], t = args[1];
	int mm = Integer.parseInt(args[2]);
	StringMatcher sm = new NaiveApproximateStringMatcher(p, t, mm);
	System.out.println(sm);
	int[] matches = sm.matches();

	if (matches.length == 0) {
	    System.out.println("No match.");
	} else {
	    System.out.println(t);
	    for (int i = 0; i < matches.length; i += 1) {
		System.out.println(spaces(matches[i]) + p);
	    }
	}
    }

    private static String spaces(int n) {
	StringBuffer sb = new StringBuffer();
	for (int i = 0; i < n; i += 1) {
	    sb.append(' ');
	}
	return sb.toString();
    }
}
