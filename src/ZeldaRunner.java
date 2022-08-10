//NOTE: IN ALL CLASSES IN THIS CODE, "STAGE" AND "LEVEL" ARE 
//      USED INTERCHANGEABLY

/**
 * This is a class that plays the a demonstration of the GridWorld v2.0 environment,
 * replicating the classic NES game, The Legend Of Zelda
 *
 */
public class ZeldaRunner 
{

	/**
	 * Runs Zelda
	 * @param args is not used.
	 */
	public static void main(String[] args) 
	{
		// Create the board 
		ZeldaBoard board = new ZeldaBoard();
				
		// Create the gui and use the board's info to run the game.
		Gui gui = new Gui(board, board.getRows(), 
				board.getColumns(), board.getTitle());
		
		gui.setTotalsMessage("Hearts: " + board.LinkLives, true);
		gui.setTextWindow(board.getInitialText());
		gui.setWinMessage("", false);
		gui.setLossMessage("", false);
		gui.displayGame();
		
		gui.setSuppress(true);
	}
}