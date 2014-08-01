public class RandomAI implements AIController
{
	@Override
	public Direction getDirection(Game state)
	{
		return Direction.fromValue((int)(Math.random() * 4));
	}
}
