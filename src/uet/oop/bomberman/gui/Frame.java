package uet.oop.bomberman.gui;

import uet.oop.bomberman.Game;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;

/**
 * Swing Frame chứa toàn bộ các component
 */
public class Frame extends JFrame {
	
	public GamePanel _gamepane;
	private JPanel _containerpane;
	private InfoPanel _infopanel;
	
	private Game _game;

	public Frame() {

        try {
            Font f = Font.createFont(Font.TRUETYPE_FONT, new File("res/font/Minecraft.ttf"));
			GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
			ge.registerFont(f);
        } catch (FontFormatException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        _containerpane = new JPanel(new BorderLayout());
		_gamepane = new GamePanel(this);
		_infopanel = new InfoPanel(_gamepane.getGame());
		
		_containerpane.add(_infopanel, BorderLayout.PAGE_START);
		_containerpane.add(_gamepane, BorderLayout.PAGE_END);
		
		_game = _gamepane.getGame();
		
		add(_containerpane);
		
		setResizable(false);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		pack();
		setLocationRelativeTo(null);
	}

	public void start() {
		_game.start();
	}
	
	public void setTime(int time) {
		_infopanel.setTime(time);
	}
	
	public void setPoints(int points) {
		_infopanel.setPoints(points);
	}

	public void setLevel(int level) {
		_infopanel.setLevel(level);
	}

	public void setEnemy(int enemy) {
		_infopanel.setEnemies(enemy);
	}

	public void setHideInfoPanel() {
		_infopanel.hideInfoPanel();
	}

	public void loadInfo() {
		_infopanel.loadInfo();
	}

	public void renderItemTime() {
		_infopanel.renderItemTime();
	}
	
}
