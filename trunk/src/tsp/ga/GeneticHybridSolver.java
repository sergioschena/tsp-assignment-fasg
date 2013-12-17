package tsp.ga;

import java.util.ArrayList;
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
import org.uncommons.watchmaker.framework.termination.ElapsedTime;
import org.uncommons.watchmaker.framework.termination.GenerationCount;
import org.uncommons.watchmaker.framework.termination.Stagnation;

import tsp.model.CityManager;
import tsp.model.Explorer;
import tsp.model.Solution;

public class GeneticHybridSolver implements Explorer {

	private final CityManager cityManager;
	private final Random rng = new MersenneTwisterRNG();
	private final CandidateFactory<Solution> candidateFactory;
	private final EvolutionaryOperator<Solution> evolutionScheme;
	private final FitnessEvaluator<Solution> fitnessEvaluator = new SolutionFitnessEvaluator();
	private final SelectionStrategy<? super Solution> selectionStrategy = new StochasticUniversalSampling();
	private final HybridGenerationalEvolutionEngine hybridGenerationalEvolutionEngine;
	private int K = 15;
	
	private int populationSize = 10;
	private int eliteCount = 2;
	private int generationCount = 20;
	
	private int maxGlobalIterations;
	
	private int max_t1 = 25;	
	private int max_y1 = 5;	
	private int max_y2 = 5;
	private int max_yi = 5;
	private int max_lambda = 100;
	
	private int iterations;
	
	public GeneticHybridSolver(CityManager cityManager, int K, Solution type){
		this.cityManager = cityManager;		
		this.K = K;
		
		this.candidateFactory = new ArrayCandidateFactory(this.cityManager, type, this.K);
		
		List<EvolutionaryOperator<Solution>> evolutionaryOperators = new ArrayList<EvolutionaryOperator<Solution>>(2);
		//evolutionaryOperators.add(new SolutionCrossover());
        evolutionaryOperators.add(new SolutionMutation(this.cityManager, new PoissonGenerator(1.5, rng), new PoissonGenerator(1.5, rng)));
		
        this.evolutionScheme  = new EvolutionPipeline<Solution>(evolutionaryOperators);
        
		this.hybridGenerationalEvolutionEngine = new HybridGenerationalEvolutionEngine(candidateFactory, evolutionScheme, fitnessEvaluator, selectionStrategy, rng, cityManager);
	}
	
	public void setParameters(int populationSize, int eliteCount, int generationCount, int maxGlobalIterations, int max_t1, int max_y1, int max_y2, int max_yi, int max_lambda){
		this.populationSize = populationSize;
		this.eliteCount = eliteCount;
		this.generationCount = generationCount;
		
		this.maxGlobalIterations = maxGlobalIterations;
		
		this.max_t1 = max_t1;
		this.max_y1 = max_y1;
		this.max_y2 = max_y2;
		this.max_yi = max_yi;
		this.max_lambda = max_lambda;
	}
	
	@Override
	public Solution explore() {
		TerminationCondition theEvoulutionEngine = (TerminationCondition)hybridGenerationalEvolutionEngine;
		GenerationCount generationCounter = new GenerationCount(generationCount);
		Stagnation stagnationRecognizer = new Stagnation((int)(Math.sqrt(generationCount)), false);
		ElapsedTime elapsedTime = new ElapsedTime(3*60*1000);
		
		//hybridGenerationalEvolutionEngine.setParameters(maxGlobalIterations, maxIntensifierIterations, maxIntensifierNotImprovingIterations, startTenure);
		hybridGenerationalEvolutionEngine.setParameters(maxGlobalIterations, max_t1, max_y1, max_y2, max_yi, max_lambda);
		
		Solution optimum = hybridGenerationalEvolutionEngine.evolve(populationSize, eliteCount, generationCounter, theEvoulutionEngine,stagnationRecognizer,elapsedTime);
		
		iterations = hybridGenerationalEvolutionEngine.getIterations();
		
		return optimum;
	}
	
	public int getIterations(){
		return iterations; 
	}

}
