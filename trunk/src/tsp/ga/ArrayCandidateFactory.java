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
import tsp.tabusearch.TSSolution;

public class ArrayCandidateFactory implements CandidateFactory<TSSolution> {

	private CityManager cityManager;
	private int K = -1;
	private City[] cities;
	
	public ArrayCandidateFactory(CityManager cityManager){
		this.cityManager = cityManager;
		cities = cityManager.getCities();
	}
	
	public ArrayCandidateFactory(CityManager cityManager, int K){
		this.cityManager = cityManager;
		this.K = K;
		cities = cityManager.getCities();
	}
	
	@Override
	public List<TSSolution> generateInitialPopulation(int populationSize,
			Random rng) {
		
		return generateInitialPopulation(populationSize, null, rng);
	}

	@Override
	public List<TSSolution> generateInitialPopulation(int populationSize,
			Collection<TSSolution> seedCandidates, Random rng) {
		ArrayList<TSSolution> candidate = new ArrayList<TSSolution>(100);
		
		if(seedCandidates != null){
			candidate.addAll(seedCandidates);
		}
		
		for(int i = candidate.size(); i < populationSize; i++){
			candidate.add(generateRandomCandidate(rng));
		}
		
		return candidate;
	}

	@Override
	public TSSolution generateRandomCandidate(Random rng) {
		
		if(K > 0){
			return kNearestCandidate(rng);
		}else{
			return randomCandidate(rng); 
		}
		
	}
	
	private TSSolution randomCandidate(Random rng) {
		int n = cityManager.n;
		City[] sol = new City[n];
		ArrayList<City> tmp = new ArrayList<City>(Arrays.asList(cities));
		
		Collections.shuffle(tmp,rng);
		cities = tmp.toArray(sol);
		
		return new TSSolution(sol);
	}
	
	private TSSolution kNearestCandidate(Random rng) {
		City[] sol = new City[cities.length];
		
		for(int i=1; i<cities.length; i++ ){
			cities[i].visited = false;
		}
		
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
		
		return new TSSolution(sol);
	}

}
