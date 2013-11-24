package tsp.tabusearch;

import tsp.model.Solution;

public interface Move extends Cloneable {

	public int evaluate();

	public void operateOn(Solution sol);
	
	public boolean equals(Object o);
	
	public int hashCode();

}