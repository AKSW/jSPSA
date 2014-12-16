package de.uni_leipzig.aksw.implementation;

import java.util.Vector;

import de.uni_leipzig.aksw.evaluate.IEvaluator;

public class FunctionEvaluator implements IEvaluator{
	
	@Override
	public double evaluate(Vector<Double> theta) {
		double[] array = new double[theta.size()];
		for(int i=0;i<theta.size();i++)
			array[i] = theta.get(i);
		return evaluate(array);
	}

	@Override
	public double evaluate(double[] theta) {
		if(theta.length<2)
			return 0;
		else {
			double x = theta[0];
			double y = theta[1];
			//((x^2 - 1) (y^2 -4) + x^2 +y^2 -5) / (x^2+y^2+1)^2
			return -1*((Math.pow(x, 2) - 1)* (Math.pow(y, 2) -4) + Math.pow(x, 2) +Math.pow(y, 2) -5) / Math.pow((Math.pow(x, 2) + Math.pow(y, 2)+1),2);
		}
	}

}
