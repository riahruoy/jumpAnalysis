package jumpAnalysis;

import optimization.Uncmin_methods;
import jumpAnalysis.OneDdata;

public class MeanShiftClusterize2DMinClass  implements Uncmin_methods{
	//	members
	public OneDdata data;
	public double dt, dx;
	public double threashold;
	double [] xpls = new double[2+1];	//	+1 is needed for the implementation of fortran
	double [] fpls = new double[1+1];
	double [] gpls = new double[2+1];
	int [] itrmcd = new int [1+1];
    double a[][] = new double[2+1][2+1];
    double udiag[] = new double[2+1];
	
    //	kernel:	 2nd order b-spline
    public static double kernel(double x_){
    	if(x_ < 0.5) return -x_*x_ + 1.25; 
      	else if(x_ < 1.5) return (x_-1.5)*(x_-1.5);
      	else return 0.0;
	}
    public static double dkernel_dt(double x_){
    	if(x_ < 0.5) return -2.0*x_; 
      	else if(x_ < 1.5) return 2.0*(x_-1.5);
      	else return 0.0;
	}
    public static double d2kernel_dt2(double x_){
    	if(x_ < 0.5) return -2.0; 
      	else if(x_ < 1.5) return 2.0;
      	else return 0.0;
	}

	//	function to be minimized
	public double f_to_minimize(double x[]){
		double sum=0.0;
		double t_ = x[0+1];
		double x_ = x[1+1];
		double delta_t = 1.5*dt;
		double tmin = t_ - delta_t;
		double tmax = t_ + delta_t;
		if(tmax > data.getTi(data.length-1)) tmax = data.getTi(data.length-1);
		for(int i = data.getFloorIndex(tmin); data.getTi(i) < tmax; ++i){
			double d2 = (t_ - data.getTi(i))*(t_ - data.getTi(i))/(dt*dt) + (x_ - data.getXi(i))*(x_ - data.getXi(i))/(dx*dx);
			sum -= kernel(Math.sqrt(d2));//	evaluation function
		}
		return sum;
	 }


	public void gradient(double x[], double g[]){
		double sum_t=0.0;
		double sum_x=0.0;
		double t_ = x[0+1];
		double x_ = x[1+1];
		double delta_t = 1.5*dt;
		double tmin = t_ - delta_t;
		double tmax = t_ + delta_t;
		if(tmax > data.getTi(data.length-1)) tmax = data.getTi(data.length-1);
		for(int i = data.getFloorIndex(tmin); data.getTi(i) < tmax; ++i){
			double d2 = (t_ - data.getTi(i))*(t_ - data.getTi(i))/(dt*dt) + (x_ - data.getXi(i))*(x_ - data.getXi(i))/(dx*dx);
			double d = Math.sqrt(d2);
			double dkernel_D = dkernel_dt(d);
			sum_t -= dkernel_D * (t_- data.getTi(i))/d;//	evaluation function
			sum_x -= dkernel_D * (x_- data.getXi(i))/d;//	evaluation function
		}
		g[0+1] = sum_t/dt/dt;
		g[1+1] = sum_x/dx/dx;
		return;
	 }

	 public void hessian(double x[], double h[][]){
		double sum_tt=0.0;
		double sum_tx=0.0;
		double sum_xx=0.0;
		double t_ = x[0+1];
		double x_ = x[1+1];
		double delta_t = 1.5*dt;
		double tmin = t_ - delta_t;
		double tmax = t_ + delta_t;
		if(tmax > data.getTi(data.length-1)) tmax = data.getTi(data.length-1);
		for(int i = data.getFloorIndex(tmin); data.getTi(i) < tmax; ++i){
			double d2 = (t_ - data.getTi(i))*(t_ - data.getTi(i))/(dt*dt) + (x_ - data.getXi(i))*(x_ - data.getXi(i))/(dx*dx);
			double D = Math.sqrt(d2);
			double dDdt = (t_- data.getTi(i))/D;
			double dDdx = (x_- data.getXi(i))/D;
			double ddDdtdt = (dt*dt*d2 - (t_- data.getTi(i))*(t_- data.getTi(i)))/(d2*D);
			double ddDdxdx = (dx*dx*d2 - (x_- data.getXi(i))*(x_- data.getXi(i)))/(d2*D);
			double ddDdtdx = (         - (t_- data.getTi(i))*(x_- data.getXi(i)))/(d2*D);
			double dkernel_D = dkernel_dt(D);
			double d2kernel_D2 = d2kernel_dt2(D);
			sum_tt -= d2kernel_D2 * dDdt * dDdt + dkernel_D * ddDdtdt;//	evaluation function
			sum_xx -= d2kernel_D2 * dDdx * dDdx + dkernel_D * ddDdxdx;//	evaluation function
			sum_tt -= d2kernel_D2 * dDdt * dDdx + dkernel_D * ddDdtdx;//	evaluation function
		}
		h[0+1][0+1] = sum_tt/(dt*dt*dt*dt); 
		h[0+1][1+1] = sum_tx/(dt*dx*dt*dx); 
		h[1+1][0+1] = sum_tx/(dt*dx*dt*dx); 
		h[1+1][1+1] = sum_xx/(dx*dx*dx*dx); 
		return;
	 }

}
