import java.util.LinkedList;
import java.util.List;

public class LambdaMan extends Entity
{
	private int vitality, lives, score;
	private static final int MOVE_SPEED = 127;
	private static final int EAT_SPEED = 127;
	private List<PlayerListener> listeners;

	public LambdaMan(Game game, int starty, int startx)
	{
		super(game, starty, startx, MOVE_SPEED, EAT_SPEED);
		vitality = 0;
		lives = 3;
		score = 0;
		listeners = new LinkedList<PlayerListener>();
	}

	public LambdaMan(Game game, Location start)
	{
		this(game, start.getY(), start.getX());
	}

	public int getLives()
	{
		return lives;
	}

	public void setLives(int value)
	{
		lives = value;
		for (PlayerListener l : listeners)
			l.livesChanged(this);
	}

	public int getScore()
	{
		return score;
	}

	public void addListener(PlayerListener listener)
	{
		listeners.add(listener);
	}

	public void incrementScore(int value)
	{
		score += value;
		for (PlayerListener l : listeners)
			l.scoreChanged(this);
	}

	private Direction determineMove()
	{
		Direction attempt = null;
		if (ai != null)
			attempt = ai.getDirection(game);
		System.err.println(attempt);
		return attempt;
	}

	private void processMove(Direction d)
	{
		int oldx = loc.getX(), oldy = loc.getY();
		if (d == null)
			d = dir;
		Location newloc = loc.displace(d.getDisplacement());
		MapContent tile = game.getMap().getTile(newloc);
		switch (tile)
		{
			case WALL:
				nextMove += normalSpeed;
				return;
			case EMPTY:
			case LAMBDA_MAN:
			case GHOST:
				nextMove += normalSpeed;
				break;
			case PILL:
				nextMove += slowSpeed;
				break;
		}
		loc = newloc;
		dir = d;
		for (PlayerListener l : listeners)
			l.playerMoved(this, oldy, oldx);
	}

	public void takeMove()
	{
		processMove(determineMove());
	}
}
