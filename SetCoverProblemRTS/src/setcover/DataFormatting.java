package setcover;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import sampling.ConfigurationsSC;

public class DataFormatting {
	int [][] matrixCovering ;
	String [] idsActions;
	HashMap<String, List<Integer>> data;
	
	public DataFormatting(HashMap<String, List<Integer>> data)
	{
		this.data=data;
	}
	
	public void fillMatrix()
	{
		matrixCovering =new int [data.size()][ConfigurationsSC.TOTAL_SCRIPTS];
		idsActions=new String[data.size()];
		int counterIds=0;
	    
	    //Criate list of actions
	    for (Map.Entry<String, List<Integer>> pair : data.entrySet()) {
	
	        System.out.println(pair.getKey() + " = " + pair.getValue());
	        idsActions[counterIds]=(String)pair.getKey();
	        counterIds++;
	        //System.out.println(Arrays.toString(((List<String>) pair.getValue()).toArray()));

	    }
	    
	    //Fill Matrix with zeros
	    for(int i=0;i<data.size();i++)
	    {
	    	for(int j=0;j<ConfigurationsSC.TOTAL_SCRIPTS;j++)
	    	{	
	    		matrixCovering[i][j]=0;
	    	}
	    }
	    
	  //Update the matrix with current actions //CHECK ORDER IDS HERE!!!
	    for(int i=0;i<data.size();i++)
	    {
	    	List<Integer> l= data.get(idsActions[i]);
			for (Integer e: l) {
			    //System.out.println(e);
			    matrixCovering[i][e]=1;
			}
	    }
	}
	
	public void printMatrix()
	{
	    for(int i=0;i<data.size();i++)
	    {
	    	for(int j=0;j<ConfigurationsSC.TOTAL_SCRIPTS;j++)
	    	{
	    		System.out.print(matrixCovering[i][j]+" ");
	    		
	    	}
	    	System.out.println("");
	    }
	}
	


}
