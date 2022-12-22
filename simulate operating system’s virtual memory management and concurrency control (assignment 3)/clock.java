import java.util.concurrent.Semaphore;

public class Clock extends Thread{
    public int time;

    public Semaphore sem;

    public Clock(Semaphore semaphore) {
        this.time = 0;
        this.sem = semaphore;
    }

    // this function is to return the current time
    public int getTime() {
        int currentTime = -1;
        try {
            this.sem.acquire();
            currentTime = this.time;
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        this.sem.release();
        return currentTime;
    }
}

