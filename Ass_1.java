
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
		      System.out.println(Arrays.toString(input)); //print the string
		      
			}
		
		//if file is not found 
		catch (FileNotFoundException e) {
		      System.out.println("An error occurred.");
	}
}
}
	
