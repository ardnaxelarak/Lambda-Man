public class Location
{
	private int x, y;
	public Location(int y, int x)
	{
		this.y = y;
		this.x = x;
	}

	public Location(Location l)
	{
		this(l.y, l.x);
	}

	public Location displace(int y, int x)
	{
		return new Location(this.y + y, this.x + x);
	}

	public Location displace(Location l)
	{
		return displace(l.y, l.x);
	}

	public int getX()
	{
		return x;
	}

	public int getY()
	{
		return y;
	}
}
