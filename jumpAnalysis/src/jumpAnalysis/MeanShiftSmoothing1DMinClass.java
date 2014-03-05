package jumpAnalysis;

import optimization.Uncmin_methods;
import jumpAnalysis.OneDdata;

public class MeanShiftSmoothing1DMinClass  implements Uncmin_methods{
	//	members
	public OneDdata data;
	public double dt, dx;
	public double threashold;
	double [] xpls = new double[2+1];	//	+1 is needed for the implementation of fortran
	double [] fpls = new double[1+1];
	double [] gpls = new double[2+1];
	int [] itrmcd = new int [1+1];
    double[][] a = new double[2+1][2+1];
    double[] udiag = new double[2+1];
    double tnow;
    int index_min, index_max;
	
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
		double x_ = x[1];
		for(int i = index_min; i < index_max; ++i){
			double delta_x =  (x_   - data.getXi(i))/dx;
			double delta_t =  (tnow - data.getTi(i))/dt;
			sum -= kernel(delta_x) * kernel(delta_t);//	evaluation function
		}
		return sum;
	 }


	public void gradient(double x[], double g[]){
		double sum=0.0;
		double x_ = x[1];
		for(int i = index_min; i < index_max; ++i){
			double delta_x =  (x_   - data.getXi(i))/dx;
			double delta_t =  (tnow - data.getTi(i))/dt;
			sum += kernel(delta_t) *dkernel_dt(delta_x);//	evaluation function
		}
		g[1] = sum/dx;
		return;
	 }

	 public void hessian(double x[], double h[][]){
		double sum=0.0;
		double x_ = x[1];
		for(int i = index_min; i < index_max; ++i){
			double delta_x =  (x_   - data.getXi(i))/dx;
			double delta_t =  (tnow - data.getTi(i))/dt;
			sum -= kernel(delta_t) *d2kernel_dt2(delta_x);//	evaluation function
		}
		h[1][1] = sum/(dx*dx); 
		return;
	 }
}
