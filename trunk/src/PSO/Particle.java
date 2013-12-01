package PSO;

import tsp.model.Solution;

class Particle implements Cloneable {
	
	Solution position;
	
	PSO_Edge[] velocity;
	
	Particle local_best_particle;
	
	//TODO costruttore
	
	public int getLength(){
		return position.length();
	}
	
	public int getLocalBestLength(){
		return local_best_particle.position.length();
	}

	protected Object clone() {
		try {
			
			Particle p = (Particle) super.clone();
			p.position = (Solution)position.clone();
			p.velocity = velocity.clone();
			
			//FIXME loop di clone?
			p.local_best_particle = (Particle) local_best_particle.clone();
			return p;
			
		} catch (CloneNotSupportedException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	void update(Particle global_best, double w, double c1, double c2, double c3){
		
		updateVelcity(global_best, w, c1, c2);
		
		updatePosition(c3);
		
	}
	
	private void updateVelcity(Particle global_best, double w, double c1, double c2){
		
		//TODO
		
	}
	
	private void updatePosition(double c3){
		
		//TODO
		
	}
	
	

}
