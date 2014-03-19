package util.str;

/**
 * Finds the largest prime number q such that q * (Character.MAX_VALUE
 * + 1) < Integer.MAX_VALUE. This is the largest prime number that can
 * be used as the modulus in the Rabin-Karp string matcher without
 * overflow; larger primes are better since they will lead to fewer
 * spurious hits.
 *
 * @author <a href="mailto:jmdale@gmail.com">Joseph Dale</a>
 * @version 20140318
 */
public class FindRabinKarpPrime {

    public static void main(String[] args) {
	int maxq = Integer.MAX_VALUE / (Character.MAX_VALUE + 1);

	System.out.println("Character.MAX_VALUE + 1 = " + (Character.MAX_VALUE + 1));
	System.out.println("Integer.MAX_VALUE = " + Integer.MAX_VALUE);
	System.out.println("maxq = " + maxq);

	for (int q = maxq; q > 1; --q) {
	    if (isPrime(q)) {
		System.out.println(q + " is prime.");
		break;
	    } else {
		System.out.println(q + " is not prime.");
	    }
	}
    }

    private static boolean isPrime(int q) {
	int maxFactor = ((int) Math.sqrt(q)) + 1;
	for (int i = 2; i <= maxFactor; ++i) {
	    if ((q % i) == 0) {
		return false;
	    }
	}
	return true;
    }

}
