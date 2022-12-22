package scheduling_with_multithread;

import java.io.PrintWriter;

public class Process extends Thread{
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
    
    /**
     * output file called writer
     */
    public PrintWriter writer;

    /**
     * Process constructor
     * 
     * @param processId   Process ID
     * @param user        Process user - From the input file
     * @param arrivalTime Process arrival time - From the input file
     * @param burstTime   Process burst time - From the input file
     */
    public Process(int processId, String user, int arrivalTime, int burstTime, PrintWriter writer) {
        this.processId = processId;
        this.user = user;
        this.arrivalTime = arrivalTime;
        this.burstTime = burstTime;
        this.isStarted = false;
        this.writer = writer ;
    }

    /**
     * Process toString() method for printing
     */
    public String toString() {
        return this.user + this.processId + " " + this.arrivalTime + " " + this.burstTime;
    }

    @Override
    public void run(){
        if(!this.isStarted) {
            this.isStarted = true;
            writer.println("User " + this.user + ", " + "Process " +  this.processId + ", started");
            System.out.println("User " + this.user + ", " + "Process " +  this.processId + ", started");
        }
        writer.println("	  " + "User " + this.user + ", " + "Process " +  this.processId + ", resumed");
        System.out.println("	" + "User " + this.user + ", " + "Process " +  this.processId + ", resumed");
        while(this.burstTime > 0 && !Thread.currentThread().isInterrupted()) {
            try {
            	
            	//for every single 1000 millisecond, process.burstTime - 1
                Thread.sleep(1000);
                this.burstTime--;
                
                //if the thread is interrupted, return nothing
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                return;
            }
        }
        if(this.burstTime == 0){
        	writer.println("	  " + "User " + this.user + ", " + "Process " +  this.processId + ", finished");
        	System.out.println("	" + "User " + this.user + ", " + "Process " +  this.processId + ", finished");
        }
        return;
    }
}
