import java.util.ArrayList;
import java.util.LinkedList;

public class Scheduling {
	
	//quantum time for each cycle
	public int cyclicQuantum;
	
	//to store users names
	public ArrayList<String> ListOfUserNames;
	
	//to store processes that are ready and waiting to be executed
	public LinkedList<Process> readyQueue;
	
	//to store all the given processes 
	public ArrayList<Process> listOfAllProcesses;
	
	//to keep track of CPU running time 
	public int clock;
	
	//public int currentRunningTime;
	
	//constructor
	public Scheduling(int cyclicQuantum) {
		this.cyclicQuantum = cyclicQuantum;
		this.ListOfUserNames = new ArrayList<String>();
		this.readyQueue = new LinkedList<Process>();
		this.listOfAllProcesses = new ArrayList<Process>();
		this.clock = 1;
	}
	
	public void addProcess(int processID, String userName, String defaultProcess) {
		
		//split the defaultProcess in half where the first half is arrival time and second half is burst time
		String[] defaultProcessInfo = defaultProcess.split(" ");	
		
		//get arrival time from the already split default process info
		int arrivalTime = Integer.parseInt(defaultProcessInfo[0]);
		
		//get burst time from the already split default process info
		int burstTime = Integer.parseInt(defaultProcessInfo[1]);
		
		Process process = new Process(processID, burstTime, arrivalTime, userName);
		this.listOfAllProcesses.add(process);
		
		//for (int i = 0; i< listOfAllProcesses.size(); i++) {
	    	 //Process curr = listOfAllProcesses.get(i);
	         //System.out.println(curr.arrivalTime);
	         
	      //}  
	}
	
	public void addProcessesToReadyQueue() {
		
		/**
		 * This ArrayList is to be made to removed the processes 
		 * that are already in the ready queue so they dont
		 * get added more than twice while the for loop is initiated
		 */
		ArrayList<Process> ProcessTobeRemovedFromListOfAllProcess = new ArrayList<Process>();
		for (Process process: this.listOfAllProcesses) {
			if(process.arrivalTime <= this.clock) {
				
				//add the process to the ready queue and the "to be removed" list
				ProcessTobeRemovedFromListOfAllProcess.add(process);
				readyQueue.addLast(process);
			}
		}
		
		//removed all the processes that are in the ready queue 
		//from list of all processes
		listOfAllProcesses.remove(ProcessTobeRemovedFromListOfAllProcess);
	}
	
	public void runScheduling() {
		
		//add processes to the ready queue
		this.addProcessesToReadyQueue();
		
		//list of all users that have processes running in the ready queue
		ArrayList<String> activeUser = new ArrayList<String>();
		
		//list of all processes in the ready queue for one specific user
		ArrayList<Process> userProcesses = new ArrayList<Process>();
		
		//for all processes in the ready queue
		for(Process process: this.readyQueue) {
			
			//if activeUser doesnt have the username of the process
			//in the ready queue, add it to the activeUser list
			if(activeUser.contains(process.userName) == false) {
				activeUser.add(process.userName);
			}	
		}
		
		if(activeUser.size() == 0) {
			return;
		}
		
		//calculate the time quantum per each active user
		int quantumPerUser = (this.cyclicQuantum)/activeUser.size();
		
		
		for(String userName: activeUser) {
			
			//clear userProcesses whenever theres a new user
			userProcesses.clear();
			
			for(Process process: this.readyQueue) {
				if(process.userName == userName) {
					userProcesses.add(process);
				}
			}
			
			int quantumPerProcess = quantumPerUser/userProcesses.size();
			
			for(Process process: userProcesses) {
				// If the process run for the first time, set its ifProcessAlreadyStarted = True
				if(process.ifProcessAlreadyStarted == false) {
					System.out.print("Time " + this.clock + ", Process " + process.userName + process.processID + ", Started");
					process.ifProcessAlreadyStarted = true;
				}
				
				
				//if the burst time is less than quantumPerProcess, run the process for the burst time
				if(process.burstTime <= quantumPerProcess) {
					quantumPerProcess = process.burstTime;
				}
				
				System.out.print("Time " + this.clock + ", Process " + process.userName + process.processID + ", Resumed");
				
				//Run the process for the quantumPerProcess
				process.burstTime = process.burstTime - quantumPerProcess;
				this.clock = this.clock + quantumPerProcess;
				
				//if the process is finished, remove it from the readyQueue
				if(process.burstTime == 0) {
					System.out.print("Time " + this.clock + ", Process " + process.userName + process.processID + ", Finished");
					this.readyQueue.remove(process);
				}
				//if its not finished, remove it and add it to the last position of the readyQueue
				else {
					System.out.print("Time " + this.clock + ", Process " + process.userName + process.processID + ", Paused");
					this.readyQueue.remove(process);
					this.readyQueue.addLast(process);
				}
			}
		}
	}
}
