package jumpAnalysis;
import optimization.*;
import jumpAnalysis.MeanShiftClusterize2DMinClass;

public class MeanShiftClusterize2D{
	//	set into the class member
	public void set(double[] x0_, double[] x1_, double dx0_, double dx1_, double threashold_){
		minClass = new MeanShiftClusterize2DMinClass();
		minClass.x0 = (double[])x0_.clone();
		minClass.x1 = (double[])x1_.clone();
		minClass.dx0 = dx0_;
		minClass.dx1 = dx1_;
		minClass.threashold = threashold_;
		x0_rslt = new double[minClass.x0.length];
		x1_rslt = new double[minClass.x0.length];
		return ;
	}
	//	run the optimization from every initial points
	public void run(){
		for(int i=0; i<minClass.x0.length; ++i){
			run1(minClass.x0[i], minClass.x1[i]);
			x0_rslt[i] = minClass.xpls[0+1];
			x1_rslt[i] = minClass.xpls[1+1];
		}
		return;
	}
	//	run the optimization from a certain initial point
	private void run1(double x0Init, double x1Init){
		double [] xinit = {0.0, x0Init, x1Init};
		Uncmin_f77.optif0_f77(2, xinit, minClass, minClass.xpls, minClass.fpls, minClass.gpls, minClass.itrmcd, minClass.a, minClass.udiag);
		return;
	}
	public double [] getResult(){return x1_rslt;} 

	// members
	private double[] x0_rslt, x1_rslt;
	private MeanShiftClusterize2DMinClass minClass; 
	
		
};
