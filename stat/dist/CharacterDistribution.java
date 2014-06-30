package stat.dist;

import java.io.Serializable;

/**
 * A probability distribution over an alphabet (a set of characters).
 *
 * @author <a href="mailto:jmdale@gmail.com">Joseph Dale</a>
 * @version 20050428
 */
public class CharacterDistribution implements Serializable {

    private String alphabet;

    /**
     * p[i] is the probability of the ith letter in the alphabet.
     */
    private double[] p;

    /**
     * Creates a new distribution over the given alphabet, such that
     * each character in the alphabet has equal probability.
     */
    public CharacterDistribution(String alphabet) {
	if (alphabet.length() < 1) {
	    throw new IllegalArgumentException("Empty alphabet.");
	}

	this.alphabet = alphabet;
	this.p = new double[alphabet.length()];
	java.util.Arrays.fill(p, 1.0 / p.length);
    }

    /**
     * Creates a new distribution over the given alphabet with the
     * given probabilities.
     */
    public CharacterDistribution(String alphabet, double[] p) {
	this.alphabet = alphabet;
	this.p = new double[p.length];
	System.arraycopy(p, 0, this.p, 0, p.length);
	stat.Lib.normalize(this.p);
    }

    public double probability(char c) {
	int i = alphabet.indexOf(c);
	return (c == -1) ? 0 : p[i];
    }

    public double logProbability(char c) {
	return Math.log(probability(c));
    }

    public char random() {
	return alphabet.charAt(stat.Lib.random(p));
    }

    public String random(int n) {
	if (n < 0) {
	    throw new IllegalArgumentException("Length of random string must be non-negative: " + n + ".");
	} else if (n == 0) {
	    return "";
	} else {
	    char[] c = new char[n];

	    for (int i = 0; i < n; ++i) {
		c[i] = random();
	    }

	    return new String(c);
	}
    }

}
