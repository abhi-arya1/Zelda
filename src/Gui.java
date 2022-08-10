import java.awt.Point;
import java.awt.Graphics;
import java.awt.KeyEventDispatcher;
import java.awt.KeyboardFocusManager;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Color;
import java.awt.Toolkit;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.ActionEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseEvent;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.ImageIcon;
import java.net.URL;
import java.util.List;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.*;
/**
 * This class provides a GUI for grid-based games.
 * This class started life as the GUI class for ElevensBoard but has been significantly modified.
 */
@SuppressWarnings("serial")
public class Gui extends JFrame implements ActionListener {

	/** Counter for Start Button. */ 
	private int x = 0;
	
	/** Width of a tile. */
	private static final int DEFAULT_TILE_WIDTH = 50;
	/** Height of a tile. */
	private static final int DEFAULT_TILE_HEIGHT = 50;
	/** Height of the game frame. */
	private static final int DEFAULT_HEIGHT = DEFAULT_TILE_WIDTH * 3;  // Two tiles, plus some extra
	/** Width of the game frame. */
	private static final int DEFAULT_WIDTH = DEFAULT_TILE_HEIGHT * 3; // Two tiles, plus some extra
	
	/** Row (y coord) of the upper left corner of the first tile. */
	private static final int LAYOUT_TOP = 30;
	/** Column (x coord) of the upper left corner of the first tile. */
	private static final int LAYOUT_LEFT = 30;
	
	/** Distance between the upper left x coords of two horizonally adjacent tiles. 
	private static int LAYOUT_WIDTH_INC = DEFAULT_TILE_WIDTH;
	 ** Distance between the upper left x coords of two vertically adjacent tiles. 
	private static int LAYOUT_HEIGHT_INC = DEFAULT_TILE_HEIGHT;
	 ** Y value for the topmost label on the right side ("status msg") 
	private static final int LABEL_TOP = 160;
	 ** Y value for the height of the labels on the right side 
	private static final int LABEL_HEIGHT_INC = 35; */
	
	/** Y value for the topmost button on the right side ("Start") */
	private static final int BUTTON_TOP = 30;
	/** Y value for the height of the buttons on the right side */
	private static final int BUTTON_HEIGHT_INC = 50;
	
	/** X value for the width of the buttons on the right side */
	private static final int BUTTON_SPACE = 100;
	/** Y value to represent the space between buttons and game tiles */
	private static final int BUFFER = 50;
	/** Y value to represent the space the messages */
	private static final int MESSAGE = 75;
	/** Y value to represent the space between buttons */
	private static final int Y_BUFFER = 50;
	
	/** Height of the text information window at the bottom */
	private static final int LAYOUT_TEXTWINDOW = 70;
	/** Height of the text information window at the bottom */
	private static final int LAYOUT_TEXTFIELD = 20;
	/** Buffer between the text information window and the last game tile row */
	private static final int TEXTFIELD_Y_BUFFER = 10;
	/** Buffer between the text console and the text information window*/
	private static final int TEXTWINDOW_Y_BUFFER = 20;
	
	/** Values to control the timer and step counters */
	private static final int MIN_DELAY_MSECS = 10, MAX_DELAY_MSECS = 1000;
	/** Initial value for the timer and slider */
    private static final int INITIAL_DELAY = MIN_DELAY_MSECS
            + (MAX_DELAY_MSECS - MIN_DELAY_MSECS) / 2;

	/** The board (<code>Board</code> subclass). */
	private Board board;
	/** The <code>Timer</code> that controls how fast/slow the game runs */
	private Timer timer;

	/** The main panel containing the game components. */
	private JPanel panel;
	/** The Start button - begins automatic execution of the <code>Board</code>*/
	private JButton startButton;
	/** The Stop button - stops automatic execution of the <code>Board</code>*/
	private JButton stopButton;
	/** The Step button - executes one step() call of the <code>Board</code>*/
	private JButton stepButton;
	/** The Restart button - returns the <code>Board</code> to the initial state*/
	private JButton restartButton;
	/** The speed slider - changes how often the <code>Board</code>'s step() function is called*/
	private JSlider speedSlider;
	
	
	/** The actual drawn components -- one for each square in the grid */
	private JLabel[][] displayThings;
	
	/** The status message - first message on the right side of the window*/
	private JLabel statusMsg;
	/** The win message - second message on the right side of the window (default: invisible and green)*/
	private JLabel winMsg;
	/** The lose message - third message on the right side of the window (default: invisible and green)*/
	private JLabel lossMsg;
	/** The totals message - fourth message on the right side of the window */
	private JLabel totalsMsg;
	/** The text field - console field for entering commands */
	private JTextField textField;
	/** The text window - console window under the game board tiles */
	private JTextArea textWindow;
	
	
	/** The coordinates of each square's display */
	private Point[][] thingCoords;

	/** selections is true iff the user has selected the square at the coordinates. */
	private boolean[][] selections;
	/** The number of games won. default: not used */
	private int totalWins;
	/** The number of games played. default: not used */
	private int totalGames;
	
	/** The number of rows of tiles on the game board */
	private int rows;
	/** The number of columns of tiles on the game board */
	private int columns;
	/** The leftmost location of buttons */
	private int button_left;
	/** The leftmost location of text labels */
	private int label_left;
	/** The text in the status message */
	private String statusMsgText;
	/** Whether or not to  print to console */
	private boolean suppress = false;
	/** Whether or not we are currently running a test */
	private boolean inTest = false;
	
	
	public String fileName = "blank.gif";
	
	/**
	 * Creates a new <code>Gui</code> object to control all the drawing.
	 * @param gameBoard The <code>Board</code> derived class containing game/program logic
	 * @param rows The number of rows of tiles
	 * @param columns The number of columns of tiles
	 * @param applicationName The string to appear in the title of the application window
	 */
	public Gui(Board gameBoard, int rows, int columns, String applicationName) 
	{
		this(gameBoard, rows, columns, applicationName, false);
	}

	public Gui(Board gameBoard, int rows, int columns, int hSize, int vSize, String applicationName) 
	{
		this(gameBoard, rows, columns, hSize, vSize, applicationName, false);
	}
	
	/**
	 * Secondary constructor to create a new <code>Gui</code> object to control all the drawing.
	 * @param gameBoard The <code>Board</code> derived class containing game/program logic
	 * @param rows The number of rows of tiles
	 * @param columns The number of columns of tiles
	 */
	public Gui(Board gameBoard, int rows, int columns)
	{
		this(gameBoard, rows, columns, "", false);
	}
	
	/**
	 * Tertiary constructor to create a new <code>Gui</code> object for testing
	 * @param gameBoard The <code>Board</code> derived class containing game/program logic
	 * @param rows The number of rows of tiles
	 * @param columns The number of columns of tiles
	 */
	public Gui(Board gameBoard, int rows, int columns, String applicationName, boolean inTest)
	{
		board = gameBoard;
		gameBoard.setGui(this);
		totalWins = 0;
		totalGames = 0;
		this.inTest = inTest;
		
		// Automatically attempt to determine the size and tile size of the board
		determineSize(gameBoard);

		// Create the coordinates of all the tile objects
		thingCoords = new Point[rows][columns];
		int x = LAYOUT_LEFT;
		int y = LAYOUT_TOP;
		for (int i = 0; i < rows; i++)
		{
			for (int j = 0; j < columns; j++)
			{
				thingCoords[i][j] = new Point(x, y);
				x += board.getTileX();
			}
			x = LAYOUT_LEFT;
			y += board.getTileY();
		}
		
		// Determine where to put the buttons/text labels on the right
		button_left = columns * board.getTileX() + BUFFER;
		label_left = button_left;
		
		this.rows = rows;
		this.columns = columns;
		selections = new boolean[rows][columns];
		initDisplay(applicationName);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		if (!inTest)
		{
			repaint();
		}
		
		// Create the timer that controls how fast the program goes
		timer = new Timer(INITIAL_DELAY, new ActionListener()
        {
            public void actionPerformed(ActionEvent evt)
            {
                step();
            }
        });
		
		// Create a KeyEvent manager to capture key presses
		KeyboardFocusManager.getCurrentKeyboardFocusManager().addKeyEventDispatcher(new
		           KeyEventDispatcher() 
		{
			public boolean dispatchKeyEvent(KeyEvent event)
		    {
				if (getFocusOwner() == null) return false;
		        String text = KeyStroke.getKeyStrokeForEvent(event).toString();
		        final String PRESSED = "pressed ";                  
		        int n = text.indexOf(PRESSED);
		        if (n < 0) return false;
		        // filter out modifier keys; they are neither characters or actions
		        if (event.getKeyChar() == KeyEvent.CHAR_UNDEFINED && !event.isActionKey()) 
		        	return false;
		        text = text.substring(0, n)  + text.substring(n + PRESSED.length());
		        boolean consumed = board.keyPressed(text);
		        if (consumed) repaint();
		        return consumed;
		     }
		 });
	}
	
	
	public Gui(Board gameBoard, int rows, int columns, int hSize, int vSize, String applicationName, boolean inTest)
	{
		board = gameBoard;
		gameBoard.setGui(this);
		totalWins = 0;
		totalGames = 0;
		this.inTest = inTest;
		
		// Automatically attempt to determine the size and tile size of the board
		// determineSize(gameBoard);

		// Create the coordinates of all the tile objects
		thingCoords = new Point[rows][columns];
		int x = LAYOUT_LEFT;
		int y = LAYOUT_TOP;
		for (int i = 0; i < rows; i++)
		{
			for (int j = 0; j < columns; j++)
			{
				thingCoords[i][j] = new Point(x, y);
				x += board.getTileX();
			}
			x = LAYOUT_LEFT;
			y += board.getTileY();
		}
		
		// Determine where to put the buttons/text labels on the right
		button_left = columns * board.getTileX() + BUFFER;
		label_left = button_left;
		
		this.rows = rows;
		this.columns = columns;
		selections = new boolean[rows][columns];
		initDisplay(applicationName);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		if (!inTest)
		{
			repaint();
		}
		
		// Create the timer that controls how fast the program goes
		timer = new Timer(INITIAL_DELAY, new ActionListener()
        {
            public void actionPerformed(ActionEvent evt)
            {
                step();
            }
        });
		
		// Create a KeyEvent manager to capture key presses
		KeyboardFocusManager.getCurrentKeyboardFocusManager().addKeyEventDispatcher(new
		           KeyEventDispatcher() 
		{
			public boolean dispatchKeyEvent(KeyEvent event)
		    {
				if (getFocusOwner() == null) return false;
		        String text = KeyStroke.getKeyStrokeForEvent(event).toString();
		        final String PRESSED = "pressed ";                  
		        int n = text.indexOf(PRESSED);
		        if (n < 0) return false;
		        // filter out modifier keys; they are neither characters or actions
		        if (event.getKeyChar() == KeyEvent.CHAR_UNDEFINED && !event.isActionKey()) 
		        	return false;
		        text = text.substring(0, n)  + text.substring(n + PRESSED.length());
		        boolean consumed = board.keyPressed(text);
		        if (consumed) repaint();
		        return consumed;
		     }
		 });
	}
	
	/**
	 * Run the game.
	 */
	public void displayGame() {
		java.awt.EventQueue.invokeLater(new Runnable() {
			public void run() {
				setVisible(true);
			}
		});
	}

	public void resize()
	{
	}
	
	/**
	 * Draw the display (tiles and messages).
	 */
	public void repaint() {
		for (int i = 0; i < displayThings.length; i++)
		{
			for (int j = 0; j < displayThings[0].length; j++)
			{
				Thing t = board.thingAt(i, j);
				
				
				// If the tile is selected, change the background color
				Color background;
				if (selections[i][j])
				{
					background = t.getSelectionColor();
				}
				else
				{
					background = t.getBackgroundColor();
				}
				
				// Get the image from the resources
				if (!(t instanceof LetterThing))
				{
					String cardImageFileName = imageFileName(t);
					URL imageURL = getClass().getResource(cardImageFileName);
					if (imageURL != null) 
					{
						ImageIcon icon = new ImageIcon(imageURL);
						displayThings[i][j].setOpaque(true);
						displayThings[i][j].setBackground(background);
						displayThings[i][j].setIcon(icon);
						displayThings[i][j].setVisible(true);
					} 
					else 
					{
							throw new RuntimeException(
								"Image file not found: \"" + cardImageFileName + "\"");				
					}
				}
				else
				{
					LetterThing lt = (LetterThing) t;
					lt.setSize(board.getTileX());
					displayThings[i][j].setOpaque(true);
					displayThings[i][j].setBackground(background);
					displayThings[i][j].setIcon(lt.getIcon());
					displayThings[i][j].setVisible(true);
				}
			}
		}
		
		// Set the status and totals messages
		// By default, win and loss message are invisible
		statusMsg.setText(statusMsgText);
		statusMsg.setVisible(true);
		//totalsMsg.setText("You've won " + totalWins
		//	 + " out of " + totalGames + " games.");
		totalsMsg.setVisible(true);
		pack();
		panel.repaint();
	}

	/**
	 * Initialize the display.
	 */
	private void initDisplay(String applicationName)	{
		panel = new JPanel() {
			public void paintComponent(Graphics g) {
				super.paintComponent(g);
			}
		};

		// If no name was passed in, create one from the Board name
		if (0 == applicationName.length())
		{
			// If board object's class name follows the standard format
			// of ...Board or ...board, use the prefix for the JFrame title
			String className = board.getClass().getSimpleName();
			int classNameLen = className.length();
			int boardLen = "Board".length();
			String boardStr = className.substring(classNameLen - boardLen);
			if (boardStr.equals("Board") || boardStr.equals("board")) 
			{
				int titleLength = classNameLen - boardLen;
				applicationName = className.substring(0, titleLength);
			}
		}
			
		setTitle(applicationName);
		setSize();
		
		// Create a JLabel for all the tiles
		displayThings = new JLabel[rows][columns];
		
		// Add all the JLabels and add mouse listeners to each
		for (int i = 0; i < rows; i++)
		{
			for (int j = 0; j < columns; j++)
			{
				displayThings[i][j] = new JLabel();
				panel.add(displayThings[i][j]);
				displayThings[i][j].setBounds(thingCoords[i][j].x, thingCoords[i][j].y, 
						board.getTileX(), board.getTileY());
				displayThings[i][j].addMouseListener(new MyMouseListener());
				selections[i][j] = false;
			}
		}
		
		// Create the Start button and its listener
		startButton = new JButton();
		startButton.setText("Start");
		panel.add(startButton);
		startButton.setBounds(button_left, BUTTON_TOP, 100, 30);
		startButton.addActionListener(this);

		// Create the Stop button and its listener
		stopButton = new JButton();
		stopButton.setText("Stop");
		panel.add(stopButton);
		stopButton.setBounds(button_left, BUTTON_TOP + BUTTON_HEIGHT_INC, 100, 30);
		stopButton.addActionListener(this);
		stopButton.setEnabled(false);

		// Create the Step button and its listener
		stepButton = new JButton();
		stepButton.setText("Step");
		panel.add(stepButton);
		stepButton.setBounds(button_left, BUTTON_TOP + 2 * BUTTON_HEIGHT_INC, 100, 30);
		stepButton.addActionListener(this);

		// Create the Restart button and its listener
		restartButton = new JButton();
		restartButton.setText("Restart");
		panel.add(restartButton);
		restartButton.setBounds(button_left, BUTTON_TOP + 3 * BUTTON_HEIGHT_INC, 100, 30);
		restartButton.addActionListener(this);
		
		// Create the speed slider button and its listener
        speedSlider = new JSlider(MIN_DELAY_MSECS, MAX_DELAY_MSECS, INITIAL_DELAY);
        speedSlider.setPreferredSize(new Dimension(50, 100));
        speedSlider.setMaximumSize(speedSlider.getPreferredSize());
        speedSlider.setBounds(label_left, BUTTON_TOP + 4 * BUTTON_HEIGHT_INC, 175, 30);
        speedSlider.setInverted(true);

        panel.add(speedSlider);
        
        speedSlider.addChangeListener(new ChangeListener()
        {
            public void stateChanged(ChangeEvent evt)
            {
                timer.setDelay(((JSlider) evt.getSource()).getValue());
            }
        });
        		
        // Create the status message
		statusMsgText = "Items: ";
		statusMsg = new JLabel(statusMsgText);
		statusMsg.setFont(new Font("SansSerif", Font.BOLD, 13));
		panel.add(statusMsg);
		statusMsg.setBounds(label_left, BUTTON_TOP + 5 * BUTTON_HEIGHT_INC, 250, 30);

		// Create the win message and default it to green and invisible
		winMsg = new JLabel();
		winMsg.setBounds(label_left, 302, 200, 30);
		winMsg.setFont(new Font("SansSerif", Font.BOLD, 12));
		winMsg.setForeground(Color.BLACK);
		winMsg.setText("You win!");
		panel.add(winMsg);
		winMsg.setVisible(false);

		// Create the loss message and default it to red and invisible
		lossMsg = new JLabel();
		lossMsg.setBounds(label_left, 322, 200, 30);
		lossMsg.setFont(new Font("SanSerif", Font.BOLD, 12));
		lossMsg.setForeground(Color.BLACK);
		lossMsg.setText("Sorry, you lose.");
		panel.add(lossMsg);
		lossMsg.setVisible(false);

		// Create the totals message
		totalsMsg = new JLabel("You've won " + totalWins
			+ " out of " + totalGames + " games.");
		totalsMsg.setBounds(label_left, BUTTON_TOP + 8 * BUTTON_HEIGHT_INC,
								  250, 30);
		panel.add(totalsMsg);
		
		// Create a text field for entering commands
		textField = new JTextField();
		textField.addActionListener(this);
		textField.setBounds(LAYOUT_LEFT, rows * board.getTileY() + LAYOUT_TOP + TEXTFIELD_Y_BUFFER,
				label_left + 120, LAYOUT_TEXTFIELD);
		
		// Create the text console window
		textWindow = new JTextArea();
		textWindow.setEditable(false);
		textWindow.setVisible(true);
		
		// Add a scrollbar to the text console window
		JScrollPane scroll = new JScrollPane(textWindow);
	    scroll.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
	    scroll.setBounds(LAYOUT_LEFT, rows * board.getTileY() + LAYOUT_TOP + TEXTFIELD_Y_BUFFER + TEXTWINDOW_Y_BUFFER, 
	    		label_left + 120, LAYOUT_TEXTWINDOW);
	    new SmartScroller(scroll);
	    
	    panel.add(textField);
	    panel.add(scroll);
	    textWindow.append("Use Gui->appendTextWindow to write messages here.");
	    		
		pack();
		getContentPane().add(panel);
		getRootPane().setDefaultButton(startButton);
		panel.setVisible(true);
	}

	/**
	 * Set the size of the application based upon the tile size
	 */
	private void setSize()
	{
		// Adjust the JFrame height/width from the defaults
		// Minimum size will be two rows (for all the status messages)
		int numCardRows = (rows > 1) ? rows : 2;
		
		int height = DEFAULT_HEIGHT;
		int proposedHeight = numCardRows * board.getTileY() + LAYOUT_TOP + Y_BUFFER + LAYOUT_TEXTFIELD + LAYOUT_TEXTWINDOW;
		
		if (proposedHeight > height)
			height = proposedHeight;
		
		int width = DEFAULT_WIDTH;
		int proposedWidth = columns* board.getTileX() + BUTTON_SPACE + BUFFER + LAYOUT_LEFT + MESSAGE; 
		if (proposedWidth > width)
			width = proposedWidth;

		this.setSize(new Dimension(width, height));
		panel.setLayout(null);
		panel.setPreferredSize(new Dimension(width - 20, height - 20));
	}
	
	/**
	 * Deal with the user clicking on something other than a button or a tile.
	 */
	private void signalError() {
		Toolkit t = panel.getToolkit();
		t.beep();
	}

	/**
	 * Returns the image that corresponds to the input card.
	 * Image names have the format "[Name][Type].gif" 
	 * 
	 * IMPORTANT: Image file names should be in all lower case to maintain jar compatibility
	 *
	 * @param c <code>Thing</code> to get the image for
	 * @return String representation of the image
	 */
	private String imageFileName(Thing c) 
	{
		String str = "";
		if (c == null) {
			return fileName;
		}
		str += c.getImageFileName();
		return str.toLowerCase();
	}

	/**
	 * Attempt to determine the tile size based upon the first image encountered.
	 * If no images are encountered, the default tile size will be the same as blank.gif
	 * The tile size can be overridden by using <code>Board</code>->setTileSize()
	 * 
	 * IMPORTANT: Image file names should be in all lower case to maintain jar compatibility
	 *
	 * @param b <code>Board</code> for which the tile size should be determined
	 */
	private void determineSize(Board b)
	{
		String s;
		if (b.isEmpty())
		{
			s = fileName;
		}
		else
		{
			// Get the first tile
			List<Location> locs = b.thingIndexes();
			Location loc = locs.get(0);
			Thing t = b.thingAt(loc);
			
			if (t instanceof LetterThing)
			{
				s = fileName;
			}
			else
			{
				s = imageFileName(t);
			}
		}
		// Try to get the image for this from the resources
		URL imageURL = getClass().getResource(s);
		if (imageURL != null) 
		{
			ImageIcon icon = new ImageIcon(imageURL);
			b.setTileSize(icon.getIconWidth(), icon.getIconHeight());			
		} 
		else 
		{
			throw new RuntimeException(
				"Image not found: \"" + s + "\"");
		}
	}
	
	
	/**
	 * Respond to a button click (on any of the four buttons).
	 * @param e the button click action event
	 */
	public void actionPerformed(ActionEvent e) {
		if (e.getSource().equals(startButton))
		{
			//Places start message at the start of the game. 
			if(x == 0) {
				appendTextWindow("\nLink says: Where am I... What is this?");
				x++;
			}
			startButtonAction();
		}
		else if (e.getSource().equals(stopButton))
		{
			stopButtonAction();
		}
		else if (e.getSource().equals(stepButton))
		{
			stepButtonAction();
		}
		else if (e.getSource().equals(restartButton))
		{
			restartButtonAction();
		}
		else if (e.getSource().equals(textField))
		{
			String text = textField.getText();
			board.textInput(text,  false);
			textField.setText("");
		}
		else {
			signalError();
			return;
		}
	}

	/**
	 * Behavior of the Start button.
	 * Begin the timer and set other buttons accordingly
	 */
	public void startButtonAction()
	{
		timer.start();
		startButton.setEnabled(false);
		stopButton.setEnabled(true);
		stepButton.setEnabled(false);
		restartButton.setEnabled(false);
	}
	
	
	/**
	 * Behavior of the Stop button.
	 * Stop the timer and set other buttons accordingly
	 */
	public void stopButtonAction()
	{
		timer.stop();
		startButton.setEnabled(true);
		stopButton.setEnabled(false);
		stepButton.setEnabled(true);	
		restartButton.setEnabled(true);
	}
	
	
	/**
	 * Behavior of the Step button.
	 * Call the step() method one time only
	 */
	public void stepButtonAction()
	{
		step();
	}
	
	
	/**
	 * Behavior of the Restart button.
	 * Return the <code>Board</code> to the new game condition
	 */
	public void restartButtonAction()
	{
		board.newGame(false);
		setWinMessage("", false);
		setLossMessage("", false); 
		repaint();
	}
	
	
	/**
	 * Remove all tile selections
	 */
	private void setSelectionsFalse()
	{
		for (int i = 0; i < displayThings.length; i++)
		{
			for (int j = 0; i < displayThings[0].length; j++)
			{
				selections[i][j] = false;
			}
		}
	}
	
	
	/**
	 * Invert the selection on a <code>Location</code> on the <code>Board</code>
	 * Usually called in response to a mouse click
	 * @param loc The <code>Location</code> on the <code>Board</code> to select or deselect
	 */
	public void select(Location loc)
	{
		if (loc.isUnset())
		{
			setSelectionsFalse();
		}
		else
		{
			selections[loc.getRow()][loc.getColumn()] = !selections[loc.getRow()][loc.getColumn()];
		}
	}
	
	/**
	 * Display a win.
	 */
	@SuppressWarnings("unused")
	private void signalWin() {
		getRootPane().setDefaultButton(restartButton);
		winMsg.setVisible(true);
		totalWins++;
		totalGames++;
	}

	/**
	 * Display a loss.
	 */
	@SuppressWarnings("unused")
	private void signalLoss() {
		getRootPane().setDefaultButton(restartButton);
		lossMsg.setVisible(true);
		totalGames++;
	}
	
	
	/**
	 * Call the <code>Board</code>'s step() method exactly once.
	 */
	public void step()
	{
		board.step();
	}
	
	
	/**
	 * Set the status message (first of the four text labels on the right of the application window)
	 * @param s The <code>String</code> to set the label
	 */
	public void setStatusMessage(String s)
	{
		statusMsgText = s;
	}

	
	/**
	 * Set the loss message (second of the four text labels on the right of the application window)
	 * @param s The <code>String</code> to set the label
	 * @param visible Whether or not the win message should be shown
	 */
	public void setWinMessage(String s, boolean visible)
	{
		lossMsg.setText(s);
		lossMsg.setVisible(visible);
	}
	
	
	/**
	 * Set the loss message (third of the four text labels on the right of the application window)
	 * @param s The <code>String</code> to set the label
	 * @param visible Whether or not the lose message should be shown
	 */
	public void setLossMessage(String s, boolean visible)
	{
		winMsg.setText(s);
		winMsg.setVisible(visible);
	}
		
	
	/**
	 * Set the totals message (fourthd of the four text labels on the right of the application window)
	 * @param s The <code>String</code> to set the label
	 * @param visible Whether or not the totals message should be shown
	 */
	public void setTotalsMessage(String s, boolean visible)
	{
		totalsMsg.setText(s);
		totalsMsg.setVisible(visible);
	}
	
	
	/**
	 * Add a string to the console window below the game tiles
	 * @param s The <code>String</code> to add to the console window
	 */
	public void appendTextWindow(String s)
	{
		textWindow.append(s + "\n");
		if (!suppress)
		{
			System.out.println(s);
		}
	}
	
	/**
	 * Add a string to the console window below the game tiles
	 * @param s The <code>String</code> to add to the console window
	 */
	public String getTextWindow()
	{
		String s = textWindow.getText();
		return s;
	}
	
	
	/**
	 * Replace the contents of the console window below the game tiles
	 * @param s The <code>String</code> to show in the console window
	 */
	public void setTextWindow(String s)
	{
		textWindow.setText(s + "\n");
		if (!suppress)
		{
			System.out.println(s);
		}
	}
	
	public void setSuppress(boolean noText)
	{
		suppress = noText;
	}
	
	public void setInTest(boolean inTest)
	{
		this.inTest = inTest;
	}
	
	public boolean getInTest()
	{
		return inTest;
	}
	
	/**
	 * Receives and handles mouse clicks.  Other mouse events are ignored.
	 */
	private class MyMouseListener implements MouseListener {

		/**
		 * Handle a mouse click on a card by toggling its "selected" property.
		 * Each card is represented as a label.
		 * @param e the mouse event.
		 */
		public void mouseClicked(MouseEvent e) {
			for (int i = 0; i < rows; i++)
			{
				for (int j = 0; j < columns; j++)
				{
					if (e.getSource().equals(displayThings[i][j])
							&& board.thingAt(i,j) != null) 
					{
						if (e.getButton() == MouseEvent.BUTTON1)
						{
							board.thingAt(i,j).mouseClick();
							//Removed for Game Functions, used in tests *ONLY*.
							//statusMsgText = "You clicked (" + i + ", " + j + ")";
							board.mouseClick(e);
						}
						else if (e.getButton() == MouseEvent.BUTTON2)
						{
							//setWinMessage("You right clicked (" + i + ", " + j + ")", true);
							board.mouseClick(e);
						}
						else if (e.getButton() == MouseEvent.BUTTON3)
						{
							//setLossMessage("You right clicked (" + i + ", " + j + ")", true);
							board.mouseClick(e);
						}
						repaint();
						return;
					}
					
				}
			}
			signalError();
		}

		/**
		 * Ignore a mouse exited event.
		 * @param e the mouse event.
		 */
		public void mouseExited(MouseEvent e) {
		}

		/**
		 * Ignore a mouse released event.
		 * @param e the mouse event.
		 */
		public void mouseReleased(MouseEvent e) {
		}

		/**
		 * Ignore a mouse entered event.
		 * @param e the mouse event.
		 */
		public void mouseEntered(MouseEvent e) {
		}

		/**
		 * Ignore a mouse pressed event.
		 * @param e the mouse event.
		 */
		public void mousePressed(MouseEvent e) {
		}
	}
}
