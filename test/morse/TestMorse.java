package morse;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.awt.*;

public class TestMorse {

    @Test
    public void testDefaultValues() {

        Morse morse = new Morse();

        Assertions.assertEquals(Morse.DEFAULT_SPEED, morse.getSpeed());
        Assertions.assertEquals(Morse.DEFAULT_AMP, morse.getAmplitude());
        Assertions.assertEquals(Morse.DEFAULT_SIGNAL_COLOR, morse.getSignalColor());
    }

    @Test
    public void testSettersAndGetters() {

        Morse morse = new Morse();


        /* Speed */
        morse.setSpeed(3);

        Assertions.assertEquals(3, morse.getSpeed());

        morse.setSpeed(0);

        Assertions.assertEquals(Morse.DEFAULT_SPEED, morse.getSpeed());

        morse.setSpeed(15);

        Assertions.assertEquals(Morse.DEFAULT_SPEED, morse.getSpeed());

        morse.setAmplitude(0);

        Assertions.assertEquals(0, morse.getAmplitude());

        morse.setAmplitude(100);

        Assertions.assertEquals(Morse.DEFAULT_AMP, morse.getAmplitude());

        morse.setSignalColor(Color.RED);

        Assertions.assertEquals(Color.RED, morse.getSignalColor());

        morse.setSignalColor(null);

        Assertions.assertEquals(Morse.DEFAULT_SIGNAL_COLOR, morse.getSignalColor());
    }

    @Test
    public void testTranslateEvent() {

        Morse morse = new Morse();

        final boolean[] status = {false};

        morse.addTranslateListener(event -> status[0] = true);

        Assertions.assertFalse(status[0]);

        morse.convert();

        Assertions.assertTrue(status[0]);
    }

    @Test
    public void testRemoveTranslateEvent() {

        Morse morse = new Morse();

        final boolean[] status = {false};


        TranslateListener listener = event -> status[0] = true;

        morse.addTranslateListener(listener);

        Assertions.assertFalse(status[0]);


        morse.removeTranslateListener(listener);

        morse.convert();

        Assertions.assertFalse(status[0]);
    }
}
