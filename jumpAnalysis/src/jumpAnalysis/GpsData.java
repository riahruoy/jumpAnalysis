package jumpAnalysis;
import optimization.Uncmin_f77;
import jumpAnalysis.OneDdata;
import jumpAnalysis.GpsDataMinClass3D;
import java.lang.Math;

public class GpsData {
	private 
	OneDdata x, y, z, dx;
	OneDdata vx, vy, vz, xbar, ybar, zbar, vamp;
	public int length;
	GpsDataMinClass3D minClass;
	
	public boolean read(String filename){
		//---	---
		CSV csv = new CSV(filename);
		double [] t = new double [csv.data.size()]; 
		double [] latitude = new double [csv.data.size()]; 
		double [] longitude = new double [csv.data.size()];
		double [] altitude = new double [csv.data.size()];
		double [] accuracy = new double [csv.data.size()];
		for(int i=0; i<t.length; ++i){
			t[i]        = csv.data.get(i).get(0);
			latitude[i]      = csv.data.get(i).get(1);
			longitude[i]    = csv.data.get(i).get(2);
			altitude[i] = csv.data.get(i).get(3);
			accuracy[i] = csv.data.get(i).get(4);
		}
		setData(t, latitude, longitude, altitude, accuracy);
		return true;
		//---	---
	}

	public void setData(double[] t, double[] latitude, double[] longitude, double[] altitude, double[] accuracy){
		//--- variables for conversion to the relative coordinate	---
		phi0 = 0.0; theta0 = 0.0; altitude0 = 0.0;
		for(int i=0; i<longitude.length; ++i){
			phi0 += latitude[i];
			theta0 += longitude[i];
			altitude0 += altitude[i];
		}
		phi0 /= t.length;
		theta0 /= t.length;
		altitude0/= t.length;
		
		getGlobalXYZfromGPSdata(phi0, theta0, altitude0, globalXYZ0);
		setRotationMatrix(theta0, phi0);
		//-------------------------------------------------------------
		x = new OneDdata(t.length);
		y = new OneDdata(t.length);
		z = new OneDdata(t.length);
		dx = new OneDdata(t.length);
		double [] XYZ = new double [3];
//		double [] xyz_= new double [3];
		
		for(int i=0; i<x.length; ++i){
			getGlobalXYZfromGPSdata(latitude[i], longitude[i], altitude[i], XYZ);
			double [] xyz_ = getLocalXYZfromGlobalXYZ(XYZ);

//			x.set(i, t[i], xyz_[0]);
//			y.set(i, t[i], xyz_[1]);
//			z.set(i, t[i], xyz_[2]);
			x.set(i, t[i], XYZ[0]-globalXYZ0[0]);
			y.set(i, t[i], XYZ[1]-globalXYZ0[1]);
			z.set(i, t[i], XYZ[2]-globalXYZ0[2]);
			dx.set(i, t[i], accuracy[i]);
		}
		//---	---
		x.setInterp();
		y.setInterp();
		z.setInterp();
		dx.setInterp();
		length = x.length;
		vx = new OneDdata(length);
		vy = new OneDdata(length);
		vz = new OneDdata(length);
		vamp = new OneDdata(length);
		xbar = new OneDdata(length);
		ybar = new OneDdata(length);
		zbar = new OneDdata(length);
		minClass = new GpsDataMinClass3D();
	}

	public void getSpeed(double delta_t, double delta_x){
		minClass.xdata = x;
		minClass.ydata = y;
		minClass.zdata = z;
		minClass.dx = delta_x;
		minClass.dt = delta_t;
		minClass.dxData = dx;
		for(int i=0; i<length; ++i)
			getSpeedOne(i);

		//---
		vx.setInterp();
		vy.setInterp();
		vz.setInterp();
		vamp.setInterp();
		xbar.setInterp();
		ybar.setInterp();
		zbar.setInterp();
	}
	
	private void getSpeedOne(int i){
		double vxtmp, vytmp, vztmp;
		if(i < length-1){
			vxtmp = (x.getXi(i+1) - x.getXi(i))/(x.getTi(i+1)-x.getTi(i));
			vytmp = (y.getXi(i+1) - y.getXi(i))/(x.getTi(i+1)-x.getTi(i));
			vztmp = (z.getXi(i+1) - z.getXi(i))/(x.getTi(i+1)-x.getTi(i));
		}
		else{
			vxtmp = (x.getXi(i-1) - x.getXi(i))/(x.getTi(i-1)-x.getTi(i));
			vytmp = (y.getXi(i-1) - y.getXi(i))/(x.getTi(i-1)-x.getTi(i));
			vztmp = (z.getXi(i-1) - z.getXi(i))/(x.getTi(i-1)-x.getTi(i));
		}
		minClass.tnow = x.getTi(i);
		minClass.index_min = x.getFloorIndex(minClass.tnow - 1.5*minClass.dt);
		minClass.index_max = x.getFloorIndex(minClass.tnow + 1.5*minClass.dt)+1;
		double [] xinit = {0.0, vxtmp, vytmp, vztmp, x.getXi(i), y.getXi(i), z.getXi(i)};
		Uncmin_f77.optif0_f77(6, xinit, minClass, minClass.xpls, minClass.fpls, minClass.gpls, minClass.itrmcd, minClass.a, minClass.udiag);
		vx.set(i, minClass.tnow, minClass.xpls[1]);
		vy.set(i, minClass.tnow, minClass.xpls[2]);
		vz.set(i, minClass.tnow, minClass.xpls[3]);
		vamp.set(i, minClass.tnow, Math.sqrt(minClass.xpls[0]*minClass.xpls[0] + minClass.xpls[1]*minClass.xpls[1] + minClass.xpls[2]*minClass.xpls[2]));
		xbar.set(i, minClass.tnow, minClass.xpls[4]);
		ybar.set(i, minClass.tnow, minClass.xpls[5]);
		zbar.set(i, minClass.tnow, minClass.xpls[6]);
	}
	public OneDdata getX1d(){	return x;	}
	public OneDdata getY1d(){	return y;	}
	public OneDdata getVamp1d(){	return vamp;	}
	public double[] getX(){	return x.getX();}
	public double[] getY(){	return y.getX();}
	public double[] getZ(){	return z.getX();}
	public double[] getT(){	return x.getT();}
	public double[] getVx(){	return vx.getX();}
	public double[] getVy(){	return vy.getX();}
	public double[] getVz(){	return vz.getX();}
	public double[] getVamp(){	return vamp.getX();}
	public double[] getXbar(){	return xbar.getX();}
	public double[] getYbar(){	return ybar.getX();}
	public double[] getZbar(){	return zbar.getX();}
	
	public GpsData(){}
	//	copy constructor
	public GpsData(GpsData oneDdata){
		this.x = oneDdata.x;
		this.y = oneDdata.x;
	}
	public int getFloorIndex(double t_){return x.getFloorIndex(t_);}
	
	//---------------------------------------------	
	//
	//			geographic functions 
	//
	//---------------------------------------------
	//	phi0: averaged latitude, theta0: averaged longitude
	private double phi0, theta0, altitude0;
	//	global coordinate of the averaged position
	private double [] globalXYZ0 = new double [3];
	
	public static final double a = 6378137.0;				//	[m]
	public static final double f = 1.0/298.257223563;
	public static final double e2 = 2.0*f - f*f;//0.006694380022900788;	//	[]
	public static final double piGps = 3.1415926535898;
	public double [][] conversionMatrix = new double [3][3];	//	conversion matrix from global (X, Y, Z) coordinate to local (x, y, z)
	public static void getGlobalXYZfromGPSdata(double phi, double theta, double h, double[] XYZ){
		double N = a/Math.sqrt(1.0-e2*Math.sin(phi*piGps/180.0)*Math.sin(phi*piGps/180.0));
		XYZ[0] = (N+h)*Math.cos(phi*piGps/180.0)*Math.cos(theta*piGps/180.0);	//	X
		XYZ[1] = (N+h)*Math.cos(phi*piGps/180.0)*Math.sin(theta*piGps/180.0);	//	Y
		XYZ[2] = (N*(1.0-e2)+h)*Math.sin(phi*piGps/180.0);						//	Z
		return;
	}
	public double[] getLocalXYZfromGlobalXYZ(double[] XYZ){
		double [] deltaX = {XYZ[0]-globalXYZ0[0], XYZ[1]-globalXYZ0[1], XYZ[2]-globalXYZ0[2]}; 
		return dot(conversionMatrix, deltaX);
	}

	public static void getGPSdatafromGlobalXYZ(double phi, double theta, double h, double[] XYZ){
		//	under construction
		return;
	}
	public void setRotationMatrix(double theta, double phi){
		double q = Math.PI;//piGps*90.0/180.0; 
		double [][] R0 = new double [3][3];
		R0[0][0] = Math.cos(q);	R0[0][1] = Math.sin(q); R0[0][2] = 0.0;
		R0[1][0] =-Math.sin(q); R0[1][1] = Math.cos(q); R0[1][2] = 0.0;
		R0[2][0] = 0.0;	        R0[2][1] = 0.0;         R0[2][2] = 1.0;
		double [][] R1 = new double [3][3];
		q = piGps*(90.0-phi)/180.0; 
		R1[0][0] = Math.cos(q);	R1[0][1] = 0.0;         R1[0][2] = -Math.sin(q);
		R1[1][0] = 0.0;	        R1[1][1] = 1.0;         R1[1][2] = 0.0;
		R1[2][0] = Math.sin(q);	R1[2][1] = 0.0;         R1[2][2] = Math.cos(q);
		double [][] R2 = new double [3][3];
		q =piGps*theta/180.0; 
		R2[0][0] = Math.cos(q);	R2[0][1] = Math.sin(q); R2[0][2] = 0.0;
		R2[1][0] =-Math.sin(q); R2[1][1] = Math.cos(q); R2[1][2] = 0.0;
		R2[2][0] = 0.0;	        R2[2][1] = 0.0;         R2[2][2] = 1.0;
		
		conversionMatrix = dot(dot(R0, R1), R2);
		return;
	}

	public static double[][] dot(double[][] a, double [][] b){
		double [][] c = new double [3][3]; 
		for(int i=0; i<3; ++i){
			for(int j=0; j<3; ++j){
				c[i][j] = 0.0;
				for(int k=0; k<3; ++k){
					c[i][j] += a[i][k] * b[k][j];
				}
			}
		}
		return c;
	}
	public static double[] dot(double[][] a, double [] b){
		double [] c = new double [3]; 
		for(int i=0; i<3; ++i){
			c[i] = 0.0;
			for(int j=0; j<3; ++j){
				c[i] += a[i][j] * b[j];
			}
		}
		return c;
	}
	
}
