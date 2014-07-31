public enum GhostVitality
{
	STANDARD(0),
	FRIGHT_MODE(1),
	INVISIBLE(2);

	private int value;
	private GhostVitality(int value)
	{
		this.value = value;
	}

	public int getValue()
	{
		return value;
	}

	public static GhostVitality fromValue(int value)
	{
		switch (value)
		{
			case 0:
				return STANDARD;
			case 1:
				return FRIGHT_MODE;
			case 2:
				return INVISIBLE;
			default:
				return null;
		}
	}
}
