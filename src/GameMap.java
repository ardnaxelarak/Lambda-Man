import java.io.IOException;
import java.io.File;
import java.io.FileReader;
import java.io.BufferedReader;
import java.util.LinkedList;
import java.util.List;

public class GameMap
{
	private MapContent[][] map;
	private int level;
	private Location fruitloc, playerloc;
	private Location[] ghostlocs;
	private List<MapListener> listeners;

	public GameMap(String[] mapString)
	{
		listeners = new LinkedList<MapListener>();
		int rows = mapString.length, cols = mapString[0].length();
		boolean rowWarning = false;
		fruitloc = null;
		playerloc = null;
		char[][] mapPieces = new char[rows][];
		LinkedList<Location> ghostList = new LinkedList<Location>();
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
						if (playerloc != null)
						{
							System.err.println("WARNING: Multiple starting locations found for Lambda-Man. Transforming extra starting locations into empty.");
							map[i][j] = MapContent.EMPTY;
						}
						else
						{
							map[i][j] = MapContent.LAMBDA_MAN;
							playerloc = new Location(i, j);
						}
						break;
					case '=':
						map[i][j] = MapContent.GHOST;
						int index = ghostList.size();
						ghostList.add(new Location(i, j));
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
		if (fruitloc == null)
			System.err.println("WARNING: No fruit location defined.");

		level = (rows * cols - 1) / 100;
		ghostlocs = new Location[ghostList.size()];
		ghostlocs = ghostList.toArray(ghostlocs);
	}

	public static GameMap fromFile(String filename) throws IOException
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
			return new GameMap(lines);
		}
		finally
		{
			if (br != null)
				br.close();
		}
	}

	public void addListener(MapListener listener)
	{
		listeners.add(listener);
	}

	public MapContent getTile(int y, int x)
	{
		return map[y][x];
	}

	public void setTile(int y, int x, MapContent value)
	{
		map[y][x] = value;
		for (MapListener listener : listeners)
			listener.mapChanged(y, x, value);
	}

	public int getRows()
	{
		return map.length;
	}

	public int getCols()
	{
		return map[0].length;
	}

	public Location getPlayerLoc()
	{
		return playerloc;
	}

	public Location getFruitLoc()
	{
		return fruitloc;
	}

	public Location getGhostLoc(int index)
	{
		return ghostlocs[index];
	}

	public int getNumGhosts()
	{
		return ghostlocs.length;
	}
}

interface MapListener
{
	public void mapChanged(int y, int x, MapContent newItem);
}
