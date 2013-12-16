package tsp.ga;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;

import org.uncommons.watchmaker.framework.CandidateFactory;
import org.uncommons.watchmaker.framework.EvaluatedCandidate;
import org.uncommons.watchmaker.framework.EvolutionEngine;
import org.uncommons.watchmaker.framework.EvolutionObserver;
import org.uncommons.watchmaker.framework.EvolutionUtils;
import org.uncommons.watchmaker.framework.EvolutionaryOperator;
import org.uncommons.watchmaker.framework.FitnessEvaluator;
import org.uncommons.watchmaker.framework.PopulationData;
import org.uncommons.watchmaker.framework.SelectionStrategy;
import org.uncommons.watchmaker.framework.TerminationCondition;

import tsp.model.CityManager;
import tsp.model.Solution;
import tsp.tabusearch.AspirationCriteria;
import tsp.tabusearch.BestEverAspirationCriteria;
import tsp.tabusearch.TSSolution;
import tsp.tabusearch.TabuSearch;

public class HybridGenerationalEvolutionEngine implements
		EvolutionEngine<TSSolution>,TerminationCondition {

	private final Set<EvolutionObserver<? super TSSolution>> observers = new CopyOnWriteArraySet<EvolutionObserver<? super TSSolution>>();

	private final Random rng;
	private final CandidateFactory<TSSolution> candidateFactory;
	private final FitnessEvaluator<? super TSSolution> fitnessEvaluator;
	private final EvolutionaryOperator<TSSolution> evolutionScheme;
    private final SelectionStrategy<? super TSSolution> selectionStrategy;
    
    private final CityManager cityManager;
    private final static AspirationCriteria aspirationCriteria = BestEverAspirationCriteria.getInstance();
    
    private int startTenure = 5;
	private int maxIntensifierIterations = 50;
	private int maxIntensifierNotImprovingIterations = 5;
	
	private int maxGlobalIterations;
	private int iterations;
	
	private List<TerminationCondition> satisfiedTerminationConditions;

	public HybridGenerationalEvolutionEngine(CandidateFactory<TSSolution> candidateFactory, EvolutionaryOperator<TSSolution> evolutionScheme, 
			FitnessEvaluator<? super TSSolution> fitnessEvaluator, SelectionStrategy<? super TSSolution>selectionStrategy, Random rng, CityManager cityManager) {
		this.candidateFactory = candidateFactory;
		this.evolutionScheme = evolutionScheme;
		this.fitnessEvaluator = fitnessEvaluator;
		this.selectionStrategy = selectionStrategy;
		this.rng = rng;
		this.cityManager = cityManager;
	}
	
	public void setParameters(int maxGlobalIterations, int maxIntensifierIterations, int maxIntensifierNotImprovingIterations, int startTenure){
		this.maxGlobalIterations = maxGlobalIterations;
		this.maxIntensifierIterations = maxIntensifierIterations;
		this.maxIntensifierNotImprovingIterations = maxIntensifierNotImprovingIterations;
		this.startTenure = startTenure;
	}

	public TSSolution evolve(int populationSize, int eliteCount, TerminationCondition... conditions) {
		return evolve(populationSize, eliteCount, Collections.<TSSolution> emptySet(), conditions);
	}

	public TSSolution evolve(int populationSize, int eliteCount, Collection<TSSolution> seedCandidates, TerminationCondition... conditions) {
		return evolvePopulation(populationSize, eliteCount, seedCandidates, conditions).get(0).getCandidate();
	}

	public List<EvaluatedCandidate<TSSolution>> evolvePopulation(int populationSize, int eliteCount, TerminationCondition... conditions) {
		return evolvePopulation(populationSize, eliteCount, Collections.<TSSolution> emptySet(), conditions);
	}

	@Override
	public List<EvaluatedCandidate<TSSolution>> evolvePopulation(int populationSize, int eliteCount, Collection<TSSolution> seedCandidates, TerminationCondition... conditions) {
		if (eliteCount < 0){
			eliteCount = 1; 
		}else if(eliteCount >= populationSize){
			eliteCount = populationSize;
		}
		if (conditions.length == 0) {
			throw new IllegalArgumentException("At least one TerminationCondition must be specified.");
		}
		
		iterations = 0;
		satisfiedTerminationConditions = null;
		int currentGenerationIndex = 0;
		long startTime = System.currentTimeMillis();

		List<TSSolution> population = candidateFactory.generateInitialPopulation(populationSize, seedCandidates, rng);

		// Calculate the fitness scores for each member of the initial
		// population.
		
		List<EvaluatedCandidate<TSSolution>> evaluatedPopulation = evaluatePopulation(population);
		EvolutionUtils.sortEvaluatedPopulation(evaluatedPopulation,fitnessEvaluator.isNatural());
		PopulationData<TSSolution> data = EvolutionUtils.getPopulationData(evaluatedPopulation, fitnessEvaluator.isNatural(), eliteCount,
				currentGenerationIndex, startTime);
		// Notify observers of the state of the population.
		notifyPopulationChange(data);
		
		
		List<TerminationCondition> satisfiedConditions = EvolutionUtils.shouldContinue(data, conditions);
		while (satisfiedConditions == null) {
			++currentGenerationIndex;
			evaluatedPopulation = nextEvolutionStep(evaluatedPopulation, eliteCount, rng);
			EvolutionUtils.sortEvaluatedPopulation(evaluatedPopulation, fitnessEvaluator.isNatural());
			data = EvolutionUtils.getPopulationData(evaluatedPopulation, fitnessEvaluator.isNatural(), eliteCount, currentGenerationIndex, startTime);
			// Notify observers of the state of the population.
			notifyPopulationChange(data);
			satisfiedConditions = EvolutionUtils.shouldContinue(data, conditions);
		}
		this.satisfiedTerminationConditions = satisfiedConditions;
		return evaluatedPopulation;
	}

	protected List<EvaluatedCandidate<TSSolution>> nextEvolutionStep(List<EvaluatedCandidate<TSSolution>> evaluatedPopulation, int eliteCount, Random rng) {
		List<TSSolution> population = new ArrayList<TSSolution>(evaluatedPopulation.size());

		// First perform any elitist selection.
		List<TSSolution> elite = new ArrayList<TSSolution>(eliteCount);
		Iterator<EvaluatedCandidate<TSSolution>> iterator = evaluatedPopulation.iterator();
		while (elite.size() < eliteCount) {
			elite.add(iterator.next().getCandidate());
		}
		// Then select candidates that will be operated on to create the evolved
		// portion of the next generation.
		population.addAll(selectionStrategy.select(evaluatedPopulation,	fitnessEvaluator.isNatural(), evaluatedPopulation.size() - eliteCount, rng));
		// Then evolve the population.
		population = evolutionScheme.apply(population, rng);
		// When the evolution is finished, add the elite to the population.
		population.addAll(elite);
		
		return evaluatePopulation(population);
	}

	protected List<EvaluatedCandidate<TSSolution>> evaluatePopulation(List<TSSolution> population) {

		final TabuSearch ts = new TabuSearch(cityManager, aspirationCriteria, startTenure, maxIntensifierNotImprovingIterations, maxIntensifierIterations);

		List<EvaluatedCandidate<TSSolution>> evaluatedPopulation = new ArrayList<EvaluatedCandidate<TSSolution>>(population.size());

		for (TSSolution candidate : population){
			if((this.maxGlobalIterations-this.iterations) > 0){
				if((this.maxGlobalIterations-this.iterations) < this.maxIntensifierIterations){
					ts.setParams(startTenure, maxIntensifierNotImprovingIterations, this.maxGlobalIterations-this.iterations);
				}
				Solution improved = ts.improve(candidate);
				candidate = (TSSolution) improved;       	
				this.iterations += ts.iterations;
			}
			evaluatedPopulation.add(new EvaluatedCandidate<TSSolution>(candidate, candidate.length()));
		}

		return evaluatedPopulation;
	}

	@Override
	public List<TerminationCondition> getSatisfiedTerminationConditions() {
		if (satisfiedTerminationConditions == null) {
			throw new IllegalStateException("EvolutionEngine has not terminated.");
		} else {
			return Collections.unmodifiableList(satisfiedTerminationConditions);
		}
	}

	@Override
	public void addEvolutionObserver(EvolutionObserver<? super TSSolution> observer) {
		observers.add(observer);
	}

	@Override
	public void removeEvolutionObserver(EvolutionObserver<? super TSSolution> observer) {
		observers.remove(observer);
	}
	
	private void notifyPopulationChange(PopulationData<TSSolution> data) {
		for (EvolutionObserver<? super TSSolution> observer : observers) {
			observer.populationUpdate(data);
		}
	}
	
	public int getIterations(){
		return this.iterations;
	}

	@Override
	public boolean shouldTerminate(PopulationData<?> populationData) {
		return (this.iterations + 1) >= maxGlobalIterations;
	}
}
