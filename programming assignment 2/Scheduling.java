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
	
	public void addProcessesToReadyQueue(ArrayList<Process>listOfAllProcesses) {
		for (Process process: listOfAllProcesses) {
			if(process.arrivalTime <= clock) {
				readyQueue.addLast(process);
			}
		}
	}
	
	
}
