package tsp.ga;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import org.uncommons.maths.random.MersenneTwisterRNG;
import org.uncommons.maths.random.PoissonGenerator;
import org.uncommons.watchmaker.framework.CandidateFactory;
import org.uncommons.watchmaker.framework.EvolutionaryOperator;
import org.uncommons.watchmaker.framework.FitnessEvaluator;
import org.uncommons.watchmaker.framework.SelectionStrategy;
import org.uncommons.watchmaker.framework.TerminationCondition;
import org.uncommons.watchmaker.framework.operators.EvolutionPipeline;
import org.uncommons.watchmaker.framework.selection.StochasticUniversalSampling;
import org.uncommons.watchmaker.framework.termination.GenerationCount;
import org.uncommons.watchmaker.framework.termination.Stagnation;

import tsp.model.CityManager;
import tsp.model.Explorer;
import tsp.model.Solution;
import tsp.tabusearch.TSSolution;

public class GeneticHybridSolver implements Explorer {

	private final CityManager cityManager;
	private final Random rng = new MersenneTwisterRNG();
	private final CandidateFactory<TSSolution> candidateFactory;
	private final EvolutionaryOperator<TSSolution> evolutionScheme;
	private final FitnessEvaluator<Solution> fitnessEvaluator = new SolutionFitnessEvaluator();
	private final SelectionStrategy<? super Solution> selectionStrategy = new StochasticUniversalSampling();
	private final HybridGenerationalEvolutionEngine hybridGenerationalEvolutionEngine;
	private int K = 15;
	
	private int populationSize = 10;
	private int eliteCount = 2;
	private int generationCount = 20;
	
	private int maxGlobalIterations;
	private int maxIntensifierIterations;
	private int maxIntensifierNotImprovingIterations;
	private int startTenure;
	
	private int iterations;
	
	public GeneticHybridSolver(CityManager cityManager, int K){
		this.cityManager = cityManager;		
		this.K = K;
		
		this.candidateFactory = new ArrayCandidateFactory(this.cityManager,this.K);
		
		List<EvolutionaryOperator<TSSolution>> evolutionaryOperators = new ArrayList<EvolutionaryOperator<TSSolution>>(2);
		evolutionaryOperators.add(new SolutionCrossover());
        evolutionaryOperators.add(new SolutionMutation(new PoissonGenerator(1.5, rng),new PoissonGenerator(1.5, rng)));
		
        this.evolutionScheme  = new EvolutionPipeline<TSSolution>(evolutionaryOperators);
        
		this.hybridGenerationalEvolutionEngine = new HybridGenerationalEvolutionEngine(candidateFactory, evolutionScheme, fitnessEvaluator, selectionStrategy, rng, cityManager);
	}
	
	public void setParameters(int populationSize, int eliteCount, int generationCount, int maxGlobalIterations, 
			int maxIntensifierIterations, int maxIntensifierNotImprovingIterations, int startTenure){
		this.populationSize = populationSize;
		this.eliteCount = eliteCount;
		this.generationCount = generationCount;
		
		this.maxGlobalIterations = maxGlobalIterations;
		this.maxIntensifierIterations = maxIntensifierIterations;
		this.maxIntensifierNotImprovingIterations = maxIntensifierNotImprovingIterations;
		this.startTenure = startTenure;
	}
	
	@Override
	public Solution explore() {
		TerminationCondition theEvoulutionEngine = (TerminationCondition)hybridGenerationalEvolutionEngine;
		GenerationCount generationCounter = new GenerationCount(generationCount);
		Stagnation stagnationRecognizer = new Stagnation((int)(Math.sqrt(generationCount)), false);
		
		hybridGenerationalEvolutionEngine.setParameters(maxGlobalIterations, maxIntensifierIterations, maxIntensifierNotImprovingIterations, startTenure);
		Solution optimum = hybridGenerationalEvolutionEngine.evolve(populationSize, eliteCount, generationCounter, theEvoulutionEngine,stagnationRecognizer);
		
		iterations = hybridGenerationalEvolutionEngine.getIterations();
		
		if(iterations < maxGlobalIterations){
			hybridGenerationalEvolutionEngine.setParameters(maxGlobalIterations-iterations, maxIntensifierIterations*2, maxIntensifierNotImprovingIterations, startTenure);
			Solution candidate = hybridGenerationalEvolutionEngine.evolve(populationSize/2, eliteCount, Arrays.asList((TSSolution)optimum), generationCounter, theEvoulutionEngine,stagnationRecognizer);
			
			iterations += hybridGenerationalEvolutionEngine.getIterations();
			
			if(candidate.length() < optimum.length()){
				optimum = candidate;
			}
		}
		
		return optimum;
	}
	
	public int getIterations(){
		return iterations; 
	}

}
