import java.awt.*;

public class Morse extends Canvas {

    private static final Color COLOR_ON = Color.YELLOW;
    private static final Color COLOR_OFF = Color.BLACK;
    private boolean on;

    private String text;
    private double[] signal;

    public Morse() {
        setSize(200, 200);
        text = "";
        on = false;
        signal = new double[0];
        setBackground(COLOR_OFF);
        disableLight();
    }


    @Override
    public void paint(Graphics graphics) {

        final int BORDERS_SIZE = 10;

        graphics.setColor(on ? COLOR_ON : COLOR_OFF);

        graphics.fillRect(BORDERS_SIZE, BORDERS_SIZE, getWidth() - BORDERS_SIZE * 2, getHeight() - BORDERS_SIZE * 2);
    }

    private void enableLight() {

        if (!on) {
            on = true;
            repaint();
        }
    }

    private void disableLight() {

        if (on) {
            on = false;
            repaint();
        }
    }


    public void convert() {

        MorseConverter converter = MorseConverter.getInstance();
        text = converter.encodeText(text);
        signal = converter.buildSignal(text);
    }

    public void play() {

        for (double s : signal) {

            if (Math.abs(s) == 0.0) {
                disableLight();
            } else {
                enableLight();
            }

            StdAudio.play(s);
        }

        StdAudio.close();

        disableLight();
    }

    public String getText() {

        return text;
    }

    public void setText(String text) {

        this.text = text;
    }
}
