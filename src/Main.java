import java.io.IOException;

public class Main {

    public static void main(String[] args) throws IOException {

        MorseConverter converter = MorseConverter.getInstance();
        converter.loadEncodeTable();

        converter.playString(converter.encodeText("SOS"));
    }
}
