package uet.oop.bomberman.gui;
import uet.oop.bomberman.Board;
import uet.oop.bomberman.Game;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

/**
 * Swing Panel hiển thị thông tin thời gian, điểm mà người chơi đạt được
 */
public class InfoPanel extends JPanel {
	private static Board _board;
	private boolean gamePaused = false;
	private JLabel timeLabel;
	private JLabel pointsLabel;
	private JButton pauseButton;
	private Game game;
	public InfoPanel(Game game) {
		setLayout(new GridLayout(1, 3));
		this.game = game;
		timeLabel = new JLabel("Time: " + game.getBoard().getTime());
		timeLabel.setForeground(Color.white);
		timeLabel.setHorizontalAlignment(JLabel.CENTER);
		
		pointsLabel = new JLabel("Points: " + game.getBoard().getPoints());
		pointsLabel.setForeground(Color.white);
		pointsLabel.setHorizontalAlignment(JLabel.CENTER);
		
		pauseButton = new JButton("Pause");
        pauseButton.setForeground(Color.WHITE);
		pauseButton.setHorizontalAlignment(JButton.CENTER);
		pauseButton.setBackground(Color.BLACK);
		pauseButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				game.pause();
				gamePaused = !gamePaused;
				pauseButton.setText(gamePaused? "Resume" : "Pause");
			}
		});
	
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
