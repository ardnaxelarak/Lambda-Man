import java.util.LinkedList;
import java.util.List;

public class Ghost extends Entity
{
	private GhostVitality vitality;
	private int index;
	private List<GhostListener> listeners;

	public Ghost(Game game, int index, int starty, int startx,
				 int standardSpeed, int slowSpeed)
	{
		super(game, starty, startx, standardSpeed, slowSpeed);
		vitality = GhostVitality.STANDARD;
		this.index = index;
		listeners = new LinkedList<GhostListener>();
	}

	public Ghost(Game game, int index, Location start,
				 int standardSpeed, int slowSpeed)
	{
		this(game, index, start.getY(), start.getX(), standardSpeed, slowSpeed);
	}

	public int getIndex()
	{
		return index;
	}

	public void addListener(GhostListener listener)
	{
		listeners.add(listener);
	}

	private Direction determineMove()
	{
		Direction attempt = null;
		if (ai != null)
			attempt = ai.getDirection(game);
		List<Direction> attempts = Direction.getOrder(dir, attempt);
		for (Direction dir : attempts)
		{
			if (game.getMap().getTile(loc.displace(dir.getDisplacement())) != MapContent.WALL)
				return dir;
		}
		return null;
	}

	private void processMove(Direction d)
	{
		int oldx = loc.getX(), oldy = loc.getY();
		if (vitality == GhostVitality.STANDARD)
			nextMove += normalSpeed;
		else
			nextMove += slowSpeed;
		if (d == null)
			return;
		loc.displaceBy(d.getDisplacement());
		dir = d;
		for (GhostListener l : listeners)
			l.ghostMoved(this, oldy, oldx);
	}

	public void takeMove()
	{
		processMove(determineMove());
	}
}
