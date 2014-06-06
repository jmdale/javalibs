package math;

/**
 * Computes Stirling numbers of the second kind.
 *
 * @author <a href="mailto:jmdale@gmail.com">Joseph Dale</a>
 * @version 20050907
 */
public class Stirling2 {

    public static void main(String[] args) {
	if (args.length != 1) {
	    System.err.println("Usage: java math.Stirling2 <n>");
	    System.exit(1);
	}

	int n = Integer.parseInt(args[0]);
	int[][] s = compute(n);

	for (int i = 0; i <= n; ++i) {
	    for (int j = 0; j <= i; ++j) {
		System.out.format("%6d", s[i][j]);
	    }
	    System.out.println();
	}
    }

    private static int[][] compute(int n) {
	int[][] s = new int[n+1][n+1];

	for (int i = 0; i <= n; i += 1) {
	    for (int j = 0; j <= i; j += 1) {
		if (j == 0) {
		    if (i == 0) {
			s[i][j] = 1;
		    } else {
			s[i][j] = 0;
		    }
		} else if (i == j) {
		    s[i][j] = 1;
		} else {
		    s[i][j] = s[i-1][j-1] + j * s[i-1][j];
		}
	    }
	}

	return s;
    }

}
