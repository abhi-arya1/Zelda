/**
 * <code>BridgeThing</code> objects change into <code>BridgeThingStatic</code> 
 * objects when they interact with water.
 *
 */
public class BridgeThingStatic extends PokeThing
{
	@SuppressWarnings("unused")
	private Location fin;
	
	public BridgeThingStatic(Location L)
	{
		super("ZelBridge.png");
		fin = L; 
	}
	
	@Override
	public void step()
	{
		//do nothing, 
		//BridgeThingStatic objects stay put once they interact with water.
	}
}
