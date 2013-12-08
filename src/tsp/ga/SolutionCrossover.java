package tsp.ga;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.uncommons.maths.number.ConstantGenerator;
import org.uncommons.maths.number.NumberGenerator;
import org.uncommons.maths.random.Probability;
import org.uncommons.watchmaker.framework.operators.AbstractCrossover;

import tsp.model.City;
import tsp.tabusearch.TSSolution;

public class SolutionCrossover extends AbstractCrossover<TSSolution> {
	
	public SolutionCrossover()
    {
        this(Probability.ONE);
    }
	
    public SolutionCrossover(Probability crossoverProbability)
    {
        super(2, // Requires exactly two cross-over points.
              crossoverProbability);
    }

    public SolutionCrossover(NumberGenerator<Probability> crossoverProbabilityVariable)
    {
        super(new ConstantGenerator<Integer>(2), // Requires exactly two cross-over points.
              crossoverProbabilityVariable);
    }
	
	@Override
	protected List<TSSolution> mate(TSSolution parent1, TSSolution parent2,
			int numberOfCrossoverPoints, Random rng) {

        TSSolution offspring1 = (TSSolution) parent1.clone();
        TSSolution offspring2 = (TSSolution) parent2.clone();

        int point1 = rng.nextInt(parent1.getSize());
        int point2 = rng.nextInt(parent1.getSize());

        int length = point2 - point1;
        if (length < 0)
        {
            length += parent1.getSize();
        }

        Map<City, City> mapping1 = new HashMap<City, City>(length * 2); // Big enough map to avoid re-hashing.
        Map<City, City> mapping2 = new HashMap<City, City>(length * 2);
        for (int i = 0; i < length; i++)
        {
            int index = (i + point1) % parent1.getSize();
            City item1 = offspring1.get(index);
            City item2 = offspring2.get(index);
            offspring1.set(index, item2);
            offspring2.set(index, item1);
            mapping1.put(item1, item2);
            mapping2.put(item2, item1);
        }

        checkUnmappedElements(offspring1, mapping2, point1, point2);
        checkUnmappedElements(offspring2, mapping1, point1, point2);

        List<TSSolution> result = new ArrayList<TSSolution>(2);
        result.add(offspring1);
        result.add(offspring2);
        return result;
    }
	
	private void checkUnmappedElements(TSSolution offspring, Map<City, City> mapping,
			int mappingStart, int mappingEnd) {
		for (int i = 0; i < offspring.getSize(); i++) {
			if (!isInsideMappedRegion(i, mappingStart, mappingEnd)) {
				City mapped = offspring.get(i);
				while (mapping.containsKey(mapped)) {
					mapped = mapping.get(mapped);
				}
				offspring.set(i, mapped);
			}
		}
	}
	
	private boolean isInsideMappedRegion(int position, int startPoint,
			int endPoint) {
		boolean enclosed = (position < endPoint && position >= startPoint);
		boolean wrapAround = (startPoint > endPoint && (position >= startPoint || position < endPoint));
		return enclosed || wrapAround;
	}

}
