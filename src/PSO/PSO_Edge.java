package PSO;

import java.util.Comparator;

import tsp.model.Edge;

public class PSO_Edge {
	
	double probability;
	
	Edge edge;
	
	public PSO_Edge(double p, Edge e) {
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

class PSO_Edge_Comparator implements Comparator<PSO_Edge>{

	@Override
	public int compare(PSO_Edge e0, PSO_Edge e1) {
		
		return Double.compare(e1.probability, e0.probability);
	}
	
}
