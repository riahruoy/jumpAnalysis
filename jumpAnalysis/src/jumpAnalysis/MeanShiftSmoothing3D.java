package jumpAnalysis;
import optimization.*;
import jumpAnalysis.MeanShiftSmoothing3DMinClass;
import jumpAnalysis.ThreeDdata;

public class MeanShiftSmoothing3D{
	//	set into the class member
	public void set(ThreeDdata src, double dt_, double dx_, double dy_, double dz_, double threashold_){
		minClass = new MeanShiftSmoothing3DMinClass();
		minClass.data = src;
		minClass.dt = dt_;
		minClass.dx = dx_;
		minClass.dy = dy_;
		minClass.dz = dz_;
		minClass.threashold = threashold_;
		x_rslt = new double[minClass.data.length];
		y_rslt = new double[minClass.data.length];
		z_rslt = new double[minClass.data.length];
		return ;
	}
	//	run the optimization from every initial points
	public void run(){
		for(int i=0; i<minClass.data.length; ++i){
			run1(i, minClass.data.getXi(i), minClass.data.getYi(i), minClass.data.getZi(i));
			x_rslt[i] = minClass.xpls[0+1];
			y_rslt[i] = minClass.xpls[1+1];
			z_rslt[i] = minClass.xpls[2+1];
		}
		rslt = new ThreeDdata(minClass.data.getT(), x_rslt, y_rslt, z_rslt);
		return;
	}
	public ThreeDdata getResult3D(){return rslt;}

	//	run the optimization from a certain initial point
	private void run1(int index, double xInit, double yInit, double zInit){
		minClass.tnow = minClass.data.getTi(index);
		minClass.index_min = minClass.data.getFloorIndex(minClass.tnow - 1.5*minClass.dt);
		minClass.index_max = minClass.data.getFloorIndex(minClass.tnow + 1.5*minClass.dt)+1;
		if(minClass.index_max > minClass.data.length) minClass.index_max = minClass.data.length;
		double [] xinit = {0.0, xInit, yInit, zInit};
		Uncmin_f77.optif0_f77(3, xinit, minClass, minClass.xpls, minClass.fpls, minClass.gpls, minClass.itrmcd, minClass.a, minClass.udiag);
		return;
	}
	public double [] getResult(){return x_rslt;} 

	// members
	private double[] x_rslt, y_rslt, z_rslt;
	private MeanShiftSmoothing3DMinClass minClass; 
	private ThreeDdata rslt;
		
};
