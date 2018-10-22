package morse.translator;

import java.awt.*;
import java.awt.font.FontRenderContext;
import java.awt.geom.Rectangle2D;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;


/**
 * Mon composant
 */

public class MorseTranslator extends Canvas {

    private static final double DEFAULT_AMP = 1.0;
    private static final double DEFAULT_SPEED = 5.0;

    private static final int DEFAULT_WIDTH = 200;
    private static final int DEFAULT_HEIGHT = 200;

    private static final Color DEFAULT_SIGNAL_COLOR = Color.CYAN;
    private static final Color DEFAULT_BACKGROUND_COLOR = Color.BLACK;

    private String text;
    private String translateText;

    private double[] signal;

    private int signalCursor;

    private double speed;
    private double amplitude;

    private Color signalColor;

    private List<TranslatedListener> translatedListeners;
    private List<EndPlayListener> endPlayListeners;
    private State state;

    public MorseTranslator() {

        setSize(DEFAULT_WIDTH, DEFAULT_HEIGHT);
        setBackground(DEFAULT_BACKGROUND_COLOR);

        text = "";
        translateText = "";

        signal = new double[0];

        translatedListeners = new ArrayList<>();
        endPlayListeners = new ArrayList<>();

        speed = DEFAULT_SPEED;
        amplitude = DEFAULT_AMP;
        signalColor = DEFAULT_SIGNAL_COLOR;

        changeState(State.WAITING);
    }

    public void convert() {

        changeState(State.TRANSLATING);

        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {

                signal = null;
                translateText = MorseTranslatorHelper.encodeText(text);
                signal = MorseTranslatorHelper.buildSignal(translateText, speed, amplitude);

                System.gc(); // La construction du signal consomme beaucoup de mémoire temporaire, alors on force un passage du Garbage Collector pour nettoyer la mémoire

                changeState(State.TRANSLATED);
                fireTranslatedEvent(new TranslatedEvent(this));
            }
        });

        thread.start();

        /*
         * Ce thread n'est pas indispensable. Mais il permet de ne pas bloquer les autres composants graphique durant
         * la phase de construction du signal. Avec cette procédure la fenêtre restera fonctionnelle durant la traduction.
         */
    }


    private void printMessage(Graphics graphics, String text) {


        Font font = new Font("Serif", Font.BOLD, 16);
        graphics.setFont(font);

        FontRenderContext fontRenderContext = new FontRenderContext(null, true, true);

        Rectangle2D stringBounds = font.getStringBounds(text, fontRenderContext);

        int x = (int) ((getSize().width / 2) - (Math.round(stringBounds.getWidth()) / 2) - Math.round(stringBounds.getX()));
        int y = (int) ((getSize().height / 2) - (Math.round(stringBounds.getHeight()) / 2) - Math.round(stringBounds.getY()));

        graphics.setFont(font);
        graphics.drawString(text, x, y);
    }

    @Override
    public void paint(Graphics graphics) {

        graphics.setColor(signalColor);

        switch (state) {

            case PLAYING:

                if (signalCursor < signal.length) {

                    final double COEFFICIENT = 2.0;

                    final int WIDTH = getWidth();
                    final int HEIGHT = getHeight();


                    final int MIN_SIZE = WIDTH < HEIGHT ? WIDTH : HEIGHT;

                    int rayon = (int) (signal[signalCursor] * (MIN_SIZE / COEFFICIENT));

                    for (int r = rayon; r >= 1; r -= COEFFICIENT) {
                        graphics.drawOval(WIDTH / 2 - r, HEIGHT / 2 - r, 2 * r, 2 * r);
                    }
                }
                break;

            case WAITING:
                printMessage(graphics, "Enter a text please...");
                break;

            case TRANSLATED:
                printMessage(graphics, "Text translated");
                break;

            case READY_TO_TRANSLATE:
                printMessage(graphics, "You can translate your text");
                break;

            case TRANSLATING:
                printMessage(graphics, "Please wait...");
                break;

            default:
                System.err.println("development error");
                System.exit(-1);
                break;
        }
    }

    public void play() {

        changeState(State.PLAYING);

        Thread thread = new Thread(new Runnable() {

            @Override
            public void run() {

                for (signalCursor = 0; signalCursor < signal.length && state == State.PLAYING; signalCursor++) {

                    StdAudio.play(signal[signalCursor]);

                    repaint();
                }

                signalCursor = 0;

                changeState(State.TRANSLATED);

                fireEndPlayEvent(new EndPlayEvent(this));
            }
        });

        thread.start();
    }


    public void stopPlay() {

        // il n'y a pas besoin d'utiliser de synchronized car tous les type de bases sont thread safe en java
        if (state == State.PLAYING) {
            changeState(State.TRANSLATED);
        }
    }

    @Override
    public String toString() {

        final DecimalFormat df = new DecimalFormat("0.000"); // import java.text.DecimalFormat;

        StringBuilder builder = new StringBuilder();

        builder.append("MorseTranslator");
        builder.append('\n');
        builder.append("\tText :");
        builder.append(text);
        builder.append("\tSpeed :");
        builder.append(speed);
        builder.append("\tAmplitude :");
        builder.append(amplitude);
        builder.append('\n');
        builder.append("Signal :\n");
        for (double s : signal) {
            builder.append(df.format(s).replace('.', ','));
            builder.append('\n');
        }

        return new String(builder);
    }

    /*
     * Listners
     */

    private void fireTranslatedEvent(TranslatedEvent event) {

        for (TranslatedListener listener : translatedListeners) {
            listener.translated(event);
        }
    }

    private void fireEndPlayEvent(EndPlayEvent event) {

        for (EndPlayListener listener : endPlayListeners) {
            listener.endPlay(event);
        }
    }

    public void addTranslatedListener(TranslatedListener listener) {
        this.translatedListeners.add(listener);
    }

    public void removeTranslatedListener(TranslatedListener listener) {
        this.translatedListeners.remove(listener);
    }

    public void addEndPlayListener(EndPlayListener listener) {
        this.endPlayListeners.add(listener);
    }

    public void removeEndPlayListener(EndPlayListener listener) {
        this.endPlayListeners.remove(listener);
    }


    private void changeState(State state) {
        this.state = state;
        repaint();
    }

    /*
     * Getters and Setters zone
     */
    public void setText(String text) {
        this.text = text;
        changeState(State.READY_TO_TRANSLATE);
    }

    public Color getSignalColor() {
        return signalColor;
    }

    public void setSignalColor(Color color) {
        signalColor = (color != null) ? color : DEFAULT_SIGNAL_COLOR;
    }

    public double getSpeed() {
        return speed;
    }

    public void setSpeed(double speed) {
        this.speed = (speed >= 1.0 && speed <= 10.0) ? speed : DEFAULT_SPEED;
        changeState(State.READY_TO_TRANSLATE);
    }

    public String getTranslateText() {
        return translateText;
    }

    public double getAmplitude() {
        return amplitude;
    }

    public void setAmplitude(double amp) {
        this.amplitude = (amp >= 0.0 && amp <= 1.0) ? amp : DEFAULT_AMP;
        changeState(State.READY_TO_TRANSLATE);
    }

    public State getState() {
        return state;
    }

    public String getText() {
        return text;
    }
}
