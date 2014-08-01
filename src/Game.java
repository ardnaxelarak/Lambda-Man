import java.io.IOException;
import java.util.List;
import java.util.LinkedList;

public class Game
{
	private GameMap map;
	private int level, ticknum;
	private LambdaMan player;
	private Ghost[] ghosts;
	private List<GameListener> listeners;

	public Game(GameMap map)
	{
		this.map = map;
		level = (map.getRows() * map.getCols() - 1) / 100;
		ticknum = 1;
		player = new LambdaMan(map.getPlayerLoc());
		int num = map.getNumGhosts();
		ghosts = new Ghost[num];
		for (int i = 0; i < num; i++)
			ghosts[i] = new Ghost(i, map.getGhostLoc(i), 130 + (i % 4) * 2, 195 + (1 % 4) * 3);
		listeners = new LinkedList<GameListener>();
	}

	public Game(String[] mapStrings)
	{
		this(new GameMap(mapStrings));
	}

	public Game(String filename) throws IOException
	{
		this(GameMap.fromFile(filename));
	}

	public void printMap()
	{
		int rows = map.getRows();
		int cols = map.getCols();
		char[][] mapout = new char[rows][cols];
		for (int i = 0; i < rows; i++)
		{
			for (int j = 0; j < cols; j++)
			{
				mapout[i][j] = map.getTile(i, j).getMapChar();
			}
		}
		for (int i = 0; i < ghosts.length; i++)
		{
			int x = ghosts[i].loc.getX();
			int y = ghosts[i].loc.getY();
			if (0 <= x && x < cols && 0 <= y && y < rows)
				mapout[y][x] = '=';
		}
		int x = player.loc.getX();
		int y = player.loc.getY();
		if (0 <= x && x < cols && 0 <= y && y < rows)
			mapout[y][x] = '\\';

		for (int i = 0; i < rows; i++)
			System.out.println(mapout[i]);
		System.out.printf("Score: %d Tick: %d Lives: %d\n", player.getScore(), ticknum, player.getLives());
	}

	public static void main(String[] args) throws IOException
	{
		if (args.length == 0)
		{
			System.out.println("Usage: Game <filename>");
		}
		else
		{
			Game g = new Game(args[0]);
			g.printMap();
		}
	}

	public GameMap getMap()
	{
		return map;
	}

	public Location getPlayerLoc()
	{
		return player.getLoc();
	}

	public Location getGhostLoc(int index)
	{
		return ghosts[index].getLoc();
	}

	public int getNumGhosts()
	{
		return ghosts.length;
	}

	public void addListener(GameListener listener)
	{
		listeners.add(listener);
	}

	public class LambdaMan
	{
		private int vitality;
		private Location loc;
		private Direction dir;
		private int lives;
		private int score;
		private int nextMove;
		private static final int MOVE_SPEED = 127;
		private static final int EAT_SPEED = 137;

		public LambdaMan(Location start)
		{
			vitality = 0;
			loc = new Location(start);
			dir = Direction.DOWN;
			lives = 3;
			score = 0;
			nextMove = 1 + MOVE_SPEED;
		}

		public LambdaMan(int starty, int startx)
		{
			vitality = 0;
			loc = new Location(starty, startx);
			dir = Direction.DOWN;
			lives = 3;
			score = 0;
			nextMove = 1 + MOVE_SPEED;
		}

		public int getScore()
		{
			return score;
		}

		public int getLives()
		{
			return lives;
		}

		public Location getLoc()
		{
			return loc;
		}
	}

	public class Ghost
	{
		private GhostVitality vitality;
		private Location loc;
		private Direction dir;
		private int nextMove;
		private int standardSpeed, slowSpeed;
		private int index;

		public Ghost(int index, Location start,
					 int standardSpeed, int slowSpeed)
		{
			vitality = GhostVitality.STANDARD;
			this.index = index;
			loc = new Location(start);
			this.standardSpeed = standardSpeed;
			this.slowSpeed = slowSpeed;
			dir = Direction.DOWN;
			nextMove = 1 + standardSpeed;
		}
		public Ghost(int index, int starty, int startx,
					 int standardSpeed, int slowSpeed)
		{
			vitality = GhostVitality.STANDARD;
			this.index = index;
			loc = new Location(starty, startx);
			this.standardSpeed = standardSpeed;
			this.slowSpeed = slowSpeed;
			dir = Direction.DOWN;
			nextMove = 1 + standardSpeed;
		}

		public Location getLoc()
		{
			return loc;
		}
	}
}

interface GameListener
{
	public void playerMoved(int oldy, int oldx, int newy, int newx);
	public void ghostMoved(int index, int oldy, int oldx, int newy, int newx);
	public void fruitChanged(boolean visible);
}
