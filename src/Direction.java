import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public enum Direction
{
	UP(0, -1, 0),
	RIGHT(1, 0, 1),
	DOWN(2, 1, 0),
	LEFT(3, 0, -1);

	private final int value;
	private final Location displace;
	private Direction(int value, int yd, int xd)
	{
		this.value = value;
		displace = new Location(yd, xd);
	}

	public int getValue()
	{
		return value;
	}

	public Location getDisplacement()
	{
		return displace;
	}

	public static Direction fromValue(int value)
	{
		switch (value)
		{
			case 0:
				return UP;
			case 1:
				return RIGHT;
			case 2:
				return DOWN;
			case 3:
				return LEFT;
			default:
				return null;
		}
	}

	public Direction getOpposite()
	{
		return Direction.fromValue((value + 2) % 4);
	}

	public static List<Direction> getOrder(Direction cur, Direction desired)
	{
		LinkedList<Direction> list = new LinkedList<Direction>();
		list.addLast(UP);
		list.addLast(RIGHT);
		list.addLast(DOWN);
		list.addLast(LEFT);
		if (list.remove(cur))
			list.addFirst(cur);
		if (desired != null)
			if (list.remove(desired))
				list.addFirst(desired);
		if (list.remove(cur.getOpposite()))
			list.addLast(cur.getOpposite());
		return list;
	}
}
