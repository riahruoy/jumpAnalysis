package jumpAnalysis;
import java.util.ArrayList;
import java.util.List;

import jumpAnalysis.ThreeDdata;
import jumpAnalysis.MeanShiftSmoothing3D;

public class AccelData extends ThreeDdata{
	AccelData(){};
	
	
	public void detectJump(double smoothing_dt, double smoothing_dg, double g_threashold, double smallestDuration){
        //	1st smoothing 
        MeanShiftSmoothing3D msc3D = new MeanShiftSmoothing3D ();
        msc3D.set(this, smoothing_dt, smoothing_dg, smoothing_dg, smoothing_dg, 1.0);
        msc3D.run();
        //	2nd smoothing 
        MeanShiftSmoothing3D msc3D_2 = new MeanShiftSmoothing3D ();
        msc3D_2.set(msc3D.getResult3D(), smoothing_dt, smoothing_dg, smoothing_dg, smoothing_dg, 1.0);
        msc3D_2.run();
        
        data_smth = new ThreeDdata(msc3D_2.getResult3D());
//        data_smth = new ThreeDdata(msc3D.getResult3D());
        detectJumpCore(g_threashold, smallestDuration);
	}
	
	public double[] getJumpDistance(OneDdata vamp){
		jump_distance = new double[jump_t_start.size()];
		for(int i=0; i<jump_distance.length; ++i){
			double v0 = vamp.get(jump_t_start.get(i));
			double vx = v0 * 0.5;
			
			double g = 9.8;	// m/s
//			double vz = 0.5 * g * jump_t_duration.get(i);
//			double vx = Math.sqrt(v*v - vz*vz);
			jump_distance[i] = vx*jump_t_duration.get(i);
		}
		return jump_distance;
	}
	
	private void detectJumpCore(double g_threashold, double smallestDuration){
		jump_t_start = new ArrayList<Double>();
		jump_t_duration = new ArrayList<Double>();
/*		double avg = 0.0;
		for(int i=0; i<length; ++i)
			avg += data_smth.getAmpi(i);
		avg /= length;
*/		
		for(int i=0; i<length-1; ++i){
			if(data_smth.getAmpi(i) > g_threashold && g_threashold >= data_smth.getAmpi(i+1) ){
				int j;
				for(j=i+1; j<length; ++j){
					if(data_smth.getAmpi(j) > g_threashold){
						break;
					}
				}
				if(j<length-1){
					double duration = data_smth.getTi(j) - data_smth.getTi(i);
					if(smallestDuration <= duration){
						jump_t_start.add(data_smth.getTi(i));
						jump_t_duration.add(duration);
					}
					i = j-1;
				}
			}
		}
	}
	private ThreeDdata data_smth;
	private List<Double> jump_t_start, jump_t_duration;
	private double [] jump_distance;
	public ThreeDdata getDataSmth(){return data_smth;}
	public double[] getJump_t_start(){
		double[] jumpTstart = new double [jump_t_start.size()];
		for(int i=0; i<jump_t_start.size(); ++i)
			jumpTstart[i] = jump_t_start.get(i);
		return jumpTstart;
	}
	public double[] getJump_t_duration(){
		double[] jumpTduration = new double [jump_t_duration.size()];
		for(int i=0; i<jump_t_duration.size(); ++i)
			jumpTduration[i] = jump_t_duration.get(i);
		return jumpTduration;
	}
	public double[] getXsmth(){return data_smth.getX();}
	public double[] getYsmth(){return data_smth.getY();}
	public double[] getZsmth(){return data_smth.getZ();}
}
