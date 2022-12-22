import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Scanner;

public class MemoryManager extends Thread {
    public int maxMemory;
    public HashMap<String, Variable> memory;
    public HashMap<String, Variable> storage;

    public Clock clock;
    

    public MemoryManager(int maxMemory, Clock clock) {
        this.maxMemory = maxMemory;
        this.memory = new HashMap<String, Variable>();
        this.storage = new HashMap<String, Variable>();
        this.clock = clock;
    }

    public void store(String address, int value) {
    	//check if the memory size is bigger or equal to the pages size
        if (this.memory.size() >= this.maxMemory) {
        	
        	//get the variable of type String in memory, so basically gets the variableID
            String addressToRelease = this.memory.keySet().iterator().next();
            
            //declaration of last access
            int lastAccessTime = 0;
            
            //traverse through the whole memory
            for (String var : this.memory.keySet()) {
            	// check if a process last access time is updated
                if (this.memory.get(var).lastAccessTime > lastAccessTime) {
                	//update the variable ID
                    addressToRelease = var;
                    
                    //update the last access time
                    lastAccessTime = this.memory.get(var).lastAccessTime;
                }
            }
            System.out.println("Clock: " + this.clock.getTime() + " - Memory Manager - Writing Variable " + addressToRelease
                    + " to storage");
            
            //swap out a page from memory into a storage since we need to do it 
            //if the memory is full and we need to store a new variables in the memory
            this.storage.put(addressToRelease, this.memory.get(addressToRelease));
            this.memory.remove(addressToRelease);
            
            System.out.println(
                    "Clock: " + this.clock.getTime() + " - Memory Manager - Removing Variable " + addressToRelease
                            + " from memory");
            writeStorage();
        }
        
        //create a new Variable with its value and store it in the memory
        Variable v = new Variable(address, value);
        this.memory.put(address, v);
        System.out.println(
                "Clock: " + this.clock.getTime() + " - Memory Manager - Writing Variable " + address + " to memory");
    }

    public void release(String address) {
    	//check if the memory has the variableID to be released
        if (this.memory.containsKey(address)) {
            System.out.println("Clock: " + this.clock.getTime() + " - Memory Manager - Writing Variable " + address
                    + " to storage");
            
            //if yes, put it in the storage
            this.storage.put(address, this.memory.get(address));
            System.out.println(
                    "Clock: " + this.clock.getTime() + " - Memory Manager - Removing Variable " + address + " from memory");
            
            //and remove it from the memory
            this.memory.remove(address);
            
        } else {
            System.out.println("Clock: " + this.clock.getTime() + " - Memory Manager - Address does not exist");
        }
    }
    
    public int lookup(String address) {
    	
    	//check if the memory contains the variableID
        if (this.memory.containsKey(address)) {
        	
        	//save the variableID in an instance called v
            Variable v = this.memory.get(address);
            
            //update the last access time after every command
            for(String var : this.memory.keySet()){
                this.memory.get(var).lastAccessTime++;
            }
            //set last access time of v = 0
            v.lastAccessTime = 0;
            //retrieve the variableID which is saved in v
            return v.value;
        } else {
        	//check if its in virtual memory (vm.txt)
            readStorage();
            
            //if yes, then...
            if (this.storage.containsKey(address)) {
            	
            	//store that variableID in v and give its last access = 0
                Variable v = this.storage.get(address);
                v.lastAccessTime = 0;
                
                if (this.memory.size() > this.maxMemory) {
                	
                	//get the variable of type String in memory, so basically gets the variableID
                    String addressToRelease = this.memory.keySet().iterator().next();
                    
                    //declaration of last access
                    int lastAccessTime = 0;
                    
                    //traverse through the whole memory
                    for (String var : this.memory.keySet()) {
                    	// check if a process last access time is updated
                        if (this.memory.get(var).lastAccessTime > lastAccessTime) {
                        	//update the variable ID
                            addressToRelease = var;
                            //update the last access time
                            lastAccessTime = this.memory.get(var).lastAccessTime;
                        }
                    }
                    
                    //swap out a page from memory into a storage since we need to do it 
                    //if the memory is full and we need to store a new variables in the memory
                    this.storage.put(addressToRelease, this.memory.get(addressToRelease));
                    this.memory.remove(addressToRelease);
                    System.out.println(
                            "Clock: " + this.clock.getTime() + " - Memory Manager - Writing Variable " + addressToRelease
                                    + " to storage");
                   
                    System.out.println(
                            "Clock: " + this.clock.getTime() + " - Memory Manager - Removing Variable " + addressToRelease
                                    + " from memory");
                    
                    //swap a page out into the storage
                    writeStorage();
                }
                System.out.println(
                        "Clock: " + this.clock.getTime() + " - Memory Manager - Reading Variable " + address
                                + " into memory");
               
                this.memory.put(address, v);
                this.storage.remove(address);

                return v.value;
                
                // if the variableID is not in storage, then print out that the address is not found
            } else {
                System.out.println("Clock: " + this.clock.getTime() + " - Memory Manager - Address does not exist");
                return -1;
            }
        }
    }

    public void readStorage() {
        File disk = new File("vm.txt");
        Scanner sc;
        try {
            sc = new Scanner(disk);
            while (sc.hasNextLine()) {
                String line = sc.nextLine();
                String[] parts = line.split(" ");
                Variable v = new Variable(parts[0], Integer.parseInt(parts[1]));
                this.storage.put(parts[0], v);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void writeStorage() {
        String content = "";
        for (String address : this.storage.keySet()) {
            content += address + " " + this.storage.get(address).value + "\n";
        }

        File disk = new File("vm.txt");
        FileWriter wt;
        try {
            wt = new FileWriter(disk);
            wt.write(content);
            wt.close();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
