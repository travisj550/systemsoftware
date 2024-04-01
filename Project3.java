//Travis Jones n00436223
//Project 3 COP3404

import java.io.*;
import java.util.*;

class Project3 {
		
    //File not found exception handling...
    public static void main(String args[]) throws FileNotFoundException {

        java.io.File file = new java.io.File(args[0]);
        Scanner in = new Scanner(file);

        String temp = in.useDelimiter("\\Z").next();

        in.close();
        
        LinkedList<DataItem> instructions = new LinkedList<DataItem>();
        
        int n = countInstr(temp);
        int p = findPrime(n);
        
        Table symtab = new Table(p);
        
		LinkedList<String> literals = new LinkedList<String>();
        
        //Read instructions line by line from temp, adding them to
        //the instructions LinkedList as DataItems, and inserting
        //labels in symtab
        readInstr(literals, temp, instructions, symtab);
		
		addLiterals(literals, symtab, instructions);
		
        //Find duplicate and undefined labels
		checkLabels(instructions, symtab);
		        
        ListIterator<DataItem> iterator = instructions.listIterator();
        
        while(iterator.hasNext()) {
        	
        		iterator.next().printLine();
        }
        
        System.out.println();
        
        symtab.printTable();
                
        /*
        //Instead of counting temp, count just the instructions with labels
        int n = countInstr(temp);
        int p = findPrime(n);

        Table table = new Table(p);

        toTable(temp, table);
        */
        
        return;
    }
    public static void addLiterals(LinkedList<String> literals, Table symtab, LinkedList<DataItem> llist) {
    	    	
    	

    		while(!literals.isEmpty()) {
    			
        		String label = literals.pop();
        		String operand = label.substring(1, label.length());
        		String mneu = "BYTE";
        		int mem = 0;
        		
        		
        		int address = llist.getLast().getNext();
        		
        		if(label.charAt(1) == 'X') {
        			mem = ( label.length() - 4 ) / 2;
        		}
        		else {
        			mem = ( label.length() - 4 );
        		}
        		
        		DataItem literal = new DataItem(address, mem, label, mneu, operand);
        		llist.add(literal);
        		
        		symtab.insertSymbol(literal);
    		}
    }
    //Check if mneu is a valid mneumonic. If it is, return the bytes
    //needed by the instruction, or -2 for an assembler directive. 
    //If it isn't, return -1
    public static int checkMneu(String mneu, boolean ext, boolean sic) {
    	
    	
    		int mem = 0;
    		
    		//If mneumonic has * but isn't a SIC mneumonic
    		if(sic == true && !mneu.matches("ST([XLA]||SW||CH)||OR||LD([ALX]||CH)||[TWR]D||J(SUB||LT||EQ||GT)||TIX||DIV||AND||MUL||COMP||ADD||(R)SUB||BYTE||WORD||RES[BW]")) {
    			mem = -7;
    			return mem;
    		}
    		
    		//This should probably be a switch...
    		
    		//If mneu is an assembler directive
		if (mneu.equals("START")) {
			if (ext == true) {
				mem = -1;
			}
			else {
				mem = -5;
			}
		}
		else if(mneu.matches("BASE")) {
			if (ext == true) {
				mem = -1;
			}
			else {
				mem = -2;
			}
		}
		else if(mneu.matches("END")) {
			if (ext == true) {
				mem = -1;
			}
			else {
				mem = -12;
			}
		}
		else if(mneu.matches("LTORG")) {
			if (ext == true) {
				mem = -1;
			}
			else {
				mem = -11;
			}
		}
    		//Need regex here for different registers with LD and ST
    		else if(mneu.matches("LD([AXLBSTF]||CH)") || mneu.matches("ST([AXLBSTFI]||SW||CH)")) {
    			mem = -22;
    		}
    		else if(mneu.matches("WORD")) {
    			mem = -13;
    			if(ext == true) {
    				mem = -1;
    			}
    		}
    		else if(mneu.matches("BYTE")) {
    			mem = -14;
    			if(ext == true) {
    				mem = -1;
    			}
    		}
    		else if(mneu.matches("RESW")) {
    			mem = -3;
    			if(ext == true) {
    				mem = -1;
    			}
    		}
    		else if(mneu.matches("RESB")) {
    			mem = -4;
    			if(ext == true) {
    				mem = -1;
    			}
    		}  
		//Register operations
    		else if(mneu.matches("(ADD||COMP||SUB||TIX||MUL||DIV)R||RMO||SHIFT[LR]||CLEAR")) {
    			
    			if(mneu.matches("SHIFT[LR]")) {
    				mem = -8;
    			}
    			else if(mneu.matches("CLEAR")) {
    				mem = -9;
    			}
    			else {
    				mem = -10;
    			}
    						
			if(ext == true) {
				mem = -1;
			}
    		}
		//Need regex for mneumonics starting with J
    		//If mneu is a subroutine operation
    		else if(mneu.matches("J(SUB||EQ||LT||GT)")) {
    			//Need to check if label is valid
    			
    			mem = -16;
    		}    	
    		else if(mneu.matches("RSUB")) {
    			//Shouldn't have an operand
    			
    			mem = -15;
    		}    
    		else if(mneu.matches("[TWR]D")) {
    			mem = -17;
    		}
		//Register A math
    		else if(mneu.matches("ADD||AND||COMP||DIV||MUL||SUB")) {
    			mem = -18;
    		}
		//Floating point math
    		else if(mneu.matches("(ADD||COMP||DIV||MUL||SUB)F")) {
    			mem = -19;
    		}
		//Uncategorized format 3 (requiring operands)
    		else if(mneu.matches("SSK||LPS")) {
    			mem = -20;
    		}
		//Uncategorized format 2 (requiring operands)
    		else if(mneu.matches("SVC")) {
    			mem = -21;
    		}
		//Format 1 operations (no operands)
    		else if(mneu.matches("[HST]IO||FIX||FLOAT||NORM")) {
    			mem = -6;
    		}
    		else {
    			mem = -1;
    		}

    		return mem;
    }
    //Split off operand checks here
    public static int[] checkOpnd(LinkedList<DataItem> llist, Table symtab, String operand, int mem, boolean  ext, boolean immed, boolean indir) {
    	
    		//{error, mem, address, opLabel}
    		int[] result = {0,0,0,0,0};
    		int error = 0;
    		int address = -1;
    		int opLabel = 0;
    		int literal = 0;
    		
		if (operand.startsWith("#")) {
			immed = true;
			operand = operand.substring(1);
			//START
			if(mem == -5) {
				error = 1;
				mem = 0;
			}
		}
		else if (operand.startsWith("@")) {
			indir = true;
			operand = operand.substring(1);
			
			//START and BASE 
			if(mem == -5 || mem == -2) {
				error = 1;
				mem = 0;
			}
		}
		
		//If mneumonic requires operand but doesn't have one
		if (operand.matches("") && mem != -15 && mem != -5 && mem != -2 && mem != -12 && mem != -11 && mem != -6) {
			mem = 0;
			error = 5;
		}
		//END
		if (mem == -12) {
			
			/*
			String startLbl = llist.getFirst().getLabel();
			
			System.out.println(startLbl);
			
			if(!operand.equals(startLbl)) {
				error = 7;
			}
			*/
			mem = 0;
		}
		//LTORG
		else if (mem == -11) {
			if(!operand.matches("")) {
				error = 4;
			}
			mem = 0;
		}
		else if(llist.isEmpty()) {
			//START
			if(mem == -5) {
				if(operand.matches("")) {
					address = 0;
				}
				else if(operand.matches("([0-9]|[A-F])*")) {
					address = Integer.parseInt(operand, 16);
				}
				else {
					error = 4;
					address = 0;
				}
				mem = 0;
			}
		}
		//RESW
		if(mem == -3) {
			if(operand.matches("[0-9]+")) {
				mem = Integer.parseInt(operand) * 3;
			}
			else {
				error = 4;
				mem = 0;
			}
		}
		//RESB
		else if(mem == -4) {
			if(operand.matches("[0-9]+")) {
				mem = Integer.parseInt(operand);
			}
			else {
				error = 4;
				mem = 0;
			}
		}
		//WORD
		else if(mem == -13) {
			//Can be X'' or C'' or ints?
			//Limits on WORD
			if(operand.matches("X'([0-9]|[A-F])?([0-9]|[A-F])?([0-9]|[A-F])?([0-9]|[A-F])?([0-9]|[A-F])?([0-9]|[A-F])?'||C'\\w{1,3}'")) {
				mem = 3;
			}
			else if (operand.matches("\\d*")) {
				
				int d = Integer.parseInt(operand);
				mem = 3;
				
				if (d > 16777215) {
					mem = 3;
					error = 4;
				}
			}
			else {
				mem = 3;
				error = 4;
			}
			
		}
		//BYTE
		else if(mem == -14) {
			//Can be X'' or C'' or ints?
			//Limits on BYTE
			if(operand.matches("X'([0-9]|[A-F])?([0-9]|[A-F])?'||C'\\w'")) {
				mem = 1;
			}
			else if (operand.matches("\\d*")) {
				
				int d = Integer.parseInt(operand);
				mem = 1;
				
				if (d > 255) {
					mem = 1;
					error = 4;
				}
			}
			else {
				mem = 1;
				error = 4;
			}
			
		}
		//RSUB, no operand
		else if(mem == -15) {
			if(operand.matches("")) {
				mem = 3;
			}
			else {
				mem = 3;
				error = 4;
			}
		}
		//Subroutine operations
		else if(mem == -16) {
			//Validate labels
			mem = 3;
			opLabel = 1;
			
			/*
			else {
				mem = 0;
				error = 4;
			}
			*/
		}
		//For mneumonics that don't need operands, like SIO
		else if (mem == -6) {
			
			if(operand.equals("")) {
				mem = 1;
			}
			else {
				error = 4;
				mem = 3;
			}
		}
		//Invalid SIC mneumonic
		else if (mem == -7) {
			error = 6;
			mem = 3;
		}
		//SHIFT[LR]
		else if (mem == -8) {
			if(operand.matches("[AXLBSTF],\\d")) {
				mem = 2;
			}
			else {
				error = 4;
				mem = 2;
			}
		}
		//CLEAR
		else if (mem == -9) {
			if(operand.matches("[AXLBSTF]")) {
				mem = 2;
			}
			else {
				error = 4;
				mem = 2;
			}
		}
		//BASE
		else if (mem == -2) {
			//Verify Labels
			opLabel = 1;
		}
		//Other register operations
		else if (mem == -10) {
			if(operand.matches("[AXLBSTF],[AXLBSTF]")) {
				mem = 2;
			}
			else {
				error = 4;
				mem = 2;
			}
		}
		//TD WD RD
		else if (mem == -17) {
			//Verify labels
			opLabel = 1;
			mem = 3;
		}
		//Register A math
		else if (mem == -18) {
			
			if(operand.matches("\\d*")) {
				mem = 3;
			}
			else if(operand.matches("=X'([0-9]|[A-F])*'")) {
				mem = 3;
			}
			//Verify labels
			else {
				opLabel = 1;
				mem = 3;
				error = 4;
			}
			
		}
		//Floating point math
		else if (mem == -19) {
			
			if(operand.matches("\\d*.*\\d*")) {
				mem = 3;
				opLabel = 1;
			}
			else if(operand.matches("=X'([0-9]|[A-F])*'")) {
				mem = 3;
			}
			//Verify labels
			else {
				opLabel = 1;
				mem = 3;
				error = 4;
			}
			
		}
		//Uncategorized format 3
		else if (mem == -20) {
			//Verify labels
			opLabel = 1;
			mem = 3;
		}
		//Uncategorized format 2
		else if (mem == -21) {
			//Verify labels
			opLabel = 1;
			mem = 2;
		}
		//LD or ST
		else if (mem == -22) {
			//Verify labels
			opLabel = 1;
			mem = 3;
		}
		if(ext == true && mem > 0) {
			mem++;
		}
		
		if(indir == true) {
			
			if(operand.matches("\\d*")) {
				error = 4;
			}
			else {
				opLabel = 1;
			}
		}
		
		if(operand.matches("=C'\\w*'") || operand.matches("=X'\\d*'")) {
			literal = 1;
		}
		
		
 		result[0] = error;
		result[1] = mem;
		result[2] = address;
		result[3] = opLabel;
		result[4] = literal;
		
    		return result;
    }
    
    //Throw error if too many or missing (required) columns
    public static LinkedList<DataItem> readInstr(LinkedList<String> literals, String s, LinkedList<DataItem> llist, Table symtab) {  
		
		Scanner in = new Scanner(s);
		
						
		while(in.hasNext()) {
			
			int address = 0;
			int mem = 0;
			String comment = "";
			int error = 0;
			boolean opLabel = false;
			
			String line = in.nextLine();
						
			Scanner inLine = new Scanner(line);
			
			
			if(!llist.isEmpty()){
				//Store mem in DataItem. Use address + mem of current DataItem
				//to create new DataItems
				//Implement own LinkedList to resolve?
				//Add mem size to DataItem for getNext?
				//(Implement getNext...
				address = llist.getLast().getNext();
			}
			
			if(line.matches("\\s*")) {
				DataItem blank = new DataItem(comment, address);
				llist.add(blank);
				continue;
			}
												
			//If a line is a comment
			if(line.startsWith(".")) {
				
				
				comment = line.substring(1);
				DataItem current = new DataItem(comment, address);
				llist.add(current);
				
				
				continue;
			}
			
			String label = "";
			
			if(!Character.isWhitespace(line.charAt(0))) {
				label = inLine.next();
			}
			
			String mneu = "";
			
			if(inLine.hasNext()) {
				mneu = inLine.next();
			}

			boolean ext = false;
			
			if (mneu.startsWith("+")) {
				ext = true;
				mneu = mneu.substring(1);
			}
			
			boolean sic = false;
			
			if (mneu.startsWith("*")) {
				sic = true;
				mneu = mneu.substring(1);
			}
			
			mem = checkMneu(mneu, ext, sic);
			
			if(mem == -1) {
				//mneumonic is invalid
				
				error = 1;
				mem = 0;
			}
			else if(mem == -2) {
				//mneumonic is an assembler directive
				
				mem = 0;
			}
			
			boolean immed = false;
			boolean indir = false;
			String operand = "";
			
			//Can split operand off into separate method...
			if(inLine.hasNext()) {
				operand = inLine.next();
			}
			//{error, mem, address}
			int[] check = {0,0,0,0};
			
			check = checkOpnd(llist, symtab, operand, mem, ext, immed, indir);
			
			error = check[0];
			mem = check[1];
			
			if(check[3] == 1) {
				opLabel = true;
			}
						
			if(check[4] == 1) {				
				literals.push(operand);
			}
			
			if(check[2] != -1) {
				address = check[2];
			}
			
			while(inLine.hasNext()) {
				inLine.useDelimiter("\\z");
				comment = inLine.next();
			}
			
			DataItem current = new DataItem(label, address, mem, mneu, ext, indir, immed, sic, operand, opLabel, comment, error);
			
			int symError = 0;
			
			//Insert labels into symbol table
			if(!label.equals("") && !label.substring(0, 1).matches("[0-9]")) {
				symError = symtab.insertSymbol(current);
			}
			
			if(!label.equals("") && label.substring(0, 1).matches("[0-9]")) {
				current.setError(7);
			}
						
			if(symError == 3) {
				current.setError(3);
			}
			if(symError == 2) {
				current.setError(2);
			}
			
			llist.add(current);
			
			inLine.close();
			
		}
		
		in.close();
		return llist;
	}
    
    public static LinkedList<DataItem> checkLabels(LinkedList<DataItem> llist, Table symtab) {
    	
    		
        ListIterator<DataItem> iterator = llist.listIterator();
        String address = "";
        String operand = "";
        
        //DataItem current = llist.getFirst();
        
        while(iterator.hasNext()) {
        	
    			DataItem current = iterator.next();
    			        	    
        		if(current.getOpLabel() == false) {

        			continue;
        		}
        		
        		//Check for undefined labels in operands
        		operand = current.getOperand();
        		
        		if(operand.startsWith("#") || operand.startsWith("@")) {
        			operand = operand.substring(1);
        		}
        		
        		if(!operand.matches("\\d*") && !operand.matches("")) {
        			
            		address = symtab.findLabel(operand);
            		
            		if(address == null) {
            			current.setError(2);
            		}
        		}
        		
        		/*
        		
        		//Check for duplicate labels
        		label = current.getLabel();
        		address = symtab.findLabel(label);
        		if(!address.equals("")) {
        			current.setError(3);
        		}
        		*/
        		
        }
    		
    		return llist;
    }

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
}