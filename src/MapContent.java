public enum MapContent
{
	WALL(0, '#'),
	EMPTY(1, ' '),
	PILL(2, '.'),
	POWER_PILL(3, 'o'),
	FRUIT(4, '%'),
	LAMBDA_MAN(5, '\\', ' '),
	GHOST(6, '=', ' ');

	private final int value;
	private final char startChar, mapChar;
	private MapContent(int value, char startChar, char mapChar)
	{
		this.value = value;
		this.startChar = startChar;
		this.mapChar = mapChar;
	}

	private MapContent(int value, char mapChar)
	{
		this(value, mapChar, mapChar);
	}

	public int getValue()
	{
		return value;
	}

	public char getStartChar()
	{
		return startChar;
	}

	public char getMapChar()
	{
		return mapChar;
	}
}
