import java.util.ArrayList;
import java.util.Random;

public class CFG_To_CNF {
	public void CFGtoCNF() {
		TxtOperations txt_op =new TxtOperations();
		CFG cfg=txt_op.createCFG();
		if(txt_op.isCorrectInput() &&checkingAlphabet(cfg)) {//checking alphabet and input
			System.out.println("\nEliminate €");
			eliminate_epsilon(cfg);
			System.out.println("\nEliminate unit products");
			eliminate_units(cfg);
			System.out.println("\nEliminate terminals");
			eliminate_terminals(cfg);
			System.out.println("\nBreak variable strings longer than 2");
			breakVariable(cfg);
			System.out.println("\nCNF");
			displayCFG(cfg);
		}else if(!checkingAlphabet(cfg)) {
			System.out.println("\nInvalid alphabet");
		}else {
			System.out.println("\nInvalid input");
		}
	}
	public void displayCFG(CFG cfg) {//display cfg
		ArrayList<Variable> variable_list=cfg.getVariables();
		for(int i=0;i<variable_list.size();i++) {
			System.out.print(variable_list.get(i).getName()+"-");
			variable_list.get(i).displayRS();
			System.out.println();
		}
	}
	private void eliminate_epsilon(CFG cfg) { 
		ArrayList<Variable> variable_list=cfg.getVariables();
		ArrayList<Variable> variables_with_epsilon=new ArrayList<Variable>();
		for(int i=0;i<variable_list.size();i++) {//searching epsilon except S and S'
			if(!variable_list.get(i).getName().equals("S")&&!variable_list.get(i).getName().equals("S'")&&variable_list.get(i).searchingRS("€")) {
				variables_with_epsilon.add(variable_list.get(i));//assigning  variables with epsilon to AList
				variable_list.get(i).setHadEpsilon(true);
			}
		}
		int epsilonarr_size=variables_with_epsilon.size();
		if(epsilonarr_size!=0) {
			for(int i=0;i<epsilonarr_size;i++) {
				String variable_name=(variables_with_epsilon.get(i).getName());
				System.out.println("Eliminating "+variable_name+" -> €");
				for(int j=0;j<variable_list.size();j++) {
					variable_list.get(j).epsilonRS(variable_name);
				}
				displayCFG(cfg);			
			}
			eliminate_epsilon(cfg);//recursive until variable with epsilon arrList element count is reset
		}
	}
	
	
 
	private void eliminate_units(CFG cfg) {
		ArrayList<Variable> variable_list=cfg.getVariables();
		for(int i=variable_list.size()-1;i>=0;i--) {//start at the bottom of the variable list
			ArrayList<String> right_side=variable_list.get(i).getRight_side();
			for(int j=0;j<variable_list.size();j++) {
				if(right_side.contains(variable_list.get(j).getName())&&!variable_list.get(i).getName().equals(variable_list.get(j).getName())) {
					ArrayList<String> unit_RS=variable_list.get(j).getRight_side();
					variable_list.get(i).addRSwithRS(unit_RS);//adding rightside using rightside of variable
					right_side.remove(variable_list.get(j).getName());
				}
				else if(right_side.contains(variable_list.get(j).getName())&&variable_list.get(i).getName().equals(variable_list.get(j).getName())) {
					right_side.remove(variable_list.get(j).getName());
				}//In order to prevent it from looping in the deletion part, the element is deleted if it comes back to itself
			}
		}
		displayCFG(cfg);
	}
	
	private void eliminate_terminals(CFG cfg) {
		ArrayList<Variable> variable_list=cfg.getVariables();
		ArrayList<String> variable_names=new ArrayList<String>();
		ArrayList<String> non_terminals=new ArrayList<String>();
		for(int i=0;i<variable_list.size();i++) {
			variable_names.add(variable_list.get(i).getName());
		}
		for(int i=0;i<variable_list.size();i++) {
			variable_list.get(i).findingTerminals(variable_names, non_terminals);
		}
		for(int i=0;i<variable_list.size();i++) {
			ArrayList<String> right_side=variable_list.get(i).getRight_side();
			for(int j=0;j<right_side.size();j++) {
				String rs_element=right_side.get(j);
				if(rs_element.length()>1) {
					for(int k=0;k<rs_element.length();k++) {
						if(non_terminals.contains(String.valueOf(rs_element.charAt(k)))) {
							String variableName=chooseLetter(variable_names);
							cfg.addVariable(new Variable(variableName,String.valueOf(rs_element.charAt(k))));
							adaptVariable(cfg,String.valueOf(rs_element.charAt(k)),variableName.charAt(0));
							rs_element=right_side.get(j);
						}
					}
				}
			}
		}
		displayCFG(cfg);
	}
	private String chooseLetter(ArrayList<String> variable_names) {//choose variable name randomly
		Random r = new Random();
		char c;
		while(true) {
			c = (char)(r.nextInt(26) + 'A');
			if(!variable_names.contains(String.valueOf(c))) {
				break;
			}
		}
		return String.valueOf(c);
		
	}
	
	private void adaptVariable(CFG cfg,String element,char variable) {
		ArrayList<Variable> variable_list=cfg.getVariables();
		for(int i=0;i<variable_list.size();i++) {			
			variable_list.get(i).updateRS(element,variable);//updating all variables rightside
		}
	}
	private void adaptVariable_breaking(CFG cfg,String element,char variable) {
		ArrayList<Variable> variable_list=cfg.getVariables();
		for(int i=0;i<variable_list.size();i++) {			
			variable_list.get(i).updateRS_breaking(element,variable);
		}
	}
	private ArrayList<String> getVariableNames(CFG cfg) {
		ArrayList<Variable> variable_list=cfg.getVariables();
		ArrayList<String> variable_names=new ArrayList<String>();
		for(int i=0;i<variable_list.size();i++) {
			variable_names.add(variable_list.get(i).getName());
		}
		return variable_names;
	}
	private void breakVariable(CFG cfg) {
		ArrayList<Variable> variable_list=cfg.getVariables();
		ArrayList<String> variable_names=new ArrayList<String>();
		for(int i=0;i<variable_list.size();i++) {
			variable_names.add(variable_list.get(i).getName());
		}
		for(int i=0;i<variable_list.size();i++) {
			ArrayList<String> right_side=variable_list.get(i).getRight_side();
			for(int j=0;j<right_side.size();j++) {
				String rs_element=right_side.get(j);
				String temp_str="";
				if(rs_element.length()>2) {
					int len=rs_element.length();
					for(int k=0;k<len;k++) {
						temp_str+=rs_element.charAt(k);
						if(k%2!=0) {
							String variableName=chooseLetter(getVariableNames(cfg));
							cfg.addVariable(new Variable(variableName,temp_str));
							adaptVariable_breaking(cfg,temp_str,variableName.charAt(0));
							temp_str="";					
						}
					}
				}
			}
		}		
		if(!checkVCount(cfg)) {
			breakVariable(cfg);
		}else {
			displayCFG(cfg);
		}		
	}
	private boolean checkVCount(CFG cfg) {
		ArrayList<Variable> variable_list=cfg.getVariables();
		boolean flag=true;
		for(int i=0;i<variable_list.size();i++) {
			ArrayList<String>RS=variable_list.get(i).getRight_side();
			for(int j=0;j<RS.size();j++) {
				if(RS.get(j).length()>2) {
					flag=false;
					break;
				}
			}
			if(!flag) {
				break;
			}
		}
		return flag;
	}
	
	private boolean checkingAlphabet(CFG cfg) {//controlling alphabet input
		ArrayList<Variable> variable_list=cfg.getVariables();
		ArrayList<String> alphabet=cfg.getAlphabet();
		ArrayList<String> variable_names=new ArrayList<String>();
		ArrayList<String> terminals=new ArrayList<String>();
		boolean flag=true;
		for(int i=0;i<variable_list.size();i++) {
			variable_names.add(variable_list.get(i).getName());
		}
		for(int i=0;i<variable_list.size();i++) {
			variable_list.get(i).findingTerminals(variable_names, terminals);
		}
		for(int i=0;i<alphabet.size();i++) {
			if(!terminals.contains(alphabet.get(i))) {
				flag=false;
			}
		}
		return flag;
	}
}
