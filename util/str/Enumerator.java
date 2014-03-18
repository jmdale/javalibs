package util.str;

import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

/**
 * Provides methods for enumerating all strings over an alphabet
 * (within a given range of lengths).
 *
 * @author <a href="mailto:jmdale@gmail.com">Joseph Dale</a>
 * @version 20140317
 */
public class Enumerator {

    /**
     * The alphabet of characters over which to enumerate.
     */
    private char[] alphabet;

    public Enumerator(String alphabet) {
	this.alphabet = convert(alphabet);
    }

    private static char[] convert(String alphabet) {
	Set<Character> chSet = new HashSet<Character>();

	for (int i = 0; i < alphabet.length(); ++i) {
	    chSet.add(alphabet.charAt(i));
	}

	char[] ch = new char[chSet.size()];
	int i = 0;

	for (char c : chSet) {
	    ch[i] = c;
	    i += 1;
	}

	Arrays.sort(ch);

	return ch;
    }


    public String[] enumerate(int n) {
	if (n < 0) {
	    throw new IllegalArgumentException("Length of strings to enumerate must be non-negative: " + n + ".");
	} else if (n == 0) {
	    return new String[]{""};
	} else {
	    LinkedList<String> strings = new LinkedList<String>();
	    enumerateInner(strings, new char[n], 0, n);

	    return (String[]) strings.toArray(new String[strings.size()]);
	}
    }

    private void enumerateInner(List<String> strings, char[] c, int i, int n) {
	if (i == n) {
	    strings.add(new String(c));
	} else {
	    for (int j = 0; j < alphabet.length; ++j) {
		c[i] = alphabet[j];
		enumerateInner(strings, c, i + 1, n);
	    }
	}
    }

    public static void main(String[] args) {
	Enumerator e = new Enumerator("ACGT");
	String[] s = e.enumerate(Integer.parseInt(args[0]));

	for (String str : s) {
	    System.out.println(str);
	}
    }

}
