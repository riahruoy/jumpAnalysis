package jumpAnalysis;
import jumpAnalysis.MyMath;

public class OneDdata {
	public OneDdata(int length_){
		t = new double[length_];
		x = new double[length_];
		dtInv = new double[length_];
		length = length_;
	}
	public OneDdata(double [] t_, double [] x_){
		length = t_.length;
		t = new double[length];
		x = new double[length];
		dtInv = new double[length];
		for(int i=0; i<length; ++i)
			set(i, t_[i], x_[i]);
		setInterp();
	}
	public void set(int i, double t_, double x_){
		t[i] = t_;
		x[i] = x_;
		return;
	}
	public void setInterp(){
		t0 = t[0];
		dt = (t[t.length-1] - t0)/(t.length-1);
		dtInv = new double [t.length-1];
		for(int i=0; i<t.length-1; ++i)
			dtInv[i] = 1.0/(t[i+1] - t[i]);
		return;
	}
	public double[] getT(){return t;}
	public double[] getX(){return x;}
	public double getXi(int i){	return x[i];	}
	public double getTi(int i){	return t[i];	}
	
	//	lower level methods
	public int getRoughIndex(double t_){
		int index =(int)Math.floor(1.0*(t_-t0)/dt);
		if(index<0) return 0;
		if(index>length) return length-1;
		return index;
	}
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
	public double get(double t_){
		int index = getFloorIndex(t_);
		double r_ceil = (t_-t[index]);
		double r_floor = (t[index+1]-t_);
		return (r_floor * x[index] + r_ceil*x[index+1])*dtInv[index];
	}
	

	//	members	
	private double [] t, x, dtInv;
	public final int length;
	private double t0, dt; 
	
	//	copy constructor
	public OneDdata(OneDdata oneDdata){
		this.t = oneDdata.t.clone();
		this.x = oneDdata.x.clone();
		this.length = oneDdata.length;
		this.t0 = oneDdata.t0;
		this.dt = oneDdata.dt;
	}

	//	signal processing
	// binomial smoothing
	public OneDdata smooth(int num){
		OneDdata rslt = new OneDdata(t, x);
		int n = num*2;
		double [] binomial = new double[n+1];
		int nFact = MyMath.factorial(n);
		double ratio = Math.pow(0.5, n);
		for(int k=0; k<num; ++k){
			int kFact = MyMath.factorial(k);
			int n_kFact = MyMath.factorial(n-k);
			binomial[k] = nFact/(kFact*n_kFact)*ratio;
			binomial[n-k] = binomial[k];
		}
		int numFact = MyMath.factorial(num);
		binomial[num] = nFact/(numFact*numFact)*ratio; 
		
		for(int i=num; i<length-num; ++i){
			double sum = 0.0;
			for(int j=-num; j<=num; ++j)
				sum += x[i+j]*binomial[j+num];
			rslt.set(i, t[i], sum);
		}
		return rslt;
	}}
