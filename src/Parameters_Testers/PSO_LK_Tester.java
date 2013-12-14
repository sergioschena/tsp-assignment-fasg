package Parameters_Testers;

public class PSO_LK_Tester implements Tester {
	
	int runs_number = 10;
	
	long distance_matrix_time = 0;
	
	long[] exploring_times;
	
	long[] improving_times;
	
	int[] tour_lengths;
	
	int min_tour_length;
	
	int max_tour_length;
	
	long[] solution_times;
	
	long[] initial_solution_times;
	
	long[] explorer_times;
	
	long[] intensifier_times;
	
//-------------------------------------------------------------------------------------
//	Parametri
//-------------------------------------------------------------------------------------
	
	//Parametri LK
	
	//numero di città iniziali da valutare al massimo
	//valori di test: 25, 50, 75, 100
	private int max_t1 = 10;
	
	//numero di archi y1 da valutare al massimo
	//valori di test: 5, 15, 35
	private int max_y1 = 5;
	
	//numero di archi y2 da valutare al massimo
	//valori di test: 5, 15, 35
	private int max_y2 = 5;

	//numero di archi yi da valutare al massimo
	//valori di test: 5, 15, 35
	private int max_yi = 5;
	
	//numero massimo di archi scambiabili a ogni iterazione
	//valori di test: 100, 250, 500, 750
	private int max_lambda = 50;
	
	
	//Parametri PSO
	
	//numero massimo di iterazioni
	//valori di test: 10, 50, 75, 100
	private int max_iter = 10;
	
	//numero di particelle
	//valori di test: 10, 50, 75
	private int num_particles = 20;
	
	//fattore di inerzia
	//valori di test: 0.3, 0.6, 0.9
	private double weight = 0.5;
	
	//coefficiente di apprendimento dalla miglior particella locale
	//valori di test: 1.2, 1.7, 2.2
	private double c1 = 1.5;
	
	//coefficiente di apprendimento dalla miglior particella globale
	//valori di test: 1.5, 2, 2.5
	private double c2 = 2;
		
	//coefficiente di mutazione
	//valori di test: 1.75, 2.25, 2.75
	private double c3 = 2;
	
//-------------------------------------------------------------------------------------
//	Metodi
//-------------------------------------------------------------------------------------
	@Override
	public void setTotalRuns(int runs_number) {
		// TODO Auto-generated method stub

	}

	@Override
	public void updateTests() {
		// TODO Auto-generated method stub

	}

	@Override
	public long getDistanceMatrixTime() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public long getAVGExploringTime() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public long getAVGImprovingTime() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getAVGTourLength() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getMINTourLength() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getMAXTourLength() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public double getErrorFromOptimum() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public long getAVGSolutionTime() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public long getAVGInitialSolutionTime() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public long getAVGExplorerConstructionTime() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public long getAVGIntensifierTime() {
		// TODO Auto-generated method stub
		return 0;
	}

}
