package jumpAnalysis;

import optimization.Uncmin_methods;
import jumpAnalysis.ThreeDdata;;

public class MeanShiftSmoothing3DMinClass  implements Uncmin_methods{
	//	members
	public ThreeDdata data;
	public double dt, dx, dy, dz;
	public double threashold;
	double [] xpls = new double[3+1];	//	+1 is needed for the implementation of fortran
	double [] fpls = new double[1+1];
	double [] gpls = new double[3+1];
	int [] itrmcd = new int [1+1];
    double[][] a = new double[3+1][3+1];
    double[] udiag = new double[3+1];
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
    public static double kernel2(double x_){
      	return Math.exp(-x_*x_);
	}
    public static double dkernel_dt(double x_){
      	return x_*Math.exp(-x_*x_);
	}
    public static double d2kernel_dt2(double x_){
      	return x_*x_*Math.exp(-x_*x_);
	}

	//	function to be minimized
	public double f_to_minimize(double x[]){
		double sum=0.0;
		double x_ = x[1];
		double y_ = x[2];
		double z_ = x[3];
		for(int i = index_min; i < index_max; ++i){
			double delta_x =  (x_   - data.getXi(i))/dx;
			double delta_y =  (y_   - data.getYi(i))/dy;
			double delta_z =  (z_   - data.getZi(i))/dz;
			double delta_t =  (tnow - data.getTi(i))/dt;
			sum -= kernel(delta_t) * kernel2(delta_x) * kernel2(delta_y) * kernel2(delta_z) ;//	evaluation function
		}
		return sum;
	 }


	public void gradient(double x[], double g[]){
		double sum_x=0.0;
		double sum_y=0.0;
		double sum_z=0.0;
		double x_ = x[1];
		double y_ = x[2];
		double z_ = x[3];
		for(int i = index_min; i < index_max; ++i){
			double delta_x =  (x_   - data.getXi(i))/dx;
			double delta_y =  (y_   - data.getYi(i))/dy;
			double delta_z =  (z_   - data.getZi(i))/dz;
			double delta_t =  (tnow - data.getTi(i))/dt;
			sum_x += kernel(delta_t) *dkernel_dt(delta_x) * kernel2(delta_y) * kernel2(delta_z);//	evaluation function
			sum_y += kernel(delta_t) *dkernel_dt(delta_y) * kernel2(delta_z) * kernel2(delta_x);//	evaluation function
			sum_z += kernel(delta_t) *dkernel_dt(delta_z) * kernel2(delta_x) * kernel2(delta_y);//	evaluation function
		}
		g[1] = sum_x/dx;
		g[2] = sum_y/dy;
		g[3] = sum_z/dz;
		return;
	 }

	 public void hessian(double x[], double h[][]){
		double sum_xx=0.0;
		double sum_xy=0.0;
		double sum_xz=0.0;
		double sum_yy=0.0;
		double sum_yz=0.0;
		double sum_zz=0.0;
		double x_ = x[1];
		double y_ = x[2];
		double z_ = x[3];
		for(int i = index_min; i < index_max; ++i){
			double delta_x =  (x_   - data.getXi(i))/dx;
			double delta_y =  (y_   - data.getYi(i))/dy;
			double delta_z =  (z_   - data.getZi(i))/dz;
			double delta_t =  (tnow - data.getTi(i))/dt;
			sum_xx -=  kernel(delta_t) *d2kernel_dt2(delta_x) * kernel2(delta_y) * kernel2(delta_z);//	evaluation function
			sum_yy -=  kernel(delta_t) *d2kernel_dt2(delta_y) * kernel2(delta_z) * kernel2(delta_x);//	evaluation function
			sum_zz -=  kernel(delta_t) *d2kernel_dt2(delta_z) * kernel2(delta_x) * kernel2(delta_y);//	evaluation function
			sum_xy -=  kernel(delta_t) *dkernel_dt(delta_x) * dkernel_dt(delta_y) * kernel2(delta_z);//	evaluation function
			sum_xz -=  kernel(delta_t) *dkernel_dt(delta_x) * dkernel_dt(delta_z) * kernel2(delta_y);//	evaluation function
			sum_yz -=  kernel(delta_t) *dkernel_dt(delta_y) * dkernel_dt(delta_z) * kernel2(delta_x);//	evaluation function
		}
		h[1][1] = sum_xx/(dx*dx); 
		h[1][2] = sum_xy/(dx*dy); 
		h[1][3] = sum_xz/(dx*dz); 
		h[2][1] = sum_xy/(dx*dy); 
		h[2][2] = sum_yy/(dy*dy); 
		h[2][3] = sum_yz/(dy*dz); 
		h[3][1] = sum_xz/(dx*dz); 
		h[3][2] = sum_yz/(dy*dz); 
		h[3][3] = sum_zz/(dz*dz); 
		return;
	 }
}
