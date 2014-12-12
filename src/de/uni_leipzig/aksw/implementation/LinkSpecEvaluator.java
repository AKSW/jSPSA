package de.uni_leipzig.aksw.implementation;

import java.util.LinkedList;
import java.util.List;
import java.util.Vector;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import de.uni_leipzig.aksw.evaluate.IEvaluator;
import de.uni_leipzig.simba.data.Mapping;
import de.uni_leipzig.simba.evaluation.PRFComputer;
import de.uni_leipzig.simba.execution.ExecutionEngine;
import de.uni_leipzig.simba.execution.planner.ExecutionPlanner;
import de.uni_leipzig.simba.execution.planner.HeliosPlanner;
import de.uni_leipzig.simba.genetics.evaluation.basics.DataSetChooser;
import de.uni_leipzig.simba.genetics.evaluation.basics.DataSetChooser.DataSets;
import de.uni_leipzig.simba.genetics.evaluation.basics.EvaluationData;
import de.uni_leipzig.simba.specification.LinkSpec;
import de.uni_leipzig.simba.specification.Operator;

/**
 * Basic implementation of an LinkSpec evaluator.
 * @author Klaus Lyko
 *
 */
public class LinkSpecEvaluator implements IEvaluator{
	EvaluationData data;
	Operator op;
	List<LinkSpec> childs;
	ExecutionPlanner planner;ExecutionEngine engine;	
	static Logger logger = Logger.getLogger("LIMES");
	
	
	public LinkSpecEvaluator(EvaluationData data, Operator op, List<LinkSpec> childs) {
		logger.setLevel(Level.ERROR);
		this.data = data;
		this.op = op;
		this.childs = childs;
		init();
	}

	
	@Override
	public double evaluate(Vector<Double> theta) {
		PRFComputer pfm = new PRFComputer();
		Mapping m = new Mapping();
		// create
		LinkSpec spec = new LinkSpec();
		spec.operator = Operator.OR;
		spec.threshold = 1d;
		if(theta.size() != childs.size()) {
			logger.warn("Number of childs("+childs.size()+") is different from number of parameters theta("+theta.size()+")");
		}
		for(int i = 0; i<Math.min(childs.size(), theta.size()); i++) {
			LinkSpec child = childs.get(i);
			child.threshold = Math.min(Math.max(theta.get(i),0.1d),1);
			spec.addChild(child);
			spec.threshold = Math.min(spec.threshold, child.threshold);
		}		
		m = engine.runNestedPlan(planner.plan(spec));
		double f = pfm.computeFScore(m, data.getReferenceMapping());
		return -1*f;//smaller values are better
	}

	
	/**
	 * Creates an default implementation: OR(title, authors) on DBLP-ACM
	 * @return default LinkSpecEvaluator over DBLP-ACM dataset
	 */
	public static LinkSpecEvaluator createDefault() {
		LinkSpec child1 = new LinkSpec();
		child1 = new LinkSpec();
		child1.setAtomicFilterExpression("levensthein", "x.title", "y.title");
		LinkSpec child2 = new LinkSpec();
		child2 = new LinkSpec();
		child2.setAtomicFilterExpression("trigrams", "x.authors", "y.authors");
		List<LinkSpec> list = new LinkedList<LinkSpec>();
		list.add(child1);
		list.add(child2);
		return new LinkSpecEvaluator(DataSetChooser.getData(DataSets.DBLPACM), Operator.OR, list);
	}

	private void init() {
		planner = new HeliosPlanner(data.getSourceCache(), data.getTargetCache());
		engine = new ExecutionEngine(data.getSourceCache(), 
				data.getTargetCache(), 
				data.getConfigReader().sourceInfo.var, 
				data.getConfigReader().targetInfo.var);
	}


	@Override
	public double evaluate(double[] theta) {
		Vector<Double> thetaVec = new Vector(theta.length);
		for(double d:theta)
			thetaVec.add(d);
		return evaluate(thetaVec);
		
	}
	
}
