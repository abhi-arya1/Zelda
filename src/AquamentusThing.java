/**
 * While this is the "Boss" of the Game, there really is not many 
 * functions of the Boss besides its lives within the AquamentusThing class. 
 */
public class AquamentusThing extends PokeThing
{
	/**
	 * The lives that Aquamentus has.
	 * To change game difficulty, increase this number.
	 */
	public int AquaLives = 10;
	
	private int x = 0; // Counter for step()
	
	// Increase to Increase Game Difficulty
	/** Number of Steps before Aquamentus spawns more Fireballs */
	private static final int SPAWNRATE = 5; 
	
	/** Location list of the 5 Spots in front of Aquamentus for Fireballs to Spawn at. */
	private Location[] locs = 
		{
			new Location(3, 12), new Location(4, 12), 
			new Location(5, 12), new Location(6, 13), new Location(7, 13) 
		};
	
	
	public AquamentusThing(String part)
	{
		super(part);
		step(); 
	}
	
	/**
	 * Aquamentus does not move, however this step method allows for it to 
	 * spawn fireballs. 
	 */
	@Override
	public void step()
	{
		ZeldaBoard b = (ZeldaBoard) getBoard();
		
		if(x == SPAWNRATE)
		{
			for(int i = 0; i < locs.length; i++)
			{
				if(b.thingAt(locs[i]) instanceof LinkThing)
				{
					LinkThing lin = (LinkThing) b.thingAt(locs[i]);
					
					//Check for Shield on Fire Spawn
					if(lin.shieldUses > 0)
					{
						lin.shieldUses--;
						b.getGui().appendTextWindow("Shield took a hit!");
					}
					else {
						b.linkReset();
						b.add(new FireballThing(), locs[i]);
					}
				}
				else
					b.add(new FireballThing(), locs[i]);
			}
		
			x = 0;
		}
		
		//Counter for Fire Spawn
		x++;
	}
}
