package jumpAnalysis;
import optimization.Uncmin_f77;
import jumpAnalysis.OneDdata;
import jumpAnalysis.GpsDataMinClass3D;

public class GpsData {
	private 
	OneDdata x, y, z, dx;
	OneDdata vx, vy, vz, xbar, ybar, zbar, vamp;
	public int length;
	GpsDataMinClass3D minClass;
	
	public boolean read(String filename){
		CSV csv = new CSV(filename);
		x = new OneDdata(csv.data.size());
		y = new OneDdata(csv.data.size());
		z = new OneDdata(csv.data.size());
		dx = new OneDdata(csv.data.size());
		for(int i=0; i<x.length; ++i){
			x.set(i, csv.data.get(i).get(0), csv.data.get(i).get(1));
			y.set(i, csv.data.get(i).get(0), csv.data.get(i).get(2));
			z.set(i, csv.data.get(i).get(0), csv.data.get(i).get(3));
			dx.set(i, csv.data.get(i).get(0), csv.data.get(i).get(4));
		}
		x.setInterp();
		y.setInterp();
		z.setInterp();
		dx.setInterp();
		length = x.length;
		vx = new OneDdata(length);
		vy = new OneDdata(length);
		vz = new OneDdata(length);
		xbar = new OneDdata(length);
		ybar = new OneDdata(length);
		zbar = new OneDdata(length);
		minClass = new GpsDataMinClass3D();
		return true;
	}

	public void getSpeed(double delta_t, double delta_x){
		minClass.xdata = x;
		minClass.ydata = y;
		minClass.zdata = z;
		minClass.dx = delta_x;
		minClass.dt = delta_t;
		minClass.dxData = dx;
		for(int i=0; i<length-1; ++i)
			getSpeedOne(i);
		vx.setInterp();
		vy.setInterp();
		vz.setInterp();
		xbar.setInterp();
		ybar.setInterp();
		zbar.setInterp();
	}
	
	private void getSpeedOne(int i){
		double vxtmp = (x.getXi(i+1) - x.getXi(i))/(x.getTi(i+1)-x.getTi(i));
		double vytmp = (y.getXi(i+1) - y.getXi(i))/(x.getTi(i+1)-x.getTi(i));
		double vztmp = (z.getXi(i+1) - z.getXi(i))/(x.getTi(i+1)-x.getTi(i));
		minClass.tnow = x.getTi(i);
		minClass.index_min = x.getFloorIndex(minClass.tnow - 1.5*minClass.dt);
		minClass.index_max = x.getFloorIndex(minClass.tnow + 1.5*minClass.dt)+1;
		double [] xinit = {0.0, vxtmp, vytmp, vztmp, x.getXi(i), y.getXi(i), z.getXi(i)};
		Uncmin_f77.optif0_f77(6, xinit, minClass, minClass.xpls, minClass.fpls, minClass.gpls, minClass.itrmcd, minClass.a, minClass.udiag);
		vx.set(i, minClass.tnow, minClass.xpls[1]);
		vy.set(i, minClass.tnow, minClass.xpls[2]);
		vz.set(i, minClass.tnow, minClass.xpls[3]);
		xbar.set(i, minClass.tnow, minClass.xpls[4]);
		ybar.set(i, minClass.tnow, minClass.xpls[5]);
		zbar.set(i, minClass.tnow, minClass.xpls[6]);
	}
	public OneDdata getX1d(){	return x;	}
	public OneDdata getY1d(){	return y;	}
	public double[] getX(){	return x.getX();}
	public double[] getY(){	return y.getX();}
	public double[] getZ(){	return z.getX();}
	public double[] getT(){	return x.getT();}
	public double[] getVx(){	return vx.getX();}
	public double[] getVy(){	return vy.getX();}
	public double[] getVz(){	return vz.getX();}
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
	
	static double getVabs(double Latitude, double Longitude){
		return 0.0;
	}
	
}
