package tsp.tabusearch;

import tsp.model.City;
import tsp.model.Solution;

/** Implements 2-opt move */ 
public class Move3Opt implements Move {
	
	City a;
	City b;
	City c;
	City d;
	City e;
	City f;
	TSObjectiveFunction objFunc;
	int eval;
	
	public Move3Opt(City a, City b, City c, City d, City e, City f, TSObjectiveFunction objFunct){
		this.a = a;
		this.b = b;
		this.c = c;
		this.d = d;
		this.e = e;
		this.f = f;
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
		if( a == null || b == null || c == null || d == null || e == null || f == null){
			
			eval = Integer.MAX_VALUE;
			
		}else{
		
			int ab = objFunc.cost(a,b);
			int cd = objFunc.cost(c,d);
			int ef = objFunc.cost(e,f);
			int bc = objFunc.cost(b,c);
			int de = objFunc.cost(d,e);
			int af = objFunc.cost(a,f);
		
			eval = Move3Opt.evaluation(ab, cd, ef, bc, de, af);
		}
		return eval;
	}
	
	/** static method for evaluation process */
	public static int evaluation(int ab, int cd, int ef, int bc, int de, int af){
		return  bc + de + af - ab - cd - ef;
	}

	
	@Override
	public void operateOn(Solution sol) {
		sol.flip(a, d);
		sol.flip(f, a);
		sol.updateLength(eval);
	}
	
	/** This method must be override the Object class one. Needed in tabu list class */
	@Override
	public boolean equals(Object o){
		Move3Opt oth = (Move3Opt)o; 
		return oth.a == a && oth.b == b && oth.c == c && oth.d == d && oth.e == e && oth.f == f;
		
	}
	
	/** This method must be override the Object class one. Needed in tabu list class */
	@Override
	public int hashCode(){
		return a.getCity()*13 + b.getCity()*131 + c.getCity()*17 + d.getCity()*173 + e.getCity()*23 + f.getCity()*233;
	}
	
	/** toString for debugging */
	@Override
	public String toString(){
		StringBuffer sb = new StringBuffer("Replace ");
		sb.append("("+a.getCity()+","+b.getCity()+") + ");
		sb.append("("+d.getCity()+","+c.getCity()+") + ");
		sb.append("("+f.getCity()+","+e.getCity()+")");
		sb.append(" with ("+f.getCity()+","+a.getCity()+") + ");
		sb.append("("+e.getCity()+","+d.getCity()+") + ");
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
