package scheduling_with_multithread;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Scanner;

public class App {
    public static void main(String[] args) throws Exception {
    	
    	try{
    		File outputfile = new File("output.txt");
    		
    		if(!outputfile.exists()) {
    			
    			outputfile.createNewFile();
    			
    		} 
    			
    		}catch(IOException e) {
    			
    			e.printStackTrace();
    		}
    		//to get the output in a output file
    	PrintWriter writer = new PrintWriter("output.txt");
    	
    	
    	writer.println("Starting Process Scheduler");

        try {
        	System.out.println("-------------------------------------------------------------------");
        	System.out.println("Reading Input File");
        	writer.println("-------------------------------------------------------------------");
        	writer.println("Reading Input File");            
            
            // Input File for Processes
            File inputFile = new File ("inputforass2.txt");

            // Scanner for reading the input file
            Scanner reader = new Scanner(inputFile);

            // Read the first line of the input file
            // This line contains the Round Robin quantum
            int quantum = Integer.parseInt(reader.nextLine());

            // Create a new Process Scheduler using the quantum
            ProcessScheduler scheduler = new ProcessScheduler(quantum, writer);

            // Read the rest of the input file
            while (reader.hasNextLine()) {
                // Read the next line of the input file
                // This line contains a process owner and its number of processes
                String line = reader.nextLine();

                // Split the line into an array of strings
                String[] lineInfo = line.split(" ");

                // Retrieve the owner and add it to the scheduler
                String user = lineInfo[0];
                scheduler.users.add(user);

                // Retrieve the number of processes for the owner
                int numProcesses = Integer.parseInt(lineInfo[1]);

                // Read the next numProcesses lines of the input file
                // These lines contain the processes for the owner
                for (int index = 0; index < numProcesses; index++) {
                    // This line contains a process
                    String rawProcess = reader.nextLine();

                    // Add the process to the list of all processes
                    scheduler.addProcess(index, user, rawProcess);
                }
            }

            // Display the status of the scheduler
            writer.println("-------------------------------------------------------------------");
            System.out.println("-------------------------------------------------------------------");
            scheduler.print();
            writer.println("-------------------------------------------------------------------");
            System.out.println("-------------------------------------------------------------------");

            // Loop until all processes are done & no processes in the queue
            while (scheduler.allProcesses.size() > 0 || scheduler.processQueue.size() > 0) {
                // Run the scheduler
                scheduler.run();

                // Display the status of the scheduler
                scheduler.printStatus();
            }

            // Close the scanner
            reader.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        writer.println("-------------------------------------------------------------------");
        System.out.println("-------------------------------------------------------------------");
        writer.println("Exiting Process Scheduler");
        System.out.println("Exiting Process Scheduler");
        writer.close();
    }
}
