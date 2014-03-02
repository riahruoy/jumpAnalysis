package jumpAnalysis;
import jumpAnalysis.CSV;

class threeDdata {
	public threeDdata(){
	};
	private 
	double[] t, x, y, z, dtInv;
	double[] amp2;
	double dt, t0;
	
	public double[] getT(){return t;}
	public double[] getX(){return x;}
	public double[] getY(){return y;}
	public double[] getZ(){return z;}
	public double[] getAmp2(){return amp2;}
	
	public boolean read(String filename){
		CSV csv = new CSV(filename);
		t = new double [csv.data.size()];
		x = new double [csv.data.size()];
		y = new double [csv.data.size()];
		z = new double [csv.data.size()];
		amp2 = new double [csv.data.size()];
		for(int i=0; i<t.length; ++i){
			t[i] = csv.data.get(i).get(0);
			x[i] = csv.data.get(i).get(1);
			y[i] = csv.data.get(i).get(2);
			z[i] = csv.data.get(i).get(3);
			amp2[i] = Math.sqrt(x[i]*x[i] + y[i]*y[i] + z[i]*z[i]);
		}
		t0 = t[0];
		dt = (t[t.length-1] - t0)/(t.length-1);
		dtInv = new double [t.length-1];
		for(int i=0; i<t.length-1; ++i)
			dtInv[i] = 1.0/(t[i+1] - t[i]);
		return true;
	}
	
	public double getT(int index){	return t[index];}
	//	methods for interpolation
	public double getX(double t_){	return getValue(t_, x);	}
	public double getY(double t_){	return getValue(t_, y);	}
	public double getZ(double t_){	return getValue(t_, z);	}

	//	lower level methods
	public int getFloorIndex(double t_){
		if(t_ < t[1]) return 0;
		else if(t_>t[t.length-2]) return t.length-2;
		else{
			int index =(int)Math.floor(1.0*(t_-t0)/dt);
			if(t[index] <= t_)
				while(t[index+1] <= t_)	++index;
			else
				while(t[index-1] > t_) --index;
			return index;
		}
	}
	private double getValue(double t_, double[] xyz){
		int index = getFloorIndex(t_);
		double r_ceil = (t_-t[index]);
		double r_floor = (t[index+1]-t_);
		return (r_floor * xyz[index] + r_ceil*xyz[index+1])*dtInv[index];
	}
};
	