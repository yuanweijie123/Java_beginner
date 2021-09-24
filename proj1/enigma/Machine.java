package enigma;

import java.util.Collection;

import static enigma.EnigmaException.*;

/** Class that represents a complete enigma machine.
 *  @author Weijie Yuan
 */
class Machine {

    /** A new Enigma machine with alphabet ALPHA, 1 < NUMROTORS rotor slots,
     *  and 0 <= PAWLS < NUMROTORS pawls.  ALLROTORS contains all the
     *  available rotors. */
    Machine(Alphabet alpha, int numRotors, int pawls,
            Collection<Rotor> allRotors) {
        _alphabet = alpha;
        _nRotors = numRotors;
        _nPawls = pawls;
        _allRotors = allRotors;
        _rotorSlot = new Rotor[numRotors];
    }

    /** Return the number of rotor slots I have. */
    int numRotors() {
        return _nRotors;
    }

    /** Return the number pawls (and thus rotating rotors) I have. */
    int numPawls() {
        return _nPawls;
    }

    /** Return the list for rotor slots. */
    Rotor[] rotorSlots() {
        return _rotorSlot;
    }

    /** Set my rotor slots to the rotors named ROTORS from my set of
     *  available rotors (ROTORS[0] names the reflector).
     *  Initially, all rotors are set at their 0 setting. */
    void insertRotors(String[] rotors) {
        if (rotors.length != numRotors()) {
            throw new EnigmaException("Incorrect number of input rotors");
        }
        int movingRotors = 0;
        for (int i = 0; i < numRotors(); i++) {
            boolean found = false;
            for (Rotor rotor : _allRotors) {
                if (rotors[i].equals(rotor.name())) {
                    _rotorSlot[i] = rotor;
                    if (_rotorSlot[i].rotates()) {
                        movingRotors += 1;
                    }
                    found = true;
                    break;
                }
            }
            if (!found) {
                throw new EnigmaException("Misnamed rotors");
            }
        }
        if (movingRotors != numPawls()) {
            throw new EnigmaException("Wrong number of pawls");
        }
    }

    /** Set my rotors according to SETTING and RINGSETTING, which must be
     * a string of numRotors()-1 characters in my alphabet. The first letter
     * refers to the leftmost rotor setting (not counting the reflector).  */
    void setRotors(String setting, String ringSetting) {
        if (setting.length() != numRotors() - 1) {
            throw new EnigmaException("Initial setting string is of "
                    + "wrong length");
        }
        for (int i = 0; i < numRotors() - 1; i++) {
            if (!_alphabet.contains(setting.charAt(i))) {
                throw new EnigmaException("Setting chars are not in "
                        + "the alphabet");
            }
            _rotorSlot[i + 1].set(setting.charAt(i), ringSetting.charAt(i));
        }
    }

    /** Set the plugboard to PLUGBOARD. */
    void setPlugboard(Permutation plugboard) {
        _plugBoard = plugboard;
    }

    /** Returns the result of converting the input character C (as an
     *  index in the range 0..alphabet size - 1), after first advancing
     *  the machine. */
    int convert(int c) {
        boolean[] checkAtNotch = new boolean[numRotors() - 1];
        boolean[] checkAdvance = new boolean[numRotors() - 1];
        for (int i = 0; i < numRotors() - 1; i++) {
            checkAtNotch[i] = _rotorSlot[i + 1].atNotch();
        }
        for (int i = 1; i < numRotors() - 1; i++) {
            if (checkAtNotch[i] && _rotorSlot[i].rotates()
                    && !checkAdvance[i - 1]) {
                _rotorSlot[i].advance();
                checkAdvance[i - 1] = true;
                if (!checkAdvance[i] && i < numRotors() - 2) {
                    _rotorSlot[i + 1].advance();
                    checkAdvance[i] = true;
                }
            }
        }
        _rotorSlot[numRotors() - 1].advance();
        checkAdvance[numRotors() - 2] = true;

        int forwardC = _plugBoard.permute(c);
        for (int in = numRotors() - 1; in >= 0; in -= 1) {
            forwardC = _rotorSlot[in].convertForward(forwardC);
        }
        int backwardC = forwardC;
        for (int out = 1; out < numRotors(); out += 1) {
            backwardC = _rotorSlot[out].convertBackward(backwardC);
        }
        int outC = _plugBoard.permute(backwardC);
        return outC;
    }

    /** Returns the encoding/decoding of MSG, updating the state of
     *  the rotors accordingly. */
    String convert(String msg) {
        String[] msgList = msg.replaceAll(" ", "").split("");
        int[] intInList = new int[msgList.length];
        char[] charOutList = new char[msgList.length];
        for (int i = 0; i < msgList.length; i += 1) {
            intInList[i] = _alphabet.toInt(msgList[i].charAt(0));
            charOutList[i] = _alphabet.toChar(convert(intInList[i]));
        }
        String msgOutput = "";
        for (int k = 0; k < charOutList.length; k += 1) {
            msgOutput += String.valueOf(charOutList[k]);
        }
        return msgOutput;
    }

    /** Common alphabet of my rotors. */
    private final Alphabet _alphabet;

    /** The number of total rotors. */
    private int _nRotors;

    /** The number of total pawls. */
    private int _nPawls;

    /** The collection of all the available rotors. */
    private Collection<Rotor> _allRotors;

    /** The field for plugBoard permutation. */
    private Permutation _plugBoard;

    /** The public field for rotor slots. */
    private Rotor[] _rotorSlot;

}
