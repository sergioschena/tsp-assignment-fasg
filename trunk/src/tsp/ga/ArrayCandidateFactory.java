package tsp.ga;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import org.uncommons.watchmaker.framework.CandidateFactory;

import tsp.model.City;
import tsp.model.CityManager;
import tsp.model.Solution;

public class ArrayCandidateFactory implements CandidateFactory<Solution> {

	private CityManager cityManager;
	private int K = -1;
	private City[] cities;
	private Solution type;
	
	public ArrayCandidateFactory(CityManager cityManager, Solution type){
		this.cityManager = cityManager;
		this.type = type;
		cities = cityManager.getCities();
	}
	
	public ArrayCandidateFactory(CityManager cityManager, Solution type, int K){
		this.cityManager = cityManager;
		this.type = type;
		this.K = K;
		cities = cityManager.getCities();
	}
	
	@Override
	public List<Solution> generateInitialPopulation(int populationSize,
			Random rng) {
		
		return generateInitialPopulation(populationSize, null, rng);
	}

	@Override
	public List<Solution> generateInitialPopulation(int populationSize,
			Collection<Solution> seedCandidates, Random rng) {
		ArrayList<Solution> candidate = new ArrayList<Solution>(100);
		
		if(seedCandidates != null){
			candidate.addAll(seedCandidates);
		}
		
		for(int i = candidate.size(); i < populationSize; i++){
			candidate.add(generateRandomCandidate(rng));
		}
		
		return candidate;
	}

	@Override
	public Solution generateRandomCandidate(Random rng) {
		
		if(K > 0){
			return kNearestCandidate(rng);
		}else{
			return randomCandidate(rng); 
		}
		
	}
	
	private Solution randomCandidate(Random rng) {
		int n = cityManager.n;
		City[] sol = new City[n];
		ArrayList<City> tmp = new ArrayList<City>(Arrays.asList(cities));
		
		Collections.shuffle(tmp,rng);
		sol = tmp.toArray(sol);
		
		return type.getSolutionFromCities(sol);
	}
	
	private Solution kNearestCandidate(Random rng) {
		City[] sol = new City[cities.length];
		
		cityManager.clearVisited();
		
		sol[0] = cities[0];
		sol[0].visited = true;
		int currK = K;
		
		for(int i=1; i<cities.length; i++ ){
			City[] bestCurr = cityManager.bestCurrentNearestOf(sol[i-1]);
			
			if(currK > bestCurr.length){
				currK = bestCurr.length;
			}
			
			sol[i] = bestCurr[rng.nextInt(currK)];
			sol[i].visited = true;
		}		
		
		return type.getSolutionFromCities(sol);
	}

}
