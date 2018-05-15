package setcover;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;

public class RunSetCover {

	public static void main(String[] args) throws FileNotFoundException, IOException {
		
		Data objData=new Data();
		HashMap<String, List<String>> data=new HashMap<String, List<String>>();
		data=objData.loadDataFromSampling();
		objData.printData(data);

	}

}
