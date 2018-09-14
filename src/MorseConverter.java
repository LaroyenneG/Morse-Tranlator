import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MorseConverter {

    private static final char LONG_VALUE = '-';
    private static final char SHORT_VALUE = '.';
    private static final char SPACE_VALUE = ' ';
    private static final char SPACE_WORD_VALUE = '/';


    private static MorseConverter ourInstance = new MorseConverter();

    public static MorseConverter getInstance() {
        return ourInstance;
    }


    private Map<Character, char[]> morseCode;


    private MorseConverter() {
        morseCode = new HashMap<>();
    }


    private String encodeChar(char c) {

        char[] code = morseCode.get(c);

        StringBuilder stringCode = new StringBuilder();

        for (int i = 0; i < code.length; i++) {
            stringCode.append(code[i]);
        }

        return new String(stringCode);
    }

    public String encodeText(String text) {

        text = text.trim().toLowerCase();

        String[] words = text.split(" ");

        StringBuilder encodeLine = new StringBuilder();

        for (int i=0; i<words.length; i++) {
            StringBuilder encodeWord = new StringBuilder();

            for (int j = 0; j < words[i].length(); j++) {
                encodeWord.append(encodeChar(words[i].charAt(j))).append(SPACE_VALUE);
            }

            encodeLine.append(encodeWord);

            if(i+1<words.length) {
                encodeLine.append(SPACE_WORD_VALUE);
            }
        }

        return new String(encodeLine);
    }


    public void loadEncodeTable() throws IOException {

        FileReader fileReader = new FileReader("morse_code.txt");
        BufferedReader reader = new BufferedReader(fileReader);

        String line;
        while ((line = reader.readLine()) != null) {

            line = line.trim().toLowerCase();

            String[] words = line.split(" ");
            Character[] characters = new Character[words.length];

            for (int i = 0; i < words.length; i++) {
                if (words[i].length() > 1) {
                    System.err.println("Invalid format file");
                    System.exit(-1);
                }
                characters[i] = words[i].charAt(0);
            }

            if (characters.length < 2) {
                System.out.println("Invalid format file (line)");
                System.exit(-1);
            }


            char[] code = new char[words.length - 1];
            for (int i = 1; i < characters.length; i++) {

                if (characters[i] != LONG_VALUE && characters[i] != SHORT_VALUE) {
                    System.out.println("Invalid format file (value)");
                    System.exit(-1);
                }

                code[i - 1] = characters[i];
            }

            morseCode.put(characters[0], code);
        }
    }


    public double[] buildSignal(String text) {

        List<double[]> signals = new ArrayList<double[]>();

        for (int i = 0; i < text.length(); i++) {
            signals.add(buildValue(text.charAt(i)));
        }

        int len = 0;
        for (double[] s : signals) {
            len += s.length;
        }

        int position = 0;
        double[] signal = new double[len];

        for (double[] s : signals) {
            System.arraycopy(s, 0, signal, position, s.length);
            position += s.length;
        }

        return signal;
    }


    private double[] buildValue(char code) {


        final double FREQ = 550.0;
        final double AMP = 1.0;
        final double TIME = 10.0;

        double[] signalValue;

        switch (code) {

            case SHORT_VALUE:
                signalValue = StdAudio.note(FREQ, 0.065 * TIME, AMP);
                break;

            case LONG_VALUE:
                signalValue = StdAudio.note(FREQ, 0.180 * TIME, AMP);
                break;

            case SPACE_VALUE:
                signalValue = StdAudio.note(FREQ, 0.065 * TIME, 0.0);
                break;

            case SPACE_WORD_VALUE:
                signalValue = StdAudio.note(FREQ, 0.180 * TIME, 0.0);
                break;

            default:
                signalValue = new double[0];
                break;
        }

        double[] whiteSignal = StdAudio.note(FREQ, 0.065 * TIME, 0.0);

        double[] signal = new double[signalValue.length + whiteSignal.length];

        System.arraycopy(signalValue, 0, signal, 0, signalValue.length);
        System.arraycopy(whiteSignal, 0, signal, signalValue.length, whiteSignal.length);

        return signal;
    }
}
