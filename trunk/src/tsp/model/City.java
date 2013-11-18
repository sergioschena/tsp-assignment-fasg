package tsp.model;

public class City {
	int x;
	int y;
	int city;
	
	public City(int n, int x, int y){
		this.x = x;
		this.y = y;
		this.city = n;
	}

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}

	public int getCity() {
		return city;
	}
	
	public static int distance(City c1, City c2){
		return 0;
	}
}
