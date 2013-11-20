package LK;

import tsp.model.*;


public class LK_Intesifier implements Intensifier {
	
	// miglior guadagno registrato
	int best_gain = 0;
	
	// miglior tour registrato
	Solution best_solution = null;

	@Override
	public Solution improve(Solution start) {
		
		best_solution = start;
		
		return best_solution;
	}

}
