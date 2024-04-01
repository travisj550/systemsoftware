//Travis Jones n00436223
//Project 3 COP3404

public class DataItem {
	
	private int error = 0;
	private String label = "";
	
	//mneu can be an instruction mneumonic or an assembler directive
	private String mneu = "";
	
	//Convert to hex with Integer.parseInt(Hex,16)
	private int address = 0;
	private int mem = 0;
	private boolean ext = false;
	private boolean indir = false;
	private boolean immed = false;
	private boolean sic = false;
	private String operand = "";
	private String comment = "";
	private boolean opLabel = false;
	
	//If normal instruction or assembler directive
	DataItem(String label, int address, int mem, String mneu, boolean ext, boolean indir, boolean immed, boolean sic, String operand, Boolean opLabel, String comment, int error) {
		this.label = label;
		this.address = address;
		this.mem = mem;
		this.mneu = mneu;
		this.ext = ext;
		this.indir = indir;
		this.immed = immed;
		this.sic = sic;
		this.operand = operand;
		this.opLabel = opLabel;
		this.comment = comment;
		this.error = error;
	}
	//If comment
	DataItem(String comment, int addr) {
		this.comment = comment;
		this.address = addr;
	}
	//For literals
	DataItem(int address, int mem, String label, String mneu, String operand) {
		this.address = address;
		this.mem = mem;
		this.label = label;
		this.mneu = mneu;
		this.operand = operand;
	}
	public int getAddress() {
		return address;
	}
	public int getNext() {
		return address + mem;
	}
	public String getLabel() {
		return label;
	}
	public String getOperand() {
		return operand;
	}
	public boolean getOpLabel() {
		return opLabel;
	}
	public void setError(int e) {
		this.error = e;
	}
	//Print this instruction or comment, followed by any errors
	public void printLine() {
		
		if(mneu.equals("")) {
			System.out.println("." + comment);
			return;
		}
		
		System.out.print(Integer.toHexString(address).toUpperCase() + "\t");
		
		if(!label.equals("")) {
			System.out.print(label + "\t");
		}
		
		else {
			System.out.print("\t");
		}
				
		if(ext == true) {
			System.out.print("+");
		}
		
		if(sic == true) {
			System.out.print("*");
		}
		
		System.out.print(mneu + "\t");
		
		if(immed == true) {
			System.out.print("#");
		}
		
		if(indir == true) {
			System.out.print("@");
		}
		
		System.out.print(operand + "\t" + comment + "\n");
		
		//Create error codes based on sicasm, rather than examples
		if(error != 0) {
			if(error == 1) {
				System.out.print("ERROR: Invalid Mneumonic \"" + mneu + 
						"\" (line will be ignored)\n");
				return;
			}
			if(error == 2) {
				System.out.print("ERROR: Undefined Label \"" + operand + "\"\n");
				return;
			}
			if(error == 3) {
				System.out.print("ERROR: Duplicate Label \"" + label + "\"\n");
				return;
			}
			if(error == 4) {
				System.out.print("ERROR: Invalid Operand \"" + operand + "\"\n");
				return;
			}
			if(error == 5) {
				System.out.print("ERROR: Missing Operand\n");
				return;
			}
			if(error == 6) {
				System.out.print("ERROR: Mneumonic Not SIC Compatible \"*" + mneu + "\" (line will be ignored)\n");
			}
			if(error == 7) {
				System.out.print("ERROR: Invalid Label \"" + label + "\"\n");
			}
		}
	}
}
