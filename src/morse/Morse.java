package morse;

import java.awt.*;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class Morse extends Canvas {

    public static final double DEFAULT_SPEED = 5.0;

    public static final int DEFAULT_WIDTH = 200;
    public static final int DEFAULT_HEIGHT = 200;

    private static final Color DEFAULT_SIGNAL_COLOR = Color.CYAN;
    private static final Color DEFAULT_BACKGROUND_COLOR = Color.BLACK;

    private String text;
    private String translateText;

    private double[] signal;

    private int signalCursor;

    private double speed;

    private Color signalColor;

    private List<TranslateListener> listener;


    public Morse() {
        setSize(DEFAULT_WIDTH, DEFAULT_HEIGHT);
        text = "";
        translateText = "";
        signal = new double[0];
        listener = new ArrayList<>();
        speed = DEFAULT_SPEED;
        signalColor = DEFAULT_SIGNAL_COLOR;
        setBackground(DEFAULT_BACKGROUND_COLOR);
    }


    @Override
    public void paint(Graphics graphics) {

        if (signalCursor >= signal.length) {
            return;
        }

        final double COEFFICIENT = 3.0;

        final int WIDTH = getWidth();
        final int HEIGHT = getHeight();

        graphics.setColor(signalColor);

        final int MIN_SIZE = WIDTH < HEIGHT ? WIDTH : HEIGHT;

        int rayon = (int) (signal[signalCursor] * (MIN_SIZE / COEFFICIENT));

        for (int r = rayon; r >= 1; r -= COEFFICIENT) {
            graphics.drawOval(WIDTH / 2 - r, HEIGHT / 2 - r, 2 * r, 2 * r);
        }
    }


    public void convert() {

        translateText = MorseConverter.encodeText(text);
        signal = MorseConverter.buildSignal(translateText, speed);

        fireTranslateEvent(new TranslateEvent(this));

        System.gc();
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

    /*
     * Getter and Setter zone
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
        signalColor = color;
    }

    public double getSpeed() {
        return speed;
    }

    public void setSpeed(double i) {
        speed = (i >= 1.0 || i <= 10) ? i : DEFAULT_SPEED;
    }

    public String getTranslateText() {
        return translateText;
    }

    @Override
    public String toString() {

        final DecimalFormat df = new DecimalFormat("0.000"); // import java.text.DecimalFormat;

        StringBuilder builder = new StringBuilder();

        builder.append("morse.Morse");
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
}
