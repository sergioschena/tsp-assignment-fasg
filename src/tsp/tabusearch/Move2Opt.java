package tsp.tabusearch;

import tsp.model.City;
import tsp.model.Solution;

/** Implements 2-opt move */ 
public class Move2Opt implements Move {
	
	City a;
	City b;
	City c;
	City d;
	TSObjectiveFunction objFunc;
	int eval;
	
	public Move2Opt(City a, City b, City c, City d, TSObjectiveFunction objFunct){
		this.a = a;
		this.b = b;
		this.c = c;
		this.d = d;
		this.objFunc = objFunct;
		eval = Integer.MAX_VALUE;
	}
	
	/** 
	 * Return the delta of the move
	 * evaluate the delta if never done
	 */
	@Override
	public int evaluate(){
		
		if (eval == Integer.MAX_VALUE){
			return updateEvaluation();
		}else{
			return eval;
		}
	}
	
	/**
	 * Evaluate the delta of the move
	 * if negative => good move
	 * else not an improve => must be thrown
	 */
	public int updateEvaluation(){
		int ab = objFunc.cost(a,b);
		int bc = objFunc.cost(b,c);
		int cd = objFunc.cost(c,d);
		int ad = objFunc.cost(a,d);
		
		eval = Move2Opt.evaluation(ab, bc, cd, ad);
	
		return eval;
	}
	
	/** static method for evaluation process */
	public static int evaluation(int ab, int bc, int cd, int ad){
		return  bc + ad - ab - cd;
	}

	
	@Override
	public void operateOn(Solution sol) {
		//TODO implementare aggiornamento della soluzione con la mossa corrente
		// aggiornare interfaccia Solution???
		// deve clonare???
		
	}
	
	public void operateOn(TSSolution sol) {
		sol.flipEdge(a, b, c, d);
		sol.updateLength(eval);
	}
	
	/** This method must be override the Object class one. Needed in tabu list class */
	@Override
	public boolean equals(Object o){
		Move2Opt oth = (Move2Opt)o; 
		return oth.a == a && oth.b == b && oth.c == c && oth.d == d;
		
	}
	
	/** This method must be override the Object class one. Needed in tabu list class */
	@Override
	public int hashCode(){
		return a.getCity()*13 + b.getCity()*131 + c.getCity()*17 + d.getCity()*173 ;
	}
	
	/** toString for debugging */
	@Override
	public String toString(){
		StringBuffer sb = new StringBuffer("Replace ");
		sb.append("("+a.getCity()+","+b.getCity()+") and ");
		sb.append("("+d.getCity()+","+c.getCity()+")");
		sb.append(" with ("+a.getCity()+","+d.getCity()+") and ");
		sb.append("("+b.getCity()+","+c.getCity()+")");
		return sb.toString();
	}
	
	/** Clone method. Used by MoveManager */
	public Object clone(){
		try {
			return super.clone();
		} catch (CloneNotSupportedException e) {
			e.printStackTrace();
			return null;
		}
	}

}
