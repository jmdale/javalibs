package util.str;

/**
 * Methods for finding the longest common subsequence of two strings.
 *
 * @author <a href="mailto:jmdale@gmail.com">Joseph Dale</a>
 * @version 20140531
 */
public class LongestCommonSubsequence {

    /**
     * Returns the longest common subsequence of two strings, computed
     * by the Wagner-Fischer algorithm.
     *
     * @see <a href="http://dl.acm.org/citation.cfm?id=321811">Robert A. Wagner and Michael J. Fischer, "The String-to-String Correction Problem". J. ACM, vol. 21, no. 1, Jan. 1974, pp. 168-173.</a>
     */
    public static String wagnerFischer(String s1, String s2) {
	int m = s1.length();
	int n = s2.length();
	int[][] L = new int[m+1][n+1];

        int maxLen = 0;
        int iMax = 0;
        int jMax = 0;

	/* Initialize the first column. */
	for (int i = 0; i <= m; ++i) {
	    L[i][0] = 0;
	}

	/* Initialize the first row. */
	for (int j = 0; j <= n; ++j) {
	    L[0][j] = 0;
	}

	/* Fill in the matrix. */
	for (int i = 1; i <= m; ++i) {
	    for (int j = 1; j <= n; ++j) {
		if (s1.charAt(i-1) == s2.charAt(j-1)) {
		    L[i][j] = L[i-1][j-1] + 1;
		}

                if (L[i][j] > maxLen) {
                  maxLen = L[i][j];
                  iMax = i;
                  jMax = j;
                }
	    }
	}

	System.err.println("L[m][n] = " + L[m][n]);
        System.err.println("maxLen = " + maxLen);
        System.err.println("iMax = " + iMax);
        System.err.println("jMax = " + jMax);

	/* Trace back to find the sequence. */
	StringBuffer lcs = new StringBuffer(); // buffer to accumulate the reverse of the LCS.
	int i = iMax;
	int j = jMax;

        while ((i > 0) && (j > 0) && (s1.charAt(i-1) == s2.charAt(j-1))) {
          lcs.append(s1.charAt(i-1));
          --i;
          --j;
        }

	return lcs.reverse().toString();
    }

  public static void main(String[] args) {
    System.out.println(wagnerFischer(args[0], args[1]));
  }

}
