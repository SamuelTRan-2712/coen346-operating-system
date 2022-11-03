public class Process {
	//for process ID
	public int processID;
	
	//for burst time
	public int burstTime;
	
	//for arrival time
	public int arrivalTime;
	
	//to check if the process already started
	public boolean ifProcessAlreadyStarted; 
	
	//for user name
	public String userName;
	
	//Thread thread;
	
	
	/**
	 * constructor
	 * it needs to have such arguments so when we
	 * create an instance process, we can pass its information (the arguments)
	 * to the listOfAllProcesses
	 */ 
	public Process(int processID, int burstTime, int arrivalTime, String userName) {
		this.processID = processID;
		this.burstTime = burstTime;
		this.arrivalTime = arrivalTime;
		this.ifProcessAlreadyStarted = false;
		this.userName = userName;
		//this.thread = new Thread();
	}
}
