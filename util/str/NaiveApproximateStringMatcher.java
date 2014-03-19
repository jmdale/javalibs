package util.str;

/**
 * An implementation of the "naive" approximate string matching
 * algorithm. Conceptually speaking, the pattern string being searched
 * for is treated as a template which is slid across the text string
 * being searched. Corresponding characters in the pattern and the
 * text are compared until either the whole pattern is matched or
 * until a specified number of non-matching characters are found; the
 * template is then shifted to the next position.
 *
 * @author <a href="mailto:jmdale@gmail.com">Joseph Dale</a>
 * @version 20140318
 */
public class NaiveApproximateStringMatcher extends StringMatcher {

    private String pattern;
    private String text;
    private int start;
    private int mismatches;

    /**
     * The length of the pattern.
     */
    private int m;

    /**
     * The length of the text.
     */
    private int n;

    public NaiveApproximateStringMatcher(String pattern, String text, int mismatches) {
	if (pattern.length() > text.length()) {
	    throw new IllegalArgumentException("Pattern must be no longer than text.");
	}

	if (mismatches < 0) {
	    throw new IllegalArgumentException("Number of allowed mismatches must be non-negative.");
	}

	this.pattern = pattern;
	this.text = text;
	this.mismatches = mismatches;
	this.start = 0;
	this.m = pattern.length();
	this.n = text.length();
    }

    public int start() {
	return start;
    }

    public void reset() {
	this.start = 0;
    }

    public int nextMatch() {
	for (int i = start; i <= n - m; ++i) {
	    boolean match = true;
	    int mm = 0;

	    for (int j = 0; j < m; ++j) {
		if (pattern.charAt(j) != text.charAt(i + j)) {
		    mm += 1;
		    if (mm > mismatches) {
			match = false;
			break;
		    }
		}
	    }

	    if (match) {
		start = i + 1;
		return i;
	    }
	}

	return -1;
    }

}
