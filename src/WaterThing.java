/**
 * The Water, which acts like a special BlockedArea for Level 3. 
 */
public class WaterThing extends BlockedArea
{
	public WaterThing()
	{
		super("zeldaWater.png");
	}
	
	@Override
	public void step()
	{
		// Do Nothing, Water is just a Special Version of 
		// BlockedArea, but its interactions are different, and defined
		// in other classes. 
	}
}
