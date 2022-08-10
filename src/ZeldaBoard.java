/**
 * Zelda Board Initializer and Method Holder for Game Map-related
 * Functions. 
 */
public class ZeldaBoard extends Board implements Maps
{

	/** The main character of Zelda, Link */
	private LinkThing link;
	/**Number of Lives that Link has */
	public int LinkLives = 3; 
	/** The Location that Link spawns on for each Map (refreshed) */
	private Location newSpawns;
	/** Number of Keys Link has on Level 5 */
	public int LinkKeys;
	
	/** Counter for Fire on Level 4 */
	private int fireCount = 0; 
	/** Max Seconds that Fire is Displayed on Level 4 */
	private final static int FIRE_MAX = 10;
	/** Direction of the Fire on Level 4 */
	private String level4Dir;

	/*
	 * All gameplay objects besides Link and the Portal are
	 * contained within the <code>ZeldaBoard</code> class. 
	 * These are class member variables so that way they don't 
	 * have to be recreated in each of the setStage[Number]() 
	 * methods.
	 * 
	 */
	
	/*
	 * All <code>BlockedArea</code> objects cannot be moved to by <code>LinkThing</code>
	 */
	BlockedArea greenRockThing = new BlockedArea("GreenRock.png");
	BlockedArea treeThing = new BlockedArea("TreeThing.png");
	BlockedArea redRock = new BlockedArea("RedRock.png"); 
	
	
	/*
	 * All Passive Mobs (Non-Player Passive Characters)
	 */
	PassiveMobThing oldManThing = new PassiveMobThing("OldMan.png");
	
	/*
	 * All items contained in Board
	 */
	HeartThing heart = new HeartThing(); 
	ShieldThing shield = new ShieldThing(); 
	
	/*
	 * Aquamentus (Final Boss)
	 */
	AquamentusThing aquaHead = new AquamentusThing("AquaHead.png");
	BlockedArea aquaTopRight = new BlockedArea("AquaTopRight.png");
	BlockedArea aquaRightFoot = new BlockedArea("AquaRightFeet.png");
	BlockedArea aquaLeftFoot = new BlockedArea("AquaFootLeft.png");
	
	/*
	 * Final strings for movement, so that way 
	 * inputs can be changed if needed. 
	 */
	public static final String UP = "UP";
	public static final String DOWN = "DOWN";
	public static final String LEFT = "LEFT";
	public static final String RIGHT = "RIGHT";
	public static final String W = "W";
	public static final String A = "A";
	public static final String S = "S";
	public static final String D = "D";
	
	/**
	 * The Help text added to the Text Window when the User types
	 * "!h" or "help"
	 */
	private static final String HELP =
		"This game is a replica of the original version of the Legend of Zelda, albeit"
		+ " \nwatered down a touch. Each level has a solution, you have to be resourceful"
		+ " \nto find it. Link can be controlled with W, A, S, and D, or the Arrow Keys."
		+ " \nOnce you get a Sword in the first level, press '1' at any time"
		+ " \nto equip it. Any other items will be automatically checked for you."
		+ " \nOnce you get to the 4th Level, remember to wait and look for the placement of fire,"
		+ " \nrather than speeding through the level, for your own sake. On level 5, get the Shield"
		+ " \nCheck this text box for any important updates throughout the game."
		+ "\n I hope you enjoy. - Abhi";
	
	
	/*
	 * 2 variables to contain the rows and columns of the board, 
	 * for easier traversing later on (in setStage methods)
	 */
	private int rows;
	private int cols;
	
	private int c1 = 0; //Counter variable that is used for setStage(int) the first 
	   					//time that the game is run. 
	private int x3 = 0; //Counter variable for Level 6 (Aquamentus)
	
	/**
	 * Creates a new <code>PokeBoard</code> with the specified number of rows and columns.
	 */
	public ZeldaBoard() 
	{
		// Call the Board constructor to make an 11x16 Board
	 	super(11, 16); 
	 	newGame(false);
	 	setTitle("\"The Legend of Zelda\" - APCSA Semester Two Final Project - "
	 			+ "Abhigyan Arya");
	 	
	 	setInitialText("Welcome.\nUse WASD or ArrowKeys to Move. \nType '!h' or 'help' (case-sensitive) in the text box at any "
	 			+ "time if necessary. Have Fun and Good Luck Traveler!");
	 	
	 	//Initializes key variables. 
	 	rows = getRows();
	 	cols = getColumns();
	 	
	 	setStage(1); 
	 	c1++;
	}
	

	/**
	 * Initial placement of all PokeThings in the PokeBoard
	 */
	@Override
	public void newGame(boolean repaint)
	{
		super.newGame(repaint);
		if(c1 == 1)
			setStage(1); 
		LinkLives = 3; 
		LinkKeys = 0; //Change to 5 to Skip Level 4 (For Testing)
	
	}
	
	/**
	 * Calls <code>LinkThing</code> Methods for character Movement, 
	 * that can be done with Arrow Keys or WASD. This also checks Link's level
	 * on every movement. 
	 * @return false in all cases, throwaway
	 */
	public boolean keyPressed(String description)
	{
		if (description.equals(UP) || description.equals(W))
		{ 
			link.moveUp();
			checkLinkLevelUpdate(); 
		}
		if (description.equals(DOWN) || description.equals(S))
		{
			link.moveDown();
			checkLinkLevelUpdate(); 
		}
		if (description.equals(RIGHT) || description.equals(D))
		{
			link.moveRight();
			checkLinkLevelUpdate(); 
		}
		if (description.equals(LEFT) || description.equals(A))
		{
			link.moveLeft();
			checkLinkLevelUpdate(); 
		}


		if(description.equals("1"))
		{
			link.equipSword(); 
		}
		
		if (description.equals(" "))
		{
			Gui g = getGui();
			g.appendTextWindow("No Key Input Detected");
		}
		
			return false; // Does nothing related to game operation, simply for the method to work. 
	}
	
	@Override
	/**
	 * Directs help return msg. to Text Window
	 */
	public void textInput(String s, boolean fromConsole)
	{
		Gui g = getGui(); 
		if(s.equals("!h") || s.equals("help"))
		{
			g.appendTextWindow(HELP);
		}
	}
	
	/**
	 * Checks if Link has moved up a level after each movement, and 
	 * calls the board adjustment method accordingly. 
	 * @Precondition Link moved.
	 */
	public void checkLinkLevelUpdate()
	{
		if(link.levelOneDone) 
		{
			setStage(2);
			link.onLevel++;
			link.levelOneDone = false; 
			link.swordEquipped = false;
		}
		if(link.levelTwoDone)
		{
			setStage(3);
			link.onLevel++;
			link.levelTwoDone = false; 
			link.swordEquipped = false;
		}
		if(link.levelThreeDone)
		{
			setStage(4);
			link.onLevel++;
			link.levelThreeDone = false; 
			link.swordEquipped = false;
		}
		if(link.levelFourDone)
		{
			setStage(5);
			link.onLevel++;
			link.levelFourDone = false;
			link.swordEquipped = false; 
		}
		if(link.levelFiveDone)
		{
			setStage(6);
			link.onLevel++;
			link.levelFiveDone = false;
			link.swordEquipped = false;
		}
		if(link.levelSixDone)
		{
			setStage(7);
			link.onLevel++;
			link.levelSixDone = false;
			link.swordEquipped = false; 
		}
	}
	
	/**
	 * Uses <code>Maps.String[][]</code> objects to initialize each stage of the game.
	 * @param stage the level to switch to, for specific initializing objects. 
	 * @Precondition <code>Maps.java</code> has valid Board setups for *EACH* level.
	 */
	public void setStage(int stage)
	{
		removeAll(false);
		String[][] board = null;
		
		if(stage == 1) 
		{
			link = new LinkThing(); 
			LinkLives = 3; 
			board = Maps.levelOne;
			add(oldManThing, new Location(5, 10));
		}
		else if(stage == 2) 
		{
			add(new OgreThing(Direction.NORTH), new Location(5, 4));
			add(new OgreThing(Direction.SOUTH), new Location(5, 5));
			
			add(new OgreThing(Direction.NORTH), new Location(5, 6));
			add(new OgreThing(Direction.SOUTH), new Location(5, 7));
			
			add(new OgreThing(Direction.NORTH), new Location(5, 8));
			add(new OgreThing(Direction.SOUTH), new Location(5, 9));
			
			board = Maps.levelTwo;
		}
		else if(stage == 3) 
		{
			Gui g = getGui();
			g.appendTextWindow("\nLink isn't a very good swimmer, so its not recommended to let "
					+ "him swim. Instead, build a bridge across the river!\n"
					+ "If you can't see a block, it is on top of "
					+ "another one. Blocks must be attached to "
					+ "the dock or other blocks. \nOnce placed over water, "
					+ "blocks can't be moved!");
			
			board = Maps.levelThree;
			
			add(new PokeThing("ZeldaDock.png"), new Location(5,7));
			add(heart, new Location(1, 15));
		}
		else if(stage == 4)
		{
			board = Maps.levelFour_VERT;
			level4Dir = "VERT";
			add(link, new Location(5, 15));
			newSpawns = new Location(5, 15);
			
			getGui().appendTextWindow("\nA mysterious voice says: \""
					+ "Patience is key! Look at the blocks and the fire in relation "
					+ "to them! You got this, Link!\"");
			
			resetKeys();
		}
		else if(stage == 5)
		{
			board = Maps.levelFive;
			newSpawns = new Location(5, 5);
			add(link, newSpawns);
			link.move(newSpawns);
			
			add(link.portalThing, new Location(5, 1));
			add(shield, new Location(4, 9));
			add(heart, new Location(6, 9));
			
			getGui().appendTextWindow("*Psst*, Heres a hint! Getting the Shield makes everything much easier!");
		}
		else if(stage == 6)
		{
			board = Maps.levelSix;
			newSpawns = new Location(5, 3);
			add(link, newSpawns);
			link.move(newSpawns);
			
			add(aquaHead, new Location(4, 13));
			add(aquaTopRight, new Location(4, 14));
			add(aquaLeftFoot, new Location(5, 13));
			add(aquaRightFoot, new Location(5, 14));
			
			getGui().appendTextWindow("Aquamentus is here! "
					+ "You will need your sword, and Aquamentus has a weak spot on its head. \nGo for that! You got this, Link!");
		}
		else if(stage == 7)
		{
			board = Maps.levelSeven;
			add(new PokeThing("LinkStatic.png"), new Location(5, 7));
			add(new PokeThing("PrincessZelda.png"), new Location(5, 9));
			
			getGui().appendTextWindow(
					"\nYou saved Zelda! Thank you for playing! \nPress 'RESTART' to Play Again."
					+ "\nThank you Mr. Sarris for a great year!");
		}
		
		
		fillBoard(board);
	}
	
	/**
	 * Fills the Board with Different Objects based on the Letter Value of 
	 * the <code>Maps.String[][]</code> element. 
	 * @param b the String passed through from <code>setStage(int)</code> to use to fill the board. 
	 * @Precondition param b is not null. 
	 */
	public void fillBoard(String[][] b)
	{
		for(int i = 0; i < b.length; i++)
		{
			for(int j = 0; j < b[0].length; j++)
			{
				Location newOne = new Location(i, j);
				
				if(b[i][j].equals("G"))
					add(greenRockThing, newOne);
				
				if(b[i][j].equals("T"))
					add(treeThing, newOne);
				
				if(b[i][j].equals("G"))
					add(greenRockThing, newOne);
				
				if(b[i][j].equals("Y")) 
				{
					add(link, newOne);
					newSpawns = newOne;
				}
				
				if(b[i][j].equals("R"))
					add(redRock, newOne);
				
				if(b[i][j].equals("P"))
					add(link.portalThing, newOne);
				
				if(b[i][j].equals("W"))
					add(new WaterThing(), newOne);
				
				if(b[i][j].equals("B"))
					add(new BridgeThing(), newOne);
				
				if(b[i][j].equals("F")) 
				{
					if(link.getLocation().equals(newOne))
					{
						getGui().appendTextWindow("Link says: 'That Hurts!!'");
						
						linkReset();
					}
					add(new FireThing(0), newOne);
				}
				
				if(b[i][j].equals("Z"))
					add(new FireThing(1), newOne);
				
				if(b[i][j].equals("L"))
					add(new BlockedArea("ZeldaBlueBlock.png"), newOne);
				
				if(b[i][j].equals("N")) 
				{
					if(newOne.equals(new Location(5, 3)))
					{
						add(new BlueArea(), newOne);
						add(link, newOne);
						link.setImageFileName("ZeldaSpriteLinkRightBlue.png");
					}
					else
						add(new BlueArea(), newOne);
				}
				
				if(b[i][j].equals("M"))
					add(new PokeThing("Portal.png"), newOne);
			}
		}	
	}
	
	/**
	 * Uses the accelerated Board.step() method from <code>PacBoard</code>, 
	 * and checks for Link's lives on every step. 
	 */
	public void step()
	{
		//Function from PacMan for Accelerated Step --------------------------------
		Gui g = getGui(); 
		
		for (int i = 0; i < getRows(); i++)
		{
		   for (int j = 0; j < getColumns(); j++)
		   {
		      Thing t = things[i][j].get();
		      if (!t.getRepaint())
		      {  
		    	 // Does not refresh BlockedAreas or Passive Mobs (Fixed Placements)
		         if (!t.isBlank() && !(t instanceof BlockedArea) && !(t instanceof PassiveMobThing) 
		        		 && !(t instanceof BlueArea))
		         {
		            t.setRepaint(true);
		            t.step();
		         }
		      }
		   }
		}
		for (int i = 0; i < getRows(); i++)
		{
		   for (int j = 0; j < getColumns(); j++)
		   {
		      things[i][j].get().setRepaint(false);
		   }
		}
		repaint();
		//------------------------------------------------------------
		//Other ZeldaBoard.step() methods
		
		//Checks and sets Lives every step. 
		g.setTotalsMessage("Hearts: " + LinkLives, true);
		
		//Restarts if Lives are 0 or Negative.
		if(LinkLives <= 0)
		{
			g.appendTextWindow("\nYou lost all your lives... \nRestarting now...");
			newGame(false); 
			ItemResets(getGui());
		}
		
		//Switches Fire Direction on Level 4. 
		if(link.onLevel == 4)
		{
			if(level4Dir.equals("VERT") && fireCount == FIRE_MAX) 
			{
				fireCount = 0; 
				clearFire();
				fillBoard(Maps.levelFour_HORIZ);
				level4Dir = "HORIZ";
			}
			else if(level4Dir.equals("HORIZ") && fireCount == FIRE_MAX)
			{
				fireCount = 0; 
				clearFire();
				fillBoard(Maps.levelFour_VERT);
				level4Dir = "VERT";
			}
			
			if(fireCount > 5)
			{
				//Makes "second(s)" at the end gramatically correct. 
				if(FIRE_MAX - fireCount == 1)
					getGui().appendTextWindow("Switching Fire in: " + (FIRE_MAX - fireCount) + " second.");
				else
					getGui().appendTextWindow("Switching Fire in: " + (FIRE_MAX - fireCount) + " seconds.");
			}
			
			fireCount++; 
			
			//Changes Keys counter, and stops it when Link gets a shield on Level 5. 
			if(LinkKeys > -1 && !link.hasShield)
			{
				//Makes "Key(s)" gramatically correct. 
				if(LinkKeys == 1)
					g.setWinMessage("2. " + LinkKeys + " Key", true);
				else if(LinkKeys > 1 || LinkKeys == 0)
					g.setWinMessage("2. " + LinkKeys + " Keys", true);
			}
		}
		
		if(link.onLevel == 6)
		{
			if(aquaHead.AquaLives <= 0 && x3 == 0)
			{
				remove(aquaHead);
				remove(aquaTopRight);
				remove(aquaRightFoot);
				remove(aquaLeftFoot);
				remove(link);
				
				add(link.portalThing, new Location(5, 15));
				
				add(new BlueArea(), new Location(4, 13));
				add(new BlueArea(), new Location(5, 13));
				add(new BlueArea(), new Location(4, 14));
				add(new BlueArea(), new Location(5, 14));
				
				add(link);
				g.appendTextWindow("Zelda says: \"Link, You Did It!\"");
				x3++;
			}
		}
		
		
		//Sets Link hasSword message every step.
		if(link.hasSword)
			g.setLossMessage("1. Sword (Equipped: " + link.swordEquipped + ")", true);
		
		//Sets Link hasShield message every step.
		if(link.hasShield)
			g.setWinMessage("2. Shield (Uses: " + link.shieldUses + ")", true);

	}
	
	/**
	 * Moves Link back to the Location that he spawned at 
	 * independent of level.
	 */
	public void linkReset()
	{
		LinkLives--;
		link.move(newSpawns);
	}
	
	/**
	 * Resets the Items that Link has once he loses all of his lives. 
	 * @param g the Board GUI
	 */
	public void ItemResets(Gui g)
	{
		g.setWinMessage("", false);
		g.setLossMessage("", false); 
	}
	
	/**
	 * Used in Level 4, this method clears all the fire objects on the board
	 * every time the fire switches directions. 
	 * @Precondition Link is on Stage 4.
	 */
	public void clearFire()
	{
		for(int i = 0; i < rows; i++)
		{
			for(int j = 0; j < cols; j++)
			{
				if(thingAt(new Location(i, j)) instanceof FireThing)
				{
					FireThing f = (FireThing) thingAt(new Location(i, j));
					remove(f);
				}
			}
		}
	}	
	
	/**
	 * Reset Keys function for Level 4.
	 */
	public void resetKeys()
	{
		LinkKeys = 0; //Change to 5 to skip level 4. 
		
		if(!(thingAt(new Location(0, 4)) instanceof KeyThing))
			add(new KeyThing(), new Location(0, 4));
		if(!(thingAt(new Location(8, 6)) instanceof KeyThing))
			add(new KeyThing(), new Location(8, 6));
		if(!(thingAt(new Location(2, 15)) instanceof KeyThing))
			add(new KeyThing(), new Location(2, 15));
		if(!(thingAt(new Location(2, 10)) instanceof KeyThing))
			add(new KeyThing(), new Location(2, 10));
		if(!(thingAt(new Location(6, 0)) instanceof KeyThing))
			add(new KeyThing(), new Location(6, 0));
	}
}
