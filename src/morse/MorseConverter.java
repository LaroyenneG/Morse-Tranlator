package morse;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public abstract class MorseConverter {

    private static final int CPU_NUMBER = Runtime.getRuntime().availableProcessors() > 1 ? Runtime.getRuntime().availableProcessors() : 2;

    private static final String MORSE_FILE_NAME = "morse_code.txt";

    private static final char LONG_VALUE = '-';
    private static final char SHORT_VALUE = '.';
    private static final char SPACE_VALUE = ' ';
    private static final char SPACE_WORD_VALUE = '/';

    private static final char UNKNOWN_CHAR = '?';


    private static Map<Character, char[]> morseCode;

    static {

        morseCode = new HashMap<>();

        try {
            loadMorseCodeFile();
        } catch (IOException | MorseCodeTableException e) {
            e.printStackTrace();
            System.exit(-1);
        }
    }


    private static double[] buildNote(double hz, double duration, double amplitude) {

        int n = (int) (StdAudio.SAMPLE_RATE * duration);

        double[] signal = new double[n + 1];

        for (int i = 0; i <= n; i++) {
            signal[i] = amplitude * Math.sin(2 * Math.PI * i * hz / StdAudio.SAMPLE_RATE);
        }

        return signal;
    }


    public static double[] buildSignal(String text, double speed, double amp) {

        if (speed <= 0) {
            return new double[0];
        }

        final int textLen = text.length();

        final double[][] signals = new double[textLen][];

        final List<Thread> builderThreads = new LinkedList<>();

        for (int i = 0; i < textLen; i++) {

            final double FINAL_SPEED = speed;
            final char C = text.charAt(i);
            final int INDEX = i;

            Thread thread = new Thread(() -> signals[INDEX] = buildValue(C, FINAL_SPEED, amp));

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

        for (int i = 0; i < signals.length; i++) {

            final int INDEX = i;
            final int P = position;

            Thread thread = new Thread(() -> System.arraycopy(signals[INDEX], 0, signal, P, signals[INDEX].length));
            thread.start();

            fusionThreads.add(thread);

            if (i + 1 % CPU_NUMBER == 0) {
                joinAllThreads(fusionThreads);
            }

            position += signals[i].length;
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


    private static double[] buildValue(char code, double speed, double amp) {


        final double FREQ = 550.0;
        final double SHORT_DURATION = 0.065;
        final double LONG_DURATION = 0.180;

        double[] signalValue;

        switch (code) {

            case SHORT_VALUE:
                signalValue = buildNote(FREQ, SHORT_DURATION * speed, amp);
                break;

            case LONG_VALUE:
                signalValue = buildNote(FREQ, LONG_DURATION * speed, amp);
                break;

            case SPACE_VALUE:
                signalValue = buildNote(FREQ, SHORT_DURATION * speed, 0.0);
                break;

            case SPACE_WORD_VALUE:
                signalValue = buildNote(FREQ, LONG_DURATION * speed, 0.0);
                break;

            default:
                return new double[0];
        }

        double[] whiteSignal = buildNote(FREQ, SHORT_DURATION * speed, 0.0);

        double[] signal = new double[signalValue.length + whiteSignal.length];

        System.arraycopy(signalValue, 0, signal, 0, signalValue.length);

        System.arraycopy(whiteSignal, 0, signal, signalValue.length, whiteSignal.length);

        return signal;
    }

    private static String encodeChar(char c) {

        if (!morseCode.containsKey(c)) {
            return String.valueOf(UNKNOWN_CHAR);
        }

        char[] code = morseCode.get(c);

        StringBuilder stringCode = new StringBuilder();

        for (int i = 0; i < code.length; i++) {
            stringCode.append(code[i]);
        }

        return new String(stringCode);
    }


    public static String encodeText(String text) {

        text = text.trim().toLowerCase().replace(System.lineSeparator(), " ");

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


    private static void loadTranslationLine(String line) throws MorseCodeTableException {

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


    private static void loadMorseCodeFile() throws IOException, MorseCodeTableException {

        FileReader fileReader = new FileReader(MORSE_FILE_NAME);
        BufferedReader reader = new BufferedReader(fileReader);

        String line;
        while ((line = reader.readLine()) != null) {
            loadTranslationLine(line);
        }

        fileReader.close();
    }
}