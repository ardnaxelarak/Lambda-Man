import java.io.IOException;
import java.io.File;
import java.io.FileReader;
import java.io.BufferedReader;
import java.util.LinkedList;

public class Game
{
	private MapContent[][] map;
	private int level, ticknum;
	private Location fruitloc;
	private LambdaMan player;
	private Ghost[] ghosts;

	private void loadMap(String[] mapString)
	{
		int rows = mapString.length, cols = mapString[0].length();
		boolean rowWarning = false;
		fruitloc = null;
		char[][] mapPieces = new char[rows][];
		player = null;
		LinkedList<Ghost> ghostList = new LinkedList<Ghost>();
		for (int i = 0; i < rows; i++)
		{
			mapPieces[i] = mapString[i].toCharArray();
			if (mapString[i].length() != cols && !rowWarning)
			{
				System.err.println("WARNING: Not all rows in the map are the same length. Adding extra walls to compensate.");
				rowWarning = true;
			}
			if (mapString[i].length() > cols)
				cols = mapString[i].length();
		}

		map = new MapContent[rows][cols];
		int j;
		for (int i = 0; i < rows; i++)
		{
		 	for (j = 0; j < mapPieces[i].length; j++)
			{
				switch (mapPieces[i][j])
				{
					case '#':
						map[i][j] = MapContent.WALL;
						break;
					case ' ':
						map[i][j] = MapContent.EMPTY;
						break;
					case '.':
						map[i][j] = MapContent.PILL;
						break;
					case 'o':
						map[i][j] = MapContent.POWER_PILL;
						break;
					case '%':
						if (fruitloc != null)
						{
							System.err.println("WARNING: Multiple fruit locations found. Transforming extra fruit starting locations into empty.");
							map[i][j] = MapContent.EMPTY;
						}
						else
						{
							map[i][j] = MapContent.FRUIT;
							fruitloc = new Location(i, j);
						}
						break;
					case '\\':
						if (player != null)
						{
							System.err.println("WARNING: Multiple starting locations found for Lambda-Man. Transforming extra starting locations into empty.");
							map[i][j] = MapContent.EMPTY;
						}
						else
						{
							map[i][j] = MapContent.LAMBDA_MAN;
							player = new LambdaMan(i, j);
						}
						break;
					case '=':
						map[i][j] = MapContent.GHOST;
						int index = ghostList.size();
						ghostList.add(new Ghost(index, i, j, 130 + (index % 4) * 2, 195 + (index % 4) * 3));
						break;
					default:
						System.err.printf("WARNING: Unknown map tile \'%s\'. Replacing with wall.\n", mapPieces[i][j]);
						map[i][j] = MapContent.WALL;
						break;
				}
			}
			while (j < cols)
			{
				map[i][j++] = MapContent.WALL;
			}
		}
		level = (rows * cols - 1) / 100;
		ticknum = 1;
		ghosts = new Ghost[ghostList.size()];
		ghosts = ghostList.toArray(ghosts);
	}

	public Game(String[] mapString)
	{
		loadMap(mapString);
	}

	public Game(String filename) throws IOException
	{
		BufferedReader br = null;
		try
		{
			br = new BufferedReader(new FileReader(new File(filename)));
			LinkedList<String> lineList = new LinkedList<String>();
			String line;
			while ((line = br.readLine()) != null)
			{
				lineList.add(line);
			}
			String[] lines = new String[lineList.size()];
			lines = lineList.toArray(lines);
			loadMap(lines);
		}
		finally
		{
			if (br != null)
				br.close();
		}
	}

	public void printMap()
	{
		int rows = map.length;
		int cols = map[0].length;
		char[][] mapout = new char[rows][cols];
		for (int i = 0; i < rows; i++)
		{
			for (int j = 0; j < cols; j++)
			{
				mapout[i][j] = map[i][j].getMapChar();
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
	}
}
