package enigma;

import static enigma.EnigmaException.*;

/** Represents a permutation of a range of integers starting at 0 corresponding
 *  to the characters of an alphabet.
 *  @author Weijie Yuan
 */
class Permutation {

    /** Set this Permutation to that specified by CYCLES, a string in the
     *  form "(cccc) (cc) ..." where the c's are characters in ALPHABET, which
     *  is interpreted as a permutation in cycle notation.  Characters in the
     *  alphabet that are not included in any cycle map to themselves.
     *  Whitespace is ignored. */
    Permutation(String cycles, Alphabet alphabet) {
        _alphabet = alphabet;
        _cycles = cycles;
    }

    /** Add the cycle c0->c1->...->cm->c0 to the permutation, where CYCLE is
     *  c0c1...cm. */
    private void addCycle(String cycle) {
        _cycles += cycle;
    }

    /** Return the value of P modulo the size of this permutation. */
    final int wrap(int p) {
        int r = p % size();
        if (r < 0) {
            r += size();
        }
        return r;
    }

    /** Returns the size of the alphabet I permute. */
    int size() {
        return _alphabet.size();
    }

    /** Return Split CYCLES. */
    static String[] splitCycles(String cycles) {
        if (cycles.contains(" ")) {
            cycles = cycles.replaceAll(" ", "");
            String[] splitRB = cycles.split("\\)\\(");
            splitRB[0] = splitRB[0].substring(1);
            int last = splitRB.length - 1;
            splitRB[last] = splitRB[last].substring(0,
                    splitRB[last].length() - 1);
            return splitRB;
        } else {
            cycles = cycles.replaceAll("\\(", "");
            cycles = cycles.replaceAll("\\)", "");
            String[] splitOrginial = {cycles};
            return splitOrginial;
        }
    }

    /** Return the result of applying this permutation to P modulo the
     *  alphabet size. */
    int permute(int p) {
        int inputInt = wrap(p);
        char inputChar = _alphabet.toChar(inputInt);
        char outputChar = permute(inputChar);
        return _alphabet.toInt(outputChar);
    }

    /** Return the result of applying the inverse of this permutation
     *  to C modulo the alphabet size. */
    int invert(int c) {
        int inputInt = wrap(c);
        char inputChar = _alphabet.toChar(inputInt);
        char outputChar = invert(inputChar);
        return _alphabet.toInt(outputChar);
    }

    /** Return the result of applying this permutation to the index of P
     *  in ALPHABET, and converting the result to a character of ALPHABET. */
    char permute(char p) {
        char permutedRes = p;
        if (_cycles.equals("")) {
            return permutedRes;
        } else {
            String[] allCycles = splitCycles(_cycles);
            for (int i = 0; i < allCycles.length; i++) {
                String curCycle = allCycles[i];
                int indP = curCycle.indexOf(String.valueOf(p));
                if (indP == -1) {
                    continue;
                } else if (indP == curCycle.length() - 1) {
                    permutedRes = curCycle.charAt(0);
                } else {
                    permutedRes = curCycle.charAt(indP + 1);
                    break;
                }
            }
            return permutedRes;
        }
    }

    /** Return the result of applying the inverse of this permutation to C. */
    char invert(char c) {
        char invertedRes = c;
        if (_cycles.equals("")) {
            return invertedRes;
        } else {
            String[] allCycles = splitCycles(_cycles);
            for (int i = 0; i < allCycles.length; i++) {
                String curCycle = allCycles[i];
                int indC = curCycle.indexOf(String.valueOf(c));
                if (indC == -1) {
                    continue;
                } else if (indC == 0) {
                    invertedRes = curCycle.charAt(curCycle.length() - 1);
                } else {
                    invertedRes = curCycle.charAt(indC - 1);
                    break;
                }
            }
            return invertedRes;
        }
    }

    /** Return the alphabet used to initialize this Permutation. */
    Alphabet alphabet() {
        return _alphabet;
    }

    /** Return true iff this permutation is a derangement (i.e., a
     *  permutation for which no value maps to itself). */
    boolean derangement() {
        String [] allCycles = splitCycles(_cycles);
        for (int i = 0; i < allCycles.length; i += 1) {
            if (allCycles[i].length() == 1) {
                return false;
            }
        }
        return true;
    }

    /** Alphabet of this permutation. */
    private Alphabet _alphabet;

    /** A string in the form "(cccc) (cc) ...". */
    private String _cycles;

}
