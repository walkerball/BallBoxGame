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
	public int resetxCoord=0;
	public int resetyCoord=0;
	public int clicked=0;
	public int sequence;
	public double flipimage = 0;
	public final static int MAX_LEVELS = 3;

	public final static int NUM_ROWS = 3;
	public final static int NUM_COLS = 3;
	public static final int MODE_ANIMATION = 0;
	public static final int MODE_CLICK = 1;
	public static final int MODE_CLICK_FINISH = 2;
	public static final int MODE_FINISHED_SEQUENCE = 3;
	public static final int MODE_STARTNEXT_SEQUENCE = 4;
	public static final int MODE_GAME_OVER = 5;



	public GridDemoFrame myParent;
	public int currentLevel;

	public Score theScore;

	public GridDemoPanel(GridDemoFrame parent)
	{
		super();
		currentLevel = 1;
		theScore = new Score(currentLevel,0);
		resetCells();
		xyValues = new ArrayList();
		sequence = 0;
		getSequence();
		mode = MODE_ANIMATION;
		theGrid[2][2].setDisplayMarker(true);
		xCoord = 0;
		yCoord = 0;
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

		theScore.resetScore();
	}

	public void resetGrid()
	{
		for (int r =0; r<NUM_ROWS; r++)
			for (int c=0; c<NUM_COLS; c++)
				theGrid[r][c].setColorID(1);;
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
			resetxCoord = row;
			resetyCoord = col;

			System.out.println(theGrid[row][col].getColorID() + ": ("+row+", "+col+") - z:" + sequence + "xyValues.size():" + xyValues.size());
			if (!theGrid[row][col].isLive()) {
				System.out.println("not Live?");

				return;
			}

			Coords firefox = xyValues.get(sequence);
			if(sequence<xyValues.size()){
				// check to see if we hit the square in right order
				if (firefox.getX() == row && firefox.getY() == col ){
					sequence++;
					theScore.addPoints(1);
					myParent.updateScore(theScore.getLevel(), theScore.getPoints(), (mode == MODE_GAME_OVER));
				}
				else {
					System.out.println("Game Over");
					System.out.println(firefox.getX() + " " + firefox.getY());
					mode = MODE_GAME_OVER;
					myParent.updateScore(theScore.getLevel(), theScore.getPoints(), (mode == MODE_GAME_OVER));

				}
			}

			if(sequence>xyValues.size()-1){
				theGrid[row][col].cycleColorIDForward();
				//	clicked = 1;
				repaint();

				sequence = 0;
				flipimage = 0;
				currentLevel++;
				theScore.addLevel(1);
				resetGrid();
				getSequence();
				mode = MODE_FINISHED_SEQUENCE;
				repaint();
				return;
			}

			theGrid[row][col].cycleColorIDForward();
			clicked = 1;
			repaint();
		}
		else{
			return;
		}
	}

	public ArrayList<Coords> getSequence(){
		Random rand = new Random();
		int i=0;
		System.out.println("sequence: xyValues.size:"+xyValues.size() +"  theScore.getLevel():"+theScore.getLevel());

		while (xyValues.size() < theScore.getLevel()){
			xyValues.add(new Coords(rand.nextInt(3), rand.nextInt(3)));
			System.out.println("sequence: ("+xyValues.get(i).getX()+", "+xyValues.get(i).getY()+") - i:" + i);
			System.out.println("  sequence: xyValues.size:"+xyValues.size() +"  theScore.getLevel():"+theScore.getLevel());

			i++;
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
			if (flipimage == theScore.getLevel()) {
				mode = MODE_CLICK;
				System.out.println("MODE changed to CLICK");

			} else {
				Coords currentSquare = xyValues.get((int) flipimage);
				System.out.println(flipimage);
				xCoord = currentSquare.getX();
				yCoord = currentSquare.getY();
				theGrid[xCoord][yCoord].cycleColorIDBackward();
				flipimage += .5;
				repaint();
			}
		}
		else if (mode == MODE_CLICK)
		{
			if (clicked == 1) {
				mode = MODE_CLICK_FINISH;
				System.out.println("MODE changed to CLICK FINISH");

			}

			return;
		}
		else if (mode == MODE_CLICK_FINISH)
		{
			//resetGrid();
			theGrid[resetxCoord][resetyCoord].cycleColorIDBackward();
			clicked = 0;
			repaint();
			mode = MODE_CLICK;
			System.out.println("MODE changed to CLICK");

			return;
		}
		else if (mode == MODE_FINISHED_SEQUENCE)
		{
			System.out.println("MODE changed to MODE_FINISHED_SEQUENCE");

			theGrid[resetxCoord][resetyCoord].cycleColorIDBackward();
			repaint();
			mode = MODE_STARTNEXT_SEQUENCE;

			return;
		}
		else if (mode == MODE_STARTNEXT_SEQUENCE)
		{
			System.out.println("MODE changed to MODE_STARTNEXT_SEQUENCE");

			theGrid[resetxCoord][resetyCoord].cycleColorIDBackward();
			repaint();
			mode = MODE_ANIMATION;
			System.out.println("MODE changed to MODE_ANIMATION");

			return;
		}
		else if (mode == MODE_GAME_OVER)
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
