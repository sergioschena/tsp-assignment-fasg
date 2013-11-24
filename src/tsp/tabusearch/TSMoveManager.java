package tsp.tabusearch;

import tsp.model.City;


public class TSMoveManager {
	// TODO: It needs to implement the AspirationCriteria interface???
	private TSTabuList tabuList;
	private TSObjectiveFunction objFunct;
	
	public TSMoveManager(TSTabuList tabuList, TSObjectiveFunction objFunct) {
		this.tabuList = tabuList;
		this.objFunct = objFunct;
	}
	
	public Move2Opt nextMove(TSSolution s){
		Move2Opt best = null;
		int bestValue = Integer.MAX_VALUE;
		
		City start = s.startFrom();
		City act = start; //a node
		
		while(!s.next(act).equals(start)){
			City next = s.next(act); // b node
			City xchg; // d node
			Move2Opt candidate = new Move2Opt(act, next, null, null, objFunct);
			Move2Opt bestCandidate = new Move2Opt(act, next, null, null, objFunct);
			int bcValue = Integer.MAX_VALUE;
			for(xchg = s.next(next); !xchg.equals(start) ; xchg = s.next(xchg)){
				candidate.d = xchg;
				candidate.c = s.next(xchg);				
				if(candidate.updateEvaluation() < bcValue && !tabuList.isTabu(candidate)){
					bestCandidate = (Move2Opt) candidate.clone();
					bcValue = bestCandidate.eval;
				}
			}
			if(bcValue < bestValue){
				best = bestCandidate;
				bestValue = bcValue;
			}
			act = next;
		}
		
		return best;
	}

}
