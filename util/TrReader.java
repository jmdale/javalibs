package util;

import java.io.IOException;
import java.io.Reader;

/** 
 * Translating Reader: a reader that translates, character by
 * character, an existing reader. This class was originally written as
 * a partial solution to a homework problem in CS 61B at UC Berkeley,
 * taught by Prof. Paul Hilfinger.
 *
 * @author <a href="mailto:jmdale@gmail.com">Joseph Dale</a>
 * @version 20130804
 */
public class TrReader extends Reader {

    private Reader str;
    private String from;
    private String to;

    /** 
     * Creates a new TrReader that produces the stream of characters
     * produced by STR, converting all characters that occur in FROM
     * to the corresponding characters in TO. That is, change
     * occurrences of FROM.charAt(0) to TO.charAt(0), etc., leaving
     * other characters unchanged.
     */
    public TrReader(Reader str, String from, String to) { 
	this.str = str;
	this.from = from;
	this.to = to;
    }

    /**
     * Close the underlying stream.
     */
    public void close() throws IOException {
	str.close();
    }

    /**
     * Set a mark in the underlying stream.
     *
     * @param readAheadLimit The minimum number of characters which must be able to be read before this mark becomes invalid.
     */
    public void mark(int readAheadLimit) throws IOException {
	str.mark(readAheadLimit);
    }

    /**
     * @return true if the underlying stream supports the mark operation, false otherwise.
     */
    public boolean markSupported() {
	return str.markSupported();
    }

    /**
     * Read and translate a single character from the underlying
     * stream.
     */
    public int read() throws IOException {
	int c = str.read();
	if (c == -1) {
	    return c;
	} else {
	    return translate((char) c);
	}
    }

    /**
     * Read and translate characters from the underlying stream, and
     * store them into an array.
     *
     * @param cbuf The array into which characters will be read.
     *
     * @return The number of characters read.
     */
    public int read(char[] cbuf) throws IOException {
	int n = str.read(cbuf);
	for (int i = 0; i < n; ++i) {
	    cbuf[i] = translate(cbuf[i]);
	}
	return n;
    }

    /**
     * Read and translate len characters from the underlying stream,
     * and store them into an array starting at position off.
     *
     * @param cbuf The array into which characters will be read.
     * @param off The index into cbuf at which the first character should be copied.
     * @param len The number of characters to be read.
     *
     * @return The number of characters read.
     */
    public int read(char[] cbuf, int off, int len) throws IOException {
	int n = str.read(cbuf, off, len);
	for (int i = off; i < n; ++i) {
	    cbuf[i] = translate(cbuf[i]);
	}
	return n;
    }

    /**
     * @return true if the underlying stream is ready to be read from, false otherwise.
     */
    public boolean ready() throws IOException {
	return str.ready();
    }

    /**
     * Reset the underlying stream to a previous mark.
     */
    public void reset() throws IOException {
	str.reset();
    }

    /**
     * Skip n characters in the underlying stream.
     *
     * @param n The number of characters to be skipped.
     * @return The number of characters skipped.
     */
    public long skip(long n) throws IOException {
	return str.skip(n);
    }

    /**
     * Translates a character.
     *
     * @param c The character to be translated.
     * @return The translated character.
     */
    private char translate(char c) {
        int n = from.length();
	for (int i = 0; i < n; ++i) {
	    if (c == from.charAt(i)) {
		return to.charAt(i);
	    }
	}
	return c;
    }
    
}
