package tsp.ga;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.uncommons.maths.number.ConstantGenerator;
import org.uncommons.maths.number.NumberGenerator;
import org.uncommons.watchmaker.framework.EvolutionaryOperator;

import tsp.model.City;
import tsp.tabusearch.TSSolution;

public class SolutionMutation implements EvolutionaryOperator<TSSolution> {

	private final NumberGenerator<Integer> mutationCountVariable;
	private final NumberGenerator<Integer> mutationAmountVariable;

	
	public SolutionMutation() {
		this(1, 1);
	}

	public SolutionMutation(int mutationCount, int mutationAmount) {
		this(new ConstantGenerator<Integer>(mutationCount),
				new ConstantGenerator<Integer>(mutationAmount));
	}

	
	public SolutionMutation(NumberGenerator<Integer> mutationCount,
			NumberGenerator<Integer> mutationAmount) {
		this.mutationCountVariable = mutationCount;
		this.mutationAmountVariable = mutationAmount;
	}

	public List<TSSolution> apply(List<TSSolution> selectedCandidates, Random rng) {
		List<TSSolution> result = new ArrayList<TSSolution>(selectedCandidates.size());
		for (TSSolution candidate : selectedCandidates) {
			TSSolution newCandidate = (TSSolution) candidate.clone();
			int mutationCount = Math.abs(mutationCountVariable.nextValue());
			for (int i = 0; i < mutationCount; i++) {
				int fromIndex = rng.nextInt(newCandidate.getSize());
				int mutationAmount = mutationAmountVariable.nextValue();
				int toIndex = (fromIndex + mutationAmount)
						% newCandidate.getSize();
				if (toIndex < 0) {
					toIndex += newCandidate.getSize();
				}
				// swap the selected city
				City tmp = newCandidate.get(fromIndex);
				newCandidate.set(fromIndex, newCandidate.get(toIndex));
				newCandidate.set(toIndex, tmp);
			}
			result.add(newCandidate);
		}
		return result;
	}

}
