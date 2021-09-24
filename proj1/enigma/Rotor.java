package enigma;

import static enigma.EnigmaException.*;

/** Superclass that represents a rotor in the enigma machine.
 *  @author Weijie Yuan
 */
class Rotor {

    /** A rotor named NAME whose permutation is given by PERM. */
    Rotor(String name, Permutation perm) {
        _name = name;
        _permutation = perm;
        _setting = 0;
        _ringSetting = 0;
    }

    /** Return my name. */
    String name() {
        return _name;
    }

    /** Return my alphabet. */
    Alphabet alphabet() {
        return _permutation.alphabet();
    }

    /** Return my permutation. */
    Permutation permutation() {
        return _permutation;
    }

    /** Return the size of my alphabet. */
    int size() {
        return _permutation.size();
    }

    /** Return true iff I have a ratchet and can move. */
    boolean rotates() {
        return false;
    }

    /** Return true iff I reflect. */
    boolean reflecting() {
        return false;
    }

    /** Return my current setting. */
    int setting() {
        return _setting;
    }

    /** Return my ring setting. */
    int ringSetting() {
        return _ringSetting;
    }

    /** Set setting() to POSN
     * Set ringSetting() to RING.  */
    void set(int posn, int ring) {
        _setting = posn;
        _ringSetting = ring;
    }

    /** Set setting() to character CPOSN.
     * Set ringSetting() to character CRING */
    void set(char cposn, char cring) {
        _setting = alphabet().toInt(cposn);
        _ringSetting = alphabet().toInt(cring);
    }

    /** Return the conversion of P (an integer in the range 0..size()-1)
     *  according to my permutation. */
    int convertForward(int p) {
        int inSetting = p + setting();
        inSetting = inSetting % size();
        int permuted = _permutation.permute(inSetting);
        return _permutation.wrap(permuted - setting());
    }

    /** Return the conversion of E (an integer in the range 0..size()-1)
     *  according to the inverse of my permutation. */
    int convertBackward(int e) {
        int outSetting = e + setting();
        outSetting = outSetting % size();
        int inverted = _permutation.invert(outSetting);
        return _permutation.wrap(inverted - setting());
    }

    /** Returns true iff I am positioned to allow the rotor to my left
     *  to advance. */
    boolean atNotch() {
        return false;
    }

    /** Advance me one position, if possible. By default, does nothing. */
    void advance() {
    }

    @Override
    public String toString() {
        return "Rotor " + _name;
    }

    /** My name. */
    private final String _name;

    /** The permutation implemented by this rotor in its 0 position. */
    private Permutation _permutation;

    /** The setting number for rotor. */
    private int _setting;

    /** The ring setting number for rotor. */
    private int _ringSetting;

}
