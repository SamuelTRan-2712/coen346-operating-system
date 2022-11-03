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
	public static ArrayList<Process> listOfAllProcesses;
	
	//to keep track of CPU running time 
	public int clock;
	
	public int numberOfActiveUsers;
	
	public ArrayList<String> activeUserNames;
	
	//public int currentRunningTime;
	
	//constructor
	public Scheduling(int cyclicQuantum) {
		this.cyclicQuantum = cyclicQuantum;
		this.ListOfUserNames = new ArrayList<String>();
		this.readyQueue = new LinkedList<Process>();
		this.listOfAllProcesses = new ArrayList<Process>();
		this.clock = 1;
		this.numberOfActiveUsers = 0;
		this.activeUserNames = new ArrayList<String>();
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
	
	
	public void addProcessesToReadyQueue(ArrayList<Process>listOfAllProcesses) {
		int timePerUser = 0; 
		ArrayList<Integer> timePerProcessPerUser = new ArrayList<Integer>();
		for (Process process: listOfAllProcesses) {
			if(process.arrivalTime <= clock) {
				readyQueue.clear();
				readyQueue.addLast(process);
				
				//this loop check the # of users that has processes that are ready to 
				//calculate the time each user will receive per cycle
				for(int i = 0; i < readyQueue.size(); i++ ) {
					int counterSameUserInList = 0;
					
					//to add the first user in ready queue
					if(activeUserNames.size() ==  0) {
						activeUserNames.add(readyQueue.get(0).userName);
					}
					
					//to check if the user is already in the active users list
					// if yes, then dont add 
					for(int j = 0; j < activeUserNames.size(); j++) {
						if(activeUserNames.get(j) == readyQueue.get(i).userName) {
							counterSameUserInList++; 	
						}
					}
					
					//if no, then add it 
					if(counterSameUserInList == 0) {
						activeUserNames.add(readyQueue.get(i).userName);
					}
				}
				
				timePerUser = cyclicQuantum/activeUserNames.size();
				
				//traverse through each user in the activeUserNames
				for(int i = 0; i < activeUserNames.size(); i++) {
					int countProcessesInUser = 0;
					
					//check how many processes there are in each active user  
					for(int j = 0; j < readyQueue.size(); j++) {
						if(activeUserNames.get(i) == readyQueue.get(j).userName) {
							countProcessesInUser++;
						}
					}
					
					//adds the time each process will run for per user 
					int timePerProcessForAUser = timePerUser/countProcessesInUser;
					timePerProcessPerUser.add(timePerProcessForAUser);
				}
				
			
			
				
				
				
			}
		}
	}
	
	public void runProcess(LinkedList<Process>readyQueue) {
		
		
	}
	
