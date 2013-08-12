
package util.str;

/**
 * Methods for finding the longest common subsequence of two strings.
 *
 * @author <a href="mailto:jdale@berkeley.edu">Joseph Dale</a>
 * @version 20050428
 */
public class LongestCommonSubsequence {

    /**
     * Returns the longest common subsequence of two strings, computed
     * by the Wagner-Fischer algorithm.
     *
     * @see <a href="">Robert A. Wagner and Michael J. Fischer, "The String-to-String Correction Problem". J. ACM, vol. 21, no. 1, Jan. 1974, pp. 168-173.</a>
     */
    public static String wagnerFischer(String s1, String s2) {

	int m = s1.length();
	int n = s2.length();
	int[][] L = new int[m+1][n+1];

	/* Initialize the first column. */
	for (int i = 0; i <= m; i += 1) {

	    L[i][0] = 0;

	}

	/* Initialize the first row. */
	for (int j = 0; j <= n; j += 1) {

	    L[0][j] = 0;

	}

	/* Fill in the matrix. */
	for (int i = 1; i <= m; i += 1) {

	    for (int j = 1; j <= n; j += 1) {

		if (s1.charAt(i-1) == s2.charAt(j-1)) {

		    L[i][j] = L[i-1][j-1] + 1;

		} else {

		    L[i][j] = Math.max(L[i-1][j], L[i][j-1]);

		}

	    }

	}

	System.out.println("L[m][n] = " + L[m][n]);

	/* Trace back to find the sequence. */
	StringBuffer lcs = new StringBuffer(); // buffer to accumulate the reverse of the LCS.
	int i = m;
	int j = n;

	while ((i > 0) && (j > 0)) {

	    if (s1.charAt(i-1) == s2.charAt(j-1)) {

		lcs.append(s1.charAt(i-1));
		i -= 1;
		j -= 1;

	    } else if (L[i-1][j] > L[i][j-1]) {

		i -= 1;

	    } else {

		j -= 1;

	    }

	}

	return lcs.reverse().toString();

    }

}
