package tsp.model;

public interface Solution extends Cloneable {
	
	public City next(City c);
	
	public City previous(City c);
	
	public boolean between(City a, City b, City c);
	
	public void flip(City a, City b);
	
	public int length();
}
