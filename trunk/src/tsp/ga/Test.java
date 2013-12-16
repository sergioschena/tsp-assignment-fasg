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
		
		testGeneticSolver();
		//testGenetic();
		//testGeneticHybridSolver();
	}
	
	private static void testGeneticHybridSolver(){
		long start = System.currentTimeMillis();
		
		City[] cities = KnownInstances.createPr1002();
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
		long start = System.currentTimeMillis();
		
		City[] cities = KnownInstances.createBerlin52();
		CityManager cityManager = new CityManager(cities,15);
		
		int populationSize = 10;
		int eliteCount = 5;
		int generationCount = 10;
		
		Random rng = new MersenneTwisterRNG();
		
		CandidateFactory<TSSolution> candidateFactory = new ArrayCandidateFactory(cityManager,15);
		
		List<EvolutionaryOperator<TSSolution>> operators = new ArrayList<EvolutionaryOperator<TSSolution>>(2);
		operators.add(new SolutionCrossover());
        operators.add(new SolutionMutation(new PoissonGenerator(1.5, rng),new PoissonGenerator(1.5, rng)));
		EvolutionaryOperator<TSSolution> evolutionScheme  = new EvolutionPipeline<TSSolution>(operators);
		
		FitnessEvaluator<Solution> fitnessEvaluator = new SolutionFitnessEvaluator();
		
		SelectionStrategy<? super TSSolution> selectionStrategy = new StochasticUniversalSampling();
		
		HybridGenerationalEvolutionEngine ee = new HybridGenerationalEvolutionEngine(candidateFactory, evolutionScheme, fitnessEvaluator, selectionStrategy, rng, cityManager);
		ee.setParameters(1000, 50, 5, 5);
		
		TSSolution improved = ee.evolve(populationSize, eliteCount, new GenerationCount(generationCount),(TerminationCondition)ee);
		long end = System.currentTimeMillis();	
		System.out.println("Cost:"+improved.length()+" - "+improved);
		System.out.println("Iterations: "+ee.getIterations());
		System.out.println((double)((end-start)/1000.0));
		
	}
	
	private static void testGeneticSolver(){
		long start,end;
		
		System.out.print("Reading instance... ");
		start = System.currentTimeMillis();
		City[] cities = KnownInstances.createPr1002();
		end = System.currentTimeMillis();
		System.out.println("done in "+((end-start)/1000.0)+" s");
		
		int K = 15;
		System.out.print("Initializing data structure... ");
		start = System.currentTimeMillis();
		CityManager cityManager = new CityManager(cities,K);
		GeneticHybridSolver ghs = new GeneticHybridSolver(cityManager, K);
		ghs.setParameters(10, 5, 10, 1000, 40, 3, 5);
		end = System.currentTimeMillis();
		System.out.println("done in "+((end-start)/1000.0)+" s");
		
		System.out.print("Start solving... ");
		start = System.currentTimeMillis();
		Solution sol = ghs.explore();
		end = System.currentTimeMillis();
		System.out.println("done in "+((end-start)/1000.0)+" s");
		System.out.println("Cost: "+sol.length()+" - "+sol);
		System.out.println("Iterations: "+ghs.getIterations());
		
	}
		
}
