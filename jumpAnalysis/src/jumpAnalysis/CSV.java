package jumpAnalysis;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.io.*;
import java.util.StringTokenizer;

public class CSV {
	public List<List<Double>> data;
    private final SimpleDateFormat logTimeSdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS", Locale.JAPAN);
	CSV(String filename){
        try {
        	FileReader in = new FileReader(filename);
        	BufferedReader br = new BufferedReader(in);
        	String line = br.readLine();
        	data = new ArrayList<List<Double>>();
        	while ((line = br.readLine()) != null) {
        		if (line.contains("Date")) {
        			//skip header
        			continue;
        		}
        		List<Double> data1 = new ArrayList<Double>();
        		String[] columns = line.split(",");
        		double second = (double)(logTimeSdf.parse(columns[0]).getTime()) / 1000;
        		data1.add(second);
        		for (int i = 1; i < columns.length; i++) {
        			data1.add(Double.valueOf(columns[i]));
        		}
        		data.add(data1);
        	}
        	br.close();
        	in.close();
        } catch (IOException e) {
        	System.out.println(e);
        } catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
