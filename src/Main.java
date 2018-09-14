import javax.swing.*;
import java.io.IOException;

public class Main {

    public static void main(String[] args) throws IOException {

        MorseConverter converter = MorseConverter.getInstance();
        converter.loadEncodeTable();

        JFrame frame = new JFrame();

        frame.setVisible(true);
        frame.setSize(200, 200);

        Morse morse = new Morse();
        frame.add(morse);

        morse.setText("SOS SOS SOS SOS SOS");
        morse.convert();
        morse.play();
        morse.setSpeed(1);

        System.out.println(morse.getText());

        System.exit(0);
    }
}
