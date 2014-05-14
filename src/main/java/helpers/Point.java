package helpers;

/**
 * Class representing a single point of a field.
 * 
 * @author jakub
 * 
 */

public class Point {
	public final int x;
	public final int y;
	
	public Point(int x, int y) {
		this.x = x;
		this.y = y;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof Point))
			return false;
		Point o = (Point) obj;
		return x == o.x && y == o.y;
	}
}
