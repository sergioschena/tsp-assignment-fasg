package tsp.ga;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.uncommons.maths.number.ConstantGenerator;
import org.uncommons.maths.number.NumberGenerator;
import org.uncommons.watchmaker.framework.EvolutionaryOperator;

import tsp.model.CityManager;
import tsp.model.Solution;

public class SolutionMutation implements EvolutionaryOperator<Solution> {

	private final NumberGenerator<Integer> mutationCountVariable;
	private final NumberGenerator<Integer> mutationAmountVariable;
	private CityManager cityManager;
	
	public SolutionMutation(CityManager cityManager) {
		this(cityManager, 1, 1);
	}

	public SolutionMutation(CityManager cityManager, int mutationCount, int mutationAmount) {
		this(cityManager, new ConstantGenerator<Integer>(mutationCount),
				new ConstantGenerator<Integer>(mutationAmount));
	}

	
	public SolutionMutation(CityManager cityManager, NumberGenerator<Integer> mutationCount,
			NumberGenerator<Integer> mutationAmount) {
		this.mutationCountVariable = mutationCount;
		this.mutationAmountVariable = mutationAmount;
		this.cityManager = cityManager;
	}

	public List<Solution> apply(List<Solution> selectedCandidates, Random rng) {
		List<Solution> result = new ArrayList<Solution>(selectedCandidates.size());
		for (Solution candidate : selectedCandidates) {
			Solution newCandidate = (Solution) candidate.clone();
			int mutationCount = Math.abs(mutationCountVariable.nextValue());
			for (int i = 0; i < mutationCount; i++) {
				int fromIndex = rng.nextInt(cityManager.n);
				int mutationAmount = mutationAmountVariable.nextValue();
				int toIndex = (fromIndex + mutationAmount) % cityManager.n;
				if (toIndex < 0) {
					toIndex += cityManager.n;
				}				
				// swap the selected city
				candidate.swap(cityManager.getCity(fromIndex+1), cityManager.getCity(toIndex+1));
				/*
				City tmp = newCandidate.get(fromIndex);
				newCandidate.set(fromIndex, newCandidate.get(toIndex));
				newCandidate.set(toIndex, tmp);
				*/
			}
			result.add(newCandidate);
		}
		return result;
	}

}
