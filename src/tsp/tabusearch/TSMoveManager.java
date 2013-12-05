package tsp.tabusearch;

import tsp.model.City;
import tsp.model.CityManager;
import tsp.model.Solution;


public class TSMoveManager {
	// TODO: It needs to implement the AspirationCriteria interface???
	private TSTabuList tabuList;
	private TSObjectiveFunction objFunct;
	private CityManager cityManager;
	
	public TSMoveManager(TSTabuList tabuList, TSObjectiveFunction objFunct, CityManager cityManager) {
		this.tabuList = tabuList;
		this.objFunct = objFunct;
		this.cityManager = cityManager;
	}
	
	public Move2Opt nextMove(Solution s){
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
				if(candidate.updateEvaluation() < bcValue && !tabuList.isTabu(s,candidate)){
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
	
	public Move2Opt nextMoveTrunc(Solution s){
		Move2Opt best = null;
		int bestMoveValue = Integer.MAX_VALUE;
		
		City start = s.startFrom();
		City ta = start; //a node
		
		while(!s.next(ta).equals(start)){
			City tb = s.next(ta); // b node
			City[] neighbors = cityManager.getNearest(tb);
			Move2Opt candidate = new Move2Opt(ta, tb, null, null, objFunct);
			Move2Opt bestCandidate = null;
			int bestCandidateValue = Integer.MAX_VALUE;
			for(City tc : neighbors){
				candidate.c = tc;
				candidate.d = s.previous(tc);
				if(candidate.updateEvaluation() < bestCandidateValue && !tabuList.isTabu(s,candidate)){
					bestCandidate = (Move2Opt) candidate.clone();
					bestCandidateValue = bestCandidate.eval;
				}
			}
			if(bestCandidateValue < bestMoveValue){
				best = bestCandidate;
				bestMoveValue = bestCandidateValue;
			}
			ta = tb;
		}
		
		return best;
	}
	
	public Move3Opt nextMoveTrunc3(Solution s){
		Move3Opt best = null;
		int bestMoveValue = Integer.MAX_VALUE;
		
		City start = s.startFrom();
		City ta = start; //a node
		
		while(!s.next(ta).equals(start)){
			City tb = s.next(ta); // b node
			City[] neighborsTb = cityManager.getNearest(tb);
			Move3Opt candidate = new Move3Opt(ta, tb, null, null, null, null, objFunct);
			Move3Opt bestCandidate = null;
			int bestCandidateValue = Integer.MAX_VALUE;
			
			for(City tc : neighborsTb){
				
				if( s.between(ta, tb, tc) ){
					
					candidate.c = tc;
					candidate.d = s.previous(tc);
					City[] neighborsTd = cityManager.getNearest(candidate.d);
					
					for(City te : neighborsTd){						
						
						if(s.between(tc, te, ta)){
							candidate.e = te;
							candidate.f = s.previous(te);
						
							if(candidate.updateEvaluation() < bestCandidateValue && !tabuList.isTabu(s,candidate)){
				 				bestCandidate = (Move3Opt) candidate.clone();
								bestCandidateValue = bestCandidate.eval;
							}
						}
						
					}
				
				}
			
			}
			
			if(bestCandidateValue < bestMoveValue){
				best = bestCandidate;
				bestMoveValue = bestCandidateValue;
			}
	
			ta = tb;
		
		}
		
		return best;
	}

}
