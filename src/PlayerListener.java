public interface PlayerListener
{
	public void playerMoved(LambdaMan player, int oldy, int oldx);
	public void scoreChanged(LambdaMan player);
	public void livesChanged(LambdaMan player);
}
