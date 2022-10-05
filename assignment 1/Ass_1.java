// Ngoc Son (Samuel) Tran - 40083531
// Theebika Thiyagaraja Iyer - 400191001
// Simren Matharoo - 40042587


import java.io.File;  // Import the File class
import java.io.FileNotFoundException;  // Import this class to handle errors
import java.util.Arrays;
import java.util.Scanner; // Import the Scanner class to read text files
import java.lang.Runnable;

import java.io.IOException;
import java.io.PrintWriter;

public class Ass_1 {

	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		
		int[]input = new int [8]; //declaration of new integer array
		PrintWriter out = new PrintWriter("output.txt"); // define a new text file called output.txt
		
		//if file is found then try
		try {
		      File myObj = new File("input.txt"); //create an object that has the same info with "input.txt"
		      Scanner myReader = new Scanner(myObj); //read the file object

		      //for every single index of an array, add the corresponding element from the file object
		      for (int i = 0; i <= 7; i++)
		      {
		    	  String temp = myReader.nextLine();
		        	input[i] = Integer.valueOf(temp);
		        	
		      }
		      //System.out.println(Arrays.toString(input)); //print the string
		      myReader.close(); // close the input file 
			}
		
		//if file is not found 
		catch (FileNotFoundException e) {
		      System.out.println("An error occurred.");
		}
		
		mergeSort(input, 0, input.length, out);
		out.close();
		
}
	
	
	
	public static void mergeSort(int[] array, int start, int end, PrintWriter out) throws IOException {
		
        
        // Break condition
        if (end - start < 2) {
            return;
        }

        // Find the middle of the array
        int mid = (start + end) / 2;

        try {
			// Left side
			Thread leftThread = new Thread(() -> {
				try {
					mergeSort(array, start, mid, out);
					} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					}
				});
				
			leftThread.start();
			int leftThreadID = (int) leftThread.getId();
			out.println("Thread " + Integer.toBinaryString(leftThreadID) + " started.");
			System.out.println("Thread " + Integer.toBinaryString(leftThreadID) + " started.");
				
			// Right side
			Thread rightThread = new Thread(() ->{
				try {
						mergeSort(array, mid, end, out);
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				});
			rightThread.start();
		    int rightThreadID = (int) rightThread.getId();
		    out.println("Thread " + Integer.toBinaryString(rightThreadID) + " started.");
		    System.out.println("Thread " + Integer.toBinaryString(rightThreadID) + " started.");
		        
		        
		    leftThread.join();
		    rightThread.join();
		    
		    // Merge the halves    
		    merge(array, start, mid, end);

		    // Display the array/sub-arrays
		    int[] temp = Arrays.copyOfRange(array, start, end);
		    int currentThreadID = (int) Thread.currentThread().getId();
		        
		    out.println("Thread " + Integer.toBinaryString(currentThreadID) + " finished: " + Arrays.toString(temp));
		    System.out.println("Thread " + Integer.toBinaryString(currentThreadID) + " finished: " + Arrays.toString(temp));
		        
		    } 
		    catch (InterruptedException e) {
		    	e.printStackTrace();
		    }
	} 

	public static void merge(int[] array, int start, int mid, int end) {
        int n = end - start; //define the length of the array
        int[] temp = new int[n]; //create a temporary array
        int i = start, j = mid; // set i= start and j = mid so when we merge, we merge 
        						//left side, right side then combine them together based on the recursive call from merge sort function
        
        // run k till it reach the length of temporary array
        for (int k = 0; k < n; k++) {
            if (i == mid) {
                temp[k] = array[j++]; // if i = mid, copy the one after mid 
            } else if (j == end) {
                temp[k] = array[i++]; // if j = end, that means theres nothing else to compare with j, moves i up by 1 
            } else if (array[j] < array[i]) {
                temp[k] = array[j++]; // compare the value of index j with index i, if j < i, then copy the value of j+1 to temp array
            } else {
                temp[k] = array[i++]; // else, copy the value of i+1 to temp
            }
        }

        // Copy the temp array back into the original array
        System.arraycopy(temp, 0, array, start, n);
    }
}
	
