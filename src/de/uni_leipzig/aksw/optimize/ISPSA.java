package de.uni_leipzig.aksw.optimize;

import java.util.Vector;

import de.uni_leipzig.aksw.evaluate.IEvaluator;

public interface ISPSA {
	
	/**
	 * Specify evaluator for the specific problem at hand. Should return smaller values for better results.
	 * @param evaluator
	 */
	public void setEvaluator(IEvaluator evaluator);
	
	
	/**
	 * Recursive implementation of the SPSA optimization method. Runs n recursive optimization steps
	 * upon the parameters theta for the specific problem at hand.
	 * Simultaneous perturnbation vectors are build upon the gain sequences a_k = (A+k+1)^alpha and c_k = c / (k+1)^gamma
	 * @param n steps of SPSA optimization
	 * @param theta initial guess of parameters to be optimized (maybe a +-1 distribution?)
	 * @param p Base probability of the Bernoulli +-1 distribution specifying which parameters to adapt in which direction.
	 * 			default 0.5d.
	 * @param a Non negative coefficient. Used to build simultaneous pertubation vectors. 
	 * @param A Non negative coefficient. Used to build simultaneous pertubation vectors.
	 * @param c Non negative coefficient. Used to build simultaneous pertubation vectors.
	 * @param alpha Non negative coefficient. Used to build simultaneous pertubation vectors.
	 * @param gamma Non negative coefficient. Used to build simultaneous pertubation vectors.
	 * @return
	 */
	public Vector<Double> runSPSA(int n, Vector<Double> theta, 
			double p, double a, double A, double c, double alpha, double gamma);
}
