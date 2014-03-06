package jumpAnalysis;
import optimization.Uncmin_f77;
import jumpAnalysis.OneDdata;
import jumpAnalysis.GpsDataMinClass;

public class GpsData {
	private 
	OneDdata x, y, z, dx;
	OneDdata vx, vy, vamp;
	public int length;
	GpsDataMinClass minClass;
	
	public boolean read(String filename){
		CSV csv = new CSV(filename);
		x = new OneDdata(csv.data.size());
		y = new OneDdata(csv.data.size());
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
		return true;
	}

	public void getSpeed(double delta_t, double delta_x){
		minClass.xdata = x;
		minClass.ydata = y;
		minClass.dx = delta_x;
		minClass.dt = delta_t;
		minClass.dxData = dx;
		for(int i=0; i<length-1; ++i)
			getSpeedOne(i);
		
	}
	
	private void getSpeedOne(int i){
		double vxtmp = (x.getXi(i+1) - x.getXi(i))/(x.getTi(i+1)-x.getTi(i));
		double vytmp = (x.getXi(i+1) - x.getXi(i))/(x.getTi(i+1)-x.getTi(i));
		minClass.tnow = x.getTi(i);
		minClass.index_min = x.getFloorIndex(minClass.tnow - 1.5*minClass.dt);
		minClass.index_max = x.getFloorIndex(minClass.tnow + 1.5*minClass.dt)+1;
		double [] xinit = {0.0, vxtmp, vytmp, x.getXi(i), y.getXi(i)};
		Uncmin_f77.optif0_f77(4, xinit, minClass, minClass.xpls, minClass.fpls, minClass.gpls, minClass.itrmcd, minClass.a, minClass.udiag);
		vx.set(i, minClass.tnow, x.getXi(i));
		vy.set(i, minClass.tnow, x.getXi(i));
	}
	
	//	copy constructor
	public GpsData(GpsData oneDdata){
		this.x = oneDdata.x;
		this.y = oneDdata.x;
	}
	public int getFloorIndex(double t_){return x.getFloorIndex(t_);}
	
}
