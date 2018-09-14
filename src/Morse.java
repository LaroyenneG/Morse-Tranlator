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

        if (signalCursor >= signal.length) {
            return;
        }

        graphics.setColor(SIGNAL_COLOR);

        int rayon = (int) (Math.abs(signal[signalCursor]) * (getWidth() + getHeight()) / 5.0);

        if (rayon > 1) {
            graphics.drawOval(getWidth() / 2 - rayon, getHeight() / 2 - rayon, 2 * rayon, 2 * rayon);
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
}
