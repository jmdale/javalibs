package util.str;

/**
 * An implementation of the "naive" string matching
 * algorithm. Conceptually speaking, the pattern string being searched
 * for is treated as a template which is slid across the text string
 * being searched. Corresponding characters in the pattern and the
 * text are compared until either the whole pattern is matched or a
 * non-matching character is found; the template is then shifted to
 * the next position.
 *
 * @author <a href="mailto:jmdale@gmail.com">Joseph Dale</a>
 * @version 20140318
 */
public class NaiveStringMatcher extends StringMatcher {

    private String pattern;
    private String text;
    private int start;

    /**
     * The length of the pattern.
     */
    private int m;

    /**
     * The length of the text.
     */
    private int n;

    public NaiveStringMatcher(String pattern, String text, int start) {
	if (pattern.length() > text.length()) {
	    throw new IllegalArgumentException("Pattern must be no longer than text.");
	}

	if (start < 0) {
	    throw new IllegalArgumentException("Start index must be non-negative.");
	}

	this.pattern = pattern;
	this.text = text;
	this.start = start;
	this.m = pattern.length();
	this.n = text.length();
    }

    public NaiveStringMatcher(String pattern, String text) {
	this(pattern, text, 0);
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

	    for (int j = 0; j < m; ++j) {
		if (pattern.charAt(j) != text.charAt(i + j)) {
		    match = false;
		    break;
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
