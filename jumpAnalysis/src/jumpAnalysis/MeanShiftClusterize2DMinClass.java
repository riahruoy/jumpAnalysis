package jumpAnalysis;

import optimization.Uncmin_methods;

public class MeanShiftClusterize2DMinClass  implements Uncmin_methods{
	//	members
	public double[] x0, x1;
	public double dx0, dx1;
	public double threashold;
	double [] xpls = new double[2+1];	//	+1 is needed for the implementation of fortran
	double [] fpls = new double[1+1];
	double [] gpls = new double[2+1];
	int [] itrmcd = new int [1+1];
    double a[][] = new double[2+1][2+1];
    double udiag[] = new double[2+1];
	
	//	function to be minimized
	public double f_to_minimize(double x[]){
		double sum=0.0;
		double x0_ = x[0+1];
		double x1_ = x[1+1];
		for(int i=0; i<x0.length; ++i){
			double length = (x0_ - x0[i])*(x0_ - x0[i])/(dx0*dx0) + (x1_ - x1[i])*(x1_ - x1[i])/(dx1*dx1);
			sum += -Math.exp(-length);
		}
		return sum;
	 }
	 public void gradient(double x[], double g[]){
		double sum_x0=0.0;
		double sum_x1=0.0;
		double x0_ = x[0+1];
		double x1_ = x[1+1];
		for(int i=0; i<x0.length; ++i){
			double length = (x0_ - x0[i])*(x0_ - x0[i])/(dx0*dx0) + (x1_ - x1[i])*(x1_ - x1[i])/(dx1*dx1);
			sum_x0 += -Math.exp(-length)*(x0_ - x0_);
			sum_x1 += -Math.exp(-length)*(x1_ - x1_);
		}
		g[0+1] = sum_x0/dx0/dx0;
		g[1+1] = sum_x1/dx1/dx1;
		return;
	 }

	 public void hessian(double x[], double h[][]){
		double sum_x0x0=0.0;
		double sum_x0x1=0.0;
		double sum_x1x1=0.0;
		double x0_ = x[0+1];
		double x1_ = x[1+1];
		for(int i=0; i<x0.length; ++i){
			double length = (x0_ - x0[i])*(x0_ - x0[i])/(dx0*dx0) + (x1_ - x1[i])*(x1_ - x1[i])/(dx1*dx1);
			sum_x0x0 += -Math.exp(-length)*((x0[i]-x0_)*(x0[i]-x0_) - dx0*dx0);
			sum_x1x1 += -Math.exp(-length)*((x1[i]-x1_)*(x1[i]-x1_) - dx1*dx1);
			sum_x0x1 += -Math.exp(-length)*((x0[i]-x0_)*(x1[i]-x1_));
		}
		h[0+1][0+1] = sum_x0x0/(dx0*dx0); 
		h[1+1][1+1] = sum_x1x1/(dx1*dx1); 
		h[0+1][1+1] = sum_x0x1/(dx0*dx1); 
		h[1+1][0+1] = sum_x0x1/(dx0*dx1); 
		return;
	 }

}
