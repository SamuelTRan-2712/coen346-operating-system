import java.io.File;
import java.util.Arrays;
import java.util.Scanner;
//import Scheduling.java;

public class RunProcessScheduler {

	public static void main(String[] args) throws Exception {
		// TODO Auto-generated method stub
		
		//read the input file 
		File inputFile = new File ("inputforass2.txt");
		Scanner reader = new Scanner(inputFile);
		
		//save the time quantum
		int cyclicQuantum = Integer.parseInt(reader.nextLine());
		
		//create an instant scheduler 
		Scheduling scheduler = new Scheduling(cyclicQuantum);
		
		int currentProcess = 0; 
		
		int totalCycleTimeNeeded = 0; 
		
		//read the rest of the input file
		while(reader.hasNextLine() == true) {
			
			//read the line after time quantum
			String line = reader.nextLine();	
			
			//Split the line into 2 parts where 1 contains user name 
			//and the other has the number of processes 
			String[] lineData = line.split(" ");
			
			//retrieve username from lineData[0]
			String userName = lineData[0];
			
			//use the already created instant scheduler to add user name to its list of username
			scheduler.ListOfUserNames.add(userName);
			
			//retrieve the number of processes own by a user
			int numOfProcessOwn = Integer.parseInt(lineData[1]);
			
			numOfProcessOwn += currentProcess;
			
			for(int i = currentProcess; i < numOfProcessOwn; i++) {
				
				//read the next line, which contains arrival and burst time of a process
				String defaultProcessData = reader.nextLine();
				
				//add the process with i = processID, username and 
				//defaultProcess data to be broken down to arrival and burst time
				scheduler.addProcess(i, userName, defaultProcessData);
				currentProcess++;
				
			}
		}
	}
}
