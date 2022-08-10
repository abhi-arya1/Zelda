public class FireThing extends PokeThing 
{
	/**
	 * Rightward Facing Fire
	 */
	private static final String RIGHT_DEF = "ZeldaFireRight.png";
	
	/**
	 * Leftward Facing Fire 
	 */
	private static final String LEFT_DEF = "ZeldaFireLeft.png";
	
	/*
	 * Same as above, just with Blue Backgrounds
	 */
	private static final String RIGHT_BLUE = "ZeldaFireRightBlue.png";
	private static final String LEFT_BLUE = "ZeldaFireLeftBlue.png";
	
	/** The Level that the FireThing is On */
	private int color; 
	/*
	 * Color = 0 -> Plain Background
	 * Color = 1 -> Blue Background
	 */
	
	/**
	 * Creates a Fire object facing Right. 
	 */
	public FireThing(int color)
	{
		super(RIGHT_DEF);
		this.color = color;
		
		if(color == 1)
			setImageFileName("ZeldaFireRightBlue.png");
	}
	
	/**
	 * Every clock cycle, the direction of the fire changes, animating 
	 * its movement. 
	 */
	@Override
	public void step()
	{
		if(color == 0) 
		{
			if(getImageFileName().equals(RIGHT_DEF))
				setImageFileName(LEFT_DEF);
			else
				setImageFileName(RIGHT_DEF);
		}
		else
		{
			if(getImageFileName().equals(RIGHT_BLUE))
				setImageFileName(LEFT_BLUE);
			else
				setImageFileName(RIGHT_BLUE);
		}
	}
}
