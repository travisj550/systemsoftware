//Travis Jones n00436223
//Project 1 COP3404

import java.util.Objects;

//Table creates a hash table with an array of Instr objects
public class Table {
	
	private Instr[] array;
	private int length;

	//Create a new Table with p elements
	public Table(int p) {
		length = p;
		array = new Instr[length];	
	}
	//Generate a hash based on s
	private int genHash(String s) {
		int hash = 0;
		//For each character in s
		for(int i=0; i<s.length(); i++) {
			int ch = s.charAt(i);
			//calculate Horner's Polynomial assuming 128 ASCII characters
			hash = (hash * 128 + ch) % length;
		}
		return hash;
	}
	//Find if a mneumonic already exists, or insert a new Instr
	public void insertInstr(String s, String n) {
		//Create a new Instr with s
		Instr instr = new Instr(s);
		//If n isn't an empty string, set the (string) number for instr to n
		if(!n.isEmpty()) {
			instr.setNum(n);
		}
		//Find a hash value for the mneumonic, s
		int hash = genHash(s);
		
		int i = 1;
		//Until an empty space in the table is found
		while(array[hash] != null) {
			//If the instruction stored has the same mneumonic
			if(Objects.equals(array[hash].getInstr(), instr.getInstr())) {
				//If a number was included, print error and return
				if(!instr.getNum().isEmpty()) {
					System.out.println("ERROR " + instr.getInstr() + 
							" already exists at location " + hash);
					return;
				}
				//If a number wasn't included, print the instruction found
				//and return
				else {
					System.out.println(instr.getInstr() + 
							" found at location " + hash + " with value " 
							+ array[hash].getNum());
					return;
				}
			}
			//If the instruction doesn't have the same mneumonic,
			//a collision has occurred
			else {
				System.out.println("collision at location " + hash);
				hash = hash + i*i;
				i++;
				hash %= length;
			}
		}
		//If a number wasn't included in n but nothing with that mneumonic
		//was found, print an error
		if(instr.getNum().isEmpty()) {
			System.out.println("ERROR " + instr.getInstr() + " not found");
		}
		//If a number was included, store instr at that part of the hash table
		else {
			array[hash] = instr;
			
			System.out.println("stored " + instr.getInstr() + " " +
			instr.getNum() + " at location " + hash);
		}
	}
}
