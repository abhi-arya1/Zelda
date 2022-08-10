/** 
 *  Seperates Passive Mobs from Hostile Mobs, however
 * 	since Link cannot stand on top of Passive Mobs, they are
 *  considered a blocked area. 
 */
public class PassiveMobThing extends BlockedArea
{
	public PassiveMobThing(String imgName)
	{
		super(imgName);
	}
}
