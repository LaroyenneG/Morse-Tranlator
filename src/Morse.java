import javax.accessibility.Accessible;
import javax.accessibility.AccessibleContext;
import javax.accessibility.AccessibleRole;
import java.awt.*;

public class Morse extends Canvas {

    public static final double DEFAULT_SPEED = 5.0;

    private static final Color SIGNAL_COLOR = Color.CYAN;
    private static final Color BACKGROUND_COLOR = Color.BLACK;

    private String text;

    private double[] signal;

    private int signalCursor;

    private double speed;


    public Morse() {

        setSize(200, 200);
        text = "";
        signal = new double[0];
        speed = DEFAULT_SPEED;
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
        signal = converter.buildSignal(text, speed);
    }

    public void play() {

        for (signalCursor = 0; signalCursor < signal.length; signalCursor++) {

            StdAudio.play(signal[signalCursor]);

            repaint();
        }

        signalCursor = 0;

        StdAudio.close();
    }

    public String getText() {

        return text;
    }

    public void setText(String text) {

        this.text = text;
    }

    private void fireEEvent(Event event) {

    }

    public double getSpeed() {
        return speed;
    }

    public void setSpeed(double i) {

        speed = i > 0 ? i : DEFAULT_SPEED;
    }
}
