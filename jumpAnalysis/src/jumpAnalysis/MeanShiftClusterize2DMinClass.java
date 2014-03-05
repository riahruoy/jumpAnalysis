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
    //	kernel:	 2nd order b-spline
    public static double kernel(double x_){
    	if(x_ <-1.5) return 0.0;
    	else if(x_ <-0.5) return (x_+1.5)*(x_+1.5);
    	else if(x_ < 0.5) return -x_*x_ + 1.25; 
      	else if(x_ < 1.5) return (x_-1.5)*(x_-1.5);
      	else return 0.0;
	}
    public static double dkernel_dt(double x_){
    	if(x_ <-1.5) return 0.0;
    	else if(x_ <-0.5) return 2.0*(x_+1.5);
    	else if(x_ < 0.5) return -2.0*x_; 
      	else if(x_ < 1.5) return 2.0*(x_-1.5);
      	else return 0.0;
	}
    public static double d2kernel_dt2(double x_){
    	if(x_<-1.5) return 0.0;
    	else if(x_ < -0.5) return 2.0; 
    	else if(x_ < 0.5) return -2.0; 
      	else if(x_ < 1.5) return 2.0;
      	else return 0.0;
	}

	//	function to be minimized
	public double f_to_minimize(double x[]){
		double sum=0.0;
		double t_ = x[0+1];
		double x_ = x[1+1];
		double t_width = 1.5*dt;
		double tmin = t_ - t_width;
		double tmax = t_ + t_width;
		if(tmax > data.getTi(data.length-1)) tmax = data.getTi(data.length-1);
		for(int i = data.getFloorIndex(tmin); data.getTi(i) < tmax; ++i){
			double delta_x =  (x_ - data.getXi(i))/dx;
			double delta_t =  (t_ - data.getTi(i))/dt;
			sum -= kernel(delta_x) * kernel(delta_t);//	evaluation function
		}
		return sum;
	 }


	public void gradient(double x[], double g[]){
		double sum_t=0.0;
		double sum_x=0.0;
		double t_ = x[0+1];
		double x_ = x[1+1];
		double t_width = 1.5*dt;
		double tmin = t_ - t_width;
		double tmax = t_ + t_width;
		if(tmax > data.getTi(data.length-1)) tmax = data.getTi(data.length-1);
		for(int i = data.getFloorIndex(tmin); data.getTi(i) < tmax; ++i){
			double delta_x =  (x_ - data.getXi(i))/dx;
			double delta_t =  (t_ - data.getTi(i))/dt;
			sum_t += dkernel_dt(delta_t)*kernel(delta_x) ;//	evaluation function
			sum_x += kernel(delta_t)*dkernel_dt(delta_x) ;//	evaluation function
		}
		g[0+1] = sum_t/dt;
		g[1+1] = sum_x/dx;
		return;
	 }

	 public void hessian(double x[], double h[][]){
		double sum_tt=0.0;
		double sum_tx=0.0;
		double sum_xx=0.0;
		double t_ = x[0+1];
		double x_ = x[1+1];
		double t_width = 1.5*dt;
		double tmin = t_ - t_width;
		double tmax = t_ + t_width;
		if(tmax > data.getTi(data.length-1)) tmax = data.getTi(data.length-1);
		for(int i = data.getFloorIndex(tmin); data.getTi(i) < tmax; ++i){
			double delta_x =  (x_ - data.getXi(i))/dx;
			double delta_t =  (t_ - data.getTi(i))/dt;
			sum_tt -= d2kernel_dt2(delta_t) * kernel(delta_x) /(dt*dt); 
			sum_tx -= dkernel_dt(delta_t) * dkernel_dt(delta_x) /(dt*dx); 
			sum_tt -= kernel(delta_t) * d2kernel_dt2(delta_x) /(dx*dx); 
		}
		h[0+1][0+1] = sum_tt; 
		h[0+1][1+1] = sum_tx; 
		h[1+1][0+1] = sum_tx; 
		h[1+1][1+1] = sum_xx; 
		return;
	 }

}
