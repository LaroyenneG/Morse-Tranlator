import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MorseConverter {

    public static final char LONG_VALUE = '-';
    public static final char SHORT_VALUE = '.';
    public static final char SPACE_VALUE = '/';


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
            if (i + 1 < code.length) {
                stringCode.append(" ");
            }
        }

        return new String(stringCode);
    }

    public String encodeLine(String line) {

        line = line.trim().toLowerCase();

        String[] words = line.split(" ");

        StringBuilder encodeLine = new StringBuilder();

        for (String word : words) {
            StringBuilder encodeWord = new StringBuilder();
            for (int i = 0; i < word.length(); i++) {
                encodeWord.append(encodeChar(word.charAt(i)));
            }
            encodeLine.append(encodeWord).append(SPACE_VALUE);
        }

        return new String(encodeLine);
    }


    public void readFile() throws IOException {

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


    public void playString(String text) {

        List<double[]> signals = new ArrayList<double[]>();

        for (int i = 0; i < text.length(); i++) {
            signals.add(playValue(text.charAt(i)));
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


        try {
            FileWriter writer = new FileWriter("truc.csv");
            for (double n : signal) {
                writer.write(String.valueOf(n) + "\n\r");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }


        StdAudio.play(signal);
    }

    private double[] playValue(char code) {

        final double FREQ = 550.0;
        final double AMP = 1.0;

        double[] signalValue;

        switch (code) {

            case SHORT_VALUE:
                signalValue = StdAudio.note(FREQ, 0.065, AMP);
                break;

            case LONG_VALUE:
                signalValue = StdAudio.note(FREQ, 0.150, AMP);
                break;

            case SPACE_VALUE:
                signalValue = StdAudio.note(0.0, 0.150, 0.0);
                break;

            default:
                signalValue = new double[0];
                break;
        }

        double[] whiteSignal = StdAudio.note(0.0, 0.065, 0.0);

        double[] signal = new double[signalValue.length + whiteSignal.length];

        System.arraycopy(signalValue, 0, signal, 0, signalValue.length);
        System.arraycopy(whiteSignal, 0, signal, signalValue.length, whiteSignal.length);

        return signal;
    }
}
