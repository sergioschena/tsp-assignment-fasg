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

import tsp.lk.LKIntesifier;
import tsp.model.CityManager;
import tsp.model.Solution;
import tsp.tabusearch.AspirationCriteria;
import tsp.tabusearch.BestEverAspirationCriteria;

public class HybridGenerationalEvolutionEngine implements
		EvolutionEngine<Solution>,TerminationCondition {

	private final Set<EvolutionObserver<? super Solution>> observers = new CopyOnWriteArraySet<EvolutionObserver<? super Solution>>();

	private final Random rng;
	private final CandidateFactory<Solution> candidateFactory;
	private final FitnessEvaluator<? super Solution> fitnessEvaluator;
	private final EvolutionaryOperator<Solution> evolutionScheme;
    private final SelectionStrategy<? super Solution> selectionStrategy;
    
    private final CityManager cityManager;
    private final static AspirationCriteria aspirationCriteria = BestEverAspirationCriteria.getInstance();
    
    private LKIntesifier intensifier;
    
    private int max_t1;
    private int max_y1;
    private int max_y2;
	private int max_yi;
	private int max_lambda;

	private int maxGlobalIterations;
	private int iterations;
	
	private List<TerminationCondition> satisfiedTerminationConditions;

	public HybridGenerationalEvolutionEngine(CandidateFactory<Solution> candidateFactory, EvolutionaryOperator<Solution> evolutionScheme, 
			FitnessEvaluator<? super Solution> fitnessEvaluator, SelectionStrategy<? super Solution>selectionStrategy, Random rng, CityManager cityManager) {
		this.candidateFactory = candidateFactory;
		this.evolutionScheme = evolutionScheme;
		this.fitnessEvaluator = fitnessEvaluator;
		this.selectionStrategy = selectionStrategy;
		this.rng = rng;
		this.cityManager = cityManager;
		
		intensifier = new LKIntesifier(this.cityManager);
	}
	
	public void setParameters(int maxGlobalIterations, int max_t1, int max_y1, int max_y2, int max_yi, int max_lambda){
		this.maxGlobalIterations = maxGlobalIterations;
		
		this.max_t1 = max_t1;
		this.max_y1 = max_y1;
		this.max_y2 = max_y2;
		this.max_yi = max_yi;
		this.max_lambda = max_lambda;
		
		intensifier.setParam(max_t1, max_y1, max_y2, max_yi, max_lambda);
	}

	public Solution evolve(int populationSize, int eliteCount, TerminationCondition... conditions) {
		return evolve(populationSize, eliteCount, Collections.<Solution> emptySet(), conditions);
	}

	public Solution evolve(int populationSize, int eliteCount, Collection<Solution> seedCandidates, TerminationCondition... conditions) {
		return evolvePopulation(populationSize, eliteCount, seedCandidates, conditions).get(0).getCandidate();
	}

	public List<EvaluatedCandidate<Solution>> evolvePopulation(int populationSize, int eliteCount, TerminationCondition... conditions) {
		return evolvePopulation(populationSize, eliteCount, Collections.<Solution> emptySet(), conditions);
	}

	@Override
	public List<EvaluatedCandidate<Solution>> evolvePopulation(int populationSize, int eliteCount, Collection<Solution> seedCandidates, TerminationCondition... conditions) {
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

		List<Solution> population = candidateFactory.generateInitialPopulation(populationSize, seedCandidates, rng);

		// Calculate the fitness scores for each member of the initial
		// population.
		
		List<EvaluatedCandidate<Solution>> evaluatedPopulation = evaluatePopulation(population);
		EvolutionUtils.sortEvaluatedPopulation(evaluatedPopulation,fitnessEvaluator.isNatural());
		PopulationData<Solution> data = EvolutionUtils.getPopulationData(evaluatedPopulation, fitnessEvaluator.isNatural(), eliteCount,
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

	protected List<EvaluatedCandidate<Solution>> nextEvolutionStep(List<EvaluatedCandidate<Solution>> evaluatedPopulation, int eliteCount, Random rng) {
		List<Solution> population = new ArrayList<Solution>(evaluatedPopulation.size());

		// First perform any elitist selection.
		List<Solution> elite = new ArrayList<Solution>(eliteCount);
		Iterator<EvaluatedCandidate<Solution>> iterator = evaluatedPopulation.iterator();
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

	protected List<EvaluatedCandidate<Solution>> evaluatePopulation(List<Solution> population) {

		
		List<EvaluatedCandidate<Solution>> evaluatedPopulation = new ArrayList<EvaluatedCandidate<Solution>>(population.size());

		for (Solution candidate : population){
			if((this.maxGlobalIterations-this.iterations) > 0){
					candidate = intensifier.improve(candidate);
					this.iterations += max_t1;
			}
			evaluatedPopulation.add(new EvaluatedCandidate<Solution>(candidate, candidate.length()));
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
	public void addEvolutionObserver(EvolutionObserver<? super Solution> observer) {
		observers.add(observer);
	}

	@Override
	public void removeEvolutionObserver(EvolutionObserver<? super Solution> observer) {
		observers.remove(observer);
	}
	
	private void notifyPopulationChange(PopulationData<Solution> data) {
		for (EvolutionObserver<? super Solution> observer : observers) {
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
