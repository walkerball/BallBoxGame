import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.Random;

public class GridDemoPanel extends JPanel implements MouseListener, KeyListener
{
	private Cell[][] theGrid;
	private ArrayList<Coords> xyValues;
	private int mode;
	public int xCoord;
	public int yCoord;
	public int z;
	public double j = 0;
	public final static int NUM_ROWS = 3;
	public final static int NUM_COLS = 3;
	public static final int MODE_ANIMATION = 0;
	public static final int MODE_CLICK = 1;
	public GridDemoFrame myParent;
	public int score;
	public int currentLevel;
	
	public GridDemoPanel(GridDemoFrame parent)
	{
		super();
		resetCells();
		xyValues = new ArrayList();
		z = 0;
		currentLevel = 1;
		getSequence();
		mode = MODE_ANIMATION;
		theGrid[2][2].setDisplayMarker(true);
		theGrid[xCoord][yCoord].setIsLive(true);
		setBackground(Color.BLACK);
		addMouseListener(this);
		//parent.addKeyListener(this); // activate this if you wish to listen to the keyboard.
		//test
		myParent = parent;
	}	
	
	/**
	 * makes a new board with random colors, completely filled in, and resets the score to zero.
	 */

	public void resetCells()
	{
		theGrid = new Cell[NUM_ROWS][NUM_COLS];
		for (int r =0; r<NUM_ROWS; r++)
			for (int c=0; c<NUM_COLS; c++)
				theGrid[r][c] = new Cell(r,c);
		score = 0;
	}
	
	public void paintComponent(Graphics g)
	{
		super.paintComponent(g);
		for (int r =0; r<NUM_ROWS; r++)
			for (int c=0; c<NUM_COLS; c++)
				theGrid[r][c].drawSelf(g);
	}
	
	/**
	 * the mouse listener has detected a click, and it has happened on the cell in theGrid at row, col
	 * @param row
	 * @param col
	 */

	public void userClickedCell(int row, int col)
	{
		if(mode == MODE_CLICK){
			System.out.println("("+row+", "+col+")");
			if (!theGrid[row][col].isLive())
				return;
			score += theGrid[row][col].getColorID();
			myParent.updateScore(score);
			Coords firefox = xyValues.get(z);
			if(z<xyValues.size()){
				if (firefox.getX() == row && firefox.getY() == col ){
					z++;
				}
				else {
					System.out.println("Game Over");
					System.out.println(firefox.getX() + " " + firefox.getY());
				}
			}
			else{
				z = 0;
				currentLevel++;
				mode = MODE_ANIMATION;
			}
			theGrid[row][col].cycleColorIDForward();
			repaint();
		}
		else{
			return;
		}
	}
	
	public ArrayList<Coords> getSequence(){
		Random rand = new Random();
		while (xyValues.size() < currentLevel){
			xyValues.add(new Coords(rand.nextInt(3), rand.nextInt(3)));
		}
		return xyValues;
	}
	
	
	/**
	 * Here's an example of a simple dialog box with a message.
	 */
	public void makeGameOverDialog()
	{
		JOptionPane.showMessageDialog(this, "Game Over.");
		
	}
	
	//============================ Mouse Listener Overrides ==========================

	@Override
	// mouse was just released within about 1 pixel of where it was pressed.
	// NOTE: this is actually kind of obnoxious because if the mouse moved much at all between press
	// and release, it won't register as a click. You may be happier with mouseReleased, instead.
	public void mouseClicked(MouseEvent e)
	{
		// TODO Auto-generated method stub
		// mouse location is at e.getX() , e.getY().
		// if you wish to convert to the rows and columns, you can integer-divide by the cell size.
		int col = e.getX()/Cell.CELL_SIZE;
		int row = e.getY()/Cell.CELL_SIZE;
		userClickedCell(row,col);
		
		
	}

	@Override
	public void mousePressed(MouseEvent e)
	{
		// TODO Auto-generated method stub
		// mouse location is at e.getX() , e.getY().
		// if you wish to convert to the rows and columns, you can integer-divide by the cell size.
				
	}

	@Override
	public void mouseReleased(MouseEvent e)
	{
		// TODO Auto-generated method stub
		// mouse location is at e.getX() , e.getY().
		// if you wish to convert to the rows and columns, you can integer-divide by the cell size.
		
	}

	@Override
	// mouse just entered this window
	public void mouseEntered(MouseEvent e)
	{
		// TODO Auto-generated method stub
		
	}

	@Override
	// mouse just left this window
	public void mouseExited(MouseEvent e)
	{
		// TODO Auto-generated method stub
		
	}
	//============================ Key Listener Overrides ==========================

	@Override
	/**
	 * user just pressed and released a key. (May also be triggered by autorepeat, if key is held down?
	 * @param e
	 */
	public void keyTyped(KeyEvent e)
	{
		char whichKey = e.getKeyChar();
		myParent.updateMessage("User just typed \""+whichKey+"\"" );
		System.out.println(whichKey);
	}

	@Override
	public void keyPressed(KeyEvent e)
	{
		// TODO Auto-generated method stub
		
	}

	@Override
	public void keyReleased(KeyEvent e)
	{
		// TODO Auto-generated method stub
		
	}
	// ============================= animation stuff ======================================
	/**
	 * if you wish to have animation, you need to call this method from the GridDemoFrame AFTER you set the window visibility.
	 */
	public void initiateAnimationLoop()
	{
		Thread aniThread = new Thread( new AnimationThread(500)); // the number here is the number of milliseconds between steps.
		aniThread.start();
	}
	
	/**
	 * Modify this method to do what you want to have happen periodically.
	 * This method will be called on a regular basis, determined by the delay set in the thread.
	 * Note: By default, this will NOT get called unless you uncomment the code in the GridDemoFrame's constructor
	 * that creates a thread.
	 *
	 */
	public void animationStep(long millisecondsSinceLastStep)
	{
		if(mode == MODE_ANIMATION) {
			if (j == currentLevel) {
				mode = MODE_CLICK;
			} else {
				if (j != (int) j) {
					Coords currentSquare = xyValues.get((int) j);
					System.out.println(j);
					xCoord = currentSquare.getX();
					yCoord = currentSquare.getY();
					theGrid[xCoord][yCoord].cycleColorIDBackward();
					j += .5;
					repaint();
				} else {
					Coords currentSquare = xyValues.get((int) j);
					System.out.println(j);
					xCoord = currentSquare.getX();
					yCoord = currentSquare.getY();
					theGrid[xCoord][yCoord].cycleColorIDBackward();
					j += .5;
					repaint();
				}
			}
		}
		else
		{
			return;
		}
	}
	// ------------------------------ animation thread - internal class -------------------
	public class AnimationThread implements Runnable
	{
		long start;
		long timestep;
		public AnimationThread(long t)
		{
			timestep = t;
			start = System.currentTimeMillis();
		}
		@Override
		public void run()
		{
			long difference;
			while (true)
			{
				difference = System.currentTimeMillis() - start;
				if (difference >= timestep)
				{
					animationStep(difference);
					start = System.currentTimeMillis();
				}
				try
				{	Thread.sleep(1);
				}
				catch (InterruptedException iExp)
				{
					System.out.println(iExp.getMessage());
					break;
				}
			}
			
		}
		
	}
}
