public class Entity
{
	protected Location loc;
	protected Direction dir;
	protected int nextMove;
	protected AIController ai;
	protected int normalSpeed, slowSpeed;
	protected Game game;
	public Entity(Game game, int starty, int startx, int normalSpeed, int slowSpeed)
	{
		this.game = game;
		loc = new Location(starty, startx);
		dir = Direction.DOWN;
		this.normalSpeed = normalSpeed;
		this.slowSpeed = slowSpeed;
		ai = null;
		nextMove = normalSpeed;
	}

	public Location getLoc()
	{
		return loc;
	}

	public int getNextMove()
	{
		return nextMove;
	}

	public void setAI(AIController ai)
	{
		this.ai = ai;
	}
}
