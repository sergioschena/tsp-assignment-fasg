package tsp.model;

public class City {
	double x;
	double y;
	int city;
	public boolean visited;
	
	
	public City(int n, double x, double y){
		this.x = x;
		this.y = y;
		this.city = n;
	}

	public double getX() {
		return x;
	}

	public double getY() {
		return y;
	}

	public int getCity() {
		return city;
	}
	
	public static int distance(City c1, City c2){
		
		double sqx = c1.x - c2.x;
		double sqy = c1.y - c2.y;
		
		double sq = sqx*sqx + sqy*sqy;
		int res = (int)(Math.sqrt(sq));
		
		return res;
	}
	
	public static int distance_squared(City c1, City c2){
		double sqx = c1.x - c2.x;
		double sqy = c1.y - c2.y;
		
		double sq = sqx*sqx + sqy*sqy;
		
		return (int)(sq);
	}
	
	public boolean equals(Object o){
		City oth = (City) o;
		return oth.city == this.city;
	}

	public void visit(boolean b) {
		this.visited=b;
		return;
		
	}
	
}
