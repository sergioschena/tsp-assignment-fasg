package tsp.pso;

import tsp.model.*;

public class PSOExplorer implements Explorer {
	
//------------------------------------------------------------------------------------------
// Attributi
//------------------------------------------------------------------------------------------

	//oggetto che crea le soluzioni iniziali delle particelle
	private InitialSolutionGenerator generator;
	
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
	
	//tempo massimo di esplorazione in ms (default: 3min = )
	private long max_exploring_time = 18000;
	
	//numero di particelle
	private int num_particles = 20;
	
	//fattore di inerzia
	private double weight = 0.5;
	
	//coefficiente di apprendimento dalla miglior particella locale
	private double c1 = 1.5;
	
	//coefficiente di apprendimento dalla miglior particella globale
	private double c2 = 2;
		
	//coefficiente di mutazione
	private double c3 = 2;
	
//------------------------------------------------------------------------------------------
// Costruttore e setter
//------------------------------------------------------------------------------------------	

	//costruttore. la soluzione passata serve solo a specificare l'implementazione utilizzata
	//per la soluzione
	public PSOExplorer(CityManager m, InitialSolutionGenerator g, Intensifier e, int num_p, 
																				  Solution s){
		city_manager = m;
		generator = g;
		exploiter = e;
		
		num_particles = num_p;
		
		particles = new Particle[num_particles];
		
		int index_global = 0;
		
		for(int i = 0; i<num_particles; i++){
			//FIXME la soluzione generata deve essere diversa particella per particella
			particles[i] = new Particle(s.getSolutionFromCities(generator.generate()), 
																			city_manager);
			
			if(particles[i].getLength() <= particles[index_global].getLength())
				//la particella è la migliore globale attualmente
				index_global = i;
		}
		
		//bisogna copiare la particella globalmente migliore
		global_best_particle = (Particle) particles[index_global].clone();
		
	}
	
	//metodo per configurare i parametri dell'algoritmo
	public void configExplorer(double w, double c1, double c2, double c3, int max_iter, 
																				long max_time){
		this.weight = w;
		this.c1 = c1;
		this.c2 = c2;
		this.c3 = c3;
		this.max_iter = max_iter;
		this.max_exploring_time = max_time;
	}
	
//------------------------------------------------------------------------------------------
// Metodi
//------------------------------------------------------------------------------------------	
	
	public Solution explore() {
		
		int i = 0;
		
		//migliora le particelle iniziali, e aggiorna le migliori locali e globali
		for(Particle p : particles)
			improve(p);
		
		long start_time = System.currentTimeMillis();
		
		long exploring_time = 0;
		
		//FIXME altri criteri di terminazione?
		//FIXME il criterio temporale dovrebbe tenere in conto il tempo medio per una
		//		iterazione: se manca un ms, ma un'iterazione ci mette 1 min si è fuori tempo
		while(i < max_iter && exploring_time < max_exploring_time){
			
			//aggiorna la velocità e la posizione delle particelle
			for(Particle p : particles)
				p.update(global_best_particle, weight, c1, c2, c3);
			
			//migliora le particelle e aggiorna le nuove particelle globali e locali
			for(Particle p : particles)
				improve(p);
			
			i++;
			
			exploring_time = System.currentTimeMillis() - start_time;
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
			//aggiorna la particella migliore locale
			particle.updateLocalBest();
		
		//si ha una nuova particella globale?
		if(new_length < global_best_particle.position.length())
			global_best_particle = (Particle)particle.clone();

	}

}
