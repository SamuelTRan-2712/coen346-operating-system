import java.util.ArrayList;
import java.util.LinkedList;
import java.lang.Runnable;


public class ProcessScheduler extends Thread {

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
    
    //Thread thread;

    /**
     * Process Scheduler Constructor
     * @param quantum Duration of the Round Robin quantum - From the Input file
     */
    public ProcessScheduler(int quantum) {
        this.quantum = quantum;
        this.currentTime = 1;
        this.users = new ArrayList<String>();
        this.allProcesses = new ArrayList<Process>();
        this.processQueue = new LinkedList<Process>();
        //this.thread = new Thread();
    }

    
    /** 
     * Adds a process to the list of all processes
     * @param User Process User
     * @param rawProcess String of process data from the input file
     */
    public void addProcess(int processId, String user, String rawProcess) {

        // Split the raw process data into an array of strings
        String[] processInfo = rawProcess.split(" ");

        // Retrieve the arrival time and burst time from the array
        int arrivalTime = Integer.parseInt(processInfo[0]);
        int burstTime = Integer.parseInt(processInfo[1]);

        // Create a new process using the data from the input file
        Process process = new Process(processId, user, arrivalTime, burstTime);

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
        
        // Find Users with Active processes
        ArrayList<String> activeUsers = new ArrayList<String>();

        for (Process process : this.processQueue) {
            if (!activeUsers.contains(process.user)) {
                activeUsers.add(process.user);
            }
        }

        if(activeUsers.size() == 0) {
            return;
        }

        // Split the quantum among the active users
        int quantumPerUser = this.quantum / activeUsers.size();

        // Loop through the active users
        for(String user: activeUsers) {
        	
            // Find the processes in the queue for the user
            ArrayList<Process> userProcesses = new ArrayList<Process>();

            for (Process process : this.processQueue) {
                if (process.user == user) {
                    userProcesses.add(process);
                }
            }

            // Split the quantum among the user's processes
            int quantumPerProcess = quantumPerUser / userProcesses.size();

            // Run each process for the given quantum
            for (Process process : userProcesses) {
                // If its the first time running a process, set isStarted to true
                if(!process .isStarted) {
                    System.out.print("Time " + this.currentTime + ", ");
                    process.isStarted = true;
                }

                // If the burst time is less than the quantum, run the process for the burst time
                if(process.burstTime < quantumPerProcess) {
                	System.out.print("Time " + this.currentTime + ", ");
                    process.run();
                }
                else {
                	//System.out.print("Time " + this.currentTime + ", ");
                	process.run();
                }

                //System.out.println("Time " + this.currentTime + ", ");

                // Run the process for the quantum
                //process.burstTime -= quantumPerProcess;
                this.currentTime += quantumPerProcess;

                // If the process is finished, remove it from the queue
                if (process.burstTime == 0) {
                    System.out.println("Time " + this.currentTime + ", Process " + process.user + process.processId + ", Finished");
                    this.processQueue.remove(process);
                // If the process is not finished, pause it , and move it to the back of the queue
                } else {
                    System.out.println("Time " + this.currentTime + ", Process " + process.user + process.processId + ", Paused");
                    this.processQueue.remove(process);
                    this.processQueue.addLast(process);
                }
            }
        }
    }

    /**
     * Prints the initial process scheduler data
     */
    public void print() {
        System.out.println("Quantum: " + this.quantum);
        System.out.println("Users: " + this.users);
        System.out.println("All Processes: " + this.allProcesses);
    }

    /**
     * Prints the current time and process queue
     */
    public void printStatus()  {
        System.out.println("Current Time: " + this.currentTime);
        System.out.println("Process Queue: " + this.processQueue);
    }

}
