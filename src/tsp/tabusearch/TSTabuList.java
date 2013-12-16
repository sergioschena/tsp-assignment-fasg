package tsp.tabusearch;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;

import tsp.model.City;
import tsp.model.Solution;

public class TSTabuList {
	// TODO: add expiration criteria
	
	public static int DEFAULT_START_TENURE = 10;
	
	/** tabuList 
	 * key: tenure
	 * value: set of moves with <key> tenure 
	*/
	private Map<Integer, HashSet<TabuEdge>> tabuList;
	/** tabu moves */
	private HashSet<TabuEdge> tabus;
	/** tabu tenure */
	private int currentTenure;
	/** aspiration criteria */
	private AspirationCriteria criteria;
	
	public TSTabuList(AspirationCriteria criteria){
		this(criteria,DEFAULT_START_TENURE);
	}
	
	public TSTabuList(AspirationCriteria criteria, int startTenure){
		this.currentTenure = startTenure;
		this.criteria = criteria;
		initialize();
	}
	
	public void initialize(){
		tabus = new HashSet<TabuEdge>(5*(currentTenure+1));
		tabuList = new HashMap<Integer,HashSet<TabuEdge>>(currentTenure*2);
	}
	
	public void addTabu(Move m){	
		if(m instanceof Move2Opt){
			addTabu((Move2Opt)m);
		}else{
			addTabu((Move3Opt)m);
		}
	}
	
	/** delete a move from the tabu list*/
	public void deleteTabu(Move m){		
		if(m instanceof Move2Opt){
			deleteTabu((Move2Opt)m);
		}else{
			deleteTabu((Move3Opt)m);
		}
	}
	
	/** delete a move from the tabu list*/
	public void resetTabuTenure(Move m){		
		if(m instanceof Move2Opt){
			resetTabuTenure((Move2Opt)m);
		}else{
			resetTabuTenure((Move3Opt)m);
		}
	}
	
	/** add a move in the tabu list */	
	public void addTabu(Move2Opt m){		
		addTabuEdge(new TabuEdge(m.a, m.d));
		addTabuEdge(new TabuEdge(m.b, m.c));
	}
	
	/** delete a move from the tabu list*/
	public void deleteTabu(Move2Opt m){		
		deleteTabuEdge(new TabuEdge(m.a, m.d));
		deleteTabuEdge(new TabuEdge(m.b, m.c));
	}
	
	/** delete a move from the tabu list*/
	public void resetTabuTenure(Move2Opt m){		
		resetTabuEdgeTenure(new TabuEdge(m.a, m.d));
		resetTabuEdgeTenure(new TabuEdge(m.b, m.c));
	}
	
	/** add a move in the tabu list */	
	public void addTabu(Move3Opt m){		
		addTabuEdge(new TabuEdge(m.a, m.f));
		addTabuEdge(new TabuEdge(m.b, m.c));
		addTabuEdge(new TabuEdge(m.d, m.e));
	}
	
	/** delete a move from the tabu list*/
	public void deleteTabu(Move3Opt m){		
		deleteTabuEdge(new TabuEdge(m.a, m.f));
		deleteTabuEdge(new TabuEdge(m.b, m.c));
		deleteTabuEdge(new TabuEdge(m.d, m.e));
	}
	
	/** delete a move from the tabu list*/
	public void resetTabuTenure(Move3Opt m){		
		resetTabuEdgeTenure(new TabuEdge(m.a, m.f));
		resetTabuEdgeTenure(new TabuEdge(m.b, m.c));
		resetTabuEdgeTenure(new TabuEdge(m.d, m.e));
	}
	
	/** check if a move is tabu */
	public boolean isTabu(Solution current, Move2Opt m){
		
		if(tabus.contains(new TabuEdge(m.a, m.b)) || tabus.contains(new TabuEdge(m.d, m.c))){
			return !criteria.isSatisfiedBy(current,m);
		}
		
		return false;
	}
	
	public boolean isTabu(Solution current, Move3Opt m){
		
		if(tabus.contains(new TabuEdge(m.a, m.b)) || tabus.contains(new TabuEdge(m.d, m.c)) || tabus.contains(new TabuEdge(m.f, m.e))){
			return !criteria.isSatisfiedBy(current,m);
		}
		
		return false;
	}
	
	/** change current tenure and refresh tabu list */
	public void setTenure(int tenure){
		int delta = tenure - currentTenure;
		
		currentTenure = tenure;
		
		if(delta < 0){
			updateTabuList(delta);
		}
	}
	
	/** getter for current tabu tenure */
	public int getTenure(){
		return currentTenure;
	}
	
	/** when iteration has expired, refresh tabu list */
	public void nextIteration(){
		updateTabuList(-1);
	}
	
	/** add an edge into the tabu list */	
	private void addTabuEdge(TabuEdge te){
		
		if(!tabuList.containsKey(currentTenure)){
			tabuList.put(currentTenure, new HashSet<TabuEdge>(5*(currentTenure+1)));
		}
		
		tabuList.get(currentTenure).add(te);
		tabus.add(te);
	}
	
	/** delete an edge from the tabu list */
	private void deleteTabuEdge(TabuEdge te){		
		for(HashSet<TabuEdge> hs : tabuList.values()){
			if(hs.contains(te)){
				hs.remove(te);
				break;
			}
		}
		tabus.remove(te);
	}
	
	/** change the tenure of the selected move to the current tabu tenure */
	private void resetTabuEdgeTenure(TabuEdge te){
		deleteTabuEdge(te);
		addTabuEdge(te);
	}
	
	/** update the tabu list applying a <delta> to all tenures */
	private void updateTabuList(int delta){
		ArrayList<Integer> keys = new ArrayList<Integer>(tabuList.keySet());
		Collections.sort(keys);
		
		for(Iterator<Integer> it = keys.iterator(); it.hasNext(); ){
			Integer key = it.next();
			int nextKey = key + delta; 
			if(nextKey < 0){
				tabus.removeAll(tabuList.get(key));
			}else{
				tabuList.put(nextKey, tabuList.get(key));
			}
			tabuList.remove(key);
		}
	}
	
	/** toString for debugging */
	public String toString(){
		StringBuffer sb = new StringBuffer();
		for(Integer k : tabuList.keySet()){
			sb.append("Tenure "+k.intValue()+"\n");
			int i = 0;
			for(TabuEdge m : tabuList.get(k)){
				sb.append(i+" "+m.toString()+"\n");
				i++;
			}
		}
		sb.append("-----------------------");
		return sb.toString();
	}
	
	private class TabuEdge {
		
		private City from;
		private City to;
		
		public TabuEdge(City from, City to) {
			if(from.city < to.city){
				this.from = from;
				this.to = to;
			}else{
				this.to = from;
				this.from = to;
			}
		}
		
		@Override
		public boolean equals(Object o){
			TabuEdge oth = (TabuEdge) o;
			return (oth.from.equals(this.from) && oth.to.equals(this.to)) ||
					(oth.to.equals(this.from) && oth.from.equals(this.to));
		}
		
		@Override
		public int hashCode(){
			return from.city*10009 + to.city*17;
		}
		
		public String toString(){
			return "("+from.city+", "+to.city+")";
		}
		
	}

	
}

