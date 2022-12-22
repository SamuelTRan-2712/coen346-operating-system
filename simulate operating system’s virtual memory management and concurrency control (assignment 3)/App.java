import java.util.ArrayList;
import java.util.Arrays;
import java.util.concurrent.Semaphore;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class App {
	

    public static void main(String[] args) throws Exception {
    	PrintStream outP = new PrintStream(new FileOutputStream("output1.txt"),true);
        System.setOut(outP);
        
    	ArrayList<String> commandStrings = new ArrayList<String>();
        ArrayList<String> processInfo = new ArrayList<String>();
        int maxMemory = 0;
        int cores = 0;
        int processes = 0;
    	
        //Reading files
    	File commandFile = new File ("command.txt");
    	File processFile = new File ("process.txt");
    	File memConfigFile = new File ("memconfig.txt");
    	try {
    		Scanner commandReader = new Scanner(commandFile);
			//Command command;
			//read the command file
			while(commandReader.hasNextLine() == true) {
				String rawCommand = commandReader.nextLine();
				//String[] splitRawCommand = rawCommand.split(" ");
				commandStrings.add(rawCommand);
			}
			//int sizeofCommandsStrings = commandStrings.size(); 
    	} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	
    	try {
			Scanner processReader = new Scanner(processFile);
			cores = Integer.parseInt(processReader.nextLine());
			processes = Integer.parseInt(processReader.nextLine());
			while (processReader.hasNextLine()) {
				String line = processReader.nextLine();
				//String[] lineInfo = line.split(" ");
				processInfo.add(line);
		        
			}
		}catch (Exception e) {
	        e.printStackTrace();
	    }
		
    	
    	try {
			Scanner memconfigReader = new Scanner(memConfigFile);
			maxMemory = Integer.parseInt(memconfigReader.nextLine());
			
		}catch (Exception e) {
		    e.printStackTrace();
		}
    	
        // --------------------------------------------

        // Shared memory manager, semaphore, and clock
    	// Create an instance of semaphore for MMU, clock
        Semaphore semaphore = new Semaphore(1);
        Semaphore clockSempahore = new Semaphore(1);
        Process[] processArray = new Process[processes];
        Clock clock = new Clock(clockSempahore);
        MemoryManager memoryManager = new MemoryManager(maxMemory, clock);

        // Command index
        int commandCount = 0;
        
        // Copy arraylist into array since processInfoParts is of type String[]
        int sizeofProcessInfo = processInfo.size();
        String[] temp1 = new String[sizeofProcessInfo];
        for(int i = 0 ; i< sizeofProcessInfo; i++) {
        	temp1[i] = processInfo.get(i);
        }

        // Create processes
        for (int i = 0; i < processes; i++) {
        	
        	// Get arrival and burst time of a process
            String[] processInfoParts = temp1[i].split(" ");
            int startTime = Integer.parseInt(processInfoParts[0]);
            int duration = Integer.parseInt(processInfoParts[1]);
            
            // Create a process and save it in processArray
            Process process = new Process(memoryManager, semaphore, clock, i + 1, startTime * 1000, duration, null);
            processArray[i] = process;
        }

        System.out.println("Starting simulation");

        // Sort processes by start time so processArray will be in order of FIFO
        Arrays.sort(processArray, (a1, a2) -> a1.startTime - a2.startTime);
        
        //copy arraylist into array since commands is of type String[]
        int sizeofCommandsStrings = commandStrings.size();
        String[] temp = new String[sizeofCommandsStrings];
        for(int i = 0 ; i< sizeofCommandsStrings; i++) {
        	temp[i] = commandStrings.get(i);
        }
        
		// Add commands to each process
        for (int i = 0; i < processArray.length; i++) {
            String[] commands = Arrays.copyOfRange(temp, commandCount,
                    commandCount + processArray[i].duration);
            
            //extracting commands from String[] commands and assign it to the running process
            processArray[i].currentCommands = commands;
            
            //update command count
            commandCount += processArray[i].duration;
        }

        ArrayList<Process> runningProcesses = new ArrayList<Process>();
        int runningCount = 0;
        // Run processes
        while (processArray.length > 0) {
            // If there is a free core, add a process to it
            if (runningCount < cores) {
                for (int i = 0; i < processArray.length; i++) {
                    if (runningCount < cores) {
                        // Check if any processes should start
                        if (processArray[i].startTime <= clock.getTime()) {
                            runningProcesses.add(processArray[i]);
                            processArray[i].start();
                            processArray[i].join(1);
                            processArray = Arrays.copyOfRange(processArray, 1, processArray.length);
                            runningCount++;
                        }
                    }
                }
            } 
            // Check if any processes have finished
            else {
                for (int i = 0; i < runningProcesses.size(); i++) {
                    if (!runningProcesses.get(i).isAlive()) {
                        runningProcesses.remove(i);
                        runningCount--;
                    }
                }
            }
            
            //wait for one second
            Thread.sleep(1000);
            clock.time += 1000;
        }
        
       
    }
}
