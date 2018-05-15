package setcover;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Data {
	
	HashMap<String, List<String>> data; 
	
	public Data() 
	{
		data=new HashMap<String, List<String>>();
	}
	
	public HashMap<String, List<String>> loadDataFromSampling() throws FileNotFoundException, IOException
	{
		List<String> scripts;
		File[] files = new File("samplings").listFiles();
	    for(int i=0;i<files.length;i++)
	    {
	    	String fileName=files[i].getName();
	    	try (BufferedReader br = new BufferedReader(new FileReader(files[i]))) {
	    	    String line;
	    	    int counterLine=0;
	    	    while ((line = br.readLine()) != null) {
	    	       // process the line.
	    	    	Matcher m = Pattern.compile("\\<(.*?)\\>").matcher(line);
	    	        while(m.find()) {
	    	          //System.out.println(fileName+"_"+m.group(1));  
	    	        	fileName=fileName.replace(".txt","");
	    	        	scripts=data.get(fileName+"_"+m.group(1));
	    	        	if(scripts==null)
	    	        	{
	    	        		scripts=new ArrayList<String>();
	    	        	}
	    	        	scripts.add(String.valueOf(counterLine));
	    	        	data.put(fileName+"_"+m.group(1), scripts);
	    	        	
	    	        }
	    	        counterLine++;
	    	    }
	    	}
	    }
	    return data;
	}
	
	public void printData(HashMap<String, List<String>> data)
	{
		System.out.println(data.size());
	    Iterator it = data.entrySet().iterator();
	    while (it.hasNext()) {
	        Map.Entry pair = (Map.Entry)it.next();
	        System.out.println(pair.getKey() + " = " + pair.getValue());
	        //System.out.println(Arrays.toString(((List<String>) pair.getValue()).toArray()));
	        it.remove(); // avoids a ConcurrentModificationException
	    }
	}

}
