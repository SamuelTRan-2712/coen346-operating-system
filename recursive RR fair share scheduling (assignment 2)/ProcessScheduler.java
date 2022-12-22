package scheduling_with_multithread;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.LinkedList;

public class ProcessScheduler {

    /**
     * Duration of the Round Robin quantum
     */
    public int quantum;

    /**
     * Current CPU time
     */
    public int currentTime;
    /**
     * List of process Users
     * ex: User A, User B, User C, ...
     */
    public ArrayList<String> users;

    /**
     * List of processes that are waiting to be executed
     */
    public LinkedList<Process> processQueue;

    /**
     * List of all processes from the input file
     */
    public ArrayList<Process> allProcesses;

    /**
     * output file called writer
     */
    public PrintWriter writer;
    
    /**
     * Process Scheduler Constructor
     * 
     * @param quantum Duration of the Round Robin quantum - From the Input file
     */
    
    public ProcessScheduler(int quantum, PrintWriter writer) {
        this.quantum = quantum;
        this.currentTime = 1;
        this.users = new ArrayList<String>();
        this.allProcesses = new ArrayList<Process>();
        this.processQueue = new LinkedList<Process>();
        this.writer = writer ;
    }

    /**
     * Adds a process to the list of all processes
     * 
     * @param User       Process User
     * @param rawProcess String of process data from the input file
     */
    public void addProcess(int processId, String user, String rawProcess) {

        // Split the raw process data into an array of strings
        String[] processInfo = rawProcess.split(" ");

        // Retrieve the arrival time and burst time from the array
        int arrivalTime = Integer.parseInt(processInfo[0]);
        int burstTime = Integer.parseInt(processInfo[1]);

        // Create a new process using the data from the input file
        Process process = new Process(processId, user, arrivalTime, burstTime, writer);

        // Add the process to the list of all processes
        this.allProcesses.add(process);
    }

    /**
     * Moves arrived processes to the process queue
     */
    public void moveArrivedProcesses() {
        ArrayList<Process> arrivedProcesses = new ArrayList<Process>();

        for (Process process : this.allProcesses) {
            if (process.arrivalTime <= this.currentTime) {
                arrivedProcesses.add(process);
                this.processQueue.addLast(process);
            }
        }

        this.allProcesses.removeAll(arrivedProcesses);
    }

    /**
     * Main Section of the Process Scheduler
     */
    public void run() {
        // Move arrived processes to the process queue
        this.moveArrivedProcesses();

        System.out.println("-------------------------------------------------------------------");
        writer.println("-------------------------------------------------------------------");
      
        // Find Users with Active processes
        ArrayList<String> activeUsers = new ArrayList<String>();

        for (Process process : this.processQueue) {
            if (!activeUsers.contains(process.user)) {
                activeUsers.add(process.user);
            }
        }

        if (activeUsers.size() == 0) {
            return;
        }

        // Split the quantum among the active users
        int quantumPerUser = this.quantum / activeUsers.size();

        // Loop through the active users
        for (String user : activeUsers) {
            // Find the processes in the queue for the user
            ArrayList<Process> userProcesses = new ArrayList<Process>();

            for (Process process : this.processQueue) {
                if (process.user.equals(user)) {
                    userProcesses.add(process);
                }
            }

            // Split the quantum among the user's processes
            int quantumPerProcess = quantumPerUser / userProcesses.size();

            for (Process process : userProcesses) {
            	//create a variable called quantumTime and save
            	//the quantumPerProcess there 
                int quantumTime = quantumPerProcess;
                
                //if burstTime < quantumPerProcess
                //then quantumTime = process.burstTime
                if (process.burstTime < quantumPerProcess) {
                    quantumTime = process.burstTime;
                }

                try {
                	//check if the process is started 
                	//if yes, remove it from the ready queue and make a copy of it 
                	//so we could start the already running thread because for some reason
                	//process.run() won't be interrupted here 
                    if (process.isStarted) {
                        this.processQueue.remove(process);
                        process = new Process(process.processId, process.user, process.arrivalTime, process.burstTime, writer);
                    }
                    
                    writer.print("Time " + this.currentTime +", ");
                    System.out.print("Time " + this.currentTime +", ");
                    
                    //start the process as a thread. This will invoke the run() function in process.java
                    process.start();
                    
                    //run for its quantum time in milliseconds
                    process.join(quantumTime * 1100);
                    
                    //interrupt the process to pause it
                    //this will invoke the catch block in process.run()
                    process.interrupt();
                } catch (InterruptedException e) {
                    
                }

                this.currentTime += quantumTime;

                if (process.burstTime > 0) {
                	
                	//if its burstTime after running is still > 0 
                	//add it again to the allProcesses so then it could be added again 
                	//to the end of the ready queue
                    this.allProcesses.add(process);
                    this.processQueue.remove(process);
                    writer.println("Time " + this.currentTime +", " + "User " + process.user + ", " + "Process " +  process.processId + ", paused");
                    System.out.println("Time " + this.currentTime +", " + "User " + process.user + ", " + "Process " +  process.processId + ", paused");
                } else {
                	//if its done running, remove it from the ready queue
                    this.processQueue.remove(process);
                }
            }
        }

        // Move arrived processes to the process queue
        this.moveArrivedProcesses();
    }

    
    /**
     * Prints the initial process scheduler data
     */
    public void print() {
        writer.println("Quantum: " + this.quantum);
        System.out.println("Quantum: " + this.quantum);
        writer.println("Users: " + this.users);
        System.out.println("Users: " + this.users);
        writer.println("All Processes: " + this.allProcesses);
        System.out.println("All Processes: " + this.allProcesses);
    }

    /**
     * Prints the current time and process queue
     */
    public void printStatus() {
    	writer.println("Current Time: " + this.currentTime);
    	System.out.println("Current Time: " + this.currentTime);
    	writer.println("Process Queue: " + this.processQueue);
    	System.out.println("Process Queue: " + this.processQueue);
    }

}
