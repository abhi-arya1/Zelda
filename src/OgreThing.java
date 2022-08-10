/**
 * Handles operations for all hostile mobs in the Zelda game, 
 * including their movement and scanning functions. 
 *
 */
public class OgreThing extends PokeThing
{
	/**
	 * The Direction of movement for the Ogre.
	 */
	private Direction d; 

	/**
	 * Creates the <code>OgreThing</code>. 
	 * @param x the Direction that the Ogre starts with. 
	 */
	public OgreThing(Direction d)
	{
		super("OgreFront.png");
		this.d = d;

		if(d.equals(Direction.SOUTH))
			setImageFileName("OgreFront.png");
		else if(d.equals(Direction.NORTH))
			setImageFileName("OgreBack.png");

	}

	/**
	 * Handles movement for each <code>OgreThing</code>, on each
	 * clock cycle of the GUI. 
	 */
	@Override
	public void step()
	{
		Location loc = getLocation();
		Location nextLoc = d.getNextLocation(loc);
		ZeldaBoard b = (ZeldaBoard) getBoard();
		Gui g = b.getGui();

		// Default Movement
		if(!(b.thingAt(nextLoc) instanceof BlockedArea) && 
				!(b.thingAt(nextLoc) instanceof LinkThing))
		{
			move(nextLoc);
		}
		//Movement if Link is in the way. 
		else if(!(b.thingAt(nextLoc) instanceof BlockedArea) && 
				(b.thingAt(nextLoc) instanceof LinkThing))
		{ 
			LinkThing link = (LinkThing) b.thingAt(nextLoc);
		 //	g.appendTextWindow("here"); //for testing
			move(nextLoc);
			
			if(link.swordEquipped)
			{
				b.remove(this);
				link.level2MobsRemoved++;
			}
			else 
			{
				g.appendTextWindow("Link says: 'OUCH!'"); 
				b.linkReset(); 
			}
		}
		else // Movement if the Ogre hits a wall. 
		{
			dirSwitch(d);
			move(d.getNextLocation(loc));
		}
	}

	public void face(Direction x)
	{
		switch(x)
		{
		case NORTH:
		{

		}
		case SOUTH:
		{

		}
		case EAST:
		{

		}
		case WEST:
		default:
		{

		}
		}
	}

	/**
	 * Changes the Direction of the Ogre
	 * @param x the Direction of the Ogre as it hits the wall. 
	 * @Precondition Ogre hit a wall.
	 */
	public void dirSwitch(Direction x)
	{
		if(x.equals(Direction.SOUTH)) 
		{
			d = Direction.NORTH;
			setImageFileName("OgreBack.png");
		}
		else if(x.equals(Direction.NORTH)) 
		{
			setImageFileName("OgreFront.png");
			d = Direction.SOUTH;
		}
	}
}

