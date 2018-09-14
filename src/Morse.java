import java.awt.*;

public class Morse extends Canvas {

    private static final Color SIGNAL_COLOR = Color.YELLOW;
    private static final Color BACKGROUND_COLOR = Color.BLACK;

    private String text;

    private double[] signal;

    private int signalCursor;


    public Morse() {
        setSize(200, 200);
        text = "";
        signal = new double[0];
        setBackground(BACKGROUND_COLOR);
    }


    @Override
    public void paint(Graphics graphics) {

        final double COEFFICIENT = 5.0;

        if (signalCursor >= signal.length) {
            return;
        }

        graphics.setColor(SIGNAL_COLOR);

        int rayon = (int) (Math.abs(signal[signalCursor]) * (getWidth() + getHeight()) / COEFFICIENT);

        for (int r = rayon; r >= 1; r -= COEFFICIENT * 2) {
            graphics.drawOval(getWidth() / 2 - r, getHeight() / 2 - r, 2 * r, 2 * r);
        }
    }


    public void convert() {

        MorseConverter converter = MorseConverter.getInstance();
        text = converter.encodeText(text);
        signal = converter.buildSignal(text);
    }

    public void play() {

        for (signalCursor = 0; signalCursor < signal.length; signalCursor++) {

            StdAudio.play(signal[signalCursor]);

            repaint();
        }

        StdAudio.close();
    }

    public String getText() {

        return text;
    }

    public void setText(String text) {

        this.text = text;
    }

    public void setSpeed(double i) {

        MorseConverter converter = MorseConverter.getInstance();
        converter.setSpeed(i);
    }
}
