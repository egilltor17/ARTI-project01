public class Point
{
	public int x;
	public int y;	

	public Point(int x, int y)
	{
		this.x = x;
		this.y = y;
	}
	
	public int manhatanDist(Point p)
	{
		return Math.abs(this.x - p.x) + Math.abs(this.x - p.y);
	}
}