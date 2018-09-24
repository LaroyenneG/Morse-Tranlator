import javax.swing.*;
import java.io.FileWriter;
import java.io.IOException;

public class Demo {

    public static void main(String[] args) {


        JFrame frame = new JFrame();

        frame.setVisible(true);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setSize(300, 500);

        Morse morse = new Morse();
        frame.add(morse);

        morse.setSpeed(1.0);
        morse.setText("SOS");
        morse.convert();
        morse.play();


        System.exit(0);
    }
}
