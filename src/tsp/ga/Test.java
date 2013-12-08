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
import org.uncommons.watchmaker.framework.termination.GenerationCount;

import tsp.model.City;
import tsp.model.CityManager;
import tsp.model.Solution;
import tsp.tabusearch.TSSolution;

/** Test class for tabu list features 
 * NOT NEEDED 
 */
public class Test {
	
	public static void main(String[] args) {
		
		//testGenetic();
		testGeneticHybridSolver();
	}
	
	private static void testGeneticHybridSolver(){
		long start = System.currentTimeMillis();
		
		City[] cities = KnownInstances.createBerlin52();
		CityManager cityManager = new CityManager(cities,15);
		
		int K = 15;
		
		int maxGlobalIterations = 1000;
		int maxIntensifierIterations = 50;
		int maxIntensifierNotImprovingIterations = (int) (maxIntensifierIterations * 0.1);
		int startTenure = 5;
		
		int populationSize = (int) (Math.sqrt(cities.length/2) + 0.5);
		int eliteCount = (int) (populationSize*0.2 + 0.5);
		int generationCount = 10;
		
		GeneticHybridSolver ghs = new GeneticHybridSolver(cityManager, K);
		ghs.setParameters(populationSize, eliteCount, generationCount, maxGlobalIterations, maxIntensifierIterations, maxIntensifierNotImprovingIterations, startTenure);
		TSSolution best = (TSSolution) ghs.explore();
		
		long end = System.currentTimeMillis();	
		System.out.println("Cost:"+best.length()+" - "+best);
		System.out.println("Iterations: "+ghs.getIterations());
		System.out.println((double)((end-start)/1000.0));
	}
	
	private static void testGenetic(){
		City[] cities = KnownInstances.createBerlin52();
		CityManager cityManager = new CityManager(cities,15);
		
		int populationSize = 10;
		int eliteCount = 2;
		int generationCount = 20;
		
		Random rng = new MersenneTwisterRNG();
		
		CandidateFactory<TSSolution> candidateFactory = new ArrayCandidateFactory(cityManager,15);
		
		List<EvolutionaryOperator<TSSolution>> operators = new ArrayList<EvolutionaryOperator<TSSolution>>(2);
		operators.add(new SolutionCrossover());
        operators.add(new SolutionMutation(new PoissonGenerator(1.5, rng),new PoissonGenerator(1.5, rng)));
		EvolutionaryOperator<TSSolution> evolutionScheme  = new EvolutionPipeline<TSSolution>(operators);
		
		//FitnessEvaluator<TSSolution> fitnessEvaluator = new SolutionFitnessEvaluator(cityManager);
		FitnessEvaluator<Solution> fitnessEvaluator = new SolutionFitnessEvaluator();
		
		SelectionStrategy<? super TSSolution> selectionStrategy = new StochasticUniversalSampling();
		// TODO: make class that modify GenerationalEvolutionEngine
		//GenerationalEvolutionEngine<TSSolution> ee = new GenerationalEvolutionEngine<TSSolution>(candidateFactory, evolutionScheme, fitnessEvaluator, selectionStrategy, rng);
		//ee.setSingleThreaded(true);
		
		HybridGenerationalEvolutionEngine ee = new HybridGenerationalEvolutionEngine(candidateFactory, evolutionScheme, fitnessEvaluator, selectionStrategy, rng, cityManager);
		ee.setParameters(1000, 50, 5, 5);
		
		TSSolution improved = ee.evolve(populationSize, eliteCount, new GenerationCount(generationCount),(TerminationCondition)ee);
		System.out.println("Cost:"+improved.length()+" - "+improved);
		System.out.println("Iterations: "+ee.getIterations());
		
		//long start = System.currentTimeMillis();
		//System.out.println(candidateFactory.generateRandomCandidate(rng));
		//long end = System.currentTimeMillis();
		//System.out.println((double)((end-start)/1000));
		
		/*AspirationCriteria aspirationCriteria = BestEverAspirationCriteria.getInstance();
		//Solution start = candidateFactory.generateRandomCandidate(rng);
		//Solution start = new TSSolution(cities);
		int startTenure = 5;
		int maxIterations = 30;
		int maxNotImprovingIterations = 3;
		TabuSearch ts = new TabuSearch(cityManager, aspirationCriteria, startTenure, maxNotImprovingIterations, maxIterations);
		List<TSSolution> list = candidateFactory.generateInitialPopulation(50, rng);
		for(Solution start: list){
			Solution improved = ts.improve(start);
			System.out.println("Cost: "+start.length()+", tour: "+(TSSolution)start);
			System.out.println("Cost: "+improved.length()+", tour: "+(TSSolution)improved);
			System.out.println("Iterations: "+ts.iterations);
		}
		evolutionScheme.apply(list, rng);
		for(Solution start: list){
			//Solution improved = ts.improve(start);
			System.out.println("Cost: "+start.length()+", tour: "+(TSSolution)start);
			//System.out.println("Cost: "+improved.length()+", tour: "+(TSSolution)improved);
			//System.out.println("Iterations: "+ts.iterations);
		}*/
	}
		
}
