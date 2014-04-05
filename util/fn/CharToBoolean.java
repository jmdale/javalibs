package util.fn;

/**
 * A function which takes a char to a boolean.
 *
 * @author <a href="mailto:jmdale@gmail.com">Joseph Dale</a>
 * @version 20140404
 */
public abstract class CharToBoolean {

    /**
     * Applies this function to the given char.
     */
    public abstract boolean apply(char c);

    /**
     * Returns a new function which computes the complement of this
     * function.
     */
    public CharToBoolean not() {
	return new CharToBoolean() {
		public boolean apply(char c) {
		    return !CharToBoolean.this.apply(c);
		}
	    };
    }

    /**
     * Returns a new function which computes the conjunction of this
     * function and the given function.
     */
    public CharToBoolean and(final CharToBoolean ctb) {
	return new CharToBoolean() {
		public boolean apply(char c) {
		    return (CharToBoolean.this.apply(c) && ctb.apply(c));
		}
	    };
    }

    public CharToBoolean or(final CharToBoolean ctb) {
	return new CharToBoolean() {
		public boolean apply(char c) {
		    return (CharToBoolean.this.apply(c) || ctb.apply(c));
		}
	    };
    }

    /**
     * Applies this function to each character in the given String.
     */
    public boolean[] map(String s) {
	boolean[] b = new boolean[s.length()];

	for (int i = 0; i < b.length; i++) {
	    b[i] = apply(s.charAt(i));
	}

	return b;
    }

    /**
     * Returns a new String containing the characters in the given
     * String for which this function returns true.
     */
    public String keep(String s) {
	StringBuffer sb = new StringBuffer();

	for (int i = 0; i < s.length(); i += 1) {
	    char c = s.charAt(i);
	    if (apply(c)) {
		sb.append(c);
	    }
	}

	return sb.toString();
    }

    /**
     * Returns a new String containing the characters in the given
     * String for which this function returns false.
     */
    public String remove(String s) {
	StringBuffer sb = new StringBuffer();

	for (int i = 0; i < s.length(); i++) {
	    char c = s.charAt(i);
	    if (!apply(c)) {
		sb.append(c);
	    }
	}

	return sb.toString();
    }

    /**
     * Counts the number of characters in the given String for which
     * this function returns true.
     */
    public int count(String s) {
	int n = 0;

	for (int i = 0; i < s.length(); ++i) {
	    if (apply(s.charAt(i))) {
                ++n;
	    }
	}

	return n;
    }

}
