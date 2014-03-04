package jumpAnalysis;

public class MyMath {
	public static int factorial(int n){
		int rslt = 1;
		if(n < 2) return rslt;
		for(int i=2; i<=n; ++i)
			rslt *=i;
		return rslt;
	}
}
