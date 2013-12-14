package Parameters_Testers;

//interfaccia per valutare i parametri degli algoritmi utilizzati
public interface Tester {
	
	//imposta il numero di esecuzioni che verranno effettuate per valutare le medie
	public void setTotalRuns(int runs_number);
	
	//esegue gli algoritmi e ricalcola le medie
	public void updateTests();
	
	//restituisce il tempo necessario a costruire una matrice delle distanze
	public long getDistanceMatrixTime();
	
	//restituisce il tempo medio per eseguire il metodo explore() di un Explorer
	public long getAVGExploringTime();
	
	//restituisce il tempo medio per eseguire il metodo improve() di un Intensifier
	public long getAVGImprovingTime();
	
	//restituisce la lunghezza media delle soluzioni ottenute
	public int getAVGTourLength();
	
	//restituisce la lunghezza minima delle soluzioni ottenute
	public int getMINTourLength();
	
	//restituisce la lunghezza massima delle soluzioni ottenute
	public int getMAXTourLength();
	
	//restituisce l'errore percentuale della soluzione media rispetto a quella ottimale
	public double getErrorFromOptimum();
	
	//restituisce il tempo medio per costruire una soluzione a partire da un array di città
	public long getAVGSolutionTime();
	
	//restituisce il tempo medio per costruire una soluzione iniziale
	public long getAVGInitialSolutionTime();
	
	//restituisce il tempo medio per costruire un oggetto Explorer
	public long getAVGExplorerConstructionTime();
	
	//restituisce il tempo medio per costruire un oggetto Intensifier
	public long getAVGIntensifierTime();
	
}
