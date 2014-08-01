import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class Display extends JFrame
{
	private static final String[] FILENAMES = new String[] {"empty.png", "ghost0.png", "ghost1.png", "ghost2.png", "ghost3.png", "lambda.png", "pill.png", "powerpill.png", "wall.png"};
	private DisplayPanel graphicsPanel;
	private Game game;
	private Color background = Color.BLACK;
	private static final int GRID_SIZE = 20;
	private Image[] mapImages;
	private Image playerImage;
	private Image[] ghostImages;

	private void loadImages()
	{
		int numFiles = FILENAMES.length;
		Image[] imageList = new Image[numFiles];
		String filename;
		for (int i = 0; i < numFiles; i++)
		{
			filename = FILENAMES[i];
			try
			{
				imageList[i] = ImageIO.read(new File(String.format("images/%s", filename)));
			}
			catch (IOException e)
			{
				System.err.printf("WARNING: File \"%s\" could not be loaded.\n", filename);
				imageList[i] = new BufferedImage(GRID_SIZE, GRID_SIZE, BufferedImage.TYPE_INT_ARGB);
			}
		}
		playerImage = imageList[5];
		ghostImages = new Image[] {imageList[1], imageList[2], imageList[3], imageList[4]};
		mapImages = new Image[] {imageList[8], imageList[0], imageList[6], imageList[7], imageList[0], imageList[0], imageList[0]};
	}

	public Display(Game game)
	{
		super("Lambda-Man");
		
		loadImages();

		this.game = game;
		graphicsPanel = new DisplayPanel();
		GameMap map = game.getMap();
		graphicsPanel.setPreferredSize(new Dimension(map.getCols() * GRID_SIZE, map.getRows() * GRID_SIZE));

		MultiListener ml = new MultiListener();
		map.addListener(ml);
		game.addListener(ml);

		getContentPane().add(graphicsPanel, BorderLayout.CENTER);
		pack();
		setVisible(true);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}

	private void paintTile(Graphics g, int y, int x, boolean clear, boolean check)
	{
		GameMap map = game.getMap();
		if (clear)
		{
			g.setColor(background);
			g.fillRect(x * GRID_SIZE, y * GRID_SIZE, GRID_SIZE, GRID_SIZE);
		}
		Image img = mapImages[map.getTile(y, x).getValue()];
		g.drawImage(img, x * GRID_SIZE, y * GRID_SIZE, GRID_SIZE, GRID_SIZE, null);
	}

	private void paintPlayer(Graphics g, boolean clear)
	{
		Location loc = game.getPlayerLoc();
		int x = loc.getX(), y = loc.getY();
		if (clear)
		{
			g.setColor(background);
			g.fillRect(x * GRID_SIZE, y * GRID_SIZE, GRID_SIZE, GRID_SIZE);
		}
		g.drawImage(playerImage, x * GRID_SIZE, y * GRID_SIZE, GRID_SIZE, GRID_SIZE, null);
	}

	private void paintGhost(Graphics g, int index, boolean clear)
	{
		Location loc = game.getGhostLoc(index);
		int x = loc.getX(), y = loc.getY();
		if (clear)
		{
			g.setColor(background);
			g.fillRect(x * GRID_SIZE, y * GRID_SIZE, GRID_SIZE, GRID_SIZE);
		}
		g.drawImage(ghostImages[index % ghostImages.length], x * GRID_SIZE, y * GRID_SIZE, GRID_SIZE, GRID_SIZE, null);
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
			Display d = new Display(g);
		}
	}

	class DisplayPanel extends JPanel
	{
		@Override
		public void paintComponent(Graphics g)
		{
			super.paintComponent(g);
			g.setColor(background);
			g.fillRect(0, 0, getWidth(), getHeight());
			int rows = game.getMap().getRows(), cols = game.getMap().getCols();
			for (int i = 0; i < rows; i++)
				for (int j = 0; j < cols; j++)
					paintTile(g, i, j, false, false);

			for (int i = 0; i < game.getNumGhosts(); i++)
				paintGhost(g, i, false);

			paintPlayer(g, false);
		}
	}

	class MultiListener implements MapListener, GameListener
	{
		@Override
		public void mapChanged(int y, int x, MapContent value)
		{
			Graphics g = graphicsPanel.getGraphics();
			paintTile(g, y, x, true, true);
		}

		@Override
		public void playerMoved(int oldy, int oldx, int newy, int newx)
		{
			Graphics g = graphicsPanel.getGraphics();
			paintPlayer(g, false);
			paintTile(g, oldy, oldx, true, true);
		}

		@Override
		public void ghostMoved(int index, int oldy, int oldx, int newy, int newx)
		{
			Graphics g = graphicsPanel.getGraphics();
			paintGhost(g, index, false);
			paintTile(g, oldy, oldx, true, true);
		}

		@Override
		public void fruitChanged(boolean visible)
		{
			Graphics g = graphicsPanel.getGraphics();
		}
	}
}
