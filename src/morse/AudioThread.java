package morse;

public class AudioThread extends Thread {

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
                demo.getMorse().play();
                demo.unlockElements();

            } catch (InterruptedException e) {
                e.printStackTrace();
                break;
            }
        }
    }

    public synchronized void play() {
        notify();
    }

    public synchronized void standby() throws InterruptedException {
        wait();
    }
}
