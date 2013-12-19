package tsp.pso;

import java.util.Comparator;

import tsp.model.Edge;

public class PSOEdge {
	
	double probability;
	
	Edge edge;
	
	public PSOEdge(double p, Edge e) {
		probability = p;
		edge = e;
	}

	public double getProbability() {
		return probability;
	}

	public Edge getEdge() {
		return edge;
	}

}

class PSO_Edge_Comparator implements Comparator<PSOEdge>{

	@Override
	public int compare(PSOEdge e0, PSOEdge e1) {
		
		return Double.compare(e1.probability, e0.probability);
	}
	
}
