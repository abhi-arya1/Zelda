/**
 * Bridges for Level 3. Can be moved until they interact with 
 * water and turn into <code>BridgeThingStatic</code>
 *
 */
public class BridgeThing extends PokeThing
{
	/**
	 * Uses <code>PokeThing</code> Constructor to create a special 
	 * Bridge Object. 
	 */
	public BridgeThing()
	{
		super("ZelBridge.png");
	}
	
	/**
	 * Bridge objects dont have any functions that rely on timers of the GUI. 
	 */
	public void step()
	{
		
	}
	
	/**
	 * Finds Link
	 * @return a String corresponding to where Link is, in relation to this object, or null if 
	 * 		   the method is incorrectly called in <code>LinkThing.levelThreeMovements(args)</code>
	 * @Precondition Link is Above, Below, or Beside this Object
	 * @Postcondition Must not return null for the program to not explode
	 */
	// Method inspired by GameOfLife
	public String findLink()
	{
		Location thisOne = this.getLocation();
		Board b = getBoard(); 
		int row = thisOne.getRow();
		int col = thisOne.getColumn();

		Location[] locs = {new Location(row - 1, col), new Location(row + 1, col),
				new Location(row, col - 1), new Location(row, col+1)};
		
		for(int i = 0; i < 4; i++)
		{
			if(b.thingAt(locs[i]) instanceof LinkThing)
			{
				if(i == 0)
					return "Abv";
				if(i == 1)
					return "Below";
				if(i == 2)
					return "Left";
				if(i == 3)
					return "Right";
			}
		}
		
		return null; 
	}

}
