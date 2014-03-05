package jumpAnalysis;
import optimization.*;
import jumpAnalysis.MeanShiftSmoothing1DMinClass;
import jumpAnalysis.OneDdata;

public class MeanShiftSmoothing1D{
	//	set into the class member
	public void set(OneDdata src, double dx0_, double dx1_, double threashold_){
		minClass = new MeanShiftSmoothing1DMinClass();
		minClass.data = src;
		minClass.dt = dx0_;
		minClass.dx = dx1_;
		minClass.threashold = threashold_;
		x_rslt = new double[minClass.data.length];
		return ;
	}
	//	run the optimization from every initial points
	public void run(){
		for(int i=0; i<minClass.data.length; ++i){
			run1(i, minClass.data.getXi(i));
			x_rslt[i] = minClass.xpls[0+1];
		}
		rslt = new OneDdata(minClass.data.getT(), x_rslt);
		return;
	}
	public OneDdata getResult1D(){return rslt;}
	//	run the optimization from a certain initial point
	private void run1(int index, double x1Init){
		minClass.tnow = minClass.data.getTi(index);
		minClass.index_min = minClass.data.getFloorIndex(minClass.tnow - 1.5*minClass.dt);
		minClass.index_max = minClass.data.getFloorIndex(minClass.tnow + 1.5*minClass.dt)+1;
		if(minClass.index_max > minClass.data.length) minClass.index_max = minClass.data.length;
		double [] xinit = {0.0, x1Init};
		Uncmin_f77.optif0_f77(1, xinit, minClass, minClass.xpls, minClass.fpls, minClass.gpls, minClass.itrmcd, minClass.a, minClass.udiag);
		return;
	}
	public double [] getResult(){return x_rslt;} 

	// members
	private double[] x_rslt;
	private MeanShiftSmoothing1DMinClass minClass; 
	private OneDdata rslt;
		
};
