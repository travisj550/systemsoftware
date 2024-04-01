//Travis Jones n00436223
//Project 3 COP3404

import java.util.Objects;

public class Table {
	
	private Instr[] array;
	private int length;

	public Table(int p) {
		length = p;
		array = new Instr[length];	
	}
	
	private int genHash(String s) {
		int hash = 0;
		
		for(int i=0; i<s.length(); i++) {
			int ch = s.charAt(i);
			//Based on Horner's polynomial and 128 ASCII characters
			hash = (hash * 128 + ch) % length;
		}
		return hash;
	}

	public int insertSymbol(DataItem data) {
		
		String label = data.getLabel();
		Instr symbol = new Instr(label);
		int address = data.getAddress();
		
		symbol.setAddress(Integer.toHexString(address).toUpperCase());
		
		int hash = genHash(label);
		
		int i = 1;
		
		while(array[hash] != null) {
			
			if(Objects.equals(array[hash].getLabel(), symbol.getLabel())) {
				if(!symbol.getAddress().isEmpty()) {
					return 3;
				}
				else {
					return 0;
				}
			}
			else {
				hash = hash + i*i;
				i++;
				hash %= length;
			}
		}
		if(symbol.getAddress().isEmpty()) {
			
			return 2;
		}
		else {
			array[hash] = symbol;
			

			
			return 0;
		}
	}
	
	public String findLabel(String s) {
		
		for(int i=0; i < length; i++) {
			if(array[i] != null) {
				if(Objects.equals(s, array[i].getLabel())) {
					return array[i].getAddress();
				}
			}
		}
		
		return null;
	}
		
	public void printTable() {
		
		System.out.println("Table Location\tLabel\t\tAddress\t\tUse\t\tCsect");
		
		for(int i=0; i < length; i++) {
			if(array[i] != null && !array[i].getLabel().equals("")) {
				System.out.println(i + "\t\t" + array[i].getLabel() + "\t\t" + array[i].getAddress() + "\t\t" + "main" + "\t\t" + "main");
			}
		}
	}
}
