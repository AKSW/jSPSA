package de.uni_leipzig.aksw.evaluate;

import java.util.Vector;
/**
 * Interface for any evaluator e.g. objective function of the SPSA algorithm.
 * @author Klaus Lyko
 *
 */
public interface IEvaluator {
	/**
	 * Evaluates the parameters theta with respect to the specific objective. Should return a double value, 
	 * whereas smaller values represent better results.
	 * @param theta Parameters of the given problem which should be optimized.
	 * @return A double value indicating the quality of the given parameterization theta. Note, by default smaller values indicate better solutions.
	 */
	public double evaluate(Vector<Double> theta);
	
	/**
	 * Evaluates the parameters theta with respect to the specific objective. Should return a double value, 
	 * whereas smaller values represent better results.
	 * @param theta Parameters of the given problem which should be optimized.
	 * @return A double value indicating the quality of the given parameterization theta. Note, by default smaller values indicate better solutions.
	 */
	public double evaluate(double[] theta);
}
