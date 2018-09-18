import javax.swing.*;
import java.io.IOException;

public class Demo {

    public static void main(String[] args) throws IOException, MorseCodeTableException {

        MorseConverter converter = MorseConverter.getInstance();
        converter.loadMorseCodeFile();

        JFrame frame = new JFrame();

        frame.setVisible(true);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setSize(300, 500);

        Morse morse = new Morse();
        frame.add(morse);

        morse.setSpeed(1);
        morse.setText("SOS je suis mort");
        morse.convert();
        morse.play();

        System.out.println(morse.getText());

        System.exit(0);
    }
}
