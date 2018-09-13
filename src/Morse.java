import java.awt.*;

public class Morse extends Canvas {

    private String text;
    private double[] signal;

    public Morse() {
        setSize(200, 200);
        text = "";
        signal = new double[0];
        setBackground(Color.BLACK);
        disableLight();
    }

    private void enableLight() {

        if (!getBackground().equals(Color.WHITE)) {
            setBackground(Color.WHITE);
            repaint();
        }
    }

    private void disableLight() {

        if (!getBackground().equals(Color.BLACK)) {
            setBackground(Color.BLACK);
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

            if (s != 0.0) {
                enableLight();
            } else {
                disableLight();
            }

            System.out.println(s);

            StdAudio.play(s);
        }

        disableLight();
    }

    public String getText() {

        return text;
    }

    public void setText(String text) {

        this.text = text;
    }
}
