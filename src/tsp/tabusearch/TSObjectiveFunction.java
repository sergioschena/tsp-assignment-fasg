package tsp.tabusearch;

import tsp.model.City;
import tsp.model.CityManager;
import tsp.model.Solution;

public class TSObjectiveFunction {
	//TODO: change Solution interface according to this implementation
	private CityManager cityManager;
	
	public TSObjectiveFunction(CityManager cm){
		this.cityManager = cm;
	}
	
	public int cost(City a, City b) {
		return cityManager.cost(a, b);
	}
	
	public int evaluate(Solution s){
		int val = 0;
		City start = s.startFrom();
		City act = start;
		do{
			val += cost(act,s.next(act));
			act = s.next(act);
		}while(!act.equals(start));
		
		//s.setLength(val);
		
		return val;
	}

}
