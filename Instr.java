//Travis Jones n00436223
//Project 3 COP3404

public class Instr {
	private String label = "";
	private String address = "";

	public Instr(String s) {
		label = s;
	}
	
	public void setAddress(String n) {
		address = n;
	}

	public String getLabel() {
		return label;
	}
	
	public String getAddress() {
		return address;
	}
	
}
