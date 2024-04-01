//Travis Jones n00436223
//Project 1 COP3404

//Instr objects store instructions and their number values
public class Instr {
	private String mneu = "";
	private String num = "";

	//Construct a new Instr with s
	public Instr(String s) {
		mneu = s;
	}
	//Setter
	public void setNum(String n) {
		num = n;
	}
	//Getter
	public String getInstr() {
		return mneu;
	}
	//Getter
	public String getNum() {
		return num;
	}
	
}
