package us.youfailed.androdino;

class Point{
	float x, y;
	@Override
	public String toString() {
		return String.format("(%4s, %4s)", x, y);
	}
	
	public void copy(Point that) {
		this.x = that.x;
		this.y = that.y;
	}
}
