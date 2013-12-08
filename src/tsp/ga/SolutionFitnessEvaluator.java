package tsp.ga;

import java.util.List;

import org.uncommons.watchmaker.framework.FitnessEvaluator;

import tsp.model.Solution;

public class SolutionFitnessEvaluator implements FitnessEvaluator<Solution> {

	@Override
	public double getFitness(Solution candidate,
			List<? extends Solution> population) {
		
		return candidate.length();
	}

	@Override
	public boolean isNatural() {
		return false;
	}

}
