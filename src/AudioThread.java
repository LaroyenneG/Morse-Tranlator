public class AudioThread extends Thread {

    /*
     * Cette classe gère la lecture du signal (audio et graphique). Elle permet de ne pas bloquer les composants javas
     * swing pendant la lecture. Cette classe d'écrit un objet thread qui lance la lecture lorsque le thread est réveillé
     * (en utilisant "playSignal").
     * Pour bloquer la lecture il suffit de mettre le thread en sommeil (en utilisant "standby").
     */

    private Demo demo;

    public AudioThread(Demo demo) {
        this.demo = demo;
    }

    @Override
    public void run() {

        while (!isInterrupted()) {

            try {

                standby();

                demo.lockElements();
                demo.getMorseTranslator().play();
                demo.unlockElements();

            } catch (InterruptedException e) {
                e.printStackTrace();
                break;
            }
        }
    }

    public synchronized void playSignal() {
        notify();
    }

    public synchronized void standby() throws InterruptedException {
        wait();
    }
}
