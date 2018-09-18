import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class MorseConverter {

    private static final int CPU_NUMBER = Runtime.getRuntime().availableProcessors() > 1 ? Runtime.getRuntime().availableProcessors() : 2;

    private static final String MORSE_FILE_NAME = "morse_code.txt";

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

        final List<Thread> builderThreads = new LinkedList<>();

        for (int i = 0; i < textLen; i++) {

            final double FINAL_SPEED = speed;
            final char C = text.charAt(i);

            Thread thread = new Thread(() -> signals.add(buildValue(C, FINAL_SPEED)));
            thread.start();

            builderThreads.add(thread);

            if (i + 1 % CPU_NUMBER == 0) {
                joinAllThreads(builderThreads);
            }
        }

        joinAllThreads(builderThreads);

        int totalLen = 0;
        for (double[] segment : signals) {
            totalLen += segment.length;
        }


        int position = 0;

        double[] signal = new double[totalLen];

        final List<Thread> fusionThreads = new LinkedList<>();

        for (int i = 0; i < signals.size(); i++) {

            final int INDEX = i;
            final int P = position;

            Thread thread = new Thread(() -> System.arraycopy(signals.get(INDEX), 0, signal, P, signals.get(INDEX).length));
            thread.start();

            fusionThreads.add(thread);

            if (i + 1 % CPU_NUMBER == 0) {
                joinAllThreads(fusionThreads);
            }

            position += signals.get(i).length;
        }

        joinAllThreads(fusionThreads);

        return signal;
    }

    private static void joinAllThreads(List<Thread> fusionThreads) {

        for (Thread thread : fusionThreads) {
            try {
                thread.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
                System.exit(-1);
            }
        }

        fusionThreads.clear();
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
                encodeWord.append(encodeChar(words[i].charAt(j)));

                if (j + 1 < words[i].length() || i + 1 < words.length) {
                    encodeWord.append(SPACE_VALUE);
                }

            }

            encodeLine.append(encodeWord);

            if (i + 1 < words.length) {
                encodeLine.append(SPACE_WORD_VALUE).append(SPACE_VALUE);
            }
        }


        return new String(encodeLine);
    }

    public void loadTranslationLine(String line) throws MorseCodeTableException {

        line = line.trim().toLowerCase();

        String[] words = line.split(" ");
        Character[] characters = new Character[words.length];

        for (int i = 0; i < words.length; i++) {
            if (words[i].length() > 1) {
                throw new MorseCodeTableException("Invalid format file");
            }
            characters[i] = words[i].charAt(0);
        }

        if (characters.length < 2) {
            throw new MorseCodeTableException("Invalid format file (line)");
        }


        char[] code = new char[words.length - 1];
        for (int i = 1; i < characters.length; i++) {

            if (characters[i] != LONG_VALUE && characters[i] != SHORT_VALUE) {
                throw new MorseCodeTableException("Invalid format file (value)");
            }

            code[i - 1] = characters[i];
        }

        morseCode.put(characters[0], code);
    }

    public void loadMorseCodeFile() throws IOException, MorseCodeTableException {

        FileReader fileReader = new FileReader(MORSE_FILE_NAME);
        BufferedReader reader = new BufferedReader(fileReader);

        String line;
        while ((line = reader.readLine()) != null) {
            loadTranslationLine(line);
        }

        fileReader.close();
    }
}
