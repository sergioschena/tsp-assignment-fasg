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
		this.cities = cities;
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
		return pa > pc ? 
				pb < pc || pa < pb :
				pa < pb && pb < pc;
	}

	@Override
	public void flip(City a, City b) {
		// TODO Auto-generated method stub
		
	}
	
	public void flipEdge(City a, City b, City c, City d){
		int pb = pos(b);
		int pd = pos(d);
		
		int cnt = (pb<pd ? pd-pb-1: size-pd-pb-1)/2; 
		for(; cnt >= 0; cnt--){
			swap(pb,pd);
			pb = (pb+1)%size;
			pd = (pd-1)%size;
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
		StringBuffer sb = new StringBuffer("Tour:");
		for(City c : cities){
			sb.append(" "+c.getCity()+" ("+c.getX()+", "+c.getY()+") ");
		}
		return sb.toString();
	}

}