package Solution_Data_Structure;

import java.util.*;

import tsp.model.City;
import tsp.model.Solution;

public class Two_level_tree implements Solution {

	LinkedList<Segment> segmenti;
	int l;  //memorizza quanti segmenti sono presenti
	
	public Two_level_tree (City[] vettore, int n){
		int l1=0;
		Client primo=null;
		Client ultimo=null;
		Client attuale=null;
		for(int i=0; i<n;i++){
			segmenti= new LinkedList<Segment>();
			Segment s=new Segment(l1,2);
			Client a= new Client(vettore[i],s);
			if(i==0){ primo=a;}
			else { attuale.setNext(a);
				   a.setPrevious(attuale);}
			s.addClient(a);
			i++;
			if(vettore[i]!=null)
			   			{Client b=new Client(vettore[i],s);
						s.addClient(b);
						attuale=b;
						b.setPrevious(a);
						a.setNext(b);
						} else {ultimo=a;
								ultimo.setNext(primo);}
			segmenti.add(s);
			i++;
			l1++;
			
		}
		primo.setPrevious(ultimo);
		l=l1;
	}
	
	@Override
	public City next(City c) {
		// TODO Auto-generated method stub
			for(Segment s:segmenti){
				Client a=s.isCity(c);
				if(a!=null){
					return a.getNext().getCity();
				}
					
			}
		return null;
	}

	@Override
	public City previous(City c) {
		// TODO Auto-generated method stub
		for(Segment s:segmenti){
			Client a=s.isCity(c);
			if(a!=null){
				return a.getPrevious().getCity();
			}
		}
		return null;
	}

	@Override
	public boolean between(City a, City b, City c) {
		// TODO Auto-generated method stub
		/* boolean f=false;
		int ia=città.indexOf(a);
		int ib=città.indexOf(b);
		int ic=città.indexOf(c);
		if(ia<ic){
			if(ia<ib && ib<ic){
				f=true;				//caso a...b...c
			}
		}
		
		if(ic<ia){
			if(ia<ib && ib>ic){
				f=true;				//caso c...a...b
			}
		}
		return f;*/
		return false;
	}

	@Override
	public void flip(City a, City b) {
		// TODO Auto-generated method stub
		Segment sa=null;
		Segment sb=null;
		for(Segment s:segmenti){
			if(s.isCity(a)!=null){
				sa=s;
			}
			if(s.isCity(b)!=null){
				sb=s;
			}
		}
		
		sa.setReverse(-sa.getReverse());
		sb.setReverse(-sb.getReverse());
		
	}

	@Override
	public int length() {
		// TODO Auto-generated method stub
		return 0;
	}

}
