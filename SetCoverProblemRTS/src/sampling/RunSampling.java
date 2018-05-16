package sampling;

import java.io.File;

public class RunSampling {
	
	static DataRecollection dataRecollection;
	
	public static void main(String[] args) {
		
		//Here we collect the data
		dataRecollection=new DataRecollection();
		dataRecollection.dataRecollection();
		
		
		//Here we sample
		
		File[] files = new File("logs_states").listFiles();
	    sampling(files);
	    
	    System.out.println("Fim");
		
		
	}
	
	public static void sampling(File[] files) {
	    for (File file : files) {
	            //System.out.println("Directory: " + file.getName());
	            //sampling(file.listFiles()); // Calls same method again.
	        	dataRecollection.sampling(file.getName(), file.listFiles().length);

	    }
	}

}
