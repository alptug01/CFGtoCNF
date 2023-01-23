import java.util.ArrayList;
public class CFG {
	private ArrayList<Variable> variables;
	private ArrayList<String> alphabet;
    public CFG() {
    	variables=new ArrayList<Variable>();
    	alphabet=new ArrayList<String>();
    }
	public void addVariable(Variable obj) {
		variables.add(obj);
	}
	public void addAlphabet(String str) {
		alphabet.add(str);
	}
	public void deleteVariable(Variable obj) {
		variables.remove(obj);
	}
	public ArrayList<Variable> getVariables() {
		return variables;
	}
	public ArrayList<String> getAlphabet() {
		return alphabet;
	}
	
	
   
   
}
