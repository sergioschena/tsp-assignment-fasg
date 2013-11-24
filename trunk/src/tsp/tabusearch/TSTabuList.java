package tsp.tabusearch;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;

public class TSTabuList {
	// TODO: add expiration criteria
	
	public static int DEFAULT_START_TENURE = 10;
	
	/** tabuList 
	 * key: tenure
	 * value: set of moves with <key> tenure 
	*/
	private Map<Integer, HashSet<Move>> tabuList;
	/** tabu moves */
	private HashSet<Move> tabus;
	/** tabu tenure */
	private int currentTenure;
	/** aspiration criteria */
	private AspirationCriteria criteria;
	
	public TSTabuList(AspirationCriteria criteria){
		this(criteria,DEFAULT_START_TENURE);
	}
	
	public TSTabuList(AspirationCriteria criteria, int startTenure){
		tabus = new HashSet<Move>();
		tabuList = new HashMap<Integer,HashSet<Move>>();
		currentTenure = startTenure;
		this.criteria = criteria;
	}
	
	
	/** add a move in the tabu list */	
	public void addTabu(Move m){
		
		if(!tabuList.containsKey(currentTenure)){
			tabuList.put(currentTenure, new HashSet<Move>());
		}
		
		tabuList.get(currentTenure).add(m);
		tabus.add(m);
	}
	
	/** change the tenure of the selected move to the current tabu tenure */
	public void resetTabuTenure(Move m){
		deleteTabu(m);
		addTabu(m);
	}
	
	/** delete a move from the tabu list */
	public void deleteTabu(Move m){
		
		for(HashSet<Move> hs : tabuList.values()){
			if(hs.contains(m)){
				hs.remove(m);
				break;
			}
		}
		tabus.remove(m);
	}
	
	/** check if a move is tabu */
	public boolean isTabu(Move m){
		
		if(tabus.contains(m)){
			return !criteria.isSatisfiedBy(m);
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
			for(Move m : tabuList.get(k)){
				sb.append(i+" "+m.toString()+"\n");
				i++;
			}
		}
		sb.append("-----------------------");
		return sb.toString();
	}
	
}
