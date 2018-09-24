import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class TestMorseConverter {

    @Test
    public void testTranslation() {


        Map<String, String> translations = new HashMap<>();

        translations.put("SOS", "... --- ...");
        translations.put("Bonjour comment allez vous ?", "-... --- -. .--- --- ..- .-. / -.-. --- -- -- . -. - / .- .-.. .-.. . --.. / ...- --- ..- ... / ..--..");
        translations.put("1 + 1 = 3", ".---- / .-.-. / .---- / -...- / ...--");
        translations.put("My e-mail is : titi.toto@truc.com ", "-- -.-- / . -....- -- .- .. .-.. / .. ... / ---... / - .. - .. .-.-.- - --- - --- .--.-. - .-. ..- -.-. .-.-.- -.-. --- --");
        translations.put("HELLO", ".... . .-.. .-.. ---");
        translations.put("hello ", ".... . .-.. .-.. ---");
        translations.put("this is a point : .", "- .... .. ... / .. ... / .- / .--. --- .. -. - / ---... / .-.-.-");
        translations.put(" ensisa", ". -. ... .. ... .-");
        translations.put("ensisa ", ". -. ... .. ... .-");
        translations.put("", "");
        translations.put("éà", "? ?");


        for (Map.Entry<String, String> entry : translations.entrySet()) {

            String sentence = entry.getKey();
            String morseCode = entry.getValue();

            Assertions.assertEquals(morseCode, MorseConverter.encodeText(sentence));
        }

    }


    @Test
    public void testSignal() {


        double[] signal = null;

        signal = MorseConverter.buildSignal("", 1.0);


        Assertions.assertArrayEquals(new double[0], signal);

        signal = MorseConverter.buildSignal("?", 1.0);


        Assertions.assertArrayEquals(new double[0], signal);

    }
}
