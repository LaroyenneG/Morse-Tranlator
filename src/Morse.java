import java.awt.*;

public class Morse extends Canvas {

    public static final double DEFAULT_SPEED = 5.0;

    public static final int DEFAULT_WIDTH = 200;
    public static final int DEFAULT_HEIGHT = 200;

    private static final Color DEFAULT_SIGNAL_COLOR = Color.CYAN;
    private static final Color DEFAULT_BACKGROUND_COLOR = Color.BLACK;

    private String text;

    private double[] signal;

    private int signalCursor;

    private double speed;

    private Color signalColor;


    public Morse() {
        setSize(DEFAULT_WIDTH, DEFAULT_HEIGHT);
        text = "";
        signal = new double[0];
        speed = DEFAULT_SPEED;
        signalColor = DEFAULT_SIGNAL_COLOR;
        setBackground(DEFAULT_BACKGROUND_COLOR);
    }


    @Override
    public void paint(Graphics graphics) {

        final double COEFFICIENT = 3.0;

        if (signalCursor >= signal.length) {
            return;
        }

        graphics.setColor(signalColor);

        final int width = getWidth();
        final int height = getHeight();

        final int minSize = width < height ? width : height;

        int rayon = (int) (Math.abs(signal[signalCursor]) * (minSize / COEFFICIENT));

        for (int r = rayon; r >= 1; r -= COEFFICIENT * 2) {
            graphics.drawOval(width / 2 - r, height / 2 - r, 2 * r, 2 * r);
        }
    }


    public void convert() {

        MorseConverter converter = MorseConverter.getInstance();
        text = converter.encodeText(text);
        signal = MorseConverter.buildSignal(text, speed);
        System.gc();
    }

    public void play() {

        for (signalCursor = 0; signalCursor < signal.length; signalCursor++) {
            StdAudio.play(signal[signalCursor]);
            repaint();
        }

        signalCursor = 0;

        StdAudio.close();
    }

    private void fireEEvent(Event event) {
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
        speed = i > 1.0 ? i : DEFAULT_SPEED;
    }

    @Override
    public String toString() {

        StringBuilder builder = new StringBuilder();

        builder.append("Morse");
        builder.append('\n');
        builder.append("\tText :");
        builder.append(text);
        builder.append("\tSpeed :");
        builder.append(speed);
        builder.append('\n');

        return new String(builder);
    }
}
