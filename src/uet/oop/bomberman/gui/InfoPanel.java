package uet.oop.bomberman.gui;
import java.awt.event.ActionListener;
import uet.oop.bomberman.Board;
import uet.oop.bomberman.Game;

import javax.swing.*;


import javafx.event.*;

import java.awt.*;

/**
 * Swing Panel hiển thị thông tin thời gian, điểm mà người chơi đạt được
 */
public class InfoPanel extends JPanel {
	
	private static Board _board;
	private boolean gamePaused = false;
	private JLabel timeLabel;
	private JLabel pointsLabel;
	private JButton pauseButton;
	public InfoPanel(Game game) {
		setLayout(new GridLayout(1, 3));
		
		timeLabel = new JLabel("Time: " + game.getBoard().getTime());
		timeLabel.setForeground(Color.white);
		timeLabel.setHorizontalAlignment(JLabel.CENTER);
		
		pointsLabel = new JLabel("Points: " + game.getBoard().getPoints());
		pointsLabel.setForeground(Color.white);
		pointsLabel.setHorizontalAlignment(JLabel.CENTER);
		
		pauseButton = new JButton("Pause");
        pauseButton.setForeground(Color.white);
		pauseButton.setHorizontalAlignment(JButton.CENTER);
		add(timeLabel);
		add(pointsLabel);
		add(pauseButton);
		setBackground(Color.black);
		setPreferredSize(new Dimension(0, 40));
	}
	
	public void setTime(int t) {
		timeLabel.setText("Time: " + t);
	}

	public void setPoints(int t) {
		pointsLabel.setText("Score: " + t);
	}
	
}
