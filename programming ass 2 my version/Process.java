public class Process extends Thread {
    /**
     * Process ID
     */
    public int processId;

    /**
     * Process user
     */
    public String user;

    /**
     * Process arrival time
     */
    public int arrivalTime;

    /**
     * Process burst time
     */
    public int burstTime;

    /**
     * Says if the process has already been started
     */
    public boolean isStarted;
    
    Thread thread;

    /**
     * Process constructor
     * 
     * @param processId   Process ID
     * @param user        Process user - From the input file
     * @param arrivalTime Process arrival time - From the input file
     * @param burstTime   Process burst time - From the input file
     */
    public Process(int processId, String user, int arrivalTime, int burstTime) {
        this.processId = processId;
        this.user = user;
        this.arrivalTime = arrivalTime;
        this.burstTime = burstTime;
        this.isStarted = false;
        this.thread = new Thread();
    }

    /**
     * Process toString() method for printing
     */
    public String toString() {
        return this.user + this.processId + " " + this.arrivalTime + " " + this.burstTime;
    }
    
    public void run() {
    	System.out.println("Process " + this.user + this.processId +", started");
    	while (this.burstTime > 0) {
    		try {
    			this.burstTime -- ;
    			thread.sleep(1000);
    		}
    		catch(InterruptedException e) {
    			System.out.println("Process " + this.user + this.processId +", paused");
    		}
    		
    	}
    	System.out.println("Process " + this.user + this.processId + ", resumed");
    }
}
