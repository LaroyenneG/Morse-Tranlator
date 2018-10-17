package morse;

import java.awt.*;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;


/*

 */
public class MorseTranslator extends Canvas {

    public static final double DEFAULT_AMP = 1.0;
    public static final double DEFAULT_SPEED = 5.0;


    public static final int DEFAULT_WIDTH = 200;
    public static final int DEFAULT_HEIGHT = 200;

    public static final Color DEFAULT_SIGNAL_COLOR = Color.CYAN;
    public static final Color DEFAULT_BACKGROUND_COLOR = Color.BLACK;

    private String text;
    private String translateText;

    private double[] signal;

    private int signalCursor;

    private double speed;
    private double amplitude;

    private Color signalColor;

    private List<TranslateListener> listener;


    public MorseTranslator() {

        setSize(DEFAULT_WIDTH, DEFAULT_HEIGHT);
        setBackground(DEFAULT_BACKGROUND_COLOR);

        text = "";
        translateText = "";

        signal = new double[0];

        listener = new ArrayList<>();

        speed = DEFAULT_SPEED;
        amplitude = DEFAULT_AMP;
        signalColor = DEFAULT_SIGNAL_COLOR;
    }


    @Override
    public void paint(Graphics graphics) {

        if (signalCursor < signal.length) {

            final double COEFFICIENT = 2.0;

            final int WIDTH = getWidth();
            final int HEIGHT = getHeight();

            graphics.setColor(signalColor);

            final int MIN_SIZE = WIDTH < HEIGHT ? WIDTH : HEIGHT;

            int rayon = (int) (signal[signalCursor] * (MIN_SIZE / COEFFICIENT));

            for (int r = rayon; r >= 1; r -= COEFFICIENT) {
                graphics.drawOval(WIDTH / 2 - r, HEIGHT / 2 - r, 2 * r, 2 * r);
            }
        }
    }


    public void convert() {

        signal = null;
        translateText = MorseHelper.encodeText(text);
        signal = MorseHelper.buildSignal(translateText, speed, amplitude);

        System.gc(); // La construction du signal consomme beaucoup de mémoire temporaire, alors on force un passage du Garbage Collector pour nettoyer la mémoire

        fireTranslateEvent(new TranslateEvent(this));
    }

    public void play() {

        for (signalCursor = 0; signalCursor < signal.length; signalCursor++) {

            StdAudio.play(signal[signalCursor]);

            repaint();
        }

        signalCursor = 0;
    }

    private void fireTranslateEvent(TranslateEvent event) {

        for (TranslateListener listener : listener) {
            listener.translate(event);
        }
    }

    public void addTranslateListener(TranslateListener event) {
        listener.add(event);
    }

    public void removeTranslateListener(TranslateListener listener) {
        this.listener.remove(listener);
    }


    @Override
    public String toString() {

        final DecimalFormat df = new DecimalFormat("0.000"); // import java.text.DecimalFormat;

        StringBuilder builder = new StringBuilder();

        builder.append("morse.MorseTranslator");
        builder.append('\n');
        builder.append("\tText :");
        builder.append(text);
        builder.append("\tSpeed :");
        builder.append(speed);
        builder.append('\n');
        builder.append("signal :\n");
        for (double s : signal) {
            builder.append(df.format(s).replace('.', ','));
            builder.append('\n');
        }

        return new String(builder);
    }

    /*
     * Getters and Setters zone
     */

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Color getSignalColor() {
        return signalColor;
    }

    public void setSignalColor(Color color) {
        signalColor = (color != null) ? color : DEFAULT_SIGNAL_COLOR;
    }

    public double getSpeed() {
        return speed;
    }

    public void setSpeed(double speed) {
        this.speed = (speed >= 1.0 && speed <= 10.0) ? speed : DEFAULT_SPEED;
    }

    public String getTranslateText() {
        return translateText;
    }

    public double getAmplitude() {
        return amplitude;
    }

    public void setAmplitude(double amp) {
        this.amplitude = (amp >= 0.0 && amp <= 1.0) ? amp : DEFAULT_AMP;
    }
}
