package jumpAnalysis;
import jumpAnalysis.OneDdata;
import optimization.*;

public class GpsDataMinClass implements Uncmin_methods{
	//	members
	public OneDdata xdata, ydata, dxData;
	public double dt, dx;
	public double threashold;
	double [] xpls = new double[4+1];	//	+1 is needed for the implementation of fortran
	double [] fpls = new double[1+1];
	double [] gpls = new double[4+1];
	int [] itrmcd = new int [1+1];
    double[][] a = new double[4+1][4+1];
    double[] udiag = new double[4+1];
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
		double vx = x[1];
		double vy = x[2];
		double xbar = x[3];
		double ybar = x[4];
		for(int i = index_min; i < index_max; ++i){
			double delta_t =  (tnow - xdata.getTi(i))/dt;
			double delta_x = vx * (xdata.getTi(i) - tnow) + xbar - xdata.getXi(i);
			double delta_y = vy * (ydata.getTi(i) - tnow) + ybar - ydata.getXi(i);
			delta_x /= dxData.getXi(i)*dx;
			delta_y /= dxData.getXi(i)*dx;
			sum -= kernel(delta_t) * kernel2(delta_x) * kernel2(delta_y);//	evaluation function
		}
		return sum;
	 }
	
	public void gradient(double x[], double g[]){
		double sum_vx=0.0;
		double sum_vy=0.0;
		double sum_x=0.0;
		double sum_y=0.0;
		double vx = x[1];
		double vy = x[2];
		double xbar = x[3];
		double ybar = x[4];
		for(int i = index_min; i < index_max; ++i){
			double delta_t =  (tnow - xdata.getTi(i))/dt;
			double delta_x = vx * (xdata.getTi(i) - tnow) + xbar - xdata.getXi(i);
			double delta_y = vy * (ydata.getTi(i) - tnow) + ybar - ydata.getXi(i);
			double delta_dx =dxData.getXi(i)*dx; 
			delta_x /= delta_dx;
			delta_y /= delta_dx;
			sum_vx += kernel(delta_t) *dkernel_dt(delta_x) * kernel2(delta_y) * (delta_t/delta_dx);//	evaluation function
			sum_vy += kernel(delta_t) *dkernel_dt(delta_y) * kernel2(delta_x) * (delta_t/delta_dx);//	evaluation function
			sum_x += kernel(delta_t) *dkernel_dt(delta_x) * kernel2(delta_y) /delta_dx;//	evaluation function
			sum_y += kernel(delta_t) *dkernel_dt(delta_y) * kernel2(delta_x) /delta_dx;//	evaluation function
		}
		g[1] = sum_vx;
		g[2] = sum_vy;
		g[3] = sum_x;
		g[4] = sum_y;
		return;
	 }

	 public void hessian(double x[], double h[][]){
		double sum_vxvx=0.0;
		double sum_vxvy=0.0;
		double sum_vx_x=0.0;
		double sum_vx_y=0.0;
		double sum_vyvy=0.0;
		double sum_vy_x=0.0;
		double sum_vy_y=0.0;
		double sum_x_x=0.0;
		double sum_x_y=0.0;
		double sum_y_y=0.0;
		double vx = x[1];
		double vy = x[2];
		double xbar = x[3];
		double ybar = x[4];
		for(int i = index_min; i < index_max; ++i){
			double delta_t =  (tnow - xdata.getTi(i))/dt;
			double delta_x = vx * (xdata.getTi(i) - tnow) + xbar - xdata.getXi(i);
			double delta_y = vy * (ydata.getTi(i) - tnow) + ybar - ydata.getXi(i);
			double delta_dx =dxData.getXi(i)*dx; 
			delta_x /= delta_dx;
			delta_y /= delta_dx;

			sum_vxvx -= kernel(delta_t) *d2kernel_dt2(delta_x) * kernel2(delta_y) * (delta_t/delta_dx) * (delta_t/delta_dx);//	evaluation function
			sum_vyvy -= kernel(delta_t) *d2kernel_dt2(delta_y) * kernel2(delta_x) * (delta_t/delta_dx) * (delta_t/delta_dx);//	evaluation function
			sum_vx_x -= kernel(delta_t) *d2kernel_dt2(delta_x) * kernel2(delta_y) * (delta_t/delta_dx /delta_dx);//	evaluation function
			sum_vy_y -= kernel(delta_t) *d2kernel_dt2(delta_y) * kernel2(delta_x) * (delta_t/delta_dx /delta_dx);//	evaluation function
			sum_x_x -= kernel(delta_t) *d2kernel_dt2(delta_y) * kernel2(delta_x)  /delta_dx /delta_dx;//	evaluation function

			sum_vxvy -= kernel(delta_t) *dkernel_dt(delta_x) * dkernel_dt(delta_y) * (delta_t/delta_dx) * (delta_t/delta_dx);//	evaluation function
			sum_vx_y -= kernel(delta_t) *dkernel_dt(delta_x) * dkernel_dt(delta_y) * (delta_t/delta_dx /delta_dx);//	evaluation function
			sum_vy_x -= kernel(delta_t) *dkernel_dt(delta_y) * dkernel_dt(delta_x) * (delta_t/delta_dx /delta_dx);//	evaluation function
			sum_x_y -= kernel(delta_t) *dkernel_dt(delta_x) * dkernel_dt(delta_y) /delta_dx /delta_dx;//	evaluation function
			
		}
		h[1][1] = sum_vxvx; 
		h[1][2] = sum_vxvy; 
		h[1][3] = sum_vx_x; 
		h[1][4] = sum_vx_y; 
		h[2][1] = h[1][2]; 
		h[2][2] = sum_vyvy; 
		h[2][3] = sum_vy_x; 
		h[2][4] = sum_vy_y; 
		h[3][1] = h[1][3]; 
		h[3][2] = h[2][3]; 
		h[3][3] = sum_x_x; 
		h[3][4] = sum_x_y; 
		h[4][1] = h[1][4]; 
		h[4][2] = h[2][4]; 
		h[4][3] = h[3][4]; 
		h[4][4] = sum_y_y; 
		return;
	 }

}
