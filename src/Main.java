import java.io.IOException;

public class Main {

    public static void main(String[] args) throws IOException, InterruptedException {


        MorseConverter converter = MorseConverter.getInstance();
        converter.readFile();
        System.out.println(converter.encodeLine("A"));

        converter.playString(".---.");

        System.out.println("Hello");
    }
}
