package enigma;

import java.io.File;
import java.io.IOException;
import java.io.PrintStream;

import java.util.ArrayList;
import java.util.NoSuchElementException;
import java.util.Scanner;

import static enigma.EnigmaException.*;

/** Enigma simulator.
 *  @author Weijie Yuan
 */
public final class Main {

    /** Process a sequence of encryptions and decryptions, as
     *  specified by ARGS, where 1 <= ARGS.length <= 3.
     *  ARGS[0] is the name of a configuration file.
     *  ARGS[1] is optional; when present, it names an input file
     *  containing messages.  Otherwise, input comes from the standard
     *  input.  ARGS[2] is optional; when present, it names an output
     *  file for processed messages.  Otherwise, output goes to the
     *  standard output. Exits normally if there are no errors in the input;
     *  otherwise with code 1. */
    public static void main(String... args) {
        try {
            new Main(args).process();
            return;
        } catch (EnigmaException excp) {
            System.err.printf("Error: %s%n", excp.getMessage());
        }
        System.exit(1);
    }

    /** Check ARGS and open the necessary files (see comment on main). */
    Main(String[] args) {
        if (args.length < 1 || args.length > 3) {
            throw error("Only 1, 2, or 3 command-line arguments allowed");
        }

        _config = getInput(args[0]);

        if (args.length > 1) {
            _input = getInput(args[1]);
        } else {
            _input = new Scanner(System.in);
        }

        if (args.length > 2) {
            _output = getOutput(args[2]);
        } else {
            _output = System.out;
        }
    }

    /** Return a Scanner reading from the file named NAME. */
    private Scanner getInput(String name) {
        try {
            return new Scanner(new File(name));
        } catch (IOException excp) {
            throw error("could not open %s", name);
        }
    }

    /** Return a PrintStream writing to the file named NAME. */
    private PrintStream getOutput(String name) {
        try {
            return new PrintStream(new File(name));
        } catch (IOException excp) {
            throw error("could not open %s", name);
        }
    }

    /** Configure an Enigma machine from the contents of configuration
     *  file _config and apply it to the messages in _input, sending the
     *  results to _output. */
    private void process() {
        Machine enigma = readConfig();
        String nextLine = _input.nextLine();
        while (nextLine.replaceAll(" ", "").isEmpty()) {
            _output.println();
            nextLine = _input.nextLine();
        }
        if (!nextLine.substring(0, 1).equals("*")) {
            throw new EnigmaException("No config found");
        }
        while (_input.hasNext()) {
            String setting = nextLine;
            setUp(enigma, setting);
            nextLine = (_input.nextLine());
            while (nextLine.isEmpty()) {
                _output.println();
                nextLine = (_input.nextLine());
            }
            while (!(nextLine.contains("*"))) {
                if (nextLine.isEmpty()) {
                    _output.println();
                } else {
                    String msgResult = enigma.convert(
                            nextLine.replaceAll(" ", "")
                    );
                    printMessageLine(msgResult);
                }
                if (_input.hasNextLine()) {
                    nextLine = (_input.nextLine());
                } else {
                    break;
                }
            }
        }
    }

    /** Return an Enigma machine configured from the contents of configuration
     *  file _config. */
    private Machine readConfig() {
        try {
            String alphabet = _config.next();
            if (alphabet.contains("(") || alphabet.contains(")")
                    || alphabet.contains("*")) {
                throw new EnigmaException("Wrong alphabet format");
            }
            _alphabet = new Alphabet(alphabet);
            if (!_config.hasNextInt()) {
                throw new EnigmaException("Wrong config format on "
                        + "number of rotors");
            }
            _nRotors = _config.nextInt();
            if (!_config.hasNextInt()) {
                throw new EnigmaException("Wrong config format on number "
                        + "of pawls");
            }
            _nPawls = _config.nextInt();
            tempPerm = _config.next();
            while (_config.hasNext()) {
                _allRotors.add(readRotor());
            }
            return new Machine(_alphabet, _nRotors, _nPawls, _allRotors);
        } catch (NoSuchElementException excp) {
            throw error("configuration file truncated");
        }
    }

    /** Return a rotor, reading its description from _config. */
    private Rotor readRotor() {
        try {
            String name = tempPerm;
            String notches = _config.next();
            String perm = "";
            tempPerm = _config.next();
            while (tempPerm.contains("(") && _config.hasNext()) {
                perm = perm.concat(tempPerm + " ");
                tempPerm = (_config.next());
            }
            if (!_config.hasNext()) {
                perm = perm.concat(tempPerm + " ");
            }

            if (notches.charAt(0) == 'M') {
                return new MovingRotor(
                        name,
                        new Permutation(perm, _alphabet),
                        notches.substring(1)
                );
            } else if (notches.charAt(0) == 'R') {
                return new Reflector(name, new Permutation(perm, _alphabet));
            } else {
                return new FixedRotor(name, new Permutation(perm, _alphabet));
            }
        } catch (NoSuchElementException excp) {
            throw error("bad rotor description");
        }
    }

    /** Adjust the initial position setting based on ring setting.
     * @param setting ring setting
     * @return new initial setting after adjustment. */
    private String adjustRing(String setting) {
        char[] newPosSetting = new char[setting.length()];
        for (int i =  0; i < _ringSetting.length(); i++) {
            int position = _alphabet.toInt(setting.charAt(i));
            int ring = _alphabet.toInt(_ringSetting.charAt(i));
            int newPos = position - ring;
            newPos = newPos % _alphabet.size();
            if (newPos < 0) {
                newPos += _alphabet.size();
            }
            newPosSetting[i] = _alphabet.toChar(newPos);
        }
        setting = new String(newPosSetting);
        return setting;
    }

    /** Set M according to the specification given on SETTINGS,
     *  which must have the format specified in the assignment. */
    private void setUp(Machine M, String settings) {
        String[] settingList = settings.split(" ");
        if (settingList.length < M.numRotors() + 2) {
            throw new EnigmaException("Not enough arguments in setting line");
        }
        String[] rotors = new String[M.numRotors()];
        for (int i = 1; i < M.numRotors() + 1; i++) {
            rotors[i - 1] = settingList[i];
        }
        String initialSetting = settingList[M.numRotors() + 1];
        if (initialSetting.length() != M.numRotors() - 1) {
            throw new EnigmaException("The initial setting string is of the "
                    + "wrong length");
        }
        for (int i = 0; i < initialSetting.length(); i++) {
            if (!_alphabet.contains(initialSetting.charAt(i))) {
                throw new EnigmaException("Initial positions string contain "
                        + "characters not in the alphabet.");
            }
        }
        for (int i = 0; i < rotors.length - 1; i++) {
            for (int j = i + 1; j < rotors.length; j++) {
                if (rotors[i].equals(rotors[j])) {
                    throw new EnigmaException("There exist repeated rotors");
                }
            }
        }
        char startPos = _alphabet.toChar(0);
        char[] ringSet = new char[M.numRotors() - 1];
        for (int i = 0; i < ringSet.length; i++) {
            ringSet[i] = startPos;
        }
        String allStartPos = new String(ringSet);
        _ringSetting = allStartPos;
        int plusPos = 2;
        if (settingList.length > M.numRotors() + 2
                && !settingList[M.numRotors() + 2].contains("(")) {
            _ringSetting = settingList[M.numRotors() + 2];
        }
        if (!_ringSetting.equals(allStartPos)) {
            initialSetting = adjustRing(initialSetting);
            plusPos = 3;
        }
        String steckerPair = "";
        for (int i = M.numRotors() + plusPos; i < settingList.length; i++) {
            steckerPair = steckerPair.concat(settingList[i] + " ");
        }
        M.insertRotors(rotors);
        if (!M.rotorSlots()[0].reflecting()) {
            throw new EnigmaException("Leftmost Rotor should be a reflector");
        }
        M.setRotors(initialSetting, _ringSetting);
        M.setPlugboard(new Permutation(steckerPair, _alphabet));
    }

    /** Print MSG in groups of five (except that the last group may
     *  have fewer letters). */
    private void printMessageLine(String msg) {
        for (int i = 0; i < msg.length(); i += 5) {
            int remain = msg.length() - i;
            if (remain <= 5) {
                _output.println(msg.substring(i, i + remain));
            } else {
                _output.print(msg.substring(i, i + 5) + " ");
            }
        }
    }

    /** Alphabet used in this machine. */
    private Alphabet _alphabet;

    /** Source of input messages. */
    private Scanner _input;

    /** Source of machine configuration. */
    private Scanner _config;

    /** File for encoded/decoded messages. */
    private PrintStream _output;

    /** Number of rotors. */
    private int _nRotors;

    /** Number of pawls. */
    private int _nPawls;

    /** Array list for all available rotors. */
    private ArrayList<Rotor> _allRotors = new ArrayList<>();

    /** temp variable for permutation. */
    private String tempPerm;

    /** String for ring setting. */
    private String _ringSetting;

}
