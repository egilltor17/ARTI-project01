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
		int xx = Math.abs(this.x - p.x);
		int yy = Math.abs(this.x - p.y);
		if((xx == 0) || (yy == 0)) return xx + yy;
		return xx + yy + 1;
	}
}