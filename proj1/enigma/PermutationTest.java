package enigma;

import org.junit.Test;
import org.junit.Rule;
import org.junit.rules.Timeout;

import static org.junit.Assert.*;

import static enigma.TestUtils.*;

/** The suite of all JUnit tests for the Permutation class.
 *  @author
 */
public class PermutationTest {

    /** Testing time limit. */
    @Rule
    public Timeout globalTimeout = Timeout.seconds(5);

    /* ***** TESTING UTILITIES ***** */

    private Permutation perm;
    private String alpha = UPPER_STRING;

    /** Check that perm has an alphabet whose size is that of
     *  FROMALPHA and TOALPHA and that maps each character of
     *  FROMALPHA to the corresponding character of FROMALPHA, and
     *  vice-versa. TESTID is used in error messages. */
    private void checkPerm(String testId,
                           String fromAlpha, String toAlpha) {
        int N = fromAlpha.length();
        assertEquals(testId + " (wrong length)", N, perm.size());
        for (int i = 0; i < N; i += 1) {
            char c = fromAlpha.charAt(i), e = toAlpha.charAt(i);
            assertEquals(msg(testId, "wrong translation of '%c'", c),
                         e, perm.permute(c));
            assertEquals(msg(testId, "wrong inverse of '%c'", e),
                         c, perm.invert(e));
            int ci = alpha.indexOf(c), ei = alpha.indexOf(e);
            assertEquals(msg(testId, "wrong translation of %d", ci),
                         ei, perm.permute(ci));
            assertEquals(msg(testId, "wrong inverse of %d", ei),
                         ci, perm.invert(ei));
        }
    }

    /* ***** TESTS ***** */

    @Test
    public void checkIdTransform() {
        perm = new Permutation("", UPPER);
        checkPerm("identity", UPPER_STRING, UPPER_STRING);
    }

    @Test
    public void checkPermute() {
        Alphabet alphabet1 = new Alphabet(NAVALA_MAP.get("I"));
        Permutation perm1 = new Permutation(NAVALA.get("I"), alphabet1);
        assertEquals(perm1.permute(alphabet1.toInt('U')), alphabet1.toInt('A'));
        assertEquals(perm1.permute(alphabet1.toInt('S')), alphabet1.toInt('S'));
        assertEquals(perm1.permute(alphabet1.toInt('I')), alphabet1.toInt('V'));
        Permutation perm2 = new Permutation("(AF)", alphabet1);
        assertEquals(perm2.permute(alphabet1.toInt('S')), alphabet1.toInt('S'));
    }

    @Test
    public void checkInvert() {
        Alphabet alphabet2 = new Alphabet(NAVALA_MAP.get("II"));
        Permutation perm2 = new Permutation(NAVALA.get("II"), alphabet2);
        assertEquals(perm2.invert(alphabet2.toInt('F')), alphabet2.toInt('W'));
        assertEquals(perm2.invert(alphabet2.toInt('J')), alphabet2.toInt('B'));
        assertEquals(perm2.permute(alphabet2.toInt('A')), alphabet2.toInt('A'));
    }

}
