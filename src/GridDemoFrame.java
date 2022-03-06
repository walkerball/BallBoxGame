import java.awt.BorderLayout;

import javax.swing.*;

public class GridDemoFrame extends JFrame {
	GridDemoPanel thePanel;
	JLabel scoreLabel, messageLabel;
	public GridDemoFrame() {
		super("Grid Demo");
		
		setSize(600,600+24+16);
		
		this.getContentPane().setLayout(new BorderLayout());
		thePanel = new GridDemoPanel(this);
		scoreLabel = new JLabel("Level: 1 Score: 0");
		messageLabel = new JLabel("");
		Box southPanel = Box.createHorizontalBox();
		
		this.getContentPane().add(thePanel,BorderLayout.CENTER);
		this.getContentPane().add(southPanel, BorderLayout.SOUTH);
		southPanel.add(Box.createHorizontalStrut(10));
		southPanel.add(scoreLabel, BorderLayout.SOUTH);
		southPanel.add(Box.createGlue());
		southPanel.add(messageLabel, BorderLayout.SOUTH);
		southPanel.add(Box.createHorizontalStrut(10));
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setVisible(true);	
		thePanel.initiateAnimationLoop(); // uncomment this line if your program uses animation.
	}
	
	public void updateMessage(String message) {
		messageLabel.setText(message);
		messageLabel.repaint();
	}

	public void updateScore(int level, int score, boolean mode) {
		if (mode == false)
			scoreLabel.setText("Level: " + level + " Score: "+ score);
		else
			scoreLabel.setText("Level: " + level + " Score: "+ score + "   GAME OVER");

		scoreLabel.repaint();
	}
}
