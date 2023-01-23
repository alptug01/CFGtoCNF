import java.util.ArrayList;
public class Variable {
	private String name;
	private ArrayList<String> right_side;
	private boolean hadEpsilon;
	private ArrayList<String> epsilon_possibilities;
	public Variable(String name){
		this.name=name;
		right_side=new ArrayList<String>();
		hadEpsilon=false;
	}
	public Variable(String name,String rs_element){
		this.name=name;
		right_side=new ArrayList<String>();
		hadEpsilon=false;
		right_side.add(rs_element);
	}
	
	public void addRS(String obj) {
		right_side.add(obj);
	}
	public void removeRS(Object obj) {
		right_side.remove(obj);
	}
	
	public Object searchingRS(Object obj) {//searching Rightside of variable
		Object tempobj = null;
		for(int i=0;i<right_side.size();i++) {
			if(right_side.get(i).equals(obj)) {
				tempobj=obj;
			}
		}
		return tempobj;
	}
	public boolean searchingRS(String obj) {//searching RS for string parameter
		boolean flag=false;
		for(int i=0;i<right_side.size();i++) {
			if((right_side.get(i)).contains(obj)) {
				flag=true;
			}
		}
		return flag;
	}
	public void displayRS() {//display RS
		int rs_size=right_side.size();
		for(int i=0;i<rs_size;i++) {
			if(i!=rs_size-1)
				System.out.print(right_side.get(i)+"|");
			else
				System.out.print(right_side.get(i));
		}
	}
	public void epsilonRS(String obj) {
		if(name.equals(obj)) {
			right_side.remove("€");
		}
		int rs_size=right_side.size();
		for(int i=0;i<rs_size;i++) {
			if((right_side.get(i)).contains(obj)) {
				String origin_str=(right_side.get(i));
				int str_len=origin_str.length();
				String temp_str=origin_str;
				char obj_ch=obj.charAt(0);
				if(str_len==1 && !hadEpsilon) {//deleting one element in RS
					right_side.add("€");
					hadEpsilon=true;
				}
				else {
					ArrayList<Integer> locations = new ArrayList<Integer>();;
					for(int j=0;j<str_len;j++) {//getting the locations of the element to be deleted
						if(origin_str.charAt(j)==obj_ch) {
							locations.add(j);
						}					
					}
					Integer arr[] = new Integer[locations.size()];
					arr = locations.toArray(arr);
					epsilon_possibilities=new ArrayList<String>();
			        for(int j=1;j<arr.length+1;j++) {
			        	epsilonCombination(arr, arr.length, j);//sending locations arrlist for combination
			        }
			        for(int j=0;j<epsilon_possibilities.size();j++) {
			        	String str=epsilon_possibilities.get(j);
			        	StringBuilder sb = new StringBuilder(temp_str);
			        	for(int k=0;k<str.length();k++) {
			        		char str_element=str.charAt(k);
			        		int index = Integer.parseInt(String.valueOf(str_element));
			        		sb.deleteCharAt(index-k);//deleting char of string according to indexes with combination
			        		String resultString = sb.toString();
			        		if(resultString=="" && !hadEpsilon) {
								right_side.add("€");
								hadEpsilon=true;
							}
			        		else if(resultString!=""&&!right_side.contains(resultString))
								right_side.add(resultString);	        		
			        	}
			        }
				}			
			}
		}		
	}
	
    private void combinationUtil(Integer[] arr, Integer[] data, int start,
                                int end, int index, int r)
    {
        if (index == r)
        {
        	String temp_str="";
            for (int j=0; j<r; j++)
                temp_str+=(int)data[j];
            epsilon_possibilities.add(temp_str);
            return;
        }
        for (int i=start; i<=end && end-i+1 >= r-index; i++)
        {
            data[index] = arr[i];
            combinationUtil(arr, data, i+1, end, index+1, r);
        }
    }
 
    // The main function that prints all combinations of size r
    // in arr[] of size n. This function mainly uses combinationUtil()
    private void epsilonCombination(Integer arr[], int n, int r)
    {
        // A temporary array to store all combination one by one
        Integer data[]=new Integer[r];
        // Print all combination using temporary array 'data[]'
        combinationUtil(arr, data, 0, n-1, 0, r);
    }

	public void setHadEpsilon(boolean hadEpsilon) {
		this.hadEpsilon = hadEpsilon;
	}

	public String getName() {
		return name;
	}

	public ArrayList<String> getRight_side() {
		return right_side;
	}

	public void addRSwithRS(ArrayList<String> arr) {
		for(int i=0;i<arr.size();i++) {
			if(!right_side.contains(arr.get(i))) {
				right_side.add(arr.get(i));
			}
		}
	}
	public void findingTerminals(ArrayList<String> variablenames,ArrayList<String> nonterminals) {
		for(int i=0;i<right_side.size();i++) {//finding terminals and adding to arrlist
			String rs_element=right_side.get(i);
			for(int j=0;j<rs_element.length();j++) {
				String str=String.valueOf(rs_element.charAt(j));
				if(!variablenames.contains(str)&&!nonterminals.contains(str)) {
					nonterminals.add(str);
				}
			}
		}
	}
	public void updateRS(String element,char variable) {
		for(int i=0;i<right_side.size();i++) {
			boolean flag=false;
			String rs_element=right_side.get(i);
			if(rs_element.length()>1) {
				StringBuilder sb = new StringBuilder(rs_element);
				for(int j=0;j<rs_element.length();j++) {
					String str=String.valueOf(rs_element.charAt(j));
					if(str.equals(element)) {
						sb.setCharAt(j, variable);
						flag=true;
					}
				}
				if(flag) {
					right_side.set(i, sb.toString());
				}
			}
			
		}
	}
	public void updateRS_breaking(String element,char variable) {
		for(int i=0;i<right_side.size();i++) {
			boolean flag=false;
			String rs_element=right_side.get(i);
			if(rs_element.length()>2) {//adding RS for those with a length greater than 2
				if(rs_element.contains(element)) {
					rs_element=rs_element.replace(element, String.valueOf(variable));
					flag=true;
				}
				if(flag) {
					right_side.set(i, rs_element);
				}
			}
			
		}
	}
	
}
