/**
 * This class differentiates between <code>PokeThing</code>s that Link can 
 * interact with, and <code>PokeThing</code>s that Link cannot interact with. 
 * 
 * It is the same as <code>PokeThing</code>, just with a different name
 * for "<code> Location instanceof BlockedArea </code>" to restrict Link's 
 * movement
 * 
 */
public class BlockedArea extends PokeThing
{
	public BlockedArea(String name)
	{
		super(name);
	}
	
	public void step()
	{
		// do nothing, BlockedAreas stay in one place. 
	}

}
