/**
 * The Fireballs that <code>AquamentusThing</code> "shoots" every 5 clock cycles. 
 *
 */
public class FireballThing extends PokeThing
{
	private final Direction D = Direction.WEST;
	
	/**
	 * Creates a new FireballThing
	 * @Precondition Link is on Level 6, and Aquamentus is on the Board.
	 */
	public FireballThing()
	{
		super("FireBall.png");
	}
	
	/**
	 * Moves FireBall things in their fixed direction (<code>Direction.WEST</code>), 
	 * while checking for <code>LinkThing</code>
	 */
	@Override
	public void step()
	{
		ZeldaBoard b = (ZeldaBoard) getBoard();
		Location next = D.getNextLocation(getLocation());
		
		if(b.thingAt(next) instanceof BlockedArea || b.thingAt(next) instanceof FireThing)
			b.remove(this);
		else if(b.thingAt(next) instanceof LinkThing)
			linkInteraction(b, next);
		else
			move(next);
	}
	
	/**
	 * Checks every time Fireball's next Location and Link's Location match. 
	 * @param b the Board
	 * @param loc the Location that the Fire is going to. 
	 * @Precondition Link is near FireballThing
	 */
	public void linkInteraction(ZeldaBoard b, Location loc)
	{
		LinkThing lin = (LinkThing) b.thingAt(loc);
		
		if(lin.shieldUses <= 0) 
		{
			b.linkReset();
			b.remove(this);
			b.getGui().appendTextWindow("Link said: \"OW!\"");
		}
		else
		{
			b.remove(this);
			lin.shieldUses--;
			b.getGui().appendTextWindow("Shield took a hit!");
		}
	}
}
