package tsp.tabusearch;

import java.util.Collection;

import tsp.model.City;
import tsp.model.Solution;


public class TSSolution implements Solution {
	// TODO: change Solution interface according to this implementation
	private City[] cities;
	private int size;
	private int length;
	
	public TSSolution(Collection<City> cities) {
		this((City[]) cities.toArray());
	}
	
	public TSSolution(City[] cities) {
		this.cities = cities.clone();
		this.size = this.cities.length;
		this.length = Integer.MAX_VALUE;
	}
	/*
	public Object clone(){
		TSSolution cl = (TSSolution) super.clone();
		return cl;
	}*/

	public City startFrom(){
		return cities[0];
	}
	
	@Override
	public City next(City c) {
		int p = pos(c);
		
		return p >= 0 ? cities[(p+1)%size] : null;
	}

	@Override
	public City previous(City c) {
		int p = pos(c);
		
		return p >= 0 ? cities[(size+p-1)%size] : null;
	}

	@Override
	public boolean between(City a, City b, City c) {
		int pa = pos(a);
		int pb = pos(b);
		int pc = pos(c);
		
		return between(pa, pb, pc);
	}
	
	private boolean between(int pa, int pb, int pc) {
		if(pa==pc)
			return false;
		
		return pa > pc ? 
				pb < pc || pa < pb :
				pa < pb && pb < pc;
	}

	@Override
	public void flip(City a, City b) {
		int src = pos(next(a));
		int dst = pos(b);
		
		if(src==dst)
			return;
		
		int cnt = (src<dst ? dst-src-1: size+dst-src-1)/2; 
		for(; cnt >= 0; cnt--){
			swap(src,dst);
			src = (src+1)%size;
			dst = (size+dst-1)%size;
		}		
	}
	
	private int pos(City c){
		for(int i = 0; i < size; i++){
			if(cities[i].equals(c)){
				return i;
			}
		}
		return -1;
	}
	
	private void swap(int s, int d){
		City t = cities[s];
		cities[s] = cities[d];
		cities[d] = t;
	}

	@Override
	public int length() {
		return length;
	}
	
	public void updateLength(int delta){
		length += delta;
	}
	
	public void setLength(int length){
		this.length = length;
	}
	
	
	@Override
	public String toString(){
		StringBuffer sb = new StringBuffer();
		for(City c : cities){
			//sb.append(" "+c.getCity()+" ("+c.getX()+", "+c.getY()+") ");
			sb.append(c.getCity()+" - ");
		}
		return sb.toString();
	}
	
	@Override
	public Object clone(){
		try {
			TSSolution cl = (TSSolution) super.clone();
			cl.cities = cities.clone();
			return cl;
		} catch (CloneNotSupportedException e) {
			e.printStackTrace();
			return null;
		}
		
	}
	
	public Solution getSolutionFromCities(City[] cities){
		return new TSSolution(cities);
	}

	

}
