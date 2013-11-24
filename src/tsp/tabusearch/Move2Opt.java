package tsp.tabusearch;

import tsp.model.City;
import tsp.model.Solution;

/** Implements 2-opt move */ 
public class Move2Opt implements Move  {
	
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
	
	/** Evaluate the delta of the move 
	 * if negative => good move
	 * else not improve (it must be thrown out)
	 */
	@Override
	public int evaluate(){
		
		if (eval == Integer.MAX_VALUE){
			
			int ab = objFunc.cost(a,b);
			int bc = objFunc.cost(b,c);
			int cd = objFunc.cost(c,d);
			int ad = objFunc.cost(a,d);
			
			eval = Move2Opt.evaluation(ab, bc, cd, ad);
		}
		
		return eval;
	}
	
	/** static method for evaluation process */
	public static int evaluation(int ab, int bc, int cd, int ad){
		return ab + cd - bc - ad;
	}

	
	@Override
	public void operateOn(Solution sol) {
		//TODO implementare aggiornamento della soluzione con la mossa corrente
		// aggiornare interfaccia Solution???
		// deve clonare???
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
		sb.append("("+a.getCity()+","+b.getCity()+") with ");
		sb.append("("+c.getCity()+","+d.getCity()+")");
		return sb.toString();
	}

}
