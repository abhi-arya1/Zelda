/**
 * A class that contains all methods for a Link object in a Zelda 
 * game in a Grid Environment. 
 * @author abhia
 *
 */
public class LinkThing extends PokeThing
{
	/*
	 * Movement Strings
	 */
	public static final String UP = "UP";
	public static final String DOWN = "DOWN";
	public static final String LEFT = "LEFT";
	public static final String RIGHT = "RIGHT";
	
	/**
	 * States if Link has a Sword given by <code>OldManThing</code>
	 * (created via board)
	 */
	public boolean hasSword = false;
	
	/**
	 * States if Link has the Shield given in Level 5. 
	 */
	public boolean hasShield = false; 
	
	/**
	 * States if the Player has equipped Link's sword, in which case it 
	 * will change Link's behaviors. 
	 */
	public boolean swordEquipped = false;
	
	/**
	 * States if the Player has equipped Link's Shield, changing his behaviors. 
	 */
	public int shieldUses = 6; //Change to increase/decrease difficulty. 
	
	/**
	 * Checks if the Sword is on the Board (Level One Only)
	 */
	public boolean swordSpawned = false; 
	
	/**
	 * Creates a exit portal for each level that requires it. 
	 */
	public PokeThing portalThing = new PokeThing("Portal.png");
	
	/** 
	 * The Sword for Level 1
	 */
	public SwordThing s = new SwordThing(); 
	
	/*
	 * All these boolean values determine which levels have been completed,
	 * as a helper for <code>ZeldaBoard</code> 
	 * to switch the contents on the Board. 
	 */
	public boolean levelOneDone = false; 
	public boolean levelTwoDone = false; 
	public boolean levelThreeDone = false;
	public boolean levelFourDone = false;
	public boolean levelFiveDone = false;
	public boolean levelSixDone = false; //Final Level
	
	/**
	 * Declares what level <code>LinkThing</code> is on currently
	 */
	public int onLevel; 
	
	/* Stores the GUI of the Board that LinkThing is on */
	private Gui g; 
	
	int x = 0; //Counter var for Sword Functions. 
	int x1 = 0; //Second Counter var for Sword Functions. 
	int x2 = 0; //Counter var for Level Two. 
	
	/**
	 * Counts the number of Mobs Link Removed from the Board in Level 2. 
	 * Used by <code>OgreThing</code> in its specific "kill" method 
	 * as well. 
	 */
	public int level2MobsRemoved = 0; 
	
	/**
	 * Constructs a <code>LinkThing</code> based on Superclass (
	 * <code>PokeThing</code>) constructors. 
	 */
	public LinkThing()
	{
		super("ZeldaSpriteLinkFront.png", "defaultLink", 0);
		hasSword = false; 
		onLevel = 1; //Link is only created on Level 1 in ZeldaBoard, so he defaults to onLevel = 1. 
	}
	
	/**
	 * Link's Move Method
	 * @param r the shift in the row location 
	 * @param c the shift in the column location
	 * @param str the message "Up, Down, Left, Right" to be printed on movement
	 * @Precondition A key was pressed on the keyboard.
	 */
	public void move(int r, int c, String str)
	{
		
		ZeldaBoard b = (ZeldaBoard) getBoard();
		g = b.getGui();
		Location loc = getLocation();
		int row = loc.getRow();
		int col = loc.getColumn();
		
		switchImg(str); 
		
		Location toMove = new Location(row + r, col + c);
		//g.appendTextWindow(str);
		/*
		 * Since Level Three is very specific in how it handles movements, 
		 * there is a seperate <code>LevelThreeMovements</code> method used. 
		 */
		if(onLevel != 3 && onLevel != 7) 
		{
			if(b.thingAt(toMove) instanceof OgreThing)
			{
				OgreThing o = (OgreThing) b.thingAt(toMove);
				
				move(toMove);
				
				if(swordEquipped) 
				{
					b.remove(o);
					level2MobsRemoved++;
					g.appendTextWindow("Ogre was slain by: Link");
				}
				else 
				{
					b.linkReset();
					g.appendTextWindow("Link says: \"OUCH!\"");
				}
			}
			else if(b.thingAt(toMove) instanceof FireThing)
			{
				b.linkReset();
				g.appendTextWindow("Link says: \"That hurts!\"");
				
				if(onLevel == 4) 
					b.resetKeys();
			}
			else if(b.thingAt(toMove) instanceof KeyThing)
			{	
				KeyThing k = (KeyThing) b.thingAt(toMove);
				move(toMove); 
				b.remove(k);
				b.LinkKeys++; 
				g.appendTextWindow("Link says: \"Shiny!\"");
			}
			else if(b.thingAt(toMove) instanceof HeartThing)
			{
				HeartThing h = (HeartThing) b.thingAt(toMove);
				heartInteraction(h, toMove);
			}
			else if(b.thingAt(toMove) instanceof AquamentusThing)
			{
				AquamentusThing a = (AquamentusThing) b.thingAt(toMove);
				if(swordEquipped)
				{
					a.AquaLives--;
					g.appendTextWindow("Aquamentus has " + a.AquaLives + " lives left.");
					move(toMove);
				}
				else
				{
					g.appendTextWindow("Link said \"I need my sword!\"");
					b.linkReset();
				}
			}
			else if(!(b.thingAt(toMove) instanceof BlockedArea))
			{
				move(toMove);
			}
			else if(b.thingAt(toMove) instanceof FireballThing)
			{
				if(shieldUses <= 0) {
					b.linkReset();
					g.appendTextWindow("Link says \"OW!\"");
				}
				else
					b.remove((FireballThing) b.thingAt(toMove));
				
				move(toMove);
			}
		}
		else if(onLevel != 7)
		{
			levelThreeMovements(toMove, toMove.getRow(), toMove.getColumn()); 
		}
		//else (if level == 7), dont do anything. 
		
		/*
		 * Checks for each level, specific to Portal Placements, 
		 * and Sword, in the case of Level One.  
		 */
		if(onLevel == 1)
			stageOneChecks(b, g);
		else if(onLevel == 2)
			stageTwoChecks(b, g); 
		else if(onLevel == 3)
			stageThreeChecks(b, g);
		else if(onLevel == 4)
			stageFourChecks(b, g);
		else if(onLevel == 5)
			stageFiveChecks(b, g);
		else if(onLevel == 6)
			stageSixChecks(b, g);
	}

	// 4 Helper Methods for Movement (U, D, L, R),
	// therefore simplifying the code.
	public void moveUp()
	{  
		move(-1, 0, UP);	
	}
	
	public void moveDown()
	{   
		move(1, 0, DOWN);
	}
	
	public void moveLeft()
	{
		move(0, -1, LEFT);
	}
	
	public void moveRight()
	{
		move(0, 1, RIGHT);
	}
	
	/**
	 * Handles the movements on Level 3, including those
	 * related to <code>BridgeThing</code> objects. 
	 * @param toMove the Location link is moving to. 
	 * @param toRow the Row of toMove
	 * @param toCol the Column of toMove
	 * @Precondition Link is on Level 3.
	 */
	public void levelThreeMovements(Location toMove, int toRow, int toCol)
	{
		ZeldaBoard b = (ZeldaBoard) getBoard();
		
		if(b.thingAt(toMove) instanceof BridgeThing)
		{
			BridgeThing br = (BridgeThing) b.thingAt(toMove);
			/*
			 * Blocks aren't allowed to move to the edges, or areas
			 * where Link can no longer move them in a certain dir. 
			 */
			if(br.findLink().equals("Below")) // North/Up
			{
				if(toRow-1 != 1) 
				{
					{
						Location to = new Location(toRow-1, toCol); //Blocks can move to Water BlockedAreas
						if(!(b.thingAt(to) instanceof BlockedArea) || toRow-1 == 5) 
						{
							bridgeMoves(toMove, to, b, br);
						}
					} 
				}
			}
			else if(br.findLink().equals("Abv")) // South/Down
			{
				if(toRow+1 != 9) 
				{
					{
						Location to = new Location(toRow+1, toCol);
						if(!(b.thingAt(to) instanceof BlockedArea) || toRow+1 == 5) 
						{
							bridgeMoves(toMove, to, b, br);
						}
					}
				}
			}
			else if(br.findLink().equals("Left")) // East/Right
			{
				if(toCol+1 != 14) 
				{
					Location to = new Location(toRow, toCol+1);
					if(!(b.thingAt(to) instanceof BlockedArea) 
							|| br.getLocation().getRow() == 5) 
					{
						bridgeMoves(toMove, to, b, br);
					}
				}
			}
			else if(br.findLink().equals("Right")) // West/Left
			{
				if(toCol-1 != 0) 
				{
					Location to = new Location(toRow, toCol-1);
					if(!(b.thingAt(to) instanceof BlockedArea) 
							|| br.getLocation().getRow() == 5) 
					{
						bridgeMoves(toMove, to, b, br);
					}
				}
			}
		}
		else if(b.thingAt(toMove) instanceof WaterThing)
		{
			Gui g = b.getGui();
			move(toMove);
				
			//Comment out this portion for ease of access during testing. 
			b.linkReset();
			g.appendTextWindow("Link Drowned...");
		}
		else if(b.thingAt(toMove) instanceof HeartThing)
		{
			HeartThing h = (HeartThing) b.thingAt(toMove);
			heartInteraction(h, toMove);
		}
		else if(!(b.thingAt(toMove) instanceof BlockedArea))
		{
			move(toMove); // Default Move if Link is not interacting with anything. 
		}		
	}
	
	/*
	 * For all stage[x]Checks(b, g) methods, its understandable to 
	 * say that checking Locations for win conditions is inefficient,
	 * however there were too many issues otherwise, so this is the 
	 * route I went for. 
	 */
	
	/**
	 * Checks for special conditions that are detrimental to 
	 * the gameplay of Level One. 
	 * @param b the Board
	 * @param g the GUI to append status messages to, 
	 * 	      to reduce NullPointerExceptions
	 * @Precondition Link is on Stage 1.
	 */
	public void stageOneChecks(Board b, Gui g)
	{
		if(getLocation().equals(new Location(5, 9)) && !hasSword)
		{
			if(x1 == 0) 
			{
				g.appendTextWindow(
						"\nOld Man says: 'Wait! It's dangerous to go alone, take this!");
				b.add(s, new Location(6, 10));
				swordSpawned = true; 
				x1++;
			}
		}
		
		if(getLocation().equals(new Location(6, 10)) && !hasSword && swordSpawned)
		{
			b.remove(s);
			
			g.appendTextWindow(
					"\nLink acquired: 1x Sword \nPress '1' to Equip.");
			
			hasSword = true; 
			getBoard().add(portalThing, new Location(5, 15));
			g.setLossMessage("1. Sword (Equipped: false)", true);
			g.repaint(); 
		}
		
		if(getLocation().equals(new Location(5, 15)))
		{
			levelOneDone = true; 
			b.remove(this);
		}
	}
	
	/**
	 * Checks for conditions related to gameplay of Level Two. 
	 * @param b the Board that Link is on.
	 * @param g the GUI of the Board. 
	 * @Precondition Link is on Stage 2.
	 */
	public void stageTwoChecks(ZeldaBoard b, Gui g)
	{
		if(level2MobsRemoved == 6) //SET TO 0 FOR TESTING (SKIP LEVEL 2), SET 6 FOR GAME
		{
			if(x2 == 0) {
				b.add(portalThing, new Location(5, 0));
				x2++;
			}
		}
		
		if(getLocation().equals(new Location(5, 0)))
		{
			levelTwoDone = true; 
			b.remove(this); 
		}
		
	}

	/**
	 * Checks for conditions related to gameplay of Level Three. 
	 * @param b the Board that Link is on.
	 * @param g the GUI of the Board. 
	 * @Precondition Link is on Stage 3.
	 */
	public void stageThreeChecks(ZeldaBoard b, Gui g) 
	{
		if(getLocation().equals(new Location(5, 15)))
		{
			levelThreeDone = true;
			b.remove(this);
		}
	}
	
	/**
	 * Checks if Link has 5 keys, to end Stage 4. 
	 * @param b the ZeldaBoard
	 * @param g Board GUI
	 * @Precondition Link is on Stage 4.
	 */
	public void stageFourChecks(ZeldaBoard b, Gui g)
	{
		if(b.LinkKeys == 5)
		{
			levelFourDone = true;
			g.setWinMessage("2. 5 Keys", true);
			b.remove(this);
		}
	}
	
	/**
	 * Checks if Link has interacted with the Portal to end Stage 5, and checks Shield
	 * conditions.  
	 * @param b the ZeldaBoard
	 * @param g the GUI
	 * @Precondition Link is on Stage 5.
	 */
	public void stageFiveChecks(ZeldaBoard b, Gui g)
	{
		if(getLocation().equals(new Location(5, 1)))
		{
			levelFiveDone = true; 
			b.remove(this);
		}
		
		if(getLocation().equals(new Location(4, 9)) && !hasShield)
		{
			hasShield = true;
			b.remove(b.shield);
			g.appendTextWindow("Link says: \"Safety is key!\"");
			g.setWinMessage("2. Shield (Uses: 6)", true);
			g.appendTextWindow("Link acquired: 1x Shield \nEquipped by Default.");
		}
		
	}
	
	/**
	 * Checks Link's Stage 6 Interactions (Portal)
	 * @param b the ZeldaBoard
	 * @param g the GUI
	 * @Precondition Link is on Stage 6.
	 */
	public void stageSixChecks(ZeldaBoard b, Gui g)
	{
		if(getLocation().equals(new Location(5, 15)))
		{
			levelSixDone = true; 
			b.remove(this);
		}
		
		if(b.thingAt(getLocation()) instanceof FireballThing)
		{
			if(hasShield && shieldUses > 0)
			{
				b.remove((FireballThing) b.thingAt(getLocation()));
				g.appendTextWindow("Shield took a hit!");
			}
			else
			{
				b.linkReset();
				g.appendTextWindow("Link said: \"OW!\"");
			}
		}
	}

	/**
	 * Used when the player presses '1' on the Keyboard on 
	 * any Level, equipping the Sword if player has a Sword
	 */
	public void equipSword() 
	{
		Board b = getBoard();
		Gui g = b.getGui();
		
		if(hasSword) {			
			g.appendTextWindow("Sword Equipped!");
			swordEquipped = true; 
		}
		else
			g.appendTextWindow("You don't have a Sword!");
	}
	
	/**
	 * Helper function for <code>levelThreeMovements()</code> to simplify Bridge
	 * and Link movements. 
	 * @param toMove the Location Link moves to
	 * @param brToMove the Location the Bridge moves to
	 * @param b the ZeldaBoard
	 * @param br the Bridge Object. 
	 * @Precondition Link is going to move a BridgeThing, and is on Level 3. 
	 */
	public void bridgeMoves(Location toMove, Location brToMove, ZeldaBoard b, BridgeThing br)
	{
		if(b.thingAt(brToMove) instanceof WaterThing)
		{
			b.add(new BridgeThingStatic(brToMove), brToMove);
			br.move(brToMove);
			b.remove(br);
			move(toMove);
		}
		else {
			br.move(brToMove);
			move(toMove);
		}
	}
	
	/**
	 * Switches the link image based on movemnet and level.
	 * @param str the string representing the direction that Link moved in
	 */
	public void switchImg(String str)
	{
		if(onLevel != 6) 
		{
			if(str.equals(UP))
				setImageFileName("ZeldaSpriteLinkBack.png");
			else if(str.equals(DOWN))
				setImageFileName("ZeldaSpriteLinkFront.png");
			else if(str.equals(LEFT))
				setImageFileName("ZeldaSpriteLinkLeft.png");
			else if(str.equals(RIGHT))
				setImageFileName("ZeldaSpriteLinkRight.png"); 
		}
		else
		{
			if(str.equals(UP))
				setImageFileName("ZeldaSpriteLinkBackBlue.png");
			else if(str.equals(DOWN))
				setImageFileName("ZeldaSpriteLinkFrontBlue.png");
			else if(str.equals(LEFT))
				setImageFileName("ZeldaSpriteLinkLeftBlue.png");
			else if(str.equals(RIGHT))
				setImageFileName("ZeldaSpriteLinkRightBlue.png");
		}
	}
	
	/**
	 * Helper function for move() and levelThreeMovements() to reduce 
	 * repetitive HeartThing scanning code. 
	 * @param h the HeartThing that Link is about to move to. 
	 * @Precondition Link is near a HeartThing
	 */
	public void heartInteraction(HeartThing h, Location toMove)
	{
		ZeldaBoard b = (ZeldaBoard) getBoard();
		
		move(toMove);
		
		if(b.LinkLives < 3)
		{
			b.LinkLives++;
			b.remove(h);
			g.appendTextWindow("Link says: \"Yay!\"");
		}
		else
			g.appendTextWindow("Woah, too many hearts!");
	}
	
	/**
	 * Unused step method, because Link does not have 
	 * any functions which rely on the timer of the GUI.  
	 */
	public void step()
	{
		
	}
}
