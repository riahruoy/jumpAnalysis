package jumpAnalysis;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class outputItx{
	outputItx(){}
	
	public static boolean writeItx(double[] src, String filename, String wavename){
	    try{
	       File file = new File(filename);
	       file.createNewFile();
	       if (checkBeforeWritefile(file)){
	          FileWriter filewriter = new FileWriter(file);

	          filewriter.write("IGOR\nWAVES/O/D/N=(");
	          filewriter.write(src.length + ") " + wavename +"\nBEGIN\n");
	          
	          for(int i=0; i<src.length; ++i){
	        	  filewriter.write(src[i]+"\n");
	          }
	          filewriter.write("END\n");

	          filewriter.close();
	        }else{
	          System.out.println("error in writing " + filename);
	        }
	      }catch(IOException e){
	        System.out.println(e);
	      }
	    return true;
	}

	private static boolean checkBeforeWritefile(File file){
		if (file.exists()){
			if (file.isFile() && file.canWrite()){
				return true;
			}
		}
		return false;
	}
}
