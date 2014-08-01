import java.io.IOException;
import java.util.List;
import java.util.LinkedList;

public class Game
{
	private GameMap map;
	private int level, ticknum;
	private LambdaMan player;
	private Ghost[] ghosts;

	public Game(GameMap map)
	{
		this.map = map;
		level = (map.getRows() * map.getCols() - 1) / 100;
		ticknum = 1;
		player = new LambdaMan(this, map.getPlayerLoc());
		int num = map.getNumGhosts();
		ghosts = new Ghost[num];
		for (int i = 0; i < num; i++)
			ghosts[i] = new Ghost(this, i, map.getGhostLoc(i), 130 + (i % 4) * 2, 195 + (1 % 4) * 3);
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

	public void runGame()
	{
		int state;
		while ((state = step()) == 0)
		{
			try
			{
				Thread.sleep(5);
			} catch (InterruptedException e) {
			}
			if (ticknum >= 127 * map.getRows() * map.getCols() * 16)
				player.setLives(0);
		}
	}

	private int step()
	{
		if (player.getNextMove() == ticknum)
			player.takeMove();
		for (Ghost g : ghosts)
			if (g.getNextMove() == ticknum)
				g.takeMove();

		// fright mode deactivate, fruit appear/disappear

		// eat stuff
		switch (map.getTile(player.getLoc()))
		{
			case PILL:
				player.incrementScore(10);
				map.setTile(player.getLoc(), MapContent.EMPTY);
				break;
			case POWER_PILL:
				player.incrementScore(50);
				map.setTile(player.getLoc(), MapContent.EMPTY);
				// add vitality, activate fright, et cetera
				break;
		}

		// kill ghost/player

		// check for cleared level

		// check for no lives remaining
		if (player.getLives() <= 0)
			return 1; // player died

		ticknum++;

		return 0;
	}

	public void setPlayerAI(AIController ai)
	{
		player.setAI(ai);
	}

	public void setGhostAIs(AIController... ais)
	{
		int len = ais.length;
		for (int i = 0; i < ghosts.length; i++)
			ghosts[i].setAI(ais[i % len]);
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

	public void addPlayerListener(PlayerListener listener)
	{
		player.addListener(listener);
	}

	public void addGhostListener(GhostListener listener)
	{
		for (Ghost g : ghosts)
			g.addListener(listener);
	}
}
