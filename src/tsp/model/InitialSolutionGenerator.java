package tsp.model;

public interface InitialSolutionGenerator {
	
	public Solution generate();
	
	public Solution generate(int k);
	
}
