package PSO;

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
