package de.uni_leipzig.aksw.optimize;

import java.util.HashMap;
import java.util.Map;
import java.util.Vector;
import java.util.Map.Entry;

import de.uni_leipzig.aksw.evaluate.IEvaluator;
import de.uni_leipzig.aksw.implementation.BasicLinkSpecSPSA;
import de.uni_leipzig.aksw.implementation.FunctionEvaluator;

public class FunctionSPSA implements ISPSA{

	IEvaluator evaluator;
	Map<Integer, String> route = new HashMap<Integer, String>();

	public static void main(String[] args) {
		IEvaluator evaluator = new FunctionEvaluator();
		ISPSA test = new FunctionSPSA();
		test.setEvaluator(evaluator);
		Vector<Double> theta = new Vector<Double>();
		theta.add(1d);
		theta.add(1d);
		double p = 0.5d; // not yet regarded
		double a = 300;
		double A = 10; 
		double c = 100; 
		double alpha = 0.602d;//1
		double gamma = 0.101d;// 1/6
		theta = test.runSPSA(1000, theta, p, a, A, c, alpha, gamma);
		System.out.println("THETA := "+theta.get(0)+", "+theta.get(1));
		double f = evaluator.evaluate(theta);
		System.out.println("Suggested best="+f);
		
		Map<Integer, String> route = test.getDebugLog();
		for(Entry<Integer,String> e: route.entrySet()) {
			System.out.println(e.getKey()+":"+e.getValue());
		}
	}

	@Override
	public void setEvaluator(IEvaluator evaluator) {
		this.evaluator = evaluator;
	}

	

	public double[] runSPSA(int n, double[] theta, 
			double p, double a, double A, double c, double alpha, double gamma) {
		//shpuld decrease
		double[] ak = new double[theta.length];
		double[] ck = new double[theta.length];
		
		for(int k=1; k<=n; k++) {			
			System.out.println("Iteration "+k);
			for(int i=0; i<theta.length; i++) { // update ak, ck
				System.out.println("\tTheta["+i+"]="+theta[i]);
				ak[i] = a / Math.pow((k+a+A), alpha);
				System.out.println("\tak["+i+"]="+ak[i]);
				ck[i] = c / Math.pow(k+1, gamma);
				System.out.println("\tck["+i+"]="+ck[i]);
			}
			
			double curF = evaluator.evaluate(theta);
			route.put(k, curF+"  theta[0]="+theta[0]+"-theta[1]="+theta[1]);
			double[] delta = BasicLinkSpecSPSA.generateBernouilliDistribution(theta.length);
		
			double[] thetaminus = new double[theta.length];
			double[] thetaplus = new double[theta.length];
			for(int i = 0; i<theta.length; i++) {
				thetaminus[i] = theta[i]-ck[i]*delta[i]; //delta in {-1, 1}
				thetaplus[i] = theta[i]+ck[i]*delta[i];
			}
			
			double yplus = evaluator.evaluate(thetaplus);
			double yminus = evaluator.evaluate(thetaminus);
			
			System.out.println(k+"th EVAL thetaplus: "+thetaplus[0] + " & " + thetaplus[1]+
					" ... thetaminus: "+thetaminus[0] + " & " + thetaminus[1]+
					"\n yplus="+yplus+" yminus="+yminus
					);

			double[] ghat = new double[theta.length];
//			if(Math.abs(yplus-yminus)<0.05) //Appropriate termination criteria?
//				return theta;
			for(int i = 0; i<theta.length; i++) {
				ghat[i] = (yplus-yminus) / 2*ck[i]*delta[i];
				
//				theta[i] = theta[i] - ak[i]*ghat[i];
				double newValue = theta[i] - ak[i]*ghat[i];
//				if(!(newValue<=0.08 || newValue>1))
					theta[i] = newValue;
				System.out.println("\t "+i+": ghat="+ghat[i]+ "=> theta:="+theta[i]);
			}
			System.out.println("---next iteration?");
//			BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
//			br.readLine();
		}
		return theta;
	}

	@Override
	public Vector<Double> runSPSA(int n, Vector<Double> theta, double p,
			double a, double A, double c, double alpha, double gamma) {
		double[] array = new double[theta.size()];
		for(int i=0;i<theta.size();i++)
			array[i] = theta.get(i);
		double[] result = runSPSA( n, array, 
				 p,  a,  A,  c,  alpha,  gamma); 
		Vector<Double> vec = new Vector<Double>();
		for(int i=0;i<result.length;i++)
			vec.add(result[i]);
		return vec;
	}

	@Override
	public Map<Integer, String> getDebugLog() {
		return route;
	}
}
