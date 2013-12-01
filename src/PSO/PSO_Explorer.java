package PSO;

import tsp.model.*;

public class PSO_Explorer implements Explorer {
	
//------------------------------------------------------------------------------------------
// Attributi
//------------------------------------------------------------------------------------------

	//oggetto che migliora una particella
	private Intensifier exploiter;
	
	//particelle che formano la popolazione
	private Particle[]	particles;
	
	//manager delle città
	private CityManager	city_manager;
	
	//particella contenente la soluzione migliore trovata
	private Particle global_best_particle;
	
//------------------------------------------------------------------------------------------
// Parametri
//------------------------------------------------------------------------------------------
	
	//numero massimo di iterazioni
	private int max_iter = 10;
	
	//numero di particelle
	private int num_particles = 20;
	
	//fattore di inerzia
	private double weight;
	
	//coefficiente di apprendimento dalla miglior particella locale
	private double c1;
	
	//coefficiente di apprendimento dalla miglior particella globale
	private double c2;
		
	//coefficiente di mutazione
	private double c3;
	
//------------------------------------------------------------------------------------------
// Costruttore e setter
//------------------------------------------------------------------------------------------	

	//TODO costruttore
	
//------------------------------------------------------------------------------------------
// Metodi
//------------------------------------------------------------------------------------------	
	
	private void Init_Explorer(){
		//TODO
	}
	
	public Solution explore() {
		
		int i = 0;
		
		//FIXME altri criteri di terminazione?
		while(i < max_iter){
			
			//migliora le particelle e aggiorna le nuove particelle globali e locali
			for(Particle p : particles)
				improve(p);
			
			//aggiorna la velocità e la posizione delle particelle, e quindi i migliori
			//globali e locali
			for(Particle p : particles)
				p.update(global_best_particle, weight, c1, c2, c3);
					
		}	
		
		return global_best_particle.position;
	}
	
	private void improve(Particle particle){
		
		//migliora la soluzione della particella
		particle.position = exploiter.improve(particle.position);
		
		particle.updateEdges();
		
		int old_length = particle.getLocalBestLength();
		
		int new_length = particle.getLength();
		
		//si ha una nuova miglior particella locale?
		if(new_length < old_length)
			//FIXME quale sarà il local_best del local_best?
			particle.local_best_particle = (Particle)particle.clone();
		
		//si ha una nuova particella globale?
		if(new_length < global_best_particle.position.length())
			global_best_particle = (Particle)particle.clone();

	}

}
