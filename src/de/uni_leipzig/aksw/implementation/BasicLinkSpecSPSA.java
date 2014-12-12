package de.uni_leipzig.aksw.implementation;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;


import de.uni_leipzig.aksw.evaluate.IEvaluator;


import de.uni_leipzig.simba.execution.ExecutionEngine;
import de.uni_leipzig.simba.execution.planner.ExecutionPlanner;
import de.uni_leipzig.simba.genetics.evaluation.basics.EvaluationData;
import de.uni_leipzig.simba.specification.LinkSpec;




public class BasicLinkSpecSPSA {
	EvaluationData data;
	LinkSpec child1, child2;
	ExecutionPlanner planner;ExecutionEngine engine;
	IEvaluator evaluator;

	
	Map<Integer, String> route = new HashMap<Integer, String>();
	
	public BasicLinkSpecSPSA() {
		evaluator = LinkSpecEvaluator.createDefault();
	}

	
	public double[] runSPSA(int n, double[] theta, 
			double p, double a, double A, double c, double alpha, double gamma) throws IOException {
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
			
			double curF = -1*evaluator.evaluate(theta);
			route.put(k, curF+"  theta[0]="+theta[0]+"-theta[1]="+theta[1]);
			double[] delta = generateBernouilliDistribution(theta.length);
		
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
			for(int i = 0; i<theta.length; i++) {
				ghat[i] = (yplus-yminus) / 2*ck[i]*delta[i];
				theta[i] = theta[i] - ak[i]*ghat[i];
				System.out.println("\t "+i+": ghat="+ghat[i]+ "=> theta:="+theta[i]);
			}
			System.out.println("---next iteration?");
			BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
//			br.readLine();
		}
		return theta;
	}
	
//	public double evaluate(double[] theta) {
//		PRFComputer pfm = new PRFComputer();
//		Mapping m = new Mapping();
//		
//		LinkSpec spec = new LinkSpec();
//		spec.operator = Operator.OR;
//		child1.threshold = Math.min(Math.max(theta[0],0.1d),1);
//		child2.threshold = Math.min(Math.max(theta[1],0.1d),1);
//		spec.addChild(child1);spec.addChild(child2);
//		spec.threshold = Math.max(theta[0], theta[1]);
//		System.out.println("Running spec: "+spec);
//		double value = 0d;
//		m = engine.runNestedPlan(planner.plan(spec));
//		double f = pfm.computeFScore(m, data.getReferenceMapping());
////		System.out.println("Evaluated spec "+spec+"\n mapping.size="+m.size()+"\n f="+f);
//		value = f;
//		if(f>bestF) {
//			bestF = f;
//			bestTheta = theta;
//		}
//		return -1*f;//smaller values are better
//	}
	
	/**
	 * generate a symmetric bernoulli +-1 distribution of size size.
	 * @param size
	 * @return An array of size size with values {-1,1}
	 */
	public static double[]  generateBernouilliDistribution(int size) {
		double[] delta = new double[size];
		Random rand = new Random();
		for(int i = 0; i<size; i++) {
			delta[i] = (2 * (Math.round(rand.nextDouble())))-1;
		}
		return delta;
	}
	
	
	public static void main(String[] args) throws IOException {
		generateBernouilliDistribution(50);
		BasicLinkSpecSPSA test = new BasicLinkSpecSPSA();
		double[] theta = new double[2];
		theta[0] = 0.3;
		theta[1] = 0.8;
		double p = 0.5d;
		double a = 0.5;
		double A = 1; 
		double c = 0.5; 
		double alpha = 0.602d;//1
		double gamma = 0.101d;// 1/6
		theta = test.runSPSA(50, theta, p, a, A, c, alpha, gamma);
		System.out.println("THETA := "+theta[0]+", "+theta[1]);
		double f = test.evaluator.evaluate(theta);
		System.out.println("Suggested spec f="+f);
		
		Map<Integer, String> route = test.route;
		for(Entry<Integer,String> e: test.route.entrySet()) {
			System.out.println(e.getKey()+":"+e.getValue());
		}
	}
	
	
}
