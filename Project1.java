//Travis Jones n00436223
//Project 1 COP3404

import java.io.*;
import java.util.*;

class Project1 {

    public static void main(String args[]) throws FileNotFoundException {

        java.io.File file = new java.io.File(args[0]);
        Scanner in = new Scanner(file);
		
		//Read the contents of the entire file to a string
        String temp = in.useDelimiter("\\Z").next();

        in.close();
		
		//Find the number of instructions
        int n = countInstr(temp);
		//Find the smallest prime number greater than
		//twice the number of instructions
        int p = findPrime(n);

		//Create a hash table of size p
        Table table = new Table(p);

		//Insert the instructions from the temp string
		//in the Table table
        toTable(temp, table);
        
        return;
    }
	//Count the number of lines in s
    public static int countInstr(String s) {  
		
		int n=0;
		Scanner in = new Scanner(s);
		
		while(in.hasNextLine()) {
			n++;
			in.nextLine();
		}
		in.close();
		return n;
	}
	//Find the smallest prime number greater than twice
	//the value of n
	public static int findPrime(int n) {
		
		n*=2;
		n+=1;
		int m=0;

		for(m=2; m<n; m++) {
			
			if(n%m==0) {
				n++;
				m=2;
			}
			else if(m==n-1 && n%m != 0) {
				break;
			}
		}
		return n;
    }
    //Read instructions from a String and insert them in a Table
    public static void toTable(String s, Table table) {

        Scanner in = new Scanner(s);
        String temp = "";
        String mneu = "";
        String num = "";
        
		//If there is another token in s, move to that token
        if(in.hasNext()) {
        		temp = in.next();
        }
		//If not, return
        else {
        		in.close();
        		return;
        }
        //Create brk and set it to false 
        boolean brk = false;
                
        do {
            //If the current token is a mneumonic
            if(!temp.isEmpty() && Character.isLetter(temp.charAt(0))) {
            		mneu = temp;
            		if(in.hasNext()) {
						//If there is another token, assume that it's a number
                		num = in.next();
            		}
					//If not, finish this loop and then break
            		else {
            			brk = true;
            		}
            		//If the next token is really a number
            		if(!num.isEmpty() && Character.isDigit(num.charAt(0))) {
						//Insert mneu and num as part of the same instruction
            			table.insertInstr(mneu, num);
						//Set num to empty
            			num = "";
            			if(in.hasNext()) {
							//If there is another token, set temp equal to it
                			temp = in.next();
            			}
            			else {
							//If not, exit the loop
            				break;
            			}
            		}
            		else {
						//If num wasn't a number, set temp equal to it
            			temp = num;
						//Set num to empty
            			num = "";
						//Search for an instruction with mneu that already 
						//exists with insertInstr, or print an error
            			table.insertInstr(mneu, num);
            		}
            }
        } while(brk == false);
        //If the while loop has finished, nothing is left to scan
        in.close();
        return;
    }
}