package util;

import util.fn.CharToBoolean;

import java.security.MessageDigest;
import java.util.StringTokenizer;

/**
 * Static methods for working with Strings.
 *
 * @author <a href="mailto:jmdale@gmail.com">Joseph Dale</a>
 * @version 20110824
 */
public class Strings {

  public static String[] tokenize(String str, String delim) {
    return getTokens(new StringTokenizer(str, delim));
  }

  public static String[] tokenize(String str) {
    return getTokens(new StringTokenizer(str));
  }

  public static String[] getTokens(StringTokenizer st) {
    String[] tokens = new String[st.countTokens()];
    for (int i = 0; i < tokens.length; i += 1) {
      tokens[i] = st.nextToken();
    }
    return tokens;
  }

  public static String reverse(String s) {
    return (new StringBuffer(s)).reverse().toString();
  }

  /**
   * Removes from s characters which occur in r.
   */
  public static String filter(String s, final String r) {
    CharToBoolean inR = new CharToBoolean() {
      public boolean apply(char c) {
        return r.indexOf(c) != -1;
      }
    };
    return inR.remove(s);
  }

  /**
   * Given a String, returns a new String in which every single
   * quote character has been replaced by two single quote
   * characters. This function is often used in constructing SQL
   * statements.
   */
  public static String escapeApostrophes(String s) {
    if (s == null) {
      return null;
    }

    StringBuffer sb = new StringBuffer();
    for (int i = 0; i < s.length(); i += 1) {
      char c = s.charAt(i);
      if (c == '\'') {
        sb.append("''");
      } else {
        sb.append(c);
      }
    }
	
    return sb.toString();
  }

  /**
   * Returns the longest common subsequence of two strings.
   */
  public static String longestCommonSubsequence(String s1, String s2) {
    return util.str.LongestCommonSubsequence.wagnerFischer(s1, s2);
  }

  public static String join(int[] x, String sep) {
    int n = x.length;
    if (n == 0) {
      return "";
    } else if (n == 1) {
      return "" + x[0];
    } else {
      StringBuffer sb = new StringBuffer("" + x[0]);
      for (int i = 1; i < n; i++) {
        sb.append(sep);
        sb.append(x[i]);
      }
      return sb.toString();
    }
  }

  public static String join(int[] x) {
    return join(x, "\t");
  }

  public static String join(double[] x, String sep) {
    int n = x.length;
    if (n == 0) {
      return "";
    } else if (n == 1) {
      return "" + x[0];
    } else {
      StringBuffer sb = new StringBuffer("" + x[0]);
      for (int i = 1; i < n; i++) {
        sb.append(sep);
        sb.append(x[i]);
      }
      return sb.toString();
    }
  }

  public static String join(double[] x) {
    return join(x, "\t");
  }

  public static String join(String[] x, String sep) {
    int n = x.length;
    if (n == 0) {
      return "";
    } else if (n == 1) {
      return "" + x[0];
    } else {
      StringBuffer sb = new StringBuffer("" + x[0]);
      for (int i = 1; i < n; i++) {
        sb.append(sep);
        sb.append(x[i]);
      }
      return sb.toString();
    }
  }

  public static String join(String[] x) {
    return join(x, "\t");
  }

  public static String toString(int[] x) {
    StringBuilder sb = new StringBuilder("[");
    sb.append(join(x, ", "));
    sb.append("]");
    return sb.toString();
  }

  public static String toString(double[] x) {
    StringBuilder sb = new StringBuilder("[");
    sb.append(join(x, ", "));
    sb.append("]");
    return sb.toString();
  }

  private static final char[] HEX = new char[]{'0', '1', '2', '3', '4', '5', '6', '7',
                                               '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};

  /**
   * Converts a String to an array of bytes, computes the MD5 hash
   * of the bytes, and returns the hash as a hexadecimal string.
   */
  public static String digest(String s) {
    MessageDigest md = null;
    try {
      md = MessageDigest.getInstance("MD5");
    } catch (java.security.NoSuchAlgorithmException nsae) {
      /*
       * Since I've hardcoded the algorithm name "MD5", this
       * shouldn't happen. If it does, we'll just get a
       * NullPointerException when we call digest() below.
       */
    }

    byte[] digest = md.digest(s.getBytes());
    StringBuilder sb = new StringBuilder();

    for (int i = 0; i < digest.length; i++) {
      byte b = digest[i];
      sb.append(HEX[(b & 0xF0) >>> 4]);
      sb.append(HEX[b & 0xF]);
    }

    return sb.toString();
  }

  /**
   * Returns a string containing the alphabetic characters in s,
   * converted to upper case.
   */
  public static String toUpperAlpha(String s) {
    StringBuilder sb = new StringBuilder();
    for (int i = 0; i < s.length(); i++) {
      char c = s.charAt(i);
      if (Character.isLetter(c)) {
        sb.append(Character.toUpperCase(c));
      }
    }
    return sb.toString();
  }

  public static void main(String[] args) {
    System.out.println("Computing the MD5 hash of \"" + args[0] + "\":");
    System.out.println(digest(args[0]));
  }
}
