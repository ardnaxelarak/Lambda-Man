public class LambdaMan
{
	private int vitality;
	private int xloc, yloc;
	private Direction dir;
	private int lives;
	private int score;
	private int nextMove;
	private static final int MOVE_SPEED = 127;
	private static final int EAT_SPEED = 137;

	public LambdaMan(int startingX, int startingY)
	{
		vitality = 0;
		xloc = startingX;
		yloc = startingY;
		dir = Direction.DOWN;
		lives = 3;
		score = 0;
		nextMove = 1 + MOVE_SPEED;
	}
}
