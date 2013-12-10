package tsp.model;

public interface InitialSolutionGenerator {
	
	public City[] generate();
	
	public City[] generate(int k);
	
}
