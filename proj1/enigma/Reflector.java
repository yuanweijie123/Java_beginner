package enigma;

import static enigma.EnigmaException.*;

/** Class that represents a reflector in the enigma.
 *  @author Weijie Yuan
 */
class Reflector extends FixedRotor {

    /** A non-moving rotor named NAME whose permutation at the 0 setting
     * is PERM. */
    Reflector(String name, Permutation perm) {
        super(name, perm);
    }

    /** Return true iff I reflect. */
    boolean reflecting() {
        return true;
    }

    @Override
    void set(int posn, int ringSetting) {
        if (posn != 0) {
            throw error("reflector has only one position");
        }
    }

}
