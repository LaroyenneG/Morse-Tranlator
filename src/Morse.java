import java.awt.*;

public class Morse extends Canvas {

    private static final Color COLOR_ON = Color.YELLOW;
    private static final Color COLOR_OFF = Color.BLACK;

    private String text;
    private double[] signal;

    public Morse() {
        setSize(200, 200);
        text = "";
        signal = new double[0];
        setBackground(COLOR_OFF);
        disableLight();
    }

    private void enableLight() {

        if (!getBackground().equals(COLOR_ON)) {
            setBackground(COLOR_ON);
            repaint();
        }
    }

    private void disableLight() {

        if (!getBackground().equals(COLOR_OFF)) {
            setBackground(COLOR_OFF);
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
