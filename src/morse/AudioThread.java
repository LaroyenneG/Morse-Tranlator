package morse;

public class AudioThread extends Thread {


    private Demo panel;


    public AudioThread(Demo panel) {
        this.panel = panel;
    }

    @Override
    public void run() {
        while (!isInterrupted()) {

            try {
                standby();
                panel.lockElements();
                panel.getMorse().play();
                panel.unlockElements();
            } catch (InterruptedException e) {
                e.printStackTrace();
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
