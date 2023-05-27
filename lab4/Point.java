public class Point {

	private int x;
	private int y;
	
	public Point(int x, int y) {
		this.x = x;
        this.y = y;
	}
	
	public int getX() {
		return this.x;
	}
	
	public int getY() {
		return this.y;
	}

    // vraća novu točku translatiranu za argument
	public Point translate(Point dp) {
        return new Point(this.x + dp.getX(), this.y + dp.getY());
	}
	
	public Point difference(Point p) {
		return new Point(this.x - p.getX(), this.y - p.getY());
	}
}
