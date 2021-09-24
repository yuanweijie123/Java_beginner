package enigma;

import org.junit.Test;

import static enigma.TestUtils.NAVALA_MAP;
import static org.junit.Assert.assertEquals;

/** The suite of all JUnit tests for the Alphabet class.
 *  @author Weijie Yuan
 */
public class AlphabetTest {

    @Test
    public void checkToInt() {
        Alphabet alphabet = new Alphabet();
        assertEquals(alphabet.toInt('V'), 21);
        Alphabet alphabet1 = new Alphabet(NAVALA_MAP.get("II"));
        assertEquals(alphabet1.toInt('J'), 1);
    }

    @Test
    public void checkToChar() {
        Alphabet alphabet = new Alphabet();
        assertEquals(alphabet.toChar(8), 'I');
        Alphabet alphabet1 = new Alphabet(NAVALA_MAP.get("IV"));
        assertEquals(alphabet1.toChar(10), 'U');
    }

}
