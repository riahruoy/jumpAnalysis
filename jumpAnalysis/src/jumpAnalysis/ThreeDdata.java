package jumpAnalysis;
import jumpAnalysis.CSV;
import jumpAnalysis.OneDdata;

class ThreeDdata {
	public ThreeDdata(){	};
	public ThreeDdata(double[] t_, double[] x_, double[] y_, double [] z_){
		x = new OneDdata(t_, x_);
		y = new OneDdata(t_, y_);
		z = new OneDdata(t_, z_);
		length = x.length;
		amp = new OneDdata(length);
		for(int i=0; i<x.length; ++i)
			amp.set(i, x.getTi(i),  
					Math.sqrt(x.getXi(i)*x.getXi(i) + y.getXi(i)*y.getXi(i) + z.getXi(i)*z.getXi(i)));
		amp.setInterp();
	};
	private 
	OneDdata x, y, z, amp;
	public int length;
	
	public double[] getT(){return x.getT();}
	public double[] getX(){return x.getX();}
	public double[] getY(){return y.getX();}
	public double[] getZ(){return z.getX();}
	public double[] getamp(){return amp.getX();}
	
	public boolean read(String filename){
		CSV csv = new CSV(filename);
		x = new OneDdata(csv.data.size());
		y = new OneDdata(csv.data.size());
		z = new OneDdata(csv.data.size());
		amp = new OneDdata(csv.data.size());
		for(int i=0; i<x.length; ++i){
			x.set(i, csv.data.get(i).get(0), csv.data.get(i).get(1));
			y.set(i, csv.data.get(i).get(0), csv.data.get(i).get(2));
			z.set(i, csv.data.get(i).get(0), csv.data.get(i).get(3));
			
			amp.set(i, csv.data.get(i).get(0),  
					Math.sqrt(x.getXi(i)*x.getXi(i) + y.getXi(i)*y.getXi(i) + z.getXi(i)*z.getXi(i)));
		}
		x.setInterp();
		y.setInterp();
		z.setInterp();
		amp.setInterp();
		length = x.length;
		return true;
	}
	
	public double getT(int index){	return x.getTi(index);}

	public double getAmpi(int index){	return amp.getXi(index);	}
	public double getXi(int index){	return x.getXi(index);	}
	public double getYi(int index){	return y.getXi(index);	}
	public double getZi(int index){	return z.getXi(index);	}
	public double getTi(int index){	return x.getTi(index);	}
	//	methods for interpolation
	public double getX(double t_){	return x.get(t_);	}
	public double getY(double t_){	return y.get(t_);	}
	public double getZ(double t_){	return z.get(t_);	}

	public OneDdata getX1d(){	return x;	}
	public OneDdata getY1d(){	return y;	}
	public OneDdata getZ1d(){	return z;	}
	public OneDdata getAmp1d(){	return amp;	}

	//	copy constructor
	public ThreeDdata(ThreeDdata oneDdata){
		this.x = oneDdata.x;
		this.y = oneDdata.y;
		this.z = oneDdata.z;
		length = x.length;
		amp = new OneDdata(length);
		for(int i=0; i<x.length; ++i)
			amp.set(i, x.getTi(i),  
					Math.sqrt(x.getXi(i)*x.getXi(i) + y.getXi(i)*y.getXi(i) + z.getXi(i)*z.getXi(i)));
		amp.setInterp();
	}
	public int getFloorIndex(double t_){return x.getFloorIndex(t_);}

};
	