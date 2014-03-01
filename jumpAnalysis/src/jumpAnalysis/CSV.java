package jumpAnalysis;
import java.util.*;
import java.io.*;
import java.util.StringTokenizer;

public class CSV {
	public List<List<Double>> data;

	CSV(String filename){
        try {
        	FileReader in = new FileReader(filename);
        	BufferedReader br = new BufferedReader(in);
        	String line = br.readLine();
        	data = new ArrayList<List<Double>>();
        	while ((line = br.readLine()) != null) {
        		List<Double> data1 = new ArrayList<Double>();
            	StringTokenizer st = new StringTokenizer(line, ",");
            	while (st.hasMoreTokens()) 
        	         data1.add(Double.valueOf(st.nextToken()));
        		data.add(data1);
        	}
        	br.close();
        	in.close();
        } catch (IOException e) {
        	System.out.println(e);
        }
	}
}
