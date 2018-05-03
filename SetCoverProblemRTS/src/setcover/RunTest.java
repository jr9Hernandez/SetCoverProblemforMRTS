package setcover;

import java.io.File;

public class RunTest {
	
	static SetCover setCover;
	
	public static void main(String[] args) {
		
		//Here we collect the data
		setCover=new SetCover();
		setCover.dataRecollection();
		
		
		//Here we sample
		
		File[] files = new File("logs_states").listFiles();
	    sampling(files);
		
		
	}
	
	public static void sampling(File[] files) {
	    for (File file : files) {
	            //System.out.println("Directory: " + file.getName());
	            //sampling(file.listFiles()); // Calls same method again.
	        	setCover.sampling(file.getName(), file.listFiles().length);

	    }
	}

}
