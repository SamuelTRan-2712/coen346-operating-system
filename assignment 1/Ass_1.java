import java.io.File;  // Import the File class
import java.io.FileNotFoundException;  // Import this class to handle errors
import java.util.Arrays;
import java.util.Scanner; // Import the Scanner class to read text files
import java.lang.Runnable;

import java.io.IOException;

public class Ass_1 {

	public static void main(String[] args) throws FileNotFoundException {
		// TODO Auto-generated method stub
		int[]input = new int [8]; //declaration of new integer array
		
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
		      
			}
		
		//if file is not found 
		catch (FileNotFoundException e) {
		      System.out.println("An error occurred.");
		}
		
		mergeSort(input, 0, input.length);
		System.out.println(Arrays.toString(input));
	}
	
	
	
	public static void mergeSort(int[] array, int start, int end) {

        // Break condition
        if (end - start < 2) {
            return;
        }

        // Find the middle of the array
        int mid = (start + end) / 2;

        // Left side
        Thread leftThread = new Thread(() -> mergeSort(array, start, mid));
        leftThread.start();
        int leftThreadID = (int) leftThread.getId();
        System.out.println("Thread " + Integer.toBinaryString(leftThreadID) + " started.");

        // Right side
        Thread rightThread = new Thread(() -> mergeSort(array, mid, end));
        rightThread.start();
        int rightThreadID = (int) rightThread.getId();
        System.out.println("Thread " + Integer.toBinaryString(rightThreadID) + " started.");


        // Wait for the threads to finish
        try {
            leftThread.join();
            rightThread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // Merge the halves
        merge(array, start, mid, end);

        // Display the array/sub-arrays
        int[] temp = Arrays.copyOfRange(array, start, end);
        int currentThreadID = (int) Thread.currentThread().getId();
        System.out.println("Thread " + Integer.toBinaryString(currentThreadID) + " finished: " + Arrays.toString(temp));
    }
	
	public static void merge(int[] array, int start, int mid, int end) {
        int n = end - start;
        int[] temp = new int[n];
        int i = start, j = mid;
        for (int k = 0; k < n; k++) {
            if (i == mid) {
                temp[k] = array[j++];
            } else if (j == end) {
                temp[k] = array[i++];
            } else if (array[j] < array[i]) {
                temp[k] = array[j++];
            } else {
                temp[k] = array[i++];
            }
        }

        // Copy the temp array back into the original array
        System.arraycopy(temp, 0, array, start, n);
    }
}
