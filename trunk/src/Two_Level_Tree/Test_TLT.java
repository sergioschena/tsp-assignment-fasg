package Two_Level_Tree;

import tsp.model.City;
import tsp.model.CityManager;

public class Test_TLT {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
		City cities[] = LK.Test_LK.createCities20();
		
		CityManager manager = new CityManager(cities);
		
		TwoLevelTree solution = new TwoLevelTree(manager, manager.getCities(), 4);
		
		System.out.println(solution);
		
		//between test 
		
		//test città non sovrapposte
		System.out.println("2 tra 2 e 4 : "+solution.between(cities[1], cities[1], cities[3]));
		System.out.println("4 tra 2 e 4 : "+solution.between(cities[3], cities[2], cities[3]));
		System.out.println("3 tra 2 e 2 : "+solution.between(cities[1], cities[2], cities[1]));
		System.out.println("3 tra 4 e 4 : "+solution.between(cities[3], cities[2], cities[3]));
		System.out.println();
		
		//città nello stesso segmento
		System.out.println("3 tra 2 e 4 : "+solution.between(cities[1], cities[2], cities[3]));
		System.out.println("1 tra 2 e 4 : "+solution.between(cities[1], cities[0], cities[3]));
		System.out.println("3 tra 4 e 2 : "+solution.between(cities[3], cities[2], cities[1]));
		System.out.println("1 tra 4 e 2 : "+solution.between(cities[3], cities[0], cities[1]));
		System.out.println();
		
		//città in segmenti diversi
		System.out.println("6 tra 2 e 10 : "+solution.between(cities[1], cities[5], cities[9]));
		System.out.println("14 tra 2 e 10 : "+solution.between(cities[1], cities[13], cities[9]));
		System.out.println("6 tra 10 e 2 : "+solution.between(cities[9], cities[5], cities[1]));
		System.out.println("14 tra 10 e 2 : "+solution.between(cities[9], cities[13], cities[1]));
		System.out.println();
		
		//a e b nello stesso segmento
		System.out.println("3 tra 2 e 10 : "+solution.between(cities[1], cities[2], cities[9]));
		System.out.println("1 tra 2 e 10 : "+solution.between(cities[1], cities[0], cities[9]));
		System.out.println("11 tra 10 e 2 : "+solution.between(cities[9], cities[10], cities[1]));
		System.out.println("9 tra 10 e 2 : "+solution.between(cities[9], cities[8], cities[1]));
		System.out.println();
		
		//b e c nello stesso segmento
		System.out.println("9 tra 2 e 10 : "+solution.between(cities[1], cities[8], cities[9]));
		System.out.println("11 tra 2 e 10 : "+solution.between(cities[1], cities[10], cities[9]));
		System.out.println("3 tra 10 e 2 : "+solution.between(cities[9], cities[2], cities[1]));
		System.out.println("1 tra 10 e 2 : "+solution.between(cities[9], cities[0], cities[1]));
		System.out.println();
		
		//flip test
		System.out.println(solution);
		
		//flip interno
		solution.flip(cities[3], cities[6]);
		System.out.println(solution);
		
		//flip segmenti esterni
		solution.flip(cities[10], cities[8]);
		System.out.println(solution);
		
		//between test con revers attivo
		
		//città nello stesso segmento
		System.out.println("3 tra 2 e 4 : "+solution.between(cities[1], cities[2], cities[3]));
		System.out.println("1 tra 2 e 4 : "+solution.between(cities[1], cities[0], cities[3]));
		System.out.println("3 tra 4 e 2 : "+solution.between(cities[3], cities[2], cities[1]));
		System.out.println("1 tra 4 e 2 : "+solution.between(cities[3], cities[0], cities[1]));
		System.out.println();
		
		//città in segmenti diversi
		System.out.println("6 tra 2 e 10 : "+solution.between(cities[1], cities[5], cities[9]));
		System.out.println("16 tra 2 e 10 : "+solution.between(cities[1], cities[15], cities[9]));
		System.out.println("6 tra 10 e 2 : "+solution.between(cities[9], cities[5], cities[1]));
		System.out.println("16 tra 10 e 2 : "+solution.between(cities[9], cities[15], cities[1]));
		System.out.println();
		
		//a e b nello stesso segmento ( R = 1 ). c in segmento con R = 1
		System.out.println("3 tra 2 e 5 : "+solution.between(cities[1], cities[2], cities[4]));
		System.out.println("1 tra 2 e 5 : "+solution.between(cities[1], cities[0], cities[4]));
		System.out.println();
		
		//a e b nello stesso segmento( R = 1 ). c in segmento con R = 0
		System.out.println("3 tra 2 e 10 : "+solution.between(cities[1], cities[2], cities[9]));
		System.out.println("1 tra 2 e 10 : "+solution.between(cities[1], cities[0], cities[9]));
		System.out.println();
		
		//b e c nello stesso segmento ( R = 1 ). a in segmento con R = 1
		System.out.println("3 tra 5 e 2 : "+solution.between(cities[4], cities[2], cities[1]));
		System.out.println("1 tra 5 e 2 : "+solution.between(cities[4], cities[0], cities[1]));
		System.out.println();
		
		//b e c nello stesso segmento ( R = 1 ). a in segmento con R = 0
		System.out.println("3 tra 10 e 2 : "+solution.between(cities[9], cities[2], cities[1]));
		System.out.println("1 tra 10 e 2 : "+solution.between(cities[9], cities[0], cities[1]));
		System.out.println();
		
		//flip test
		System.out.println(solution);
		
		//flip interno con reverse attivo
		solution.flip(cities[19], cities[16]);
		System.out.println(solution);
		
		//flip con inversione dei segmenti esterni
		solution.flip(cities[17], cities[19]);
		System.out.println(solution);
		
		//flip con next(a) e b agli estremi di un segmento con R = 1
		solution.flip(cities[7], cities[11]);
		System.out.println(solution);
		
		//flip con next(a) e b agli estremi di un segmento con R = 0
		solution.flip(cities[7], cities[8]);
		System.out.println(solution);
		
		//flip con next(a) e b agli estremi di una serie di segmenti, entrambi con R = 0
		solution.flip(cities[3], cities[15]);
		System.out.println(solution);
		
		//flip con next(a) e b agli estremi di una serie di segmenti, entrambi con R = 1
		solution.flip(cities[6], cities[12]);
		System.out.println(solution);
		
		//flip con next(a) estremo di un segmento con R = 1 e b di un segmento con R = 0
		solution.flip(cities[15], cities[8]);
		System.out.println(solution);
		
		//flip con next(a) estremo di un segmento con R = 0 e b di un segmento con R = 1
		solution.flip(cities[6], cities[19]);
		System.out.println(solution);
		
		//flip con split sul segmento di next(a) (R=0), merge a sinistra (R = 0)
		solution.flip(cities[11], cities[6]);
		System.out.println(solution);
		
		//flip con split sul segmento di next(a) (R=0), merge a sinistra (R = 1)
		solution.flip(cities[19], cities[15]);
		System.out.println(solution);
		
		//flip con split sul segmento di next(a) (R=1), merge a sinistra (R = 0)
		solution.flip(cities[7], cities[12]);
		System.out.println(solution);
		
		solution.flip(cities[0], cities[8]);
		System.out.println(solution);
		
		//flip con split sul segmento di next(a) (R=1), merge a sinistra (R = 1)
		solution.flip(cities[15], cities[1]);
		System.out.println(solution);
		
		//flip con split sul segmento di b (R=0), merge a destra (R = 0)
		solution.flip(cities[7], cities[18]);
		System.out.println(solution);
		
		//flip con split sul segmento di b (R=0), merge a destra (R = 1)
		solution.flip(cities[0], cities[3]);
		System.out.println(solution);
		
		solution.flip(cities[17], cities[4]);
		System.out.println(solution);
		
		//flip con split sul segmento di b (R=1), merge a destra (R = 0)
		solution.flip(cities[8], cities[6]);
		System.out.println(solution);
		
		solution.flip(cities[6], cities[8]);
		System.out.println(solution);
		
		//flip con split sul segmento di b (R=1), merge a destra (R = 1)
		solution.flip(cities[0], cities[16]);
		System.out.println(solution);
		
		//Flip con doppio split, next(a) verso sinistra, b verso sinistra
		solution.flip(cities[10], cities[11]);
		System.out.println(solution);
		
		//Flip con doppio split, next(a) verso sinistra, b verso destra
		solution.flip(cities[5], cities[3]);
		System.out.println(solution);
		
		//Flip con doppio split, next(a) verso destra, b verso sinistra
		solution.flip(cities[14], cities[1]);
		System.out.println(solution);
		
		//Flip con doppio split, next(a) verso destra, b verso destra
		solution.flip(cities[5], cities[8]);
		System.out.println(solution);
		
		//Flip con doppio split, next(a) e b diventano gli estremi dello stesso segmento
		solution.flip(cities[2], cities[19]);
		System.out.println(solution);
		
		//Flip critico, b potrebbe non essere più un estremo del suo segmento
		solution.flip(cities[9], cities[2]);
		System.out.println(solution);
		
		//Flip critico, next(a) potrebbe non essere più un estremo del suo segmento
		solution.flip(cities[12], cities[14]);
		System.out.println(solution);
		
		
		//soluzione con 76 città e toll = 2
		cities = LK.Test_LK.createCities76();
		
		manager = new CityManager(cities);
		
		solution = new TwoLevelTree(manager, manager.getCities(), 16);
		
		System.out.println(solution);
		
		//flip senza doppio split (differenza di client da scambiare <  di toll)
		//porzione di next(a) più numerosa di quella di b
		//segmento di next(a) con R = 0, segmento di b con R = 0
		solution.flip(cities[27], cities[65]);
		System.out.println(solution);
		
		//flip senza doppio split (differenza di client da scambiare <  di toll)
		//porzione di next(a) meno numerosa di quella di b
		//segmento di next(a) con R = 0, segmento di b con R = 0
		solution.flip(cities[14], cities[17]);
		System.out.println(solution);
		
		//flip senza doppio split (differenza di client da scambiare <  di toll)
		//porzione di next(a) più numerosa di quella di b
		//segmento di next(a) con R = 1, segmento di b con R = 0
		solution.flip(cities[51], cities[31]);
		System.out.println(solution);
		
		//flip senza doppio split (differenza di client da scambiare <  di toll)
		//porzione di next(a) meno numerosa di quella di b
		//segmento di next(a) con R = 1, segmento di b con R = 0
		solution.flip(cities[52], cities[50]);
		System.out.println(solution);
		
		//flip senza doppio split (differenza di client da scambiare <  di toll)
		//porzione di next(a) più numerosa di quella di b
		//segmento di next(a) con R = 0, segmento di b con R = 1
		solution.flip(cities[12], cities[62]);
		System.out.println(solution);
		
		//flip senza doppio split (differenza di client da scambiare <  di toll)
		//porzione di next(a) meno numerosa di quella di b
		//segmento di next(a) con R = 0, segmento di b con R = 1
		solution.flip(cities[62], cities[17]);
		System.out.println(solution);
		
		//flip senza doppio split (differenza di client da scambiare <  di toll)
		//porzione di next(a) meno numerosa di quella di b
		//segmento di next(a) con R = 1, segmento di b con R = 1
		solution.flip(cities[50], cities[45]);
		System.out.println(solution);
		
		//flip senza doppio split (differenza di client da scambiare <  di toll)
		//porzione di next(a) più numerosa di quella di b
		//segmento di next(a) con R = 1, segmento di b con R = 1
		solution.flip(cities[50], cities[49]);
		System.out.println(solution);
	}

}
