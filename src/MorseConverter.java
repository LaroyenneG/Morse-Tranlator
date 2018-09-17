import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

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

        if (!morseCode.containsKey(c)) {
            return String.valueOf(c);
        }

        char[] code = morseCode.get(c);

        StringBuilder stringCode = new StringBuilder();

        for (int i = 0; i < code.length; i++) {
            stringCode.append(code[i]);
        }

        return new String(stringCode);
    }

    public static double[] buildSignal(String text, double speed) {

        if (speed <= 0) {
            return new double[0];
        }

        final List<double[]> signals = Collections.synchronizedList(new ArrayList<double[]>());

        final int textLen = text.length();

        final Thread[] builderThreads = new Thread[textLen];

        for (int i = 0; i < textLen; i++) {

            final double finalSpeed = speed;
            final char c = text.charAt(i);

            builderThreads[i] = new Thread(() -> signals.add(buildValue(c, finalSpeed)));
            builderThreads[i].start();
        }

        joinAllThreads(builderThreads);


        int totalLen = 0;
        for (double[] segment : signals) {
            totalLen += segment.length;
        }


        int position = 0;

        double[] signal = new double[totalLen];

        final Thread[] fusionThreads = new Thread[signals.size()];

        for (int i = 0; i < signals.size(); i++) {

            final int index = i;
            final int p = position;

            fusionThreads[i] = new Thread(() -> System.arraycopy(signals.get(index), 0, signal, p, signals.get(index).length));
            fusionThreads[i].start();
            position += signals.get(i).length;
        }

        joinAllThreads(fusionThreads);

        return signal;
    }

    private static void joinAllThreads(Thread[] fusionThreads) {

        for (Thread thread : fusionThreads) {
            try {
                thread.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
                System.exit(-1);
            }
        }
    }

    private static double[] buildValue(char code, double speed) {


        final double FREQ = 550.0;
        final double AMP = 1.0;

        double[] signalValue;

        switch (code) {

            case SHORT_VALUE:
                signalValue = StdAudio.note(FREQ, 0.065 * speed, AMP);
                break;

            case LONG_VALUE:
                signalValue = StdAudio.note(FREQ, 0.180 * speed, AMP);
                break;

            case SPACE_VALUE:
                signalValue = StdAudio.note(FREQ, 0.065 * speed, 0.0);
                break;

            case SPACE_WORD_VALUE:
                signalValue = StdAudio.note(FREQ, 0.180 * speed, 0.0);
                break;

            default:
                signalValue = new double[0];
                break;
        }

        double[] whiteSignal = StdAudio.note(FREQ, 0.065 * speed, 0.0);

        double[] signal = new double[signalValue.length + whiteSignal.length];

        System.arraycopy(signalValue, 0, signal, 0, signalValue.length);

        System.arraycopy(whiteSignal, 0, signal, signalValue.length, whiteSignal.length);

        return signal;
    }

    public String encodeText(String text) {

        text = text.trim().toLowerCase();

        String[] words = text.split(" ");

        StringBuilder encodeLine = new StringBuilder();

        for (int i = 0; i < words.length; i++) {

            StringBuilder encodeWord = new StringBuilder();

            for (int j = 0; j < words[i].length(); j++) {
                encodeWord.append(encodeChar(words[i].charAt(j))).append(SPACE_VALUE);
            }

            encodeLine.append(encodeWord);

            if (i + 1 < words.length) {
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
                System.err.println("Invalid format file (line)");
                System.exit(-1);
            }


            char[] code = new char[words.length - 1];
            for (int i = 1; i < characters.length; i++) {

                if (characters[i] != LONG_VALUE && characters[i] != SHORT_VALUE) {
                    System.err.println("Invalid format file (value)");
                    System.exit(-1);
                }

                code[i - 1] = characters[i];
            }

            morseCode.put(characters[0], code);
        }
    }
}
