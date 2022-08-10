/**
 * Blue blocks for Level 6. These do nothing, and act like a 
 * blank space, but needed to be there so that level 6 had a 
 * different background. 
 */
public class BlueArea extends PokeThing
{
	public BlueArea()
	{
		super("blue.png");
	}
	
	@Override
	public void step()
	{
		// do nothing
	}
}
