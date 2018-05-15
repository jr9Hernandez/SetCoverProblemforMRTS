package setcover;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class RunSetCover {

	public static void main(String[] args) throws FileNotFoundException, IOException {
		
		Data objData=new Data();
		
		HashMap<String, List<Integer>> data=new HashMap<String, List<Integer>>();
		data=objData.loadDataFromSampling();
		objData.printDataMap(data);
		//System.out.println(data.size());
		DataFormatting sc=new DataFormatting(data);
		sc.fillMatrix();
		sc.printMatrix();		
		
		/*File[] files = new File("samplings").listFiles();		
		List<List<String>> data=objData.loadDataFromSampling(files);
		objData.printDataList(data);*/
		List<Integer> setCover=new ArrayList<Integer>();
		HillClimbing hc=new HillClimbing(sc.matrixCovering, sc.idsActions);
		setCover=hc.doHillCLimbing();
		System.out.println(setCover);

	}

}
