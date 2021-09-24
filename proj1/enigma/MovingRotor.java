package enigma;

import static enigma.EnigmaException.*;

/** Class that represents a rotating rotor in the enigma machine.
 *  @author Weijie Yuan
 */
class MovingRotor extends Rotor {

    /** A rotor named NAME whose permutation in its default setting is
     *  PERM, and whose notches are at the positions indicated in NOTCHES.
     *  The Rotor is initally in its 0 setting (first character of its
     *  alphabet).
     */
    MovingRotor(String name, Permutation perm, String notches) {
        super(name, perm);
        _notches = notches;
    }

    /** Return my notches. */
    String notches() {
        return _notches;
    }

    /** Return true b/c MovingRotor can move. */
    @Override
    boolean rotates() {
        return true;
    }

    @Override
    boolean atNotch() {
        String[] notchList = notches().split("");
        int[] notchInt = new int[notchList.length];
        for (int i = 0; i < notchList.length; i++) {
            notchInt[i] =  alphabet().toInt(notchList[i].charAt(0));
        }
        for (int i = 0; i < notchList.length; i++) {
            if (super.permutation().wrap(this.setting() + ringSetting())
                    == notchInt[i]) {
                return true;
            }
        }
        return false;
    }

    @Override
    void advance() {
        super.set(super.permutation().wrap(super.setting() + 1), ringSetting());
    }

    /** Fields for notches.*/
    private String _notches;

}
