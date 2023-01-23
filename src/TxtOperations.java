import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class TxtOperations {
	private boolean correctInput;
	public CFG createCFG() {
		File file = new File("CFG.txt");
		CFG cfg=new CFG();
		correctInput=true;
		try {
			BufferedReader br= new BufferedReader(new FileReader(file));
			String str;
			try {
				System.out.println("CFG Form");
				while ((str = br.readLine()) != null) {
					System.out.println(str);
					String[] strarr;
					if(str.contains("=")){
						str=str.replace("=", " ").replace(",", " ");
						strarr=str.split(" ");
						for(int i=0;i<strarr.length;i++) {
							if(i>0)
								cfg.addAlphabet(strarr[i]);
						}
					}else if(str.contains("-")) {
						str=str.replace("-", " ").replace("|", " ");
						strarr=str.split(" ");
						for(int i=0;i<strarr.length;i++) {
							if(i>0&&strarr[0].equals("S")&&strarr[i].contains("S")) {//for S contains S in rightside
								Variable s0_variable=new Variable("S0");
								cfg.addVariable(s0_variable);
								s0_variable.addRS("S");
							}
						}
						Variable temp_variable=new Variable(strarr[0]);
						cfg.addVariable(temp_variable);
						for(int i=0;i<strarr.length;i++) {
							if(i>0)
								temp_variable.addRS(strarr[i]);
						}
					}else {
						System.out.println("Incorrect input");
						correctInput=false;
						break;
					}
				}
				
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return cfg;
	}
	public boolean isCorrectInput() {
		return correctInput;
	}
	
	
}
