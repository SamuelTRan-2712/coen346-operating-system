import java.util.Random;
import java.util.concurrent.Semaphore;

public class Process extends Thread {
    public int id; //process ID
    public int startTime; //process arrival time 
    public int duration; // burst time 
    public String[] currentCommands;

    public MemoryManager memoryManager;
    Semaphore sem;
    Clock clock;
   
    public Process(MemoryManager memoryManager, Semaphore sem, Clock clock, int id, int startTime, int duration,
            String[] commands) {
        this.memoryManager = memoryManager;
        this.id = id;
        this.startTime = startTime;
        this.duration = duration;
        this.currentCommands = commands;
        this.sem = sem;
        this.clock = clock;
    }

    public void run() {
        // Run each command one by one
        for (int i = 0; i < this.currentCommands.length; i++) {
            String[] commandParts = this.currentCommands[i].split(" "); // Split raw command in to parts for exp: "Store" "1" "5"
            String commandType = commandParts[0]; // Store, lookup or release in commandType
            
            //generate a random time so the process could run with that 
            int randomSleepTime = new Random().nextInt(900) + 100;

            System.out.println("Clock: " + this.clock.getTime() + " - Process " + this.id + " - " + this.currentCommands[i]);

            // Run command
            // Needs semaphore to access memory manager
            if (commandType.equals("Store")) {
                try {
                	
                	//blocks the access of all process but 1, depends on which one comes first (FIFO) 
                    this.sem.acquire();
                    
                    //commandParts[1] = variable and commandParts[2] = value
                    //store the corresponding variables and values into the memoryManager
                    this.memoryManager.store(commandParts[1], Integer.parseInt(commandParts[2]));
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                //release the lock when the process is done running
                this.sem.release();
                
                //similar here but for release case  
            } else if (commandType.equals("Release")) {
                try {
                    this.sem.acquire();
                    
                    this.memoryManager.release(commandParts[1]);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                this.sem.release();
                
              //similar here but for lookup case
            } else if (commandType.equals("Lookup")) {
                try {
                    this.sem.acquire();
                    
                    this.memoryManager.lookup(commandParts[1]);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                this.sem.release();
                
                //exception handling case
            } else {
                System.out.println("Invalid command");
            }

            // Update Time and sleep
            try {
                this.clock.sem.acquire();
                this.clock.time += randomSleepTime; //update the clock = the time after the process is done running
                this.clock.sem.release();
                Thread.sleep(randomSleepTime); //let the process runs for a random time
                System.out.println(
                        "Clock: " + this.clock.getTime() + " - Process " + this.id + " - Done Command " + commandType);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        System.out.println("Clock: " + this.clock.getTime() + " - Process " + this.id + " - Done");
    }
}
