import java.io.Reader;
import java.io.IOException;

/** Translating Reader: a stream that is a translation of an
 *  existing reader.
 *  @author WEIJIE YUAN
 */
public class TrReader extends Reader {
    /** A new TrReader that produces the stream of characters produced
     *  by STR, converting all characters that occur in FROM to the
     *  corresponding characters in TO.  That is, change occurrences of
     *  FROM.charAt(i) to TO.charAt(i), for all i, leaving other characters
     *  in STR unchanged.  FROM and TO must have the same length. */
    private Reader _source;
    private String _from;
    private String _to;

    public TrReader(Reader str, String from, String to) {
        if (from == null) {
            throw new IllegalArgumentException("FROM should be non-null.");
        }
        if (to == null) {
            throw new IllegalArgumentException("TO should be non-null.");
        }
        if (from.length() != to.length()) {
            throw new IllegalArgumentException("The length of FROM should be the same as that of TO.");
        }

        this._source = str;
        this._from = from;
        this._to = to;
    }

    @Override
    public int read(char cbuf[], int off, int len) throws IOException {
        int NumberRead = this._source.read(cbuf, off, len);
        for (int i = off; i < off+len; i++) {
            cbuf[i] = this.translate(cbuf[i]);
        }
        return Math.min(len, NumberRead);
    }

    @Override
    public void close() throws IOException {
        this._source.close();
    }

    /**
     *
     * @param input reading-in char
     * @return the corresponding translated char in TO
     */
    public char translate(char input) {
        int currentIndex = _from.indexOf(input);
        if (currentIndex == -1) {
            return input;
        } else {
            return _to.charAt(currentIndex);
        }
    }
}
