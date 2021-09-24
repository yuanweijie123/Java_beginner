package enigma;

import org.junit.Test;

import static enigma.TestUtils.NAVALA;
import static org.junit.Assert.assertEquals;

/** The suite of all JUnit tests for the rotor class.
 *  @author Weijie Yuan
 */
public class RotorTest {

    @Test
    public void checkConvertForward() {
        Alphabet alphabet = new Alphabet("ABCDEFGHIJKLMNOPQRSTUVWXYZ");
        Permutation perm = new Permutation(NAVALA.get("III"), alphabet);
        Rotor rotorTest = new Rotor("III", perm);
        rotorTest.set('X', 'A');
        assertEquals(rotorTest.convertForward(alphabet.toInt('V')),
                alphabet.toInt('J'));

        Permutation perm2 = new Permutation(NAVALA.get("IV"), alphabet);
        Rotor rotorTest2 = new Rotor("IV", perm2);
        rotorTest2.set('L', 'A');
        assertEquals(rotorTest2.convertForward(alphabet.toInt('I')),
                alphabet.toInt('V'));
    }

    @Test
    public void checkConvertBackward() {
        Alphabet alphabet = new Alphabet("ABCDEFGHIJKLMNOPQRSTUVWXYZ");
        Permutation perm = new Permutation(NAVALA.get("Beta"), alphabet);
        Rotor rotorTest = new Rotor("Beta", perm);
        rotorTest.set('A', 'A');
        assertEquals(rotorTest.convertBackward(alphabet.toInt('H')),
                alphabet.toInt('X'));

        Permutation perm2 = new Permutation(NAVALA.get("I"), alphabet);
        Rotor rotorTest2 = new Rotor("IV", perm2);
        rotorTest2.set('F', 'A');
        assertEquals(rotorTest2.convertBackward(alphabet.toInt('J')),
                alphabet.toInt('H'));
    }
}
